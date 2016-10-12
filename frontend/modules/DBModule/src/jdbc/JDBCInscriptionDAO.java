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
import interfaces.InscriptionDAO;
import java.sql.*;
import java.util.List;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.InfoSession;
import objects.Inscription;
import objects.User;
import org.apache.commons.lang3.StringUtils;

class JDBCInscriptionDAO extends JDBCDataAccessObject<Inscription> implements InscriptionDAO {

    JDBCInscriptionDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Inscription.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Inscription.Field.ID;
    }

    @Override
    public Filter<Inscription> getFilter() {
        return new JDBCFilter<Inscription>() {};
    }

    @Override
    public Inscription createInscription(InfoSession session, User user) throws DataAccessException {
        
        return new Inscription(create(session, user, false), session, user, false);
    }

    @Override
    public void updateInscription(Inscription inscription) throws DataAccessException {
        update(inscription);
    }

    @Override
    public void deleteInscription(Inscription inscription) throws DataAccessException {
        deleteByID(inscription.getId());
    }

    @Override
    public Inscription getInscription(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<Inscription> getInscriptionsByInfoSession(InfoSession session) throws DataAccessException {
        return getByAttr("session", session.getId());
    }

    @Override
    public List<Inscription> getInscriptionsByUser(User user) throws DataAccessException {
        return getByAttr("user", user.getId());
    }

    @Override
    public Inscription getInscription(Integer sessionId, Integer userId) throws DataAccessException {
        try {
            if (stmts.get("receiveBySessionAndUser") == null) {
                stmts.put("receiveBySessionAndUser", connection.prepareStatement(
                        "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins() + " WHERE Inscription_table.SESSION = ? AND Inscription_table.USER = ?;"));
            }
            PreparedStatement ps = stmts.get("receiveBySessionAndUser");
            ps.setObject(1, sessionId);
            ps.setObject(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return parseObject(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get Inscription: " + ex.getMessage());
        }
    }

}
