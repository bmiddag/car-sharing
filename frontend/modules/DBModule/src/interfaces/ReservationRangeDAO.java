package interfaces;

import objects.ReservationRange;
import objects.Car;
import exceptions.DataAccessException;
import java.util.List;

/**
 * This DAO Regulates access to the Reservations in the database.
 */
public interface ReservationRangeDAO extends DataAccessObject<ReservationRange> {

    public ReservationRange createReservationRange(Car car, Long start, Long end, ReservationRange.Day day) throws DataAccessException;

    public List<ReservationRange> getReservationRanges(Integer carID) throws DataAccessException;

    public ReservationRange getReservationRange (Integer id) throws DataAccessException;
    
    public List<ReservationRange> getReservationRanges(Integer carID, ReservationRange.Day day) throws DataAccessException;

    public void updateReservationRange (ReservationRange range) throws DataAccessException;

    public void deleteReservationRange (Integer id) throws DataAccessException;


}
