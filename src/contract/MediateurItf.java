package contract;

public interface MediateurItf {
    // Définir les requêtes (vues) en se basant sur le dictionnaire de données du schéma global du médiateur ;

    // Méthode/fonction pour envoyer les requêtes aux sources de données ;
    void sendReq();

    // Méthode/fonction pour récupérer les résultats des 3 sources ;
    void getResult();

    // Méthode/fonction pour agréger les résultats des 3 sources.
    void agregate();
}

