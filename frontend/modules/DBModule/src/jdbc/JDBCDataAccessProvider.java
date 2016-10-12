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
import interfaces.DataAccessProvider;
import interfaces.DataAccessContext;
import java.sql.*;

public class JDBCDataAccessProvider implements DataAccessProvider {

    private String dbname = "test";
    private String dbuser = "root";
    private String dbpasswd = "root";

    private static final DataAccessProvider dap = new JDBCDataAccessProvider();

    private JDBCDataAccessProvider() {
        /* Empty private (singleton) contructor */
    }

    @Override
    public void init(String dbname, String dbuser, String dbpasswd) {
        this.dbname = dbname;
        this.dbuser = dbuser;
        this.dbpasswd = dbpasswd;
    }

    /**
     * Provides a static DAP object. THIS NEEDS TO BE INITIALISED!!
     * @return a static DAP
     */
    public static DataAccessProvider getDataAccessProvider() {
        return dap;
    }

    private Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Couldn't find the JDBC Driver");
        }

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname + "?noAccessToProcedureBodies=true", dbuser, dbpasswd);
        //connection.setClientInfo("noAccessToProcedureBodies", "true");
        return connection;
    }

    @Override
    public DataAccessContext getDataAccessContext() throws DataAccessException {
        
        try {
            return new JDBCDataAccessContext(getConnection());
        } catch (SQLException e) {
            throw new DataAccessException("Could not create DAC: " + e.toString());
        }

    }

    @Override
    public DataAccessContext getDataAccessContextWithPriviligeID(int privilegeLevel) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataAccessContext getDataAccessContextWithUserID(int userID) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
