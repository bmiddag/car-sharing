package interfaces;

import objects.PriceRange;
import exceptions.DataAccessException;
import java.util.List;

/**
 * This DAO Regulates access to the PraceRanges in the database.
 */
public interface PriceRangeDAO extends DataAccessObject<PriceRange> {

    public PriceRange createPriceRange(Integer min, Integer max, Integer price) throws DataAccessException;

    /**
     *
     * @param id the id of the PraceRange to find in the db
     * @return a PraceRange object representing the range in the db; null if not found
     * @throws DataAccessException
     */
    public PriceRange getPriceRange(Integer id) throws DataAccessException;

    /**
     * 
     * @return a list of all PriceRanges in the db
     * @throws DataAccessException 
     */
    public List<PriceRange> getAllPriceRanges() throws DataAccessException;

    /**
     *
     * @param range an adapted PraceRange object to update in the db
     * @throws DataAccessException
     */
    public void updatePriceRange(PriceRange range) throws DataAccessException;

    /**
     *
     * @param range the PraceRange object to delete from the db
     * @throws DataAccessException
     */
    public void deletePriceRange(PriceRange range) throws DataAccessException;

}
