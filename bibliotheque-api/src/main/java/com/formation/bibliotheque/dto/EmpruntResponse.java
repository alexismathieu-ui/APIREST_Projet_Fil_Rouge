package com.formation.bibliotheque.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "livreId", "emprunteur", "emailEmprunteur"})
public class EmpruntResponse {
    private final Long id;
    private final Long livreId;
    private final String emprunteur;
    private final String emailEmprunteur;

    public EmpruntResponse(Long id, Long livreId, String emprunteur, String emailEmprunteur) {
        this.id = id;
        this.livreId = livreId;
        this.emprunteur = emprunteur;
        this.emailEmprunteur = emailEmprunteur;
    }
}
