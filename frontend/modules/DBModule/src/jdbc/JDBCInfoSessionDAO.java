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
import interfaces.InfoSessionDAO;
import java.util.List;
import objects.Address;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.InfoSession;

class JDBCInfoSessionDAO extends JDBCDataAccessObject<InfoSession> implements InfoSessionDAO {

    JDBCInfoSessionDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return InfoSession.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return InfoSession.Field.ID;
    }

    @Override
    public Filter<InfoSession> getFilter() {
        return new JDBCFilter<InfoSession>() {};
    }
    
    @Override
    public InfoSession createInfoSession(Address address, Long date, Boolean owners, Integer places) throws DataAccessException {

        return new InfoSession(create(address, date, owners, places), address, date, owners, places);
    }

    @Override
    public void updateInfoSession(InfoSession session) throws DataAccessException {
        update(session);

    }

    @Override
    public void deleteInfoSession(InfoSession session) throws DataAccessException {
        deleteByID(session.getId());
    }

    @Override
    public InfoSession getInfoSession(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<InfoSession> getInfoSessions() throws DataAccessException {
        return getAll();
    }
    
}
