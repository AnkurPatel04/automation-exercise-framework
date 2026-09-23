package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility for capturing and persisting browser screenshots with automated directory management.
 */
public final class ScreenshotUtil {

    private static final String SCREENSHOTS_DIR = "reports/screenshots";

    private ScreenshotUtil() {
        // Prevent direct instantiation of utility class
    }

    /**
     * Captures a screenshot of the current browser viewport and stores it in the screenshots directory.
     *
     * @param driver the active WebDriver instance
     * @param testName identifier for the running test case
     * @return relative path string to the stored screenshot file
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            return null;
        }

        try {
            // Unconditionally create the screenshots directory before any file write
            Path directoryPath = Paths.get(SCREENSHOTS_DIR);
            Files.createDirectories(directoryPath);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String fileName = testName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
            Path destinationPath = directoryPath.resolve(fileName);

            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(sourceFile.toPath(), destinationPath);

            // Return relative path from reports/extent to reports/screenshots for HTML embedding
            return "../screenshots/" + fileName;
        } catch (IOException e) {
            System.err.println("Failed to capture and save screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Captures a screenshot and returns the raw Base64 string for direct HTML embedding.
     *
     * @param driver the active WebDriver instance
     * @return Base64 encoded screenshot string
     */
    public static String captureBase64(WebDriver driver) {
        if (driver == null) {
            return null;
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
}
