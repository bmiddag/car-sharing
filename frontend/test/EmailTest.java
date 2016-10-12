import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import play.Logger;

import java.util.List;

/**
 * Contains all the tests that have something to do with emails. It tests e.g. the creation and deletion of
 * templates, etc.
 * Created by Gilles on 9/03/14.
 */
public class EmailTest {

    private WebDriver driver;

    public EmailTest(WebDriver driver){
        this.driver = driver;
    }

    /**
     * First go to the admin page, then click on tabMailPrefs
     */
    private void goToEmailTemplates(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("admin")));
        tab.click();

        wait = new WebDriverWait(driver, 5);
        tab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("tabMailPrefs")));
        tab.click();
    }

    public void testEmail(){
        testEmailCreation();
        testEmailDeletion();
    }

    /**
     * Create a template with the name Test, afterwards check if the list with templates contain one more entry.
     */
    public void testEmailCreation(){
        goToEmailTemplates();
        List<WebElement> templateRows = driver.findElements(By.id("templateRow"));
        //Get the number of templates before creating a new one
        int size1 = templateRows.size();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("createNewTemplate"))).click();
        wait = new WebDriverWait(driver, 5);
        WebElement nameTemplate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("templateName")));
        String s = "Test";
        nameTemplate.sendKeys(s);
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("createAccept"))).click();
        wait = new WebDriverWait(driver, 5);
        templateRows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("templateRow")));
        int size2 = templateRows.size();
        //Check if a new element was added, and that it is the right one
        Assert.assertTrue((size2 - size1) == 1);
        Assert.assertTrue(templateRows.get(size1).getText().equals("Test"));
    }

    /**
     * Delete the last template of the list, afterwards check if the list contains one entry less.
     */
    public void testEmailDeletion(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        goToEmailTemplates();
        List<WebElement> templateRows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("templateRow")));
        //Get the number of templates before deleting one
        int size1 = templateRows.size();
        templateRows.get(size1 - 1).click();
        driver.findElement(By.name("deleteTemplate")).click();
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("deleteAccept"))).click();
        wait = new WebDriverWait(driver, 5);
        templateRows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("templateRow")));
        int size2 = templateRows.size();
        //Check that size has been decreased by 1
        Assert.assertTrue((size1 - size2) == 1);
    }

}
