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
import interfaces.DamageDocDAO;
import interfaces.Filter;
import java.util.List;
import objects.Damage;
import objects.DamageDoc;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.File;

class JDBCDamageDocDAO extends JDBCDataAccessObject<DamageDoc> implements DamageDocDAO {
    
    JDBCDamageDocDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return DamageDoc.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return DamageDoc.Field.FILE;
    }

    @Override
    public Filter<DamageDoc> getFilter() {
        return new JDBCFilter<DamageDoc>() {};
    }

    @Override
    public DamageDoc createDamageDoc(Damage damage, File file) throws DataAccessException {
        create(damage, file);
        return new DamageDoc(damage.getId(), file.getId());
    }

    @Override
    public void deleteDamageDoc(DamageDoc doc) throws DataAccessException {
        deleteByAttr("file", doc.getFile());
    }

    @Override
    public DamageDoc getDamageDoc(Integer id) throws DataAccessException {
        return getByID(id);
    }

    @Override
    public List<DamageDoc> getDamageDocByDamage(Damage damage) throws DataAccessException {
        return getByAttr("damage", damage.getId());
    }
    
    @Override
    public List<DamageDoc> getDamageDocByDamage(Integer damage) throws DataAccessException {
        return getByAttr("damage", damage);
    }
    
}
