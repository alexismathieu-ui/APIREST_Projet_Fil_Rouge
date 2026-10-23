package com.formation.bibliotheque.mapper;

import com.formation.bibliotheque.dto.EmpruntRequest;
import com.formation.bibliotheque.dto.EmpruntResponse;
import com.formation.bibliotheque.model.Emprunt;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class EmpruntMapper {

    private EmpruntMapper() {
    }

    public static Emprunt versEntite(EmpruntRequest request) {
        Emprunt emprunt = new Emprunt();
        emprunt.setEmprunteur(request.getEmprunteur());
        emprunt.setEmailEmprunteur(request.getEmailEmprunteur());
        return emprunt;
    }

    public static EmpruntResponse versResponse(Emprunt emprunt) {

        boolean enRetard = Boolean.FALSE.equals(emprunt.getRendu())
                && emprunt.getDateRetourPrevue() != null
                && emprunt.getDateRetourPrevue().isBefore(LocalDate.now());

        return new EmpruntResponse(
                emprunt.getId(),
                emprunt.getLivreId(),
                emprunt.getEmprunteur(),
                emprunt.getDateEmprunt(),
                emprunt.getDateRetourPrevue(),
                emprunt.getDateRetourEffective(),
                emprunt.getRendu(),
                enRetard
        );
    }

    public static List<EmpruntResponse> versResponses(List<Emprunt> emprunts) {
        List<EmpruntResponse> resultat = new ArrayList<>();
        for (Emprunt emprunt : emprunts) {
            resultat.add(versResponse(emprunt));
        }
        return resultat;
    }

    public static PagedModel<EmpruntResponse> versPage(Page<Emprunt> page) {
        return new PagedModel<>(page.map(EmpruntMapper::versResponse));
    }
}