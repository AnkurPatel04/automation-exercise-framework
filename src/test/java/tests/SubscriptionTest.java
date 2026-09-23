package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;

/**
 * Test suite verifying newsletter subscription form functionality on home and cart page footers.
 */
public class SubscriptionTest extends BaseTest {

    /**
     * Verifies submitting a valid email into the home page footer subscription input displays the success notice.
     */
    @Test(description = "Test Case 10: Verify Subscription in home page")
    public void testVerifySubscriptionHomePage_TC10() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        homePage.scrollToBottom();
        Assert.assertTrue(homePage.isSubscriptionHeaderVisible(), "'SUBSCRIPTION' text is not visible in home footer");

        homePage.subscribeNewsletter("sub_home_" + System.currentTimeMillis() + "@testmail.com");
        Assert.assertEquals(homePage.getSubscriptionSuccessMessage(),
                "You have been successfully subscribed!",
                "Subscription success alert message mismatch");
    }

    /**
     * Verifies submitting a valid email into the cart page footer subscription input displays the success notice.
     */
    @Test(description = "Test Case 11: Verify Subscription in Cart page")
    public void testVerifySubscriptionCartPage_TC11() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        CartPage cartPage = homePage.clickCart();
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page is not visible");

        homePage.scrollToBottom();
        Assert.assertTrue(cartPage.isSubscriptionHeaderVisible(), "'SUBSCRIPTION' text is not visible in cart footer");

        cartPage.subscribeNewsletter("sub_cart_" + System.currentTimeMillis() + "@testmail.com");
        Assert.assertEquals(cartPage.getSubscriptionSuccessMessage(),
                "You have been successfully subscribed!",
                "Subscription success alert message mismatch");
    }
}
