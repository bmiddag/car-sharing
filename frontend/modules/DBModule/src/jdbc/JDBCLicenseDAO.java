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
import interfaces.LicenseDAO;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.File;
import objects.License;

class JDBCLicenseDAO extends JDBCDataAccessObject<License> implements LicenseDAO {

    JDBCLicenseDAO(JDBCDataAccessContext dac) {
        super(dac);
    }

    @Override
    public Filter<License> getFilter() {
        return new JDBCFilter<License>() {};
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return License.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return License.Field.ID;
    }
    
    @Override
    public License createLicense(String number, Integer file) throws DataAccessException {
        return new License(create(number, file), number, file);

    }

    @Override
    public void updateLicense(Integer id, String number, Integer file) throws DataAccessException {
        update(id, number, file);
    }

    @Override
    public void updateLicense(License license) throws DataAccessException {
        updateLicense(license.getId(), license.getNumber(), license.getFile());
    }

    @Override
    public void deleteLicense(License license) throws DataAccessException {
        deleteByID(license.getId());

    }

    @Override
    public License getLicense(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public License createLicense(String number, File file) throws DataAccessException {
        return createLicense(number, file.getId());
    }

    @Override
    public void deleteLicense(Integer id) throws DataAccessException {
        deleteByID(id);
    }

    @Override
    public License getLicenseByNumber(String license) throws DataAccessException {
        return getSingleByAttr("license", license);
    }
    
}
