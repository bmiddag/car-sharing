/*        ______ _____  _____            _   _                    *
 *       |  ____|  __ \|  __ \     /\   | \ | |Verantwoordelijken:*
 *       | |__  | |  | | |__) |   /  \  |  \| |                   *
 *       |  __| | |  | |  _  /   / /\ \ | . ` | Laurens De Graeve *
 *       | |____| |__| | | \ \  / ____ \| |\  |  & Wouter Termont *
 *       |______|_____/|_|  \_\/_/    \_\_|_\_|    _              *
 *       |  __ \|  _ \                    | |     | |             *
 *       | |  | | |_) |_ __ ___   ___   __| |_   _| | ___         *
 *       | |  | |  _ <| '_ ` _ \ / _ \ / _` | | | | |/ _ \        *
 *       | |__| | |_) | | | | | | (_) | (_| | |_| | |  __/        *
 *       |_____/|____/|_| |_| |_|\___/ \__,_|\__,_|_|\___|        *
 *                                                                * 
 *****************************************************************/
package jdbc;

import exceptions.DataAccessException;
import interfaces.Filter;
import interfaces.PermissionDAO;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Permission;

class JDBCPermissionDAO extends JDBCDataAccessObject<Permission> implements PermissionDAO {

    JDBCPermissionDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Permission.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Permission.Field.ID;
    }

    @Override
    public Filter<Permission> getFilter() {
        return new JDBCFilter<Permission>() {};
    }

    @Override
    public Permission createPermission(String name) throws DataAccessException {

        return new Permission(create(name), name);

    }

    @Override
    public void updatePermission(Integer permission, String name) throws DataAccessException {
        update(permission, name);

    }

    @Override
    public void updatePermission(Permission permission) throws DataAccessException {
        updatePermission(permission.getId(), permission.getName());
    }

    @Override
    public void deletePermission(Permission permission) throws DataAccessException {
        deleteByID(permission.getId());
    }

    @Override
    public Permission getPermission(Integer id) throws DataAccessException {
        return getByID(id);
    }
    
    @Override
    public void deletePermission(Integer permission) throws DataAccessException {
        deleteByID(permission);
    }

    @Override
    public Permission getPermissionByName(String name) throws DataAccessException {
        return getSingleByAttr("name", name);
    }
    
}
