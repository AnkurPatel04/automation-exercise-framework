package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtil;

/**
 * Page object modeling the Automation Exercise home page with navigation, subscription, and banners.
 */
public class HomePage {

    private final WebDriver driver;
    private final WaitUtil waitUtil;

    // Navigation locators
    private final By homeNav = By.xpath("//header//a[contains(text(), 'Home')]");
    private final By productsNav = By.cssSelector("a[href='/products']");
    private final By cartNav = By.cssSelector("a[href='/view_cart']");
    private final By signupLoginNav = By.cssSelector("a[href='/login']");
    private final By testCasesNav = By.cssSelector("a[href='/test_cases']");
    private final By contactUsNav = By.cssSelector("a[href='/contact_us']");
    private final By logoutNav = By.cssSelector("a[href='/logout']");
    private final By deleteAccountNav = By.cssSelector("a[href='/delete_account']");
    private final By loggedInUserText = By.xpath("//header//li[contains(., 'Logged in as')]");

    // Subscription locators
    private final By subscriptionHeading = By.xpath("//h2[text()='Subscription']");
    private final By subscriptionEmailInput = By.id("susbscribe_email");
    private final By subscriptionSubmitButton = By.id("subscribe");
    private final By subscriptionSuccessAlert = By.id("success-subscribe");

    // Scroll & Hero text locators
    private final By scrollUpArrow = By.id("scrollUp");
    private final By heroBannerText = By.xpath("//div[contains(@class, 'carousel-inner')]//h2[contains(text(), 'Full-Fledged practice website for Automation Engineers')]");

    // Recommended items locators
    private final By recommendedItemsHeading = By.xpath("//h2[contains(text(), 'recommended items')]");
    private final By recommendedAddToCartButton = By.cssSelector("#recommended-item-carousel .item.active .add-to-cart, #recommended-item-carousel .add-to-cart");
    private final By modalViewCartLink = By.xpath("//div[@id='cartModal']//u[text()='View Cart']/parent::a | //div[@id='cartModal']//a[@href='/view_cart']");

    /**
     * Initializes the home page object and synchronization utility.
     *
     * @param driver the active WebDriver instance
     */
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
    }

    /**
     * Verifies that the home page is loaded and the primary brand logo or home navigation item is visible.
     *
     * @return true if the home page is displayed
     */
    public boolean isHomePageVisible() {
        return waitUtil.waitForVisible(homeNav).isDisplayed();
    }

    /**
     * Navigates to the products catalog page.
     *
     * @return newly initialized ProductsPage
     */
    public ProductsPage clickProducts() {
        waitUtil.waitForClickable(productsNav).click();
        return new ProductsPage(driver);
    }

    /**
     * Navigates to the shopping cart page.
     *
     * @return newly initialized CartPage
     */
    public CartPage clickCart() {
        waitUtil.waitForClickable(cartNav).click();
        return new CartPage(driver);
    }

    /**
     * Navigates to the user signup and authentication page.
     *
     * @return newly initialized SignupLoginPage
     */
    public SignupLoginPage clickSignupLogin() {
        waitUtil.waitForClickable(signupLoginNav).click();
        return new SignupLoginPage(driver);
    }

    /**
     * Navigates to the test cases overview page.
     */
    public void clickTestCases() {
        waitUtil.waitForClickable(testCasesNav).click();
    }

    /**
     * Navigates to the contact us submission form.
     *
     * @return newly initialized ContactUsPage
     */
    public ContactUsPage clickContactUs() {
        waitUtil.waitForClickable(contactUsNav).click();
        return new ContactUsPage(driver);
    }

    /**
     * Terminates the current user session by clicking the logout navigation link.
     *
     * @return newly initialized SignupLoginPage
     */
    public SignupLoginPage clickLogout() {
        waitUtil.waitForClickable(logoutNav).click();
        return new SignupLoginPage(driver);
    }

    /**
     * Navigates to the account deletion confirmation endpoint.
     *
     * @return newly initialized AccountInformationPage
     */
    public AccountInformationPage clickDeleteAccount() {
        WebElement element = waitUtil.waitForClickable(deleteAccountNav);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        if (driver.getCurrentUrl().contains("#google_vignette")) {
            driver.get(utils.ConfigReader.getUrl() + "delete_account");
        }
        return new AccountInformationPage(driver);
    }

    /**
     * Checks whether the header displays the authenticated user name badge.
     *
     * @return true if logged in text is visible
     */
    public boolean isLoggedInAsVisible() {
        return waitUtil.waitForVisible(loggedInUserText).isDisplayed();
    }

    /**
     * Retrieves the full text content of the logged-in user header element.
     *
     * @return logged in status text
     */
    public String getLoggedInUserText() {
        return waitUtil.waitForVisible(loggedInUserText).getText();
    }

    /**
     * Scrolls the current browser viewport to the bottom of the page.
     */
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Scrolls the current browser viewport to the very top of the page.
     */
    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Checks if the footer subscription section header is visible.
     *
     * @return true if subscription header is displayed
     */
    public boolean isSubscriptionHeaderVisible() {
        return waitUtil.waitForVisible(subscriptionHeading).isDisplayed();
    }

    /**
     * Enters an email address into the footer subscription box and submits the form.
     *
     * @param email subscription email address
     */
    public void subscribeNewsletter(String email) {
        WebElement input = waitUtil.waitForVisible(subscriptionEmailInput);
        input.clear();
        input.sendKeys(email);
        waitUtil.waitForClickable(subscriptionSubmitButton).click();
    }

    /**
     * Retrieves the confirmation text displayed upon successful newsletter subscription.
     *
     * @return subscription alert message text
     */
    public String getSubscriptionSuccessMessage() {
        return waitUtil.waitForVisible(subscriptionSuccessAlert).getText();
    }

    /**
     * Clicks the floating scroll-up arrow button located in the bottom-right corner.
     */
    public void clickScrollUpArrow() {
        waitUtil.waitForClickable(scrollUpArrow).click();
    }

    /**
     * Verifies that the top hero carousel banner text is visible on screen.
     *
     * @return true if hero banner text is visible
     */
    public boolean isHeroBannerTextVisible() {
        return waitUtil.waitForVisible(heroBannerText).isDisplayed();
    }

    /**
     * Verifies that the recommended items section header is visible on the page.
     *
     * @return true if recommended items header is visible
     */
    public boolean isRecommendedItemsVisible() {
        return waitUtil.waitForVisible(recommendedItemsHeading).isDisplayed();
    }

    /**
     * Adds an active recommended item into the shopping cart and navigates to the cart page via modal.
     *
     * @return newly initialized CartPage
     */
    public CartPage addRecommendedItemToCartAndViewCart() {
        WebElement addToCartBtn = waitUtil.waitForClickable(recommendedAddToCartButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addToCartBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
        waitUtil.waitForClickable(modalViewCartLink).click();
        return new CartPage(driver);
    }
}
