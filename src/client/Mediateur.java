package client;


import contract.MediateurItf;
import model.Excel;
import model.MySQL;
import model.XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        //this.extracteurSrc1.setMediateurReq(reqMed);
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
                if(this.resSrc1 != null) listRes.addAll(this.resSrc1);
                if(this.resSrc2 != null) listRes.addAll(this.resSrc2);
                if(this.resSrc3 != null) listRes.addAll(this.resSrc3);
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

    private List<HashMap<String, Object>> agregateReq2(){
        List<HashMap<String, Object>> listRes = new ArrayList<>();
        HashMap<String, Object> tmp = new HashMap<>();
        String key = "nb_etudiant_francais";
        int nbTotal = 0;

        if(this.resSrc1 != null){
            for (HashMap<String, Object> entry : this.resSrc1) {
                nbTotal += Integer.parseInt(entry.get(key).toString());
            }
        }

        if(this.resSrc2 != null){
            for (HashMap<String, Object> entry : this.resSrc2) {
                nbTotal += Integer.parseInt(entry.get(key).toString());
            }
        }

        if(this.resSrc3 != null){
            for (HashMap<String, Object> entry : this.resSrc3) {
                nbTotal += Integer.parseInt(entry.get(key).toString());
            }
        }

        tmp.put(key,nbTotal);
        listRes.add(tmp);

        return listRes;
    }

    @SuppressWarnings("Duplicates")
    private List<HashMap<String, Object>> agregateReq3(){
        List<HashMap<String, Object>> listRes = new ArrayList<>();
        HashMap<String, Object> cmH = new HashMap<>();
        HashMap<String, Object> tdH = new HashMap<>();
        HashMap<String, Object> tpH = new HashMap<>();

        String key = "nb_cours_par_type";
        String key2 = "cours.type";
        String type;
        int cm = 0, td = 0, tp = 0;

        if(this.resSrc1 != null){
            for (HashMap<String, Object> entry : this.resSrc1) {
                type = (String) entry.get(key2);
                switch (type){
                    case "CM":
                        cmH.put(key2,"CM");
                        cm += Integer.parseInt(entry.get(key).toString());
                        cmH.put(key, cm);
                        break;
                    case "TD":
                        tdH.put(key2,"TD");
                        td += Integer.parseInt(entry.get(key).toString());
                        tdH.put(key, td);
                        break;
                    case "TP":
                        tpH.put(key2,"TP");
                        tp += Integer.parseInt(entry.get(key).toString());
                        tpH.put(key, tp);
                        break;
                }
            }
        }

        if(this.resSrc2 != null){
            for (HashMap<String, Object> entry : this.resSrc2) {
                type = (String) entry.get(key2);
                switch (type){
                    case "CM":
                        cmH.put(key2,"CM");
                        cm += Integer.parseInt(entry.get(key).toString());
                        cmH.put(key, cm);
                        break;
                    case "TD":
                        tdH.put(key2,"TD");
                        td += Integer.parseInt(entry.get(key).toString());
                        tdH.put(key, td);
                        break;
                    case "TP":
                        tpH.put(key2,"TP");
                        tp += Integer.parseInt(entry.get(key).toString());
                        tpH.put(key, tp);
                        break;
                }
            }
        }

        if(this.resSrc3 != null){
            for (HashMap<String, Object> entry : this.resSrc3) {
                type = (String) entry.get(key2);
                switch (type){
                    case "CM":
                        cmH.put(key2,"CM");
                        cm += Integer.parseInt(entry.get(key).toString());
                        cmH.put(key, cm);
                        break;
                    case "TD":
                        tdH.put(key2,"TD");
                        td += Integer.parseInt(entry.get(key).toString());
                        tdH.put(key, td);
                        break;
                    case "TP":
                        tpH.put(key2,"TP");
                        tp += Integer.parseInt(entry.get(key).toString());
                        tpH.put(key, tp);
                        break;
                }
            }
        }

        listRes.add(cmH);
        listRes.add(tdH);
        listRes.add(tpH);

        return listRes;
    }
}
