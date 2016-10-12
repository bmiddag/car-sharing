package jdbc;

import exceptions.DataAccessException;
import interfaces.Filter;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import objects.DataObject;
import objects.DataObject.DataField;
import org.apache.commons.lang3.StringUtils;

/**
 * JDBC implementation of the filter interface.
 * @param <F>
 */
abstract class JDBCFilter<T extends DataObject> implements Filter<T> {

    private final List<String> criteria;
    private final List<Object> arguments;

    public JDBCFilter() {
        this.criteria = new ArrayList<>();
        this.arguments = new ArrayList<>();
    }

    @Override
    public void fieldContains(DataField field, String content) {
        criteria.add(field.name() + " LIKE ?");
        arguments.add("%" + content + "%");
    }

    @Override
    public void fieldStartsWith(DataField field, String content) {
        criteria.add(field.name() + " LIKE ?");
        arguments.add(content + "%");
    }
        
    @Override
    public void fieldIsNull(DataField field) {
        criteria.add(field.name() + " IS NULL");        
    }

    @Override
    public void fieldEndsWith(DataField field, String content) {
        criteria.add(field.name() + " LIKE ?");
        arguments.add("%" + content);
    }

    @Override
    public void fieldEqualsCaseInsensitive(DataField field, String content) {
        criteria.add(field.name() + " LIKE ?");
        arguments.add(content);
    }
    
    @Override
    public void fieldEquals(DataField field, Serializable item) {
        if (item == null) {
            criteria.add(field.name() + " IS NULL");
        } else {
            criteria.add(field.name() + " = ?");
            arguments.add(item);
        }
    }

    @Override
    public void fieldEqualsNot(DataField field, Serializable item) {
        if (item == null) {
            criteria.add(field.name() + " IS NOT NULL");
        } else {
            criteria.add(field.name() + " <> ?");
            arguments.add(item);
        }
    }

    @Override
    public void fieldLessThan(DataField field, Comparable item) {
        criteria.add(field.name() + " < ?");
        arguments.add(item);
    }

    @Override
    public void fieldLessThanOrEquals(DataField field, Comparable item) {
        criteria.add(field.name() + " <= ?");
        arguments.add(item);
    }

    @Override
    public void fieldGreaterThan(DataField field, Comparable item) {
        criteria.add(field.name() + " > ?");
        arguments.add(item);
    }

    @Override
    public void fieldGreaterThanOrEquals(DataField field, Comparable item) {
        criteria.add(field.name() + " >= ?");
        arguments.add(item);
    }

    @Override
    public void fieldBefore(DataField field, Long date) {
        fieldLessThan(field, date);
    }

    @Override
    public void fieldAfter(DataField field, Long date) {
        fieldGreaterThan(field, date);
    }

    @Override
    public String toWhereClause() {
        return toWhereClause("");
    }
    
    @Override
    public String toWhereClause(String prefix) {
       if (criteria.isEmpty()) {
           return "";
       }
       return "WHERE " + prefix + StringUtils.join(criteria, " AND " + prefix);
    }

    @Override
    public void fillInArguments(PreparedStatement ps) throws DataAccessException {
        fillInArguments(ps, 0);
    }

    @Override
    public void fillInArguments(PreparedStatement ps, int offset) throws DataAccessException {
        int i = 1 + offset;
        for (Object argument : arguments) {
            try {
                ps.setObject(i, argument);
                i++;
            } catch (SQLException ex) {
                throw new DataAccessException("Couldn't fill in argument: " + ex.getMessage());
            }
        }
    }

    @Override
    public int getArgumentsAmount() {
        assert criteria.size() == arguments.size();
        return criteria.size();
    }
    
}
