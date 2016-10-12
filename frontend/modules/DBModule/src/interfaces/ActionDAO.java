package interfaces;

import exceptions.DataAccessException;
import java.util.List;
import objects.Action;

public interface ActionDAO extends DataAccessObject<Action> {

    /**
     * Creates a new Action in the DB with this name
     *
     * @see objects.Action
     * @param name the name for the new Action
     * @return A new objects.Action object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Action createAction(String name) throws DataAccessException;

    public Action createAction(String name, Long start, Long period) throws DataAccessException;

    /**
     * Returns the Action with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param id The id of the Action to get from the DB
     * @return A new objects.Action object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Action getAction(Integer id) throws DataAccessException;

    /**
     * Returns all Actions.
     * 
     * @return A list of objects.Action objects
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Action> getActions() throws DataAccessException;

    /**
     * Writes all fields of this Action to the DB.
     *
     * @param action the Action to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateAction(Action action) throws DataAccessException;

    /**
     * Deletes the specified Action.
     *
     * @param action The Action to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteAction(Action action) throws DataAccessException;

}
