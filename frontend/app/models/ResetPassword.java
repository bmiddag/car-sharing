package models;

/**
 * Created by laurens on 17.04.14.
 */
public class ResetPassword {

    public String token;
    public String pass1;
    public String pass2;

    public ResetPassword(String token) {
        this.token = token;
    }

    public ResetPassword() {

    }

}
