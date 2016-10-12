import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.validation.constraints.AssertTrue;
import java.util.List;

/**
 * This class tests everything InfoSessions
 * Created by Bart on 21/04/'14.
 */
public class InfoSessionsTest {

    private WebDriver driver;

    public InfoSessionsTest(WebDriver driver){
        this.driver = driver;
    }

    public void testInfoSessions() {
        goToInfoSessions();
        addWrongInfoSession();
        addInfoSession();
        testInscription();
        editAndDeleteInfoSession();
    }

    private void goToInfoSessions(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("infosessions"))).click();
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addInfoSessionButton"))); // It's loaded!
    }

    private void goToMain(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("navbar-brand"))).click();
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mainInfoSessionsPanel"))); // It's loaded!
    }

    /**
     * Add a new infosession, resulting in failure.
     */
    private void addWrongInfoSession() {
        WebElement addButton = driver.findElement(By.id("addInfoSessionButton"));
        addButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmButton"))).click();
        WebElement errors = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-danger")));
        Assert.assertNotNull(errors);
    }

    /**
     * Add a new infosession, resulting in success.
     */
    private void addInfoSession() {
        WebElement addButton = driver.findElement(By.id("addInfoSessionButton"));
        addButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal")));
        modal.findElement(By.name("dateDay")).sendKeys("19");
        modal.findElement(By.name("dateMonth")).sendKeys("07");
        modal.findElement(By.name("dateYear")).sendKeys("2064");
        modal.findElement(By.name("dateHour")).sendKeys("19");
        modal.findElement(By.name("dateMinute")).sendKeys("02");
        modal.findElement(By.name("addressStreet")).sendKeys("Lolstraat");
        modal.findElement(By.name("addressNumber")).sendKeys("707");
        modal.findElement(By.name("addressBus")).sendKeys("L");
        modal.findElement(By.name("addressZip")).sendKeys("9000");
        modal.findElement(By.name("addressPlace")).sendKeys("Gent");
        modal.findElement(By.name("places")).sendKeys("1");
        modal.findElement(By.id("confirmButton")).click();
        // refresh page after posting
        WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
        Assert.assertNotNull(success);
    }

    /**
     * Add and remove an inscription.
     */
    private void testInscription() {
        List<WebElement> rows = driver.findElements(By.name("sessionRow"));
        WebElement row = rows.get(rows.size() - 1); // Get the last row
        Assert.assertNotNull(row); // At least one infosession should exist
        row.click();
        String idString  = row.getAttribute("id");
        int id = Integer.parseInt(idString.replace("session", ""));
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal" + id)));
        // Selenium hates checkboxes that were hidden first, and thinking it's still not visible doesn't allow to click them. This is a hack to fix that.
        ((JavascriptExecutor) driver).executeScript("$('#inscriptionCheckbox" + id + "').prop('checked', true).trigger(\"change\");");
        modal.findElement(By.id("confirmButton" + id)).click();
        WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
        Assert.assertNotNull(success);

        goToMain();
        row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("session" + id)));
        Assert.assertNotNull(row); // At least one infosession should exist
        row.click();
        modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal" + id)));
        // Selenium hates checkboxes that were hidden first, and thinking it's still not visible doesn't allow to click them. This is a hack to fix that.
        ((JavascriptExecutor) driver).executeScript("$('#inscriptionCheckbox" + id + "').prop('checked', true).trigger(\"change\");");
        modal.findElement(By.id("confirmButton" + id)).click();
        success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
        Assert.assertNotNull(success);
    }

    /**
     * Edit an infosession and delete it afterwards.
     */
    private void editAndDeleteInfoSession() {
        List<WebElement> rows = driver.findElements(By.name("sessionRow"));
        int amountOfRows = rows.size();
        WebElement row = rows.get(rows.size()-1); // Get the last row
        Assert.assertNotNull(row); // At least one infosession should exist
        row.click();
        String idString  = row.getAttribute("id");
        int id = Integer.parseInt(idString.replace("session", ""));
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal" + id)));
        WebElement street = modal.findElement(By.name("addressStreet"));
        Assert.assertEquals("Lolstraat",street.getAttribute("value"));
        street.clear();
        street.sendKeys("Roflstraat");
        modal.findElement(By.id("confirmButton" + id)).click();
        WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
        Assert.assertNotNull(success);

        // Check if the changes were saved
        row = driver.findElement(By.id("session" + id));
        Assert.assertNotNull(row); // At least one infosession should exist
        row.click();
        modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal" + id)));
        street = modal.findElement(By.name("addressStreet"));
        Assert.assertEquals("Roflstraat",street.getAttribute("value")); // Successfully edited

        // Click remove button and checkbox, then click confirm
        WebElement removeButton = modal.findElement(By.id("removeButton" + id));
        removeButton.findElement((By.tagName("button"))).click();
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("removeAlert" + id)));
        Assert.assertTrue(alert.isDisplayed());
        // Selenium hates checkboxes that were hidden first, and thinking it's still not visible doesn't allow to click them. This is a hack to fix that.
        ((JavascriptExecutor) driver).executeScript("$('#removeCheckbox" + id + "').prop('checked', true).trigger(\"change\");");
        modal.findElement(By.id("confirmButton" + id)).click();
        success = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
        Assert.assertNotNull(success);
        Assert.assertEquals(amountOfRows-1,driver.findElements(By.name("sessionRow")).size());
    }
}
