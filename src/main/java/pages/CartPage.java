package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Page object modeling the shopping cart table, item removals, subscription box, and checkout triggers.
 */
public class CartPage {

    private final WebDriver driver;
    private final WaitUtil waitUtil;

    // Cart Items Locators
    private final By cartInfoSection = By.id("cart_info");
    private final By cartRows = By.xpath("//tr[starts-with(@id, 'product-')]");
    private final By emptyCartMessage = By.id("empty_cart");
    private final By proceedToCheckoutButton = By.cssSelector(".check_out");

    // Checkout Modal
    private final By registerLoginModalLink = By.xpath("//div[@id='checkoutModal']//a[@href='/login']");

    // Subscription Locators
    private final By subscriptionHeading = By.xpath("//h2[text()='Subscription']");
    private final By subscriptionEmailInput = By.id("susbscribe_email");
    private final By subscriptionSubmitButton = By.id("subscribe");
    private final By subscriptionSuccessAlert = By.id("success-subscribe");

    /**
     * Initializes the cart page object.
     *
     * @param driver the active WebDriver instance
     */
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
    }

    /**
     * Verifies that the cart table container is displayed on the page.
     *
     * @return true if cart view is visible
     */
    public boolean isCartPageVisible() {
        return waitUtil.waitForVisible(cartInfoSection).isDisplayed();
    }

    /**
     * Counts the total number of distinct product rows currently in the cart table.
     *
     * @return count of cart item rows
     */
    public int getCartItemCount() {
        return driver.findElements(cartRows).size();
    }

    /**
     * Collects the names of all products currently present in the cart.
     *
     * @return list of product name strings
     */
    public List<String> getCartItemNames() {
        List<WebElement> rows = driver.findElements(cartRows);
        List<String> names = new ArrayList<>();
        for (WebElement row : rows) {
            names.add(row.findElement(By.xpath(".//td[@class='cart_description']//h4/a")).getText().trim());
        }
        return names;
    }

    /**
     * Retrieves the listed unit price for a 1-indexed cart row.
     *
     * @param index 1-based row index
     * @return unit price text string
     */
    public String getItemPrice(int index) {
        By priceLocator = By.xpath("(//tr[starts-with(@id, 'product-')])[" + index + "]//td[@class='cart_price']/p");
        return waitUtil.waitForVisible(priceLocator).getText().trim();
    }

    /**
     * Retrieves the selected purchase quantity for a 1-indexed cart row.
     *
     * @param index 1-based row index
     * @return item quantity string
     */
    public String getItemQuantity(int index) {
        By quantityLocator = By.xpath("(//tr[starts-with(@id, 'product-')])[" + index + "]//td[@class='cart_quantity']/button");
        return waitUtil.waitForVisible(quantityLocator).getText().trim();
    }

    /**
     * Retrieves the calculated line total price for a 1-indexed cart row.
     *
     * @param index 1-based row index
     * @return total price text string
     */
    public String getItemTotal(int index) {
        By totalLocator = By.xpath("(//tr[starts-with(@id, 'product-')])[" + index + "]//td[@class='cart_total']/p");
        return waitUtil.waitForVisible(totalLocator).getText().trim();
    }

    /**
     * Removes a product line item from the cart by clicking its delete ('X') button.
     *
     * @param index 1-based row index to delete
     */
    public void removeItemByIndex(int index) {
        By deleteButtonLocator = By.xpath("(//tr[starts-with(@id, 'product-')])[" + index + "]//td[@class='cart_delete']/a[@class='cart_quantity_delete']");
        WebElement deleteBtn = waitUtil.waitForClickable(deleteButtonLocator);
        deleteBtn.click();
        waitUtil.waitForInvisibility(deleteButtonLocator);
    }

    /**
     * Checks whether a specific product name is present among current cart items.
     *
     * @param productName title of the product to search for
     * @return true if the product is present in the cart
     */
    public boolean isProductInCart(String productName) {
        return getCartItemNames().stream().anyMatch(name -> name.equalsIgnoreCase(productName.trim()));
    }

    /**
     * Initiates the checkout sequence by clicking 'Proceed To Checkout' for authenticated users.
     *
     * @return newly initialized CheckoutPage
     */
    public CheckoutPage proceedToCheckout() {
        WebElement checkoutBtn = waitUtil.waitForClickable(proceedToCheckoutButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutBtn);
        return new CheckoutPage(driver);
    }

    /**
     * Clicks 'Proceed To Checkout' expecting the guest authentication prompt modal to appear.
     */
    public void proceedToCheckoutExpectingLoginModal() {
        WebElement checkoutBtn = waitUtil.waitForClickable(proceedToCheckoutButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutBtn);
    }

    /**
     * Clicks the 'Register / Login' link inside the checkout prompt modal.
     *
     * @return newly initialized SignupLoginPage
     */
    public SignupLoginPage clickRegisterLoginFromModal() {
        WebElement link = waitUtil.waitForClickable(registerLoginModalLink);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        return new SignupLoginPage(driver);
    }

    /**
     * Verifies that the footer subscription section heading is visible on the cart page.
     *
     * @return true if subscription heading is visible
     */
    public boolean isSubscriptionHeaderVisible() {
        return waitUtil.waitForVisible(subscriptionHeading).isDisplayed();
    }

    /**
     * Submits an email address into the cart page footer newsletter subscription form.
     *
     * @param email subscription email
     */
    public void subscribeNewsletter(String email) {
        WebElement input = waitUtil.waitForVisible(subscriptionEmailInput);
        input.clear();
        input.sendKeys(email);
        waitUtil.waitForClickable(subscriptionSubmitButton).click();
    }

    /**
     * Retrieves the newsletter subscription confirmation text from the alert banner.
     *
     * @return subscription alert message text
     */
    public String getSubscriptionSuccessMessage() {
        return waitUtil.waitForVisible(subscriptionSuccessAlert).getText();
    }
}
