package interfaces;

import objects.Refueling;
import exceptions.DataAccessException;
import java.util.List;
import objects.Ride;

/**
 * This DAO Regulates access to the Refuelings in the database.
 */
public interface RefuelingDAO extends DataAccessObject<Refueling> {
    
    /**
     * 
     * @param ride the ride in which the car was refueled
     * @param price the cost of the new refueling
     * @return a Refueling object representing the refueling in the db
     * @throws DataAccessException 
     */
    public Refueling createRefueling(Ride ride, Integer price) throws DataAccessException;
    
    public Refueling createRefueling(Ride ride, Integer price, Double litre, String type, Boolean approved, Double mileage, Integer proof) throws DataAccessException;
    
    /**
     *
     * @param rideId 
     * @param price
     * @param litre
     * @param type
     * @return
     * @throws DataAccessException
     */
    public Refueling createRefueling(Integer rideId, Integer price, Double litre, String type) throws DataAccessException;
    /**
     * 
     * @param id the id of the Refueling to find in the db
     * @return a Refueling object representing the Refueling in the db; null if not found
     * @throws DataAccessException 
     */
    public Refueling getRefueling (Integer id) throws DataAccessException;
    
    /**
     * 
     * @param ride the Ride for which Refuelings are sought
     * @return a list of all Refuelings done during the given Ride 
     * @throws DataAccessException 
     */
    public List<Refueling> getRefuelingsByRide(Ride ride) throws DataAccessException;
 
    /**
     * Returns all refuelings that are still pending, so approved = null
     * 
     * @return a list of all Refuelings done during the given Ride 
     * @throws DataAccessException 
     */
    public List<Refueling> getPendingRefuelings() throws DataAccessException; 
    /**
     * 
     * @param refueling an adapted Refueling object to update in the db
     * @throws DataAccessException 
     */
    public void updateRefueling (Refueling refueling) throws DataAccessException;
    
    /**
     * 
     * @param type the new type for the refueling
     * @param id the db id of the refueling to change its type 
     * @throws DataAccessException 
     */
    public void updateRefuelingType (String type, Integer id) throws DataAccessException;
    
    /**
     * 
     * @param price the new price for the refueling
     * @param id the db id of the refueling to change its price
     * @throws DataAccessException 
     */
    public void updateRefuelingPrice (Integer price, Integer id) throws DataAccessException;
    
    /**
     * 
     * @param litre the new number of litres for the refueling
     * @param id the db id of the refueling to update
     * @throws DataAccessException 
     */
    public void updateRefuelingLitre (Double litre, Integer id) throws DataAccessException;
    
    /**
     * 
     * @param id the id of the Refueling to delete from the db
     * @throws DataAccessException 
     */
    public void deleteRefueling (Refueling refueling) throws DataAccessException;
    
    public Refueling createRefueling(Integer rideId, Integer price, Double litre, String type, Boolean approved, Double mileage, Integer proof) throws DataAccessException;

    public void deleteRefueling (Integer id) throws DataAccessException;
    
    public List<Refueling> getRefuelingsByUser(Integer user, Long from, Long to) throws DataAccessException;

}