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

import interfaces.DataAccessContext;
import exceptions.*;
import interfaces.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdbc.JDBCReservationRangeDAO;
import objects.*;
import objects.ReservationRange;

class JDBCDataAccessContext implements DataAccessContext {
    
    private final Map<Class<? extends DataObject>, JDBCDataAccessObject> daos = new HashMap<>();
    private final Connection connection;
    private Savepoint savePoint;
    
    /**
     * Constructs a DAP with a SightlyBetterConnection to a db.
     * @param connection 
     */
    public JDBCDataAccessContext(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void begin() throws DataAccessException {
        try {
            connection.setAutoCommit(false);
            savePoint = connection.setSavepoint();
        } catch (SQLException ex) {
            throw new DataAccessException("An error occurred starting the connection." + ex.getMessage());
        }
    }

    @Override
    public void commit() throws DataAccessException {
        
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw new DataAccessException("Error committing to the DB: " + ex.getMessage());
        }
        
        try {
            savePoint = connection.setSavepoint();
        } catch (SQLException ex) {
            throw new DataAccessException("Commit succesful, but couldn't create a savepoint [Which commits should do automatically!]: " + ex.getMessage());
        }
        
    }

    @Override
    public void rollback() throws DataAccessException {
        try {
            connection.rollback(savePoint);
        } catch (SQLException ex) {
            throw new DataAccessException("Roleback failed: " + ex.getMessage());
        }
    }

    @Override
    public void close() {
        
        for (Map.Entry<Class<? extends DataObject>, JDBCDataAccessObject> entry : daos.entrySet()) try {
            entry.getValue().close();
        } catch (Exception ex) {
            /* Do nothing */
        }
        
        try {
            connection.close();
        } catch (SQLException ex) {
            /* Do nothing */
        }
        
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public JDBCDataAccessObject getDAO(Class<? extends DataObject> type) {
        String className = type.getSimpleName();
        switch (className) {
            case "Action":
                    return (JDBCDataAccessObject) getActionDAO();
            case "Address":
                    return (JDBCDataAccessObject) getAddressDAO();
            case "Car":
                    return (JDBCDataAccessObject) getCarDAO();
            case "CarPhoto":
                    return (JDBCDataAccessObject) getCarPhotoDAO();
            case "Country":
                    return (JDBCDataAccessObject) getCountryDAO();
            case "File":
                    return (JDBCDataAccessObject) getFileDAO();
            case "User":
                    return (JDBCDataAccessObject) getUserDAO();
            case "IdCard":
                    return (JDBCDataAccessObject) getIdCardDAO();
            case "InfoSession":
                    return (JDBCDataAccessObject) getInfoSessionDAO();
            case "Inscription":
                    return (JDBCDataAccessObject) getInscriptionDAO();
            case "Cost":
                    return (JDBCDataAccessObject) getCostDAO();
            case "Comment":
                    return (JDBCDataAccessObject) getCommentDAO();
            case "Place":
                    return (JDBCDataAccessObject) getPlaceDAO();
            case "PriceRange":
                    return (JDBCDataAccessObject) getPriceRangeDAO();
            case "Privilege":
                    return (JDBCDataAccessObject) getPrivilegeDAO();
            case "Reservation":
                    return (JDBCDataAccessObject) getReservationDAO();
            case "License":
                    return (JDBCDataAccessObject) getLicenseDAO();
            case "Ride":
                    return (JDBCDataAccessObject) getRideDAO();
            case "Role":
                    return (JDBCDataAccessObject) getRoleDAO();
            case "Damage":
                    return (JDBCDataAccessObject) getDamageDAO();
            case "DamageDoc":
                    return (JDBCDataAccessObject) getDamageDocDAO();
            case "Refueling":
                    return (JDBCDataAccessObject) getRefuelingDAO();
            case "Insurance":
                    return (JDBCDataAccessObject) getInsuranceDAO();
            case "Paper":
                    return (JDBCDataAccessObject) getPaperDAO();
            case "Zone":
                    return (JDBCDataAccessObject) getZoneDAO();
            case "Template":
                    return (JDBCDataAccessObject) getTemplateDAO();
            case "Permission":
                    return (JDBCDataAccessObject) getPermissionDAO();
            case "UserPermission":
                    return (JDBCDataAccessObject) getUserPermissionDAO();
            case "Notification":
                    return (JDBCDataAccessObject) getNotificationDAO();
            case "Invoice":
                    return (JDBCDataAccessObject) getInvoiceDAO();
            case "Token":
                    return (JDBCDataAccessObject) getTokenDAO();
            default: return null;                
        }
    }
    
    
    @Override
    public NotificationDAO getNotificationDAO() {
        if (daos.get(Notification.class) == null) daos.put(Notification.class, new JDBCNotificationDAO(this));
        return (NotificationDAO) daos.get(Notification.class);    
    }

    @Override
    public InvoiceDAO getInvoiceDAO() {
        if (daos.get(Invoice.class) == null) daos.put(Invoice.class, new JDBCInvoiceDAO(this));
        return (InvoiceDAO) daos.get(Invoice.class);
    }

    @Override
    public TokenDAO getTokenDAO() {
        if (daos.get(Token.class) == null) daos.put(Token.class, new JDBCTokenDAO(this));
        return (TokenDAO) daos.get(Token.class);    }

    @Override
    public ActionDAO getActionDAO() {
        if (daos.get(Action.class) == null) daos.put(Action.class, new JDBCActionDAO(this));
        return (ActionDAO) daos.get(Action.class);    
    }

    @Override
    public AddressDAO getAddressDAO() {
        if (daos.get(Address.class) == null) daos.put(Address.class, new JDBCAddressDAO(this));
        return (AddressDAO) daos.get(Address.class);    
    }

    @Override
    public CarDAO getCarDAO() {
        if (daos.get(Car.class) == null) daos.put(Car.class, new JDBCCarDAO(this));
        return (CarDAO) daos.get(Car.class);    
    }

    @Override
    public CarPhotoDAO getCarPhotoDAO() {
        if (daos.get(CarPhoto.class) == null) daos.put(CarPhoto.class, new JDBCCarPhotoDAO(this));
        return (CarPhotoDAO) daos.get(CarPhoto.class);    
    }
    
    @Override
    public CountryDAO getCountryDAO() {
        if (daos.get(Country.class) == null) daos.put(Country.class, new JDBCCountryDAO(this));
        return (CountryDAO) daos.get(Country.class);    
    }

    @Override
    public FileDAO getFileDAO() {
        if (daos.get(File.class) == null) daos.put(File.class, new JDBCFileDAO(this));
        return (FileDAO) daos.get(File.class);    
    }

    @Override
    public UserDAO getUserDAO() {
        if (daos.get(User.class) == null) daos.put(User.class, new JDBCUserDAO(this));
        return (UserDAO) daos.get(User.class);    
    }

    @Override
    public IdCardDAO getIdCardDAO() {
        if (daos.get(IdCard.class) == null) daos.put(IdCard.class, new JDBCIdCardDAO(this));
        return (IdCardDAO) daos.get(IdCard.class);    
    }

    @Override
    public InfoSessionDAO getInfoSessionDAO() {
        if (daos.get(InfoSession.class) == null) daos.put(InfoSession.class, new JDBCInfoSessionDAO(this));
        return (InfoSessionDAO) daos.get(InfoSession.class);    
    }

    @Override
    public InscriptionDAO getInscriptionDAO() {
        if (daos.get(Inscription.class) == null) daos.put(Inscription.class, new JDBCInscriptionDAO(this));
        return (InscriptionDAO) daos.get(Inscription.class);    
    }

    @Override
    public CostDAO getCostDAO() {
        if (daos.get(Cost.class) == null) daos.put(Cost.class, new JDBCCostDAO(this));
        return (CostDAO) daos.get(Cost.class);    
    }

    @Override
    public CommentDAO getCommentDAO() {
        if (daos.get(Comment.class) == null) daos.put(Comment.class, new JDBCCommentDAO(this));
        return (CommentDAO) daos.get(Comment.class);    
    }

    @Override
    public PlaceDAO getPlaceDAO() {
        if (daos.get(Place.class) == null) daos.put(Place.class, new JDBCPlaceDAO(this));
        return (PlaceDAO) daos.get(Place.class);    
    }

    @Override
    public PriceRangeDAO getPriceRangeDAO() {
        if (daos.get(PriceRange.class) == null) daos.put(PriceRange.class, new JDBCPriceRangeDAO(this));
        return (PriceRangeDAO) daos.get(PriceRange.class);    
    }

    @Override
    public PrivilegeDAO getPrivilegeDAO() {
        if (daos.get(Privilege.class) == null) daos.put(Privilege.class, new JDBCPrivilegeDAO(this));
        return (PrivilegeDAO) daos.get(Privilege.class);    
    }

    @Override
    public ReservationDAO getReservationDAO() {
        if (daos.get(Reservation.class) == null) daos.put(Reservation.class, new JDBCReservationDAO(this));
        return (ReservationDAO) daos.get(Reservation.class);    
    }

    @Override
    public ReservationRangeDAO getReservationRangeDAO() {
        if (daos.get(ReservationRange.class) == null) daos.put(ReservationRange.class, new JDBCReservationRangeDAO(this));
        return (ReservationRangeDAO) daos.get(ReservationRange.class);
    }

    @Override
    public LicenseDAO getLicenseDAO() {
        if (daos.get(License.class) == null) daos.put(License.class, new JDBCLicenseDAO(this));
        return (LicenseDAO) daos.get(License.class);    
    }

    @Override
    public RideDAO getRideDAO() {
        if (daos.get(Ride.class) == null) daos.put(Ride.class, new JDBCRideDAO(this));
        return (RideDAO) daos.get(Ride.class);    
    }

    @Override
    public RoleDAO getRoleDAO() {
        if (daos.get(Role.class) == null) daos.put(Role.class, new JDBCRoleDAO(this));
        return (RoleDAO) daos.get(Role.class);    
    }

    @Override
    public DamageDAO getDamageDAO() {
        if (daos.get(Damage.class) == null) daos.put(Damage.class, new JDBCDamageDAO(this));
        return (DamageDAO) daos.get(Damage.class);    
    }

    @Override
    public DamageDocDAO getDamageDocDAO() {
        if (daos.get(DamageDoc.class) == null) daos.put(DamageDoc.class, new JDBCDamageDocDAO(this));
        return (DamageDocDAO) daos.get(DamageDoc.class);    
    }

    @Override
    public RefuelingDAO getRefuelingDAO() {
        if (daos.get(Refueling.class) == null) daos.put(Refueling.class, new JDBCRefuelingDAO(this));
        return (RefuelingDAO) daos.get(Refueling.class);    
    }

    @Override
    public InsuranceDAO getInsuranceDAO() {
        if (daos.get(Insurance.class) == null) daos.put(Insurance.class, new JDBCInsuranceDAO(this));
        return (InsuranceDAO) daos.get(Insurance.class);    
    }

    @Override
    public PaperDAO getPaperDAO() {
        if (daos.get(Paper.class) == null) daos.put(Paper.class, new JDBCPaperDAO(this));
        return (PaperDAO) daos.get(Paper.class);        
    }

    @Override
    public ZoneDAO getZoneDAO() {
        if (daos.get(Zone.class) == null) daos.put(Zone.class, new JDBCZoneDAO(this));
        return (ZoneDAO) daos.get(Zone.class);
    }

    @Override
    public TemplateDAO getTemplateDAO() {
        if (daos.get(Template.class) == null) daos.put(Template.class, new JDBCTemplateDAO(this));
        return (TemplateDAO) daos.get(Template.class);
    }

    @Override
    public PermissionDAO getPermissionDAO(){
        if (daos.get(Permission.class) == null) daos.put(Permission.class, new JDBCPermissionDAO(this));
        return (PermissionDAO) daos.get(Permission.class);
    }

    @Override
    public UserPermissionDAO getUserPermissionDAO(){
        if (daos.get(UserPermission.class) == null) daos.put(UserPermission.class, new JDBCUserPermissionDAO(this));
        return (UserPermissionDAO) daos.get(UserPermission.class);
    }

}