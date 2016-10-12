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
import interfaces.ZoneDAO;
import java.util.List;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Zone;

class JDBCZoneDAO extends JDBCDataAccessObject<Zone> implements ZoneDAO {

    JDBCZoneDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Zone.Field.values();
    }

    @Override
    public Filter<Zone> getFilter() {
        return new JDBCFilter<Zone>() {};
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Zone.Field.ID;
    }
    
    @Override
    public Zone createZone(String name) throws DataAccessException {
        return new Zone(create(name), name);
    }
    
    @Override
    public void updateZone(Integer id, String name) throws DataAccessException {
        update(id, name);
    }

    @Override
    public void updateZone(Zone zone) throws DataAccessException {
        updateZone(zone.getId(), zone.getName());        
    }

    @Override
    public Zone getZone(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<Zone> getAllZones() throws DataAccessException {
        return getAll();
    }

    @Override
    public void deleteZone(Integer id) throws DataAccessException {
        deleteByID(id);
    }

    @Override
    public void deleteZone(Zone zone) throws DataAccessException {
        deleteByID(zone.getId());
    }

    @Override
    public Zone getZoneByName(String zone) throws DataAccessException {
        return getSingleByAttr("zone", zone);
    }
    
}
