package jdbc;

import interfaces.DataAccessContext;
import interfaces.AddressDAO;
import interfaces.DataAccessProvider;
import interfaces.RoleDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Address;
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
public class JDBCAddressDAOTest {

    private static DataAccessContext dac;
    private static AddressDAO dao;
    private static Random rand;
    //private Address obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getAddressDAO();
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
    public void testCreateAddress() throws Exception {
        Address obj1 = dao.createAddress(GenerateMocks.randomPlace(), GenerateMocks.generateFirstName(12) + "straat", GenerateMocks.generateInt(), "");
        Address obj2 = dao.getAddress(obj1.getId());
        assertEquals(obj1.getBus(), obj2.getBus());
        assertEquals(obj1.getId(), obj2.getId());
        assertEquals(obj1.getNumber(), obj2.getNumber());
        assertEquals(obj1.getStreet(), obj2.getStreet());
        assertEquals(obj1.getPlace().getId(), obj2.getPlace().getId());
        dac.rollback();

    }

    @Test
    public void testRollbackCreateAddress() throws Exception {
        Address obj1 = dao.createAddress(GenerateMocks.randomPlace(), GenerateMocks.generateFirstName(12) + "straat", GenerateMocks.generateInt(), "");
        dac.rollback();
        Address obj2 = dao.getAddress(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteAddress() throws Exception {
        Address obj1 = dao.createAddress(GenerateMocks.randomPlace(), GenerateMocks.generateFirstName(12) + "straat", GenerateMocks.generateInt(), "");
        dao.deleteAddress(obj1);
        Address obj2 = dao.getAddress(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteAddress() throws Exception {
        Address obj1 = dao.createAddress(GenerateMocks.randomPlace(), GenerateMocks.generateFirstName(12) + "straat", GenerateMocks.generateInt(), "");
        dac.commit();
        dao.deleteAddress(obj1);
        dac.rollback();
        Address obj2 = dao.getAddress(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteAddress(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateAddressAddress() throws Exception {
        Address obj1 = dao.createAddress(GenerateMocks.randomPlace(), GenerateMocks.generateFirstName(12) + "straat", GenerateMocks.generateInt(), "");
        dao.updateAddress(obj1);
        Address obj2 = dao.getAddress(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateAddresssetNumber() throws Exception {
        Address obj1 = dao.createAddress(GenerateMocks.randomPlace(), GenerateMocks.generateFirstName(12) + "straat", GenerateMocks.generateInt(), "");
        obj1.setNumber(GenerateMocks.generateInt());
        dao.updateAddress(obj1);
        Address obj2 = dao.getAddress(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateAddresssetBus() throws Exception {
        Address obj1 = dao.createAddress(GenerateMocks.randomPlace(), GenerateMocks.generateFirstName(12) + "straat", GenerateMocks.generateInt(), "");
        obj1.setBus(GenerateMocks.generateString(3));
        dao.updateAddress(obj1);
        Address obj2 = dao.getAddress(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateAddresssetStreet() throws Exception {
        Address obj1 = dao.createAddress(GenerateMocks.randomPlace(), GenerateMocks.generateFirstName(12) + "straat", GenerateMocks.generateInt(), "");
        obj1.setStreet(GenerateMocks.generateFirstName(12) + "straat");
        dao.updateAddress(obj1);
        Address obj2 = dao.getAddress(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateAddresssetPlace() throws Exception {
        Address obj1 = dao.createAddress(GenerateMocks.randomPlace(), GenerateMocks.generateFirstName(12) + "straat", GenerateMocks.generateInt(), "");
        obj1.setPlace(GenerateMocks.randomPlace());
        dao.updateAddress(obj1);
        Address obj2 = dao.getAddress(obj1.getId());
        assertNotSame(obj1, obj2);

    }
}
