package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Utility wrapper providing explicit synchronization strategies for WebElements.
 */
public class WaitUtil {

    private final WebDriverWait wait;
    private final WebDriver driver;

    /**
     * Initializes the wait utility using the configured explicit wait timeout.
     *
     * @param driver the active WebDriver instance
     */
    public WaitUtil(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
    }

    /**
     * Waits until the element located by the specified locator is visible in the DOM and rendered.
     *
     * @param locator locator strategy for the target element
     * @return the visible WebElement instance
     */
    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the given WebElement is visible and rendered on the page.
     *
     * @param element the target WebElement
     * @return the visible WebElement instance
     */
    public WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until the element located by the specified locator is visible and enabled for user clicks.
     *
     * @param locator locator strategy for the target element
     * @return the clickable WebElement instance
     */
    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the given WebElement is visible and enabled for user clicks.
     *
     * @param element the target WebElement
     * @return the clickable WebElement instance
     */
    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until the element located by the specified locator contains the expected text substring.
     *
     * @param locator locator strategy for the target element
     * @param text expected text substring to wait for
     * @return true once the text condition is met
     */
    public boolean waitForTextPresent(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Waits until the element located by the specified locator becomes invisible or removed from the DOM.
     *
     * @param locator locator strategy for the target element
     * @return true when the element is invisible
     */
    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits until the current browser URL contains the expected partial URL string.
     *
     * @param partialUrl substring expected in the current URL
     * @return true once the URL matches the condition
     */
    public boolean waitForUrlContains(String partialUrl) {
        return wait.until(ExpectedConditions.urlContains(partialUrl));
    }

    /**
     * Waits until the page title contains the expected partial title string.
     *
     * @param partialTitle substring expected in the page title
     * @return true once the title matches the condition
     */
    public boolean waitForTitleContains(String partialTitle) {
        return wait.until(ExpectedConditions.titleContains(partialTitle));
    }

    /**
     * Waits for an active JavaScript alert dialog to be present on the page.
     *
     * @return the alert instance once present
     */
    public org.openqa.selenium.Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }
}
