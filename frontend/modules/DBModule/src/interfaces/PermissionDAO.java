package interfaces;

import exceptions.DataAccessException;
import objects.Permission;

/**
 * This DAO Regulates access to the Permissions in the database.
 */
public interface PermissionDAO extends DataAccessObject<Permission> {

    /**
     *
     * @param title the title of the new permission
     * @return a Permission object representing the new permission in the db
     * @throws exceptions.DataAccessException
     */
    public Permission createPermission(String title) throws DataAccessException;

    /**
     *
     * @param id the id of the Permission to find in the db
     * @return a Permission object representing the Permission in the db; null if not found
     * @throws exceptions.DataAccessException
     */
    public Permission getPermission(Integer id) throws DataAccessException;

    /**
     *
     * @param permission an adapted Permission object to update in the db
     * @throws exceptions.DataAccessException
     */
    public void updatePermission(Permission permission) throws DataAccessException;

    /**
     *
     * @param permission an adapted Permission object to update in the db
     * @throws exceptions.DataAccessException
     */
    public void updatePermission(Integer permission, String name) throws DataAccessException;

    /**
     *
     * @param permission the Permission object to delete from the db
     * @throws exceptions.DataAccessException
     */
    public void deletePermission(Permission permission) throws DataAccessException;

    /**
     *
     * @param permission the Permission object to delete from the db
     * @throws exceptions.DataAccessException
     */
    public void deletePermission(Integer permission) throws DataAccessException;
    
    public Permission getPermissionByName(String name) throws DataAccessException;

}
