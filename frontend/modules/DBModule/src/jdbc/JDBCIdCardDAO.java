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
import interfaces.IdCardDAO;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.File;
import objects.IdCard;

class JDBCIdCardDAO extends JDBCDataAccessObject<IdCard> implements IdCardDAO {

    JDBCIdCardDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return IdCard.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{};
    }
    
    @Override
    protected DataField getKey() {
        return IdCard.Field.ID;
    }

    @Override
    public Filter<IdCard> getFilter() {
        return new JDBCFilter<IdCard>() {};
    }

    @Override
    public IdCard createIdCard(String number, String register, File file) throws DataAccessException {
        return createIdCard(number, register, file.getId());
    }

    @Override
    public IdCard createIdCard(String number, String register, Integer file) throws DataAccessException {
        return new IdCard(create(number, register, file), number, register, file);

    }

    @Override
    public void updateIdCard(IdCard idcard) throws DataAccessException {
        update(idcard);

    }

    @Override
    public void deleteIdCard(Integer idcard) throws DataAccessException {
        deleteByID(idcard);
    }

    @Override
    public void deleteIdCard(IdCard idcard) throws DataAccessException {
        deleteByID(idcard.getId());
    }

    @Override
    public IdCard getIdCard(Integer id) throws DataAccessException {
        return getByID(id);
    }
    
}