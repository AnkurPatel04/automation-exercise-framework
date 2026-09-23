package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.WaitUtil;

/**
 * Test suite verifying navigation links, Test Cases routing, and bidirectional page scrolling behavior.
 */
public class NavigationTest extends BaseTest {

    /**
     * Verifies that clicking the 'Test Cases' header navigation link lands on the test cases overview page.
     */
    @Test(description = "Test Case 7: Verify Test Cases Page navigation")
    public void testVerifyTestCasesPage_TC07() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        homePage.clickTestCases();

        WaitUtil waitUtil = new WaitUtil(driver);
        Assert.assertTrue(waitUtil.waitForUrlContains("test_cases"), "User was not navigated to the test cases page");
    }

    /**
     * Verifies scrolling down to the footer and returning to the top using the floating arrow button.
     */
    @Test(description = "Test Case 25: Verify Scroll Up using 'Arrow' button and Scroll Down functionality")
    public void testScrollUpWithArrow_TC25() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        homePage.scrollToBottom();
        Assert.assertTrue(homePage.isSubscriptionHeaderVisible(), "'SUBSCRIPTION' text is not visible after scrolling to bottom");

        homePage.clickScrollUpArrow();
        Assert.assertTrue(homePage.isHeroBannerTextVisible(), "Top hero banner text is not visible after scroll-up arrow click");
    }

    /**
     * Verifies scrolling down to the footer and returning to the top programmatically without using the arrow button.
     */
    @Test(description = "Test Case 26: Verify Scroll Up without 'Arrow' button and Scroll Down functionality")
    public void testScrollUpWithoutArrow_TC26() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        homePage.scrollToBottom();
        Assert.assertTrue(homePage.isSubscriptionHeaderVisible(), "'SUBSCRIPTION' text is not visible after scrolling to bottom");

        homePage.scrollToTop();
        Assert.assertTrue(homePage.isHeroBannerTextVisible(), "Top hero banner text is not visible after manual scroll up");
    }
}
