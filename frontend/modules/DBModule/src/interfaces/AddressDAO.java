package interfaces;

import objects.Address;
import objects.Place;
import exceptions.DataAccessException;

import java.util.List;

public interface AddressDAO extends DataAccessObject<Address> {

    /**
     * Creates a new Address with these specific parameters.
     *
     * @see objects.Address
     * @param place Parameter for the new Address
     * @param street Parameter for the new Address
     * @param number Parameter for the new Address
     * @param bus Parameter for the new Address
     * @return A new objects.Address object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Address createAddress(Place place, String street, Integer number, String bus) throws DataAccessException;
    
    
    /**
     * Creates a new Address with these specific parameters.
     *
     * @see objects.Address
     * @param place Parameter for the new Address
     * @param street Parameter for the new Address
     * @param number Parameter for the new Address
     * @param bus Parameter for the new Address
     * @return A new objects.Address object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Address createAddress(Integer place, String street, Integer number, String bus) throws DataAccessException;

    /**
     * Returns the Address with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param id The id of the Address to get from the DB
     * @return A new objects.Address object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Address getAddress(Integer id) throws DataAccessException;

    /**
     * Returns all addresses in this specific Place. [e.g. Ghent]
     *
     * @param place The place for which we want all addresses
     * @return A new objects.Address object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Address> getAddressesByPlace(Place place) throws DataAccessException;
    
    /**
     * Returns all addresses in this specific Place. [e.g. Ghent]
     *
     * @param place The place for which we want all addresses
     * @return A new objects.Address object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<Address> getAddressesByPlace(Integer place) throws DataAccessException;

    /**
     * Writes all fields of this Address to the DB.
     *
     * @param address the Address to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateAddress(Address address) throws DataAccessException;
    
    /** Deletes the specified Address.
     *
     * @param address The Address to delete
     * @param place place
     * @param street Parameter for the new Address
     * @param number Parameter for the new Address
     * @param bus Parameter for the new Address
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateAddress(Integer address, Place place, String street, Integer number, String bus) throws DataAccessException;
    
    /**
     * Deletes the specified Address.
     *
     * @param address The Address to delete
     * @param place place
     * @param street Parameter for the new Address
     * @param number Parameter for the new Address
     * @param bus Parameter for the new Address
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateAddress(Integer address, Integer place, String street, Integer number, String bus) throws DataAccessException;
    
    /**
     * Deletes the specified Address.
     *
     * @param address The Address to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteAddress(Integer address) throws DataAccessException;
    
    /**
     * Deletes the specified Address.
     *
     * @param address The Address to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteAddress(Address address) throws DataAccessException;

}
