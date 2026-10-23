package com.formation.bibliotheque.controller;

import com.formation.bibliotheque.dto.LivreRequest;
import com.formation.bibliotheque.dto.LivreResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    @GetMapping
    public PagedModel<LivreResponse> listerTous(
        return LivreMapper.versPage(livreService.listerTous(pageable));
    )
    @GetMapping("/{id}")
    public LivreResponse obtenirParId(@PathVariable Long id){
        return LivreMapper.versResponse(livreService.obtenirParId(id));
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivreResponse creer(@Valid @RequestBody LivreRequest request){
        return LivreMapper.versResponse(livreService.creer(request));
    }
    @PutMapping("/{id}")
    public LivreResponse mettreAJour(@PathVariable long id,
                                     @Valid @RequestBody LivreRequest request){
        return LivreMapper.versResponse(livreService.mettreAJour(id, request));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id){
        livreService.supprimer(id);
    }
    @GetMapping("/recherche")
    public PagedModel<LivreResponse> rechercher(
            @RequestParam String titre,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable){
        return LivreMapper.versPage(livreService.rechercher(titre, pageable));
    }
    @GetMapping("/disponibles")
    public LivreResponse listerDisponibles(@PageableDefault(size = 10, sort = "titre") Pageable pageable){
        return LivreMapper.versPage(livreService.listerDisponibles(pageable));
    }
    @PatchMapping("/{id}/stock")
    public LivreResponse ajouterStock(@PathVariable Long id,
                                      @RequestParam Integer quantite){
        return LivreMapper.versResponse(livreService.ajouterStock(id, quantite));
    }
}
