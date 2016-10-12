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
import interfaces.AddressDAO;
import interfaces.CarDAO;
import interfaces.FileDAO;
import interfaces.Filter;
import interfaces.UserDAO;
import interfaces.ZoneDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import objects.Address;
import objects.Car;
import objects.DataObject.DataField;
import objects.User;
import objects.Zone;
import org.apache.commons.lang3.StringUtils;

class JDBCCarDAO extends JDBCDataAccessObject<Car> implements CarDAO {

    JDBCCarDAO(JDBCDataAccessContext dac) {
        super(dac);
    }

    @Override
    public Filter<Car> getFilter() {
        return new JDBCFilter<Car>() {};
    }

    @Override
    protected DataField[] getFields() {
        return Car.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Car.Field.TIME, Car.Field.EDIT};
    }
    
    @Override
    protected DataField getKey() {
        return Car.Field.ID;
    }

    @Override
    public Car createCar(User owner, String name, String plate, Address address, Zone zone,
            Integer inscription, String make, String type, String model,
            Integer year, String fuel, String description, Integer doors,
            Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis,
            Integer value, Float mileage, Double kmpy) throws DataAccessException {
        return createCar(owner, name, plate, null, address, zone, inscription, make, type, model, year, fuel, description, doors, capacity, tow, gps, consumption, chassis, value, mileage, kmpy);
    }

    @Override
    public Car createCar(User owner, String name, String plate, Boolean approved, Address address, Zone zone,
            Integer inscription, String make, String type, String model,
            Integer year, String fuel, String description, Integer doors,
            Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis,
            Integer value, Float mileage, Double kmpy) throws DataAccessException {

        return createCar(owner, name, plate, approved, address, zone, inscription, make, type, model, year, fuel, description, doors, capacity, null, tow, gps, null, null, null, null, null, null, null, null, null, null, consumption, chassis, value, mileage, kmpy, null);

    }

    @Override
    public Car createCar(User owner, String name, String plate, Boolean approved, Address address, Zone zone, Integer inscription, String make, String type, String model, Integer year, String fuel, String description, Integer doors, Integer capacity, String reachability, Boolean tow, Boolean gps, Boolean airco, Boolean kiddyseats, Boolean automatic, Boolean large, Boolean wastedisposal, Boolean pets, Boolean smokefree, Boolean cdplayer, Boolean radio, Boolean mp3, Float consumption, String chassis, Integer value, Float mileage, Double kmpy, Boolean active) throws DataAccessException {
        return new Car(create(name, plate, address, zone, inscription, owner, make, type, model, year, fuel, description, doors, capacity, tow, gps, consumption, chassis, value, mileage, kmpy, approved, reachability, airco, kiddyseats, automatic, large, wastedisposal, pets, smokefree, cdplayer, radio, mp3, active), name, plate, address, zone, inscription, owner, make, type, model, year, fuel, description, doors, capacity, tow, gps, consumption, chassis, value, mileage, kmpy, approved, reachability, airco, kiddyseats, automatic, large, wastedisposal, pets, smokefree, cdplayer, radio, mp3, active, new Date().getTime(), new Date().getTime());
    }

    @Override
    public Car createCar(User owner, String name, String plate) throws DataAccessException {
        return createCar(owner, name, plate, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);
    }

    @Override
    public Car getCar(Integer id) throws DataAccessException {
        return getSingleByAttr("id", id);
    }

    @Override
    public Car getCarByUserId(Integer owner) throws DataAccessException {
        return getSingleByAttr("owner", owner);
    }
    

    private PreparedStatement getSearchStmt() throws SQLException {
        if (stmts.get("search") == null) {
            stmts.put("search", connection.prepareStatement(
                    "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins()
                            + " WHERE Car_table.NAME LIKE ?"
                            + " OR  Car_ADDRESS_table.STREET LIKE ?"
                            + " OR  Car_ADDRESS_PLACE_table.NAME LIKE ?"
                            + " OR  Car_OWNER_table.NAME LIKE ?"
                            + " OR  Car_OWNER_table.SURNAME LIKE ?;"));
        }
        return stmts.get("search");
    }

    @Override
    public List<Car> searchCar(String query) throws DataAccessException {
        try {
            PreparedStatement ps = getSearchStmt();
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ps.setString(3, "%" + query + "%");
            ps.setString(4, "%" + query + "%");
            ps.setString(5, "%" + query + "%");

            List<Car> result = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(parseObject(rs));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't find the car with query " + query + ": " + ex.getMessage());
        }
    }

    @Override
    public List<Car> getCars(User user) throws DataAccessException {
        return getByAttr("owner", user);

    }

    @Override
    public List<Car> getCars() throws DataAccessException {
        return getAll();
    }

    @Override
    public List<Car> getCars(Filter<Car> filter) throws DataAccessException {
        return getByFilter(filter);
    }

    private PreparedStatement getGetAvailableStmt(Filter filter) throws SQLException, DataAccessException {
        if (stmts.get("getAvailable") == null) {
            PreparedStatement whereclause = connection.prepareStatement(filter.toWhereClause());
            filter.fillInArguments(whereclause);
            stmts.put("getAvailable", connection.prepareCall("CALL getAvailableCars(?,?,?,\"" + StringUtils.join(getAliases(), ", ") + "\",\"" + getJoins() + "\",\'" + whereclause.toString() + "\')"));
            /*
            stmts.put("getAvailable", connection.prepareStatement(
                    "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins()
                    + " WHERE " + (whereClause.isEmpty() ? "" : whereClause + " AND")
                    + " Car_table.ID NOT IN (SELECT DISTINCT Car.id FROM Car"
                    + " INNER JOIN Reservation ON (Car.id = Reservation.car)"
                    + " WHERE Reservation.begin BETWEEN ? AND ? AND Reservation.end BETWEEN ? AND ?);"));
            */
        }
        return stmts.get("getAvailable");
    }
    
    /*
    
    @Override
    public List<Car> getAvailableCars(Long from, Long to, Long duration) throws DataAccessException {
        try {
            PreparedStatement ps = getGetAvailableStmt("");
            ps.setLong(1, to);
            ps.setLong(2, from);
            ps.setLong(3, to - from);
            return executeToList(ps);
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get available cars: " + ex.getMessage());
        }
    }

    @Override
    public List<Car> getAvailableCars(Long from, Long to) throws DataAccessException {
        return getAvailableCars(from, to, to - from);
    }

    */
    
    @Override
    public List<PossibleReservation> getAvailableCars(Long from, Long to, Long duration, Filter<Car> filter) throws DataAccessException {
        
        //filter.fieldEquals(Car.Field.APPROVED, true);
        //filter.fieldEquals(Car.Field.ACTIVE, true);
        
        try {
            List<PossibleReservation> list = new ArrayList<>();
            PreparedStatement whereclause = connection.prepareStatement(filter.toWhereClause());
            filter.fillInArguments(whereclause);
            int where = whereclause.toString().indexOf("WHERE");
            PreparedStatement ps = connection.prepareCall("CALL getAvailableCars(?,?,?,\"" + StringUtils.join(getAliases(), ", ") + "\",\"" + getJoins() + "\",\"" 
                    + (where < 0 ? "" : whereclause.toString().substring(where)) + "\")");
            ps.setLong(1, from);
            ps.setLong(2, to);
            ps.setLong(3, duration);
            //filter.fillInArguments(ps,3); 
            //System.out.println(ps.toString());          
            boolean result = ps.execute();
            
            while (result) {
                ResultSet rs = ps.getResultSet();
                if (rs.first()) {
                    Car car = parseObject(rs);
                    list.add(new PossibleReservation(car, rs.getLong("try_begin"), rs.getLong("try_end")));
                }
                result = ps.getMoreResults();
            }
            
            return list;
                        
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get available cars: " + ex.getMessage());
        }
        
        /*
        try {
            PreparedStatement ps = getGetAvailableStmt(filter.toWhereClause("Car_table."));
            filter.fillInArguments(ps);

            int filterArgumentAmount = filter.getArgumentsAmount();
            ps.setLong(filterArgumentAmount + 1, from);
            ps.setLong(filterArgumentAmount + 2, to);
            ps.setLong(filterArgumentAmount + 3, from);
            ps.setLong(filterArgumentAmount + 4, to);
            return executeToList(ps);
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get available cars: " + ex.getMessage());
        }
        */
    }

    @Override
    public void updateCar(Car car) throws DataAccessException {
        update(car);
    }

    @Override
    public void deleteCar(Car car) throws DataAccessException {
        deleteByID(car.getId());

    }

    private AddressDAO getAddressDAO() {
        return new JDBCAddressDAO(dac);
    }
    private ZoneDAO getZoneDAO() {
        return new JDBCZoneDAO(dac);
    }
    private FileDAO getFileDAO()  {
        return new JDBCFileDAO(dac);
    }
    private UserDAO getUserDAO() {
        return new JDBCUserDAO(dac);
    }

    @Override
    public List<Car> getCars(Integer owner) throws DataAccessException {
        return getByAttr("owner", owner);
    }

    @Override
    public void updateCar(Integer id, Integer owner, String name, String plate, Integer address, Integer zone, Integer inscription, String make, String type, String model, Integer year, String fuel, String description, Integer doors, Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis, Integer value, Float mileage, Double kmpy) throws DataAccessException {
        updateCar(id, owner, name, plate, address, zone, inscription, make, type, model, year, fuel, description, doors, capacity, tow, gps, consumption, chassis, value, mileage, kmpy, false);
    }

    @Override
    public void updateCar(Integer id, Integer owner, String name, String plate, Integer address, Integer zone, Integer inscription, String make, String type, String model, Integer year, String fuel, String description, Integer doors, Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis, Integer value, Float mileage, Double kmpy, Boolean approved) throws DataAccessException {
        updateCar(id, owner, name, plate, address, zone, inscription, make, type, model, year, fuel, description, doors, capacity, tow, gps, consumption, chassis, value, mileage, kmpy, approved, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    @Override
    public void updateCar(Integer id, Integer owner, String name, String plate, Integer address, Integer zone, Integer inscription, String make, String type, String model, Integer year, String fuel, String description, Integer doors, Integer capacity, Boolean tow, Boolean gps, Float consumption, String chassis, Integer value, Float mileage, Double kmpy, Boolean approved, String reachability, Boolean airco, Boolean kiddyseats, Boolean automatic, Boolean large, Boolean wastedisposal, Boolean pets, Boolean smokefree, Boolean cdplayer, Boolean radio, Boolean mp3, Boolean active) throws DataAccessException {
        update(id, name, plate, address, zone, inscription, owner, make, type, model, year, fuel, description, doors, capacity, tow, gps, consumption, chassis, value, mileage, kmpy, approved, reachability, airco, kiddyseats, automatic, large, wastedisposal, pets, smokefree, cdplayer, radio, mp3, active);
    }

    @Override
    public void deleteCar(Integer car) throws DataAccessException {
        deleteByID(car);
    }
}
