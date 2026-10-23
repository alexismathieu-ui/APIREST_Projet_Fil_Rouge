package com.formation.bibliotheque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


@Entity
@Table(name = "livres")
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String titre;

    @NotBlank(message = "L'isbn est obligatoire")
    @Column(nullable = false, unique = true)
    private String isbn;

    @Positive(message = "Le prix est forcement positif")
    @Column(nullable = false)
    private Double prix;

    private Integer anneePublication;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Long auteurId;

    public Livre() {
    }

    public Livre(String titre, String isbn, Double prix, Integer anneePublication, Integer stock, Long auteurId) {
        this.titre = titre;
        this.isbn = isbn;
        this.prix = prix;
        this.anneePublication = anneePublication;
        this.stock = stock;
        this.auteurId = auteurId;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre= titre;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public Double getPrix() {
        return prix;
    }
    public void setPrix(Double  prix) {
        this.prix = prix;
    }
    public Integer getAnneePublication() {
        return anneePublication;
    }
    public void setAnneePublication(Integer  anneePublication) {
        this.anneePublication = anneePublication;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer  stock) {
        this.stock = stock;
    }
    public Long getAuteurId() {
        return auteurId;
    }
    public void setAuteurId(Long  auteurid) {
        this.auteurId = auteurid;
    }
    @Override
    public String toString() {
        return "Livre{id=" + id + ", titre='" + titre + "', isbn=" + isbn + ", prix=" + prix + ", anneePublication= " + anneePublication + ", stock=" + stock + " , auteurid= " + auteurId +"}";
    }
}
