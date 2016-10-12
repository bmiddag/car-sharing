import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Gilles on 24/04/14.
 */
public class MyInfoTest {

    //TODO: test changing of phone number and address

    private WebDriver driver;

    public MyInfoTest(WebDriver driver){
        this.driver = driver;
    }

    private void goToMyInfo(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("myinfo"))).click();
    }

    public void testChangePassword(){
        giveWrongOldPassword();
        changePasswordCorrectly();
    }

    public void testChangeEmail(){
        giveWrongOldEmail();
        changeEmailCorrectly();
    }

    public void testChangeAddress(){
        changeUnexistingAddress();
        changeExistingAddress();
    }

    private void changeEmail(String old, String new1, String new2){
        goToMyInfo();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("changeEmail"))).click();
        wait = new WebDriverWait(driver, 5);
        WebElement oldPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("oldEmail")));
        oldPassword.sendKeys(old);
        wait = new WebDriverWait(driver, 5);
        WebElement newPassword1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newEmail1")));
        newPassword1.sendKeys(new1);
        wait = new WebDriverWait(driver, 5);
        WebElement newPassword2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newEmail2")));
        newPassword2.sendKeys(new2);
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("changeEmailAccept"))).click();
    }

    private void changePassword(String old, String new1, String new2){
        goToMyInfo();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("changePassword"))).click();
        wait = new WebDriverWait(driver, 5);
        WebElement oldPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("oldPassword")));
        oldPassword.sendKeys(old);
        wait = new WebDriverWait(driver, 5);
        WebElement newPassword1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newPassword1")));
        newPassword1.sendKeys(new1);
        wait = new WebDriverWait(driver, 5);
        WebElement newPassword2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newPassword2")));
        newPassword2.sendKeys(new2);
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("changePasswordAccept"))).click();
    }

    private void giveWrongOldPassword(){
        changePassword("test", "test", "test");
        WebElement errorMsg = driver.findElement(By.className("alerticon"));
        Assert.assertNotNull(errorMsg);
    }

    private void changePasswordCorrectly(){
        //Change the password and check that no error message has been given.
        changePassword("admin", "test", "test");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("logout"))).click();

        //Now logout and check if the login works with the new password
        ApplicationTest.login("admin@admin.admin", "test");
        WebElement out = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("logout")));
        Assert.assertNotNull(out);

        //Change the password back to normal
        changePassword("test", "admin", "admin");
    }

    private void giveWrongOldEmail(){
        changeEmail("test@test.test", "test@test.test", "test@test.test");
        WebElement errorMsg = driver.findElement(By.className("alerticon"));
        Assert.assertNotNull(errorMsg);
    }

    private void changeEmailCorrectly(){
        //Change the email and check that no error message has been given.
        changeEmail("admin@admin.admin", "test@test.test", "test@test.test");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("logout"))).click();

        //Now logout and check if the login works with the new email
        ApplicationTest.login("test@test.test", "admin");
        WebElement out = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("logout")));
        Assert.assertNotNull(out);

        //Change the email back to normal
        changeEmail("test@test.test", "admin@admin.admin", "admin@admin.admin");
    }

    public void changeUnexistingAddress(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("changeAddress"))).click();
        WebElement newStreet = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newStreet")));
        newStreet.sendKeys("Teststraat");
        WebElement newNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newNumber")));
        newNumber.sendKeys("666");
        WebElement newBus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newBus")));
        newBus.sendKeys("A");

        //We now give in an unexisting City and Zip Code, which should lead to an error (Test / 1337 doesn't exist in Belgium)
        WebElement newCity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newCity")));
        newCity.sendKeys("Test");
        WebElement newPCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newPCode")));
        newPCode.sendKeys("1337");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("changeAddressAccept"))).click();

        //There should be an error message on the webpage now
        WebElement errorMsg = driver.findElement(By.className("alerticon"));
        Assert.assertNotNull(errorMsg);
    }

    public void changeExistingAddress(){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("changeAddress"))).click();
        WebElement newStreet = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newStreet")));
        newStreet.sendKeys("Molleree");
        WebElement newNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newNumber")));
        newNumber.sendKeys("43");
        WebElement newBus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newBus")));
        newBus.sendKeys("");

        //We now give in an unexisting City and Zip Code, which should lead to an error (Test / 1337 doesn't exist in Belgium)
        WebElement newCity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newCity")));
        newCity.sendKeys("Hertsberge");
        WebElement newPCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("newPCode")));
        newPCode.sendKeys("8020");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("changeAddressAccept"))).click();

    }
}
