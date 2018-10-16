package domain;

public class Inscription {
    private int id_etudiant;
    private int id_cours;
    private int annee;
    private int note;

    public Inscription() {
    }

    public Inscription(int id_etudiant, int id_cours, int annee, int note) {
        this.id_etudiant = id_etudiant;
        this.id_cours = id_cours;
        this.annee = annee;
        this.note = note;
    }

    public int getId_etudiant() {
        return id_etudiant;
    }

    public void setId_etudiant(int id_etudiant) {
        this.id_etudiant = id_etudiant;
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

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Inscription{" +
                "id_etudiant=" + id_etudiant +
                ", id_cours=" + id_cours +
                ", annee=" + annee +
                ", note=" + note +
                '}';
    }
}
