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
import interfaces.CarPhotoDAO;
import interfaces.Filter;
import java.util.List;
import objects.CarPhoto;
import objects.DataObject;
import objects.DataObject.DataField;

class JDBCCarPhotoDAO extends JDBCDataAccessObject<CarPhoto> implements CarPhotoDAO {

    JDBCCarPhotoDAO(JDBCDataAccessContext dac) {
        super(dac);
    }

    @Override
    public Filter<CarPhoto> getFilter() {
        return new JDBCFilter<CarPhoto>() {};
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return CarPhoto.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return CarPhoto.Field.FILE;
    }
    
    @Override
    public CarPhoto createCarPhoto(Integer car, Integer file) throws DataAccessException {
        create(car, file);
        return new CarPhoto(car, file);

    }

    @Override
    public void deleteLicense(Integer file) throws DataAccessException {
        deleteByAttr("file", file);

    }

    @Override
    public List<CarPhoto> getPhotosByCar(Integer car) throws DataAccessException {
        return getByAttr("car", car);
    }

}
