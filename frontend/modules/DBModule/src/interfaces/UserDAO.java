package interfaces;

import objects.User;
import objects.Role;
import exceptions.DataAccessException;
import java.util.List;
import objects.Address;
import objects.IdCard;
import objects.License;
import objects.Zone;

/**
 * This DAO Regulates access to the Users in the database.
 */
public interface UserDAO extends DataAccessObject<User> {

    /**
     *
     * @param role the role this user has in the system
     * @param title the title by which to address this user
     * @param name the user's first name
     * @param surname the user's last name
     * @param email the user's e-mail address
     * @param login the user's login name
     * @param pass the user's password (hashed and salted)
     * @param phone the user's telephone number
     * @param guarantee the amount of money the user paid as guarantee
     * @param past a short description of the user's past
     * @param zone the zone to which the user belongs
     * @param address the user's address
     * @param domicile the user's domicile address
     * @param license the users driver's license
     * @param idcard the user's id card
     * @return
     * @throws exceptions.DataAccessException
     */
    public User createUser(String name, String surname, Role role, String title, String email, 
            String pass, String phone, Integer guarantee, String past, Integer address, Zone zone,
            Integer domicile, License license, IdCard idcard) throws DataAccessException;

    public User createUser(String name, String surname, Role role, String title, String email, 
            String pass, String phone, Integer guarantee, String past, Address address, Zone zone,
            Address domicile, License license, IdCard idcard) throws DataAccessException;
        
    /**
     *
     * @param name the first name of the new user
     * @param surname the last name of the new user
     * @param role the role of the new user in the system
     * @param email the e-mail address of the new user
     * @param login the login name of the new user
     * @param pass the password of the new user (hashed)
     * @return a User object representing the new user in the db
     * @throws DataAccessException
     */
    public User createUser(String name, String surname, Role role, String email, String pass)
            throws DataAccessException;

    /**
     *
     * @param id the id of the user to find in the db
     * @return a User object representing the user in the db; null if not found
     * @throws DataAccessException
     */
    public User getUser(Integer id) throws DataAccessException;
    
    
    /**
     *
     * @return a list of all users
     * @throws DataAccessException
     */
    public List<User> getAllUsers() throws DataAccessException;

    /**
     *
     * @param email The user's login email.
     * @return The password hash associated with this email if it exists, null
     * otherwise.
     * @throws DataAccessException
     */
    public String getHash(String email) throws DataAccessException;

    /**
     *
     * @param email The user's login email.
     * @return The user id associated with this email if it exists, null
     * otherwise.
     * @throws DataAccessException
     */
    public String getIdByMail(String email) throws DataAccessException;

    /**
     *
     * @param user an adapted User object to update in the db
     * @throws DataAccessException
     */
    public void updateUser(User user) throws DataAccessException;

    /**
     *
     * @param user the user object to delete from the db
     * @throws DataAccessException
     */
    public void deleteUser(User user) throws DataAccessException;

    /**
     *
     * @param user the id of the user object to delete from the db
     * @throws DataAccessException
     */
    public void deleteUser(Integer user) throws DataAccessException;



}
