package com.formation.bibliotheque.repository;

import com.formation.bibliotheque.model.Auteur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuteurRepository extends JpaRepository<Auteur, Long> {
}