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
import interfaces.ReservationDAO;
import java.sql.*;
import java.util.Date;
import java.util.List;
import objects.Car;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Reservation;
import objects.User;
import org.apache.commons.lang3.StringUtils;

class JDBCReservationDAO extends JDBCDataAccessObject<Reservation> implements ReservationDAO {

    JDBCReservationDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Reservation.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Reservation.Field.TIME, Reservation.Field.EDIT};
    }
    
    @Override
    protected DataField getKey() {
        return Reservation.Field.ID;
    }

    @Override
    public Reservation createReservation(User user, Car car, Long begin, Long end) throws DataAccessException {
        
        return new Reservation(create(user, car, begin, end, null, true), user, car, begin, end, null, true, new Date().getTime(), new Date().getTime());

    }

    @Override
    public void updateReservation(Reservation reservation) throws DataAccessException {
        update(reservation);

    }

    @Override
    public void deleteReservation(Reservation reservation) throws DataAccessException {
        deleteByID(reservation.getId());
    }

    @Override
    public Reservation getReservation(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<Reservation> getReservations(Car car) throws DataAccessException {
        return getByAttr("car", car.getId());
    }

    @Override
    public List<Reservation> getReservations(User user) throws DataAccessException {
        return getByAttr("user", user.getId());
    }

    @Override
    public List<Reservation> getAllReservations() throws DataAccessException {
        return getAll();
    }

    private PreparedStatement getGetWithoutRideStmt() throws SQLException {
        if (stmts.get("getWithoutRide") == null) {
            stmts.put("getWithoutRide", connection.prepareStatement(
                    "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins()
                    + " LEFT JOIN Ride r ON r.user = Reservation_table.USER "
                    + " AND r.car = Reservation_table.CAR "
                    + " AND r.begin = Reservation_table.BEGIN " 
                    + " AND r.end = Reservation_table.END "
                    + " WHERE r.id IS NULL AND Reservation_table.USER = ?"));
        }
        return stmts.get("getWithoutRide");
    }
    
    @Override
    public List<Reservation> getReservationsWithoutRide(Integer userid) throws DataAccessException {
        try {
            PreparedStatement ps = getGetWithoutRideStmt();
            ps.setInt(1, userid);
            return executeToList(ps);
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get reservation without rides: " + ex.getMessage());
        }
    }
    
    @Override
    public Filter<Reservation> getFilter() {
        return new JDBCFilter<Reservation>() {};
    }
    
}
