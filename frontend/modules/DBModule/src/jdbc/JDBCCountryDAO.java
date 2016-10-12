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
import interfaces.CountryDAO;
import interfaces.Filter;
import java.util.List;
import objects.Country;
import objects.DataObject;
import objects.DataObject.DataField;

class JDBCCountryDAO extends JDBCDataAccessObject<Country> implements CountryDAO {

    JDBCCountryDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Country.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return Country.Field.CODE;
    }

    @Override
    public Filter<Country> getFilter() {
        return new JDBCFilter<Country>() {};
    }

    @Override
    public Country createCountry(String code, String name) throws DataAccessException {
        create(code, name);
        return new Country(code, name);
    }

    @Override
    public void deleteCountry(String code) throws DataAccessException {
        deleteByAttr("code", code);

    }

    @Override
    public Country getCountry(String code) throws DataAccessException {
        return getSingleByAttr("code", code);
    }

    @Override
    public List<Country> getAllCountries() throws DataAccessException {
        return getAll();
    }

    @Override
    public void deleteCountry(Country country) throws DataAccessException {
        deleteCountry(country.getCode());
    }
    
}
