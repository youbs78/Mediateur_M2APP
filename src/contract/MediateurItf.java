package contract;

import java.util.List;

public interface MediateurItf {
    // Définir les requêtes (vues) en se basant sur le dictionnaire de données du schéma global du médiateur ;
//	List<Object> src1
//	List<Object> src2
//	List<Object> src
//	
    // Méthode/fonction pour envoyer les requêtes aux sources de données ;
    void sendReq(String reqMed);

    // Méthode/fonction pour récupérer le résultat des 3 sources ;
    void getResult();

    // Méthode/fonction pour agréger les résultats des 3 sources.
    List<Object> agregate();
    
}

