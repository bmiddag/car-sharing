import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Created by Gilles on 7/05/14.
 */
public class RegistrationTest {

    private WebDriver driver;

    public RegistrationTest(WebDriver driver){
        this.driver = driver;
    }

    /**
     * Searches for the tab "register" and clicks on it.
     */
    private void goToReservations() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("register"))).click();
    }

    public void makeRegistration(){
        goToReservations();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        List<WebElement> textFields = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='col-sm-9']/div[@class='input-group']/input")));
        //name
        textFields.get(0).sendKeys("Robert");
        //family name
        textFields.get(1).sendKeys("De Test");
        //email 1
        textFields.get(2).sendKeys("testmail@test.test");
        //email 2
        textFields.get(3).sendKeys("testmail@test.test");
        //password 1
        textFields.get(4).sendKeys("test");
        //password 2
        textFields.get(5).sendKeys("test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerUser"))).click();
    }

    public void makeRegistrationAgain(){
        makeRegistration();
        //There should be an error message on the webpage now
        WebElement errormsg = driver.findElement(By.className("alerticon"));
        Assert.assertNotNull("Error message present!", errormsg);
    }
}
