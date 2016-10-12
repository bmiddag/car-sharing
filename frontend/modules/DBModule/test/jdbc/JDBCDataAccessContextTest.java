package jdbc;

import interfaces.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author laurens
 */
public class JDBCDataAccessContextTest {
    
  
   
    @Test
    public void testCreation() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        assertNotNull(instance);        
    }

    @Test
    public void testEmptyCommit() throws Exception {
        try (DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            instance.begin();
        }
    }

    @Test
    public void testRollbacks1() throws Exception {
        try (DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            instance.begin();
            instance.rollback();
            instance.rollback();
        }
    }
    
   @Test
    public void testRollbacks2() throws Exception {
        try (DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            instance.begin();
            instance.rollback();
        }
    }
    
    @Test
    public void testRollbacks3() throws Exception {
        try (DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext()) {
            instance.begin();
            instance.rollback();
        }
    }

    @Test
    public void testGetActionDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        ActionDAO result = instance.getActionDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetAddressDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        AddressDAO result = instance.getAddressDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetCarDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        CarDAO result = instance.getCarDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetCommentDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        CommentDAO result = instance.getCommentDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetCostDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        CostDAO result = instance.getCostDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetCountryDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();        
        CountryDAO result = instance.getCountryDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetDamageDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        DamageDAO result = instance.getDamageDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetFileDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        FileDAO result = instance.getFileDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetIdCardDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        IdCardDAO result = instance.getIdCardDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetInfoSessionDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        InfoSessionDAO result = instance.getInfoSessionDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetInscriptionDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        InscriptionDAO result = instance.getInscriptionDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetInsuranceDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        InsuranceDAO result = instance.getInsuranceDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetLicenseDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        LicenseDAO result = instance.getLicenseDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetNotificationDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        NotificationDAO result = instance.getNotificationDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetPaperDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        PaperDAO result = instance.getPaperDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetPlaceDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        PlaceDAO result = instance.getPlaceDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetPriceRangeDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        PriceRangeDAO result = instance.getPriceRangeDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetPrivilegeDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        PrivilegeDAO result = instance.getPrivilegeDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetRefuelingDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        RefuelingDAO result = instance.getRefuelingDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetReservationDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        ReservationDAO result = instance.getReservationDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetRideDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        RideDAO result = instance.getRideDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetRoleDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        RoleDAO result = instance.getRoleDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetTemplateDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        TemplateDAO result = instance.getTemplateDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetUserDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        UserDAO result = instance.getUserDAO();
        assertNotNull(result);
    }
    @Test
    public void testGetZoneDAO() throws Exception {
        DataAccessContext instance = JDBCDataAccessProvider.getDataAccessProvider().getDataAccessContext();
        instance.begin();
        ZoneDAO result = instance.getZoneDAO();
        assertNotNull(result);             
    }

}
