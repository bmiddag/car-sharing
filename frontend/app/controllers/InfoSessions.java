package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import exceptions.DataAccessException;
import models.InfoSessionsModel;
import models.UserModel;
import models.RoleModel;
import objects.*;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.formdata.InfoSessionFormData;
import views.html.index;
import views.html.infosessions;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the controller for the InfoSessions part of the application, serving as a link between
 * the model and the view. See the individual methods' documentation for more information.
 *
 * @author  Bart Middag
 * @see     objects.InfoSession
 * @see     views.formdata.InfoSessionFormData
 * @see     models.InfoSessionsModel
 */
public class InfoSessions extends Controller {
    /**
     * Display the index page. This page looks different depending on the role of the user.
     * @return The index page.
     */
    @Restrict({@Group("admin"),@Group("new_owner"),@Group("new_user")})
    public static Result index() throws DataAccessException {
        MyDynamicResourceHandler drh = new MyDynamicResourceHandler();
        if( false){//!drh.checkPermission("infosessions.view")) {
            flash("error","Je hebt onvoldoende rechten om deze pagina te bekijken.");
            return badRequest(index.render("home", null, null, null, null, null, null, null, null));
        }
        User user = UserModel.getCurrentUser();
        Logger.debug(user.getId().toString());
        List<InfoSessionFormData> formDataList = InfoSessionsModel.getInfoSessionFormDataList(user);
        formDataList.add(new InfoSessionFormData());
        List<Form<InfoSessionFormData>> formList = new ArrayList<Form<InfoSessionFormData>>();
        for(InfoSessionFormData formData: formDataList) {
            formList.add(Form.form(InfoSessionFormData.class).fill(formData));
        }

        return ok(infosessions.render(formList, drh.checkPermission("infosessions.edit")));
    }

    /**
     * Process a form submission for the InfoSession with the specified ID.
     * First we bind the HTTP POST data to an instance of InfoSessionFormData.
     * The binding process will invoke the InfoSessionFormData.validate() method.
     * If errors are not found, update the information in the database.
     * Finally, re-render the page.
     * @return The index page with the results of validation.
     */
    @Restrict({@Group("admin"),@Group("new_owner"),@Group("new_user")})
    public static Result postIndex(String id) throws DataAccessException {
        List<InfoSessionFormData> formDataList;
        List<Form<InfoSessionFormData>> formList = new ArrayList<Form<InfoSessionFormData>>();
        User user = UserModel.getCurrentUser();

        // Get the submitted form data from the request object, and run validation.
        Form<InfoSessionFormData> sessionForm = Form.form(InfoSessionFormData.class).bindFromRequest();

        if(sessionForm.hasErrors()) {
            // Don't call formData.get() when there are errors, pass 'null' to helpers instead.
            String errorList = "<ul>";
            for(String key: sessionForm.errors().keySet()) {
                errorList += "<li>" + sessionForm.errors().get(key).get(0).message();
                //errorList += key + ": " + sessionForm.errors().get(key) + "<br>";
            }
            errorList += "</ul>";
            flash("error", "Er zijn fouten opgetreden: "+errorList);
            formDataList = InfoSessionsModel.getInfoSessionFormDataList(user);
            formDataList.add(new InfoSessionFormData());
            for(InfoSessionFormData formData: formDataList) {
                /*if(formData.id.equals(id)) {
                    formList.add(sessionForm);
                } else {*/
                    formList.add(Form.form(InfoSessionFormData.class).fill(formData));
                //}
            }
            return badRequest(infosessions.render(formList, RoleModel.hasPermission(RoleModel.Permission.EDIT_INFOSESSIONS)));
        }
        else {
            InfoSession session;
            try {
            if(RoleModel.hasPermission(RoleModel.Permission.EDIT_INFOSESSIONS)&&(!sessionForm.get().inscriptionOnly.equals("inscriptionOnly"))) {
                session = InfoSessionsModel.manageInfoSession(sessionForm.get(),user);
                if(session == null) {
                    flash("success", "Je hebt deze infosessie met succes verwijderd.");
                } else {
                    flash("success", "Je hebt een infosessie aangemaakt of gewijzigd met identificatienummer #" + session.getId());
                }
            } else {
                session = InfoSessionsModel.manageInscription(sessionForm.get(),user);
                flash("success", "Je hebt jouw inschrijving aangepast voor infosessie met identificatienummer #" + session.getId());

                if(!RoleModel.hasPermission(RoleModel.Permission.VIEW_INFOSESSIONS)) {
                    return redirect(routes.Application.index("home"));
                }
            }
            } catch(DataAccessException e) {
                flash("error","Er is helaas een fout opgetreden - de aanpassing werd niet opgeslagen. Probeer het later opnieuw.");
                Logger.error("", e);
            }
            formDataList = InfoSessionsModel.getInfoSessionFormDataList(user);
            formDataList.add(new InfoSessionFormData());
            for(InfoSessionFormData formData: formDataList) {
                formList.add(Form.form(InfoSessionFormData.class).fill(formData));
            }
            return ok(infosessions.render(formList, RoleModel.hasPermission(RoleModel.Permission.EDIT_INFOSESSIONS)));
        }
    }
}
