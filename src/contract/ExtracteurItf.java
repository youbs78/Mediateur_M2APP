package contract;

import java.util.List;

public interface ExtracteurItf {
     /*  Générer une table de correspondance entre la source et le médiateur pour
         décrire le template des attributs ; (HASHMAP ==>  Cle: Requete Mediateur , Valeur : Requete Source)
         hashmap<String,String> TableCorespond;*/


    /**
     * Méthode/fonction de connexion à la source de données
     * crée préalablement qui suit un schéma conceptuel décrit dans un catalogue
     * dans la BDD/fichier) ;
     *
     * @return vrai (true) si la connexion s'est bien passée
     */
    boolean connexion();

    /**
     * Méthode/fonction de déconnexion à la source de données
     * crée préalablement qui suit un schéma conceptuel décrit dans un catalogue
     * dans la BDD/fichier) ;
     *
     * @return vrai (true) si la déconnexion s'est bien passée
     */
    boolean deconnexion();

    /**
     * Méthode/fonction pour recevoir les requêtes du médiateur respectant
     * le schéma conceptuel global
     * La requête doit être mise en minucule !!!
     *
     * @param reqMed La requête SQL envoyé par le médiateur
     */
    void setMediateurReq(String reqMed);


    /**
     * Méthode/fonction pour traduire la requête (vue) du médiateur par le schéma de la source
     * en parcourant la table de correspondance générée par le générateur d’adaptateur
     * et chercher le template qui correspond à la requête du médiateur ;
     *
     * @param reqMed La requête SQL envoyé par le médiateur
     * @return La requête SQL correspondante à celle de la source
     */
    String reqMedtoReqSrc(String reqMed);

    /**
     * Méthode/fonction pour exécuter (interroger la source) les requêtes du médiateur sur la source
     *
     * @param reqSrc La requête SQL de la source à exécuter
     */
    void executeReq(String reqSrc);

    /**
     * Méthode/fonction pour récupérer le résultat de la requête
     *
     * @param req La requête SQL à exécuter
     * @return La liste de résultat obtenu par la requête source
     */
    List<Object> getResFromExecuteReq(String req);

    /**
     * Méthode/fonction pour traduire le résultat et l’envoyer au médiateur.
     *
     * @param resSrc La liste de résultat obtenu par la requête source
     * @return La liste de résultat traduite selon le schéma conceptuel global
     */
    List<Object> tradResToMed(List<Object> resSrc);


}
