package com.formation.bibliotheque.mapper;

public final class AuteurMapper{
    private AuteurMapper(){
        public static Auteur versEntite(AuteurRequest request){
            Auteur auteur = new Auteur();
            auteur.setNom(request.getNom());
            auteur.setPrenom(request.getPrenom());
            auteur.setNationnalite(request.getNationnalite());
            return auteur;
        }
        public static AuteurResponse versResponse(AuteurResponse response){
            return new AuteurResponse(),
                auteur.getId(),
                    auteur.getNom(),
                    auteur.getPrenom(),
                    auteur.getNationnalite()
        }
        public static List<AuteurResponse> versResponses(List<Auteur> auteurs) {
            List<AuteurResponse> resultat = new ArrayList<>();
            for (Auteur auteur : auteurs) {
                resultat.add(versResponse(auteur));
            }
            return resultat;
        }
        PagedModel<AuteurResponse> versPage(Page<Auteur>page) {
            return new PagedModel<>(page.map(AuteurMapper::versResponse));
        }
}
