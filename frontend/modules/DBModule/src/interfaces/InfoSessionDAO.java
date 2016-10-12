package interfaces;

import objects.InfoSession;
import objects.Address;
import exceptions.DataAccessException;
import java.util.List;

public interface InfoSessionDAO extends DataAccessObject<InfoSession> {

    /**
     * Creates a new InfoSession with these specific parameters.
     *
     * @see objects.InfoSession
     * @param address The address of the new Infosession
     * @param date The date of the new Infosession
     * @param owners Is it a carowners only infosession?
     * @param places The number of available places in the new Infosession
     * @return A new objects.InfoSession object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public InfoSession createInfoSession(Address address, Long date, Boolean owners, Integer places) throws DataAccessException;

    /**
     * Returns the InfoSession with the specified id if it exists in the DB,
     * null otherwise.
     *
     * @param id The id of the InfoSession to get from the DB
     * @return A new objects.InfoSession object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public InfoSession getInfoSession(Integer id) throws DataAccessException;

    /**
     * Returns a List of all infosessions.
     *
     * @return A list of all infosessions.
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<InfoSession> getInfoSessions() throws DataAccessException;

    /**
     * Writes all fields of this InfoSession to the DB.
     *
     * @param session the InfoSession to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateInfoSession(InfoSession session) throws DataAccessException;

    /**
     * Deletes the specified InfoSession.
     *
     * @param session The InfoSession to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteInfoSession(InfoSession session) throws DataAccessException;

}
