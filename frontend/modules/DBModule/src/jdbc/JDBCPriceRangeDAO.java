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
import interfaces.PriceRangeDAO;
import java.util.List;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.PriceRange;

class JDBCPriceRangeDAO extends JDBCDataAccessObject<PriceRange> implements PriceRangeDAO {

    JDBCPriceRangeDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return PriceRange.Field.values();
    }

    @Override
    public Filter<PriceRange> getFilter() {
        return new JDBCFilter<PriceRange>() {};
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return PriceRange.Field.ID;
    }

    @Override
    public PriceRange createPriceRange(Integer min, Integer max, Integer price) throws DataAccessException {

       return new PriceRange(create(min, max, price), min, max, price);
    }

    @Override
    public void updatePriceRange(PriceRange range) throws DataAccessException {
        update(range);
    }

    @Override
    public void deletePriceRange(PriceRange range) throws DataAccessException {
        deleteByID(range.getId());
    }

    @Override
    public PriceRange getPriceRange(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<PriceRange> getAllPriceRanges() throws DataAccessException {
        return getAll();
    }
    
}
