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
import interfaces.RoleDAO;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Role;

class JDBCRoleDAO extends JDBCDataAccessObject<Role> implements RoleDAO {

    JDBCRoleDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Role.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Role.Field.ID;
    }

    @Override
    public Filter<Role> getFilter() {
        return new JDBCFilter<Role>() {};
    }

    @Override
    public Role createRole(String name) throws DataAccessException {

        return new Role(create(name), name);

    }

    @Override
    public void updateRole(Integer role, String name) throws DataAccessException {

        update(role, name);

    }

    @Override
    public void updateRole(Role role) throws DataAccessException {
        updateRole(role.getId(), role.getName());
    }

    @Override
    public void deleteRole(Role role) throws DataAccessException {
        deleteByID(role.getId());
    }

    @Override
    public Role getRole(Integer id) throws DataAccessException {
        return getByID(id);
    }
    
    @Override
    public void deleteRole(Integer role) throws DataAccessException {
        deleteByID(role);
    }

    @Override
    public Role getRoleByName(String name) throws DataAccessException {
        return getSingleByAttr("name", name);
    }
    
}
