package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectNotPresent;
import exceptions.DataAccessException;
import models.CarManagementModel;
import models.UserModel;
import objects.*;
import objects.Car;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.register;
import views.html.registercar;
import views.html.verificationmail;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by laurens on 27.03.14.
 */
public class Registration extends Controller {

    /**
     * Register user - index
     */
    @SubjectNotPresent
    public static Result index() {
        return ok(register.render());
    }

    /**
     * Register a car - index
     */
    @Restrict({@Group("admin"),@Group("new_user")})
    public static Result indexCar(){
        return ok(registercar.render());
    }

    /**
     * The POST route for registrations
     */
    @SubjectNotPresent
    public static Result register() {
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        try {
            String id = UserModel.getIdByMail(postData.get("email")[0]);
            if (id != null) {
                flash("error", "Dit e-mailadres zit reeds in de database. <a href=\"" + routes.ForgotPassword.forgot() + "\" > Wachtwoord vergeten?</a>");
                return redirect(routes.Login.login());
            }
        } catch (DataAccessException e) {
            flash("error", "Het is ons niet gelukt uw ID te achterhalen. Onze excuses. Probeer later opnieuw.");
        }

        try {
            User u = UserModel.createNewUser(postData, false);
            if(u != null) {
                flash("success","Gefeliciteerd! Je bent geregistreerd. Gelieve de instructies in de bevestigingsmail te volgen.");
            } else {
                flash("error","Er is helaas een fout opgetreden - je werd niet geregistreerd. Probeer het later opnieuw.");
            }
        } catch(DataAccessException e) {
            flash("error","Er is helaas een fout opgetreden - je werd niet geregistreerd. Probeer het later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Application.index("home"));
    }
    
    public static Result completeRegister() throws FileNotFoundException {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        try {
            UserModel.completeRegistrationUser(body, UserModel.getCurrentUser());
        } catch(DataAccessException e) {
            flash("error","Er is helaas een fout opgetreden - de gegevens zijn niet opgeslaan. Probeer het later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Application.index("home"));
    }

    /**
     * The POST route for car registrations
     */
    @Restrict({@Group("admin"),@Group("new_user")})
    public static Result registerCar() {


        Http.MultipartFormData body = request().body().asMultipartFormData();


        try {
            User u = UserModel.getCurrentUser();
            Car c = CarManagementModel.createNewCar(body.asFormUrlEncoded(), u, body);
            if(c != null) {
                UserModel.setRole(u.getId(),"new_owner");
            }
            flash("success","Je auto is geregistreerd. Deze moet echter nog goedgekeurd worden door een beheerder. Hou je notificaties in het oog!");
        } catch(DataAccessException e) {
            flash("error","Er is helaas een fout opgetreden - je auto werd niet geregistreerd. Probeer het later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Application.index("home"));
    }


    public static Result verifymail(String token) {
        Integer id = null;
        try {
            User u = UserModel.verifymailAndGetId(token);
            session("current_user", u.getId().toString());
            flash("succes", "Jouw wachtwoord is geverifïeerd!");
            return redirect(routes.Application.index("home"));
        } catch (DataAccessException e) {
            flash("error", "Er ging iets mis! Gelieve contact op te nemen met een administator." + e.toString());
            return redirect(routes.Application.index("home"));
        }

    }

    public static Result resendVerification(){
        String mail = request().body().asFormUrlEncoded().get("email")[0];
        if(mail == null || mail.isEmpty()) {
            flash("error", "Gelieve een mailadres op te geven.");
            return redirect(routes.Registration.resendVerification());
        }
        try{
            String result = UserModel.createAndMailToken(mail);
            if(result != null){
                flash("error", result);
                return redirect(routes.Application.index("home") );
            }
        }catch(DataAccessException e){
            flash("error","Er trad een fout op bij het aanmaken van een security token.");
        }

        return ok(views.html.mailsent.apply(mail));
    }

    public static Result resendveri() {
        return ok(verificationmail.render());
    }
}

