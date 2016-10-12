package interfaces;

import objects.DamageDoc;
import objects.Damage;
import objects.File;
import exceptions.DataAccessException;
import java.util.List;

public interface DamageDocDAO extends DataAccessObject<DamageDoc> {

    /**
     * Creates a new DamageDoc with these specific parameters.
     *
     * @see objects.DamageDoc
     * @param damage Parameter for the new DamageDoc
     * @param file Parameter for the new DamageDoc
     * @return A new objects.DamageDoc object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public DamageDoc createDamageDoc(Damage damage, File file) throws DataAccessException;

    /**
     * Returns the DamageDoc with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param id The id of the DamageDoc to get from the DB
     * @return A new objects.DamageDoc object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public DamageDoc getDamageDoc(Integer id) throws DataAccessException;

    /**
     * Get all documents associated with this damagecase
     *
     * @param ride
     * @return A new objects.DamageDoc object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<DamageDoc> getDamageDocByDamage(Damage damage) throws DataAccessException;

    /**
     * Get all documents associated with this damagecase
     *
     * @param ride
     * @return A new objects.DamageDoc object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public List<DamageDoc> getDamageDocByDamage(Integer damage) throws DataAccessException;

    /**
     * Deletes the specified DamageDoc.
     *
     * @param paper The DamageDoc to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteDamageDoc(DamageDoc paper) throws DataAccessException;

}
