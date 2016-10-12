package models;

import interfaces.*;
import interfaces.ReservationRangeDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import jdbc.JDBCDataAccessProvider;

import objects.*;

import play.Logger;
import play.core.Router;
import play.mvc.Call;

import utils.TimeUtils;

import exceptions.DataAccessException;

/**
 * Model for the reservations and rides.
 *
 * @author Wouter Pinnoo
 * @author Stijn Seghers
 */
public class ReserveModel {
    /**
     * Gets the rides of a user, with all relevant information about those
     * rides (e.g. refuelings, damage proofs, etc.)
     *
     * @param u The driver
     * @return A list of all rides, with all relevant information about those rides.
     */
    public static List<RideWrapper> getRideWrappers(User u) throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RideDAO rideDAO = context.getRideDAO();
            RefuelingDAO refuelingDAO = context.getRefuelingDAO();
            DamageDAO damageDAO = context.getDamageDAO();
            DamageDocDAO damageDocDAO = context.getDamageDocDAO();

            List<RideWrapper> result = new ArrayList<>();

            List<Ride> rides = rideDAO.getRidesByUser(u);

            for(Ride r : rides){
                RideWrapper wrapper = new RideWrapper();
                wrapper.ride = r;
                wrapper.refuelings = refuelingDAO.getRefuelingsByRide(r);

                wrapper.damages = new HashMap<>();
                List<Damage> list = damageDAO.getDamageByRide(r);
                for(Damage d : list){
                    wrapper.damages.put(d, damageDocDAO.getDamageDocByDamage(d));
                }

                result.add(wrapper);
            }

            return result;
        }
    }

    /**
     * Gets all reservations of a user.
     *
     * @return A list of all reservations
     * @see    interfaces.ReservationDAO.getReservations
     */
    public static List<Reservation> getReservations() throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            ReservationDAO dao = context.getReservationDAO();
            return dao.getReservations(UserModel.getCurrentUser());
        }
    }

    /**
     * Gets all reservations that don't have ride details
     * @return
     * @throws DataAccessException
     */
    public static List<Reservation> getReservationsWithoutRide() throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            ReservationDAO dao = context.getReservationDAO();
            return dao.getReservationsWithoutRide(UserModel.getCurrentUser().getId());
        }
    }

    /**
     * Gets the reservations of a user within a specific time lapse.
     *
     * @param  start The start of the time lapse
     * @param  end The end of the time lapse
     * @return A list of the reservations
     * @see    interfaces.ReservationDAO.getReservations
     */
    public static List<Reservation> getReservations(long start, long end) throws DataAccessException {
        // TODO take start and end into account
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            ReservationDAO dao = context.getReservationDAO();
            return dao.getReservations(UserModel.getCurrentUser());
        }
    }

    /**
     * Returns a list of all zones.
     *
     * @return the zones
     * @throws DataAccessExeption
     */
    public static List<Zone> getZones() throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            ZoneDAO dao = context.getZoneDAO();
            return dao.getAllZones();
        }
    }

    /**
     * Gets a car filter object from the DAO.
     *
     * @see interfaces.CarDAO
     * @see interfaces.Filter
     */
    public static Filter<Car> getCarFilter() throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            return context.getCarDAO().getFilter();
        }
    }

    /**
     * Gets a chronologically ordered list of moments when no car is available
     * for hire.
     *
     * See {@link getUnavailableMoments(DataAccessContext, Filter<Car>)}
     * for more in-depth information.
     *
     * @return A list of unavailable periods
     * @see    interfaces.CarDAO
     */
    public static List<Moment> getUnavailableMoments() throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            return getUnavailableMoments(context, context.getCarDAO().getFilter());
        }
    }

    /**
     * Gets a chronologically ordered list of moments when no car (with the
     * applied filters) is available for hire.
     *
     * See {@link getUnavailableMoments(DataAccessContext, Filter<Car>)}
     * for more in-depth information.
     *
     * @param  carFilter A filter which selects only a certain kind of cars
     * @return A list of unavailable periods
     * @see    interfaces.CarDAO
     */
    public static List<Moment> getUnavailableMoments(Filter<Car> carFilter) throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            carFilter.fieldEquals(Car.Field.APPROVED, true);
            return getUnavailableMoments(context, carFilter);
        }
    }

    /**
     * Gets a chronologically ordered list of moments when no car (with the
     * applied filters) is available for hire.
     *
     * The following algorithm is used.
     * <pre>
     * (1) Create an array which initially holds one element: the entire
     *     period.
     * (2) For every car that fits the filters and for as long as the array is
     *     not empty (i.e. there are still periods that aren't available):
     *     (2.1) For every period between reservations and for as long as the
     *           array is not empty:
     *           (2.1.1) Search for an overlay or multiple overlays between the
     *                   period and a period in the array so far.
     *           (2.1.2) Remove the overlay in the array.
     * (3) Return the array.
     * </pre>
     *
     * @param  context   The database connection object to use
     * @param  carFilter A filter which selects only a certain kind of cars
     * @return A list of unavailable periods
     * @see    interfaces.CarDAO
     */
    private static List<Moment> getUnavailableMoments(
            DataAccessContext context, Filter<Car> carFilter)
            throws DataAccessException {

        CarDAO carDAO = context.getCarDAO();

        List<Moment> unavailableMoments = new ArrayList<>();
        unavailableMoments.add(new Moment(new Date(1), new Date(Long.MAX_VALUE - 1)));

        for (Car car : carDAO.getCars(carFilter)) {

            ListIterator<Moment> unavailableIt = unavailableMoments.listIterator();
            if (! unavailableIt.hasNext())
                break;
            Moment unavailableMoment = unavailableIt.next();

            try {
                for (Moment availableMoment : getAvailableMoments(context, car)) {
                    // Iterate both lists until there is an overlay
                    while (unavailableMoment.getEnd().before(availableMoment.getStart()))
                        unavailableMoment = unavailableIt.next();
                    if (availableMoment.getEnd().before(unavailableMoment.getStart()))
                        continue;

                    // At this point, we know for certain that there is an
                    // overlay. Let's remove it from 'unavailableMoments'.
                    // There are four cases.
                    if (! availableMoment.getStart().after(unavailableMoment.getStart())) {
                        if (availableMoment.getEnd().before(unavailableMoment.getEnd())) {
                            // Case (1)
                            unavailableMoment.setStart(availableMoment.getEnd());
                        } else {
                            // Case (2)
                            unavailableIt.remove();
                            unavailableMoment = unavailableIt.next();
                        }
                    } else {
                        if (availableMoment.getEnd().before(unavailableMoment.getEnd())) {
                            // Case (3)
                            Date unavailableEnd = unavailableMoment.getEnd();
                            unavailableMoment.setEnd(availableMoment.getStart());
                            unavailableIt.add(new Moment(availableMoment.getEnd(), unavailableEnd));
                            unavailableMoment = unavailableIt.previous();
                        } else {
                            // Case (4)
                            unavailableMoment.setEnd(availableMoment.getStart());
                        }
                    }
                }
            } catch (NoSuchElementException e) {
                // Ignore. This means we reached the end of the list of
                // unavailable moments. (Using try-catch instead of calls
                // to hasNext & breaks, makes it much more readable.)
            }
        }

        return unavailableMoments;
    }

    /**
     * Returns the available (i.e. unreservered) periods for a given car.
     * Actually transforms the list of reservation into its complement.
     *
     * @param  car The car for which the available periods are returned
     * @return A list of available periods
     * @throws DataAccessException
     */
    private static List<Moment> getAvailableMoments(DataAccessContext context, Car car)
                                                    throws DataAccessException {

        ReservationDAO reservationDAO = context.getReservationDAO();
        List<Moment> available = new ArrayList<>();

        // TODO sort reservations & only from now on
        //      Date now = new Date(System.currentTimeMillis());
        // TODO order reservations in db instead of here
        List<Reservation> reservations = reservationDAO.getReservations(car);
        Collections.sort(reservations, new ReservationComparator());

        Date previousEnd = new Date(0);
        for (Reservation reservation : reservations) {
            available.add(new Moment(previousEnd, new Date(reservation.getBegin())));
            previousEnd = new Date(reservation.getEnd());
        }
        available.add(new Moment(previousEnd, new Date(Long.MAX_VALUE)));

        return available;
    }

    /**
     * Makes a reservation in the db for the car with the given "carId" and for
     * the given time range.
     *
     * @param carId The id of the car for which to make a reservation
     * @param start The start of the reservation
     * @param end   The end of the reservation
     * @throws DataAccessException
     */
    public static void reserve(int carId, long start, long end) throws DataAccessException {
        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            context.begin();

            ReservationDAO reservationDAO = context.getReservationDAO();
            CarDAO carDAO = context.getCarDAO();

            Car car = carDAO.getCar(carId);
            int id = reservationDAO.createReservation(UserModel.getCurrentUser(), car, start, end).getId();
            context.commit();
            if(car.getOwner().getId() == UserModel.getCurrentUser().getId())
                CarManagementModel.acceptReservation(id);
            // Send notification to the borrower
            User user = UserModel.getCurrentUser();
            String url = controllers.routes.CarManagement.carManagement("reservations").url();
            String message =
                user.getName() + " vraagt een reservatie aan voor " + car.getName()
                + " van " + TimeUtils.getHumanReadableDate(start)
                + " tot " + TimeUtils.getHumanReadableDate(end)
                + ". Gelieve deze reservatie <a href=\"" + url + "\">goed of af te keuren</a>.";
            String phone = user.getPhone();
            if (phone != null && !phone.isEmpty()) {
                message += " Mocht u verdere afspraken willen maken kunt u "
                    + user.getName() + " bereiken op het nummer " + phone + ".";
            }
            String subject = "Reservatieaanvraag van " + user.getName();
            NotificationModel.createNotification(car.getOwner(), subject, message);
        }
    }

    public static void reserveOwnCar(long start, long end, objects.ReservationRange.Day day) throws DataAccessException{

        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {

            context.begin();
            ReservationRangeDAO reservationRangeDAO = context.getReservationRangeDAO();
            Car car = UserModel.getCarOwnedByUser(UserModel.getCurrentUser().getId());
 
            reservationRangeDAO.createReservationRange(car, start,end,day);

            context.commit();

        }

    }

    public static List<ReservationRange> getReservationRangesPerDay(objects.ReservationRange.Day day) throws DataAccessException{

        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {

            context.begin();
            ReservationRangeDAO reservationRangeDAO = context.getReservationRangeDAO();
            Car car = UserModel.getCarOwnedByUser(UserModel.getCurrentUser().getId());

            return reservationRangeDAO.getReservationRanges(car.getId(),day);
        }

    }

    public static void changeReservationRange(int id, long start, long end) throws DataAccessException{

        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {

            context.begin();
            ReservationRangeDAO reservationRangeDAO = context.getReservationRangeDAO();
            ReservationRange rr = reservationRangeDAO.getReservationRange(new Integer(id));
            rr.setBegin(new Long(start));
            rr.setEnd(new Long(end));
            reservationRangeDAO.updateReservationRange(rr);
            context.commit();

        }


    }

    public static void deleteReservationRange(int id) throws DataAccessException{

        try (DataAccessContext context = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {

            context.begin();
            ReservationRangeDAO reservationRangeDAO = context.getReservationRangeDAO();
            reservationRangeDAO.deleteReservationRange(new Integer(id));
            context.commit();
        }

    }




    public static String convertLongtoHour(long time){



        double value = (double)(time/(1000.0*60*60));

        String hours;
        if(value<10){
        hours = "0"+String.valueOf((int)Math.floor(value));
        }
        else{
        hours = String.valueOf((int)Math.floor(value));
        }
        double number = value - (int)Math.floor(value);
       
        String minutes = "";
        int minute = (int)(Math.round(number*60));

        if(minute >= 10){
        minutes=String.valueOf(minute);
        }
        else if(minute < 10 && minute > 0){
        minutes="0"+String.valueOf(minute);
        }
        else{
        minutes = "00";
        }
        return hours+":"+minutes;

    }

    /**
     * Representation of a period of time. This class is merely a wrapper for
     * two Date objects.
     */
    public static class Moment {
        private Date start, end;

        Moment(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }

        /**
         * For debugging purposes.
         */
        public String toString() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            int startHours = calendar.get(Calendar.HOUR_OF_DAY);
            int startMinutes = calendar.get(Calendar.MINUTE);
            calendar.setTime(end);
            int endHours = calendar.get(Calendar.HOUR_OF_DAY);
            int endMinutes = calendar.get(Calendar.MINUTE);
            return start + " " + startHours + ":" + startMinutes + " tot "
                    + end + " " + endHours + ":" + endMinutes;
        }
    }

    /**
     * A comparator for reservations, using the start time to compare. This
     * is, of course, only correct if the reservations don't overlay.
     */
    private static class ReservationComparator implements Comparator<Reservation> {
        @Override
        public int compare(Reservation reservation1, Reservation reservation2) {
            return (int) (reservation1.getBegin() - reservation2.getBegin());
        }
    }
}
