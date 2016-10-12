package interfaces;

import exceptions.DataAccessException;
import objects.User;
import objects.Permission;
import objects.UserPermission;

import java.util.List;

/**
 * This DAO Regulates access to the UserPermissions in the database.
 */
public interface UserPermissionDAO extends DataAccessObject<UserPermission> {

    /**
     *
     * @param user the user of the new userpermission
     * @param permission the permission of the new userpermission
     * @return a UserPermission object representing the new userpermission in the db
     * @throws exceptions.DataAccessException
     */
    public UserPermission createUserPermission(User user, Permission permission) throws DataAccessException;

    /**
     *
     * @param id the id of the UserPermission to find in the db
     * @return a UserPermission object representing the UserPermission in the db; null if not found
     * @throws exceptions.DataAccessException
     */
    public UserPermission getUserPermission(Integer id) throws DataAccessException;

    /**
     *
     * @param userpermission an adapted UserPermission object to update in the db
     * @throws exceptions.DataAccessException
     */
    public void updateUserPermission(UserPermission userpermission) throws DataAccessException;

    /**
     *
     * @param userpermission an adapted UserPermission object to update in the db
     * @throws exceptions.DataAccessException
     */
    public void updateUserPermission(Integer userpermission, User user, Permission permission) throws DataAccessException;

    /**
     *
     * @param userpermission the UserPermission object to delete from the db
     * @throws exceptions.DataAccessException
     */
    public void deleteUserPermission(UserPermission userpermission) throws DataAccessException;

    /**
     *
     * @param userpermission the UserPermission object to delete from the db
     * @throws exceptions.DataAccessException
     */
    public void deleteUserPermission(Integer userpermission) throws DataAccessException;
    
    public List<UserPermission> getUserPermissionByUser(User user) throws DataAccessException;

}
