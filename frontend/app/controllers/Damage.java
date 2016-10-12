package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import interfaces.*;

import java.io.FileInputStream;
import java.io.IOException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jdbc.JDBCDataAccessProvider;

import objects.*;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import utils.TimeUtils;

import exceptions.DataAccessException;

/**
 * Created by wouter on 15/03/14.
 */
public class Damage  extends Controller {


    /**
     * Views will send a post request to this method to update the details of a damage
     * @return
     * @throws exceptions.DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result updateDamage() throws DataAccessException {
        // Get the data from the POST request
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String value = postData.get("value")[0];
        String name = postData.get("name")[0];
        String damageId = postData.get("pk")[0];
        int parsedDamageId = Integer.parseInt(damageId);

        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            context.begin();
            DamageDAO dao = context.getDamageDAO();

            switch(name){
                case "time":
                    dao.updateDamageOccurred(TimeUtils.stringToLong("dd/MM/yyyy HH:mm", value), parsedDamageId);
                    break;
                case "description":
                    dao.updateDamageDescription(value, parsedDamageId);
                    break;
            }

            context.commit();
        } catch (DataAccessException | NumberFormatException | ParseException e) {
            flash("error", "Er is een fout opgetreden, het aanpassen van dit schadegeval is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }

    /**
     * Views will send a post request to this method to add a new damage
     * @return
     * @throws exceptions.DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result newDamage() throws DataAccessException {
        // Get the uploaded files
        Http.MultipartFormData body = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> files = body.getFiles();

        // Get other POST data
        Map<String, String[]> postData = body.asFormUrlEncoded();
        String date = postData.get("date-hidden")[0];
        String description = postData.get("description")[0];
        String rideId = postData.get("newDamageRideId")[0];

        try {
            int parsedRideId = Integer.parseInt(rideId);
            long dateEpoch = TimeUtils.stringToLong("dd/MM/yyyy HH:mm", date);

            DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
            context.begin();

            // Make the new damage
            RideDAO rideDAO = context.getRideDAO();
            objects.Ride ride = rideDAO.getRide(parsedRideId);

            if(! (ride.getBegin() < dateEpoch && dateEpoch < ride.getEnd())){
                flash("error", "Het toevoegen van het schadegeval is mislukt. De datum van het schadegeval moet tussen de begin- en einddatum van de rit vallen.");
            } else {
                DamageDAO damageDAO = context.getDamageDAO();
                objects.Damage damage = damageDAO.createDamage(ride, description, dateEpoch);
                damage.setStatus("In behandeling");
                damageDAO.updateDamage(damage);

                // For each uploaded file, make a DamageDoc
                for(Http.MultipartFormData.FilePart fp : files) {
                    FileDAO fileDAO = context.getFileDAO();

                    objects.File daoFile = fileDAO.createFile(new FileInputStream(fp.getFile()), fp.getFilename());

                    DamageDocDAO damageDocDAO = context.getDamageDocDAO();
                    damageDocDAO.createDamageDoc(damage, daoFile);
                }

                context.commit();
            }
        }
        catch (DataAccessException | NumberFormatException | IOException | ParseException e){
            flash("error", "Er is een fout opgetreden, het aanmaken van dit schadegeval is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }

    /**
     * Views will send a post request to this method to add extra damage proofs
     * @return
     * @throws exceptions.DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result extraDamageFile() throws DataAccessException {
        // Get the uploaded files
        Http.MultipartFormData body = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> files = body.getFiles();

        // Get other POST data
        Map<String, String[]> postData = body.asFormUrlEncoded();
        String id = postData.get("id")[0];

        try {
            int parsedId = Integer.parseInt(id);

            DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
            context.begin();

            DamageDAO damageDAO = context.getDamageDAO();
            objects.Damage damage = damageDAO.getDamage(parsedId);

            // For each uploaded file, make a DamageDoc
            for(Http.MultipartFormData.FilePart fp : files) {
                FileDAO fileDAO = context.getFileDAO();

                objects.File daoFile = fileDAO.createFile(new FileInputStream(fp.getFile()));

                DamageDocDAO damageDocDAO = context.getDamageDocDAO();
                damageDocDAO.createDamageDoc(damage, daoFile);
            }

            context.commit();
        }
        catch (DataAccessException | NumberFormatException | IOException e){
            flash("error", "Er is een fout opgetreden, het toevoegen van dit document is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }

    /**
     * Views will send a post request to this method to delete a damage (with damagedoc)
     * @return
     * @throws DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result deleteDamage() throws DataAccessException {
        // Get the data from the POST request
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String damageId = postData.get("damageId")[0];
        String[] damageDocIds = postData.get("damageDocId");

        try {
            int parsedDamageId = Integer.parseInt(damageId);

            DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
            context.begin();

            if(damageDocIds != null) {
                for(String docId : damageDocIds){
                    int parsedDamageDocId = Integer.parseInt(docId);
                    DamageDocDAO damageDocDAO = context.getDamageDocDAO();
                    damageDocDAO.deleteDamageDoc(new DamageDoc(null, parsedDamageDocId));
                }
            }

            DamageDAO damageDAO = context.getDamageDAO();
            damageDAO.deleteDamage(parsedDamageId);

            context.commit();
        }
        catch (DataAccessException | NumberFormatException e){
            flash("error", "Er is een fout opgetreden, het verwijderen van dit document is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }

    public static Result search() throws DataAccessException {
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        if(postData != null && postData.get("query") != null) {
            String query = postData.get("query")[0];

            List<objects.Damage> damages = new ArrayList<>();
            try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
                context.begin();
                DamageDAO dao = context.getDamageDAO();
                damages = dao.searchDamage(query);
                context.commit();
            } catch (DataAccessException | NumberFormatException e) {
                flash("error", "Er is een fout opgetreden, het opzoeken van schadegevallen is niet gelukt.");
                Logger.error("", e);
            }

            return ok(views.xml.damages.render(damages));
        } else {
            return badRequest("De query is ongeldig.");
        }
    }
}
