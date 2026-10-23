package com.formation.bibliotheque.mapper;

import com.formation.bibliotheque.dto.LivreRequest;
import com.formation.bibliotheque.dto.LivreResponse;
import com.formation.bibliotheque.model.Livre;
import com.formation.bibliotheque.model.Livre;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

import java.util.ArrayList;
import java.util.List;

public final class LivreMapper {

    private LivreMapper() {
    }

    public static Livre versEntite(LivreRequest request) {
        Livre livre = new Livre();
        livre.setTitre(request.getTitre());
        livre.setIsbn(request.getIsbn());
        livre.setPrix(request.getPrix());
        livre.setAnneePublication(request.getAnneePublication());
        livre.setStock(request.getStock());
        return livre;
    }

    public static LivreResponse versResponse(Livre livre) {
        return new LivreResponse(
                livre.getId(),
                livre.getTitre(),
                livre.getIsbn(),
                livre.getPrix(),
                livre.getAnneePublication(),
                livre.getStock(),
                livre.getAuteurId()
        );
    }

    public static List<LivreResponse> versResponses(List<Livre> livres) {
        List<LivreResponse> resultat = new ArrayList<>();
        for (Livre livre : livres) {
            resultat.add(versResponse(livre));
        }
        return resultat;
    }

    public static PagedModel<LivreResponse> versPage(Page<Livre> page) {
        return new PagedModel<>(page.map(LivreMapper::versResponse));
    }
}