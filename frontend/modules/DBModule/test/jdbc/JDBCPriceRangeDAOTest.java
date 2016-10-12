package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.PriceRangeDAO;
import interfaces.UserDAO;
import java.math.BigDecimal;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.PriceRange;
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
public class JDBCPriceRangeDAOTest {

    private static DataAccessContext dac;
    private static PriceRangeDAO dao;
    private static Random rand;
    //private PriceRange obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getPriceRangeDAO();
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
    public void testCreatePriceRange() throws Exception {
        PriceRange obj1 = dao.createPriceRange(GenerateMocks.generateInt(), GenerateMocks.generateInt(), rand.nextInt(Integer.MAX_VALUE));
        PriceRange obj2 = dao.getPriceRange(obj1.getId());
        assertEquals(obj1.getId(), obj2.getId());
        assertEquals(obj1.getMax(), obj2.getMax());
        assertEquals(obj1.getMin(), obj2.getMin());
        assertEquals(obj1.getPrice().doubleValue(), obj2.getPrice().doubleValue(), 0.1);
        dac.rollback();

    }

    @Test
    public void testRollbackCreatePriceRange() throws Exception {
        PriceRange obj1 = dao.createPriceRange(GenerateMocks.generateInt(), GenerateMocks.generateInt(), rand.nextInt(Integer.MAX_VALUE));
        dac.rollback();
        PriceRange obj2 = dao.getPriceRange(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeletePriceRange() throws Exception {
        PriceRange obj1 = dao.createPriceRange(GenerateMocks.generateInt(), GenerateMocks.generateInt(), rand.nextInt(Integer.MAX_VALUE));
        dao.deletePriceRange(obj1);
        PriceRange obj2 = dao.getPriceRange(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeletePriceRange() throws Exception {
        PriceRange obj1 = dao.createPriceRange(GenerateMocks.generateInt(), GenerateMocks.generateInt(), rand.nextInt(Integer.MAX_VALUE));
        dac.commit();
        dao.deletePriceRange(obj1);
        dac.rollback();
        PriceRange obj2 = dao.getPriceRange(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deletePriceRange(obj1);
        dac.commit();

    }

    @Test
    public void testUpdatePriceRangesetMin() throws Exception {
        PriceRange obj1 = dao.createPriceRange(GenerateMocks.generateInt(), GenerateMocks.generateInt(), rand.nextInt(Integer.MAX_VALUE));
        obj1.setMin(GenerateMocks.generateInt());
        dao.updatePriceRange(obj1);
        PriceRange obj2 = dao.getPriceRange(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdatePriceRangesetMax() throws Exception {
        PriceRange obj1 = dao.createPriceRange(GenerateMocks.generateInt(), GenerateMocks.generateInt(), rand.nextInt(Integer.MAX_VALUE));
        obj1.setMax(GenerateMocks.generateInt());
        dao.updatePriceRange(obj1);
        PriceRange obj2 = dao.getPriceRange(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdatePriceRangesetPrice() throws Exception {
        PriceRange obj1 = dao.createPriceRange(GenerateMocks.generateInt(), GenerateMocks.generateInt(), rand.nextInt(Integer.MAX_VALUE));
        obj1.setPrice(rand.nextInt(Integer.MAX_VALUE));
        dao.updatePriceRange(obj1);
        PriceRange obj2 = dao.getPriceRange(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
