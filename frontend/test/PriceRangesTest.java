import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import play.Logger;

import java.util.List;

/**
 * This class tests everything that has something to do with the settings of the price ranges, used for facturisation.
 * Created by Gilles on 29/03/14.
 */
public class PriceRangesTest {

    private WebDriver driver;

    public PriceRangesTest(WebDriver driver){
        this.driver = driver;
    }

    private void goToRideFacturisation(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("admin"))).click();
        wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("accounts"))).click();
    }

    /**
     * Delete all price ranges, afterwards no price ranges should remain and there should be a warning
     * in the upper right corner, saying something is wrong with the price ranges.
     */
    public void deleteAllPriceRanges(){
        goToRideFacturisation();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deletePriceRange"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deletePriceRange"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deletePriceRange"))).click();
        List<WebElement> rows = driver.findElements(By.name("priceRangeRow"));
        Assert.assertTrue(rows.size() == 0);

        //There should also be a warning sign on the page, saying the price ranges are incorrect.
        WebElement warningSign = driver.findElement(By.name("priceRangeError"));
        Assert.assertNotNull(warningSign);
        WebElement deleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deletePriceRange")));
    }

    /**
     * After creating the new price ranges, we edit the price ranges so that there is no error left.
     */
    public void editPriceRange(){
        goToRideFacturisation();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        List<WebElement> editRangeStart = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("priceRangeMinimum")));
        editRangeStart.get(editRangeStart.size() - 1).click();
        wait = new WebDriverWait(driver, 5);
        WebElement edit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='priceRangeTableBody']/tr/td/div/div/div/form/div/div/div/input[@class='form-control input-sm']")));
        edit.clear();
        edit.sendKeys("501");
        wait = new WebDriverWait(driver, 5);
        WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='priceRangeTableBody']/tr/td/div/div/div/form/div/div/div/button[@class='btn btn-primary btn-sm editable-submit']")));
        submit.click();
        goToRideFacturisation();
        //There should also be a OK sign on the page, saying the price ranges are correct.
        WebElement OKSign = driver.findElement(By.name("priceRangeOK"));
        Assert.assertNotNull(OKSign);
    }

    /**
     * Create the following price ranges: 0-250, 251-500, 450-65535.
     * Since price range 2 and 3 overlap, an error should be generated.
     */
    public void createPriceRanges(){
        goToRideFacturisation();
        String[] beginValues = { "0", "251", "450" };
        String[] endValues = { "250", "500", "65535" };
        String[] prices = { "0.30", "0.25", "0.20" };
        WebDriverWait wait;
        for(int i = 0; i < 3; i++){
            wait = new WebDriverWait(driver, 5);
            WebElement rangeStart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pricerange-begin")));
            rangeStart.sendKeys(beginValues[i]);
            wait = new WebDriverWait(driver, 5);
            WebElement rangeEnd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pricerange-end")));
            rangeEnd.sendKeys(endValues[i]);
            wait = new WebDriverWait(driver, 5);
            WebElement rangePrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pricerange-price")));
            rangePrice.sendKeys(prices[i]);
            wait = new WebDriverWait(driver, 5);
            WebElement rangeSubmit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pricerange-submit")));
            rangeSubmit.click();
        }

        //There should also be a warning sign on the page, saying the price ranges are incorrect.
        WebElement warningSign = driver.findElement(By.name("priceRangeError"));
        Assert.assertNotNull(warningSign);
    }
}
