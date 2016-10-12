package models;

import exceptions.DataAccessException;
import interfaces.*;
import jdbc.JDBCDataAccessProvider;
import objects.*;
import play.Logger;
import utils.TimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;


/**
 * This class contains method for showing information on the dashboard for all types of users.
 * @author Gilles
 */
public class DashboardModel {

    private static int MILLISECONDS_PER_HOUR = 3600000;
    private static String[] daysOfTheWeek = { "maandag", "dinsdag", "woensdag", "donderdag", "vrijdag", "zaterdag", "zondag" };

    /**
     * Returns all pending reservations belonging to a user to put on his dashboard.
     * @param u the user
     * @return a list will all pending reservations
     */
    public static List<Reservation> getPendingReservations(User u) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            ReservationDAO reservationDAO = dac.getReservationDAO();
            Filter<Reservation> filter = reservationDAO.getFilter();
            filter.fieldEquals(Reservation.Field.PENDING, true);
            filter.fieldEquals(Reservation.Field.USER, u.getId());
            filter.fieldGreaterThan(Reservation.Field.BEGIN, new Date().getTime());
            return reservationDAO.getByFilter(filter, Reservation.Field.BEGIN, false);
        }
    }

    /**
     * Return all pending reservations for a specific car, so an owner knows which reservations he still has to accept
     * @param c the car
     * @return a list of all pending reservations belonging to that car
     * @throws DataAccessException
     */
    public static List<Reservation> getPendingReservationsForCar(Car c) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            ReservationDAO reservationDAO = dac.getReservationDAO();
            Filter<Reservation> filter = reservationDAO.getFilter();
            filter.fieldEquals(Reservation.Field.PENDING, true);
            filter.fieldEquals(Reservation.Field.CAR, c.getId());
            filter.fieldGreaterThan(Reservation.Field.BEGIN, new Date().getTime());
            return reservationDAO.getByFilter(filter, Reservation.Field.BEGIN, false);
        }
    }

    /**
     * return all ride information for a specific car, so an owner knows which rides he still has to accept/reject
     * @param c the car
     * @return a list of all rides belonging to that car
     * @throws DataAccessException
     */
    public static List<Ride> getPendingRideInfoByCar(Car c) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RideDAO rideDAO = dac.getRideDAO();
            Filter<Ride> filter = rideDAO.getFilter();
            filter.fieldEquals(Ride.Field.PENDING, true);
            filter.fieldEquals(Ride.Field.CAR, c.getId());
            filter.fieldGreaterThan(Ride.Field.BEGIN, new Date().getTime());
            return rideDAO.getByFilter(filter, Ride.Field.BEGIN, false);
        }
    }

    /**
     * Return all ride information that hasn't been filled in yet.
     * @param u the user
     * @return a list will all pending ride information
     */
    public static List<Ride> getPendingRideInfo(User u) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RideDAO rideDAO = dac.getRideDAO();
            Filter<Ride> filter = rideDAO.getFilter();
            filter.fieldEquals(Ride.Field.PENDING, true);
            filter.fieldEquals(Ride.Field.USER, u.getId());
            filter.fieldGreaterThan(Ride.Field.BEGIN, new Date().getTime());
            return rideDAO.getByFilter(filter, Ride.Field.BEGIN, false);
        }
    }

    /**
     * Returns all damage documents that aren't closed  yet.
     * @param u the user
     * @return a list with all pending damage documents.
     */
    //TODO: Filter implementeren (alleen pending)
    public static List<Damage> getPendingDamages(User u) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            DamageDAO damageDAO = dac.getDamageDAO();
            return damageDAO.getDamageByUser(u);
        }
    }

    /**
     * This method will calculate stats for a specific user and converts them into strings (see the private methodes)
     * @param u the user
     * @return a list of stats converted to strings
     */
    public static List<String> getStats(User u) throws DataAccessException {
        List<Ride> rides;
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RideDAO rideDAO = dac.getRideDAO();
            rides = rideDAO.getRidesByUser(u);
        }
        List<String> stats = new ArrayList<>();
        if(rides == null) rides = new ArrayList<>();

        //Favorite day in the week
        double[] favoriteDayInTheWeek = getFavoriteDayInTheWeek(rides);

        String stringFavoriteDayInTheWeek = "<pre><div class=\"col-sm-6\">Van al uw ritten hebt u... </br>";
        for(int i = 0; i < 7; i++){
            stringFavoriteDayInTheWeek += "&#9; op " + daysOfTheWeek[i] + " <strong>" + favoriteDayInTheWeek[i] + "%</strong> gereden<br/>" ;
        }
        stringFavoriteDayInTheWeek += "</div><div class=\"col-sm-6\">" + pieChart("favDayPlot","Favoriete Dag", favoriteDayInTheWeek) + "</div></pre>";
        stats.add(stringFavoriteDayInTheWeek);

        //Numbers of hours driven per week, month and in total
        double[] numberOfHoursDriven = getNumberOfHoursDriven(rides);
        String stringNumberOfHoursDriven = "<pre>Uren gereden:<br/>";
        stringNumberOfHoursDriven += "&#9; De laatste 7 dagen: &#9;<strong>" + numberOfHoursDriven[0] + "</strong> uur<br/>";
        stringNumberOfHoursDriven += "&#9; De laatste 30 dagen: &#9;<strong>" + numberOfHoursDriven[1] + "</strong> uur<br/>";
        stringNumberOfHoursDriven += "&#9; Totale aantal: &#9;<strong>" + numberOfHoursDriven[2] + "</strong> uur<br/></pre>";
        stats.add(stringNumberOfHoursDriven);

        //Number of kilometers driven per week, month and in total
        double[] numberOfKMDriven = getNumberOfKilometersDriven(rides);
        String stringNumberOfKMDriven = "<pre>Uren gereden:<br/>";
        stringNumberOfKMDriven += "\t De laatste 7 dagen: \t<strong>" + numberOfKMDriven[0] + "</strong> km<br/>";
        stringNumberOfKMDriven += "\t De laatste 30 dagen: \t<strong>" + numberOfKMDriven[1] + "</strong> km<br/>";
        stringNumberOfKMDriven += "\t Totale aantal: \t<strong>" + numberOfKMDriven[2] + "</strong> km<br/></pre>";
        stats.add(stringNumberOfKMDriven);

        //Favorite car
        Car favoriteCar = getFavoriteCar(rides);
        if(favoriteCar != null){
            String stringFavoriteCar = "<pre>Uw heeft al het meest met <strong>" + favoriteCar.getName() + "</strong> van <strong>" + favoriteCar.getOwner().getName() + "</strong> gereden.</pre>";
            stats.add(stringFavoriteCar);
        }

        //Average number of kilometres per ride
        double averageKM = getAverageNumberOfKMPerRide(rides);
        String stringAverageKM = "<pre>U rijdt gemiddeld <strong>" + averageKM + "</strong> km per rit.</pre>";
        stats.add(stringAverageKM);

        return stats;
    }

    /**
     * This method will calculate the favorite day in the week for user u to drive with a car.
     * @param rides the list of rides
     * @return a list will the average hours driven each day in the week. (Monday = index 0, Sunday = index 6)
     */
    //TODO: requires testing
    public static double[] getFavoriteDayInTheWeek(List<Ride> rides){
        double[] hoursPerWeekDay = new double[7];
        double totalHours = 0;
        double[] fractionPerWeekDay = new double[7];
        for(Ride ride : rides){
            Date d = new Date(ride.getBegin());
            DateFormat format = new SimpleDateFormat("u");
            int indexWeekDay = Integer.parseInt(format.format(d)) - 1;
            hoursPerWeekDay[indexWeekDay] += ride.getEnd() - ride.getBegin();
            totalHours += ride.getEnd() - ride.getBegin();
        }
        if(totalHours != 0){
            for(int i = 0; i < 7; i++){
                if(totalHours != 0) fractionPerWeekDay[i] = (hoursPerWeekDay[i]/totalHours)*100;
                else fractionPerWeekDay[i] = 0;
            }
        }
        return fractionPerWeekDay;
    }

    /**
     * Generate the html of a pie chart with given values
     * @param plotId    id of the plot in the html
     * @param plotTitle the plot title
     * @param values    the values to generate the chart with
     */
    public static String pieChart(String plotId, String plotTitle, double[] values){
        String resultString = "<div style=\"width: 100%; \" id=\"" + plotId + "\"></div>";
        resultString += "<script type=\"text/javascript\">";
        resultString += "$(document).ready(function(){ $.jqplot.config.enablePlugins = true; " + plotId + " = $.jqplot('" + plotId + "', [[";
        for(int i = 0; i < values.length; i++){
            resultString += "['" + daysOfTheWeek[i] + "', " + values[i] + "]";
            if(i < values.length-1) resultString += ", ";
        }
        resultString += "]], { title: '" + plotTitle;
        resultString += "', seriesColors: [ \"#c2f2b8\", \"#9fea90\", \"#7ae264\", \"#55d83a\", \"#3abc24\", \"#20840c\", \"#2e4729\" ],"; // colours
        resultString += "legend: { show:true, location: 'e' }, seriesDefaults: {shadow: true, renderer: $.jqplot.PieRenderer, rendererOptions: { showDataLabels: true }}});";
        resultString += "window.onresize = function(event) { " + plotId + ".replot(); }";
        resultString += "});</script>";
        return resultString;
    }

    /**
     * This method will calculate the total number of hours driven for certain periods of time.
     * @param rides the list of rides
     * @return a list with three doubles: 0=hours driven this week, 1=hours driven this month, 2=total hours driven
     */
    //TODO: requires testing
    public static double[] getNumberOfHoursDriven(List<Ride> rides){
        double hoursWeek = 0; double hoursMonth = 0; double hoursTotal = 0;
        Date today = new Date();
        long todayMS = today.getTime();
        for(Ride ride : rides){
            //The ride has to be finished
            if(ride.getEnd() < todayMS){
                // If the number of miliseconds between the current moment and the begin of the ride is less than 604800000 ms, then the ride was this week.
                if(ride.getBegin() - todayMS <= 604800000f){
                    hoursWeek += ((ride.getEnd() - ride.getBegin())/MILLISECONDS_PER_HOUR);
                }
                // If the number of miliseconds between the current moment and the begin of the ride is less than 2628000000 ms, than the ride was this week
                if(ride.getBegin() - todayMS <= 2628000000f){
                    hoursMonth += ((ride.getEnd() - ride.getBegin())/MILLISECONDS_PER_HOUR);
                }
                hoursTotal += ((ride.getEnd() - ride.getBegin())/MILLISECONDS_PER_HOUR);
            }
        }
        double[] result = new double[3];
        result[0] = hoursWeek; result[1] = hoursMonth; result[2] = hoursTotal;
        return result;
    }

    /**
     * This method will calculate the total number of kilometers driven for certain periods of time.
     * @param rides the list of rides
     * @return a list with three doubles: 0=km driven this week, 1=km driven this month, 2=total km driven
     */
    //TODO: requires testing
    public static double[] getNumberOfKilometersDriven(List<Ride> rides){
        double kmWeek = 0; double kmMonth = 0; double kmTotal = 0;
        Date today = new Date();
        long todayMS = today.getTime();
        for(Ride ride : rides){
            //The ride has to be finished
            if(ride.getEnd() < todayMS && ride.getStopKM() != null && ride.getStartKM() != null){
                // If the number of miliseconds between the current moment and the begin of the ride is less than 604800000 ms, then the ride was this week.
                if(ride.getBegin() - todayMS <= 604800000f){
                    kmWeek += ride.getStopKM() - ride.getStartKM();
                }
                // If the number of miliseconds between the current moment and the begin of the ride is less than 2628000000 ms, than the ride was this week
                if(ride.getBegin() - todayMS <= 2628000000f){
                    kmMonth += ride.getStopKM() - ride.getStartKM();
                }
                kmTotal += ride.getStopKM() - ride.getStartKM();
            }
        }
        double[] result = new double[3];
        result[0] = kmWeek; result[1] = kmMonth; result[2] = kmTotal;
        return result;
    }

    /**
     * This method returns the favorite car of an user.
     * @param rides the list of rides
     * @return the favorite car
     */
    //TODO: requires testing
    public static Car getFavoriteCar(List<Ride> rides) throws DataAccessException {
        HashMap<Car, Long> hoursDrivenPerCar = new HashMap<>();
        for(Ride ride : rides){
            if(!hoursDrivenPerCar.containsKey(ride.getCar())){
                hoursDrivenPerCar.put(ride.getCar(), (ride.getEnd() - ride.getBegin())/MILLISECONDS_PER_HOUR);
            } else {
                long hours = hoursDrivenPerCar.get(ride.getCar());
                hours+=(ride.getEnd() - ride.getBegin());
                hoursDrivenPerCar.put(ride.getCar(), hours);
            }
        }
        long max = 0;
        Car favorite = null;
        for(Car c : hoursDrivenPerCar.keySet()){
            if(hoursDrivenPerCar.get(c) > max){
                max = hoursDrivenPerCar.get(c);
                favorite = c;
            }
        }
        return favorite;
    }

    /**
     * Calculate the average number of kilometers driven per ride by a specific user.
     * @param rides the list of rides
     * @return the average number
     */
    //TODO: requires testing
    public static double getAverageNumberOfKMPerRide(List<Ride> rides){
        double totalKM = 0;
        double frequencies = 0;
        for(Ride ride : rides){
            if(ride.getStartKM() != null && ride.getStopKM() != null){
                frequencies++;
                totalKM += (ride.getStopKM() - ride.getStartKM());
            }
        }
        if(frequencies != 0) return totalKM/frequencies;
        else return 0;
    }

    /**
     * Method to calculate the total number of hours driven from a list of rides
     * @param rides the list of rides
     * @return the total number of hours driven
     */
    public static long getTotalNumberOfHoursDriven(List<Ride> rides){
        long totalHours = 0;
        for(Ride ride : rides){
            totalHours += ride.getEnd() - ride.getBegin();
        }
        return totalHours/MILLISECONDS_PER_HOUR;
    }

    public static double getTotalNumberOfKMDriven(List<Ride> rides){
        long totalKM = 0;
        for(Ride ride: rides){
            if(ride.getStartKM() != null && ride.getStopKM() != null){
                totalKM += (ride.getStopKM() - ride.getStartKM());
            }
        }
        return totalKM;
    }

    public static int getTimesDrivenWithCar(List<Ride> rides, Car c) throws DataAccessException {
        int frequency = 0;
        for(Ride ride: rides){
            if(ride.getCar().equals(c)) frequency++;
        }
        return frequency;
    }

    public static int getNumberOfNewUsers() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            UserDAO userDAO = dac.getUserDAO();
            Filter<User> filter = userDAO.getFilter();
            filter.fieldGreaterThan(User.Field.TIME, TimeUtils.getDateOffset(new Date().getTime(), 0, 0, -7));
            return userDAO.getByFilter(filter).size();
        }
    }

    public static int getNumberOfPendingUsers() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            UserDAO userDAO = dac.getUserDAO();
            Filter<User> filter = userDAO.getFilter();
            filter.fieldEquals(User.Field.APPROVED, null);
            return userDAO.getByFilter(filter).size();
        }
    }

    public static int getNumberOfPendingCars() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CarDAO carDAO = dac.getCarDAO();
            Filter<Car> filter = carDAO.getFilter();
            filter.fieldEquals(Car.Field.APPROVED, null);
            return carDAO.getByFilter(filter).size();
        }
    }

    public static int getNumberOfPendingProofs() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CostDAO costDAO = dac.getCostDAO();
            Filter<Cost> filter = costDAO.getFilter();
            filter.fieldEquals(Cost.Field.APPROVED, null);
            return costDAO.getByFilter(filter).size();
        }
    }

    public static long getNumberOfTotalKMDrivenThisWeek() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RideDAO rideDAO = dac.getRideDAO();
            return rideDAO.getSumKMs(TimeUtils.getDateOffset(new Date().getTime(), 0, 0, -7));
        }
    }

    public static long getNumberOfTotalKMDrivenThisMonth() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RideDAO rideDAO = dac.getRideDAO();
            return rideDAO.getSumKMs(TimeUtils.getDateOffset(new Date().getTime(), 0, -1, 0));
        }
    }

    public static long getNumberOfTotalKMDrivenOverall() throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RideDAO rideDAO = dac.getRideDAO();
            return rideDAO.getSumKMs();
        }
    }

}
