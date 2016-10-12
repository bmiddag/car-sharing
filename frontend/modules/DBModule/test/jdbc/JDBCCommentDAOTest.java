package jdbc;

import interfaces.DataAccessContext;
import interfaces.CommentDAO;
import interfaces.DataAccessProvider;
import interfaces.UserDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Comment;
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
public class JDBCCommentDAOTest {

    private static DataAccessContext dac;
    private static CommentDAO dao;
    private static Random rand;
    //private Comment obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getCommentDAO();
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
    public void testCreateComment() throws Exception {
        Comment obj1 = dao.createComment(GenerateMocks.RandomUser(), GenerateMocks.RandomDamage(), GenerateMocks.generateTextMaybeEmpty(0.0, 250));
        Comment obj2 = dao.getComment(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateComment() throws Exception {
        Comment obj1 = dao.createComment(GenerateMocks.RandomUser(), GenerateMocks.RandomDamage(), GenerateMocks.generateTextMaybeEmpty(0.0, 250));
        dac.rollback();
        Comment obj2 = dao.getComment(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteComment() throws Exception {
        Comment obj1 = dao.createComment(GenerateMocks.RandomUser(), GenerateMocks.RandomDamage(), GenerateMocks.generateTextMaybeEmpty(0.0, 250));
        dao.deleteComment(obj1);
        Comment obj2 = dao.getComment(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteComment() throws Exception {
        Comment obj1 = dao.createComment(GenerateMocks.RandomUser(), GenerateMocks.RandomDamage(), GenerateMocks.generateTextMaybeEmpty(0.0, 250));
        dac.commit();
        dao.deleteComment(obj1);
        dac.rollback();
        Comment obj2 = dao.getComment(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteComment(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateCommentsetContent() throws Exception {
        Comment obj1 = dao.createComment(GenerateMocks.RandomUser(), GenerateMocks.RandomDamage(), GenerateMocks.generateTextMaybeEmpty(0.0, 250));
        obj1.setContent(GenerateMocks.generateTextMaybeEmpty(0.0, 250));
        dao.updateComment(obj1);
        Comment obj2 = dao.getComment(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
