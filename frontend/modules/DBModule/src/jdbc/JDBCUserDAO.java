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
import interfaces.UserDAO;
import java.sql.*;
import java.util.List;
import objects.*;
import objects.DataObject.DataField;

public class JDBCUserDAO extends JDBCDataAccessObject<User> implements UserDAO {
    
    JDBCUserDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return User.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{User.Field.TIME, User.Field.EDIT};
    }
    
    @Override
    protected DataField getKey() {
        return User.Field.ID;
    }

    @Override
    public Filter<User> getFilter() {
        return new JDBCFilter<User>() {};
    }

    @Override
    public User createUser(String name, String surname, Role role, String email, String pass) throws DataAccessException {
        return createUser(name, surname, role, null, email, pass, null, null, null, (Address) null, null, (Address) null, null, null);
    }
    
    @Override
    public User createUser(String name, String surname, Role role, String title, String email, 
            String pass, String phone, Integer guarantee, String past, Integer address, Zone zone,
            Integer domicile, License license, IdCard idcard) throws DataAccessException {
        
        Address addressObj = address == null ? null : dac.getAddressDAO().getAddress(address);
        Address domicileObj = domicile == null ? null : dac.getAddressDAO().getAddress(domicile);
        
        return createUser(name, surname, role, title, email, pass, phone, guarantee, past, addressObj, zone, domicileObj, license, idcard);
        
    }

    @Override
    public User createUser(String name, String surname, Role role, String title, String email, 
            String pass, String phone, Integer guarantee, String past, Address address, Zone zone,
            Address domicile, License license, IdCard idcard) throws DataAccessException {

        return new User(create(role == null ? null : role.getId(), title, name, surname, email, false, pass, phone, guarantee, past, 
                zone == null ? null : zone.getId(), address == null ? null : address.getId(), domicile == null ? null : domicile.getId(), 
                license == null ? null : license.getId(), idcard == null ? null : idcard.getId(), null, false),
                role, title, name, surname, email, false, pass, phone, guarantee, past, zone, address, domicile, license, idcard, null, false, System.currentTimeMillis(), System.currentTimeMillis());

    }

    @Override
    public void updateUser(User user) throws DataAccessException {

        update(user);

    }
    
    @Override
    public void deleteUser(Integer id) throws DataAccessException {
        deleteByID(id);
    }

    @Override
    public void deleteUser(User user) throws DataAccessException {
        deleteUser(user.getId());
    }
    
    

    @Override
    public User getUser(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public String getHash(String email) throws DataAccessException {
        try {
            
            PreparedStatement ps = getByEmailStmt();
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getNullable(String.class, rs, "pass");
                } else {
                    return null;
                }
            }
            
        } catch (SQLException e) {
            throw new DataAccessException("Problems returning a hash" + e.getMessage());
        }
    }

    private PreparedStatement getByEmailStmt() throws SQLException {
        if (stmts.get("email") == null) {
            stmts.put("email", connection.prepareStatement(
                    "SELECT pass, id FROM User WHERE mail = ?;"));
        }
        return stmts.get("email");
    }
    
    @Override
    public String getIdByMail(String email) throws DataAccessException {
        try {
            
            PreparedStatement ps = getByEmailStmt();
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return String.valueOf(getNullable(Integer.class, rs, "id"));
                } else {
                    return null;
                }
            }
            
        } catch (SQLException e) {
            throw new DataAccessException("Problems returning an id" + e.getMessage());
        }
    }
    

    @Override
    public List<User> getAllUsers() throws DataAccessException {
        return getAll();
    }
}
