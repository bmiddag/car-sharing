import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class contains all the tests that have something to do with the
 * reservation part of the page. It will test e.g the drag function of the
 * calendar, the accepting and rejecting of reservations, etc.
 */
public class ReservationTest {

    private WebDriver driver;

    public ReservationTest(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * This test consists of two parts: one where the calendar is tested,
     * another where the management of reservations is tested.
     */
    public void reservationTest() {
        testReservationCalendar();
        testReservationsManagement();
    }

    /**
     * This test checks if reservations are made correctly.
     */
    public void testReservationCalendar() {
        testMakeReservation();
    }

    /**
     * This functions tests whether a reservation can be made and whether the
     * reservation shows in the car management tab. The last part is done by
     * counting the amount of checkboxes before and after the reservation.
     */
    private void testMakeReservation() {
        WebDriverWait wait5s = new WebDriverWait(driver, 5);
        WebDriverWait wait15s = new WebDriverWait(driver, 15);

        // Get the number of pending reservations before making the reservation
        goToCarManagementReservations();
        wait5s.until(ExpectedConditions.visibilityOfElementLocated(By.id("showAccResTable"))).click();
        WebElement row = wait5s.until(ExpectedConditions.visibilityOfElementLocated(
                                    By.id("acceptedReservationsTable")));
        List<WebElement> checkboxes = row.findElements(By.name("selectedReservations"));
        int sizeBefore = checkboxes.size();

        // Make a reservation
        String carName = makeReservation(3, 3, "9:00", 3, 5, "17:00");

        // Check whether the reservation got added
        goToCarManagementReservations();
        wait5s.until(ExpectedConditions.visibilityOfElementLocated(By.id("showAccResTable"))).click();
        row = wait5s.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("acceptedReservationsTable")));
        checkboxes = row.findElements(By.name("selectedReservations"));
        int sizeAfter = checkboxes.size();
        Assert.assertThat("The reservation wasn't added! (voor: " + sizeBefore + ", na: " + sizeAfter + ")",
                          sizeAfter - sizeBefore, is(1));
        goToReservations();

        // Check whether there can be another reservation for this car just
        // before and after the reservation
        searchCars(3, 3, "8:00", 3, 3, "8:59");
        Assert.assertThat("There can't be a reservation before the other!",
                          firstCarName(), is(carName));
        searchCars(3, 5, "17:01", 3, 5, "18:00");
        Assert.assertThat("There can't be a reservation after the other!",
                          firstCarName(), is(carName));

        // Check whether there can't be another reservation that completely or
        // partly overlays with the reservation
        searchCars(3, 3, "8:00", 3, 5, "18:00");
        Assert.assertThat("There can be an overlapping reservation!",
                          firstCarName(), is(not(carName)));
        searchCars(3, 3, "8:00", 3, 3, "9:01");
        Assert.assertThat("There can be an overlapping reservation!",
                          firstCarName(), is(not(carName)));
        searchCars(3, 5, "16:59", 3, 5, "18:00");
        Assert.assertThat("There can be an overlapping reservation!",
                          firstCarName(), is(not(carName)));
    }

    /**
     * Make a reservation in the next month
     * @param startWeek
     * @param startDay
     * @param startTime
     * @param endWeek
     * @param endDay
     * @param endTime
     * @return the name of the reserved car
     */
    public String makeReservation(int startWeek, int startDay, String startTime,
                                 int endWeek, int endDay, String endTime){
        WebDriverWait wait15s = new WebDriverWait(driver, 15);
        // (1) Search for cars that are available for a certain period
        searchCars(startWeek, startDay, startTime, endWeek, endDay, endTime);
        // (2) Click on the first car
        WebElement carRow = wait15s.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tbody[@id='carTableBody']/tr")));
        String carName = carRow.findElement(By.id("car_name")).getText();
        carRow.findElement(By.xpath("td/button")).click();
        // (3) Click on the confirm button
        wait15s.until(ExpectedConditions.visibilityOfElementLocated(By.id("carModalConfirmButton"))).click();
        return carName;
    }

    /**
     * This functions selects a certain period (with given time) in the
     * calendar and searches for available cars.
     */
    private void searchCars(int startWeek, int startDay, String startTime,
                            int endWeek, int endDay, String endTime) {
        WebDriverWait wait5s = new WebDriverWait(driver, 5);

        goToReservations();
        // Select a period in the calendar by dragging
        testDragFunctionCalender(startWeek, startDay, endWeek, endDay);
        // Fill in a time in the confirmation box
        WebElement startTimeField = wait5s.until(ExpectedConditions.visibilityOfElementLocated(By.id("picker-start")));
        startTimeField.clear();
        startTimeField.sendKeys(startTime);
        WebElement endTimeField = wait5s.until(ExpectedConditions.visibilityOfElementLocated(By.id("picker-end")));
        endTimeField.clear();
        endTimeField.sendKeys(endTime);
        // Search for cars (click on the search button)
        wait5s.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@id='confirmation']/div/form/div/div/button[@type='submit']")))
              .click();
    }

    /**
     * Returns the name of the first car in the list of search results.
     * Naturally, this assumes that there has been a search for cars.
     */
    private String firstCarName() {
        WebDriverWait wait15s = new WebDriverWait(driver, 15);
        WebElement carNameCell = wait15s.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//tbody[@id='carTableBody']/tr/td[@id='car_name']")));
        return carNameCell.getText();
    }

    /**
     * This function tests the drag function of the calendar. It selects two
     * dates in the next month, so there is no selection in the past. When the
     * dragging is finished, a confirmation box should be shown under the
     * calendar. This function checks whether this box appears and whether the
     * confirmation dates are correct.
     */
    private void testDragFunctionCalender(int startWeek, int startDay, int endWeek, int endDay) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        // Go to the next month so we're sure we don't make a selection in the past
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@id='calendar']/table/tbody/tr/td/span[2]")))
            .click();
        // Find the two day cells that are given as parameters
        String startXPath = "//tr[@class='fc-week'][" + startWeek + "]/td[" + startDay + "]";
        String endXPath = "//tr[@class='fc-week'][" + endWeek + "]/td[" + endDay + "]";
        WebElement startCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(startXPath)));
        WebElement endCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(endXPath)));
        // Drag from the start day to the end day
        new Actions(driver)
            .moveToElement(startCell, 1, 1)
            .clickAndHold()
            .moveToElement(endCell, 1, 1)
            .release()
            .perform();
        //(new Actions(driver)).dragAndDrop(startCell, endCell).perform();
        // Check if the start date shows in the confirmation div
        String startDate = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                    By.id("confirmation-start-date")))
                               .getText();
        // Check if the end date shows in the confirmation div
        String endDate = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                    By.id("confirmation-end-date")))
                               .getText();
        // The date has the format "xx month", we need the first two digits, so we split with delimiter " "
        int startDayNumber = Integer.parseInt(startDate.split(" ")[0]);
        int endDayNumber = Integer.parseInt(endDate.split(" ")[0]);
        // Check if the difference between the two dates displayed under the calender is correct
        int difference = (endWeek - startWeek) * 7 + endDay - startDay;
        Assert.assertThat("The date difference doesn't match!",
                          endDayNumber - startDayNumber, is(difference));
        // Check if the day numbers equal the ones in the data-fields of the calendar cells
        String startCellDate = startCell.getAttribute("data-date");
        String endCellDate = endCell.getAttribute("data-date");
        Assert.assertThat("The start day number doesn't match!",
                          "" + startDayNumber, is(startCellDate.split("-")[2]));
        Assert.assertThat("The end day number doesn't match!",
                          "" + endDayNumber, is(endCellDate.split("-")[2]));
    }


    /**
     * This test will first accept all pending reservations, then it will check
     * if the list of pending reservations is empty. Afterwards, all accepted
     * reservations will be rejected, the same check will now happen for the
     * list of accepted reservations. Finally, all rejected reservations will
     * be accepted again.
     */
    public void testReservationsManagement() {
        //First do some checks on the accepting and rejecting reservations for a car owner

        goToCarManagementReservations();

        //Accept all pending reservations
        acceptPendingReservations();
        //Check if all pending reservations are accepted.
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pendingReservationsTable")));
        List<WebElement> checkboxes = row.findElements(By.name("selectedReservations"));
        Assert.assertTrue("Not all pending reservations were accepted!",
                          checkboxes.size() == 0);

        //Afterwards reject them all again
        cancelAcceptedReservations();
        //Check if all accepted reservations are canceled.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showAccResTable"))).click();
        wait = new WebDriverWait(driver, 5);
        row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acceptedReservationsTable")));
        checkboxes = row.findElements(By.name("selectedReservations"));
        Assert.assertTrue("Not all pending reservations were canceled!",
                          checkboxes.size() == 0);

        //Finally accept them again
        acceptRejectedReservations();
        //Check if all rejected reservations are accepted.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showRejResTable"))).click();
        wait = new WebDriverWait(driver, 5);
        row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rejectedReservationsTable")));
        checkboxes = row.findElements(By.name("selectedReservations"));
        Assert.assertTrue("Not all rejected reservations were accepted!",
                          checkboxes.size() == 0);
        //Check if selecting no checkboxes doesn't result in a NullPointerException
        cancelNoAcceptedReservations();
        WebElement errormsg = driver.findElement(By.className("alerticon"));
        Assert.assertNotNull("Error message present!", errormsg);
    }

    /**
     * This test will check what happens when no reservations are selected when
     * clicking the accept button.
     */
    private void cancelNoAcceptedReservations() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showAccResTable"))).click();
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acceptedReservationsTable")));
        List<WebElement> submit = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@id='acceptedReservationsTable']/form/div/div/input[@type='submit'][@name='submit']")));
        submit.get(0).click();
    }

    private void cancelAcceptedReservations() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showAccResTable"))).click();
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acceptedReservationsTable")));
        wait = new WebDriverWait(driver, 10);
        List<WebElement> checkboxes = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//form[@name='acceptedRes']/div/div/table/tbody/tr/td/span/button")));
        for(int i = 0; i < checkboxes.size(); i++) checkboxes.get(i).click();
        row.findElement(By.name("reason")).sendKeys("Omdat dit een test is!");
        List<WebElement> submit = row.findElements(By.name("submit"));
        submit.get(0).click();
    }

    private void acceptRejectedReservations() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("showRejResTable"))).click();
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rejectedReservationsTable")));
        wait = new WebDriverWait(driver, 10);
        List<WebElement> checkboxes = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//form[@name='rejectedRes']/div/div/table/tbody/tr/td/span/button")));
        for(int i = 0; i < checkboxes.size(); i++) checkboxes.get(i).click();
        List<WebElement> submit = row.findElements(By.name("submit"));
        submit.get(0).click();
    }

    private void acceptPendingReservations() {
        WebElement row = driver.findElement(By.id("pendingReservationsTable"));
        List<WebElement> checkboxes = row.findElements(By.xpath("//form[@name='pendingRes']/div/div/table/tbody/tr/td/span/button"));
        for(int i = 0; i < checkboxes.size(); i++) checkboxes.get(i).click();
        List<WebElement> submit = row.findElements(By.name("submit"));
        submit.get(0).click();
    }

    /**
     * First it searches for the tab carmanagement and clicks on it,
     * afterwards it clicks on the tab with the name reservations.
     */
    private void goToCarManagementReservations() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("carmanagement"))).click();
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("reservations"))).click();
    }

    /**
     * Searches for the tab "reserve" and clicks on it.
     */
    private void goToReservations() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("reserve"))).click();
    }
}
