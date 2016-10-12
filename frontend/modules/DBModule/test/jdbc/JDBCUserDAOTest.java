package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.FileDAO;
import interfaces.UserDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.User;
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
public class JDBCUserDAOTest {

    private static DataAccessContext dac;
    private static UserDAO dao;
    private static Random rand;
    //private User obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getUserDAO();
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
    public void testCreateUser() throws Exception {
        User obj1 = dao.createUser(GenerateMocks.generateFirstName(5), GenerateMocks.generateLastName(7), GenerateMocks.RandomRole(), GenerateMocks.generateMail(), GenerateMocks.generateRandomWord());
        User obj2 = dao.getUser(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testRollbackCreateUser() throws Exception {
        User obj1 = dao.createUser(GenerateMocks.generateFirstName(5), GenerateMocks.generateLastName(7), GenerateMocks.RandomRole(), GenerateMocks.generateMail(), GenerateMocks.generateRandomWord());
        dac.rollback();
        User obj2 = dao.getUser(obj1.getId());
        assertNull("Should have returned null after rollback!", obj2);
    }

    @Test
    public void testDeleteUser() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        dao.deleteUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNull("Should have returned null after delete!", obj2);
        dac.rollback();
    }

    @Test
    public void testRollbackDeleteUser() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        dao.deleteUser(obj1);
        dac.rollback();
        User obj2 = dao.getUser(obj1.getId());
        assertEquals(obj1, obj2);
    }

    @Test
    public void testUpdateUsersetTitle() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setTitle(GenerateMocks.generateTitle());
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetRole() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setRole(GenerateMocks.RandomRole());
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetName() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setName("new_name");
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetSurname() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setSurname(GenerateMocks.generateLastName(7));
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetMail() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setMail(GenerateMocks.generateMail());
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetPass() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setPass(GenerateMocks.generateRandomWord());
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetPhone() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setPhone(GenerateMocks.generateRandomPhone());
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetGuarantee() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setGuarantee(rand.nextInt(Integer.MAX_VALUE));
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetPast() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setPast(GenerateMocks.generateTextMaybeEmpty(0.2, 250));
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetAddress() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setAddress(GenerateMocks.randomAddress());
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetZone() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setZone(GenerateMocks.RandomZone());
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetDomicile() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setAddress(GenerateMocks.randomAddress());
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetLicense() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setLicense(GenerateMocks.RandomLicense());
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testUpdateUsersetIdCard() throws Exception {
        User obj1 = dao.getUser(rand.nextInt(GenerateMocks.NUMBER_OF_USERS - 150) + 1);
        obj1.setIdCard(GenerateMocks.RandomIdCard());
        dao.updateUser(obj1);
        User obj2 = dao.getUser(obj1.getId());
        assertNotSame(obj1, obj2);
        dac.rollback();
    }

}
