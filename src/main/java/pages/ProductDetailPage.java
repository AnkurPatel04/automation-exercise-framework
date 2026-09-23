package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtil;

/**
 * Page object modeling detailed product specifications, quantity selectors, cart additions, and customer reviews.
 */
public class ProductDetailPage {

    private final WebDriver driver;
    private final WaitUtil waitUtil;

    // Product Information Locators
    private final By productName = By.xpath("//div[@class='product-information']/h2");
    private final By productCategory = By.xpath("//div[@class='product-information']/p[contains(text(), 'Category:')]");
    private final By productPrice = By.xpath("//div[@class='product-information']//span/span[contains(text(), 'Rs.')]");
    private final By productAvailability = By.xpath("//div[@class='product-information']/p[b[contains(text(), 'Availability:')]]");
    private final By productCondition = By.xpath("//div[@class='product-information']/p[b[contains(text(), 'Condition:')]]");
    private final By productBrand = By.xpath("//div[@class='product-information']/p[b[contains(text(), 'Brand:')]]");

    // Quantity & Cart
    private final By quantityInput = By.id("quantity");
    private final By addToCartButton = By.cssSelector("button.cart");
    private final By viewCartModalLink = By.xpath("//div[@id='cartModal']//u[text()='View Cart']/parent::a | //div[@id='cartModal']//a[@href='/view_cart']");

    // Review Form Locators
    private final By reviewNameInput = By.id("name");
    private final By reviewEmailInput = By.id("email");
    private final By reviewTextInput = By.id("review");
    private final By reviewSubmitButton = By.id("button-review");
    private final By reviewSuccessAlert = By.xpath("//div[@id='review-section']//span | //div[contains(@class, 'alert-success')]/span");

    /**
     * Initializes the product detail page object.
     *
     * @param driver the active WebDriver instance
     */
    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
    }

    /**
     * Verifies that the primary product information container is visible.
     *
     * @return true if product details are rendered
     */
    public boolean isProductInformationVisible() {
        return waitUtil.waitForVisible(productName).isDisplayed();
    }

    /**
     * Retrieves the display title of the product.
     *
     * @return product name string
     */
    public String getProductName() {
        return waitUtil.waitForVisible(productName).getText();
    }

    /**
     * Retrieves the category breadcrumb string of the product.
     *
     * @return product category description
     */
    public String getProductCategory() {
        return waitUtil.waitForVisible(productCategory).getText();
    }

    /**
     * Retrieves the listed unit price of the product.
     *
     * @return product price text
     */
    public String getProductPrice() {
        return waitUtil.waitForVisible(productPrice).getText();
    }

    /**
     * Retrieves the inventory availability status text.
     *
     * @return product availability string
     */
    public String getProductAvailability() {
        return waitUtil.waitForVisible(productAvailability).getText();
    }

    /**
     * Retrieves the condition descriptor of the product (e.g., 'New').
     *
     * @return product condition text
     */
    public String getProductCondition() {
        return waitUtil.waitForVisible(productCondition).getText();
    }

    /**
     * Retrieves the manufacturer brand name of the product.
     *
     * @return product brand name
     */
    public String getProductBrand() {
        return waitUtil.waitForVisible(productBrand).getText();
    }

    /**
     * Configures the desired purchase quantity in the quantity input field.
     *
     * @param quantity desired quantity integer
     */
    public void setQuantity(int quantity) {
        WebElement input = waitUtil.waitForVisible(quantityInput);
        input.clear();
        input.sendKeys(String.valueOf(quantity));
    }

    /**
     * Adds the current product with its selected quantity to the shopping cart.
     */
    public void addToCart() {
        waitUtil.waitForClickable(addToCartButton).click();
    }

    /**
     * Clicks the View Cart link presented within the post-add confirmation modal.
     *
     * @return newly initialized CartPage
     */
    public CartPage clickViewCartModal() {
        WebElement link = waitUtil.waitForClickable(viewCartModalLink);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        return new CartPage(driver);
    }

    /**
     * Submits a customer review with author name, email, and feedback text.
     *
     * @param name reviewer name
     * @param email reviewer email address
     * @param review customer feedback body
     */
    public void submitReview(String name, String email, String review) {
        WebElement nameEl = waitUtil.waitForVisible(reviewNameInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nameEl);
        nameEl.sendKeys(name);
        waitUtil.waitForVisible(reviewEmailInput).sendKeys(email);
        waitUtil.waitForVisible(reviewTextInput).sendKeys(review);
        waitUtil.waitForClickable(reviewSubmitButton).click();
    }

    /**
     * Retrieves the thank-you confirmation message displayed upon review submission.
     *
     * @return review submission confirmation message
     */
    public String getReviewSuccessMessage() {
        return waitUtil.waitForVisible(reviewSuccessAlert).getText();
    }
}
