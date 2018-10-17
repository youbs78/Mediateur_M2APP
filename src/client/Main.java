package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    /**
     * Il est important de bien mettre des alias à chaque colonne pour que la table de correspondances fonctionne
     * Laisser les espaces entres les sauts de lignes pour éviter les cas de "colxFROM Enseign.."
     */
    private static final List<String> reqSQL = new ArrayList<>();
    static{
        //1. Afficher pour chaque enseignant, son nombre total d’heures assurées.
        reqSQL.add( " SELECT Enseignant.ID-Enseignant as id, Enseignant.Nom as nom, Enseignant.Prenom as prenom, SUM(Cours.Heures) as heures " +
                    " FROM   Enseignant, Enseigne, Cours " +
                    " WHERE  Enseignant.ID-Enseignant = Enseigne.ID-Enseignant " +
                    "   AND  Cours.ID-Cours = Enseigne.ID-Cours  " +
                    " GROUP BY Enseignant.ID-Enseignant ;");

        //2. Retourner le nombre d’étudiants dont le pays de Provenance est la ‘France’.
        reqSQL.add( " SELECT COUNT(Etudiant.ID-Etudiant) as nb_etudiant_francais " +
                    " FROM   Etudiant " +
                    " WHERE  Etudiant.PaysFormationPrecedente = 'France'; ");
        /*
        //3. Afficher le nombre de cours par Type (CM, TD ou TP).
        reqSQL.add( "SELECT... " +
                    "FROM... ");
        */
    }

    // Instancier un médiateur M ;
    // Faire appel aux classes d’adaptateur de chaque source ;
    public static void main(String[] args){
        Mediateur med = new Mediateur();
        List<HashMap<String, Object>> resultatReq;

        System.out.println("Hello World");

        // Parcout des requêtes définit en static
        for(String element : reqSQL){
            med.sendReq(element);
            med.getResult();
            resultatReq = med.agregate();
            afficherResultat(resultatReq);
        }
    }

    private static void afficherResultat(List<HashMap<String, Object>> resultat){
        for(HashMap<String, Object> element : resultat){
            System.out.println(element.toString());
        }
    }
}
