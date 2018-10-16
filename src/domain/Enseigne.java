package domain;

public class Enseigne {
    private int id_enseignant;
    private int id_cours;
    private int annee;
    private int heures;

    public Enseigne() {
    }

    public Enseigne(int id_enseignant, int id_cours, int annee, int heures) {
        this.id_enseignant = id_enseignant;
        this.id_cours = id_cours;
        this.annee = annee;
        this.heures = heures;
    }

    public int getId_enseignant() {
        return id_enseignant;
    }

    public void setId_enseignant(int id_enseignant) {
        this.id_enseignant = id_enseignant;
    }

    public int getId_cours() {
        return id_cours;
    }

    public void setId_cours(int id_cours) {
        this.id_cours = id_cours;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getHeures() {
        return heures;
    }

    public void setHeures(int heures) {
        this.heures = heures;
    }

    @Override
    public String toString() {
        return "Enseigne{" +
                "id_enseignant=" + id_enseignant +
                ", id_cours=" + id_cours +
                ", annee=" + annee +
                ", heures=" + heures +
                '}';
    }
}
