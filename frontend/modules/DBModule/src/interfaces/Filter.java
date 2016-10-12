package interfaces;

import exceptions.DataAccessException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import objects.DataObject;
import objects.DataObject.DataField;

/**
 * Abstraction of a query filter, with the sole purpose of returning a decent
 * query string.
 */
public interface Filter<T extends DataObject> {
    public void fieldIsNull(DataField field);
    
    public void fieldContains(DataField field, String content);
    public void fieldStartsWith(DataField field, String content);
    public void fieldEndsWith(DataField field, String content);

    public void fieldEqualsCaseInsensitive(DataField field, String content);
    public void fieldEquals(DataField field, Serializable item);
    public void fieldEqualsNot(DataField field, Serializable item);

    public void fieldLessThan(DataField field, Comparable item);
    public void fieldLessThanOrEquals(DataField field, Comparable item);
    public void fieldGreaterThan(DataField field, Comparable item);
    public void fieldGreaterThanOrEquals(DataField field, Comparable item);

    public void fieldBefore(DataField field, Long date);
    public void fieldAfter(DataField field, Long date);


    /**
     * Returns the filter as a (partial) SQL WHERE clause.
     *
     * The arguments are represented by question marks. Once a
     * PreparedStatement has been made, the arguments kan be filled in with
     * {@link #toWhereClause()}.
     * No spaces are added to either side of the string.
     *
     * @return The clause
     */
    public String toWhereClause(String prefix);
    public String toWhereClause();

    /**
     * Fills in the arguments of a PreparedStatement.
     *
     * This PreparedStatement has to be made previously with the SQL WHERE
     * clause given by {@link #toWhereClause}.
     *
     * @param ps The PreparedStatement for which the argument have to filled in
     */
    public void fillInArguments(PreparedStatement ps) throws DataAccessException;
    public void fillInArguments(PreparedStatement ps, int offset) throws DataAccessException;

    /**
     * Returns the amount of arguments this filter has.
     *
     * This method can be useful when constructing a PreparedStatement with
     * more arguments than solely the WHERE clause of this filter.
     *
     * @return The amount of arguments
     */
    public int getArgumentsAmount();
}
