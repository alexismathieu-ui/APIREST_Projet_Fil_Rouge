package com.formation.bibliotheque.controller;

import com.formation.bibliotheque.dto.AuteurRequest;
import com.formation.bibliotheque.dto.AuteurResponse;
import com.formation.bibliotheque.mapper.AuteurMapper;
import com.formation.bibliotheque.model.Auteur;
import com.formation.bibliotheque.service.AuteurService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auteurs")
@Tag(name = "Auteurs", description = "Gestion des auteurs")
public class AuteurController {
    private AuteurService auteurService;

    public AuteurController(AuteurService auteurService){
        this.auteurService = auteurService;
    }

    @Operation(summary = "Lister tous les auteurs")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des auteurs récupérée avec succès")
    })
    @GetMapping
    public ResponseEntity<PagedModel<AuteurResponse>> listerTous(@PageableDefault(size = 10, sort = "nom") Pageable pageable) {
        Page<Auteur> page = auteurService.recupererTous(pageable);
        return ResponseEntity.ok(AuteurMapper.versPage(page)); // 200
    }

    @Operation(summary = "Obtenir un auteur par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Auteur trouvé"),
            @ApiResponse(responseCode = "404", description = "Auteur inexistant")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuteurResponse> getById(@PathVariable Long id){
        Optional<Auteur> resultat = auteurService.recupererParId(id);
        if (resultat.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(AuteurMapper.versResponse(resultat.get()));
    }

    @Operation(summary = "Créer un auteur")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Auteur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données de la requête invalides")
    })
    @PostMapping
    public ResponseEntity<AuteurResponse> creer(@Valid @RequestBody AuteurRequest request) {
        Auteur auteurACreer = AuteurMapper.versEntite(request);
        Auteur auteurCreer = auteurService.creer(auteurACreer);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuteurMapper.versResponse(auteurCreer));
    }

    @Operation(summary = "Modifier un auteur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Auteur mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Auteur inexistant")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuteurResponse> modifier(@PathVariable Long id, @Valid @RequestBody AuteurRequest request){
        Optional<Auteur> resultat = auteurService.recupererParId(id);
        if (resultat.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Auteur auteurExistant =  resultat.get();
        Auteur nouvelleValeurs = AuteurMapper.versEntite(request);
        Auteur auteurMiseAJour = auteurService.mettreAJour(auteurExistant, nouvelleValeurs);

        return ResponseEntity.ok(AuteurMapper.versResponse(auteurMiseAJour));
    }

    @Operation(summary = "Supprimer un auteur (impossible s'il a encore des livres)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Auteur supprimé"),
            @ApiResponse(responseCode = "404", description = "Auteur inexistant"),
            @ApiResponse(responseCode = "409", description = "L'auteur a encore des livres")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!auteurService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        if (auteurService.aDesLivres(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        auteurService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lister les livres d'un auteur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des livres de l'auteur récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Auteur inexistant")
    })
    @GetMapping("/{id}/livres")
    public ResponseEntity<org.springframework.data.web.PagedModel<com.formation.bibliotheque.dto.LivreResponse>> listerLivres(
            @PathVariable Long id,
            @org.springframework.data.web.PageableDefault(size = 10, sort = "titre") org.springframework.data.domain.Pageable pageable) {
        if (!auteurService.existe(id)) {
            return ResponseEntity.notFound().build(); // 404
        }
        org.springframework.data.domain.Page<com.formation.bibliotheque.model.Livre> page = auteurService.recupererLivres(id, pageable);
        return ResponseEntity.ok(com.formation.bibliotheque.mapper.LivreMapper.versPage(page)); // 200
    }
}
