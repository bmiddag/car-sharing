package jdbc;

import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.RoleDAO;
import interfaces.ZoneDAO;
import java.util.Random;
import objects.Role;
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
public class JDBCRoleDAOTest {

    private static DataAccessContext dac;
    private static RoleDAO dao;
    private static Random rand;
    //private Role obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getRoleDAO();
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
    public void testCreateRole() throws Exception {
        Role obj1 = dao.createRole("test_role");
        Role obj2 = dao.getRole(obj1.getId());
        assertEquals(obj1.getId(), obj2.getId());
        assertEquals(obj1.getName(), obj2.getName());
        dac.rollback();

    }

    @Test
    public void testRollbackCreateRole() throws Exception {
        Role obj1 = dao.createRole("test_role");
        dac.rollback();
        Role obj2 = dao.getRole(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteRole() throws Exception {
        Role obj1 = dao.createRole("test_role");
        dao.deleteRole(obj1);
        Role obj2 = dao.getRole(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteRole() throws Exception {
        Role obj1 = dao.createRole("test_role");
        dac.commit();
        dao.deleteRole(obj1);

        dac.rollback();
        Role obj2 = dao.getRole(obj1.getId());
        assertEquals(obj1.getId(), obj2.getId());
        assertEquals(obj1.getName(), obj2.getName());

        dao.deleteRole(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateRolesetName() throws Exception {
        Role obj1 = dao.createRole("test_role");
        obj1.setName("new_test_name");
        dao.updateRole(obj1);
        Role obj2 = dao.getRole(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
