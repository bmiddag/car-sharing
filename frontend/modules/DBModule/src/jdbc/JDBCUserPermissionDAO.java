package jdbc;

import exceptions.DataAccessException;
import interfaces.Filter;
import interfaces.UserPermissionDAO;
import java.util.List;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Permission;
import objects.User;
import objects.UserPermission;

class JDBCUserPermissionDAO extends JDBCDataAccessObject<UserPermission> implements UserPermissionDAO {

    JDBCUserPermissionDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return UserPermission.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return UserPermission.Field.ID;
    }

    @Override
    public Filter<UserPermission> getFilter() {
        return new JDBCFilter<UserPermission>() {};
    }
    
    @Override
    public UserPermission createUserPermission(User user, Permission permission) throws DataAccessException {
        return new UserPermission(create(user == null ? null : user.getId(), permission == null ? null : permission.getId()), user, permission);
    }

    @Override
    public UserPermission getUserPermission(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public void updateUserPermission(UserPermission userpermission) throws DataAccessException {
        updateUserPermission(userpermission.getId(), userpermission.getUser(), userpermission.getPermission());
    }

    @Override
    public void updateUserPermission(Integer userpermission, User user, Permission permission) throws DataAccessException {
        update(userpermission, user, permission);
    }

    @Override
    public void deleteUserPermission(UserPermission userpermission) throws DataAccessException {
        deleteUserPermission(userpermission.getId());
    }

    @Override
    public void deleteUserPermission(Integer userpermission) throws DataAccessException {
        deleteByID(userpermission);
    }

    @Override
    public List<UserPermission> getUserPermissionByUser(User user) throws DataAccessException {
        return getByAttr("user", user.getId());
       
    }
}
