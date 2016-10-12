package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.ReservationDAO;
import java.util.Date;
import java.util.List;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Reservation;
import objects.User;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author laurens
 */
public class JDBCReservationDAOTest {

    private static DataAccessContext dac;
    private static ReservationDAO dao;
    private static Random rand;
    //private Reservation obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getReservationDAO();
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
    public void testGetReservationsWithoutRide() throws Exception {
        User user = GenerateMocks.RandomUser();
        Reservation obj = dao.createReservation(user, GenerateMocks.RandomCar(), GenerateMocks.generateDate(), GenerateMocks.generateDate());
        List<Reservation> l = dao.getReservationsWithoutRide(user.getId());
        assertTrue(!l.isEmpty());
        dac.rollback();

    }

    @Test
    public void testCreateReservation() throws Exception {
        Reservation obj1 = dao.createReservation(GenerateMocks.RandomUser(), GenerateMocks.RandomCar(), GenerateMocks.generateDate(), GenerateMocks.generateDate());
        Reservation obj2 = dao.getReservation(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateReservation() throws Exception {
        Reservation obj1 = dao.createReservation(GenerateMocks.RandomUser(), GenerateMocks.RandomCar(), GenerateMocks.generateDate(), GenerateMocks.generateDate());
        dac.rollback();
        Reservation obj2 = dao.getReservation(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteReservation() throws Exception {
        Reservation obj1 = dao.createReservation(GenerateMocks.RandomUser(), GenerateMocks.RandomCar(), GenerateMocks.generateDate(), GenerateMocks.generateDate());
        dao.deleteReservation(obj1);
        Reservation obj2 = dao.getReservation(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteReservation() throws Exception {
        Reservation obj1 = dao.createReservation(GenerateMocks.RandomUser(), GenerateMocks.RandomCar(), GenerateMocks.generateDate(), GenerateMocks.generateDate());
        dac.commit();
        dao.deleteReservation(obj1);
        dac.rollback();
        Reservation obj2 = dao.getReservation(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteReservation(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateReservationReservation() throws Exception {
        Reservation obj1 = dao.createReservation(GenerateMocks.RandomUser(), GenerateMocks.RandomCar(), GenerateMocks.generateDate(), GenerateMocks.generateDate());
        dao.updateReservation(obj1);
        Reservation obj2 = dao.getReservation(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateReservationsetBegin() throws Exception {
        Reservation obj1 = dao.createReservation(GenerateMocks.RandomUser(), GenerateMocks.RandomCar(), GenerateMocks.generateDate(), GenerateMocks.generateDate());
        obj1.setBegin(GenerateMocks.generateDate());
        dao.updateReservation(obj1);
        Reservation obj2 = dao.getReservation(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateReservationsetEnd() throws Exception {
        Reservation obj1 = dao.createReservation(GenerateMocks.RandomUser(), GenerateMocks.RandomCar(), GenerateMocks.generateDate(), GenerateMocks.generateDate());
        obj1.setEnd(GenerateMocks.generateDate());
        dao.updateReservation(obj1);
        Reservation obj2 = dao.getReservation(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateReservationsetApproved() throws Exception {
        Reservation obj1 = dao.createReservation(GenerateMocks.RandomUser(), GenerateMocks.RandomCar(), GenerateMocks.generateDate(), GenerateMocks.generateDate());
        obj1.setApproved(GenerateMocks.generateBoolean());
        dao.updateReservation(obj1);
        Reservation obj2 = dao.getReservation(obj1.getId());
        assertNotSame(obj1, obj2);

    }
}
