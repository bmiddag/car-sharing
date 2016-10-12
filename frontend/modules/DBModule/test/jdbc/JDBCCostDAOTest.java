package jdbc;

import interfaces.DataAccessContext;
import interfaces.CostDAO;
import interfaces.DataAccessProvider;
import java.util.List;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Cost;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author laurens
 */
public class JDBCCostDAOTest {

    DataAccessContext dac;
    CostDAO dao;
    Random rand;

    @Before
    public void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getCostDAO();
        assertNotNull(dao);
        rand = new Random();
    }

    @After
    public void close() {
        dac.close();
    }

    @Test
    public void testCreateCost() throws Exception {
        dac.begin();
        int car = rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1;
        Cost obj1 = dao.createCost(car, rand.nextInt(Integer.MAX_VALUE));
        Cost obj2 = dao.getCost(car, obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();
    }

    @Test
    public void testRollbackCreateCost() throws Exception {
        dac.begin();
        int car = rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1;
        Cost obj1 = dao.createCost(car, rand.nextInt(Integer.MAX_VALUE));
        dac.rollback();
        Cost obj2 = dao.getCost(car, obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }
    }

    @Test
    public void testDeleteCost() throws Exception {
        dac.begin();
        int car = rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1;
        Cost obj1 = dao.createCost(car, rand.nextInt(Integer.MAX_VALUE));
        dao.deleteCost(obj1);
        Cost obj2 = dao.getCost(car, obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }
    }

    @Test
    public void testRollbackDeleteCost() throws Exception {
        dac.begin();
        int car = rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1;
        Cost obj1 = dao.createCost(car, rand.nextInt(Integer.MAX_VALUE));
        dac.commit();
        dao.deleteCost(obj1);
        dac.rollback();
        Cost obj2 = dao.getCost(car, obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteCost(obj1);
        dac.commit();
    }

    @Test
    public void testUpdateCostSetPrice() throws Exception {
        int car = rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1;
        Cost obj1 = dao.createCost(car, rand.nextInt(Integer.MAX_VALUE));
        obj1.setPrice(rand.nextInt(Integer.MAX_VALUE));
        dao.updateCost(obj1);
        Cost obj2 = dao.getCost(car, obj1.getId());
        assertNotSame(obj1, obj2);
    }

    @Test
    public void testUpdateCostSetType() throws Exception {
        int car = rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1;
        Cost obj1 = dao.createCost(car, rand.nextInt(Integer.MAX_VALUE));
        obj1.setType(GenerateMocks.generateRandomWord());
        dao.updateCost(obj1);
        Cost obj2 = dao.getCost(car, obj1.getId());
        assertNotSame(obj1, obj2);
    }

    @Test
    public void testUpdateCostSetProof() throws Exception {
        int car = rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1;
        Cost obj1 = dao.createCost(car, rand.nextInt(Integer.MAX_VALUE));
        obj1.setProof(rand.nextInt(GenerateMocks.NUMBER_OF_FILES));
        dao.updateCost(obj1);
        Cost obj2 = dao.getCost(car, obj1.getId());
        assertNotSame(obj1, obj2);
    }

    @Test
    public void testUpdateCostSetDescription() throws Exception {
        int car = rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1;
        Cost obj1 = dao.createCost(car, rand.nextInt(Integer.MAX_VALUE));
        obj1.setDescription(GenerateMocks.generateTextMaybeEmpty(0.05, 80));
        dao.updateCost(obj1);
        Cost obj2 = dao.getCost(car, obj1.getId());
        assertNotSame(obj1, obj2);
    }

    @Test
    public void testUpdateCostSetApproved() throws Exception {
        int car = rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1;
        Cost obj1 = dao.createCost(car, rand.nextInt(Integer.MAX_VALUE));
        obj1.setApproved(rand.nextBoolean());
        dao.updateCost(obj1);
        Cost obj2 = dao.getCost(car, obj1.getId());
        assertNotSame(obj1, obj2);
    }
    
    @Test
    public void testGetPendingCosts() throws Exception {
        dac.begin();
        int car = rand.nextInt(GenerateMocks.NUMBER_OF_CARS - 150) + 1;
        Cost obj1 = dao.createCost(car, rand.nextInt(Integer.MAX_VALUE));
        obj1.setApproved(null);
        dao.updateCost(obj1);
        List<Cost> costs = dao.getPendingCosts();
        //assertTrue(costs.contains(obj1));
        dac.rollback();
    }

}
