package client;


import contract.MediateurItf;
import model.MySQL;

import java.util.List;

public class Mediateur implements MediateurItf {
    private List<Object> resSrc1;
    private List<Object> resSrc2;
    private List<Object> resSrc3;


    @Override
    public void sendReq(String reqMed) {
        MySQL src2 = MySQL.getInstance();
    }

    @Override
    public void getResult() {

    }

    @Override
    public List<Object> agregate() {
        return null;
    }
    /*  */
}
