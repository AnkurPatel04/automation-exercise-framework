package listeners;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentManager;
import utils.ScreenshotUtil;

/**
 * TestNG listener bridging test lifecycle events with ExtentReports logging and automated failure screenshots.
 */
public class TestListener implements ITestListener {

    /**
     * Registers a new ExtentTest node when a test method commences execution.
     *
     * @param result details of the commencing test method
     */
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.trim().isEmpty()) {
            description = "Execution of test: " + testName;
        }
        ExtentManager.createTest(testName, description);
    }

    /**
     * Logs successful test execution status in the ExtentReports document.
     *
     * @param result details of the successful test method
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test passed successfully: " + result.getMethod().getMethodName());
        }
        ExtentManager.removeTest();
    }

    /**
     * Captures failure screenshots and attaches them alongside exception stack traces to the report.
     *
     * @param result details of the failed test method
     */
    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        String testName = result.getMethod().getMethodName();

        WebDriver driver = null;
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest) {
            driver = ((BaseTest) testInstance).getDriver();
        }

        if (test != null) {
            test.log(Status.FAIL, "Test failed: " + testName);
            test.log(Status.FAIL, result.getThrowable());

            if (driver != null) {
                String screenshotRelativePath = ScreenshotUtil.captureScreenshot(driver, testName);
                if (screenshotRelativePath != null) {
                    test.fail("Failure Screenshot",
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotRelativePath).build());
                }
            }
        }
        ExtentManager.removeTest();
    }

    /**
     * Records skipped test occurrences and reasons in the test execution report.
     *
     * @param result details of the skipped test method
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
            if (result.getThrowable() != null) {
                test.log(Status.SKIP, result.getThrowable());
            }
        }
        ExtentManager.removeTest();
    }

    /**
     * Finalizes and persists the complete test suite report upon completion of all suite tests.
     *
     * @param context the TestNG test execution context
     */
    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
    }
}
