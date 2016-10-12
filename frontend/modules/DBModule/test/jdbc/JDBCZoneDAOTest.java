package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.ZoneDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Zone;
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
public class JDBCZoneDAOTest {

    private static DataAccessContext dac;
    private static ZoneDAO dao;
    private static Random rand;
    //private Zone obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getZoneDAO();
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
    public void testCreateZone() throws Exception {
        Zone obj1 = dao.createZone(GenerateMocks.generateString(15));
        Zone obj2 = dao.getZone(obj1.getId());
        assertEquals(obj1.getId(), obj2.getId());
        assertEquals(obj1.getName(), obj2.getName());
        dac.rollback();
    }

    @Test
    public void testRollbackCreateZone() throws Exception {
        Zone obj1 = dao.createZone(GenerateMocks.generateString(15));
        dac.rollback();
        Zone obj2 = dao.getZone(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }
    }

    @Test
    public void testDeleteZone() throws Exception {
        Zone obj1 = dao.createZone(GenerateMocks.generateString(15));
        dao.deleteZone(obj1);
        Zone obj2 = dao.getZone(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }
    }

    @Test
    public void testUpdateZonesetName() throws Exception {
        Zone obj1 = dao.createZone(GenerateMocks.generateString(15));
        obj1.setName("new-name");
        dao.updateZone(obj1);
        Zone obj2 = dao.getZone(obj1.getId());
        assertNotSame(obj1, obj2);
    }

}
