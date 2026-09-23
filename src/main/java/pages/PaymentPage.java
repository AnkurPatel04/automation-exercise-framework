package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtil;

/**
 * Page object modeling payment processing, order placement, success confirmation, and invoice download.
 */
public class PaymentPage {

    private final WebDriver driver;
    private final WaitUtil waitUtil;

    // Card Details Locators
    private final By nameOnCardInput = By.cssSelector("input[data-qa='name-on-card']");
    private final By cardNumberInput = By.cssSelector("input[data-qa='card-number']");
    private final By cvcInput = By.cssSelector("input[data-qa='cvc']");
    private final By expiryMonthInput = By.cssSelector("input[data-qa='expiry-month']");
    private final By expiryYearInput = By.cssSelector("input[data-qa='expiry-year']");
    private final By payAndConfirmButton = By.cssSelector("button[data-qa='pay-button']");

    // Success Confirmation Locators
    private final By orderPlacedHeader = By.cssSelector("h2[data-qa='order-placed']");
    private final By orderSuccessMessage = By.xpath("//div[@id='success_message']//div[contains(@class, 'alert-success')] | //p[contains(text(), 'Your order has been placed successfully!')]");
    private final By downloadInvoiceButton = By.xpath("//a[contains(@href, '/download_invoice') or contains(text(), 'Download Invoice')]");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");

    /**
     * Initializes the payment page object.
     *
     * @param driver the active WebDriver instance
     */
    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
    }

    /**
     * Enters payment credit card credentials into the corresponding form fields.
     *
     * @param name cardholder name
     * @param cardNumber credit card number
     * @param cvc card verification code
     * @param expiryMonth two-digit or single-digit expiry month
     * @param expiryYear four-digit expiry year
     */
    public void fillPaymentDetails(String name, String cardNumber, String cvc, String expiryMonth, String expiryYear) {
        waitUtil.waitForVisible(nameOnCardInput).sendKeys(name);
        waitUtil.waitForVisible(cardNumberInput).sendKeys(cardNumber);
        waitUtil.waitForVisible(cvcInput).sendKeys(cvc);
        waitUtil.waitForVisible(expiryMonthInput).sendKeys(expiryMonth);
        waitUtil.waitForVisible(expiryYearInput).sendKeys(expiryYear);
    }

    /**
     * Submits payment authorization by clicking the 'Pay and Confirm Order' button.
     */
    public void clickPayAndConfirmOrder() {
        WebElement button = waitUtil.waitForClickable(payAndConfirmButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    /**
     * Verifies that the order placement confirmation header is displayed.
     *
     * @return true if order confirmed header is visible
     */
    public boolean isOrderSuccessMessageVisible() {
        return waitUtil.waitForVisible(orderPlacedHeader).isDisplayed();
    }

    /**
     * Retrieves the confirmation text of the placed order.
     *
     * @return order confirmation message text
     */
    public String getOrderSuccessMessage() {
        return waitUtil.waitForVisible(orderPlacedHeader).getText();
    }

    /**
     * Checks if the Download Invoice button is displayed and enabled on the confirmation screen.
     *
     * @return true if invoice download button is visible
     */
    public boolean isDownloadInvoiceButtonVisible() {
        return waitUtil.waitForVisible(downloadInvoiceButton).isDisplayed();
    }

    /**
     * Triggers invoice download by clicking the 'Download Invoice' button.
     */
    public void clickDownloadInvoice() {
        WebElement button = waitUtil.waitForClickable(downloadInvoiceButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    /**
     * Clicks the post-order Continue button to return to the home page.
     *
     * @return newly initialized HomePage
     */
    public HomePage clickContinue() {
        waitUtil.waitForClickable(continueButton).click();
        return new HomePage(driver);
    }
}
