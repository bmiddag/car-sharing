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
import interfaces.RefuelingDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Refueling;
import objects.Ride;
import org.apache.commons.lang3.StringUtils;

class JDBCRefuelingDAO extends JDBCDataAccessObject<Refueling> implements RefuelingDAO {

    JDBCRefuelingDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Refueling.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Refueling.Field.TIME, Refueling.Field.EDIT};
    }
    
    @Override
    protected DataField getKey() {
        return Refueling.Field.ID;
    }

    @Override
    public Filter<Refueling> getFilter() {
        return new JDBCFilter<Refueling>() {};
    }

    @Override
    public Refueling createRefueling(Ride ride, Integer price) throws DataAccessException {
        return createRefueling(ride, price, null, null, null, null, null);
    }

    @Override
    public Refueling createRefueling(Integer rideId, Integer price, Double litre, String type) throws DataAccessException {
        return createRefueling(rideId, price, litre, type, null, null, null);
    }
    
    @Override
    public Refueling createRefueling(Integer rideId, Integer price, Double litre, String type, Boolean approved, Double mileage, Integer proof) throws DataAccessException {

        Ride rideObj = dac.getRideDAO().getRide(rideId);
        return createRefueling(rideObj, price, litre, type, approved, mileage, proof);

    }
    
    @Override
    public Refueling createRefueling(Ride ride, Integer price, Double litre, String type, Boolean approved, Double mileage, Integer proof) throws DataAccessException {

        return new Refueling(create(ride, price, litre, proof, type, approved, mileage), ride, price, litre, proof, type, approved, mileage, new Date().getTime(), new Date().getTime());

    }
    
    @Override
    public void updateRefueling(Refueling refueling) throws DataAccessException {

        update(refueling);
        
    }

    @Override
    public void updateRefuelingType(String type, Integer id) throws DataAccessException {
        updateAttrByID(id, "type", type);
    }

    @Override
    public void updateRefuelingPrice(Integer price, Integer id) throws DataAccessException {
        updateAttrByID(id, "price", price);
    }

    @Override
    public void updateRefuelingLitre(Double litre, Integer id) throws DataAccessException {
        updateAttrByID(id, "litre", litre);
    }

    @Override
    public void deleteRefueling(Refueling refueling) throws DataAccessException {
        deleteByID(refueling.getId());
    }
    
    @Override
    public void deleteRefueling(Integer id) throws DataAccessException {
        deleteByID(id);
    }
    
    @Override
    public Refueling getRefueling(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<Refueling> getRefuelingsByRide(Ride ride) throws DataAccessException {
        return getByAttr("ride", ride.getId());
    }
    
    @Override
    public List<Refueling> getPendingRefuelings() throws DataAccessException {
        return getByAttr("approved", null);
    }
    
    @Override
    public List<Refueling> getRefuelingsByUser(Integer user, Long from, Long to) throws DataAccessException {
        try {
            PreparedStatement ps = getReceiveByUserStmt("");
            ps.setLong(1, from);
            ps.setLong(2, to);
            ps.setInt(3, user);
            return executeToList(ps);
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get available cars: " + ex.getMessage());
        }
    }    

    private PreparedStatement getReceiveByUserStmt(String whereClause) throws SQLException {
        if (stmts.get("getByUser") == null) {
            stmts.put("getByUser", connection.prepareStatement(
                    "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins()
                    + " WHERE Refueling_table.APPROVED = b'1' AND Refueling_RIDE_table.END BETWEEN ? AND ? AND Refueling_RIDE_table.USER = ?;"));
        }
        return stmts.get("getByUser");
    }
    
}
