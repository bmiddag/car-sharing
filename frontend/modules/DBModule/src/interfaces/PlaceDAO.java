package interfaces;

import objects.Place;
import exceptions.DataAccessException;
import java.util.List;

/**
 * This DAO Regulates access to the Places in the database.
 */
public interface PlaceDAO extends DataAccessObject<Place> {

    /**
     *
     * @param name the name of the new place
     * @param zip the zip/postal code of the new place
     * @return a Place object representing the new place in the db
     * @throws DataAccessException
     */
    public Place createPlace(String name, String zip) throws DataAccessException;

    /**
     *
     * @param name the name of the new place
     * @param zip the zip/postal code of the new place
     * @param countrycode the country of the place
     * @return a Place object representing the new place in the db
     * @throws DataAccessException
     */
    public Place createPlace(String name, String zip, String countrycode) throws DataAccessException;

    /**
     *
     * @param id the id of the Place to find in the db
     * @return a Place object representing the Place in the db; null if not
     * found
     * @throws DataAccessException
     */
    public Place getPlace(Integer id) throws DataAccessException;

    /**
     *
     * @param place an adapted Place object to update in the db
     * @throws DataAccessException
     */
    public void updatePlace(Place place) throws DataAccessException;

    /**
     *
     * @param id an adapted Place object to update in the db
     * @param name name of the place
     * @param zip zip code
     * @param countrycode country
     * @throws DataAccessException
     */
    public void updatePlace(Integer id, String name, String zip, String countrycode) throws DataAccessException;

    /**
     *
     * @param place the Place object to delete from the db
     * @throws DataAccessException
     */
    public void deletePlace(Place place) throws DataAccessException;

    /**
     *
     * @param place the Place object to delete from the db
     * @throws DataAccessException
     */
    public void deletePlace(Integer id) throws DataAccessException;

    /**
     *
     * @param zipCode zip/postal code to search in the db
     * @return a list of all Places with the given code
     * @throws DataAccessException
     */
    public List<Place> getPlacesWithZip(String zipCode) throws DataAccessException;

}
