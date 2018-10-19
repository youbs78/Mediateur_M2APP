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

        this.extracteurSrc1.setMediateurReq(reqMed);
        //this.extracteurSrc2.setMediateurReq(reqMed);
        //this.extracteurSrc3.setMediateurReq(reqMed);

        //this.extracteurSrc1.setMediateurReq(reqMed);
        this.extracteurSrc2.setMediateurReq(reqMed);
        this.extracteurSrc3.setMediateurReq(reqMed);

    }

    @Override
    public void getResult() {
        // La requéte traduite en 'langage source'
        String srcReq;
        // La liste de résultat issue directement depuis la source
        List<HashMap<String, Object>> tmp;

        //region Source 1
        this.extracteurSrc1.connexion();
        srcReq = this.extracteurSrc1.reqMedtoReqSrc();
        this.extracteurSrc1.executeReq(srcReq);
        tmp = this.extracteurSrc1.getResFromExecuteReq();
        this.resSrc1 = this.extracteurSrc1.tradResToMed(tmp);
        this.extracteurSrc1.deconnexion();
        //endregion
        
/*
        //region Source 2
        this.extracteurSrc2.connexion();
        srcReq = this.extracteurSrc2.reqMedtoReqSrc();
        this.extracteurSrc2.executeReq(srcReq);
        tmp = this.extracteurSrc2.getResFromExecuteReq();
        this.resSrc2 = this.extracteurSrc2.tradResToMed(tmp);
        this.extracteurSrc2.deconnexion();
        //endregion
<<<<<<< HEAD
*/
/*
=======

        /*
>>>>>>> branch 'master' of https://github.com/youbs78/Mediateur_M2APP.git
        //region Source 3
        //this.extracteurSrc3.connexion();
        srcReq = this.extracteurSrc3.reqMedtoReqSrc();
        this.extracteurSrc3.executeReq(srcReq);
        //tmp = this.extracteurSrc3.getResFromExecuteReq();
        //this.resSrc3 = this.extracteurSrc3.tradResToMed(tmp);
        //this.extracteurSrc3.deconnexion();
        //endregion
        */
    }

    @Override
    public List<HashMap<String, Object>> agregate() {
        List<HashMap<String, Object>> listRes = new ArrayList<>();

        if(this.resSrc1 != null) listRes.addAll(this.resSrc1);
        if(this.resSrc2 != null) listRes.addAll(this.resSrc2);
        if(this.resSrc3 != null) listRes.addAll(this.resSrc3);

        return listRes;
    }
    /*  */
}
