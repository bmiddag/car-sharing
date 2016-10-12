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
import interfaces.InsuranceDAO;
import java.util.Date;
import objects.Car;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Insurance;

class JDBCInsuranceDAO extends JDBCDataAccessObject<Insurance> implements InsuranceDAO {

    JDBCInsuranceDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Insurance.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Insurance.Field.TIME, Insurance.Field.EDIT};
    }
    
    @Override
    protected DataField getKey() {
        return Insurance.Field.ID;
    }

    @Override
    public Filter<Insurance> getFilter() {
        return new JDBCFilter<Insurance>() {};
    }
    
    @Override
    public Insurance createInsurance(Car car) throws DataAccessException {
        return createInsurance(car, null, null, null, null, null);
    }
    
    @Override
    public Insurance createInsurance(Integer car, String number, String company, Integer bonusmalus, Boolean omnium, java.sql.Date expiration) throws DataAccessException {

        Car carObj = dac.getCarDAO().getCar(car);
        return createInsurance(carObj, number, company, bonusmalus, omnium, expiration);
        
    }
    
    @Override
    public Insurance createInsurance(Car car, String number, String company, Integer bonusmalus, Boolean omnium, java.sql.Date expiration) throws DataAccessException {

        return new Insurance(create(car, company, number, bonusmalus, omnium, expiration), car, company, number, bonusmalus, omnium, expiration, new Date().getTime(), new Date().getTime());
        
    }

    @Override
    public void updateInsurance(Insurance insurance) throws DataAccessException {
        update(insurance);
    }

    @Override
    public void deleteInsurance(Insurance insurance) throws DataAccessException {
        deleteByID(insurance.getId());
    }

    @Override
    public Insurance getInsurance(Integer id) throws DataAccessException {
        return getByID(id);
    }
    
}
