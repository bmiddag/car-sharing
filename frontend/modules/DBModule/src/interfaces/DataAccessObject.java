package interfaces;

import exceptions.DataAccessException;
import java.util.List;
import objects.DataObject;

/**
 * Represents access to the data provider.
 * @param <T>
 */
public interface DataAccessObject<T extends DataObject> extends AutoCloseable {
    
    /**
     * Returns a filter for the fields of the DataObject.
     *
     * @return the filter
     * @see    interfaces.Filter
     */
    public Filter<T> getFilter();
    
    /**
     * Executes an SQL query, defined by this filter and returns a list of
     * results.
     *
     * @param  filter Combination of selection createria. (Can be null.)
     * @return A list of results
     * @throws DataAccessException
     * @see    interfaces.Filter
     */
    public List<T> getByFilter(Filter<T> filter) throws DataAccessException;

    /**
     * Executes an SQL query, defined by this filter and the sorting
     * preferences and returns a list of results.
     *
     * @param  filter    Combination of selection criteria. (Can be null.)
     * @param  orderBy   The field by which to order. (Can be null.)
     * @param  ascending Whether to sort in ascending or descending order.
     * @return A list of results
     * @throws DataAccessException
     * @see    interfaces.Filter
     */
    public List<T> getByFilter(Filter<T> filter, DataObject.DataField orderBy, boolean ascending) throws DataAccessException;

    /**
     * Executes an SQL query, defined by this filter, the sorting
     * preferences and pagination preferences (limit and offset) and returns a
     * list of results.
     *
     * @param  filter    Combination of selection criteria. (Can be null.)
     * @param  orderBy   The field by which to order. (Can be null.)
     * @param  ascending Whether to sort in ascending or descending order.
     * @param limit
     * @param offset
     * @return A list of results
     * @throws DataAccessException
     * @see    interfaces.Filter
     */
    public List<T> getByFilter(Filter<T> filter, DataObject.DataField orderBy, boolean ascending,
            Integer limit, Integer offset) throws DataAccessException;
    
     /**
     * Deletes the objects defined by the filter.
     *
     * @param  filter    Combination of deletion criteria. (Can be null.)
     * @throws DataAccessException
     * @see    interfaces.Filter
     */
    public void deleteByFilter(Filter<T> filter) throws DataAccessException;
    
    public List<T> getPage(int offset, int limit) throws DataAccessException;
    
    public List<T> getAll() throws DataAccessException;
            
}
