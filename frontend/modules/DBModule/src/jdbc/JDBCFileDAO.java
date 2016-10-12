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
import interfaces.FileDAO;
import interfaces.Filter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.File;

class JDBCFileDAO extends JDBCDataAccessObject<File> implements FileDAO {

    JDBCFileDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return File.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{File.Field.TIME};
    }
    
    @Override
    protected DataField getKey() {
        return File.Field.ID;
    }

    @Override
    public Filter<File> getFilter() {
        return new JDBCFilter<File>() {};
    }
    
    @Override
    public File createFile(java.io.File file) throws DataAccessException {
        return createFile(file, null);
    }
    
    @Override
    public File createFile(java.io.File file, String filename) throws DataAccessException {
        try {
            return createFile(new FileInputStream(file), filename);
        } catch (FileNotFoundException ex) {
            throw new DataAccessException("Could not read file!");
        }
    }

    @Override
    public File createFile(InputStream data) throws DataAccessException {
        return createFile(data, null);
    }

    @Override
    public File createFile(InputStream data, String filename) throws DataAccessException {
      
        return new File(create(filename, data), filename, data, new Date().getTime());
        
    }

    @Override
    public void deleteFile(File file) throws DataAccessException {
        deleteByID(file.getId());
    }

    @Override
    public void deleteFile(Integer id) throws DataAccessException {
        deleteByID(id);
    }

    @Override
    public File getFile(Integer id) throws DataAccessException {
        return getByID(id);
    }
    
}
