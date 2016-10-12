package jdbc;

import exceptions.DataAccessException;
import interfaces.DataAccessContext;
import interfaces.DataAccessProvider;
import interfaces.PrivilegeDAO;
import interfaces.UserDAO;
import java.util.List;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Privilege;
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
public class JDBCPrivilegeDAOTest {

    private static DataAccessContext dac;
    private static PrivilegeDAO dao;
    private static Random rand;
    //private Privilege obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getPrivilegeDAO();
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
    public void testCreatePrivilege() throws Exception {
        Privilege obj1 = dao.createPrivilege(GenerateMocks.RandomUser(), GenerateMocks.RandomCar());
        assertNotNull(obj1);
        dac.rollback();

    }

    @Test
    public void testRollbackCreatePrivilege() throws Exception {
        Privilege obj1 = dao.createPrivilege(GenerateMocks.RandomUser(), GenerateMocks.RandomCar());
        dac.rollback();
        try {
            List<Privilege> p = dao.getPrivileges(obj1.getUser());
            if (p.contains(obj1)) {
                fail("Creation should've been rolled back");
            }

        } catch (DataAccessException e) {
        }

    }

    @Test
    public void testDeletePrivilege() throws Exception {
        Privilege obj1 = dao.createPrivilege(GenerateMocks.RandomUser(), GenerateMocks.RandomCar());
        dao.deletePrivilege(obj1);
        try {
            List<Privilege> p = dao.getPrivileges(obj1.getUser());
            if (p.contains(obj1)) {
                fail("Privilege should've been removed!");
            }

        } catch (DataAccessException e) {
        }

    }

    @Test
    public void testRollbackDeletePrivilege() throws Exception {
        Privilege obj1 = dao.createPrivilege(GenerateMocks.RandomUser(), GenerateMocks.RandomCar());
        dac.commit();
        dao.deletePrivilege(obj1);
        dac.rollback();
        List<Privilege> p = dao.getPrivileges(obj1.getUser());
        if (!p.contains(obj1)) {
            fail("Privilege should've been in the DB");
        }
        dao.deletePrivilege(obj1);
        dac.commit();

    }
}
