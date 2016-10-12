package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import interfaces.CarDAO;
import interfaces.DataAccessContext;
import interfaces.ReservationDAO;
import interfaces.RideDAO;

import java.text.ParseException;
import java.util.Map;

import jdbc.JDBCDataAccessProvider;

import models.NotificationModel;
import models.UserModel;

import objects.Reservation;
import objects.User;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import utils.TimeUtils;

import exceptions.DataAccessException;

/**
 * Controller for the rides view.
 *
 * @author Wouter Pinnoo
 * @author Stijn Seghers
 */
public class Ride extends Controller {

    /**
     * Views will send a post request to this method to update the details of a ride.
     *
     * @return A redirection to the rides page
     * @throws exceptions.DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result updateRide() throws DataAccessException {
        // Get the data from the POST request
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String value = postData.get("value")[0];
        String name = postData.get("name")[0];
        String rideID = postData.get("pk")[0];

        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            int parsedRideId = Integer.parseInt(rideID);
            double parsedValue = Double.parseDouble(value);

            context.begin();
            RideDAO dao = context.getRideDAO();
            objects.Ride ride = dao.getRide(parsedRideId);

            if (ride.getApproved() != null) {
                String msg = "Het is niet mogelijk een rit aan te passen die al verwerkt is. Deze rit werd reeds ";
                msg += ride.getApproved() ? "goedgekeurd." : "afgekeurd.";
                flash("error", msg);
            } else {
                switch(name) {
                    case "km-start":
                        if(parsedValue > ride.getStopKM()){
                            return badRequest("De km stand bij het vertrek moet kleiner zijn dan bij het toekomen.");
                        }
                        dao.updateRideStartKM(parsedRideId, parsedValue);
                        // Send notification to the owner
                        User user = UserModel.getCurrentUser();
                        String message =
                            "Een lener van " + ride.getCar().getName()
                            + " heeft gegevens ingezonden over een rit"
                            + " van " + TimeUtils.getHumanReadableDate(ride.getBegin())
                            + " tot " + TimeUtils.getHumanReadableDate(ride.getEnd())
                            + ". Gelieve deze ritgegevens zo snel mogelijk goed of af te keuren.";
                        String phone = user.getPhone();
                        if (phone != null && !phone.isEmpty()) {
                            message += " Als je het niet eens bent of vragen hebt, kan je "
                                + "de lener bereiken op het nummer " + phone + ".";
                        }
                        String subject = "Ritgegevens van " + user.getName();
                        NotificationModel.createNotification(ride.getCar().getOwner(), subject, message);
                        break;
                    case "km-end":
                        if(parsedValue < ride.getStartKM()){
                            return badRequest("De km stand bij het toekomen moet groter zijn dan bij het vertrekken.");
                        }
                        dao.updateRideStopKM(parsedRideId, parsedValue);
                        break;
                }
                context.commit();
            }
        } catch (DataAccessException | NumberFormatException e) {
            flash("error", "Er is een fout opgetreden, het aanpassen van de "
                    + "ritgegevens is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }

    /**
     * Views will send a post request to this method to add a new ride.
     *
     * @return A redirection to the rides page
     * @throws exceptions.DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result addRide() throws DataAccessException {
        // Get the data from the POST request
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String carId = postData.get("carId")[0];
        String start = postData.get("start")[0];
        String end = postData.get("end")[0];

        int parsedCarId;
        try {
            parsedCarId = Integer.parseInt(carId);
        } catch(NumberFormatException e){
            flash("error", "Je hebt geen geldige auto opgegeven. Gelieve opnieuw te proberen.");
            return redirect(routes.Reserve.index("trips"));
        }

        long startEpoch;
        long endEpoch;
        try {
            startEpoch = TimeUtils.stringToLong("dd/MM/yyyy HH:mm", start);
            endEpoch = TimeUtils.stringToLong("dd/MM/yyyy HH:mm", end);
        } catch(NumberFormatException | ParseException e){
            flash("error", "De begin- of einddatum dat je opgaf voor de rit is ongeldig. Gelieve opnieuw te proberen.");
            return redirect(routes.Reserve.index("trips"));
        }

        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CarDAO carDAO = context.getCarDAO();
            objects.Car car = carDAO.getCar(parsedCarId);

            addRide(context, car, startEpoch, endEpoch);
        } catch (DataAccessException e) {
            flash("error", "Er is een fout opgetreden, toevoegen van de rit is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        flash("success", "Proficiat, de rit is succesvol aangemaakt! Klik links op de nieuwe rit om de ritgegevens in te vullen.");
        return redirect(routes.Reserve.index("trips"));
    }

    /**
     * Views will send a post request to this method to add a new ride, based on a reservation.
     *
     * @return A redirection to the rides page
     * @throws exceptions.DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result addRideFromReservation() throws DataAccessException {
        // Get the data from the POST request
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        String reservationId = postData.get("reservationId")[0];

        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            ReservationDAO reservationDAO = context.getReservationDAO();

            Reservation reservation = reservationDAO.getReservation(Integer.parseInt(reservationId));
            User user = reservation.getUser();
            objects.Car car = reservation.getCar();

            addRide(context, car, reservation.getBegin(), reservation.getEnd());
        } catch (DataAccessException | NumberFormatException e) {
            flash("error", "Er is een fout opgetreden, toevoegen van de rit is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }
        return redirect(routes.Reserve.index("trips"));
    }

    /**
     * Help method to add a ride to the database.
     *
     * @param  context The database connection
     * @param  car     The car with which the ride has been made
     * @param  start   The start time of the ride (in ms since epoch)
     * @param  end     The end time of the ride (in ms since epoch)
     * @throws exceptions.DataAccessException
     */
    private static void addRide(DataAccessContext context, objects.Car car,
            long start, long end) throws DataAccessException {

        context.begin();

        User user = UserModel.getCurrentUser();

        RideDAO rideDAO = context.getRideDAO();
        objects.Ride ride = rideDAO.createRide(user, car);
        ride.setBegin(start);
        ride.setEnd(end);
        ride.setStartKM(0.);
        ride.setStopKM(0.);

        rideDAO.updateRide(ride);

        context.commit();
    }
}
