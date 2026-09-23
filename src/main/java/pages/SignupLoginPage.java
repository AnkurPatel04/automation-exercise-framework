package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtil;

/**
 * Page object modeling the Signup and Login portal with dual authentication forms.
 */
public class SignupLoginPage {

    private final WebDriver driver;
    private final WaitUtil waitUtil;

    // Login Form Locators
    private final By loginHeader = By.xpath("//div[@class='login-form']/h2[text()='Login to your account']");
    private final By loginEmailInput = By.cssSelector("input[data-qa='login-email']");
    private final By loginPasswordInput = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");
    private final By loginErrorMessage = By.xpath("//form[@action='/login']/p");

    // Signup Form Locators
    private final By signupHeader = By.xpath("//div[@class='signup-form']/h2[text()='New User Signup!']");
    private final By signupNameInput = By.cssSelector("input[data-qa='signup-name']");
    private final By signupEmailInput = By.cssSelector("input[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("button[data-qa='signup-button']");
    private final By signupErrorMessage = By.xpath("//form[@action='/signup']/p");

    /**
     * Initializes the signup/login page object.
     *
     * @param driver the active WebDriver instance
     */
    public SignupLoginPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
    }

    /**
     * Verifies that the 'Login to your account' form header is displayed.
     *
     * @return true if login header is visible
     */
    public boolean isLoginHeaderVisible() {
        return waitUtil.waitForVisible(loginHeader).isDisplayed();
    }

    /**
     * Verifies that the 'New User Signup!' form header is displayed.
     *
     * @return true if signup header is visible
     */
    public boolean isSignupHeaderVisible() {
        return waitUtil.waitForVisible(signupHeader).isDisplayed();
    }

    /**
     * Submits credentials to log into the application with valid account details.
     *
     * @param email registered email address
     * @param password valid account password
     * @return newly initialized HomePage upon successful authentication
     */
    public HomePage login(String email, String password) {
        waitUtil.waitForVisible(loginEmailInput).sendKeys(email);
        waitUtil.waitForVisible(loginPasswordInput).sendKeys(password);
        waitUtil.waitForClickable(loginButton).click();
        return new HomePage(driver);
    }

    /**
     * Attempts a login with credentials intended to fail for validation checks.
     *
     * @param email incorrect or invalid email
     * @param password incorrect or invalid password
     */
    public void loginExpectingFailure(String email, String password) {
        waitUtil.waitForVisible(loginEmailInput).sendKeys(email);
        waitUtil.waitForVisible(loginPasswordInput).sendKeys(password);
        waitUtil.waitForClickable(loginButton).click();
    }

    /**
     * Initiates user registration with initial name and email address.
     *
     * @param name full user display name
     * @param email new unregistered email address
     * @return newly initialized AccountInformationPage for full profile completion
     */
    public AccountInformationPage signup(String name, String email) {
        waitUtil.waitForVisible(signupNameInput).sendKeys(name);
        waitUtil.waitForVisible(signupEmailInput).sendKeys(email);
        waitUtil.waitForClickable(signupButton).click();
        return new AccountInformationPage(driver);
    }

    /**
     * Attempts user registration with duplicate data expected to be rejected.
     *
     * @param name user name
     * @param email existing or already registered email
     */
    public void signupExpectingFailure(String name, String email) {
        waitUtil.waitForVisible(signupNameInput).sendKeys(name);
        waitUtil.waitForVisible(signupEmailInput).sendKeys(email);
        waitUtil.waitForClickable(signupButton).click();
    }

    /**
     * Retrieves the error text displayed when invalid login credentials are submitted.
     *
     * @return login error message string
     */
    public String getLoginErrorMessage() {
        return waitUtil.waitForVisible(loginErrorMessage).getText();
    }

    /**
     * Retrieves the error text displayed when an already registered email is submitted.
     *
     * @return signup error message string
     */
    public String getSignupErrorMessage() {
        return waitUtil.waitForVisible(signupErrorMessage).getText();
    }
}
