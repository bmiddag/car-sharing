package interfaces;

import objects.Paper;
import objects.Insurance;
import objects.File;
import exceptions.DataAccessException;

/**
 * This DAO Regulates access to the Papers in the database.
 */
public interface PaperDAO extends DataAccessObject<Paper> {

    /**
     *
     * @param insurance the insurance to which the new Paper belongs
     * @param file the file of this new Paper
     * @return a Paper object representing this new Paper in the db
     * @throws DataAccessException
     */
    public Paper createPaper(Insurance insurance, Integer file) throws DataAccessException;

    /**
     *
     * @param id the id of the Paper to find in the db
     * @return a Paper object representing the Paper in the db; null if not
     * found
     * @throws DataAccessException
     */
    public Paper getPaper(Integer id) throws DataAccessException;

    /**
     *
     * @param paper the Paper object to delete from the db
     * @throws DataAccessException
     */
    public void deletePaper(Paper paper) throws DataAccessException;

}
