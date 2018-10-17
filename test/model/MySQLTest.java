package model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class MySQLTest {
    private MySQL srcMySQL = MySQL.getInstance();
    private static final String reqMed_1 =  "SELECT Enseignant.ID-Enseignant, Enseignant.Nom, Enseignant.Prenom, Enseignant.adresseMail " +
                                            "FROM Enseignant";
    private static final String reqSrc_1 =  "select enseignant.id_ens, enseignant.nom, enseignant.prenom, 'Source 2' as adressemail " +
                                            "from enseignant";
    private static final List<String> key_tradReq_1 = new ArrayList<>();
    static{
        //region Liste colonne traduite depuis reqSrc_1 attendu
        key_tradReq_1.add("enseignant.id-enseignant");
        key_tradReq_1.add("enseignant.nom");
        key_tradReq_1.add("enseignant.prenom");
        key_tradReq_1.add("adressemail");
        //endregione
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getInstance() {
        Assert.assertEquals(MySQL.class, MySQL.getInstance().getClass());
    }

    @Test
    public void connexion() {
        this.srcMySQL.connexion();
        Assert.assertNotNull(this.srcMySQL.getConn());
    }

    @Test
    public void deconnexion() {
        // Connexion obligatoire avant
        this.srcMySQL.connexion();
        this.srcMySQL.deconnexion();
        Assert.assertNull( this.srcMySQL.getConn());
    }

    @Test
    public void setMediateurReq() {
        String reqMed = "Test1";
        this.srcMySQL.setMediateurReq(reqMed);
        Assert.assertEquals(reqMed.toLowerCase(), this.srcMySQL.getMedSQL());
    }

    @Test
    public void reqMedtoReqSrc() {
        this.srcMySQL.setMediateurReq(reqMed_1);
        Assert.assertEquals(reqSrc_1, this.srcMySQL.reqMedtoReqSrc());
    }

    @Test
    public void executeReq() throws SQLException {
        this.srcMySQL.connexion();

        this.srcMySQL.executeReq(reqSrc_1);
        Assert.assertNotNull(this.srcMySQL.getStmt());
        // Vérifie s'il existe au moins un résultat
        Assert.assertTrue(this.srcMySQL.getRset().next());

        this.srcMySQL.deconnexion();
    }

    @Test
    public void getResFromExecuteReq() {
        this.srcMySQL.connexion();
        this.srcMySQL.executeReq(reqSrc_1);

        Assert.assertNotNull(this.srcMySQL.getResFromExecuteReq());
        // TODO: Vérifier le résultat de la requête exemple également

        this.srcMySQL.deconnexion();
    }

    @Test
    public void tradResToMed() {
        List<HashMap<String, Object>> res;
        List<HashMap<String, Object>> tradRes;

        this.srcMySQL.connexion();
        this.srcMySQL.executeReq(reqSrc_1);
        res = this.srcMySQL.getResFromExecuteReq();
        tradRes = this.srcMySQL.tradResToMed(res);

        Assert.assertNotEquals(0, tradRes.size());

        for (HashMap<String, Object> row : tradRes) {
            //On compare les colonnes attendues avec celles obtenues
            for(String e : key_tradReq_1){
                // On voti s'il existe une valeur attribué à la clef
                Assert.assertNotNull(row.getOrDefault(e,null));
            }
        }

        this.srcMySQL.deconnexion();
    }
}