package interfaces;

import objects.Role;
import exceptions.DataAccessException;

/**
 * This DAO Regulates access to the Roles in the database.
 */
public interface RoleDAO extends DataAccessObject<Role> {

    /**
     *
     * @param title the title of the new role
     * @return a Role object representing the new role in the db
     * @throws DataAccessException
     */
    public Role createRole(String title) throws DataAccessException;

    /**
     *
     * @param id the id of the Role to find in the db
     * @return a Role object representing the Role in the db; null if not found
     * @throws DataAccessException
     */
    public Role getRole(Integer id) throws DataAccessException;

    /**
     *
     * @param role an adapted Role object to update in the db
     * @throws DataAccessException
     */
    public void updateRole(Role role) throws DataAccessException;
    
    /**
     *
     * @param role an adapted Role object to update in the db
     * @throws DataAccessException
     */
    public void updateRole(Integer role, String name) throws DataAccessException;

    /**
     *
     * @param role the Role object to delete from the db
     * @throws DataAccessException
     */
    public void deleteRole(Role role) throws DataAccessException;

    /**
     *
     * @param role the Role object to delete from the db
     * @throws DataAccessException
     */
    public void deleteRole(Integer role) throws DataAccessException;
    
    public Role getRoleByName(String name) throws DataAccessException;

}
