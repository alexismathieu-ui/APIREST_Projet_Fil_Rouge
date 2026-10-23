package com.formation.bibliotheque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.*;

public class EmpruntRequest {

    @NotBlank(message = "Le livreId est obligatoire")
    @Positive(message = "Le livreId ne peut pas être négatif")
    private Long livreId;

    @NotBlank(message = "Le nom de l'emprunteur est obligatoire")
    private String emprunteur;

    @Email @NotBlank(message = "L'email de l'emprunteur est obligatoire")
    private String emailEmprunteur;

    public EmpruntRequest() {
    }

    public Long getLivreId() {
        return livreId;
    }

    public void setLivreId(Long livreId) {
        this.livreId = livreId;
    }

    public String getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(String emprunteur) {
        this.emprunteur = emprunteur;
    }

    public String getEmailEmprunteur() {
        return emailEmprunteur;
    }

    public void setEmailEmprunteur(String emailEmprunteur) {
        this.emailEmprunteur = emailEmprunteur;
    }

}