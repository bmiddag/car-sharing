package controllers;

import interfaces.CarDAO;
import interfaces.CommentDAO;
import interfaces.DamageDAO;
import interfaces.DataAccessContext;
import interfaces.Filter;
import interfaces.UserDAO;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdbc.JDBCDataAccessProvider;

import models.NotificationModel;
import models.ReserveModel;
import models.ReserveModel.Moment;
import models.RideWrapper;
import models.RoleModel;
import models.UserModel;

import objects.Car;
import objects.Reservation;
import objects.User;
import objects.Zone;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;

import utils.TimeUtils;

import views.html.index;
import views.html.reserve;
import views.html.reserve_damage;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import exceptions.DataAccessException;

/**
 * Controller for the reserve.scala.html view
 *
 * @author Wouter Pinnoo
 * @author Stijn Seghers
 */
public class Reserve extends Controller {
    /**
     * Renderer for the reserve.scala.html view
     *
     * @param  tab The current tab in this view
     * @return The rendered view
     * @throws DataAccessException
     * @see    models.ReserveModel.getRideWrappers
     * @see    models.ReserveModel.getReservations
     * @see    views.html.reserve
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result index(String tab) throws DataAccessException {
        // Check if user has the right permissions
        if(!RoleModel.hasPermission(RoleModel.Permission.VIEW_RESERVATIONS)) {
            flash("error","Je hebt onvoldoende rechten om deze pagina te bekijken.");
            return badRequest(index.render("home",null, null, null, null, null, null, null, null));
        }

        // Get necessary data from db
        User currentUser = UserModel.getCurrentUser();
        List<Moment> unavailableMoments = ReserveModel.getUnavailableMoments();
        List<RideWrapper> rideWrappers = ReserveModel.getRideWrappers(currentUser);
        List<Reservation> reservations = ReserveModel.getReservations();
        List<Reservation> reservationsWithoutRide = ReserveModel.getReservationsWithoutRide();
        List<Zone> zones = ReserveModel.getZones();
        Map<String, String[]> extraData = new HashMap<>();

        // Show info flash for reserve tab
        if (tab == "reserve") {
            flash("info", "In deze kalender kan je zoeken wanneer en welke"
                    + " auto's er vrij zijn. Een groene kleur betekent dat die"
                    + " dag een auto beschikbaar is; een rode kleur dat er"
                    + " geen beschikbaar is. De geelgekleurde dag is vandaag."
                    + " Aan de rechterkant kan je beperkingen opleggen op de"
                    + " auto's.<br /><br />"
                    + "Je zoekt een auto door eerst een periode te selecteren"
                    + " in de kalender. (Klik op een startdag en sleep naar de"
                    + " einddag.) Vervolgens geef je een start- en einduur in"
                    + " onderaan de pagina en klik je op de auto die je wilt"
                    + " reserveren. ");
        }

        // Render page
        return ok(reserve.render(tab, unavailableMoments, rideWrappers,
                reservationsWithoutRide, reservations, currentUser, zones,
                extraData));
    }


    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result damage(Integer id) throws DataAccessException{
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
        return ok(reserve_damage.render(damage, user, car, comments));
    }


    /**
     * Renders the reserve.scala.html view, but applies the car filters first.
     *
     * @return The rendered view
     * @throws DataAccessException
     * @see    Reserve.index
     * @see    views.html.reserve
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result filter() throws DataAccessException {
        // Parse filters
        Request request = request();
        // TODO use duration_days and duration_hours in filter
        String duration_days = request.getQueryString("duration_days");
        String duration_hours = request.getQueryString("duration_hours");
        String car_name = request.getQueryString("car_name");
        String seats = request.getQueryString("seats");
        String zone = request.getQueryString("zone");
        String has_towbar = request.getQueryString("has_towbar");
        String has_gps = request.getQueryString("has_gps");

        // Construct filter with parameters
        Filter<Car> filter = makeCarFilter(car_name, seats, zone, has_towbar, has_gps);

        // Get necessary data from db
        User currentUser = UserModel.getCurrentUser();
        List<Moment> unavailableMoments = ReserveModel.getUnavailableMoments(filter);
        List<RideWrapper> rideWrappers = new ArrayList<>();
        List<Reservation> reservations = ReserveModel.getReservations();
        List<Reservation> reservationsWithoutRide = ReserveModel.getReservationsWithoutRide();
        List<Zone> zones = ReserveModel.getZones();
        Map<String, String[]> extraData = request.queryString();

        // Render page
        return ok(reserve.render("reserve", unavailableMoments, rideWrappers,
                reservationsWithoutRide, reservations, currentUser, zones,
                extraData));
    }

    /**
     * Get the reservations made and renders it to an xml
     *
     * @param  startEpoch Start of the interval (in epoch)
     * @param  endEpoch   End of the interval (in epoch)
     * @return The rendered xml that contains the reservations
     * @throws DataAccessException
     * @see    models.ReserveModel.getReservations
     * @see    views.xml.reservations
     */
    public static Result getReservations(long start, long end) throws DataAccessException {
        List<Reservation> reservations = ReserveModel.getReservations(start, end);
        return ok(views.xml.reservations.render(reservations));
    }

    /**
     * Get the available cars and renders it to an xml
     *
     * @param  from         Start of the interval (in epoch)
     * @param  to           End of the interval (in epoch)
     * @param  filterString url encoded query string representing the car filters
     * @return The rendered xml that contains the available cars
     * @throws DataAccessException
     * @see    views.xml.cars
     */
    public static Result getAvailableCars(long from, long to, String filterString) throws DataAccessException {
        // Parse filterString to a String->String map
        Map<String, String> filterMap = queryToMap(filterString);
        // Construct a filter from the map
        Filter<Car> filter = makeCarFilter(filterMap.get("car_name"),
                                           filterMap.get("seats"),
                                           filterMap.get("zone"),
                                           filterMap.get("has_towbar"),
                                           filterMap.get("has_gps"));
        filter.fieldEquals(Car.Field.APPROVED, true);        
        
        long duration;
        if ("on".equals(filterMap.get("use_duration"))) {
            duration = (Integer.parseInt(filterMap.get("duration_days")) * 24 + Integer.parseInt(filterMap.get("duration_hours"))) * 60 * 60 * 1000;
        } else {
            duration = to - from;
        }

        // Get available cars from db
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CarDAO dao = context.getCarDAO();
            List<CarDAO.PossibleReservation> pcars = dao.getAvailableCars(from, to, duration, filter);
            
            // Render page
            return ok(views.xml.pcars.render(pcars));
        }
    }



    /**
     * This method is used to commit a reservation in the database from the
     * view. The view reserve.scala.html will post the parameters of the
     * reservation to this method.
     *
     * @return The homepage of reserve.scala.html
     * @throws DataAccessException
     */
    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
    public static Result reserve() throws DataAccessException {
        // Parse the data from the POST request
        Map<String, String[]> postData = request().body().asFormUrlEncoded();
        int carId = Integer.parseInt(postData.get("carModalCarId")[0]);
        long start = Long.parseLong(postData.get("carModalStart")[0]);
        long end = Long.parseLong(postData.get("carModalEnd")[0]);

        try {
            // Put reservation in db
            ReserveModel.reserve(carId, start, end);

            // Show success flash message.
            DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
            String startDateString = dateformat.format(new Date(start));
            String endDateString = dateformat.format(new Date(end));

            DateFormat timeformat = new SimpleDateFormat("HH:mm");
            String startTimeString = timeformat.format(new Date(start));
            String endTimeString = timeformat.format(new Date(end));

            flash("success", "Je hebt succesvol gereserveerd van "
                             + startDateString
                             + " om "
                             + startTimeString
                             + " tot "
                             + endDateString
                             + " om "
                             + endTimeString
                             + ". Je kan uw reservaties bekijken in het tabblad \"Mijn reservaties\".");
        } catch (DataAccessException | NumberFormatException e){
            flash("error", "Er is een fout opgetreden. Jouw reservatie is niet gelukt. Probeer later opnieuw.");
            Logger.error("", e);
        }

        // Render reserve page
        return index("reserve");
    }

    /**
     * Makes a filter object from the given parameters.
     *
     * @param  car_name   A car name to satisfy
     * @param  seats      The minimum number of seats the car must have
     * @param  zone       The zone in which the car must be
     * @param  has_towbar Whether the car must have a towbar
     * @param  has_gps    Whether the car must have a gps
     * @return The filter object
     * @throws DataAccessException
     */
    private static Filter<Car> makeCarFilter(String car_name,
            String seats, String zone, String has_towbar, String has_gps)
            throws DataAccessException {
        // Get an empty car filter
        Filter<Car> filter = ReserveModel.getCarFilter();

        // Check every field if it's not null or empty, else apply it to the
        // filter.
        if (car_name != null && ! car_name.isEmpty())
            filter.fieldEquals(Car.Field.NAME, car_name);
        if (seats != null && ! seats.isEmpty()) {
            try {
                filter.fieldGreaterThanOrEquals(Car.Field.CAPACITY, Integer.parseInt(seats));
            } catch (NumberFormatException e) {
                // Do nothing.
            }
        }
        if (zone != null && ! zone.isEmpty())
            try {
                filter.fieldEquals(Car.Field.ZONE, Integer.parseInt(zone));
            } catch (NumberFormatException e) {
                // Do nothing.
            }
        if (has_towbar != null && ! has_towbar.isEmpty())
            filter.fieldEquals(Car.Field.TOW, "on".equals(has_towbar));
        if (has_gps != null && ! has_gps.isEmpty())
            filter.fieldEquals(Car.Field.GPS, "on".equals(has_gps));

        return filter;
    }

    /**
     * Makes a String-to-String mapping from a query string of the form
     * "?name=value&name=value&...".
     *
     * @param  query The query string to parse
     * @return The constructed mapping
     */
    private static Map<String, String> queryToMap(String query) {
        // Make a mapping to be filled
        Map<String, String> map = new HashMap<String, String>();
        // Split the query string on the "&"
        String[] pairs = query.split("&");

        // For every name-value pair, split on "=", url decode it and put it in
        // the mapping.
        for (String pair : pairs) {
            try {
                int index = pair.indexOf("=");
                String name = URLDecoder.decode(pair.substring(0, index), "UTF-8");
                String value = URLDecoder.decode(pair.substring(index + 1), "UTF-8");
                map.put(name, value);
            } catch (Exception e) {
                // Do nothing. This should only happen with maliciously formed
                // query strings.
            }
        }

        return map;
    }

    @Restrict({@Group("admin"),@Group("owner"),@Group("user")})
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
        return redirect(routes.Reserve.index("trips"));
    }

}
