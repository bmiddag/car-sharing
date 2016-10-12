package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.PlaceDAO;
import interfaces.RoleDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Place;
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
public class JDBCPlaceDAOTest {

    private static DataAccessContext dac;
    private static PlaceDAO dao;
    private static Random rand;
    //private Place obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getPlaceDAO();
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
    public void testCreatePlace() throws Exception {
        Place obj1 = dao.createPlace(GenerateMocks.generateCityName(), GenerateMocks.generateZip(), "BE");
        Place obj2 = dao.getPlace(obj1.getId());
        assertEquals(obj1.getId(), obj2.getId());
        assertEquals(obj1.getName(), obj2.getName());
        assertEquals(obj1.getZip(), obj2.getZip());
        dac.rollback();

    }

    @Test
    public void testRollbackCreatePlace() throws Exception {
        Place obj1 = dao.createPlace(GenerateMocks.generateCityName(), GenerateMocks.generateZip(), "BE");
        dac.rollback();
        Place obj2 = dao.getPlace(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeletePlace() throws Exception {
        Place obj1 = dao.createPlace(GenerateMocks.generateCityName(), GenerateMocks.generateZip(), "BE");
        dao.deletePlace(obj1);
        Place obj2 = dao.getPlace(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testUpdatePlacesetName() throws Exception {
        Place obj1 = dao.createPlace(GenerateMocks.generateCityName(), GenerateMocks.generateZip(), "BE");
        obj1.setName("new_name");
        dao.updatePlace(obj1);
        Place obj2 = dao.getPlace(obj1.getId());
        assertNotSame(obj1.getName(), obj2.getName());

    }

    @Test
    public void testUpdatePlacesetZip() throws Exception {
        Place obj1 = dao.createPlace(GenerateMocks.generateCityName(), GenerateMocks.generateZip(), "BE");
        obj1.setZip(GenerateMocks.generateZip());
        dao.updatePlace(obj1);
        Place obj2 = dao.getPlace(obj1.getId());
        assertNotSame(obj1, obj2);

    }
}
