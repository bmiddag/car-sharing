package jdbc;

import interfaces.DataAccessContext;
import interfaces.DamageDAO;
import interfaces.DataAccessProvider;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Damage;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author laurens
 */
public class JDBCDamageDAOTest {

    private static DataAccessContext dac;
    private static DamageDAO dao;
    private static Random rand;
    //private Damage obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getDamageDAO();
        assertNotNull(dao);
        rand = new Random();
    }
    
    @Before
    public void start() throws Exception {
        //dac.begin();
    }

    @After
    public void stop() {
    }

    @AfterClass
    public static void close() {
        dac.close();
    }

    @Test
    public void testCreateDamage() throws Exception {
        dac.begin();
        Damage obj1 = dao.createDamage(rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, null, GenerateMocks.generateString(20), System.currentTimeMillis());
        Damage obj2 = dao.getDamage(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testRollbackCreateDamage() throws Exception {
        dac.begin();
        Damage obj1 = dao.createDamage(rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, null, GenerateMocks.generateString(20), System.currentTimeMillis());
        dac.rollback();
        Damage obj2 = dao.getDamage(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }
    }

    @Test
    public void testDeleteDamage() throws Exception {
        dac.begin();
        Damage obj1 = dao.createDamage(rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, null, GenerateMocks.generateString(20), System.currentTimeMillis());
        dao.deleteDamage(obj1);
        Damage obj2 = dao.getDamage(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }
    }

    @Test
    public void testRollbackDeleteDamage() throws Exception {
        dac.begin();
        Damage obj1 = dao.createDamage(rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, null, GenerateMocks.generateString(20), System.currentTimeMillis());
        dac.commit();
        dao.deleteDamage(obj1);
        dac.rollback();
        Damage obj2 = dao.getDamage(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteDamage(obj1);
        dac.commit();
    }

    @Test
    public void testUpdateDamagesetStatus() throws Exception {
        dac.begin();
        Damage obj1 = dao.createDamage(rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, null, GenerateMocks.generateString(20), System.currentTimeMillis());
        obj1.setStatus(GenerateMocks.generateRandomWord());
        dao.updateDamage(obj1);
        Damage obj2 = dao.getDamage(obj1.getId());
        assertNotSame(obj1, obj2);
    }

    @Test
    public void testUpdateDamagesetDescription() throws Exception {
        dac.begin();
        Damage obj1 = dao.createDamage(rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1, rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1, null, GenerateMocks.generateString(20), System.currentTimeMillis());
        obj1.setDescription(GenerateMocks.generateTextMaybeEmpty(0.2, 100));
        dao.updateDamage(obj1);
        Damage obj2 = dao.getDamage(obj1.getId());
        assertNotSame(obj1, obj2);
    }
}
