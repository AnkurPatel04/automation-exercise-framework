package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.WaitUtil;

/**
 * Page object modeling the account registration form and account lifecycle notifications.
 */
public class AccountInformationPage {

    private final WebDriver driver;
    private final WaitUtil waitUtil;

    // Form Header Locators
    private final By accountInfoHeader = By.xpath("//b[contains(text(), 'Enter Account Information')] | //h2[contains(., 'Enter Account Information')]");

    // Profile Details
    private final By titleMrRadio = By.id("id_gender1");
    private final By titleMrsRadio = By.id("id_gender2");
    private final By passwordInput = By.id("password");
    private final By daysSelect = By.id("days");
    private final By monthsSelect = By.id("months");
    private final By yearsSelect = By.id("years");
    private final By newsletterCheckbox = By.id("newsletter");
    private final By optinCheckbox = By.id("optin");

    // Address Information
    private final By firstNameInput = By.id("first_name");
    private final By lastNameInput = By.id("last_name");
    private final By companyInput = By.id("company");
    private final By address1Input = By.id("address1");
    private final By address2Input = By.id("address2");
    private final By countrySelect = By.id("country");
    private final By stateInput = By.id("state");
    private final By cityInput = By.id("city");
    private final By zipcodeInput = By.id("zipcode");
    private final By mobileNumberInput = By.id("mobile_number");
    private final By createAccountButton = By.cssSelector("button[data-qa='create-account']");

    // Lifecycle Confirmation Locators
    private final By accountCreatedHeader = By.cssSelector("h2[data-qa='account-created']");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");
    private final By accountDeletedHeader = By.cssSelector("h2[data-qa='account-deleted']");

    /**
     * Initializes the account registration page object.
     *
     * @param driver the active WebDriver instance
     */
    public AccountInformationPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
    }

    /**
     * Verifies that the 'ENTER ACCOUNT INFORMATION' section header is displayed.
     *
     * @return true if account information header is visible
     */
    public boolean isEnterAccountInformationVisible() {
        return waitUtil.waitForVisible(accountInfoHeader).isDisplayed();
    }

    /**
     * Selects the 'Sign up for our newsletter!' checkbox.
     */
    public void selectNewsletter() {
        WebElement element = waitUtil.waitForClickable(newsletterCheckbox);
        if (!element.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Selects the 'Receive special offers from our partners!' checkbox.
     */
    public void selectSpecialOffers() {
        WebElement element = waitUtil.waitForClickable(optinCheckbox);
        if (!element.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Fills out the entire account creation form with the provided profile and address values.
     *
     * @param password account password
     * @param day day of birth
     * @param month month of birth
     * @param year year of birth
     * @param firstName first name
     * @param lastName last name
     * @param company company name
     * @param address1 primary street address
     * @param address2 apartment or suite
     * @param country country selection
     * @param state state or province
     * @param city city name
     * @param zipcode postal code
     * @param mobileNumber mobile telephone number
     */
    public void fillFullAccountDetails(String password, String day, String month, String year,
                                       String firstName, String lastName, String company,
                                       String address1, String address2, String country,
                                       String state, String city, String zipcode, String mobileNumber) {
        WebElement titleElement = waitUtil.waitForClickable(titleMrRadio);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", titleElement);

        waitUtil.waitForVisible(passwordInput).sendKeys(password);

        new Select(waitUtil.waitForVisible(daysSelect)).selectByValue(day);
        new Select(waitUtil.waitForVisible(monthsSelect)).selectByVisibleText(month);
        new Select(waitUtil.waitForVisible(yearsSelect)).selectByValue(year);

        selectNewsletter();
        selectSpecialOffers();

        waitUtil.waitForVisible(firstNameInput).sendKeys(firstName);
        waitUtil.waitForVisible(lastNameInput).sendKeys(lastName);
        waitUtil.waitForVisible(companyInput).sendKeys(company);
        waitUtil.waitForVisible(address1Input).sendKeys(address1);
        waitUtil.waitForVisible(address2Input).sendKeys(address2);
        new Select(waitUtil.waitForVisible(countrySelect)).selectByVisibleText(country);
        waitUtil.waitForVisible(stateInput).sendKeys(state);
        waitUtil.waitForVisible(cityInput).sendKeys(city);
        waitUtil.waitForVisible(zipcodeInput).sendKeys(zipcode);
        waitUtil.waitForVisible(mobileNumberInput).sendKeys(mobileNumber);
    }

    /**
     * Fills standard default account details with a specified password for standard test execution.
     *
     * @param password account password
     */
    public void fillDefaultAccountDetails(String password) {
        fillFullAccountDetails(password, "15", "July", "1995", "Alex", "Tester",
                "Quality QA Corp", "123 Automation Blvd", "Suite 400", "United States",
                "California", "San Francisco", "94105", "+14155552671");
    }

    /**
     * Submits the completed account information form to create the new account.
     */
    public void clickCreateAccount() {
        WebElement button = waitUtil.waitForClickable(createAccountButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    /**
     * Verifies that the 'ACCOUNT CREATED!' success heading is visible.
     *
     * @return true if account creation banner is visible
     */
    public boolean isAccountCreatedVisible() {
        return waitUtil.waitForVisible(accountCreatedHeader).isDisplayed();
    }

    /**
     * Clicks the post-registration Continue button to proceed to the main portal.
     *
     * @return newly initialized HomePage
     */
    public HomePage clickContinue() {
        WebElement button = waitUtil.waitForClickable(continueButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get(utils.ConfigReader.getUrl());
        }
        return new HomePage(driver);
    }

    /**
     * Verifies that the 'ACCOUNT DELETED!' success heading is visible.
     *
     * @return true if account deleted banner is visible
     */
    public boolean isAccountDeletedVisible() {
        if (driver.getCurrentUrl().contains("#google_vignette") || !driver.getCurrentUrl().contains("delete_account")) {
            driver.get(utils.ConfigReader.getUrl() + "delete_account");
        }
        return waitUtil.waitForVisible(accountDeletedHeader).isDisplayed();
    }

    /**
     * Clicks the post-deletion Continue button to return to the home page.
     *
     * @return newly initialized HomePage
     */
    public HomePage clickDeleteContinue() {
        WebElement button = waitUtil.waitForClickable(continueButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get(utils.ConfigReader.getUrl());
        }
        return new HomePage(driver);
    }
}
