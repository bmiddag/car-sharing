package interfaces;

import objects.Privilege;
import objects.User;
import objects.Car;
import exceptions.DataAccessException;
import java.util.List;

/**
 * This DAO Regulates access to the Privileges in the database.
 */
public interface PrivilegeDAO extends DataAccessObject<Privilege> {
    
    /**
     * 
     * @param user the user having the new privilege
     * @param car the car for the new privilege
     * @return a Privilege object representing the new privilege in the db
     * @throws DataAccessException 
     */
    public Privilege createPrivilege(User user, Car car) throws DataAccessException;
    
    /**
     * 
     * @param user the user having the new privilege
     * @param car the car for the new privilege
     * @return a Privilege object representing the new privilege in the db
     * @throws DataAccessException 
     */
    public Privilege createPrivilege(Integer user, Integer car) throws DataAccessException;
    
    /**
     * 
     * @param user the user for which to search privileges in the db
     * @return a list of all Privileges for the given user
     * @throws DataAccessException 
     */
    public List<Privilege> getPrivileges(Integer user) throws DataAccessException;
    
    /**
     * 
     * @param user the user for which to search privileges in the db
     * @return a list of all Privileges for the given user
     * @throws DataAccessException 
     */
    public List<Privilege> getPrivileges(User user) throws DataAccessException;
    
    /**
     * 
     * @param privilege the Privilege object to delete from the db
     * @throws DataAccessException 
     */
    public void deletePrivilege(Privilege privilege) throws DataAccessException;


}