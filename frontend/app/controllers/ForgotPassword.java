package controllers;

import exceptions.DataAccessException;
import models.ForgotPasswordModel;
import models.ResetPassword;
import models.UserModel;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import views.html.forgotPassword;
import views.html.mailsent;
import views.html.resetPassword;
import views.html.resetSuccessful;


import java.util.Map;

import static play.data.Form.form;

/**
 * Created by laurens on 30.03.14.
 */
public class ForgotPassword extends Controller{



    public static Result forgot() {
        return ok(
                forgotPassword.render(form(ForgotPasswordModel.class))
        );

    }

    public static Result sendResetToken() {
        String mail = request().body().asFormUrlEncoded().get("email")[0];
        if(mail == null || mail.isEmpty()) {
            flash("error", "Gelieve een mailadres op te geven.");
            return redirect(routes.ForgotPassword.forgot());
        }
        try{
        String result = UserModel.createAndMailToken(mail);
        if(result != null){
            flash("error", result);
            return redirect(routes.Registration.index() );
        }
        }catch(DataAccessException e){
            flash("error","Er trad een fout op bij het aanmaken van een security token.");
        }

        return ok(views.html.mailsent.apply(mail));
    }

    /* POST to /resetPassword */
    public static Result resetPassword() {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        for(Map.Entry<String, String[]> e : form.entrySet()) Logger.info( e.getKey() + " " + e.getValue());
        try{
        UserModel.setNewPassword(form.get("token")[0], form.get("pass1")[0]);
        }catch(DataAccessException e){
            flash("error","Er trad een fout op bij het vernieuwen van het wachtwoord. Probeer later aub opnieuw.");
        }

        return ok(views.html.resetSuccessful.render());
    }

    /* GET to /resetPassword */
    public static Result reset(String token) {
        return ok(resetPassword.render(form(ResetPassword.class).fill(new ResetPassword(token)))
        );
    }



}
