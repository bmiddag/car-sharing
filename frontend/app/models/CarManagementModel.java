package models;

import com.mysql.jdbc.MySQLConnection;
import controllers.Reserve;
import interfaces.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import jdbc.JDBCDataAccessProvider;

import objects.*;

import views.formdata.MyCarFormData;
import static play.mvc.Controller.session;

import objects.User;
import play.Logger;
import play.mvc.Http;

import utils.TimeUtils;
import java.io.FileInputStream;

import java.lang.Math;

import exceptions.DataAccessException;



/**
 * This class contains the link with the DAO's (see DBModule), who on their turn contain the link with the data in the DB.
 * The methods in this class are straight-forward, given their names.
 * @author Gilles Vandewiele
 */
public class CarManagementModel {



    /**
     * Uses the DAO to get the Cost that corresponds with the specified ID.
     *
     * @param  id   the ID of the Cost
     * @return      the Cost with the specified ID
     * @see         objects.Cost
     */
    public static Cost getCostById(int car,int id) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CostDAO costDAO = dac.getCostDAO();
            return costDAO.getCost(car,id);
        }
    }

    /**
     * Uses the DAO to get the Car that corresponds with the specified User ID.
     *
     * @param  id   the ID of the User
     * @return      the Car with the specified User ID
     * @see         objects.Car
     */
    public static Car getCarByUserId(int id) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CarDAO carDAO = dac.getCarDAO();
            return carDAO.getCarByUserId(id);
        }
    }

    /**
     * Uses the DAO to get the Car that corresponds with the specified ID.
     *
     * @param  id   the ID of the Car
     * @return      the Car with the specified ID
     * @see         objects.Car
     */
    public static Car getCarById(int id) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CarDAO carDAO = dac.getCarDAO();
            return carDAO.getCar(id);
        }
    }


    public static List<Ride> getRidesForCar(Car car) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RideDAO rideDAO = dac.getRideDAO();
            return rideDAO.getRidesByCar(car);
        }
    }

    public static List<Ride>getRidesForUser(User u) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            RideDAO rideDAO = dac.getRideDAO();
            return rideDAO.getRidesByUser(u);
        }
    }

    /**
     * Get the mileage. In the beginning mileage is a nullpointer, so we have to check that!
     @return the mileage of a car
     */
    public static float getMileage() throws DataAccessException{

        Float mileage = UserModel.getCarOwnedByUser(Integer.parseInt(session("current_user"))).getMileage();
        if(mileage == null){
            return 0.0f;
        }
        else{
            return mileage.floatValue();
        }
    }

    /**
     * Add a new cost to the database.
     */
    public static void acceptCost(Integer price, String type, Integer proof, String description, Long date) throws DataAccessException{
        try(DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()){
            dac.begin();
            CostDAO cost = dac.getCostDAO();
            Integer car = UserModel.getCarOwnedByUser(Integer.parseInt(session("current_user"))).getId();
            //Integer car, Integer price, String type, Integer proof, String description, Boolean approved, Long moment
            cost.createCost(car, price, type, proof, description, new Boolean(false), date);
            dac.commit();
        }
    }

    /**
     * Get all the reservations of the current user
     */
    public static List<Reservation> getReservations() throws DataAccessException{

        try(DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()){
            dac.begin();
            ReservationDAO res = dac.getReservationDAO();
            String user = session("current_user");
            Car car = UserModel.getCarOwnedByUser(Integer.parseInt(user));
            return res.getReservations(car);
        }

    }

    /**
     * Get costs of a car, necessary to display them in the tab 'My car'.
     * @return All the costs of a car
     */
    public static List<Cost> getCosts() throws DataAccessException{
        try(DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()){
            dac.begin();
            CostDAO costdao = dac.getCostDAO();
            String user = session("current_user");
            Car car = UserModel.getCarOwnedByUser(Integer.parseInt(user));
            return costdao.getCostsByCar((int) car.getId().intValue());
        }
    }

    /**
     * Method for showing the next car borrower for a specific reservation
     * @param r the reservation
     * @return the user who will borrow the car next to you.
     */
    public static User getNextBorrower(Reservation r) throws DataAccessException{
        try(DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()){
            ReservationDAO reservationDAO = dac.getReservationDAO();
            Filter<Reservation> filter = reservationDAO.getFilter();

            //Search on a reservation belonging to Car c, which has been approved and where the start-time is
            //greater than our start-time (all next borrowers)
            filter.fieldEquals(Reservation.Field.CAR, r.getCar().getId());
            filter.fieldEquals(Reservation.Field.APPROVED, true);
            filter.fieldGreaterThan(Reservation.Field.BEGIN, r.getBegin());

            //We apply the filter and sort the list by beginning, in ascending order,
            //we also only ask for the first element of that list (which will be the smallest value)
            List<Reservation> reservations = reservationDAO.getByFilter(filter, Reservation.Field.BEGIN, true, 1, 0);
            if(!reservations.isEmpty()){return reservations.get(0).getUser();}
            else{return null;}
        }
    }

    /**
     * Method for showing the previous car borrower for a specific reservation
     * @param r the reservation
     * @return the user who will borrow the car before you.
     */
    public static User getPreviousBorrower(Reservation r) throws DataAccessException{
        try(DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()){
            ReservationDAO reservationDAO = dac.getReservationDAO();
            Filter<Reservation> filter = reservationDAO.getFilter();
            filter.fieldEquals(Reservation.Field.CAR, r.getCar().getId());
            filter.fieldEquals(Reservation.Field.APPROVED, true);
            filter.fieldLessThan(Reservation.Field.BEGIN, r.getBegin());
            List<Reservation> reservations = reservationDAO.getByFilter(filter, Reservation.Field.BEGIN, false, 1, 0);
            if(!reservations.isEmpty()) return reservations.get(0).getUser();
            else return null;
        }
    }

    /**
     * This method updates all cars
     * @param map a map containing HTML tags as keys and their values as values
     */
    public static void updateCars(Map<String, String[]> map) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            CarDAO carDAO = dac.getCarDAO();
            for(Map.Entry<String, String[]> e : map.entrySet()){
                // nullcheck
                if("".equals(e.getValue()[0])) continue;

                String id = e.getKey().split("carField")[1];
                Car c = carDAO.getCar(Integer.parseInt(id));
                c.setApproved(Boolean.parseBoolean(e.getValue()[0]));
                carDAO.updateCar(c);
                String message = "Jouw auto,"+c.getName()+", is goedgekeurd. Proficiat!";
                NotificationModel.createNotification(c.getOwner(), "Auto goedgekeurd", message);
            }
            dac.commit();
        }
    }

    /**
     * This method is used to get a list of all cars that still need to be accepted or declined into the system by an admin
     * @return a list of all pending cars
     */
    public static List<Car> getPendingCars() throws DataAccessException{
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            CarDAO carDAO = dac.getCarDAO();
            //First get the cars with approved equal to null
            Filter<Car> filter1 = carDAO.getFilter();
            filter1.fieldIsNull(Car.Field.APPROVED);
            List<Car> list1 = carDAO.getByFilter(filter1);

            //Now get the cars with approved equal to false;
            Filter<Car> filter2 = carDAO.getFilter();
            filter2.fieldEquals(Car.Field.APPROVED, false);
            List<Car> list2 = carDAO.getByFilter(filter2);
            if(list2 != null && list1 != null) list1.addAll(list2);

            return list1;
        }
    }

    public static List<Reservation> getReservationsForCar(Car car) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            ReservationDAO reservationDAO = dac.getReservationDAO();
            List<Reservation> result = new ArrayList<>();
            for(Reservation reservation : reservationDAO.getReservations(car)){
                if(!(reservation.getBegin() < new java.util.Date().getTime()))
                    result.add(reservation);
            }
            return result;
        }
    }

    public static void acceptReservation(int id) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            ReservationDAO reservationDAO = dac.getReservationDAO();

            Reservation r = reservationDAO.getReservation(id);
            r.setApproved(true);
            r.setPending(false);
            reservationDAO.updateReservation(r);
            dac.commit();

            // Send notification to the borrower
            String message =
                "Uw reservatie-aanvraag van \"" + r.getCar().getName() + "\""
                + " van " + TimeUtils.getHumanReadableDate(r.getBegin())
                + " tot " + TimeUtils.getHumanReadableDate(r.getEnd())
                + " is goedgekeurd.";
            String phone = UserModel.getCurrentUser().getPhone();
            if (phone != null && !phone.isEmpty()) {
                message += " U kunt de auto-eigenaar bereiken op " + phone
                    + " voor verdere afspraken.";
            }
            NotificationModel.createNotification(r.getUser(), "Reservatie goedgekeurd", message);
        }
    }

    public static void declineReservation(int id, String reason) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            ReservationDAO reservationDAO = dac.getReservationDAO();
            Reservation r = reservationDAO.getReservation(id);
            r.setApproved(false);
            r.setPending(false);
            reservationDAO.updateReservation(r);
            dac.commit();

            // Send notification to the borrower
            String message =
                "Uw reservatie-aanvraag van \"" + r.getCar().getName() + "\""
                + " van " + TimeUtils.getHumanReadableDate(r.getBegin())
                + " tot " + TimeUtils.getHumanReadableDate(r.getEnd())
                + " is helaas afgekeurd.";
            if (reason != null && !reason.isEmpty())
                message += " De auto-eigenaar gaf de volgende reden: \"" + reason + "\".";
            String phone = UserModel.getCurrentUser().getPhone();
            if (phone != null && !phone.isEmpty()) {
                message += " U kunt de auto-eigenaar bereiken op " + phone
                    + " om samen een tijdstip overeen te komen dat wel past voor jullie beide.";
            }
            NotificationModel.createNotification(r.getUser(), "Reservatie afgekeurd", message);
        }
    }

    public static void acceptRide(int id) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            RideDAO rideDAO = dac.getRideDAO();
            PriceRangeDAO priceRangeDAO = dac.getPriceRangeDAO();
            List<PriceRange> priceRanges = priceRangeDAO.getAllPriceRanges();
            Ride r = rideDAO.getRide(id);
            if(r != null && r.getStopKM() != null && r.getStartKM() != null) {
                double drivenKM = r.getStopKM() - r.getStartKM();
                int price = -1;
                //Check for every PriceRange if the number of driven kilometers is between the minimum and maximum of the range
                //If so: multiply the number of driven kilometers with the price of that range.
                //price should never be -1!
                for(PriceRange priceRange: priceRanges){
                    if(drivenKM >= priceRange.getMin() && drivenKM <= priceRange.getMax())
                        price = (int)Math.round(priceRange.getPrice() * drivenKM);
                }
                r.setPrice(price);
                r.setPending(false);
                r.setApproved(true);
                rideDAO.updateRide(r);
                dac.commit();

                // Send notification to the borrower
                String message =
                        "Uw inzending van de gegevens van de rit met \"" + r.getCar().getName() + "\""
                                + " van " + TimeUtils.getHumanReadableDate(r.getBegin())
                                + " tot " + TimeUtils.getHumanReadableDate(r.getEnd())
                                + " is goedgekeurd.";
                NotificationModel.createNotification(r.getUser(), "Ritgegevens goedgekeurd", message);
            } else {
                throw new DataAccessException("De ritgegevens moeten ingevuld zijn!");
            }
        }
    }

    public static void declineRide(int id, String reason) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            RideDAO rideDAO = dac.getRideDAO();
            Ride r = rideDAO.getRide(id);
            r.setPending(false);
            r.setApproved(false);
            rideDAO.updateRide(r);
            dac.commit();

            // Send notification to the borrower
            String message =
                "Uw inzending van de gegevens van de rit met \"" + r.getCar().getName() + "\""
                + " van " + TimeUtils.getHumanReadableDate(r.getBegin())
                + " tot " + TimeUtils.getHumanReadableDate(r.getEnd())
                + " is helaas afgekeurd.";
            if (reason != null && !reason.isEmpty())
                message += " De auto-eigenaar gaf de volgende reden: \"" + reason + "\".";
            String phone = UserModel.getCurrentUser().getPhone();
            if (phone != null && !phone.isEmpty()) {
                message += " U kunt de auto-eigenaar bereiken op " + phone
                    + " als u niet begrijpt waarom deze gegevens werden afgekeurd.";
            }
            NotificationModel.createNotification(r.getUser(), "Ritgegevens afgekeurd", message);
        }
    }


    public static Car createNewCar(Map<String, String[]> postData, User user, Http.MultipartFormData body) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            dac.begin();
            List<File> photosCar = new ArrayList<File>();
            CarDAO carDAO = dac.getCarDAO();
            CarPhotoDAO carPhotoDAO = dac.getCarPhotoDAO();


           /* User owner, String name, String plate, Address address, Zone zone,
                    Integer inscription, String make, String type, String model,
                    Integer year, String fuel, String description, Integer doors,
                    Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis,
                    Integer value, Float mileage, Double kmpy*/


            objects.File inscription = null;
            try{
                FileDAO fileDAO = dac.getFileDAO();
                inscription = fileDAO.createFile(new FileInputStream(body.getFile("inscription").getFile()),body.getFile("inscription").getFilename());
            }catch(Exception e){}


            Car car = carDAO.createCar(user,
                    postData.get("carName")[0],
                    postData.get("plate")[0],
                    user.getAddress(),
                    user.getZone(),
                    inscription.getId(),
                    postData.get("make")[0],
                    postData.get("type")[0],
                    postData.get("model")[0],
                    Integer.parseInt(postData.get("byear")[0]),
                    postData.get("fuel")[0],
                    postData.get("description")[0],
                    Integer.parseInt(postData.get("doors")[0]),
                    Integer.parseInt(postData.get("capacity")[0]),
                    postData.containsKey("tow"),
                    postData.containsKey("gps"),
                    Float.parseFloat(postData.get("consumption")[0]),
                    postData.get("chassis")[0],
                    Math.round(Float.parseFloat(postData.get("value")[0]) * 100), // !! SHOULD BE PARSED MORE PRECISE
                    Float.parseFloat(postData.get("km")[0]),
                    Double.parseDouble(postData.get("kmpy")[0]));

            int i = 1;
            objects.File file = null;
            while(body.getFile("foto"+i) != null){
                try{
                FileDAO fileDAO = dac.getFileDAO();
                file = fileDAO.createFile(new FileInputStream(body.getFile("foto" + i).getFile()), body.getFile("foto" + i).getFilename());
                carPhotoDAO.createCarPhoto(car.getId(),file.getId());
                }catch(Exception e){}
                i++;
            }

            dac.commit();
            return car;

        }
    }


    /**
     * Remove a Cost from the database.
     *
     * @param   dac     the Data Access Context used to get the DAO objects
     * @param   cost    the Cost you want to remove
     */
    public static void removeCost(DataAccessContext dac, Cost cost) throws DataAccessException{
        CostDAO costDAO = dac.getCostDAO();
        costDAO.deleteCost(cost);
    }

    /**
     * Convert an Cost object to Play form data, useful for displaying the information in a form
     * on the website. Can also be used to just display the information in String format.
     */
    private static MyCarFormData makeMyCarFormData(Cost cost) throws DataAccessException {

        return new MyCarFormData(cost.getId(),
                cost.getPrice(),
                cost.getMoment(),
                cost.getType() == null ? "Niet gespecifieerd" : cost.getType(),
                cost.getDescription(), cost.getProof());
    }




    /**
     * Manage a cost in the database. This can be a removal of a cost or an edit.
     * @param   user    the user whose cost you want to manage
     * @return          the changed cost
     */
    public static Cost manageCost(MyCarFormData formData, User user, Car car, Http.MultipartFormData.FilePart filePart) throws DataAccessException {


        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {


            dac.begin();
            CostDAO costDAO = dac.getCostDAO();

            //remove a cost
            if(formData.remove.equals("removeCost") && (!formData.id.equals(""))) {

                Cost cost = getCostById(car.getId(), Integer.parseInt(formData.id));
                removeCost(dac, cost);
                dac.commit();
                return null;
            } else {

                String description = "";
                if(formData.description != null) description = formData.description;

                objects.File file = null;
                if(filePart != null){
                    try{
                        FileDAO fileDAO = dac.getFileDAO();
                        file = fileDAO.createFile(new FileInputStream(filePart.getFile()), filePart.getFilename());
                    }catch(Exception e){}
                }
                
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(formData.dateYear), Integer.parseInt(formData.dateMonth) - 1, Integer.parseInt(formData.dateDay));
                long date = cal.getTimeInMillis();
                
                Cost cost;
                if(!formData.id.equals("")) {
                    cost = getCostById(car.getId(), Integer.parseInt(formData.id));
                    cost.setDescription(description);
                    cost.setType(formData.type);
                    cost.setPrice(new Integer((int)(100*Float.parseFloat(formData.price))));
                    cost.setMoment(date);
                    if(filePart!=null){
                        cost.setProof(file.getId());
                    }
                    costDAO.updateCost(cost);

                }
                else{
                    //Integer car, Float price, String type, Integer proof, String description, Boolean approved, Long moment
                    cost = costDAO.createCost(car.getId(),new Integer((int)(Float.parseFloat(formData.price)*100)),formData.type,file.getId(),formData.description,false, new Long(date));
                }
                dac.commit();
                return cost;
            }
        }
    }


    /**
     * Convert Cost data to Play form data.
     * @param   user    the user whose costs you want to show
     * @return          a list of Costs represented as data for a Play form
     * @see             views.formdata.MyCarFormData
     */
    public static List<MyCarFormData> getCostFormDataList(User user) throws DataAccessException {
        try (DataAccessContext dac = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {

            CostDAO costDAO = dac.getCostDAO();

            List<MyCarFormData> formDataList = new ArrayList<MyCarFormData>();
            for (Cost cost : costDAO.getCostsByCar(getCarByUserId(user.getId()).getId())) {
                formDataList.add(makeMyCarFormData(cost));
            }
            return formDataList;
        }
    }


}
