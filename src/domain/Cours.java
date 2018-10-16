package domain;

public class Cours {
    private int     id_cours;
    private String  libele;
    private String  type;
    private String  niveau;
    private int     heures;

    public Cours() {
    }

    public Cours(int id_cours, String libele, String type, String niveau, int heures) {
        this.id_cours = id_cours;
        this.libele = libele;
        this.type = type;
        this.niveau = niveau;
        this.heures = heures;
    }

    public int getId_cours() {
        return id_cours;
    }

    public void setId_cours(int id_cours) {
        this.id_cours = id_cours;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public int getHeures() {
        return heures;
    }

    public void setHeures(int heures) {
        this.heures = heures;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id_cours=" + id_cours +
                ", libele='" + libele + '\'' +
                ", type='" + type + '\'' +
                ", niveau='" + niveau + '\'' +
                ", heures=" + heures +
                '}';
    }
}
