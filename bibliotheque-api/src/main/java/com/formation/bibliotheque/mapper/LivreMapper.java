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
        livre.setNom(request.getNom());
        return livre;
    }

    public static LivreResponse versResponse(Livre livre) {
        return new LivreResponse(
                livre.getId(),
                livre.getNom(),
        );
    }

    public static List<LivreResponse> versResponses(List<Livre> livres) {
        List<LivreResponse> resultat = new ArrayList<>();
        for (Livre livre livres : livres) {
            resultat.add(versResponse(livre));
        }
        return resultat;
    }

    public static PagedModel<LivreResponse> versPage(Page<Livre> page) {
        return new PagedModel<>(page.map(LivreMapper::versResponse));
    }
}