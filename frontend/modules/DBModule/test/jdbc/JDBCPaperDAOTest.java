package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.PaperDAO;
import interfaces.UserDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Paper;
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
public class JDBCPaperDAOTest {

    private static DataAccessContext dac;
    private static PaperDAO dao;
    private static Random rand;
    //private Paper obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getPaperDAO();
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
    public void testCreatePaper() throws Exception {
        Paper obj1 = dao.createPaper(GenerateMocks.RandomInsurance(), rand.nextInt(GenerateMocks.NUMBER_OF_FILES - 150)+1);
        Paper obj2 = dao.getPaper(obj1.getFile());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreatePaper() throws Exception {
        Paper obj1 = dao.createPaper(GenerateMocks.RandomInsurance(), rand.nextInt(GenerateMocks.NUMBER_OF_FILES - 150)+1);
        dac.rollback();
        Paper obj2 = dao.getPaper(obj1.getFile());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeletePaper() throws Exception {
        Paper obj1 = dao.createPaper(GenerateMocks.RandomInsurance(), rand.nextInt(GenerateMocks.NUMBER_OF_FILES - 150)+1);
        dao.deletePaper(obj1);
        Paper obj2 = dao.getPaper(obj1.getFile());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeletePaper() throws Exception {
        Paper obj1 = dao.createPaper(GenerateMocks.RandomInsurance(),rand.nextInt(GenerateMocks.NUMBER_OF_FILES - 150)+1);
        dac.commit();
        dao.deletePaper(obj1);
        dac.rollback();
        Paper obj2 = dao.getPaper(obj1.getFile());
        assertEquals(obj1, obj2);
        dao.deletePaper(obj1);
        dac.commit();
    }

}
