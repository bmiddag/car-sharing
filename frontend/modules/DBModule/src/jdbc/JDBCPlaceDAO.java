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
import interfaces.PlaceDAO;
import java.util.List;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Place;

class JDBCPlaceDAO extends JDBCDataAccessObject<Place> implements PlaceDAO {

    JDBCPlaceDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Place.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Place.Field.ID;
    }

    @Override
    public Filter<Place> getFilter() {
        return new JDBCFilter<Place>() {};
    }

    @Override
    public Place createPlace(String name, String zip, String countrycode) throws DataAccessException {

        return new Place(create(name, zip, countrycode), name, zip, countrycode);

    }

    @Override
    public Place getPlace(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public void updatePlace(Integer id, String name, String zip, String countrycode) throws DataAccessException {

        update(id, name, zip, countrycode);

    }

    @Override
    public void deletePlace(Place place) throws DataAccessException {
        deleteByID(place.getId());
    }

    @Override
    public List<Place> getPlacesWithZip(String zipCode) throws DataAccessException {
        return getByAttr("zip", zipCode);
    }
    
    @Override
    public void updatePlace(Place place) throws DataAccessException {
        updatePlace(place.getId(), place.getName(), place.getZip(), place.getCountryCode());
    }

    @Override
    public void deletePlace(Integer id) throws DataAccessException {
        deleteByID(id);
    }

    @Override
    public Place createPlace(String name, String zip) throws DataAccessException {
        return createPlace(name, zip, "BE");
    }

}
