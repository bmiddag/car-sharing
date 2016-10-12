package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.InsuranceDAO;
import interfaces.UserDAO;
import java.util.Date;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Insurance;
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
public class JDBCInsuranceDAOTest {

    private static DataAccessContext dac;
    private static InsuranceDAO dao;
    private static Random rand;
    //private Insurance obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getInsuranceDAO();
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
    public void testCreateInsurance() throws Exception {
        Insurance obj1 = dao.createInsurance(GenerateMocks.RandomCar());
        Insurance obj2 = dao.getInsurance(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateInsurance() throws Exception {
        Insurance obj1 = dao.createInsurance(GenerateMocks.RandomCar());
        dac.rollback();
        Insurance obj2 = dao.getInsurance(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteInsurance() throws Exception {
        Insurance obj1 = dao.createInsurance(GenerateMocks.RandomCar());
        dao.deleteInsurance(obj1);
        Insurance obj2 = dao.getInsurance(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteInsurance() throws Exception {
        Insurance obj1 = dao.createInsurance(GenerateMocks.RandomCar());
        dac.commit();
        dao.deleteInsurance(obj1);
        dac.rollback();
        Insurance obj2 = dao.getInsurance(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteInsurance(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateInsurancesetNumber() throws Exception {
        Insurance obj1 = dao.createInsurance(GenerateMocks.RandomCar());
        obj1.setNumber(GenerateMocks.generateRandomWord());
        dao.updateInsurance(obj1);
        Insurance obj2 = dao.getInsurance(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateInsurancesetCompany() throws Exception {
        Insurance obj1 = dao.createInsurance(GenerateMocks.RandomCar());
        obj1.setCompany(GenerateMocks.generateRandomWord());
        dao.updateInsurance(obj1);
        Insurance obj2 = dao.getInsurance(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateInsurancesetBonusmalus() throws Exception {
        Insurance obj1 = dao.createInsurance(GenerateMocks.RandomCar());
        obj1.setBonusmalus(GenerateMocks.generateInt());
        dao.updateInsurance(obj1);
        Insurance obj2 = dao.getInsurance(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateInsurancesetOmnium() throws Exception {
        Insurance obj1 = dao.createInsurance(GenerateMocks.RandomCar());
        obj1.setOmnium(GenerateMocks.generateBoolean());
        dao.updateInsurance(obj1);
        Insurance obj2 = dao.getInsurance(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateInsurancesetExpiration() throws Exception {
        Insurance obj1 = dao.createInsurance(GenerateMocks.RandomCar());
        obj1.setExpiration(new java.sql.Date(new Date().getTime()));
        dao.updateInsurance(obj1);
        Insurance obj2 = dao.getInsurance(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
