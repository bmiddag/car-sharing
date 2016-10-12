package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.RideDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Ride;
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
public class JDBCRideDAOTest {

    private static DataAccessContext dac;
    private static RideDAO dao;
    private static Random rand;
    //private Ride obj1

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(user, password, database); // comment to use default user "root" with password "root" on database "test"
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getRideDAO();
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
    public void testCreateRide() throws Exception {
        Ride obj1 = dao.createRide(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1);
        Ride obj2 = dao.getRide(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateRide() throws Exception {
        Ride obj1 = dao.createRide(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1);
        dac.rollback();
        Ride obj2 = dao.getRide(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteRide() throws Exception {
        Ride obj1 = dao.createRide(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1);
        dao.deleteRide(obj1.getId());
        Ride obj2 = dao.getRide(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteRide() throws Exception {
        Ride obj1 = dao.createRide(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1);
        dac.commit();
        dao.deleteRide(obj1.getId());
        dac.rollback();
        Ride obj2 = dao.getRide(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteRide(obj1.getId());
        dac.commit();

    }

    @Test
    public void testUpdateRidesetBegin() throws Exception {
        Ride obj1 = dao.createRide(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1);
        obj1.setBegin(System.currentTimeMillis());
        dao.updateRide(obj1);
        Ride obj2 = dao.getRide(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateRidesetEnd() throws Exception {
        Ride obj1 = dao.createRide(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1);
        obj1.setEnd(System.currentTimeMillis());
        dao.updateRide(obj1);
        Ride obj2 = dao.getRide(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateRidesetApproved() throws Exception {
        Ride obj1 = dao.createRide(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1);
        obj1.setApproved(rand.nextBoolean());
        dao.updateRide(obj1);
        Ride obj2 = dao.getRide(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateRidesetStartKM() throws Exception {
        Ride obj1 = dao.createRide(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1);
        obj1.setStartKM(rand.nextDouble());
        dao.updateRide(obj1);
        Ride obj2 = dao.getRide(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateRidesetStopKM() throws Exception {
        Ride obj1 = dao.createRide(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1);
        obj1.setStopKM(rand.nextDouble());
        dao.updateRide(obj1);
        Ride obj2 = dao.getRide(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
