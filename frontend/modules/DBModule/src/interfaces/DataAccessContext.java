package interfaces;

import exceptions.DataAccessException;
import interfaces.ReservationRangeDAO;
import objects.DataObject;

/**
 * Provides a connection to the database. Access to data can be obtained
 * with DAO get methods.
 */
public interface DataAccessContext extends AutoCloseable {

    @Override
    public void close();
    
    /**
     * Begins a new transaction to the db.
     * @throws DataAccessException 
     */
    public void begin() throws DataAccessException;

    /**
     * Commits the current transaction to the db.
     * @throws DataAccessException 
     */
    public void commit() throws DataAccessException;

    /**
     * Rolls back the changes of the current transaction.
     * @throws DataAccessException 
     */
    public void rollback() throws DataAccessException;

    /**
     * 
     * @return ActionDAO providing access to Action data in the db
     */
    public ActionDAO getActionDAO();

    /**
     * 
     * @return AddressDAO providing access to Address data in the db
     */
    public AddressDAO getAddressDAO();

    /**
     * 
     * @return CarDAO providing access to Car data in the db
     */
    public CarPhotoDAO getCarPhotoDAO();

    /**
     * 
     * @return CarDAO providing access to Car data in the db
     */
    public CarDAO getCarDAO();

    /**
     * 
     * @return CountryDAO providing access to Country data in the db
     */
    public CountryDAO getCountryDAO();

    /**
     * 
     * @return MessageDAO providing access to Message data in the db
     */
    public NotificationDAO getNotificationDAO();
    
    /**
     * 
     * @return MessageDAO providing access to Message data in the db
     */
    public TokenDAO getTokenDAO();

    /**
     * 
     * @return FileDAO providing access to File data in the db
     */
    public FileDAO getFileDAO();

    /**
     * 
     * @return UserDAO providing access to User data in the db
     */
    public UserDAO getUserDAO();

    /**
     * 
     * @return IdCardDAO providing access to IdCard data in the db
     */
    public IdCardDAO getIdCardDAO();

    /**
     * 
     * @return InfoSessionDAO providing access to InfoSession data in the db
     */
    public InfoSessionDAO getInfoSessionDAO();

    /**
     * 
     * @return InscriptionDAO providing access to Inscription data in the db
     */
    public InscriptionDAO getInscriptionDAO();

    /**
     * 
     * @return CostDAO providing access to Cost data in the db
     */
    public CostDAO getCostDAO();

    /**
     * 
     * @return CommentDAO providing access to Comment data in the db
     */
    public CommentDAO getCommentDAO();

    /**
     * 
     * @return PlaceDAO providing access to Place data in the db
     */
    public PlaceDAO getPlaceDAO();

    /**
     * 
     * @return PriceRangeDAO providing access to PriceRange data in the db
     */
    public PriceRangeDAO getPriceRangeDAO();

    /**
     * 
     * @return PrivilegeDAO providing access to Privilege data in the db
     */
    public PrivilegeDAO getPrivilegeDAO();

    /**
     * 
     * @return ReservationDAO providing access to Reservation data in the db
     */
    public ReservationDAO getReservationDAO();

    /**
     *
     * @return ReservationRangeDAO providing access to ReservationRange data in the db
     */
    public ReservationRangeDAO getReservationRangeDAO();


    /**
     * 
     * @return LicenseDAO providing access to License data in the db
     */
    public LicenseDAO getLicenseDAO();

    /**
     * 
     * @return RideDAO providing access to Ride data in the db
     */
    public RideDAO getRideDAO();

    /**
     * 
     * @return RoleDAO providing access to Role data in the db
     */
    public RoleDAO getRoleDAO();

    /**
     * 
     * @return DamageDAO providing access to Damage data in the db
     */
    public DamageDAO getDamageDAO();

    /**
     * 
     * @return DamageDocDAO providing access to DamageDoc data in the db
     */
    public DamageDocDAO getDamageDocDAO();

    /**
     * 
     * @return TemplateDAO providing access to Template data in the db
     */
    public TemplateDAO getTemplateDAO();

    /**
     * 
     * @return RefuelingDAO providing access to Refueling data in the db
     */
    public RefuelingDAO getRefuelingDAO();

    /**
     * 
     * @return InsuranceDAO providing access to Insurance data in the db
     */
    public InsuranceDAO getInsuranceDAO();

    /**
     * 
     * @return PaperDAO providing access to Paper data in the db
     */
    public PaperDAO getPaperDAO();

    /**
     * 
     * @return ZoneDAO providing access to Zone data in the db
     */
    public ZoneDAO getZoneDAO();

    /**
     *
     * @return PermissionDAO providing access to Permission data in the db
     */
    public PermissionDAO getPermissionDAO();

    /**
     *
     * @return UserPermissionDAO providing access to UserPermission data in the db
     */
    public UserPermissionDAO getUserPermissionDAO();

    /**
     *
     * @return InvoiceDAO providing access to Invoice data in the db
     */
    public InvoiceDAO getInvoiceDAO();

}
