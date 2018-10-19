package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    /**
     * Il est important de bien mettre des alias Ã  chaque colonne pour que la table de correspondances fonctionne
     * Laisser les espaces entres les sauts de lignes pour Ã©viter les cas de "colxFROM Enseign.."
     */
    private static final List<String> reqSQL = new ArrayList<>();
    static{
        //1. Afficher pour chaque enseignant, son nombre total d'heures assurées
        reqSQL.add( " SELECT Enseignant.ID-Enseignant as id, Enseignant.Nom as nom, Enseignant.Prenom as prenom, SUM(Cours.Heures) as heures " +
                    " FROM   Enseignant, Enseigne, Cours " +
                    " WHERE  Enseignant.ID-Enseignant = Enseigne.ID-Enseignant " +
                    "   AND  Cours.ID-Cours = Enseigne.ID-Cours  " +
                    " GROUP BY Enseignant.ID-Enseignant ;");

        //2. Retourner le nombre d'étudiants dont le pays de Provenance est la France
        reqSQL.add( " SELECT COUNT(Etudiant.ID-Etudiant) as nb_etudiant_francais " +
                    " FROM   Etudiant " +
                    " WHERE  Etudiant.Provenance = 'France'; ");

        //3. Afficher le nombre de cours par Type (CM, TD ou TP).
        reqSQL.add( " SELECT Cours.Type as type, COUNT(Cours.Id-Cours) as nb_cours_par_type " +
                    " FROM Cours " +
                    " GROUP BY Cours.Type; ");

        //4. Afficher les étudiants de plus de 32 ans qui suivent le cours de M. Dubois Jean
/*        reqSQL.add( " SELECT Etudiant.ID-Etudiant as id, Etudiant.Nom as nom, Etudiant.prenom as prenom," +


        //4. Afficher les Ã©tudiants de plus de 32 ans qui suivent le cours de M. Dubois Jean
        reqSQL.add( " SELECT Etudiant.ID-Etudiant as id, Etudiant.Nom as nom, Etudiant.prenom as prenom," +

                    "   Etudiant.Age as age" +
                    " FROM Etudiant, Inscription, Cours, Enseigne, Enseignant" +
                    " WHERE Etudiant.ID-Etudiant = Inscription.ID-Etudiant" +
                    "   AND Inscription.ID-Cours = Cours.ID-Cours" +
                    "   AND Cours.ID-Cours = Enseigne.ID-Cours" +
                    "   AND Enseigne.ID-Enseignant = Enseignant.ID-Enseignant " +
                    "   AND Etudiant.Age > 32 " +
                    "   AND LOWER(Enseignant.Nom) = 'dubois' " +
                    "   AND LOWER(Enseignant.Prenom) = 'jean' ; ");*/
    }

    // Instancier un médiateur M ;
    // Faire appel aux classes d'adaptateur de chaque source ;
    public static void main(String[] args){
        Mediateur med = new Mediateur();
        List<HashMap<String, Object>> resultatReq;

        System.out.println("Hello Mediateur");

        // Parcout des requétes définit en static
        for(String element : reqSQL){
            System.out.println("\n" + element);
            med.sendReq(element);
            med.getResult();
            resultatReq = med.agregate();
            afficherResultat(resultatReq);
        }
    }

    private static void afficherResultat(List<HashMap<String, Object>> resultat){
    	System.out.println("Resultat transformé pour le mediateur");
    	for(HashMap<String, Object> element : resultat){
            System.out.println(element.toString());
        }
    }
}
