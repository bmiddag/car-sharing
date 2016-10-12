package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import exceptions.DataAccessException;
import interfaces.*;
import jdbc.JDBCDataAccessProvider;
import objects.DamageDoc;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

/**
 * Created by wouter on 1/04/14.
 */
public class File extends Controller {


    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result deleteFile() throws DataAccessException {
        // Get the data from the POST request
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String id = postData.get("picsModalDeleteId")[0];
        String type = postData.get("picsModalDeleteType")[0];

        try {
            int parsedId = Integer.parseInt(id);

            DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
            context.begin();

            if(type.equals("damageDoc")){
                DamageDocDAO damageDocDAO = context.getDamageDocDAO();
                damageDocDAO.deleteDamageDoc(damageDocDAO.getDamageDoc(parsedId));
            } else if (type.equals("refuelingDoc")){
                RefuelingDAO refuelingDAO = context.getRefuelingDAO();
                FileDAO fileDAO = context.getFileDAO();

                objects.Refueling refueling = refuelingDAO.getRefueling(parsedId);
                Integer fileId = refueling.getProof();
                refueling.setProof(null);
                refuelingDAO.updateRefueling(refueling);

                fileDAO.deleteFile(fileId);
            }
            context.commit();
        }
        catch (DataAccessException | NumberFormatException e){
            flash("error", "Er is een fout opgetreden, het verwijderen van dit bestand is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }
}
