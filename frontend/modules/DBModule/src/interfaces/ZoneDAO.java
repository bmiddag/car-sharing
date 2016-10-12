package interfaces;

import objects.Zone;
import exceptions.DataAccessException;
import java.util.List;

/**
 * This DAO Regulates access to the Zones in the database.
 */
public interface ZoneDAO extends DataAccessObject<Zone> {

    /**
     *
     * @param name name of the new zone
     * @return Zone object representing the new zone in the db
     * @throws DataAccessException
     */
    public Zone createZone(String name) throws DataAccessException;

    /**
     *
     * @param id id of the zone to find in the db
     * @return Zone object representing the zone with the given id; null if not found
     * @throws DataAccessException
     */
    public Zone getZone(Integer id) throws DataAccessException;

    /**
     *
     * @param zone Adapted Zone object to update in the db
     * @throws DataAccessException
     */
    public void updateZone(Zone zone) throws DataAccessException;
    
    /**
     *
     * @param id Adapted Zone object to update in the db
     * @param name new zone name
     * @throws DataAccessException
     */
    public void updateZone(Integer id, String name) throws DataAccessException;

    /**
     *
     * @param zone Zone object to delete from the db
     * @throws DataAccessException
     */
    public void deleteZone(Zone zone) throws DataAccessException;
    
    /**
     *
     * @param id Zone object to delete from the db
     * @throws DataAccessException
     */
    public void deleteZone(Integer id) throws DataAccessException;

    /**
     *
     * @return a list of all zones in the db
     * @throws DataAccessException
     */
    public List<Zone> getAllZones() throws DataAccessException;

    public Zone getZoneByName(String zone) throws DataAccessException ;

}
