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
import interfaces.ReservationRangeDAO;
import java.util.List;
import objects.Car;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.ReservationRange;

class JDBCReservationRangeDAO extends JDBCDataAccessObject<ReservationRange> implements ReservationRangeDAO {

    JDBCReservationRangeDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return ReservationRange.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return ReservationRange.Field.ID;
    }

    @Override
    public ReservationRange createReservationRange(Car car, Long begin, Long end, ReservationRange.Day day) throws DataAccessException {
        return new ReservationRange(create(car, begin, end, day.ordinal()), car, begin, end, day.ordinal());
    }

    @Override
    public void updateReservationRange(ReservationRange reservation) throws DataAccessException {
        update(reservation);

    }

    @Override
    public void deleteReservationRange(Integer id) throws DataAccessException {
        deleteByID(id);
    }

    @Override
    public ReservationRange getReservationRange(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<ReservationRange> getReservationRanges(Integer carID) throws DataAccessException {
        return getByAttr("car", carID);
    }
    
    @Override
    public Filter<ReservationRange> getFilter() {
        return new JDBCFilter<ReservationRange>() {};
    }

    @Override
    public List<ReservationRange> getReservationRanges(Integer carID, ReservationRange.Day day) throws DataAccessException {
        Filter<ReservationRange> filter = getFilter();
        filter.fieldEquals(ReservationRange.Field.CAR, carID);
        filter.fieldEquals(ReservationRange.Field.DAY, day.ordinal());
        return getByFilter(filter);
    }
    
}
