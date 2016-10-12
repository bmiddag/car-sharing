package interfaces;

import exceptions.DataAccessException;

/**
 * Class representing a data provider. I.e. a database. Connections can be made
 * via the DataAccessContext getters. This DAP needs to be initialised with
 * connection info before first use.
 */
public interface DataAccessProvider {

    /**
     *
     * @param privilegeLevel the privilege level to be stored as context
     * @return a DAC with a privilege level as context
     * @throws DataAccessException
     */
    public DataAccessContext getDataAccessContextWithPriviligeID(int privilegeLevel) throws DataAccessException;

    /**
     *
     * @param userID the user id to be stored as context
     * @return a DAC with a user id as context
     * @throws DataAccessException
     */
    public DataAccessContext getDataAccessContextWithUserID(int userID) throws DataAccessException;

    /**
     *
     * @return a DAC providing connection to the database
     * @throws DataAccessException
     */
    public DataAccessContext getDataAccessContext() throws DataAccessException;

    /**
     * Sets the DAP up with connection info.
     *
     * @param dbname database name
     * @param dbuser username for db access
     * @param dbpasswd user password for db access
     */
    public void init(String dbname, String dbuser, String dbpasswd);

}
