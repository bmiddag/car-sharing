package interfaces;

import objects.Damage;
import objects.User;
import objects.Car;
import exceptions.DataAccessException;
import java.util.List;
import objects.Ride;

public interface DamageDAO extends DataAccessObject<Damage> {

    /**
     * Creates a new Damage with these specific parameters.
     *
     * @param status the status of the damage
     * @param description description of the damage
     * @param occurred when the damage occurred
     * @see objects.Damage
     * @param car Parameter for the new Damage
     * @param user Parameter for the new Damage
     * @parm description Parameter for the new Damage
     * @parm occurred Parameter for the new Damage
     * @return A new objects.Damage object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Damage createDamage(Integer car, Integer user, String status, String description, Long occurred) throws DataAccessException;

    /**
     * Creates a new Damage with these specific parameters.
     *
     * @param description description of the damage
     * @param occurred when the damage occurred
     * @see objects.Damage
     * @param car Parameter for the new Damage
     * @param user Parameter for the new Damage
     * @parm description Parameter for the new Damage
     * @parm occurred Parameter for the new Damage
     * @return A new objects.Damage object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Damage createDamage(Car car, User user, String description, Long occurred) throws DataAccessException;

    /**
     * Creates a new Damage with these specific parameters
     *
     * @param ride Parameter for the new Damage
     * @param description Parameter for the new Damage
     * @param occurred Parameter for the new Damage
     * @return A new objects.Damage object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Damage createDamage(Ride ride, String description, Long occurred) throws DataAccessException;

    /**
     * Returns the Damage with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param id The id of the Damage to get from the DB
     * @return A new objects.Damage object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Damage getDamage(Integer id) throws DataAccessException;

    /**
     * Return all the Damages this user is associated with.
     *
     * @param user The user whose damages we're collecting.
     * @return A new objects.Damage object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Damage> getDamageByUser(Integer user) throws DataAccessException;

    /**
     * Return all the Damages this user is associated with.
     *
     * @param user The user whose damages we're collecting.
     * @return A new objects.Damage object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Damage> getDamageByUser(User user) throws DataAccessException;

    /**
     * Return all damages associated with this car
     *
     * @param car The car whose damages we're collecting
     * @return A new objects.Damage object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Damage> getDamageByCar(Integer car) throws DataAccessException;

    /**
     * Return all damages associated with this car
     *
     * @param car The car whose damages we're collecting
     * @return A new objects.Damage object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Damage> getDamageByCar(Car car) throws DataAccessException;

    /**
     * Writes all fields of this Damage to the DB
     *
     * @param damage
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateDamage(Damage damage) throws DataAccessException;

    /**
     * Updates a particular field [WHY IS THIS BEING USED?]
     *
     * @param occurred the new time parameter
     * @param id The id of the Damage to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateDamageOccurred(Long occurred, Integer id) throws DataAccessException;

    /**
     * Updates the description of this Damage in the DB. [WHY IS THIS BEING
     * USED?]
     *
     * @param description The new description
     * @param id The id of the Damage to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateDamageDescription(String description, Integer id) throws DataAccessException;

    /**
     * Deletes the specified Damage.
     *
     * @param damage The Damage to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteDamage(Damage damage) throws DataAccessException;
    
    /**
     * Deletes the specified Damage.
     *
     * @param id The Damage to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteDamage(Integer id) throws DataAccessException;
   
    public List<Damage> getDamageByRide(Ride ride) throws DataAccessException;

    public List<Damage> searchDamage(String query) throws DataAccessException;
}
