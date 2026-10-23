import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientBibliotheque {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final HttpClient client = HttpClient.newHttpClient();

    private static int nombreEtapes = 0;
    private static int nombreConformes = 0;

    private static HttpResponse<String> get(String chemin) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + chemin))
                .header("Accept", "application/json")
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> post(String chemin, String corpsJson) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + chemin))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corpsJson))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> patch(String chemin) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + chemin))
                .header("Accept", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> delete(String chemin) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + chemin))
                .DELETE()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void afficher(String description, String methode, String chemin, HttpResponse<String> reponse, int codeAttendu) {
        nombreEtapes++;
        boolean conforme = (reponse.statusCode() == codeAttendu);
        if (conforme) {
            nombreConformes++;
        }

        System.out.println("=== Étape " + nombreEtapes + " : " + description + " ===");
        System.out.println(methode + " " + chemin + " → " + reponse.statusCode() + " (attendu : " + codeAttendu + ") " + (conforme ? "CONFORME" : "NON CONFORME"));
        System.out.println("Réponse : " + reponse.body());
        System.out.println();
    }

    public static void main(String[] args) throws Exception {

        HttpResponse<String> r1 = get("/livres/disponibles?size=3");
        afficher("Lister les 3 premiers livres disponibles", "GET", "/livres/disponibles?size=3", r1, 200);

        HttpResponse<String> r2 = get("/livres/99");
        afficher("Lire un livre inexistant", "GET", "/livres/99", r2, 404);

        String auteurJson = """
                {
                  "nom": "Verne",
                  "prenom": "Jules",
                  "nationalite": "Française"
                }
                """;
        HttpResponse<String> r3 = post("/auteurs", auteurJson);
        afficher("Créer l'auteur Jules Verne", "POST", "/auteurs", r3, 201);

        String empruntLivre2 = """
                {
                  "livreId": 2,
                  "emprunteur": "Alice Durand",
                  "emailEmprunteur": "alice.durand@email.com"
                }
                """;
        HttpResponse<String> r4 = post("/emprunts", empruntLivre2);
        afficher("Emprunter le livre 2", "POST", "/emprunts", r4, 201);

        String empruntLivre8 = """
                {
                  "livreId": 8,
                  "emprunteur": "Alice Durand",
                  "emailEmprunteur": "alice.durand@email.com"
                }
                """;
        HttpResponse<String> r5 = post("/emprunts", empruntLivre8);
        afficher("Emprunter le livre 8 (stock à 0)", "POST", "/emprunts", r5, 409);

        HttpResponse<String> r6 = patch("/emprunts/2/retour");
        afficher("Rendre l'emprunt 2", "PATCH", "/emprunts/2/retour", r6, 200);

        HttpResponse<String> r7 = patch("/emprunts/2/retour");
        afficher("Rendre l'emprunt 2 une deuxième fois", "PATCH", "/emprunts/2/retour", r7, 409);

        HttpResponse<String> r8 = delete("/auteurs/1");
        afficher("Supprimer l'auteur 1 (il a des livres)", "DELETE", "/auteurs/1", r8, 409);

        String livreInvalide = """
                {
                  "titre": "",
                  "isbn": "978-2-0000-0000-0",
                  "prix": -5,
                  "stock": 1,
                  "auteurId": 1
                }
                """;
        HttpResponse<String> r9 = post("/livres", livreInvalide);
        afficher("Créer un livre invalide (titre vide, prix négatif)", "POST", "/livres", r9, 400);

        HttpResponse<String> r10 = delete("/auteurs/5");
        afficher("Supprimer l'auteur 5 (aucun livre)", "DELETE", "/auteurs/5", r10, 204);

        System.out.println("Bilan : " + nombreConformes + "/" + nombreEtapes + " étapes conformes");
    }
}