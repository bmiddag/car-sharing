package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.LicenseDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.License;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author laurens
 */
public class JDBCLicenseDAOTest {

    private static DataAccessContext dac;
    private static LicenseDAO dao;
    private static Random rand;
    //private License obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getLicenseDAO();
        assertNotNull(dao);
        rand = new Random();
    }

    @Before
    public void start() throws Exception {
        dac.begin();
    }

    @After
    public void stop() {
    }

    @AfterClass
    public static void close() {
        dac.close();
    }

    @Test
    public void testCreateLicense() throws Exception {
        License obj1 = dao.createLicense(GenerateMocks.generatePlate(), GenerateMocks.RandomFile());
        License obj2 = dao.getLicense(obj1.getId());
        assertEquals(obj1.getFile(), obj2.getFile());
        assertEquals(obj1.getId(), obj2.getId());
        assertEquals(obj1.getNumber(), obj2.getNumber());
        dac.rollback();

    }

    @Test
    public void testRollbackCreateLicense() throws Exception {
        License obj1 = dao.createLicense(GenerateMocks.generatePlate(), GenerateMocks.RandomFile());
        dac.rollback();
        License obj2 = dao.getLicense(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteLicense() throws Exception {
        License obj1 = dao.createLicense(GenerateMocks.generatePlate(), GenerateMocks.RandomFile());
        dao.deleteLicense(obj1);
        License obj2 = dao.getLicense(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteLicense() throws Exception {
        License obj1 = dao.createLicense(GenerateMocks.generatePlate(), GenerateMocks.RandomFile());
        dac.commit();
        dao.deleteLicense(obj1);
        dac.rollback();
        License obj2 = dao.getLicense(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteLicense(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateLicensesetNumber() throws Exception {
        License obj1 = dao.createLicense(GenerateMocks.generatePlate(), GenerateMocks.RandomFile());
        obj1.setNumber(GenerateMocks.generatePlate());
        dao.updateLicense(obj1);
        License obj2 = dao.getLicense(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateLicensesetFile() throws Exception {
        License obj1 = dao.createLicense(GenerateMocks.generatePlate(), GenerateMocks.RandomFile());
        obj1.setFile(rand.nextInt(GenerateMocks.NUMBER_OF_FILES -150 ) + 1);
        dao.updateLicense(obj1);
        License obj2 = dao.getLicense(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
