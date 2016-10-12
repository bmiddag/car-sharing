package models;

import static play.mvc.Controller.flash;

/**
 * Created by laurens on 30.03.14.
 */
public class ForgotPasswordModel {
    public String email;

    public String validate() {
        flash("validated");
        return null;
    }
}
