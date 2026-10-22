public interface LivreRepository extends JpaRepository<Livre, Long> {
    Optional<Livre> findByIsbn(String isbn); // le livre qui a cet ISBN
    boolean existsByAuteurId(Long auteurId); // true si l'auteur a au moins
    un livre
    Page<Livre> findByAuteurId(Long auteurId, Pageable pageable); // les livres d'un auteur
    Page<Livre> findByTitreContainingIgnoreCase(String titre, Pageable pageable); // titre contenant le texte
    Page<Livre> findByStockGreaterThan(Integer stock, Pageable pageable); // stock strictement supérieur
}