package interfaces;

import exceptions.DataAccessException;
import java.util.List;
import objects.CarPhoto;

/**
 * This DAO Regulates access to the CarPhoto's in the database.
 */
public interface CarPhotoDAO extends DataAccessObject<CarPhoto> {
    
    public CarPhoto createCarPhoto(Integer car, Integer file) throws DataAccessException;

    public void deleteLicense(Integer file) throws DataAccessException;

    public List<CarPhoto> getPhotosByCar(Integer car) throws DataAccessException;
    
}
