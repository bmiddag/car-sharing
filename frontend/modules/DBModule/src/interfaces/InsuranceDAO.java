package interfaces;

import objects.Insurance;
import objects.Car;
import exceptions.DataAccessException;

public interface InsuranceDAO extends DataAccessObject<Insurance> {

    /**
     * Creates a new insurance for the Car object. Either a new insurance is
     * created or an exception is thrown, no nulls.
     *
     * @see objects.Insurance
     * @param car The car this insurance insures
     * @return A new objects.Insurance object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Insurance createInsurance(Car car) throws DataAccessException;

    public Insurance createInsurance(Integer car, String number, String company, Integer bonusmalus, Boolean omnium, java.sql.Date expiration) throws DataAccessException;
    
    public Insurance createInsurance(Car car, String number, String company, Integer bonusmalus, Boolean omnium, java.sql.Date expiration) throws DataAccessException;

    /**
     * Returns the Insurance with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param id The id of the Insurance to get
     * @return A new objects.Insurance object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Insurance getInsurance(Integer id) throws DataAccessException;

    /**
     * Writes all fields of this insurance to the DB.
     *
     * @param insurance the Insurance to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateInsurance(Insurance insurance) throws DataAccessException;

    /**
     * Deletes the specified Insurance.
     *
     * @param insurance The insurance to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteInsurance(Insurance insurance) throws DataAccessException;
}
