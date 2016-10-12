package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import exceptions.DataAccessException;
import interfaces.*;
import jdbc.JDBCDataAccessProvider;
import objects.*;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Controller for refuelings
 */
public class Refueling extends Controller {


    /**
     * Views will send a post request to this method to update the details of a refueling
     * @return
     * @throws exceptions.DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result updateRefueling() throws DataAccessException {
        // Get the data from the POST request
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String value = postData.get("value")[0];
        String name = postData.get("name")[0];
        String refuelingId = postData.get("pk")[0];
        int parsedRefuelingId = Integer.parseInt(refuelingId);

        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            context.begin();
            RefuelingDAO dao = context.getRefuelingDAO();

            switch(name){
                case "type":
                    dao.updateRefuelingType(value, parsedRefuelingId);
                    break;
                case "litre":
                    Double parsedValue = Double.parseDouble(value);
                    dao.updateRefuelingLitre(parsedValue, parsedRefuelingId);
                    break;
                case "price":
                    int parsedIntValue = (int)Math.round(Double.parseDouble(value)*100);
                    dao.updateRefuelingPrice(parsedIntValue, parsedRefuelingId);
                    break;
            }

            context.commit();
        } catch (DataAccessException | NumberFormatException e){
            flash("error", "Er is een fout opgetreden, het aanpassen van de ritgegevens is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }

    /**
     * Views will send a post request to this method to add a new refueling
     * @return
     * @throws DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result newRefueling() throws DataAccessException {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart filePart = body.getFile("proof");

        Map<String, String[]> postData = body.asFormUrlEncoded();
        String type = postData.get("type")[0];
        String litre = postData.get("litre")[0];
        String price = postData.get("price")[0];
        String rideId = postData.get("newRefuelingRideId")[0];

        try {
            double parsedLitre = Double.parseDouble(litre);
            int parsedPrice = (int)Math.round(Double.parseDouble(price)*100);
            int parsedRideId = Integer.parseInt(rideId);

            DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
            context.begin();
            RefuelingDAO refuelingDAO = context.getRefuelingDAO();

            objects.Refueling refueling = refuelingDAO.createRefueling(parsedRideId, parsedPrice, parsedLitre, type);

            if(filePart != null){
                FileDAO fileDAO = context.getFileDAO();
                objects.File file = fileDAO.createFile(new FileInputStream(filePart.getFile()));
                refueling.setProof(file.getId());

                refuelingDAO.updateRefueling(refueling);
            }

            context.commit();
        }
        catch (DataAccessException | NumberFormatException | IOException e){
            flash("error", "Er is een fout opgetreden, het aanpassen van de ritgegevens is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result extraRefuelingFile() throws DataAccessException {
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

            RefuelingDAO refuelingDAO = context.getRefuelingDAO();
            objects.Refueling refueling = refuelingDAO.getRefueling(parsedId);

            for(Http.MultipartFormData.FilePart fp : files) {
                FileDAO fileDAO = context.getFileDAO();

                objects.File file = fileDAO.createFile(new FileInputStream(fp.getFile()));

                refueling.setProof(file.getId());
                refuelingDAO.updateRefueling(refueling);
            }

            context.commit();
        }
        catch (DataAccessException | NumberFormatException | IOException e){
            flash("error", "Er is een fout opgetreden, het aanpassen van de ritgegevens is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }

    /**
     * Views will send a post request to this method to delete a refueling
     * @return
     * @throws DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result deleteRefueling() throws DataAccessException {
        // Get the data from the POST request
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String id = postData.get("refuelingId")[0];

        try {
            DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
            context.begin();
            RefuelingDAO dao = context.getRefuelingDAO();

            dao.deleteRefueling(Integer.parseInt(id));

            context.commit();
        }
        catch (DataAccessException | NumberFormatException e){
            flash("error", "Er is een fout opgetreden, het aanpassen van de ritgegevens is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }
}
