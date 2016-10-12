package controllers;

import exceptions.DataAccessException;
import models.UserModel;
import objects.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

import static play.data.Form.form;

/**
 * Created by laurens on 20.03.14.
 */
public class Login extends Controller {

    public String email;
    public String password;
    public boolean remember_me;

/**
 * The GET /login route
 *
 * */
     public static Result login() {
            return ok(
                    login.render(form(Login.class))
            );
    }

    public static Result logout() {
        session().clear();
        flash("success", "Je bent succesvol afgemeld.");
        return redirect(
                routes.Application.index("home")
        );
    }

    public String validate() {
        String error = null;
        switch(edran.auth.Authentication.authenticate(email,password)){
            case 0:
                break;
            case 1:
                error = "Wachtwoord verkeerd, bent u het <a href=\"" + routes.ForgotPassword.forgot() + "\">vergeten</a>?";
                break;
            case 2:
                error = "Er bestaat geen account met dit emailadres.";
                break;
            case 3:
                error = "Je emailadres is nog niet geverifieerd. Gelieve eerst de link in de mail te volgen vooraleer in te loggen.  <a href=\"" + routes.Registration.resendVerification() + "\" Verificatie opnieuw sturen?";
                break;
            case -1:
            default:
                error = "Er ging iets enorm fout. De webbeheerder heeft een melding gekregen. Gelieve later opnieuw te proberen";
                break;
        }
        if(error !=null) flash("error", error);

        return error;
    }

    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(form(Login.class)));
        } else {
            session().clear();
            try{
            session("current_user", UserModel.getIdByMail(loginForm.get().email));
            Logger.info(UserModel.getIdByMail(loginForm.get().email));
            }catch(DataAccessException e){
                flash("error","Het was onmogelijk om jouw ID op te vragen. Onze excuses voor dit ongemak.");
            }
            return redirect(
                    routes.Application.index("home")
            );
        }
     }


}
