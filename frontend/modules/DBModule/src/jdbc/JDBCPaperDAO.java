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
import interfaces.PaperDAO;
import java.sql.*;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Insurance;
import objects.Paper;

class JDBCPaperDAO extends JDBCDataAccessObject<Paper> implements PaperDAO {
   
    JDBCPaperDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Paper.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Paper.Field.FILE;
    }

    @Override
    public Filter<Paper> getFilter() {
        return new JDBCFilter<Paper>() {};
    }

    @Override
    public Paper createPaper(Insurance insurance, Integer file) throws DataAccessException {

        create(insurance, file);
        return new Paper(insurance.getId(), file);

    }

    @Override
    public void deletePaper(Paper paper) throws DataAccessException {
        deleteByAttr("file", paper.getFile());

    }

    @Override
    public Paper getPaper(Integer id) throws DataAccessException {
        return getSingleByAttr("file", id);
    }
    
}
