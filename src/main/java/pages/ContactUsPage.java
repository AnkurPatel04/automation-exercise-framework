package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtil;

/**
 * Page object modeling the contact inquiry submission form, file attachment, and confirmation alerts.
 */
public class ContactUsPage {

    private final WebDriver driver;
    private final WaitUtil waitUtil;

    // Contact Form Locators
    private final By getInTouchHeader = By.xpath("//h2[contains(text(), 'Get In Touch')]");
    private final By nameInput = By.cssSelector("input[data-qa='name']");
    private final By emailInput = By.cssSelector("input[data-qa='email']");
    private final By subjectInput = By.cssSelector("input[data-qa='subject']");
    private final By messageTextArea = By.cssSelector("textarea[data-qa='message']");
    private final By fileUploadInput = By.cssSelector("input[name='upload_file']");
    private final By submitButton = By.cssSelector("input[data-qa='submit-button']");

    // Success & Return Locators
    private final By successAlert = By.cssSelector(".status.alert-success");
    private final By homeButton = By.cssSelector("a.btn.btn-success[href='/']");

    /**
     * Initializes the contact us page object.
     *
     * @param driver the active WebDriver instance
     */
    public ContactUsPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
    }

    /**
     * Verifies that the 'GET IN TOUCH' section header is displayed.
     *
     * @return true if header is visible
     */
    public boolean isGetInTouchVisible() {
        return waitUtil.waitForVisible(getInTouchHeader).isDisplayed();
    }

    /**
     * Completes all fields of the contact form including optional file attachment.
     *
     * @param name sender name
     * @param email sender email address
     * @param subject message subject
     * @param message body text
     * @param filePath absolute path to file for upload, or null/empty if none
     */
    public void fillContactForm(String name, String email, String subject, String message, String filePath) {
        waitUtil.waitForVisible(nameInput).sendKeys(name);
        waitUtil.waitForVisible(emailInput).sendKeys(email);
        waitUtil.waitForVisible(subjectInput).sendKeys(subject);
        waitUtil.waitForVisible(messageTextArea).sendKeys(message);

        if (filePath != null && !filePath.trim().isEmpty()) {
            driver.findElement(fileUploadInput).sendKeys(filePath);
        }
    }

    /**
     * Submits the contact us form to trigger the browser confirmation prompt.
     */
    public void clickSubmit() {
        WebElement button = waitUtil.waitForClickable(submitButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    /**
     * Waits for and accepts the browser JavaScript confirmation alert.
     */
    public void acceptConfirmationAlert() {
        Alert alert = waitUtil.waitForAlert();
        alert.accept();
    }

    /**
     * Retrieves the text displayed in the submission success alert banner.
     *
     * @return contact submission success message string
     */
    public String getSuccessMessage() {
        return waitUtil.waitForVisible(successAlert).getText();
    }

    /**
     * Clicks the success screen Home button to return to the home page.
     *
     * @return newly initialized HomePage
     */
    public HomePage clickHomeButton() {
        WebElement button = waitUtil.waitForClickable(homeButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        return new HomePage(driver);
    }
}
