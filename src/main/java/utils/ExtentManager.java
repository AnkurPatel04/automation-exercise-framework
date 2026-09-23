package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Thread-safe singleton manager providing ExtentReports instance and active ExtentTest nodes.
 */
public final class ExtentManager {

    private static final String EXTENT_DIR = "reports/extent";
    private static final String REPORT_FILE_PATH = EXTENT_DIR + "/extent-report.html";
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> TEST_CONTAINER = new ThreadLocal<>();

    private ExtentManager() {
        // Prevent direct instantiation of utility class
    }

    /**
     * Initializes and returns the shared ExtentReports instance, creating target directories if needed.
     *
     * @return the configured ExtentReports instance
     */
    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            try {
                // Unconditionally create the extent directory before any file write
                Path dir = Paths.get(EXTENT_DIR);
                Files.createDirectories(dir);

                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_FILE_PATH);
                sparkReporter.config().setDocumentTitle("Automation Exercise Execution Report");
                sparkReporter.config().setReportName("UI Test Automation Results");
                sparkReporter.config().setTheme(Theme.STANDARD);
                sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

                extentReports = new ExtentReports();
                extentReports.attachReporter(sparkReporter);
                extentReports.setSystemInfo("Application", "Automation Exercise");
                extentReports.setSystemInfo("Base URL", ConfigReader.getUrl());
                extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
                extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
                extentReports.setSystemInfo("Browser", ConfigReader.getBrowser());
                extentReports.setSystemInfo("Headless Mode", String.valueOf(ConfigReader.isHeadless()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to create reports/extent directory: " + e.getMessage(), e);
            }
        }
        return extentReports;
    }

    /**
     * Creates and registers a new test node under ExtentReports for the current executing thread.
     *
     * @param testName name of the test method being executed
     * @param description brief description of the test case
     * @return the initialized ExtentTest node
     */
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        TEST_CONTAINER.set(test);
        return test;
    }

    /**
     * Retrieves the current thread's active ExtentTest node.
     *
     * @return the ExtentTest instance for the calling thread
     */
    public static ExtentTest getTest() {
        return TEST_CONTAINER.get();
    }

    /**
     * Unregisters and removes the ExtentTest node for the current thread to prevent memory leakage.
     */
    public static void removeTest() {
        TEST_CONTAINER.remove();
    }

    /**
     * Flushes and finalizes all test execution records to the HTML report file.
     */
    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
