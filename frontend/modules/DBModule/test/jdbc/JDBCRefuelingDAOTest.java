package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.RefuelingDAO;
import interfaces.UserDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Refueling;
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
public class JDBCRefuelingDAOTest {

    private static DataAccessContext dac;
    private static RefuelingDAO dao;
    private static Random rand;
    //private Refueling obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getRefuelingDAO();
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
    public void testCreateRefueling() throws Exception {
        Refueling obj1 = dao.createRefueling(GenerateMocks.RandomRide(), rand.nextInt(Integer.MAX_VALUE));
        Refueling obj2 = dao.getRefueling(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateRefueling() throws Exception {
        Refueling obj1 = dao.createRefueling(GenerateMocks.RandomRide(), rand.nextInt(Integer.MAX_VALUE));
        dac.rollback();
        Refueling obj2 = dao.getRefueling(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteRefueling() throws Exception {
        Refueling obj1 = dao.createRefueling(GenerateMocks.RandomRide(), rand.nextInt(Integer.MAX_VALUE));
        dao.deleteRefueling(obj1);
        Refueling obj2 = dao.getRefueling(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }
    }

    @Test
    public void testRollbackDeleteRefueling() throws Exception {
        Refueling obj1 = dao.createRefueling(GenerateMocks.RandomRide(), rand.nextInt(Integer.MAX_VALUE));
        dac.commit();
        dao.deleteRefueling(obj1);
        dac.rollback();
        Refueling obj2 = dao.getRefueling(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteRefueling(obj1);
        dac.commit();
    }

    @Test
    public void testGetRefuelingByUser() throws Exception {
        dao.getRefuelingsByUser(1, System.currentTimeMillis(), System.currentTimeMillis());

    }

    @Test
    public void testUpdateRefuelingsetLitre() throws Exception {
        Refueling obj1 = dao.createRefueling(GenerateMocks.RandomRide(), rand.nextInt(Integer.MAX_VALUE));
        obj1.setLitre(rand.nextDouble());
        dao.updateRefueling(obj1);
        Refueling obj2 = dao.getRefueling(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateRefuelingsetPrice() throws Exception {
        Refueling obj1 = dao.createRefueling(GenerateMocks.RandomRide(), rand.nextInt(Integer.MAX_VALUE));
        obj1.setPrice(rand.nextInt(Integer.MAX_VALUE));
        dao.updateRefueling(obj1);
        Refueling obj2 = dao.getRefueling(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateRefuelingsetProof() throws Exception {
        Refueling obj1 = dao.createRefueling(GenerateMocks.RandomRide(), rand.nextInt(Integer.MAX_VALUE));
        obj1.setProof(rand.nextInt(GenerateMocks.NUMBER_OF_FILES - 150) +1);
        dao.updateRefueling(obj1);
        Refueling obj2 = dao.getRefueling(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateRefuelingsetType() throws Exception {
        Refueling obj1 = dao.createRefueling(GenerateMocks.RandomRide(), rand.nextInt(Integer.MAX_VALUE));
        obj1.setType(GenerateMocks.generateRandomWord());
        dao.updateRefueling(obj1);
        Refueling obj2 = dao.getRefueling(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateRefuelingsetApproved() throws Exception {
        Refueling obj1 = dao.createRefueling(GenerateMocks.RandomRide(), rand.nextInt(Integer.MAX_VALUE));
        obj1.setApproved(GenerateMocks.generateBoolean());
        dao.updateRefueling(obj1);
        Refueling obj2 = dao.getRefueling(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateRefuelingsetMileage() throws Exception {
        Refueling obj1 = dao.createRefueling(GenerateMocks.RandomRide(), rand.nextInt(Integer.MAX_VALUE));
        obj1.setMileage(rand.nextDouble());
        dao.updateRefueling(obj1);
        Refueling obj2 = dao.getRefueling(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
