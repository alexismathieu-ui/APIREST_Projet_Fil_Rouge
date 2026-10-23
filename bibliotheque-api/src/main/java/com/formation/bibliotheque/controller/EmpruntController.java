package com.formation.bibliotheque.controller;

import com.formation.bibliotheque.dto.EmpruntRequest;
import com.formation.bibliotheque.dto.EmpruntResponse;
import com.formation.bibliotheque.mapper.EmpruntMapper;
import com.formation.bibliotheque.model.Emprunt;
import com.formation.bibliotheque.service.EmpruntService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/emprunts")
@Tag(name = "Emprunts", description = "Emprunts et retours des livres")
public class EmpruntController {

    private final EmpruntService empruntService;

    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
    }

    @Operation(summary = "Lister tous les emprunts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste de tous les emprunts récupérée avec succès")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EmpruntResponse>> listerTous(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererTous(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }

    @Operation(summary = "Obtenir un emprunt par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Emprunt trouvé"),
            @ApiResponse(responseCode = "404", description = "Emprunt inexistant")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmpruntResponse> obtenirParId(@PathVariable Long id) {

        Optional<Emprunt> resultat = empruntService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EmpruntMapper.versResponse(resultat.get()));
    }

    @Operation(summary = "Créer un emprunt")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Emprunt créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou livre inexistant"),
            @ApiResponse(responseCode = "409", description = "Livre non disponible (stock insuffisant)")
    })
    @PostMapping
    public ResponseEntity<EmpruntResponse> creer(@Valid @RequestBody EmpruntRequest request) {

        if (!empruntService.livreExiste(request.getLivreId())) {
            return ResponseEntity.badRequest().build();
        }

        if (!empruntService.livreDisponible(request.getLivreId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // 2. Agir
        Emprunt empruntACreer = EmpruntMapper.versEntite(request);
        Emprunt empruntCree = empruntService.creer(empruntACreer);

        // 3. Répondre
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EmpruntMapper.versResponse(empruntCree));
    }

    @Operation(summary = "Enregistrer le retour d'un livre emprunté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre retourné avec succès"),
            @ApiResponse(responseCode = "404", description = "Emprunt inexistant"),
            @ApiResponse(responseCode = "409", description = "L'emprunt a déjà été rendu")
    })
    @PatchMapping("/{id}/retour")
    public ResponseEntity<EmpruntResponse> retourner(@PathVariable Long id) {
        // 1. Vérifier :
        // a) L'emprunt existe-t-il ? (404 si inexistant)
        Optional<Emprunt> resultat = empruntService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        Emprunt emprunt = resultat.get();
        if (Boolean.TRUE.equals(emprunt.getRendu())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Emprunt empruntRendu = empruntService.retourner(emprunt);

        return ResponseEntity.ok(EmpruntMapper.versResponse(empruntRendu));
    }

    @Operation(summary = "Lister les emprunts en cours")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des emprunts en cours récupérée avec succès")
    })
    @GetMapping("/en-cours")
    public ResponseEntity<PagedModel<EmpruntResponse>> listerEnCours(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererEnCours(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }

    @Operation(summary = "Lister les emprunts en retard")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des emprunts en retard récupérée avec succès")
    })
    @GetMapping("/en-retard")
    public ResponseEntity<PagedModel<EmpruntResponse>> listerEnRetard(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererEnRetard(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }
}