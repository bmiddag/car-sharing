package interfaces;

import objects.IdCard;
import objects.File;
import exceptions.DataAccessException;

public interface IdCardDAO extends DataAccessObject<IdCard> {
    
    /**
     * Creates a new IdCard with these specific parameters.
     * @see objects.IdCard
     * @param number Parameter for the new IdCard
     * @param register Parameter for the new IdCard
     * @param file Parameter for the new IdCard
     * @return A new objects.IdCard object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
    public IdCard createIdCard(String number, String register, File file) throws DataAccessException;    
    
    /**
     * Creates a new IdCard with these specific parameters.
     * @see objects.IdCard
     * @param number Parameter for the new IdCard
     * @param register Parameter for the new IdCard
     * @param file Parameter for the new IdCard
     * @return A new objects.IdCard object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
    public IdCard createIdCard(String number, String register, Integer file) throws DataAccessException;
    
    /**
     * Returns the IdCard with the specified id if it exists in the DB, null otherwise.
     * @param id The id of the IdCard to get from the DB
     * @return A new objects.IdCard object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
     public IdCard getIdCard(Integer id) throws DataAccessException;
     
     /**
      * Writes all fields of this IdCard to the DB.
      * @param idcard the IdCard to update
      * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
      */
    public void updateIdCard (IdCard idcard) throws DataAccessException;
    
    /**
     * Deletes the specified IdCard.
     * @param idcard The IdCard to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
    public void deleteIdCard (IdCard idcard) throws DataAccessException;
    
    /**
     * Deletes the specified IdCard.
     * @param idcard The IdCard to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
    public void deleteIdCard (Integer idcard) throws DataAccessException;

}
