package jdbc;

import interfaces.DataAccessContext;
import interfaces.ActionDAO;
import interfaces.DataAccessProvider;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Action;
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
public class JDBCActionDAOTest {

    private static DataAccessContext dac;
    private static ActionDAO dao;
    private static Random rand;
    //private Action obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getActionDAO();
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
    public void testCreateAction() throws Exception {
        Action obj1 = dao.createAction(GenerateMocks.generateString(20));
        Action obj2 = dao.getAction(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateAction() throws Exception {
        Action obj1 = dao.createAction(GenerateMocks.generateString(20));
        dac.rollback();
        Action obj2 = dao.getAction(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteAction() throws Exception {
        Action obj1 = dao.createAction(GenerateMocks.generateString(20));
        dao.deleteAction(obj1);
        Action obj2 = dao.getAction(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteAction() throws Exception {
        Action obj1 = dao.createAction(GenerateMocks.generateString(20));
        dac.commit();
        dao.deleteAction(obj1);
        dac.rollback();
        Action obj2 = dao.getAction(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteAction(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateActionsetName() throws Exception {
        Action obj1 = dao.createAction(GenerateMocks.generateString(20));
        obj1.setName(GenerateMocks.generateString(20));
        dao.updateAction(obj1);
        Action obj2 = dao.getAction(obj1.getId());
        assertEquals(obj1, obj2);

        /* Make sure the DB doesn't change */
        dao.deleteAction(obj1);

    }

    @Test
    public void testUpdateActionsetStart() throws Exception {
        Action obj1 = dao.createAction(GenerateMocks.generateString(20));
        obj1.setStart(GenerateMocks.generateDate());
        dao.updateAction(obj1);
        Action obj2 = dao.getAction(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateActionsetPeriod() throws Exception {
        Action obj1 = dao.createAction(GenerateMocks.generateString(20));
        obj1.setPeriod(GenerateMocks.generateDate());
        dao.updateAction(obj1);
        Action obj2 = dao.getAction(obj1.getId());
        assertNotSame(obj1, obj2);

    }
}
