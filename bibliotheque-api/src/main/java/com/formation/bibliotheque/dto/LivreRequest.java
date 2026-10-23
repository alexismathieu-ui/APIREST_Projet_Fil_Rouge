package com.formation.bibliotheque.dto;

import jakarta.validation.constraints.*;


public class LivreRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    private String titre;

    @NotBlank(message = "L'isbn est obligatoire")
    @Size(max = 100, message = "L'isbn ne doit pas dépasser 100 caractères")
    private String isbn;

    @NotBlank(message = "Le prix est obligatoire")
    @Positive(message = "Le prix ne doit pas être négatif")
    private Double prix;

    @Positive(message = "L'année de publication ne doit pas être négatif")
    private Integer anneePublication;

    @NotBlank(message = "Le stock est obligatoire")
    @PositiveOrZero(message = "Le stock ne peut pas être négatif")
    private Integer stock;

    @NotBlank(message = "L'auteurId est obligatoire")
    @Positive(message = "L'auteurId ne peut pas être négatif")
    private Long auteurId;

    public LivreRequest() {
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
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

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Integer getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(Integer anneePublication) {
        this.anneePublication = anneePublication;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(Long auteurId) {
        this.auteurId = auteurId;
    }
}