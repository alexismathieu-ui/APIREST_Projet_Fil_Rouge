package com.formation.bibliotheque.repository;

import com.formation.bibliotheque.model.Emprunt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    Page<Emprunt> findByRenduFalse(Pageable pageable); // emprunts non rendus

    Page<Emprunt> findByRenduFalseAndDateRetourPrevueBefore(LocalDate date, Pageable pageable);}