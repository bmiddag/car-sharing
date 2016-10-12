package jdbc;

import interfaces.DataAccessContext;
import interfaces.CarDAO;
import interfaces.DataAccessProvider;
import interfaces.UserDAO;
import java.util.Random;
import jdbc.util.GenerateMocks;
import objects.Car;
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
public class JDBCCarDAOTest {

    private static DataAccessContext dac;
    private static CarDAO dao;
    private static Random rand;
    //private Car obj1;

    @BeforeClass
    public static void init() throws Exception {
        DataAccessProvider dap = JDBCDataAccessProvider.getDataAccessProvider();
        //dap.init(...);
        dac = dap.getDataAccessContext();
        assertNotNull(dac);
        dao = dac.getCarDAO();
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
    public void testCreateCar() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        Car obj2 = dao.getCar(obj1.getId());
        assertEquals(obj1, obj2);
        dac.rollback();

    }

    @Test
    public void testRollbackCreateCar() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        dac.rollback();
        Car obj2 = dao.getCar(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after rollback!");
        }

    }

    @Test
    public void testDeleteCar() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        dao.deleteCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        if (obj2 != null) {
            fail("Should have returned null after delete!");
        }

    }

    @Test
    public void testRollbackDeleteCar() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        dac.commit();
        dao.deleteCar(obj1);

        dac.rollback();
        Car obj2 = dao.getCar(obj1.getId());
        assertEquals(obj1, obj2);
        dao.deleteCar(obj1);
        dac.commit();

    }

    @Test
    public void testUpdateCarsetOwner() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setOwner(GenerateMocks.RandomUser());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetName() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setName(GenerateMocks.generateString(15));
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetPlate() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setPlate(GenerateMocks.generatePlate());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetAddress() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setAddress(GenerateMocks.RandomAddress());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetZone() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setZone(GenerateMocks.RandomZone());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetInscription() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setInscription(rand.nextInt(GenerateMocks.NUMBER_OF_FILES - 150) + 1);
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetMake() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setMake(GenerateMocks.generateRandomWord());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetType() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setType(GenerateMocks.generateRandomWord());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetModel() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setModel(GenerateMocks.generateRandomWord());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetYear() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setYear(GenerateMocks.generateInt());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetFuel() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setFuel(GenerateMocks.generateRandomWord());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    //@Test
    public void testUpdateCarsetDescription() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setDescription(GenerateMocks.generateTextMaybeEmpty(0.3, 150));
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetDoors() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setDoors((GenerateMocks.generateInt() % 8) + 1);
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetCapacity() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setCapacity((GenerateMocks.generateInt() % 8) + 1);
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetTow() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setTow(GenerateMocks.generateBoolean());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetGps() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setGps(GenerateMocks.generateBoolean());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetConsumption() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setConsumption(GenerateMocks.generateFloat());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetChassis() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setChassis(GenerateMocks.generateRandomWord());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetValue() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setValue(rand.nextInt(Integer.MAX_VALUE));
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetMileage() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setMileage(GenerateMocks.generateFloat());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

    @Test
    public void testUpdateCarsetKmpy() throws Exception {
        Car obj1 = dao.createCar(GenerateMocks.RandomUser(), GenerateMocks.generateString(20), GenerateMocks.generatePlate());
        obj1.setKmpy(rand.nextDouble());
        dao.updateCar(obj1);
        Car obj2 = dao.getCar(obj1.getId());
        assertNotSame(obj1, obj2);

    }

}
