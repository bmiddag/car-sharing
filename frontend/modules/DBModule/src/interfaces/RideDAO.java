package interfaces;

import objects.Ride;
import objects.User;
import objects.Car;
import exceptions.DataAccessException;
import java.util.List;

/**
 * This DAO Regulates access to the Rides in the database.
 */
public interface RideDAO extends DataAccessObject<Ride> {
    
    public Long getSumKMs(Long from) throws DataAccessException;
    public Long getSumKMs() throws DataAccessException;

    /**
     *
     * @param user the user who made the ride
     * @param car the car that was driven
     * @param begin when the ride started
     * @param end when the ride stopped
     * @param approved if the owner approves the ride made (and the noted
     * mileages)
     * @param pending if a administrator checked this ride
     * @param startKM the mileage before the ride
     * @param stopKM the mileage after the ride
     * @return a Ride object representing the new ride in the db
     * @throws DataAccessException
     */
    public Ride createRide(Integer user, Integer car, Long begin, Long end, Boolean approved, Boolean pending, Double startKM, Double stopKM) throws DataAccessException;

    public Ride createRide(User user, Car car, Long begin, Long end, Boolean approved, Boolean pending, Double startKM, Double stopKM) throws DataAccessException;
    
    public Ride createRide(User user, Car car, Long begin, Long end, Boolean approved, Boolean pending, Double startKM, Double stopKM, Integer price) throws DataAccessException;
    
    /**
     *
     * @param user the user who made this new ride
     * @param car the car that was driven
     * @return a Ride object representing the new ride in the db
     * @throws DataAccessException
     */
    public Ride createRide(User user, Car car) throws DataAccessException;

    public Ride createRide(Integer user, Integer car) throws DataAccessException;

    /**
     *
     * @param id the id of the Ride to find in the db
     * @return a Ride object representing the Ride in the db; null if not found
     * @throws DataAccessException
     */
    public Ride getRide(Integer id) throws DataAccessException;

    /**
     *
     * @param ride an adapted Ride object to update in the db
     * @throws DataAccessException
     */
    public void updateRide(Ride ride) throws DataAccessException;

    /**
     *
     * @param km number of kms to update the mileage at the start of the ride
     * @param id the id of the ride to update
     * @throws DataAccessException
     */
    public void updateRideStartKM(Integer id, Double km) throws DataAccessException;

    /**
     *
     * @param km number of kms to update the mileage at the end of the ride
     * @param id the id of the ride to update
     * @throws DataAccessException
     */
    public void updateRideStopKM(Integer id, Double km) throws DataAccessException;

    /**
     *
     * @param ride the Ride object to delete from the db
     * @throws DataAccessException
     */
    public void deleteRide(Integer ride) throws DataAccessException;

    /**
     *
     * @param car the car for which to search all rides
     * @return a list of all rides made with the given car
     * @throws DataAccessException
     */
    public List<Ride> getRidesByCar(Integer car) throws DataAccessException;

    /**
     *
     * @param user the user for which to search all rides
     * @return a list of all rides made with the given car
     * @throws DataAccessException
     */
    public List<Ride> getRidesByUser(Integer user) throws DataAccessException;

    /**
     *
     * @param car the car for which to search all rides
     * @return a list of all rides made with the given car
     * @throws DataAccessException
     */
    public List<Ride> getRidesByCar(Car car) throws DataAccessException;

    /**
     *
     * @param user the user for which to search all rides
     * @return a list of all rides made with the given car
     * @throws DataAccessException
     */
    public List<Ride> getRidesByUser(User user) throws DataAccessException;

    /**
     *
     * @return a list of all rides
     * @throws DataAccessException
     */
    public List<Ride> getAllRides() throws DataAccessException;

}
