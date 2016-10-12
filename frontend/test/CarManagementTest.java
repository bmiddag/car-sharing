import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
* Contains all tests for the view of the ride details.
*/
public class CarManagementTest {

    private WebDriver driver;

    public CarManagementTest(WebDriver driver){
        this.driver = driver;
    }

    private void goToRideManagement(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("carmanagement"))).click();
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("trips"))).click();
    }

    /**
     * Test for accepting and rejecting reservations.
     * First, reject all pending rides info and check if afterwards no pending ride information remains to be handled.
     * Second, accept all the just rejected ride information.
     */


    public void testRideManagement(){
        goToRideManagement();
        rejectAllPendingRides();
        //Check if there is no pending ride information remaining
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pendingRidesTable")));
        List<WebElement> checkboxes = row.findElements(By.name("selectedRides"));
        Assert.assertTrue(checkboxes.size() == 0);

        acceptAllRejectedRides();
        //Check if there are no rejected ride informations left
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showRejectedRidesTable"))).click();
        wait = new WebDriverWait(driver, 5);
        row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rejectedRidesTable")));
        checkboxes = row.findElements(By.name("selectedRides"));
        Assert.assertTrue(checkboxes.size() == 0);

    }

    private void rejectAllPendingRides(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        //The checkbox in the table header selects all reservations
        List<WebElement> checkboxes = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//form[@name='pendingRides']/div/div/table/tbody/tr/td/span/button")));
        for(WebElement checkbox: checkboxes) checkbox.click();
        wait = new WebDriverWait(driver, 5);
        List<WebElement> submit = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//form[@name='pendingRides']/div/div/input[@name='submit']")));
        submit.get(1).click();
    }

    private void acceptAllRejectedRides(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showRejectedRidesTable"))).click();
        //The checkbox in the table header selects all reservations
        List<WebElement> checkboxes = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//form[@name='rejectedRides']/div/div/table/tbody/tr/td/span/button")));
        for(WebElement checkbox: checkboxes) checkbox.click();
        wait = new WebDriverWait(driver, 5);
        List<WebElement> submit = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//form[@name='rejectedRides']/div/div/input[@name='submit']")));
        submit.get(0).click();
    }
}
