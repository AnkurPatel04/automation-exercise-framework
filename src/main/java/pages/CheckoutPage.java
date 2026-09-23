package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Page object modeling the order review step, address verification, and order comments.
 */
public class CheckoutPage {

    private final WebDriver driver;
    private final WaitUtil waitUtil;

    // Address Details Locators
    private final By addressDetailsHeader = By.xpath("//h2[contains(text(), 'Address Details')]");
    private final By deliveryAddressContainer = By.id("address_delivery");
    private final By deliveryAddressLines = By.xpath("//ul[@id='address_delivery']/li");
    private final By billingAddressContainer = By.id("address_invoice");
    private final By billingAddressLines = By.xpath("//ul[@id='address_invoice']/li");

    // Review & Comments
    private final By commentTextArea = By.xpath("//textarea[@name='message']");
    private final By placeOrderButton = By.xpath("//a[@href='/payment']");

    /**
     * Initializes the checkout page object.
     *
     * @param driver the active WebDriver instance
     */
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
    }

    /**
     * Verifies that the Address Details section heading is displayed.
     *
     * @return true if address section is visible
     */
    public boolean isAddressDetailsVisible() {
        return waitUtil.waitForVisible(addressDetailsHeader).isDisplayed();
    }

    /**
     * Retrieves the entire raw text content of the delivery address card.
     *
     * @return delivery address text block
     */
    public String getDeliveryAddressText() {
        return waitUtil.waitForVisible(deliveryAddressContainer).getText();
    }

    /**
     * Retrieves the entire raw text content of the billing address card.
     *
     * @return billing address text block
     */
    public String getBillingAddressText() {
        return waitUtil.waitForVisible(billingAddressContainer).getText();
    }

    /**
     * Collects all individual text lines from the delivery address card.
     *
     * @return list of delivery address lines
     */
    public List<String> getDeliveryAddressLines() {
        List<WebElement> elements = driver.findElements(deliveryAddressLines);
        List<String> lines = new ArrayList<>();
        for (WebElement element : elements) {
            lines.add(element.getText().trim());
        }
        return lines;
    }

    /**
     * Collects all individual text lines from the billing address card.
     *
     * @return list of billing address lines
     */
    public List<String> getBillingAddressLines() {
        List<WebElement> elements = driver.findElements(billingAddressLines);
        List<String> lines = new ArrayList<>();
        for (WebElement element : elements) {
            lines.add(element.getText().trim());
        }
        return lines;
    }

    /**
     * Enters an order instruction or remark in the order comment textarea.
     *
     * @param comment customer comment text
     */
    public void enterOrderComment(String comment) {
        WebElement textarea = waitUtil.waitForVisible(commentTextArea);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", textarea);
        textarea.clear();
        textarea.sendKeys(comment);
    }

    /**
     * Proceeds to the payment gateway step by clicking the 'Place Order' button.
     *
     * @return newly initialized PaymentPage
     */
    public PaymentPage clickPlaceOrder() {
        WebElement button = waitUtil.waitForClickable(placeOrderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        return new PaymentPage(driver);
    }
}
