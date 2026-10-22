public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    Page<Emprunt> findByRenduFalse(Pageable pageable); // emprunts non rendus
    Page<Emprunt> findByRenduFalseAndDateRetourPrevueBefore(LocalDate date, // non rendus ET date prévue
                                                            Pageable pageable); // strictement avant "date"
}