package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.text.*;


import exceptions.DataAccessException;
import interfaces.*;
import jdbc.JDBCDataAccessProvider;
import models.*;

import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

import objects.ReservationRange;
import org.apache.http.impl.cookie.DateUtils;
import play.mvc.Controller;
import play.mvc.Result;

import play.Logger;

import objects.User;
import objects.Cost;
import utils.TimeUtils;
import views.formdata.MyCarFormData;
import views.html.carmanagement;
import views.html.carmanagementdamage;
import views.html.index;
import play.mvc.*;
import play.data.Form;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;



/**
 * This class contains the controller-functions for the view of the tab 'Car Management'.
 * It uses CarManagementModel (which is linked to DAO's) to get the needed data.
 * Written by Gilles.
 */

public class CarManagement extends Controller {
    /**
     * This method renders the correct tab (the page Car Management is divided in four tabs).
     * @param tab one of the following strings: reservations, mycar, trips, calendar
     * @return the correct tab that belongs to the given string
     */

    private static List<Form<MyCarFormData>> formList;


    @Restrict({@Group("admin"),@Group("owner")})
    public static Result carManagement(String tab) throws DataAccessException{
        if(!RoleModel.hasPermission(RoleModel.Permission.VIEW_CARMANAGEMENT)) {
            flash("error","Je hebt onvoldoende rechten om deze pagina te bekijken.");
            return badRequest(index.render("home",null, null, null, null, null, null, null, null));
        }
        User user = UserModel.getCurrentUser();
        List<MyCarFormData> formDataList = CarManagementModel.getCostFormDataList(user);
        formDataList.add(new MyCarFormData());
        formList = new ArrayList<Form<MyCarFormData>>();

        for (MyCarFormData formData : formDataList) {
            formList.add(Form.form(MyCarFormData.class).fill(formData));
        }

        return ok(carmanagement.render(tab,
                CarManagementModel.getReservationsForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),
                CarManagementModel.getRidesForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())), formList));
    }

    @Restrict({@Group("admin"),@Group("owner")})
    public static Result carManagementDamage(Integer id) throws DataAccessException{
        objects.Damage damage = null;
        objects.User user = null;
        objects.Car car = null;
        List<objects.Comment> comments = new ArrayList<>();
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()){
            context.begin();

            DamageDAO damageDAO = context.getDamageDAO();
            damage = damageDAO.getDamage(id);

            UserDAO userDAO = context.getUserDAO();
            user = userDAO.getUser(damage.getUser());

            CarDAO carDAO = context.getCarDAO();
            car = carDAO.getCar(damage.getCar());

            CommentDAO commentDAO = context.getCommentDAO();
            comments = commentDAO.getCommentsOnDamage(damage);
        }
        catch (DataAccessException e){
            flash("error", "Er is een fout opgetreden, het opzoeken van de gegevens van dit schadegeval is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return ok(carmanagementdamage.render(damage, user, car, comments));
    }

    /**
     * When a person wants to accept or decline a reservation on the tab 'Reservations', this method is called.
     * It will check which button has been pushed, if the button 'Accept' has been pushed, all selected
     * reservations will lose the state 'pending' (if they were in that state) and get a new state 'accepted'.
     * The procedure is analogous when the button 'Reject' has been pushed.
     * @return  the same webpage together with a flash message that says if the post succeeded/failed
     */
    @Restrict({@Group("admin"),@Group("owner")})
    public static Result postReservations() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        ArrayList<Integer> checkboxes = new ArrayList<>();
        //This is done in a try-catch block, to make sure the user selected reservations to process
        //If the user didn't select any, a NullPointer will be thrown up and caught here.
        try{
            for(String checkbox: map.get("selectedReservations")) checkboxes.add(Integer.valueOf(checkbox));
        } catch(NullPointerException e){
            flash("error", "Gelieve eerst reservaties te selecteren.");
            return ok(carmanagement.render("reservations",
                    CarManagementModel.getReservationsForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),
                    CarManagementModel.getRidesForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),null
            ));
        }
        // Process every selected (through a checkbox) reservation
        String action = getFirstIfExists(map, "submit");
        try {
            if ("Goedkeuren".equals(action)) {
                for (int i = 0; i < checkboxes.size(); i++) {
                    CarManagementModel.acceptReservation(checkboxes.get(i));
                }
                flash("success", "De geselecteerde reservaties zijn goedgekeurd!");
            } else if ("Afwijzen".equals(action)) {
                String reason = getFirstIfExists(map, "reason");
                for (int i = 0; i < checkboxes.size(); i++) {
                    CarManagementModel.declineReservation(checkboxes.get(i), reason);
                }
                flash("success", "De geselecteerde reservaties zijn afgekeurd!");
            } else {
                return badRequest("Deze actie is niet toegestaan.");
            }
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. De status van deze reservatie kon niet aangepast worden. Probeer het later opnieuw.");
            Logger.error("", e);
        }

        return ok(carmanagement.render("reservations",
                CarManagementModel.getReservationsForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),
                CarManagementModel.getRidesForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),null
        ));
    }

    /**
     * Very analogous to the postReservations method, only that we now work with rides instead of reservations
     * @return the same passage with a flash message saying if the post succeeded or failed
     */
    @Restrict({@Group("admin"),@Group("owner")})
    public static Result postRides() throws DataAccessException {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        ArrayList<Integer> checkboxes = new ArrayList<>();
        //This is done in a try-catch block, to make sure the user selected reservations to process
        //If the user didn't select any, a NullPointer will be thrown up and caught here.
        try{
            for(String checkbox: map.get("selectedRides")) checkboxes.add(Integer.valueOf(checkbox));
        } catch(NullPointerException e){
            flash("error", "Gelieve eerst ritgegevens te selecteren.");
            return ok(carmanagement.render("trips",
                    CarManagementModel.getReservationsForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),
                    CarManagementModel.getRidesForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),null
            ));
        }
        // Process every selected (through a checkbox) reservation
        String action = getFirstIfExists(map, "submit");
        try {
            if ("Goedkeuren".equals(action)){
                for (int i = 0; i < checkboxes.size(); i++) {
                    CarManagementModel.acceptRide(checkboxes.get(i));
                }
                flash("success", "De geselecteerde ritgegevens zijn goedgekeurd!");
            } else if ("Afwijzen".equals(action)) {
                String reason = getFirstIfExists(map, "reason");
                for (int i = 0; i < checkboxes.size(); i++) {
                    CarManagementModel.declineRide(checkboxes.get(i), reason);
                }
                flash("success", "De geselecteerde ritgegevens zijn afgekeurd!");
            } else {
                return badRequest("Deze actie is niet toegestaan.");
            }
        } catch(DataAccessException e) {
            flash("error", "Er is een fout opgetreden. De status van deze ritgegevens kon niet aangepast worden. Probeer het later opnieuw.");
            Logger.error("", e);
        }

        return ok(carmanagement.render("trips",
                CarManagementModel.getReservationsForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),
                CarManagementModel.getRidesForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),null
        ));
    }

    /**
     * Returns the first (probably only) value for a given key in a (key ->
     * value[]) map if it exists, else null.
     * Checks are added so no NullPointerException or
     * ArrayIndexOutOfBoundsException occurs.
     *
     * @param  map The map in which to search
     * @param  key The key that maps onto the requested value
     * @return The first value in the array if it exists, else null
     */
    private static <K,V> V getFirstIfExists(Map<K, V[]> map, K key) {
        V[] valueSingleton = map.get(key);
        return (valueSingleton == null || valueSingleton.length == 0) ? null : valueSingleton[0];
    }




    /**
     * Process a form submission for the Cost with the specified ID.
     * First we bind the HTTP POST data to an instance of Cost.
     * Then re-render the page.
     * @return The index page with the results of the post.
     */
    @Restrict({@Group("admin"),@Group("owner")})
    public static Result postCost(String id) throws DataAccessException {


        List<MyCarFormData> formDataList;
        List<Form<MyCarFormData>> formList = new ArrayList<Form<MyCarFormData>>();
        User user = UserModel.getCurrentUser();

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart filePart = body.getFile("proof");

        Form<MyCarFormData> sessionForm = Form.form(MyCarFormData.class).bindFromRequest();

        Cost cost;
        cost = CarManagementModel.manageCost(sessionForm.get(),user,CarManagementModel.getCarByUserId(user.getId()),filePart);
        if(cost == null){
            flash("success","Dit bewijsmateriaal is met succes verwijderd!");
        }
        else{
            flash("success","Dit bewijsmateriaal is toegevoegd!");
        }

        formDataList = CarManagementModel.getCostFormDataList(user);
        formDataList.add(new MyCarFormData());
        for(MyCarFormData formData: formDataList) {
            formList.add(Form.form(MyCarFormData.class).fill(formData));
        }
        return ok(carmanagement.render("mycar",
                CarManagementModel.getReservationsForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),
                CarManagementModel.getRidesForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())), formList));
    }


    @Restrict({@Group("admin"),@Group("owner")})
    public static Result postHour(String day) throws DataAccessException {

        Map<String,Integer> map = new HashMap<>();

        map.put("Maandag",1);
        map.put("Dinsdag",2);
        map.put("Woensdag",3);
        map.put("Donderdag",4);
        map.put("Vrijdag",5);
        map.put("Zaterdag",6);
        map.put("Zondag",7);

        Map<String,objects.ReservationRange.Day> map2 = new HashMap<>();
        map2.put("Maandag",ReservationRange.Day.MONDAY);
        map2.put("Dinsdag",ReservationRange.Day.TUESDAY);
        map2.put("Woensdag",ReservationRange.Day.WEDNESDAY);
        map2.put("Donderdag",ReservationRange.Day.THURSDAY);
        map2.put("Vrijdag",ReservationRange.Day.FRIDAY);
        map2.put("Zaterdag",ReservationRange.Day.SATURDAY);
        map2.put("Zondag",ReservationRange.Day.SUNDAY);

        Map<String, String[]> postData = request().body().asFormUrlEncoded();

        String begin = postData.get("start"+day)[0];
        String einde = postData.get("einde"+day)[0];


        String[] uurMinuut1 = begin.split(":");
        String[] uurMinuut2 = einde.split(":");

        long beginLong = ((Integer.parseInt(uurMinuut1[0]))*60*60*1000) + ((Integer.parseInt(uurMinuut1[1]))*60*1000);
        long endLong = ((Integer.parseInt(uurMinuut2[0]))*60*60*1000) + ((Integer.parseInt(uurMinuut2[1]))*60*1000);


        try{

        ReserveModel.reserveOwnCar(beginLong,endLong,map2.get(day));

        flash("success", "Jouw reservatierange is toegevoegd!");


        }catch(Exception e){
            flash("error","Het toevoegen van de reservatierange is mislukt. Onze excuses.");
        }


        return ok(carmanagement.render("mycar",
                CarManagementModel.getReservationsForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),
                CarManagementModel.getRidesForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())), formList));


    }

    @Restrict({@Group("admin"),@Group("owner")})
    public static Result changeHour(String day, String id) throws DataAccessException{

        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String begin = postData.get("start"+day+id)[0];
        String einde = postData.get("einde"+day+id)[0];

        String[] uurMinuut1 = begin.split(":");
        String[] uurMinuut2 = einde.split(":");

        long beginLong = ((Integer.parseInt(uurMinuut1[0]))*60*60*1000) + ((Integer.parseInt(uurMinuut1[1]))*60*1000);
        long endLong = ((Integer.parseInt(uurMinuut2[0]))*60*60*1000) + ((Integer.parseInt(uurMinuut2[1]))*60*1000);

        try{
        ReserveModel.changeReservationRange(Integer.parseInt(id),beginLong,endLong);
        flash("success","Jouw reservatierange is aangepast!");
        }catch(Exception e){
            flash("error","Het aanpassen van de reservatierange is mislukt. Onze excues.");
        }

        return ok(carmanagement.render("mycar",
                CarManagementModel.getReservationsForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),
                CarManagementModel.getRidesForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())), formList));


    }

    @Restrict({@Group("admin"),@Group("owner")})
    public static Result deleteHour(int id) throws DataAccessException{

        try{
        ReserveModel.deleteReservationRange(id);
        flash("success","De reservatierange is met succes verwijderd!");
        }catch(Exception e){
        flash("error","Het was helaas niet mogelijk om deze reservatierange te verwijderen");
        }


        return ok(carmanagement.render("mycar",
                CarManagementModel.getReservationsForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())),
                CarManagementModel.getRidesForCar(CarManagementModel.getCarByUserId(UserModel.getCurrentUser().getId())), formList));

    }


    @Restrict({@Group("admin"),@Group("owner")})
    public static Result newComment() throws DataAccessException {
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String damageId = postData.get("damage")[0];
        String content = postData.get("content")[0];

        int parsedDamageId = 0;
        try {
            parsedDamageId = Integer.parseInt(damageId);
        } catch(NumberFormatException e){
            Logger.error("", e);
            return badRequest("Het Damage ID is niet geldig.");
        }

        try {
            DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
            context.begin();

            DamageDAO damageDAO = context.getDamageDAO();
            objects.Damage damage = damageDAO.getDamage(parsedDamageId);

            CarDAO carDAO = context.getCarDAO();
            objects.Car car = carDAO.getCar(damage.getCar());

            objects.User user = UserModel.getCurrentUser();

            CommentDAO commentDAO = context.getCommentDAO();
            commentDAO.createComment(user, damage, content);

            // Send notification to the receiver
            User receiver = UserModel.getUserById(damage.getUser());
            String message =
                    "De eigenaar van de auto " + car.getName()
                            + " heeft een commentaar geplaatst op het schadegeval";
            if(damage.getOccurred() != null){
                message += " van " + TimeUtils.getHumanReadableDate(damage.getOccurred());
            }
            message += ".";

            String subject = "Nieuw commentaar over schadegeval";
            NotificationModel.createNotification(receiver, subject, message);

            context.commit();
        }
        catch (DataAccessException e){
            flash("error", "Er is een fout opgetreden, het aanmaken van deze comment is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.CarManagement.carManagementDamage(parsedDamageId));
    }


}
