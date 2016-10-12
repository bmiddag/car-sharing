package interfaces;

import objects.Reservation;
import objects.User;
import objects.Car;
import exceptions.DataAccessException;
import java.util.List;

/**
 * This DAO Regulates access to the Reservations in the database.
 */
public interface ReservationDAO extends DataAccessObject<Reservation> {

    /**
     *
     * @param user the user who made the new reservation
     * @param car the car that was reserved
     * @param start the start time of the new reservation
     * @param end the end of the new reservation
     * @return a Reservation object representing the new reservation in the db
     * @throws DataAccessException
     */
    public Reservation createReservation(User user, Car car, Long start, Long end) throws DataAccessException;

    /**
     *
     * @param car the car of which to find all reservations
     * @return a list of all the reservations made for the given car
     * @throws DataAccessException
     */
    public List<Reservation> getReservations(Car car) throws DataAccessException;

    /**
     *
     * @param id the id of the Reservation to find in the db
     * @return a Reservation object representing the Reservation in the db; null if not found
     * @throws DataAccessException
     */
    public Reservation getReservation (Integer id) throws DataAccessException;

    /**
     *
     * @param reservation an adapted Reservation object to update in the db
     * @throws DataAccessException
     */
    public void updateReservation (Reservation reservation) throws DataAccessException;

    /**
     *
     * @param reservation the Reservation object to delete from the db
     * @throws DataAccessException
     */
    public void deleteReservation (Reservation reservation) throws DataAccessException;

    /**
     *
     * @param user user for which to get all reservations
     * @return list of all reservations made by the given user
     * @throws DataAccessException
     */
    public List<Reservation> getReservations(User user) throws DataAccessException;

    /**
     *
     * @return list of all reservations
     * @throws DataAccessException
     */
    public List<Reservation> getAllReservations() throws DataAccessException;

    public List<Reservation> getReservationsWithoutRide(Integer userid) throws DataAccessException;

}
