package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.NotificationDAO;
import interfaces.UserDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Notification;
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
public class JDBCNotificationDAOTest {

    private static DataAccessContext dac;
    private static NotificationDAO dao;
    private static Random rand;
    //private Notification obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getNotificationDAO();
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
    public void testCreateNotification() throws Exception {
        Notification obj1 = dao.createNotification(GenerateMocks.RandomUser(), GenerateMocks.generateTextMaybeEmpty(0.0, 10), GenerateMocks.generateTextMaybeEmpty(0.0, 144));
        Notification obj2 = dao.getNotification(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateNotification() throws Exception {
        Notification obj1 = dao.createNotification(GenerateMocks.RandomUser(), GenerateMocks.generateTextMaybeEmpty(0.0, 10), GenerateMocks.generateTextMaybeEmpty(0.0, 144));
        dac.rollback();
        Notification obj2 = dao.getNotification(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteNotification() throws Exception {
        Notification obj1 = dao.createNotification(GenerateMocks.RandomUser(), GenerateMocks.generateTextMaybeEmpty(0.0, 10), GenerateMocks.generateTextMaybeEmpty(0.0, 144));
        dao.deleteNotification(obj1);
        Notification obj2 = dao.getNotification(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteNotification() throws Exception {
        Notification obj1 = dao.createNotification(GenerateMocks.RandomUser(), GenerateMocks.generateTextMaybeEmpty(0.0, 10), GenerateMocks.generateTextMaybeEmpty(0.0, 144));
        dac.commit();
        dao.deleteNotification(obj1);
        dac.rollback();
        Notification obj2 = dao.getNotification(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteNotification(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateNotificationsetSubject() throws Exception {
        Notification obj1 = dao.createNotification(GenerateMocks.RandomUser(), GenerateMocks.generateTextMaybeEmpty(0.0, 10), GenerateMocks.generateTextMaybeEmpty(0.0, 144));
        obj1.setSubject(GenerateMocks.generateTextMaybeEmpty(0.0, 10));
        dao.updateNotification(obj1);
        Notification obj2 = dao.getNotification(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateNotificationsetContent() throws Exception {
        Notification obj1 = dao.createNotification(GenerateMocks.RandomUser(), GenerateMocks.generateTextMaybeEmpty(0.0, 10), GenerateMocks.generateTextMaybeEmpty(0.0, 144));
        obj1.setContent(GenerateMocks.generateTextMaybeEmpty(0.0, 144));
        dao.updateNotification(obj1);
        Notification obj2 = dao.getNotification(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
