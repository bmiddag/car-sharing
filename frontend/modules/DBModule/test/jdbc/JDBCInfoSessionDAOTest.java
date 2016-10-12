package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.InfoSessionDAO;
import interfaces.UserDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.InfoSession;
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
public class JDBCInfoSessionDAOTest {

    private static DataAccessContext dac;
    private static InfoSessionDAO dao;
    private static Random rand;
    //private InfoSession obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getInfoSessionDAO();
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
    public void testCreateInfoSession() throws Exception {
        InfoSession obj1 = dao.createInfoSession(GenerateMocks.RandomAddress(), GenerateMocks.generateDate(), true, 4);
        InfoSession obj2 = dao.getInfoSession(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateInfoSession() throws Exception {
        InfoSession obj1 = dao.createInfoSession(GenerateMocks.RandomAddress(), GenerateMocks.generateDate(), true, 5);
        dac.rollback();
        InfoSession obj2 = dao.getInfoSession(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteInfoSession() throws Exception {
        InfoSession obj1 = dao.createInfoSession(GenerateMocks.RandomAddress(), GenerateMocks.generateDate(), false, 6);
        dao.deleteInfoSession(obj1);
        InfoSession obj2 = dao.getInfoSession(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteInfoSession() throws Exception {
        InfoSession obj1 = dao.createInfoSession(GenerateMocks.RandomAddress(), GenerateMocks.generateDate(), false, 7);
        dac.commit();
        dao.deleteInfoSession(obj1);
        dac.rollback();
        InfoSession obj2 = dao.getInfoSession(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteInfoSession(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateInfoSessionsetAddress() throws Exception {
        InfoSession obj1 = dao.createInfoSession(GenerateMocks.RandomAddress(), GenerateMocks.generateDate(), true, 8);
        obj1.setAddress(GenerateMocks.RandomAddress());
        dao.updateInfoSession(obj1);
        InfoSession obj2 = dao.getInfoSession(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateInfoSessionsetDate() throws Exception {
        InfoSession obj1 = dao.createInfoSession(GenerateMocks.RandomAddress(), GenerateMocks.generateDate(), false, 9);
        obj1.setDate(GenerateMocks.generateDate());
        dao.updateInfoSession(obj1);
        InfoSession obj2 = dao.getInfoSession(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateInfoSessionsetOwners() throws Exception {
        InfoSession obj1 = dao.createInfoSession(GenerateMocks.RandomAddress(), GenerateMocks.generateDate(), true, 3);
        obj1.setOwners(!obj1.getOwners());
        dao.updateInfoSession(obj1);
        InfoSession obj2 = dao.getInfoSession(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateInfoSessionsetPlaces() throws Exception {
        InfoSession obj1 = dao.createInfoSession(GenerateMocks.RandomAddress(), GenerateMocks.generateDate(), false, 2);
        obj1.setPlaces(GenerateMocks.generateInt());
        dao.updateInfoSession(obj1);
        InfoSession obj2 = dao.getInfoSession(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
