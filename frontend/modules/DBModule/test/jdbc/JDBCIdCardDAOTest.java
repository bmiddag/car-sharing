package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.FileDAO;
import interfaces.IdCardDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.IdCard;
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
public class JDBCIdCardDAOTest {

    private static DataAccessContext dac;
    private static IdCardDAO dao;
    private static Random rand;
    //private IdCard obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getIdCardDAO();
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
    public void testCreateIdCard() throws Exception {
           IdCard obj1 = dao.createIdCard(GenerateMocks.generateRandomWord(),GenerateMocks.generateRandomWord(),GenerateMocks.RandomFile());
            IdCard obj2 =dao.getIdCard(obj1.getId());
            assertEquals(obj1, obj2);
            dac.rollback();
        
    }

    @Test
    public void testRollbackCreateIdCard() throws Exception {
           IdCard obj1 = dao.createIdCard(GenerateMocks.generateRandomWord(),GenerateMocks.generateRandomWord(),GenerateMocks.RandomFile());
            dac.rollback();
            IdCard obj2 =dao.getIdCard(obj1.getId());
            if (obj2 != null) fail("Should have returned null after rollback!");
        
    }


    @Test
    public void testDeleteIdCard() throws Exception {
           IdCard obj1 = dao.createIdCard(GenerateMocks.generateRandomWord(),GenerateMocks.generateRandomWord(),GenerateMocks.RandomFile());
            dao.deleteIdCard(obj1);
            IdCard obj2 =dao.getIdCard(obj1.getId());
            if (obj2 != null) fail("Should have returned null after delete!");
        
    }

    @Test
        public void testUpdateIdCardsetNumber() throws Exception {

          IdCard obj1 = dao.createIdCard(GenerateMocks.generateRandomWord(),GenerateMocks.generateRandomWord(),GenerateMocks.RandomFile());
            obj1.setNumber(GenerateMocks.generateRandomWord());
            dao.updateIdCard(obj1);
            IdCard obj2 =dao.getIdCard(obj1.getId());
            assertNotSame(obj1, obj2);
        
    }


    @Test
        public void testUpdateIdCardsetFile() throws Exception {
           IdCard obj1 = dao.createIdCard(GenerateMocks.generateRandomWord(),GenerateMocks.generateRandomWord(),GenerateMocks.RandomFile());
            obj1.setFile(rand.nextInt(GenerateMocks.NUMBER_OF_FILES - 150) + 1);
            dao.updateIdCard(obj1);
            IdCard obj2 =dao.getIdCard(obj1.getId());
            assertNotSame(obj1, obj2);
        
    }


    @Test
        public void testUpdateIdCardsetRegister() throws Exception {
           IdCard obj1 = dao.createIdCard(GenerateMocks.generateRandomWord(),GenerateMocks.generateRandomWord(),GenerateMocks.RandomFile());
            obj1.setRegister(GenerateMocks.generateRandomWord());
            dao.updateIdCard(obj1);
            IdCard obj2 =dao.getIdCard(obj1.getId());
            assertNotSame(obj1, obj2);
        
    }

}