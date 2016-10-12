package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.TemplateDAO;
import interfaces.UserDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Template;
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
public class JDBCTemplateDAOTest {

    private static DataAccessContext dac;
    private static TemplateDAO dao;
    private static Random rand;
    //private Template obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getTemplateDAO();
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
    public void testCreateTemplate() throws Exception {
        Template obj1 = dao.createTemplate(GenerateMocks.generateString(10));
        Template obj2 = dao.getTemplate(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateTemplate() throws Exception {
        Template obj1 = dao.createTemplate(GenerateMocks.generateString(10));
        dac.rollback();
        Template obj2 = dao.getTemplate(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteTemplate() throws Exception {
        Template obj1 = dao.createTemplate(GenerateMocks.generateString(10));
        dao.deleteTemplate(obj1);
        Template obj2 = dao.getTemplate(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteTemplate() throws Exception {
        Template obj1 = dao.createTemplate(GenerateMocks.generateString(10));
        dac.commit();
        dao.deleteTemplate(obj1);
        dac.rollback();
        Template obj2 = dao.getTemplate(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteTemplate(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateTemplatesetContent() throws Exception {
        Template obj1 = dao.createTemplate(GenerateMocks.generateString(10));
        obj1.setContent(GenerateMocks.generateTextMaybeEmpty(0.10, 500));
        dao.updateTemplate(obj1);
        Template obj2 = dao.getTemplate(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateTemplatesetName() throws Exception {
        Template obj1 = dao.createTemplate(GenerateMocks.generateString(10));
        obj1.setName("new_tetest_name");
        dao.updateTemplate(obj1);
        Template obj2 = dao.getTemplate(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
