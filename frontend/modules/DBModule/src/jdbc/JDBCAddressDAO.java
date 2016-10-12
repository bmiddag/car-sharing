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
import interfaces.AddressDAO;
import interfaces.Filter;
import java.util.List;
import objects.Address;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Place;

class JDBCAddressDAO extends JDBCDataAccessObject<Address> implements AddressDAO {
    
    JDBCAddressDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Address.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Address.Field.ID;
    }

    @Override
    public Filter<Address> getFilter() {
        return new JDBCFilter<Address>() {};
    }

    @Override
    public Address createAddress(Place place, String street, Integer number, String bus) throws DataAccessException {
        return new Address(create(street, number, bus, place), street, number, bus, place);        
    }

    @Override
    public Address getAddress(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<Address> getAddressesByPlace(Place place) throws DataAccessException {
        return getByAttr("place", place.getId());
    }

    @Override
    public void updateAddress(Address address) throws DataAccessException {
        updateAddress(address.getId(), address.getPlace(), address.getStreet(), address.getNumber(), address.getBus());
    }

    @Override
    public void deleteAddress(Address address) throws DataAccessException {
        deleteByID(address.getId());
    }

    @Override
    public Address createAddress(Integer place, String street, Integer number, String bus) throws DataAccessException {
        
        Place placeObj = dac.getPlaceDAO().getPlace(place);
        return createAddress(placeObj, street, number, bus);
        
    }

    @Override
    public List<Address> getAddressesByPlace(Integer place) throws DataAccessException {
        return getByAttr("place", place);
    }

    @Override
    public void updateAddress(Integer address, Place place, String street, Integer number, String bus) throws DataAccessException {
        updateAddress(address, place.getId(), street, number, bus);
    }

    @Override
    public void updateAddress(Integer address, Integer place, String street, Integer number, String bus) throws DataAccessException {
        
        update(address, street, number, bus, place);
    }

    @Override
    public void deleteAddress(Integer address) throws DataAccessException {
        deleteByID(address);
    }
    
}
