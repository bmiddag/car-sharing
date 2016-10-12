package interfaces;

import objects.Cost;
import exceptions.DataAccessException;
import java.util.List;

/**
 * DAO for data about Costs.
 */
public interface CostDAO extends DataAccessObject<Cost> {

    /**
     *
     * @param car for which car the cost was made
     * @param price how much the cost was
     * @param type the type of cost
     * @param proof the proof file
     * @param description a description
     * @param approved if the cost was approved by an admin
     * @param moment moment this cost was made
     * @return
     * @throws exceptions.DataAccessException
     */
    public Cost createCost(Integer car, Integer price, String type, Integer proof, String description, Boolean approved, Long moment) throws DataAccessException;

    /**
     * Creates a new Cost with these specific parameters.
     *
     * @see objects.Cost
     * @param car Parameter for the new Cost
     * @param cost Parameter for the new Cost
     * @return A new objects.Cost object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Cost createCost(Integer car, Integer price) throws DataAccessException;

    /**
     * Returns the Cost with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param car The car to which the cost belongs
     * @param id The id of the Cost to get from the DB
     * @return A new objects.Cost object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Cost getCost(Integer car, Integer id) throws DataAccessException;
    
    /**
     * Returns the Cost with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param car The car to which the cost belongs
     * @param id The id of the Cost to get from the DB
     * @return A new objects.Cost object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Cost getCost(Integer id) throws DataAccessException;

    /**
     * Returns a List of all the Costs made for this car.
     *
     * @param car
     * @return A new objects.Cost object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Cost> getCostsByCar(Integer car) throws DataAccessException;
    
     /**
     * Returns a List of all the costs pending approval in the Database.
     *
     * @return A new objects.Cost list
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Cost> getPendingCosts() throws DataAccessException;

    /**
     *
     * @param id the id of the cost to update
     * @param car for which car the cost was made
     * @param price how much the cost was
     * @param type the type of cost
     * @param proof the proof file
     * @param description a description
     * @param approved if the cost was approved by an admin
     * @param moment moment this cost was made
     * @throws DataAccessException
     */
    public void updateCost(Integer id, Integer car, Integer price, String type, Integer proof, String description, Boolean approved, Long moment) throws DataAccessException;

    /**
     * Writes all fields of this Cost to the DB.
     *
     * @param cost the Cost to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateCost(Cost cost) throws DataAccessException;

    /**
     * Deletes the specified Cost.
     *
     * @param cost The Cost to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteCost(Cost cost) throws DataAccessException;

}
