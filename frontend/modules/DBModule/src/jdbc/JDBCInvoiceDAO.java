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
import interfaces.InvoiceDAO;
import java.util.Date;
import objects.DataObject;
import objects.DataObject.DataField;
import objects.Invoice;
import objects.User;

class JDBCInvoiceDAO extends JDBCDataAccessObject<Invoice> implements InvoiceDAO {

    JDBCInvoiceDAO(JDBCDataAccessContext dac) {
        super(dac);
    }
    
    @Override
    protected DataObject.DataField[] getFields() {
        return Invoice.Field.values();
    }
    
    @Override
    protected DataField[] getStamps() {
        return new DataField[]{Invoice.Field.TIME};
    }
    
    @Override
    protected DataField getKey() {
        return Invoice.Field.ID;
    }

    @Override
    public Filter<Invoice> getFilter() {
        return new JDBCFilter<Invoice>() {};
    }
    
    @Override
    public Invoice createInvoice(User user, Integer pdf, Integer excel, Integer totalcost, Long till) throws DataAccessException {

        return new Invoice(create(user, pdf, excel, totalcost, till), user, pdf, excel, totalcost, till, System.currentTimeMillis());
    }
    
    @Override
    public Invoice createInvoice(User user, Long till) throws DataAccessException {

        return new Invoice(create(user, null, null, null, till), user, null, null, null, till, System.currentTimeMillis());
    }

    @Override
    public void updateInvoice(Invoice invoice) throws DataAccessException {
        update(invoice);
    }

    @Override
    public void deleteInvoice(Invoice invoice) throws DataAccessException {
        deleteByID(invoice.getId());
    }

    @Override
    public Invoice getInvoice(Integer id) throws DataAccessException {
        return getByID(id);
    }
    
}
