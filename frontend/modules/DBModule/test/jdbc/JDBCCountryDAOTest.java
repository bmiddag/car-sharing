package jdbc;

import interfaces.DataAccessContext;
import interfaces.CountryDAO;
import interfaces.DataAccessProvider;
import interfaces.FileDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Country;
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
public class JDBCCountryDAOTest {

    private static DataAccessContext dac;
    private static CountryDAO dao;
    private static Random rand;
    //private Country obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getCountryDAO();
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
    public void testCreateCountry() throws Exception {
        Country obj1 = dao.createCountry(GenerateMocks.generateString(2), GenerateMocks.generateString(20));
        Country obj2 = dao.getCountry(obj1.getCode());
        assertEquals(obj1.getCode(), obj2.getCode());
        assertEquals(obj1.getName(), obj2.getName());
        dac.rollback();

    }

    @Test
    public void testRollbackCreateCountry() throws Exception {
        Country obj1 = dao.createCountry(GenerateMocks.generateString(2), GenerateMocks.generateString(20));
        dac.rollback();
        Country obj2 = dao.getCountry(obj1.getCode());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteCountry() throws Exception {
        Country obj1 = dao.createCountry(GenerateMocks.generateString(2), GenerateMocks.generateString(20));
        dao.deleteCountry(obj1);
        Country obj2 = dao.getCountry(obj1.getCode());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteCountry() throws Exception {
        Country obj1 = dao.createCountry(GenerateMocks.generateString(2), GenerateMocks.generateString(20));
        dac.commit();
        dao.deleteCountry(obj1);
        dac.rollback();
        Country obj2 = dao.getCountry(obj1.getCode());
        assertEquals(obj1.getCode(), obj2.getCode());
        assertEquals(obj1.getName(), obj2.getName());
        dao.deleteCountry(obj2);
        dac.commit();

    }
}
