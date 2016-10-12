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
import interfaces.ActionDAO;
import interfaces.Filter;
import java.util.List;
import objects.Action;
import objects.DataObject;
import objects.DataObject.DataField;

class JDBCActionDAO extends JDBCDataAccessObject<Action> implements ActionDAO {

    JDBCActionDAO(JDBCDataAccessContext dac) {
        super(dac);
    }

    @Override
    public Filter<Action> getFilter() {
        return new JDBCFilter<Action>() {};
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Action.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Action.Field.ID;
    }
    
    @Override
    public Action createAction(String name) throws DataAccessException {
        return createAction(name, null, null); 
    }

    @Override
    public Action createAction(String name, Long start, Long period) throws DataAccessException {
        return new Action(create(name, start, period), name, start, period);
        
    }

    @Override
    public void updateAction(Action action) throws DataAccessException {
        update(action);
        
    }

    @Override
    public void deleteAction(Action action) throws DataAccessException {
        deleteByID(action.getId());
    }

    @Override
    public Action getAction(Integer id) throws DataAccessException {
        return getByID(id);
    }
    
    @Override
    public List<Action> getActions() throws DataAccessException {
        return getAll();
    }

}
