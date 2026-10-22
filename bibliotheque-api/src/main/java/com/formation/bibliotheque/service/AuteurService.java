package com.formation.bibliotheque.service;

import com.formation.bibliotheque.model.Auteur;
import com.formation.bibliotheque.repository.AuteurRepository;
import com.formation.bibliotheque.repository.LivreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class AuteurService {

    private final AuteurRepository auteurRepository;
    private final LivreRepository livreRepository;

    public AuteurService(AuteurRepository auteurRepository, LivreRepository livreRepository) {
        this.auteurRepository = auteurRepository;
        this.livreRepository = livreRepository;
    }

    // Écrivez ici les méthodes du tableau

    //renvoie tout les auteurs
    public Page<Auteur> recupererTous(Pageable pageable) {
        return AuteurRepository.findAll();
    }

    //retourne un auteur par son Id ou retourne
    public Optional<Auteur> recupererParId(Long id) {
        return AuteurRepository.findById();
    }

    //
    public boolean existe(Long id) {
        if (AuteurRepository.existsById(id)) {
            return true;
        }
        else {
            return false;
        }
    }

    public Auteur creer(Auteur auteur) {
        auteur.setId(null);
        return AuteurRepository.save();
    }

    public Auteur mettreAJour(Auteur auteurExistant, Auteur nouvellesValeurs) {
        return AuteurRepository.findById(id)
                .map(produitExistant -> {
                    produitExistant.setNom(nouvellesValeurs.getNom());
                    produitExistant.setDescription(nouvellesValeurs.getDescription());
                    produitExistant.setPrix(nouvellesValeurs.getPrix());
                    produitExistant.setStock(nouvellesValeurs.getStock());
                    return AuteurRepository.save(produitExistant);
                });
    }

    public boolean aDesLivres(Long id) {
        return true;
    }

    public void supprimer(Long id) {
        if (AuteurRepository.existsById(id)) {
            AuteurRepository.deleteById(id);
        }

    }
}
