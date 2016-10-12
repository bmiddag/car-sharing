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
import interfaces.CostDAO;
import interfaces.Filter;
import java.util.Date;
import java.util.List;
import objects.Cost;
import objects.DataObject;
import objects.DataObject.DataField;

/**
 * JDBC implementation of the CostDAO interface.
 *
 */
class JDBCCostDAO extends JDBCDataAccessObject<Cost> implements CostDAO {

    JDBCCostDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Cost.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Cost.Field.TIME, Cost.Field.EDIT};
    }
    
    @Override
    protected DataField getKey() {
        return Cost.Field.ID;
    }

    @Override
    public Filter<Cost> getFilter() {
        return new JDBCFilter<Cost>() {};
    }

    @Override
    public Cost createCost(Integer car, Integer price, String type, Integer proof, String description, Boolean approved, Long moment) throws DataAccessException {
        return new Cost(create(car, price, type, proof, description, approved, moment), car, price, type, proof, description, approved, moment, new Date().getTime(), new Date().getTime());

    }

    @Override
    public Cost createCost(Integer car, Integer price) throws DataAccessException {
        return createCost(car, price, null, null, null, null, System.currentTimeMillis());
    }

    @Override
    public void updateCost(Integer id, Integer car, Integer price, String type, Integer proof, String description, Boolean approved, Long moment) throws DataAccessException {

        update(id, car, price, type, proof, description, approved, moment);
        

    }

    @Override
    public void updateCost(Cost cost) throws DataAccessException {
        updateCost(cost.getId(), cost.getCar(), cost.getPrice(), cost.getType(), cost.getProof(), cost.getDescription(), cost.getApproved(), cost.getMoment());
    }

    @Override
    public void deleteCost(Cost cost) throws DataAccessException {
        deleteByID(cost.getId());

    }

    @Override
    public Cost getCost(Integer car, Integer id) throws DataAccessException {
        return getCost(id);
    }
    
    @Override
    public Cost getCost(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<Cost> getCostsByCar(Integer car) throws DataAccessException {
        return getByAttr("car", car);
    }
    
    @Override
    public List<Cost> getPendingCosts() throws DataAccessException {
        return getByAttr("approved", null);
    }

}
