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
import interfaces.PrivilegeDAO;
import java.util.List;
import objects.Car;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Privilege;
import objects.User;

class JDBCPrivilegeDAO extends JDBCDataAccessObject<Privilege> implements PrivilegeDAO {
    
    JDBCPrivilegeDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Privilege.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Privilege.Field.ID;
    }

    @Override
    public Filter<Privilege> getFilter() {
        return new JDBCFilter<Privilege>() {};
    }

    @Override
    public Privilege createPrivilege(User user, Car car) throws DataAccessException {
        return createPrivilege(user.getId(), car.getId());
    }

    @Override
    public Privilege createPrivilege(Integer user, Integer car) throws DataAccessException {

        return new Privilege(create(user, car), user, car);

    }
    
    @Override
    public void deletePrivilege(Privilege privilege) throws DataAccessException {

        deleteByID(privilege.getId());

    }

    @Override
    public List<Privilege> getPrivileges(User user) throws DataAccessException {
        return getByAttr("user", user.getId());
    }

    @Override
    public List<Privilege> getPrivileges(Integer user) throws DataAccessException {
        return getByAttr("user", user);
    }
    
}
