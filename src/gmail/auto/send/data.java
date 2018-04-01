package gmail.auto.send;

import java.util.ArrayList;


public class data {

    public static ArrayList<data> listData;
    private String user;
    private String pass;
    private String recoveryMail;

    public data() {

    }



    public data(String user, String pass, String recoveryMail) {
        this.user = user;
        this.pass = pass;
        this.recoveryMail = recoveryMail;
    }

    @Override
    public String toString() {
        return user + "," + pass + "," + recoveryMail;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setRecoveryMail(String recoveryMail) {
        this.recoveryMail = recoveryMail;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getRecoveryMail() {
        return recoveryMail;
    }
}
