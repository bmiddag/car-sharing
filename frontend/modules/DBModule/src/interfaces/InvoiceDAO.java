package interfaces;

import objects.Invoice;
import objects.User;
import exceptions.DataAccessException;

public interface InvoiceDAO extends DataAccessObject<Invoice> {

    /**
     * Creates a new Invoice with these specific parameters.
     *
     * @see objects.Comment
     * @param user User paying the invoice
     * @param till date till which costs are accumulated
     * @return A new objects.Invoice object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Invoice createInvoice(User user, Long till) throws DataAccessException;
    public Invoice createInvoice(User user, Integer pdf, Integer excel, Integer totalcost, Long till) throws DataAccessException;

    /**
     * Returns the Comment with the specified id if it exists in the DB, null
     * otherwise.
     *
     * @param id The id of the Invoice to get from the DB
     * @return A new objects.Invoice object
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public Invoice getInvoice(Integer id) throws DataAccessException;

    /**
     * Writes all fields of this Invoice to the DB.
     *
     * @param invoice the Invoice to update
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void updateInvoice(Invoice invoice) throws DataAccessException;

    /**
     * Deletes the specified Comment.
     *
     * @param invoice The Invoice to delete
     * @throws DataAccessException whenever something goes wrong. [e.g. No DB
     * connection]
     */
    public void deleteInvoice(Invoice invoice) throws DataAccessException;

}
