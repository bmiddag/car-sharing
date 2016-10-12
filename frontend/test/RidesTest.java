import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Contains all tests for the view of the ride details.
 * Created by Wouter Pinnoo on 17/03/14.
 */
public class RidesTest {

    private WebDriver driver;

    public RidesTest(WebDriver driver){
        this.driver = driver;
    }

    /**
     * Open the tab 'Lenen', then the tab 'Ritgegevens'
     */
    private void goToRideDetails(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("reserve")));
        tab.click();

        wait = new WebDriverWait(driver, 5);
        tab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("rideDetails")));
        tab.click();
    }

    private List<WebElement> getRideList(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='ridesTable']/tbody")));
        return table.findElements(By.name("ridesRowElement"));
    }

    /**
     * In the list of rides, select the first one.
     * @return True if the list contains at least 1 ride, false otherwise.
     */
    private boolean selectFirstRide(){
        List<WebElement> rows = getRideList();
        if(rows != null && rows.size() > 0){
            rows.get(0).click();
            return true;
        }
        else { return false; }
    }

    /**
     * Tests adding a new ride
     */
    public void testNewRide(){
        goToRideDetails();

        List<WebElement> rows = getRideList();
        int numRowsBefore = rows == null ? 0 : rows.size();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addRideButton"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='addRideButton']/../ul/li[1]/a"))).click();

        wait = new WebDriverWait(driver, 10);
        WebElement startDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ride-start")));
        startDate.click();
        startDate.clear();
        startDate.sendKeys("20/03/2014 12:31");

        WebElement endDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ride-end")));
        endDate.click();
        endDate.clear();
        endDate.sendKeys("23/03/2014 12:31");

        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search")));
        searchField.click();
        searchField.clear();
        searchField.sendKeys("a");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='carSearchTableBody']/tr"))).click();

        driver.findElement(By.id("rideModalConfirmButton")).click();

        goToRideDetails();
        rows = getRideList();
        int numRowsAfter = rows == null ? 0 : rows.size();

        Assert.assertEquals("Expecting there was one row added (RidesTest.testNewRide)", numRowsBefore + 1, numRowsAfter);
    }

    /**
     * Tests an update in the 'km-start'-inputfield.
     */
    public void testUpdateKM(){
        goToRideDetails();
        if(selectFirstRide()){
            WebDriverWait wait = new WebDriverWait(driver, 5);
            WebElement startKM = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("km-start")));
            startKM.click();

            wait = new WebDriverWait(driver, 5);
            WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='editable-input']/input")));
            String input = "123";
            inputField.clear();
            inputField.sendKeys(input);
            driver.findElement(By.xpath("//div[@class='editable-buttons']/button[@type='submit']")).click();

            wait = new WebDriverWait(driver, 10);
            startKM = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='km-start'][@class='editable editable-click']")));
            Assert.assertTrue("Expecting the 'startKM' field to be updated (RidesTest.testUpdateKM)", startKM.getText().equals(input));
        }
    }

    /**
     * Test an update in the list of refuelings
     */
    public void testRefuelings(){
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Add a new refueling
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        goToRideDetails();
        if(!selectFirstRide()){
            return;
        }

        int initialRows = driver.findElements(By.xpath("//tbody[@id='refuelingsTableBody']/tr")).size();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tfoot[@id='addNewRefueling']/tr/th/input[@name='type']")));
        String inputType = "Benzine";
        inputField.clear();
        inputField.sendKeys(inputType);

        inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tfoot[@id='addNewRefueling']/tr/th/input[@name='litre']")));
        String inputLitre = "1.23";
        inputField.clear();
        inputField.sendKeys(inputLitre);

        inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tfoot[@id='addNewRefueling']/tr/th/input[@name='price']")));
        String inputPrice = "4.56";
        inputField.clear();
        inputField.sendKeys(inputPrice);

        WebElement addButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tfoot[@id='addNewRefueling']/tr/th/button[@name='submit']")));
        addButton.click();

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Check if the new refueling was added, and if so, make some updates
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        goToRideDetails();
        if(!selectFirstRide()){
            return;
        }

        wait = new WebDriverWait(driver, 5);
        List<WebElement> rows = driver.findElements(By.xpath("//tbody[@id='refuelingsTableBody']/tr"));
        int rowsAfterAdding = rows.size();
        Assert.assertEquals("Expecting there was one row added (RidesTest.testRefuelings)", initialRows + 1, rowsAfterAdding);

        // Update type
        WebElement fieldType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='refuelingsTableBody']/tr[last()]/td/a[@data-name='type']")));
        Assert.assertTrue("Expecting the 'type' field to be correct (RidesTest.testRefuelings)", fieldType.getText().equals(inputType));

        fieldType.click();
        WebElement inputFieldType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='editable-input']/input")));
        String changedInputType = "Diesel";
        inputFieldType.clear();
        inputFieldType.sendKeys(changedInputType);
        driver.findElement(By.xpath("//div[@class='editable-buttons']/button[@type='submit']")).click();

        wait = new WebDriverWait(driver, 10);
        fieldType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='refuelingsTableBody']/tr[last()]/td/a[@data-name='type'][@class='editable editable-click']")));
        Assert.assertTrue("Expecting the 'type' field to be updated (RidesTest.testRefuelings)", fieldType.getText().equals(changedInputType));

        // Update litre
        wait = new WebDriverWait(driver, 5);
        WebElement fieldLitre = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='refuelingsTableBody']/tr[last()]/td/a[@data-name='litre']")));
        Assert.assertTrue("Expecting the 'litre' field to be correct (RidesTest.testRefuelings)", fieldLitre.getText().equals(inputLitre));

        fieldLitre.click();
        WebElement inputFieldLitre = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='editable-input']/input")));
        String changedInputLitre = "1.24";
        inputFieldLitre.clear();
        inputFieldLitre.sendKeys(changedInputLitre);
        driver.findElement(By.xpath("//div[@class='editable-buttons']/button[@type='submit']")).click();

        wait = new WebDriverWait(driver, 10);
        fieldLitre = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='refuelingsTableBody']/tr[last()]/td/a[@data-name='litre'][@class='editable editable-click']")));
        Assert.assertTrue("Expecting the 'litre' field to be updated (RidesTest.testRefuelings)", fieldLitre.getText().equals(changedInputLitre));

        // Update price
        wait = new WebDriverWait(driver, 5);
        WebElement fieldPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='refuelingsTableBody']/tr[last()]/td/a[@data-name='price']")));
        Assert.assertTrue("Expecting the 'price' field to be correct (RidesTest.testRefuelings)", fieldPrice.getText().equals(inputPrice));

        fieldPrice.click();
        WebElement inputFieldPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='editable-input']/input")));
        String changedInputPrice = "4.57";
        inputFieldPrice.clear();
        inputFieldPrice.sendKeys(changedInputPrice);
        driver.findElement(By.xpath("//div[@class='editable-buttons']/button[@type='submit']")).click();

        wait = new WebDriverWait(driver, 10);
        fieldPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='refuelingsTableBody']/tr[last()]/td/a[@data-name='price'][@class='editable editable-click']")));
        Assert.assertTrue("Expecting the 'price' field to be updated (RidesTest.testRefuelings)", fieldPrice.getText().equals(changedInputPrice));

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Delete a refueling
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        goToRideDetails();
        if(!selectFirstRide()){
            return;
        }
        driver.findElement(By.xpath("//tbody[@id='refuelingsTableBody']/tr[last()]/td/form/button[@name='submit']")).click();
        goToRideDetails();
        if(!selectFirstRide()){
            return;
        }
        int rowsAfterDeleting = driver.findElements(By.xpath("//tbody[@id='refuelingsTableBody']/tr")).size();
        Assert.assertEquals("Expecting there was one row added (RidesTest.testRefuelings)", initialRows, rowsAfterDeleting);
    }

    /**
     * Test an update in the list of damages
     */
    public void testDamages(){
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Add a new damage
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        goToRideDetails();
        if(!selectFirstRide()){
            return;
        }

        List<WebElement> rows = driver.findElements(By.xpath("//tbody[@id='damagesTableBody']/tr"));
        int initialRows = rows == null ? 0 : rows.size();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tfoot[@id='addNewDamage']/tr/th/input[@name='date']")));
        String inputDate = "22/03/2014 12:31";
        inputField.clear();
        inputField.sendKeys(inputDate);

        inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tfoot[@id='addNewDamage']/tr/th/input[@name='description']")));
        String inputDescription = "description123";
        inputField.clear();
        inputField.sendKeys(inputDescription);

        WebElement addButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tfoot[@id='addNewDamage']/tr/th/button[@name='submit']")));
        addButton.click();


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Check if the new damage was added, and if so, make some updates
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        goToRideDetails();
        if(!selectFirstRide()){
            return;
        }

        wait = new WebDriverWait(driver, 5);
        rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//tbody[@id='damagesTableBody']/tr")));
        int rowsAfterAdding = rows == null ? 0 : rows.size();
        Assert.assertEquals("Expecting there was one row added (RidesTest.testDamages)", initialRows + 1,rowsAfterAdding);

        // Update date
        WebElement fieldDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='damagesTableBody']/tr[last()]/td/a[@data-name='time']")));
        Assert.assertTrue("Expecting the 'date' field to be correct (RidesTest.testDamages)", fieldDate.getText().equals(inputDate));

        fieldDate.click();
        WebElement inputFieldDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='editable-input']/span/select[@class='day form-control']")));
        Select dropdown = new Select(inputFieldDate);

        dropdown.selectByVisibleText("21");
        String changedInputDate = "21/03/2014 12:31";
        driver.findElement(By.xpath("//div[@class='editable-buttons']/button[@type='submit']")).click();

        wait = new WebDriverWait(driver, 10);
        fieldDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='damagesTableBody']/tr[last()]/td/a[@data-name='time'][@class='editable editable-click']")));
        Assert.assertTrue("Expecting the 'date' field to be updated (RidesTest.testDamages)", fieldDate.getText().equals(changedInputDate));

        // Update description
        wait = new WebDriverWait(driver, 5);
        WebElement fieldDescription = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='damagesTableBody']/tr[last()]/td/a[@data-name='description']")));
        Assert.assertTrue("Expecting the 'descriptino' field to be correct (RidesTest.testDamages)", fieldDescription.getText().equals(inputDescription));

        fieldDescription.click();
        WebElement inputFieldDescription = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='editable-input']/textarea")));
        String changedInputDescription = "description1234";
        inputFieldDescription.clear();
        inputFieldDescription.sendKeys(changedInputDescription);
        driver.findElement(By.xpath("//div[@class='editable-buttons']/button[@type='submit']")).click();

        wait = new WebDriverWait(driver, 10);
        fieldDescription = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@id='damagesTableBody']/tr[last()]/td/a[@data-name='description'][@class='editable editable-pre-wrapped editable-click']")));
        Assert.assertTrue("Expecting the 'description' field to be updated (RidesTest.testDamages)", fieldDescription.getText().equals(changedInputDescription));

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Delete a damage
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        goToRideDetails();
        if(!selectFirstRide()){
            return;
        }
        driver.findElement(By.xpath("//tbody[@id='damagesTableBody']/tr[last()]/td/form/button[@name='submit']")).click();
        goToRideDetails();
        if(!selectFirstRide()){
            return;
        }

        rows = driver.findElements(By.xpath("//tbody[@id='damagesTableBody']/tr"));
        int rowsAfterDeleting = rows == null ? 0 : rows.size();
        Assert.assertEquals("Expecting there was one row deleted (RidesTest.testDamages)", initialRows, rowsAfterAdding - 1);
        Assert.assertEquals("Expecting the number of rows is correct (RidesTest.testDamages)", initialRows, rowsAfterDeleting);

    }
}
