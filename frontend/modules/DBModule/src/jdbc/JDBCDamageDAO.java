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
import interfaces.DamageDAO;
import interfaces.Filter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import objects.Car;
import objects.Damage;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Ride;
import objects.User;
import org.apache.commons.lang3.StringUtils;

class JDBCDamageDAO extends JDBCDataAccessObject<Damage> implements DamageDAO {

    JDBCDamageDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Damage.Field.values();
    }

    @Override
    public Filter<Damage> getFilter() {
        return new JDBCFilter<Damage>() {};
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Damage.Field.TIME, Damage.Field.EDIT};
    }
    
    @Override
    protected DataField getKey() {
        return Damage.Field.ID;
    }

    @Override
    public Damage createDamage(Integer car, Integer user, String status, String description, Long occurred) throws DataAccessException {
        return new Damage(create(user, car, status, description, occurred), user, car, status, description, occurred, new Date().getTime(), new Date().getTime());
    }

    @Override
    public Damage createDamage(Car car, User user, String description, Long occurred) throws DataAccessException {
        return createDamage(car.getId(), user.getId(), null, description, occurred);
    }

    @Override
    public Damage createDamage(Ride ride, String description, Long occurred) throws DataAccessException {
        return this.createDamage(ride.getCarID(), ride.getUserID(), null, description, occurred);
    }

    @Override
    public void updateDamage(Damage damage) throws DataAccessException {
        update(damage);
        
    }

    @Override
    public void updateDamageOccurred(Long occurred, Integer id) throws DataAccessException {
        updateAttrByID(id, "occurred", occurred);
    }

    @Override
    public void updateDamageDescription(String description, Integer id) throws DataAccessException {
        updateAttrByID(id, "description", description);
    }
    
    @Override
    public void deleteDamage(Integer id) throws DataAccessException {
        deleteByID(id);
    }

    @Override
    public void deleteDamage(Damage damage) throws DataAccessException {
        deleteByID(damage.getId());
    }

    @Override
    public Damage getDamage(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<Damage> getDamageByUser(Integer user) throws DataAccessException {
        return getByAttr("user", user);
    }

    @Override
    public List<Damage> getDamageByUser(User user) throws DataAccessException {
        return getDamageByUser(user.getId());
    }

    @Override
    public List<Damage> getDamageByCar(Integer car) throws DataAccessException {
        return getByAttr("car", car);
    }

    @Override
    public List<Damage> getDamageByCar(Car car) throws DataAccessException {
        return getDamageByCar(car.getId());
    }
    
    @Override
    public List<Damage> getDamageByRide(Ride ride) throws DataAccessException {
        Filter<Damage> filter = getFilter();
        filter.fieldEquals(Damage.Field.CAR, ride.getCar().getId());
        filter.fieldEquals(Damage.Field.USER, ride.getUser().getId());
        filter.fieldAfter(Damage.Field.OCCURRED, ride.getBegin());
        filter.fieldBefore(Damage.Field.OCCURRED, ride.getEnd());
        return getByFilter(filter);
    }

    private PreparedStatement getSearchStmt() throws SQLException {
        if (stmts.get("search") == null) {
            stmts.put("search", connection.prepareStatement(
                    "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins()
                    + " JOIN (User AS user, Car AS car, User AS owner) "
                    + " ON (Damage_table.USER = user.id AND Damage_table.CAR = car.id AND car.owner = owner.id) "
                    + " WHERE user.name LIKE ? OR user.surname LIKE ? OR user.mail LIKE ? OR user.phone LIKE ? "
                    + " OR owner.name LIKE ? OR owner.surname LIKE ? OR owner.mail LIKE ? OR owner.phone LIKE ? "
                    + " OR car.name LIKE ? OR car.plate LIKE ? "
                    + " OR Damage_table.status LIKE ?"));
        }
        return stmts.get("search");
    }

    @Override
    public List<Damage> searchDamage(String query) throws DataAccessException {
        try {
            PreparedStatement ps = getSearchStmt();
            for(int i = 1; i <= 11; i++){
                ps.setString(i, "%" + query + "%");
            }

            List<Damage> result = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(parseObject(rs));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't find damages with query " + query + ": " + ex.toString());
        }
    }

}
