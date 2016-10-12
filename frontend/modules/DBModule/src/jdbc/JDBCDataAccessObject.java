package jdbc;

import exceptions.DataAccessException;
import interfaces.DataAccessObject;
import interfaces.Filter;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import objects.DataObject;
import objects.DataObject.DataField;
import org.apache.commons.lang3.StringUtils;

/**
 * JDBC implementation of the DataAccessObject interface.
 *
 * @param <T> DBModule object type
 */
abstract class JDBCDataAccessObject<T extends DataObject> implements DataAccessObject<T> {

    protected final Class<T> type;
    protected final String table;
    protected final String[] AUTO_GEN = {"id"};
    protected final Map<String, PreparedStatement> stmts;
    protected final Connection connection;
    protected final JDBCDataAccessContext dac;

    /**
     * Creates a new instance using the provided connection.
     *
     * @param dac the DAC providing the connection
     */
    protected JDBCDataAccessObject(JDBCDataAccessContext dac) {
        this.stmts = new HashMap<>();
        this.dac = dac;
        this.connection = dac.getConnection();
        this.type = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        this.table = type.getSimpleName();
    }

    /*°°°°°°°*\
    | BUILDER | ===============================================================
    \*°°°°°°°*/
    
    protected abstract DataField getKey();
    protected abstract DataField[] getFields();
    protected DataField[] getStamps() {
        return new DataField[]{};
    }

    protected String getJoins() {
        return getJoins(null);
    }

    protected String getJoins(String alias) {
        String tablename = alias == null ? type.getSimpleName() : alias;
        String joins = "(" + type.getSimpleName() + " " + tablename + "_table";
        for (DataField f : getFields()) {
            Class fieldtype = f.getType();
            if (DataObject.class.isAssignableFrom(fieldtype)) {
                joins = joins + " LEFT JOIN " + dac.getDAO(fieldtype).getJoins(tablename + "_" + f.name()) + " ON " + tablename + "_table" + "." + f.name()
                        + " = " + tablename + "_" + f.name() + "_table." + dac.getDAO(fieldtype).getKey().name();
            }
        }
        return joins + ")";
    }

    protected List<String> getAliases() {
        return getAliases(null);
    }

    protected List<String> getAliases(String alias) {
        List<String> aliases = new ArrayList<>();
        String tablename = alias == null ? type.getSimpleName() : alias;
        for (DataField f : getFields()) {
            Class fieldtype = f.getType();
            if (DataObject.class.isAssignableFrom(fieldtype)) {
                aliases.addAll(dac.getDAO(fieldtype).getAliases(tablename + "_" + f.name()));
            } else {
                aliases.add(tablename + "_table." + f.name() + " AS " + tablename + "_" + f.name());
            }
        }
        return aliases;
    }

    protected T parseObject(ResultSet rs) throws DataAccessException {
        return parseObject(rs, null);
    }

    protected T parseObject(ResultSet rs, String alias) throws DataAccessException {
        try {

            DataField[] fields = getFields();
            List<Class> types = new ArrayList<>();
            List<Object> data = new ArrayList<>();
            String tablename = alias == null ? type.getSimpleName() : alias;

            for (DataField f : fields) {
                Class fieldtype = f.getType();
                types.add(fieldtype);
                if (DataObject.class.isAssignableFrom(fieldtype)) {
                    data.add(dac.getDAO(fieldtype).parseObject(rs, tablename + "_" + f.name()));
                } else if (f.isStamp()) {
                    data.add(getTimeStamp(rs, tablename + "_" + f.name()));
                } else if (fieldtype.equals(InputStream.class)) {
                    InputStream stream = rs.getBinaryStream(tablename + "_" + f.name());
                    data.add(rs.wasNull() ? null : stream);
                } else {
                    data.add(getNullable(f.getType(), rs, tablename + "_" + f.name()));
                    if (f.isKey() && rs.wasNull()) {
                        return null;
                    }
                }
            }

            T result = type.getConstructor(types.toArray(new Class[types.size()])).newInstance(data.toArray());

            return result;

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new DataAccessException("Failed to parse " + type.getSimpleName() + ": " + ex.getMessage());
        } catch (NoSuchMethodException | SecurityException | SQLException ex) {
            throw new DataAccessException("Failed to parse " + type.getSimpleName() + ": " + ex.getMessage());
        }
    }

    /**
     * Executes a statement and transform the ResultSet into a List of objects.
     *
     * @param ps The PreparedStatement to execute
     * @return The list of objects
     * @throws exceptions.DataAccessException
     */
    protected List<T> executeToList(PreparedStatement ps) throws DataAccessException {
        
        try (ResultSet rs = ps.executeQuery()) {
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(parseObject(rs)); // PARSING OBJECTS IN GENERIC WAY !!
            }
            return list;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to parse list: " + ex.getMessage());
        }
    }

    /*°°°°°°*\
    | HELPER | ================================================================
    \*°°°°°°*/

    protected static <E> E getNullable(Class<E> fieldtype, ResultSet rs, String column) throws SQLException {
        E result = rs.getObject(column, fieldtype);
        return rs.wasNull() ? null : result;
    }

    protected static long getTimeStamp(ResultSet rs, String column) throws SQLException {
        //Timestamp ts = rs.getObject(column, Timestamp.class);
        Timestamp ts = rs.getTimestamp(column);
        if (rs.wasNull()) {
            return 0l;
        } else {
            return ts.getTime();
        }
    }

    /*°°°°°°*\
    | CREATE | ================================================================
    \*°°°°°°*/
    
    protected T create2(Object... data) throws DataAccessException {

        Integer result;
        PreparedStatement ps;
        try {

            ps = getCreateStmt();
            for (int i = 0; i < data.length; i++) {
                ps.setObject(i + 1, data[i]);
            }
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't create " + table + ": " + ex.getMessage());
        }

        try (ResultSet keys = ps.getGeneratedKeys()) {
            keys.first();
            result = keys.getInt("GENERATED_KEY");
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't retrieve key: " + ex.getMessage());
        }

        int i = 0;
        List<Object> obj = new ArrayList<>();
        List<Class> types = new ArrayList<>();
        ListIterator<DataField> it = new ArrayList<>(Arrays.asList(getFields())).listIterator();
        while (it.hasNext()) {
            DataField f = it.next();
            types.add(f.getType());
            if (f.name().equals("ID")) {
                obj.add(result);
            } else if (f.isStamp()) {
                obj.add(Long.valueOf(System.currentTimeMillis()));
            } else {
                obj.add(data[i++]);
            }
        }

        try {
            return (T) type.getConstructor(types.toArray(new Class[types.size()])).newInstance(obj);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new DataAccessException("Couldn't create " + table + ": " + ex.getMessage());
        }

    }

    protected Integer create(Object... data) throws DataAccessException {

        PreparedStatement ps;
        try {
            int i = 0;
            ps = getCreateStmt();
            for (DataField f : getFields()) {
                if (f.isStamp() || f.name().equals("ID")) {
                    continue;
                }
                Object o = data[i++];
                if (o != null && DataObject.class.isAssignableFrom(o.getClass())) {
                    ps.setObject(i, ((DataObject)o).getData(dac.getDAO(((DataObject)o).getClass()).getKey()));
                } else {
                    ps.setObject(i, o);
                }
            }
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't create " + table + ": " + ex.getMessage());
        }

        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.first()) {
                return keys.getInt("GENERATED_KEY");
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't retrieve key: " + ex.getMessage());
        }

    }

    protected PreparedStatement getCreateStmt() throws SQLException {
        if (stmts.get("create") == null) {
            List<DataField> fields = new ArrayList<>(Arrays.asList(getFields()));
            ListIterator<DataField> i = fields.listIterator();
            while (i.hasNext()) {
                DataField f = i.next();
                if (f.isStamp() || f.name().equals("ID")) {
                    i.remove();
                }
            }
            String stmt = "INSERT INTO " + table + "(" + StringUtils.join(fields, ", ")
                    + ") VALUES (" + StringUtils.repeat("?", ", ", fields.size()) + ");";
            stmts.put("create", dac.getConnection().prepareStatement(stmt.toString(), AUTO_GEN));
        }
        return stmts.get("create");
    }

    /*°°°°°°*\
    | SELECT | ================================================================
    \*°°°°°°*/
    
    protected T getSingleByAttr(String attr, Object value) throws DataAccessException {
        List<T> results = getByAttr(attr, value);
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    protected T getByID(Integer id) throws DataAccessException {
        return getSingleByAttr("id", id);
    }

    protected List<T> getByAttr(String attr, Object value) throws DataAccessException {
        try {
            PreparedStatement ps;
            if (value == null) {
                ps = getReceiveByNullStmt(attr);
            } else {
                ps = getReceiveByAttrStmt(attr);
                ps.setObject(1, value);
            }
            return executeToList(ps);
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get " + table + "s: " + ex.getMessage());
        }
    }

    @Override
    public List<T> getAll() throws DataAccessException {
        try {
            PreparedStatement ps = getGetAllStmt();
            return executeToList(ps);
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get " + table + "s: " + ex.getMessage());
        }
    }    

    @Override
    public List<T> getPage(int offset, int limit) throws DataAccessException {
        try {
            PreparedStatement ps = getGetPageStmt();  
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            return executeToList(ps);
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get " + table + "s: " + ex.getMessage());
        }
    }

    @Override
    public List<T> getByFilter(Filter<T> filter) throws DataAccessException {
        return getByFilter(filter, null, true);
    }

    @Override
    public List<T> getByFilter(Filter<T> filter, DataField orderBy, boolean ascending) throws DataAccessException {
        return getByFilter(filter, orderBy, ascending, null, null);
    }

    @Override
    public List<T> getByFilter(Filter<T> filter, DataField orderBy, boolean ascending,
            Integer limit, Integer offset) throws DataAccessException {

        PreparedStatement ps;
        try {
            String query = "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins();
            if (filter != null) {
                query += " " + filter.toWhereClause(type.getSimpleName() + "_table.");
            }
            if (orderBy != null) {
                query += " ORDER BY " + orderBy;
                query += ascending ? " ASC" : " DESC";
            }
            if (limit != null && offset != null) {
                query += " LIMIT " + limit;
                query += " OFFSET " + offset;
            }
            query += ";";
            ps = connection.prepareStatement(query);
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get " + table + "s: " + ex.getMessage());
        }
        filter.fillInArguments(ps);
        return executeToList(ps);

    }

    protected PreparedStatement getGetPageStmt() throws SQLException {
        if (stmts.get("all") == null) {
            String stmt = "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins() + " LIMIT ? OFFSET ?;";
            stmts.put("all", connection.prepareStatement(stmt));
        }
        return stmts.get("all");
    }

    protected PreparedStatement getGetAllStmt() throws SQLException {
        if (stmts.get("all") == null) {
            String stmt = "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins() + ";";
            stmts.put("all", connection.prepareStatement(stmt));
        }
        return stmts.get("all");
    }

    protected PreparedStatement getReceiveStmt() throws SQLException {
        return getReceiveByAttrStmt(getKey().name());
    }

    protected PreparedStatement getReceiveByAttrStmt(String attr) throws SQLException {
        if (stmts.get("receive" + attr) == null) {
            String stmt = "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins() + " WHERE " + type.getSimpleName() + "_table." + attr.toUpperCase() + " = ?;";
            stmts.put("receive" + attr, connection.prepareStatement(stmt));
            //System.out.println(stmt);
        }
        return stmts.get("receive" + attr);
    }

    protected PreparedStatement getReceiveByNullStmt(String attr) throws SQLException {
        if (stmts.get("receive" + attr) == null) {
            String stmt = "SELECT " + StringUtils.join(getAliases(), ", ") + " FROM " + getJoins() + " WHERE " + type.getSimpleName() + "_table." + attr.toUpperCase() + " IS NULL;";
            stmts.put("receive" + attr, connection.prepareStatement(stmt));
            //System.out.println(stmt);
        }
        return stmts.get("receive" + attr);
    }

    /*°°°°°°*\
    | UPDATE | ================================================================
    \*°°°°°°*/
        
    public void update(Object key, Object... obj) throws DataAccessException {

        try {
            
            int i = 0;
            PreparedStatement ps = getUpdateStmt();
            for (DataField f : getFields()) {
                if (f.isStamp() || f == getKey()) {
                    continue;
                }
                Object data = obj[i++];
                if (data != null && DataObject.class.isAssignableFrom(data.getClass())) {
                    ps.setObject(i, ((DataObject)data).getData(dac.getDAO(((DataObject)data).getClass()).getKey()));
                } else {
                    ps.setObject(i, data);
                }
            }
            ps.setObject(i+1, key);
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't update action: " + ex.toString());
        }

    }
    
    public void update(DataObject o) throws DataAccessException {

        DataField[] fields = getFields();
        List<DataField> columns = new ArrayList<>();
        DataField key = null;
        for (DataField f : fields) {
            if (f.isKey()) {
                key = f;
            } else if (!f.isStamp()) {
                columns.add(f);
            }
        }
        
        try {
            
            int i = 0;
            ListIterator<DataField> iterator = columns.listIterator();
            PreparedStatement ps = getUpdateStmt();
            while (iterator.hasNext()) {
                i = iterator.nextIndex();
                DataField f = iterator.next();
                Object data = o.getData(f);
                if (data != null && DataObject.class.isAssignableFrom(f.getType())) {
                    ps.setObject(i + 1, ((DataObject)data).getData(dac.getDAO(f.getType()).getKey()));
                } else {
                    ps.setObject(i + 1, data);
                }
            }
            ps.setObject(i+2, o.getData(key));
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't update action: " + ex.toString());
        }

    }
    
    protected void updateAttrByID(Integer id, String attr, Object value) throws DataAccessException {
        try {

            PreparedStatement ps = getUpdateAttrStmt(attr);
            ps.setObject(1, value);
            ps.setObject(2, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't update " + table + ": " + ex.getMessage());
        }
    }

    protected PreparedStatement getUpdateStmt() throws SQLException {
        if (stmts.get("update") == null) {
            
            DataField[] fields = getFields();
            List<DataField> columns = new ArrayList<>();
            DataField key = null;
            for (DataField f : fields) {
                if (f.isKey()) {
                    key = f;
                } else if (!f.isStamp()) {
                    columns.add(f);
                }
            }
            
            String stmt = "UPDATE " + table + " SET " + StringUtils.join(columns, " = ?, ") + " = ?" 
                    + " WHERE " + key.name() + " = ?;";
            stmts.put("update", dac.getConnection().prepareStatement(stmt));
        }
        return stmts.get("update");
    }

    protected PreparedStatement getUpdateAttrStmt(String attr) throws SQLException {
        if (stmts.get("update" + attr) == null) {
            String stmt = "UPDATE " + table + " SET " + attr + " = ?" + " WHERE " + getKey() + " = ?;";
            stmts.put("update" + attr, dac.getConnection().prepareStatement(stmt));
        }
        return stmts.get("update" + attr);
    }
    
    /*°°°°°°*\
    | DELETE | ================================================================
    \*°°°°°°*/
    
    public void deleteByID(int id) throws DataAccessException {
        deleteByAttr("id", id);
    }

    public void deleteByAttr(String attr, Object value) throws DataAccessException {
        try {
            PreparedStatement ps = getDeleteByAttrStmt(attr);
            ps.setObject(1, value);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't delete " + table + ": " + ex.getMessage());
        }
    }

    @Override
    public void deleteByFilter(Filter<T> filter) throws DataAccessException {
        try {
            String query = "DELETE FROM " + table;
            if (filter != null) {
                query += " " + filter.toWhereClause();
            }
            query += ";";
            PreparedStatement ps = connection.prepareStatement(query);
            filter.fillInArguments(ps);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Couldn't get " + table + "s: " + ex.getMessage());
        }
    }

    protected PreparedStatement getDeleteStmt() throws SQLException {
        return getDeleteByAttrStmt(getKey().name());
    }

    protected PreparedStatement getDeleteByAttrStmt(String attr) throws SQLException {
        if (stmts.get("delete") == null) {
            String stmt = "DELETE FROM " + table + " WHERE " + attr + " = ?;";
            stmts.put("delete", connection.prepareStatement(stmt));
        }
        return stmts.get("delete");
    }

    /*°°°°°*\
    | OTHER | ================================================================
    \*°°°°°*/
    
    @Override
    public void close() {
        for (Map.Entry<String, PreparedStatement> entry : stmts.entrySet()) {
            try {
                entry.getValue().close();
            } catch (SQLException ex) {
                /* Do nothing */
            }
        }
    }

    public void printTableColumnsToCsv(String file) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * INTO OUTFILE '" + file + "'"
                    + "FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"'"
                    + "ESCAPED BY ‘\\\\’"
                    + "LINES TERMINATED BY '\n'"
                    + "FROM " + table + ";");
            ps.executeUpdate();
        } catch (SQLException ex) {
            /* Quiet fail :( */
        }
    }

}
