package interfaces;

import objects.Inscription;
import objects.InfoSession;
import objects.User;
import exceptions.DataAccessException;
import java.util.List;

public interface InscriptionDAO extends DataAccessObject<Inscription> {
    
    /**
     * Creates a new Inscription in the DB.
     * @see objects.Inscription
     * @param session 
     * @param user
     * @return A new objects.Inscription object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
    public Inscription createInscription(InfoSession session, User user) throws DataAccessException;
    
    /**
     * Returns the Inscription with the specified sessionId and userId if it exists in the DB, null otherwise.
     * @param sessionId
     * @param userId
     * @return A new objects.Inscription object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
    public Inscription getInscription(Integer sessionId, Integer userId) throws DataAccessException;
    
    public Inscription getInscription(Integer id) throws DataAccessException;
    
    /**
     * Return a List of all the inscriptions to a specific infosession.
     * @param session
     * @return A new objects.Inscription object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
    public List<Inscription> getInscriptionsByInfoSession(InfoSession session)  throws DataAccessException;
    
    /**
     * Return all infosessions this user was ever inscribed to.
     * @param user The user we're retrieveing inscriptions for.
     * @return A new objects.Inscription object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
    public List<Inscription> getInscriptionsByUser(User user)  throws DataAccessException;
    
    /**
     * Writes all fields of this Inscription to the DB.
     * @param inscription the Inscription to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
    public void updateInscription (Inscription inscription) throws DataAccessException;
    
    /**
     * Deletes the specified Inscription.
     * @param inscription The Inscription to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB connection]
     */
    public void deleteInscription (Inscription inscription) throws DataAccessException;


}
