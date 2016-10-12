import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import play.Logger;
import utils.TimeUtils;

import java.io.File;
import java.io.IOException;


/**
* A class that contains tests for all functionalities of our application.
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {

    private static WebDriver driver;
    private static final String SCREENSHOT_PATH = "/screenshots/";
    static {
        driver = new FirefoxDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.get("http://localhost:9000");
    }

    private static void takeScreenshot(String message){
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            String fileName = TimeUtils.getFormattedDate("yyyy.MM.dd-HH.mm.ss'-" + message + ".png'", System.currentTimeMillis());
            FileUtils.copyFile(srcFile, new File(SCREENSHOT_PATH + fileName));
        } catch(IOException e){
            Logger.error(e.getMessage());
        }
    }

    public static void login(String uname, String pw){
        driver.get("http://localhost:9000/login");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/form/div/p/input[@name='email']")));
        email.click();
        email.sendKeys(uname);
        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/form/div/p/input[@name='password']")));
        password.click();
        password.sendKeys(pw);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/form/div/p/button[@type='submit']"))).click();
    }

    //@Test
    public void test01Register(){
        try {
            RegistrationTest regTest = new RegistrationTest(driver);
            regTest.makeRegistration();
            //driver.findElement(By.name("logout")).click();
            regTest.makeRegistrationAgain();
        } catch(Exception e){
            takeScreenshot("Register");
            throw e;
        }
    }

    /**
     * Login
     */
    @Test
    public void test02Login(){
        try {
            //Do a wrong login
            login("admin@admin.admin", "wrong");
            WebDriverWait wait = new WebDriverWait(driver, 5);
            WebElement in = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
            Assert.assertNotNull(in);

            //Do a correct login
            login("admin@admin.admin", "admin");
            wait = new WebDriverWait(driver, 5);
            WebElement out = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("logout")));
            Assert.assertNotNull(out);
        } catch(Exception e){
            takeScreenshot("Login");
            throw e;
        }
    }

    /**
     * Fill the database with some reservations on two different accounts
     */
    @Test
    public void test03fillDB(){
        try{
            //First approve the almighty leswouwou car
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("admin"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("cars"))).click();
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name("carRow"))).get(0).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acceptCar"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveChanges"))).click();

            ReservationTest resTest = new ReservationTest(driver);
            //Make reservations on admin@admin.admin
            resTest.makeReservation(4, 1, "9:00", 4, 2, "10:00");
            resTest.makeReservation(4, 2, "12:00", 4, 2, "14:00");
            //Make reservations on user@user.user
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("logout"))).click();
            ApplicationTest.login("lener@lener.lener", "lener");
            resTest.makeReservation(4, 3, "8:00", 4, 3, "8:30");
            //Log back in on admin@admin.admin
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("logout"))).click();
            ApplicationTest.login("admin@admin.admin", "admin");
        } catch(Exception e){
            takeScreenshot("fillDB");
            throw e;
        }
    }

    /**
     * This method contains all the tests that have something to do with the calendar for reservations
     */
    @Test
    public void test04ReservationsCalender(){
        try {
            ReservationTest resTest = new ReservationTest(driver);
            resTest.testReservationCalendar();
        } catch(Exception e){
            takeScreenshot("ReservationsCalender");
            throw e;
        }
    }

    /**
     * This method contains all the tests that have something to do with the management of reservations
     */
    @Test
    public void test05ReservationsManagement(){
        try {
            ReservationTest resTest = new ReservationTest(driver);
            resTest.testReservationsManagement();
        } catch(Exception e){
            takeScreenshot("ReservationsManagement");
            throw e;
        }
    }

    /**
     * This method contains the tests for email management
     */
    @Test
    public void test06Emails(){
        try {
            EmailTest emailTest = new EmailTest(driver);
            emailTest.testEmail();
        } catch(Exception e){
            takeScreenshot("Emails");
            throw e;
        }
    }

    /**
     * This method contains the tests for the view of ride details
     */
    @Test
    public void test07Rides(){
        try {
            RidesTest ridesTest = new RidesTest(driver);
            ridesTest.testNewRide();
            ridesTest.testUpdateKM();
            ridesTest.testRefuelings();
            ridesTest.testDamages();
        } catch(Exception e){
            takeScreenshot("Rides");
            throw e;
        }
    }

    /**
     * This method contains the tests for the car management view
     */
    @Test
    public void test08CarManagement(){
        try {
            CarManagementTest carManagementTest = new CarManagementTest(driver);
            carManagementTest.testRideManagement();
        } catch(Exception e){
            takeScreenshot("CarManagement");
            throw e;
        }
    }

    /**
     * Contains the tests for price ranges
     */
    @Test
    public void test09PriceRanges(){
        try{
            PriceRangesTest priceRangesTest = new PriceRangesTest(driver);
            priceRangesTest.deleteAllPriceRanges();
            priceRangesTest.createPriceRanges();
            priceRangesTest.editPriceRange();
        } catch(Exception e){
            takeScreenshot("PriceRanges");
            throw e;
        }
    }

    /**
     * Contains the tests for infosessions
     */
    @Test
    public void test10InfoSessions(){
        try{
            InfoSessionsTest infoSessionsTest = new InfoSessionsTest(driver);
            infoSessionsTest.testInfoSessions();
        } catch(Exception e){
            takeScreenshot("InfoSessions");
            throw e;
        }
    }

    /**
     * Contains the tests for the page my info
     */
    @Test
    public void test11MyInfo(){
        try{
            MyInfoTest myInfoTest = new MyInfoTest(driver);
            myInfoTest.testChangePassword();
            myInfoTest.testChangeEmail();
            myInfoTest.testChangeAddress();
        } catch(Exception e){
            takeScreenshot("MyInfo");
            throw e;
        }
    }

    /**
     * Contains the tests for resetting passwords
     */
    @Test
    public void test12ResetPassword(){
        try{
            ResetPasswordTest myprt = new ResetPasswordTest(driver);
            myprt.resetpassword();
            myprt.email();
        } catch(Exception e){
            takeScreenshot("resetPassword");
            throw e;
        }
    }

    /**
     * Contains the tests for approving and rejecting costs.
     */
    @Test
    public void test13AdminTabApproveCosts(){
        try{
            AdminTabApproveCostsTest myatact = new AdminTabApproveCostsTest(driver);
            myatact.changeChoice();
            myatact.reject();
            myatact.accept();
            myatact.rejectAndAccept();
        } catch(Exception e){
            takeScreenshot("adminTabApproveCosts");
            throw e;
        }
    }
}
