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

    //renvoie tout les auteurs
    public Page<Auteur> recupererTous(Pageable pageable) {
        return auteurRepository.findAll(pageable);
    }

    //retourne un auteur par son Id ou retourne
    public Optional<Auteur> recupererParId(Long id) {
        return auteurRepository.findById(id);
    }

    //
    public boolean existe(Long id) {
        if (auteurRepository.existsById(id)) {
            return true;
        }
        else {
            return false;
        }
    }

    public Auteur creer(Auteur auteur) {
        auteur.setId(null);
        return auteurRepository.save(auteur);
    }

    public Auteur mettreAJour(Auteur auteurExistant, Auteur nouvellesValeurs) {
        auteurExistant.setNom(nouvellesValeurs.getNom());
        auteurExistant.setPrenom(nouvellesValeurs.getPrenom());
        auteurExistant.setNationalite(nouvellesValeurs.getNationalite());
        return auteurRepository.save(auteurExistant);
    }

    public boolean aDesLivres(Long id) {
        return livreRepository.existsByAuteurId(id);
    }

    public void supprimer(Long id) {
        if (auteurRepository.existsById(id)) {
            auteurRepository.deleteById(id);
        }

    }
}
