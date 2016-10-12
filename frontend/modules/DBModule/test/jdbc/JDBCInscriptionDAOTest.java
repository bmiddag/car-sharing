package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.InscriptionDAO;
import interfaces.UserDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Inscription;
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
public class JDBCInscriptionDAOTest {

    private static DataAccessContext dac;
    private static InscriptionDAO dao;
    private static Random rand;
    //private Inscription obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getInscriptionDAO();
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
    public void testCreateInscription() throws Exception {
        Inscription obj1 = dao.createInscription(GenerateMocks.RandomInfosession(), GenerateMocks.RandomUser());
        Inscription obj2 = dao.getInscription(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateInscription() throws Exception {
        Inscription obj1 = dao.createInscription(GenerateMocks.RandomInfosession(), GenerateMocks.RandomUser());
        dac.rollback();
        Inscription obj2 = dao.getInscription(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteInscription() throws Exception {
        Inscription obj1 = dao.createInscription(GenerateMocks.RandomInfosession(), GenerateMocks.RandomUser());
        dao.deleteInscription(obj1);
        Inscription obj2 = dao.getInscription(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteInscription() throws Exception {
        Inscription obj1 = dao.createInscription(GenerateMocks.RandomInfosession(), GenerateMocks.RandomUser());
        dac.commit();
        dao.deleteInscription(obj1);
        dac.rollback();
        Inscription obj2 = dao.getInscription(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteInscription(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateInscriptionsetPresent() throws Exception {
        Inscription obj1 = dao.createInscription(GenerateMocks.RandomInfosession(), GenerateMocks.RandomUser());
        obj1.setPresent(!obj1.getPresent());
        dao.updateInscription(obj1);
        Inscription obj2 = dao.getInscription(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
