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
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auteurs")
public class AuteurController {
    private AuteurService auteurService;

    public AuteurController(AuteurService auteurService){
        this.auteurService = auteurService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<AuteurResponse>> listerTous(@PageableDefault(size = 10, sort = "nom") Pageable pageable) {
        Page<Auteur> page = auteurService.recupererTous(pageable);
        return ResponseEntity.ok(AuteurMapper.versPage(page)); // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuteurResponse> getById(@PathVariable Long id){
        Optional<Auteur> resultat = auteurService.recupererParId(id);
        if (resultat.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(AuteurMapper.versResponse(resultat.get()));
    }

    @PostMapping
    public ResponseEntity<AuteurResponse> creer(@Valid @RequestBody AuteurRequest request) {
        Auteur auteurACreer = AuteurMapper.versEntite(request);
        Auteur auteurCreer = auteurService.creer(auteurACreer);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuteurMapper.versResponse(auteurCreer));
    }

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
