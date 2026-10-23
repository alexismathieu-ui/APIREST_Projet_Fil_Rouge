package com.formation.bibliotheque.controller;

import com.formation.bibliotheque.dto.LivreRequest;
import com.formation.bibliotheque.dto.LivreResponse;
import com.formation.bibliotheque.mapper.LivreMapper;
import com.formation.bibliotheque.model.Livre;
import com.formation.bibliotheque.service.LivreService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    @GetMapping
    public PagedModel<LivreResponse> listerTous(
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        return LivreMapper.versPage(livreService.recupererTous(pageable));
    }

    @GetMapping("/{id}")
    public LivreResponse obtenirParId(@PathVariable Long id) {
        return LivreMapper.versResponse(trouverOu404(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivreResponse creer(@Valid @RequestBody LivreRequest request) {
        verifierAuteur(request.getAuteurId());
        if (livreService.isbnExiste(request.getIsbn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ISBN déjà utilisé");
        }
        Livre livre = LivreMapper.versEntite(request);
        return LivreMapper.versResponse(livreService.creer(livre));
    }

    @PutMapping("/{id}")
    public LivreResponse mettreAJour(@PathVariable Long id,
                                     @Valid @RequestBody LivreRequest request) {
        Livre existant = trouverOu404(id);
        verifierAuteur(request.getAuteurId());
        if (livreService.isbnUtiliseParUnAutreLivre(request.getIsbn(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ISBN déjà utilisé par un autre livre");
        }
        Livre nouvellesValeurs = LivreMapper.versEntite(request);
        return LivreMapper.versResponse(livreService.mettreAJour(existant, nouvellesValeurs));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        if (!livreService.existe(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livre introuvable : " + id);
        }
        livreService.supprimer(id);
    }

    @GetMapping("/recherche")
    public PagedModel<LivreResponse> rechercher(
            @RequestParam String titre,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        return LivreMapper.versPage(livreService.rechercherParTitre(titre, pageable));
    }

    @GetMapping("/disponibles")
    public PagedModel<LivreResponse> listerDisponibles(
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        return LivreMapper.versPage(livreService.recupererDisponibles(pageable));
    }

    @PatchMapping("/{id}/stock")
    public LivreResponse ajouterStock(@PathVariable Long id,
                                      @RequestParam Integer quantite) {
        if (quantite <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité doit être positive");
        }
        Livre livre = trouverOu404(id);
        return LivreMapper.versResponse(livreService.ajouterStock(livre, quantite));
    }

    private Livre trouverOu404(Long id) {
        return livreService.recupererParId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Livre introuvable : " + id));
    }

    private void verifierAuteur(Long auteurId) {
        if (!livreService.auteurExiste(auteurId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auteur inexistant : " + auteurId);
        }
    }
}