package base;

import drivers.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.ConfigReader;

/**
 * Abstract base class managing WebDriver lifecycle and test setup/teardown hooks.
 */
@Listeners(listeners.TestListener.class)
public abstract class BaseTest {

    protected WebDriver driver;

    /**
     * Initializes the WebDriver instance, maximizes the viewport, and navigates to the application URL.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();
        driver.manage().window().maximize();
        driver.get(ConfigReader.getUrl());
    }

    /**
     * Safely quits the active WebDriver session after test method completion.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Retrieves the current WebDriver instance associated with this test.
     *
     * @return the active WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
}
