/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jdbc;

import jdbc.util.GenerateMocks;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author laurens
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({jdbc.JDBCZoneDAOTest.class, jdbc.JDBCInsuranceDAOTest.class, 
    jdbc.JDBCPlaceDAOTest.class, jdbc.JDBCCarDAOTest.class, jdbc.JDBCInscriptionDAOTest.class,
    jdbc.JDBCCountryDAOTest.class, jdbc.JDBCTemplateDAOTest.class, jdbc.JDBCRideDAOTest.class, 
    jdbc.JDBCAddressDAOTest.class, jdbc.JDBCRefuelingDAOTest.class, jdbc.JDBCActionDAOTest.class,
    jdbc.JDBCDamageDAOTest.class, jdbc.JDBCDataAccessProviderTest.class, jdbc.JDBCRoleDAOTest.class, 
    jdbc.JDBCLicenseDAOTest.class, jdbc.JDBCNotificationDAOTest.class, jdbc.JDBCPaperDAOTest.class,
    jdbc.JDBCFileDAOTest.class, jdbc.JDBCIdCardDAOTest.class, jdbc.JDBCReservationDAOTest.class, 
    jdbc.JDBCPrivilegeDAOTest.class, jdbc.JDBCUserDAOTest.class, jdbc.JDBCCostDAOTest.class, 
    jdbc.JDBCDataAccessContextTest.class, jdbc.JDBCCommentDAOTest.class, jdbc.JDBCPriceRangeDAOTest.class, 
    jdbc.JDBCInfoSessionDAOTest.class})
public class DatabaseTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
