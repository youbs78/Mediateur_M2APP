package contract;

import java.util.HashMap;
import java.util.List;

public interface MediateurItf {
    /*  Définir les requêtes (vues) en se basant sur le dictionnaire de données du schéma global du médiateur ;
        List<Object> src1
        List<Object> src2
        List<Object> src3 */

    /**
     * Méthode/fonction pour envoyer les requêtes aux sources de données
     *
     * @param reqMed La requête SQL respectant le schéma global
     */
    void sendReq(String reqMed);

    /**
     * Méthode/fonction pour récupérer le résultat des 3 sources
     * Chaque résultat sera récupérés au sein de la classe
     *
     * Precondition: Avoir lancé la méthode "sendReq()"
     */
    void getResult();

    /**
     * Méthode/fonction pour agréger les résultats des 3 sources.
     *
     * @return la liste des résultats des 3 sources agrégés
     */
    List<HashMap<String, Object>> agregate();

}

