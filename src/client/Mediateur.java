package client;


import contract.MediateurItf;
import model.Excel;
import model.MySQL;
import model.XML;

import java.util.*;

public class Mediateur implements MediateurItf {
    private Excel extracteurSrc1;
    private MySQL extracteurSrc2;
    private XML   extracteurSrc3;
    private List<HashMap<String, Object>> resSrc1;
    private List<HashMap<String, Object>> resSrc2;
    private List<HashMap<String, Object>> resSrc3;


    Mediateur() {
        this.extracteurSrc1 = Excel.getInstance();
        this.extracteurSrc2 = MySQL.getInstance();
        this.extracteurSrc3 = XML.getInstance();
    }

    public Excel getExtracteurSrc1() {
        return extracteurSrc1;
    }

    public MySQL getExtracteurSrc2() {
        return extracteurSrc2;
    }

    public XML getExtracteurSrc3() {
        return extracteurSrc3;
    }

    public List<HashMap<String, Object>> getResSrc1() {
        return resSrc1;
    }

    public List<HashMap<String, Object>> getResSrc2() {
        return resSrc2;
    }

    public List<HashMap<String, Object>> getResSrc3() {
        return resSrc3;
    }

    @Override
    public void sendReq(String reqMed) {
        this.extracteurSrc1.setMediateurReq(reqMed);
        this.extracteurSrc2.setMediateurReq(reqMed);
        this.extracteurSrc3.setMediateurReq(reqMed);
    }

    @Override
    public void getResult() {
        // La requête traduite en 'langage source'
        String srcReq;
        // La liste de résultat issue directement depuis la source
        List<HashMap<String, Object>> tmp;
/*
        //region Source 1
        this.extracteurSrc1.connexion();
        srcReq = this.extracteurSrc1.reqMedtoReqSrc();
        this.extracteurSrc1.executeReq(srcReq);
        tmp = this.extracteurSrc1.getResFromExecuteReq();
        this.resSrc1 = this.extracteurSrc1.tradResToMed(tmp);
        this.extracteurSrc1.deconnexion();
        //endregion
*/
        //region Source 2
        this.extracteurSrc2.connexion();
        srcReq = this.extracteurSrc2.reqMedtoReqSrc();
        this.extracteurSrc2.executeReq(srcReq);
        tmp = this.extracteurSrc2.getResFromExecuteReq();
        this.resSrc2 = this.extracteurSrc2.tradResToMed(tmp);
        this.extracteurSrc2.deconnexion();
        //endregion


        //region Source 3
        this.extracteurSrc3.connexion();
        srcReq = this.extracteurSrc3.reqMedtoReqSrc();
        this.extracteurSrc3.executeReq(srcReq);
        tmp = this.extracteurSrc3.getResFromExecuteReq();
        this.resSrc3 = this.extracteurSrc3.tradResToMed(tmp);
        this.extracteurSrc3.deconnexion();
        //endregion

    }

    @Override
    public List<HashMap<String, Object>> agregate(int numero_requete) {
        List<HashMap<String, Object>> listRes = new ArrayList<>();

        switch (numero_requete){
            case 1:
                listRes = this.agregateReq1();
                break;
            case 2:
                listRes = this.agregateReq2();
                break;
            case 3:
                listRes = this.agregateReq3();
                break;
        }

        return listRes;
    }

    private List<HashMap<String, Object>> agregateReq1(){
        List<HashMap<String, Object>> listRes = new ArrayList<>();
        List<HashMap<String, Object>> tmp = new ArrayList<>();
        String key = "enseignant.id", key2="heures";

        if(this.resSrc1 != null) tmp.addAll(this.resSrc1);
        if(this.resSrc2 != null) tmp.addAll(this.resSrc2);
        if(this.resSrc3 != null) tmp.addAll(this.resSrc3);

        for(HashMap<String, Object> row : tmp){
            HashMap<String, Object> tmpH = new HashMap<>();
            String id = row.get(key).toString();

            for(HashMap.Entry<String, Object> entry : row.entrySet()){
                String clef = entry.getKey();
                Object valeur = entry.getValue();

               // valeur = row.remove(clef);
                if(clef.equals("heures")) valeur = this.countReqBySource(id, key, key2);
                tmpH.put(clef, valeur);
            }

            if(!presentInList(listRes, key, id)) listRes.add(tmpH);
        }

        return listRes;
    }

    private boolean presentInList(List<HashMap<String, Object>> list, String key, String id){
        for(HashMap<String, Object> row : list){
            String test = row.get(key).toString();
            if(test.equals(id)) return true;
        }
        return false;
    }

    private List<HashMap<String, Object>> agregateReq2(){
        List<HashMap<String, Object>> listRes = new ArrayList<>();
        HashMap<String, Object> tmp = new HashMap<>();
        int nbTotal = 0;

        nbTotal += countReq2BySource(this.resSrc1);
        nbTotal += countReq2BySource(this.resSrc2);
        nbTotal += countReq2BySource(this.resSrc3);

        tmp.put("nb_etudiant_francais",nbTotal);
        listRes.add(tmp);

        return listRes;
    }

    private int countReq2BySource (List<HashMap<String, Object>> src){
        if (src==null) return 0;
        int total = 0;

        for (HashMap<String, Object> entry : src) {
            total += Integer.parseInt(entry.get("nb_etudiant_francais").toString());
        }

        return total;
    }

    private List<HashMap<String, Object>> agregateReq3(){
        List<HashMap<String, Object>> listRes = new ArrayList<>();
        HashMap<String, Object> cmH = new HashMap<>();
        HashMap<String, Object> tdH = new HashMap<>();
        HashMap<String, Object> tpH = new HashMap<>();
        String key = "cours.type", key2="nb_cours_par_type";

        cmH.put(key,"CM");
        cmH.put(key2,countReqBySource("CM", key, key2));
        listRes.add(cmH);

        tdH.put(key,"TD");
        tdH.put(key2,countReqBySource("TD", key, key2));
        listRes.add(tdH);

        tpH.put(key,"TP");
        tpH.put(key2,countReqBySource("TP", key, key2));
        listRes.add(tpH);

        return listRes;
    }

    private int countReqBySearch (List<HashMap<String, Object>> src, String search, String key, String key2){
        if(src == null) return 0;
        int cpt=0;

        for (HashMap<String, Object> entry : src) {
            if(entry.get(key).equals(search)) {
                cpt += Integer.parseInt(entry.get(key2).toString());
            }
        }
        return cpt;
    }

    private int countReqBySource (String type, String key, String key2) {
        int total=0;

        total += this.countReqBySearch(this.resSrc1, type, key, key2);
        total += this.countReqBySearch(this.resSrc2, type, key, key2);
        total += this.countReqBySearch(this.resSrc3, type, key, key2);

        return total;
    }

}
