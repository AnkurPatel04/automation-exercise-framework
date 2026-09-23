package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AccountInformationPage;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.PaymentPage;
import pages.ProductsPage;
import pages.SignupLoginPage;

import java.util.List;

/**
 * Test suite verifying complete end-to-end checkout flows, payment processing, address verification, and invoice downloads.
 */
public class CheckoutTest extends BaseTest {

    private String generateUniqueEmail() {
        return "checkout_qa_" + System.currentTimeMillis() + "@testmail.com";
    }

    /**
     * Verifies end-to-end checkout by adding products as a guest, registering mid-checkout, and completing order placement.
     */
    @Test(description = "Test Case 14: Place Order: Register while Checkout")
    public void testPlaceOrderRegisterWhileCheckout_TC14() {
        String email = generateUniqueEmail();
        String name = "Mid Checkout User";
        String password = "Password123!";

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        productsPage.addProductToCartByIndex(1);
        CartPage cartPage = productsPage.clickViewCartModal();
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page is not visible");

        cartPage.proceedToCheckoutExpectingLoginModal();
        SignupLoginPage signupLoginPage = cartPage.clickRegisterLoginFromModal();
        AccountInformationPage accountPage = signupLoginPage.signup(name, email);
        accountPage.fillDefaultAccountDetails(password);
        accountPage.clickCreateAccount();
        homePage = accountPage.clickContinue();

        Assert.assertTrue(homePage.isLoggedInAsVisible(), "'Logged in as username' header is not visible");

        cartPage = homePage.clickCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutPage.isAddressDetailsVisible(), "Address details are not visible on checkout page");

        checkoutPage.enterOrderComment("Mid-checkout automated test order comment.");
        PaymentPage paymentPage = checkoutPage.clickPlaceOrder();

        paymentPage.fillPaymentDetails("Mid Checkout User", "4111111111111111", "321", "12", "2028");
        paymentPage.clickPayAndConfirmOrder();

        Assert.assertTrue(paymentPage.isOrderSuccessMessageVisible(), "Order success message is not displayed");

        accountPage = homePage.clickDeleteAccount();
        Assert.assertTrue(accountPage.isAccountDeletedVisible(), "'ACCOUNT DELETED!' banner is not visible");
        accountPage.clickDeleteContinue();
    }

    /**
     * Verifies end-to-end checkout by registering an account before adding products to cart, and placing the order.
     */
    @Test(description = "Test Case 15: Place Order: Register before Checkout")
    public void testPlaceOrderRegisterBeforeCheckout_TC15() {
        String email = generateUniqueEmail();
        String name = "Pre Reg User";
        String password = "Password123!";

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        SignupLoginPage signupLoginPage = homePage.clickSignupLogin();
        AccountInformationPage accountPage = signupLoginPage.signup(name, email);
        accountPage.fillDefaultAccountDetails(password);
        accountPage.clickCreateAccount();
        homePage = accountPage.clickContinue();

        Assert.assertTrue(homePage.isLoggedInAsVisible(), "'Logged in as username' header is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        productsPage.addProductToCartByIndex(1);
        CartPage cartPage = productsPage.clickViewCartModal();
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page is not visible");

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutPage.isAddressDetailsVisible(), "Address details are not visible on checkout page");

        checkoutPage.enterOrderComment("Register before checkout order comment.");
        PaymentPage paymentPage = checkoutPage.clickPlaceOrder();

        paymentPage.fillPaymentDetails("Pre Reg User", "4111111111111111", "321", "08", "2029");
        paymentPage.clickPayAndConfirmOrder();

        Assert.assertTrue(paymentPage.isOrderSuccessMessageVisible(), "Order success message is not displayed");

        accountPage = homePage.clickDeleteAccount();
        Assert.assertTrue(accountPage.isAccountDeletedVisible(), "'ACCOUNT DELETED!' banner is not visible");
        accountPage.clickDeleteContinue();
    }

    /**
     * Verifies end-to-end checkout by authenticating an existing user before shopping, and placing the order.
     */
    @Test(description = "Test Case 16: Place Order: Login before Checkout")
    public void testPlaceOrderLoginBeforeCheckout_TC16() {
        String email = generateUniqueEmail();
        String name = "Pre Login User";
        String password = "Password123!";

        // Register initial account then log out
        HomePage homePage = new HomePage(driver);
        SignupLoginPage signupLoginPage = homePage.clickSignupLogin();
        AccountInformationPage accountPage = signupLoginPage.signup(name, email);
        accountPage.fillDefaultAccountDetails(password);
        accountPage.clickCreateAccount();
        homePage = accountPage.clickContinue();
        signupLoginPage = homePage.clickLogout();

        // Perform login before checkout
        homePage = signupLoginPage.login(email, password);
        Assert.assertTrue(homePage.isLoggedInAsVisible(), "'Logged in as username' header is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        productsPage.addProductToCartByIndex(1);
        CartPage cartPage = productsPage.clickViewCartModal();
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page is not visible");

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutPage.isAddressDetailsVisible(), "Address details are not visible on checkout page");

        checkoutPage.enterOrderComment("Login before checkout order comment.");
        PaymentPage paymentPage = checkoutPage.clickPlaceOrder();

        paymentPage.fillPaymentDetails("Pre Login User", "4111111111111111", "321", "10", "2027");
        paymentPage.clickPayAndConfirmOrder();

        Assert.assertTrue(paymentPage.isOrderSuccessMessageVisible(), "Order success message is not displayed");

        accountPage = homePage.clickDeleteAccount();
        Assert.assertTrue(accountPage.isAccountDeletedVisible(), "'ACCOUNT DELETED!' banner is not visible");
        accountPage.clickDeleteContinue();
    }

    /**
     * Verifies that the delivery and billing addresses displayed at checkout strictly match registration entries.
     */
    @Test(description = "Test Case 23: Verify address details in checkout page")
    public void testVerifyAddressDetailsInCheckout_TC23() {
        String email = generateUniqueEmail();
        String name = "Address Verification User";
        String password = "Password123!";
        String addressLine1 = "789 Automation Way";
        String city = "San Francisco";
        String state = "California";
        String zipcode = "94105";

        HomePage homePage = new HomePage(driver);
        SignupLoginPage signupLoginPage = homePage.clickSignupLogin();
        AccountInformationPage accountPage = signupLoginPage.signup(name, email);
        accountPage.fillFullAccountDetails(password, "10", "May", "1992", "Address", "User",
                "QA Org", addressLine1, "Apt 2B", "United States", state, city, zipcode, "+14155551234");
        accountPage.clickCreateAccount();
        homePage = accountPage.clickContinue();

        ProductsPage productsPage = homePage.clickProducts();
        productsPage.addProductToCartByIndex(1);
        CartPage cartPage = productsPage.clickViewCartModal();

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutPage.isAddressDetailsVisible(), "Address details are not visible");

        List<String> deliveryLines = checkoutPage.getDeliveryAddressLines();
        List<String> billingLines = checkoutPage.getBillingAddressLines();

        String fullDeliveryText = String.join(" ", deliveryLines);
        String fullBillingText = String.join(" ", billingLines);

        Assert.assertTrue(fullDeliveryText.contains(addressLine1), "Delivery address does not contain registered address line 1");
        Assert.assertTrue(fullDeliveryText.contains(city), "Delivery address does not contain registered city");
        Assert.assertTrue(fullBillingText.contains(addressLine1), "Billing address does not contain registered address line 1");
        Assert.assertTrue(fullBillingText.contains(city), "Billing address does not contain registered city");

        accountPage = homePage.clickDeleteAccount();
        accountPage.clickDeleteContinue();
    }

    /**
     * Verifies completing order placement, downloading the invoice document, and confirming invoice availability.
     */
    @Test(description = "Test Case 24: Download Invoice after purchase order")
    public void testDownloadInvoiceAfterPurchase_TC24() {
        String email = generateUniqueEmail();
        String name = "Invoice Download User";
        String password = "Password123!";

        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = homePage.clickProducts();
        productsPage.addProductToCartByIndex(1);
        CartPage cartPage = productsPage.clickViewCartModal();

        cartPage.proceedToCheckoutExpectingLoginModal();
        SignupLoginPage signupLoginPage = cartPage.clickRegisterLoginFromModal();
        AccountInformationPage accountPage = signupLoginPage.signup(name, email);
        accountPage.fillDefaultAccountDetails(password);
        accountPage.clickCreateAccount();
        homePage = accountPage.clickContinue();

        cartPage = homePage.clickCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        PaymentPage paymentPage = checkoutPage.clickPlaceOrder();
        paymentPage.fillPaymentDetails("Invoice User", "4111111111111111", "321", "05", "2030");
        paymentPage.clickPayAndConfirmOrder();

        Assert.assertTrue(paymentPage.isOrderSuccessMessageVisible(), "Order success message is not displayed");
        Assert.assertTrue(paymentPage.isDownloadInvoiceButtonVisible(), "Download invoice button is not visible");

        paymentPage.clickDownloadInvoice();

        accountPage = homePage.clickDeleteAccount();
        accountPage.clickDeleteContinue();
    }
}
