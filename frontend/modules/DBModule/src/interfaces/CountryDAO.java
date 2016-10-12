package interfaces;

import objects.Country;
import exceptions.DataAccessException;
import java.util.List;

public interface CountryDAO extends DataAccessObject<Country> {

    /**
     * Creates a new Country with these specific parameters.
     *
     * @see objects.Country
     * @param name Country name, [e.g. Belgium]
     * @param code Country Code [e.g. BE]
     * @return A new objects.Country object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Country createCountry(String name, String code) throws DataAccessException;

    /**
     * Returns the Country with the specified code if it exists in the DB, null
     * otherwise.
     *
     * @param code
     * @return A new objects.Country object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Country getCountry(String code) throws DataAccessException;

    /**
     * Return a list of all Countries in the DB.
     *
     * @return a list of all Countries in the DB.
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Country> getAllCountries() throws DataAccessException;

    /**
     * Deletes the specified Country.
     *
     * @param country The Country to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteCountry(Country country) throws DataAccessException;

    /**
     * Deletes the specified Country.
     *
     * @param code The Country to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteCountry(String code) throws DataAccessException;

}
