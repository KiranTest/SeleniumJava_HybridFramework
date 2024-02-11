package com.qaexamples.testcases;

import com.qaexamples.util.CustomBy;
import com.qaexamples.util.CustomException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRegistrationFormTest {

    WebDriverWait wait;
    JavascriptExecutor executor;
    ChromeDriver driver;
    public static final String BASE_URL = "https://demoqa.com/";
    public static final String FILE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "ProfilePic.jpg";

    String logoImageDesc = "/images/Toolsqa.jpg";
    String formsHeaderDesc = "Forms";
    String formHeaderLeftMenu = "Forms";
    String formLink = "Practice Form";
    String partialText = "En";
    String subject = "English";
    String hobbiesValue = "Sports";
    WebElement reactDatePicker;

    public final By FORMS_HEADER = By.xpath(String.format("//h5[text()='%s']", formsHeaderDesc));
    public final By PRACTICE_FORM_LINK = By.xpath(String.format("//div[text()='%s']/../../following-sibling::div//li/span[text()='%s']", formHeaderLeftMenu, formLink));
    public final By FIRST_NAME = By.id("firstName");
    public final By LAST_NAME = By.id("lastName");
    public final By EMAIL_ID = By.id("userEmail");
    public final By GENDER_MALE = By.xpath("//label[@for='gender-radio-1']");
    public final By MOBILE_NUMBER = By.id("userNumber");
    public final By DOB = By.id("dateOfBirthInput");
    public final By DATE_PICKER = By.cssSelector(".react-datepicker");
    public final By DATE_PICKER_YEAR = By.cssSelector(".react-datepicker__year-select");
    public final By DATE_PICKER_MONTH = By.cssSelector(".react-datepicker__month-select");
    public final By DATE_PICKER_MONTH_WEEK = By.cssSelector(".react-datepicker__month");

    public final By SUBJECTS_INPUT = By.id("subjectsInput");
    public final By SUBJECTS_DROPDOWN = By.cssSelector("div.subjects-auto-complete__menu-list");
    public final By SUBJECTS_OPTIONS_LIST = By.cssSelector("div.subjects-auto-complete__option");
    public final By SUBJECTS_BYVALUE = By.xpath("//div[contains(@class,'subjects-auto-complete__option') and text()='" + subject + "']");
    public final By HOBBIE_BYVALUE = By.xpath("//label[text()='" + hobbiesValue + "']");
    public final By UPLOAD_PIC = By.id("uploadPicture");
    public final By ADDRESS = By.id("currentAddress");
    public final By STATE = By.id("state");
    public final By STATE_DROPDOWN = By.xpath("//div[@id='state']//div[contains(@class,'menu')]");
    public final By STATE_OPTIONS= By.xpath("/div/div[contains(@class,'-option')]");
    public final By CITY = By.id("city");

    @BeforeTest
    private void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-infobars");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("excludeSwitches", new String[]{"load-extension", "enable-automation"});
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        executor = (JavascriptExecutor) driver;
    }

    @Test(groups = {"Regression", "SystemIntegration"})
    public void verifyUserRegistrationWithValidData() throws MalformedURLException, InterruptedException {

        // Navigate to the HomePage
        gotoURL(new URL(BASE_URL));
        wait.until(ExpectedConditions.elementToBeClickable(CustomBy.imageSource(logoImageDesc)));

        // Click on the forms Header
        waitAndClick(FORMS_HEADER);

        // Click on Practice Form link
        waitAndClick(PRACTICE_FORM_LINK);

        // Fill the form with proper details
        sendKeys(FIRST_NAME, "Smith");
        sendKeys(LAST_NAME, "Ford");
        sendKeys(EMAIL_ID, "user@testemail.com");
        selectRadioJS(GENDER_MALE);
        sendKeys(MOBILE_NUMBER, "2234231234");

        // Date Picker
        selectDOB("1982", "July", "10");

        // Select subjects - enter partial or full text and then select the value from the dropdown
        selectSubjects(partialText);
        waitAndClick(HOBBIE_BYVALUE);
        // File Upload
        WebElement fileInput = waitForElement(UPLOAD_PIC);
        fileInput.sendKeys(FILE_PATH);

        sendKeys(ADDRESS, "1234 Elm Street, Apt 567, Springfield, USA");
        waitAndClick(STATE);
        WebElement stateDropDown = waitForElement(STATE_DROPDOWN);
        List<WebElement> stateList = stateDropDown.findElements(STATE_OPTIONS);
        if (! stateList.isEmpty()) {
            stateList.forEach(e -> System.out.println("Options visible: " + e.getText()));
            //selecting the top value
            WebElement optionVisible = stateList.stream().filter(WebElement :: isDisplayed).findFirst().orElseThrow();
            System.out.println("Attempting to click on the option value: " + optionVisible.getText());
            optionVisible.click();
        } else
            throw new CustomException("Error displaying option elements in the state dropdown menu: List is empty");


    }

    public void selectDOB(String year, String month, String day) {
        waitAndClick(DOB);
        reactDatePicker = waitForElement(DATE_PICKER);
        datePickerFindElement(DATE_PICKER_YEAR, "option[@value='" + year + "']");
        datePickerFindElement(DATE_PICKER_MONTH, "option[text()='" + month + "']");
        datePickerFindElement(DATE_PICKER_MONTH_WEEK, "//div[@class='react-datepicker__week']/div[text()='" + day + "'][contains(@class,'react-datepicker__day')]");
    }

    @AfterMethod
    private void teardown() {
        driver.quit();
    }


    private void selectSubjects(String partialText) {
        sendKeys(SUBJECTS_INPUT, partialText);
        List<WebElement> optionsVisible = waitForElement(SUBJECTS_DROPDOWN).findElements(SUBJECTS_OPTIONS_LIST);
        if (! optionsVisible.isEmpty()) {
            optionsVisible.forEach(e -> System.out.println("Options visible: " + e.getText()));
            //selecting the top value
            WebElement optionVisible = optionsVisible.stream().filter(WebElement :: isDisplayed).findFirst().orElseThrow();
            System.out.println("Attempting to click on the option value: " + optionVisible.getText());
            optionVisible.click();
        } else
            throw new CustomException("Error displaying option elements in the subject dropdown menu: List is empty");
    }

    private void datePickerFindElement(By DATE_PICKER_YEAR, String xpathExpression) {
        WebElement datePickerYear = reactDatePicker.findElement(DATE_PICKER_YEAR);
        datePickerYear.click();
        datePickerYear.findElement(By.xpath(xpathExpression)).click();
    }

    private void gotoURL(URL url) {
        String urlString = url.toString();
        System.out.println("Navigating to URL: " + urlString);
        try {
            driver.get(urlString);
            System.out.println("Navigation successful.");
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception stack trace
            throw new CustomException("Error navigating to URL: " + urlString, e);
        }
    }

    private WebElement waitForElement(By locatorVal) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locatorVal));
        } catch (Exception e) {
            throw new CustomException("Error waiting for the element with locator: " + locatorVal, e);
        }
    }

    private WebElement waitForElements(By locatorVal) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locatorVal));
        } catch (Exception e) {
            throw new CustomException("Error waiting for the element with locator: " + locatorVal, e);
        }
    }

    private void waitAndClick(By locatorVal) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locatorVal)).click();
            System.out.println("Clicked the element located by: " + locatorVal);
        } catch (Exception e) {
            throw new CustomException("Error clicking the element with locator: " + locatorVal, e);
        }
    }

    private void sendKeys(By locatorVal, String message) {
        try {
            WebElement element = waitForElement(locatorVal);
            element.sendKeys(message);
            System.out.println("Entered text '" + message + "' in the element located by: " + locatorVal);
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage() + e.getStackTrace());
            throw new CustomException("Error sending text for the element with locator: " + locatorVal, e);
        }
    }

    private void sendKeysWithEnter(By locatorVal, String message) {
        try {
            WebElement element = waitForElement(locatorVal);
            element.clear();
            element.sendKeys(message, Keys.ENTER);
        } catch (Exception e) {
            throw new CustomException("Error sending text for the element with locator: " + locatorVal, e);
        }
    }

    private void selectRadio(By locatorVal) {
        try {
            WebElement element = waitForElement(locatorVal);
            if (! element.isSelected())
                element.click();
        } catch (Exception e) {
            throw new CustomException("Error selecting radio element with locator: " + locatorVal, e);
        }
    }

    private void selectRadioJS(By locatorVal) {
        try {
            WebElement element = waitForElement(locatorVal);
            executor.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            throw new CustomException("Error selecting radio element with locator: " + locatorVal, e);
        }
    }


}
