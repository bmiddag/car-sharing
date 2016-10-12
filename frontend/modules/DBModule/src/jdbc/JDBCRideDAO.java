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
import interfaces.RideDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import objects.Car;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Ride;
import objects.User;
import org.apache.commons.lang3.StringUtils;

class JDBCRideDAO extends JDBCDataAccessObject<Ride> implements RideDAO {
    
    JDBCRideDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Ride.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Ride.Field.TIME};
    }
    
    @Override
    protected DataField getKey() {
        return Ride.Field.ID;
    }
    
    @Override
    public Ride createRide(Integer user, Integer car, Long begin, Long end, Boolean approved, Boolean pending, Double startKM, Double stopKM) throws DataAccessException {
        User userObj = user == null ? null : dac.getUserDAO().getUser(user);
        Car carObj = car == null ? null : dac.getCarDAO().getCar(car);
        return createRide(userObj, carObj, begin, end, approved, pending, startKM, stopKM, null);
    }
    
    @Override
    public Ride createRide(User user, Car car, Long begin, Long end, Boolean approved, Boolean pending, Double startKM, Double stopKM) throws DataAccessException {

        return createRide(user, car, begin, end, approved, pending, startKM, stopKM, null);

    }
    
    @Override
    public Ride createRide(User user, Car car, Long begin, Long end, Boolean approved, Boolean pending, Double startKM, Double stopKM, Integer price) throws DataAccessException {

        return new Ride(create(user == null ? null : user.getId(), car == null ? null : car.getId(), begin, end, approved, pending, startKM, stopKM, price),
                user, car, begin, end, approved, pending, startKM, stopKM, price, System.currentTimeMillis());

    }

    @Override
    public Ride createRide(User user, Car car) throws DataAccessException {
        return createRide(user.getId(), car.getId(), null, null, null, true, null, null);
    }
    
    @Override
    public Ride createRide(Integer user, Integer car) throws DataAccessException {
        return createRide(user, car, null, null, null, true, null, null);
    }

    @Override
    public void updateRide(Ride ride) throws DataAccessException {

        update(ride);

    }

    @Override
    public void updateRideStartKM(Integer id, Double km) throws DataAccessException {
        updateAttrByID(id, "startkm", km);
    }

    @Override
    public void updateRideStopKM(Integer id, Double km) throws DataAccessException {
        updateAttrByID(id, "stopkm", km);
    }

    @Override
    public void deleteRide(Integer ride) throws DataAccessException {
        deleteByID(ride);
    }
    
    @Override
    public Ride getRide(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<Ride> getRidesByUser(Integer user) throws DataAccessException {
        return getByAttr("user", user);
    }

    @Override
    public List<Ride> getRidesByCar(Integer car) throws DataAccessException {
        return getByAttr("car", car);
    }

    @Override
    public List<Ride> getRidesByUser(User user) throws DataAccessException {
        return getByAttr("user", user.getId());
    }

    @Override
    public List<Ride> getRidesByCar(Car car) throws DataAccessException {
        return getByAttr("car", car.getId());
    }
    
    @Override
    public List<Ride> getAllRides() throws DataAccessException {
        return getAll();
    }

    @Override
    public Filter<Ride> getFilter() {
        return new JDBCFilter<Ride>() {};        
    }    

    private PreparedStatement getSumKMStmt(String whereClause) throws SQLException {
        if (stmts.get("sumKM") == null) {
            stmts.put("sumKM", connection.prepareStatement(
                    "SELECT SUM(stopKM - startKM) as sum FROM Ride WHERE begin > ?;"));
        }
        return stmts.get("sumKM");
    }
    
    @Override
    public Long getSumKMs(Long from) throws DataAccessException {
        try {
            PreparedStatement ps = getSumKMStmt("");
            ps.setLong(1, from);
            ResultSet rs = ps.executeQuery();
            rs.first();
            return rs.getLong("sum");
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get sum of KM's: " + ex.getMessage());
        }
    }    

    private PreparedStatement getSumAllKMStmt(String whereClause) throws SQLException {
        if (stmts.get("sumAllKM") == null) {
            stmts.put("sumAllKM", connection.prepareStatement(
                    "SELECT SUM(stopKM - startKM) as sum FROM Ride;"));
        }
        return stmts.get("sumAllKM");
    }
    
    @Override
    public Long getSumKMs() throws DataAccessException {
        try {
            PreparedStatement ps = getSumAllKMStmt("");
            ResultSet rs = ps.executeQuery();
            rs.first();
            return rs.getLong("sum");
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get sum of KM's: " + ex.getMessage());
        }
    }

}
