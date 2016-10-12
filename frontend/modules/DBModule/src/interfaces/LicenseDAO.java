package interfaces;

import objects.License;
import objects.File;
import exceptions.DataAccessException;

/**
 * This DAO Regulates access to the Licenses in the database.
 */
public interface LicenseDAO extends DataAccessObject<License> {

    /**
     *
     * @param number license number of the new license
     * @param file file object to prove the new license
     * @return license object representing the new license in the db
     * @throws DataAccessException
     */
    public License createLicense(String number, Integer file) throws DataAccessException;

    /**
     *
     * @param number license number of the new license
     * @param file file object to prove the new license
     * @return license object representing the new license in the db
     * @throws DataAccessException
     */
    public License createLicense(String number, File file) throws DataAccessException;

    /**
     *
     * @param id the id of the License to find in the db
     * @return a License object representing the License in the db; null if not
     * found
     * @throws DataAccessException
     */
    public License getLicense(Integer id) throws DataAccessException;

    /**
     *
     * @param license an adapted License object to update in the db
     * @throws DataAccessException
     */
    public void updateLicense(License license) throws DataAccessException;
    
    /**
     *
     * @param id id of the license
     * @param number new number
     * @param file new file
     * @throws DataAccessException
     */
    public void updateLicense(Integer id, String number, Integer file) throws DataAccessException;

    /**
     *
     * @param license the License object to delete from the db
     * @throws DataAccessException
     */
    public void deleteLicense(License license) throws DataAccessException;

    /**
     *
     * @param id the License object to delete from the db
     * @throws DataAccessException
     */
    public void deleteLicense(Integer id) throws DataAccessException;

    public License getLicenseByNumber(String license) throws DataAccessException;
    
}
