package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import utils.WaitUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Page object modeling the catalog of products, search functionality, category/brand filters, and cart modals.
 */
public class ProductsPage {

    private final WebDriver driver;
    private final WaitUtil waitUtil;

    // Header & Search Locators
    private final By allProductsHeading = By.xpath("//h2[contains(@class, 'title') and contains(text(), 'All Products')]");
    private final By searchedProductsHeading = By.xpath("//h2[contains(@class, 'title') and contains(text(), 'Searched Products')]");
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By productCards = By.cssSelector(".features_items .col-sm-4");
    private final By productNames = By.cssSelector(".features_items .col-sm-4 .productinfo p");

    // Modal Locators
    private final By cartModal = By.id("cartModal");
    private final By continueShoppingButton = By.xpath("//div[@id='cartModal']//button[contains(@class, 'close-modal') or contains(text(), 'Continue Shopping')]");
    private final By viewCartModalLink = By.xpath("//div[@id='cartModal']//u[text()='View Cart']/parent::a | //div[@id='cartModal']//a[@href='/view_cart']");

    // Dynamic Title Header
    private final By pageTitleHeader = By.xpath("//div[@class='features_items']/h2[contains(@class, 'title')]");

    /**
     * Initializes the products catalog page object.
     *
     * @param driver the active WebDriver instance
     */
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
    }

    /**
     * Verifies that the 'ALL PRODUCTS' title heading is displayed.
     *
     * @return true if all products heading is visible
     */
    public boolean isAllProductsVisible() {
        return waitUtil.waitForVisible(allProductsHeading).isDisplayed();
    }

    /**
     * Verifies that the catalog contains one or more product card elements.
     *
     * @return true if products are rendered in the catalog
     */
    public boolean isProductListVisible() {
        return !driver.findElements(productCards).isEmpty();
    }

    /**
     * Executes a product search query using the search bar.
     *
     * @param keyword search term to query
     */
    public void searchProduct(String keyword) {
        WebElement input = waitUtil.waitForVisible(searchInput);
        input.clear();
        input.sendKeys(keyword);
        waitUtil.waitForClickable(searchButton).click();
    }

    /**
     * Verifies that the 'SEARCHED PRODUCTS' title banner is displayed.
     *
     * @return true if searched products header is visible
     */
    public boolean isSearchedProductsHeadingVisible() {
        return waitUtil.waitForVisible(searchedProductsHeading).isDisplayed();
    }

    /**
     * Collects the text names of all products currently displayed in the search or catalog view.
     *
     * @return list of product title strings
     */
    public List<String> getDisplayedProductNames() {
        List<WebElement> elements = driver.findElements(productNames);
        List<String> names = new ArrayList<>();
        for (WebElement element : elements) {
            names.add(element.getText().trim());
        }
        return names;
    }

    /**
     * Navigates to the product detail view for the first product listed in the catalog.
     *
     * @return newly initialized ProductDetailPage
     */
    public ProductDetailPage clickFirstViewProduct() {
        return clickViewProductByIndex(1);
    }

    /**
     * Navigates to the product detail view for a specific 1-indexed product in the catalog.
     *
     * @param index 1-based index of the product card
     * @return newly initialized ProductDetailPage
     */
    public ProductDetailPage clickViewProductByIndex(int index) {
        By viewProductLocator = By.xpath("(//div[@class='choose']//a[contains(@href, '/product_details/')])[" + index + "]");
        WebElement link = waitUtil.waitForClickable(viewProductLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", link);
        link.click();
        return new ProductDetailPage(driver);
    }

    /**
     * Hovers over the specified product card and clicks its Add to Cart button.
     *
     * @param index 1-based index of the product card
     */
    public void addProductToCartByIndex(int index) {
        By productCardLocator = By.xpath("(//div[contains(@class, 'product-image-wrapper')])[" + index + "]");
        By addToCartLocator = By.xpath("(//div[@class='productinfo text-center']//a[contains(@class, 'add-to-cart')])[" + index + "]");

        WebElement card = waitUtil.waitForVisible(productCardLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", card);

        Actions actions = new Actions(driver);
        actions.moveToElement(card).perform();

        WebElement addToCartBtn = waitUtil.waitForClickable(addToCartLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
    }

    /**
     * Clicks the 'Continue Shopping' button within the post-add cart confirmation modal.
     */
    public void clickContinueShopping() {
        waitUtil.waitForVisible(cartModal);
        WebElement button = waitUtil.waitForClickable(continueShoppingButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    /**
     * Clicks the 'View Cart' link within the post-add cart confirmation modal.
     *
     * @return newly initialized CartPage
     */
    public CartPage clickViewCartModal() {
        waitUtil.waitForVisible(cartModal);
        WebElement link = waitUtil.waitForClickable(viewCartModalLink);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        return new CartPage(driver);
    }

    /**
     * Expands a parent category accordion in the left sidebar and selects a subcategory link.
     *
     * @param category parent category name (e.g., 'Women', 'Men')
     * @param subCategory target subcategory text (e.g., 'Dress', 'Tshirts')
     */
    public void selectCategory(String category, String subCategory) {
        By categoryAccordion = By.xpath("//a[@data-toggle='collapse' and contains(., '" + category + "')]");
        WebElement accordion = waitUtil.waitForClickable(categoryAccordion);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", accordion);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accordion);

        By subCategoryLink = By.xpath("//div[@id='" + category + "']//a[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + subCategory.toLowerCase() + "')]");
        WebElement subLink = waitUtil.waitForClickable(subCategoryLink);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", subLink);
    }

    /**
     * Selects a brand filter link from the Brands sidebar panel.
     *
     * @param brandName name of the brand to filter by
     */
    public void selectBrand(String brandName) {
        By brandLinkLocator = By.xpath("//div[@class='brands-name']//a[contains(., '" + brandName + "')]");
        WebElement brandLink = waitUtil.waitForClickable(brandLinkLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", brandLink);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", brandLink);
    }

    /**
     * Retrieves the text of the primary title banner for the current filtered view.
     *
     * @return active page title heading string
     */
    public String getPageTitleText() {
        return waitUtil.waitForVisible(pageTitleHeader).getText();
    }
}
