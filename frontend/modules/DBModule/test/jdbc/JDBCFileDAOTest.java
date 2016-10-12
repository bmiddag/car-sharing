package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.FileDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.File;
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
public class JDBCFileDAOTest {

    private static DataAccessContext dac;
    private static FileDAO dao;
    private static Random rand;
    //private File obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getFileDAO();
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
    public void testCreateFile() throws Exception {
        File obj1 = dao.createFile(GenerateMocks.generateBlob().getBinaryStream());
        File obj2 = dao.getFile(obj1.getId());
        assertEquals(obj1.getTime(), obj1.getTime());
        assertEquals(obj1.getId(), obj1.getId());
        assertEquals(obj1.getFileAsStream(), obj1.getFileAsStream());
        dac.rollback();
    }

    @Test
    public void testRollbackCreateFile() throws Exception {
        File obj1 = dao.createFile(GenerateMocks.generateBlob().getBinaryStream());
        dac.rollback();
        File obj2 = dao.getFile(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }
    }

    @Test
    public void testDeleteFile() throws Exception {
        File obj1 = dao.createFile(GenerateMocks.generateBlob().getBinaryStream());
        dao.deleteFile(obj1);
        File obj2 = dao.getFile(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }
    }

}
