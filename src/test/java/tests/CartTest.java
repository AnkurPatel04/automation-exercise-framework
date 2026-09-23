package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AccountInformationPage;
import pages.CartPage;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.ProductsPage;
import pages.SignupLoginPage;

/**
 * Test suite verifying shopping cart addition, quantity calculation, item removal, and persistent cart sessions.
 */
public class CartTest extends BaseTest {

    private String generateUniqueEmail() {
        return "cart_qa_" + System.currentTimeMillis() + "@testmail.com";
    }

    /**
     * Verifies adding multiple distinct products to cart and validates individual price, quantity, and total math.
     */
    @Test(description = "Test Case 12: Add Products in Cart and verify prices, quantities, and totals")
    public void testAddProductsInCart_TC12() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        productsPage.addProductToCartByIndex(1);
        productsPage.clickContinueShopping();

        productsPage.addProductToCartByIndex(2);
        CartPage cartPage = productsPage.clickViewCartModal();

        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Expected 2 products in the cart");

        String price1 = cartPage.getItemPrice(1);
        String qty1 = cartPage.getItemQuantity(1);
        String total1 = cartPage.getItemTotal(1);

        String price2 = cartPage.getItemPrice(2);
        String qty2 = cartPage.getItemQuantity(2);
        String total2 = cartPage.getItemTotal(2);

        Assert.assertFalse(price1.isEmpty(), "Product 1 price is empty");
        Assert.assertEquals(qty1, "1", "Product 1 quantity is not 1");
        Assert.assertFalse(total1.isEmpty(), "Product 1 total is empty");

        Assert.assertFalse(price2.isEmpty(), "Product 2 price is empty");
        Assert.assertEquals(qty2, "1", "Product 2 quantity is not 1");
        Assert.assertFalse(total2.isEmpty(), "Product 2 total is empty");
    }

    /**
     * Verifies selecting a custom product quantity on product detail and confirms the exact quantity displays in the cart.
     */
    @Test(description = "Test Case 13: Verify Product quantity in Cart")
    public void testVerifyProductQuantityInCart_TC13() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        ProductDetailPage detailPage = productsPage.clickFirstViewProduct();
        Assert.assertTrue(detailPage.isProductInformationVisible(), "Product detail view is not visible");

        detailPage.setQuantity(4);
        detailPage.addToCart();
        CartPage cartPage = detailPage.clickViewCartModal();

        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page is not visible");
        Assert.assertEquals(cartPage.getItemQuantity(1), "4", "Cart product quantity does not match the configured 4 units");
    }

    /**
     * Verifies deleting a product row from the cart table using its remove button.
     */
    @Test(description = "Test Case 17: Remove Products From Cart")
    public void testRemoveProductsFromCart_TC17() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        productsPage.addProductToCartByIndex(1);
        CartPage cartPage = productsPage.clickViewCartModal();

        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page is not visible");
        int initialCount = cartPage.getCartItemCount();
        Assert.assertTrue(initialCount >= 1, "No items found in cart prior to removal");

        cartPage.removeItemByIndex(1);
        Assert.assertEquals(cartPage.getCartItemCount(), initialCount - 1, "Item count did not decrement after deletion");
    }

    /**
     * Verifies that items added to the cart by a guest user remain preserved after logging in.
     */
    @Test(description = "Test Case 20: Search Products and Verify Cart After Login")
    public void testSearchProductsAndVerifyCartAfterLogin_TC20() {
        String email = generateUniqueEmail();
        String name = "Cart Preserved User";
        String password = "Password123!";

        // Register initial test account
        HomePage homePage = new HomePage(driver);
        SignupLoginPage signupLoginPage = homePage.clickSignupLogin();
        AccountInformationPage accountPage = signupLoginPage.signup(name, email);
        accountPage.fillDefaultAccountDetails(password);
        accountPage.clickCreateAccount();
        homePage = accountPage.clickContinue();
        signupLoginPage = homePage.clickLogout();

        // Search product and add to cart as guest
        ProductsPage productsPage = homePage.clickProducts();
        productsPage.searchProduct("Tshirt");
        Assert.assertTrue(productsPage.isSearchedProductsHeadingVisible(), "'SEARCHED PRODUCTS' heading is not visible");

        productsPage.addProductToCartByIndex(1);
        CartPage cartPage = productsPage.clickViewCartModal();
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page is not visible");
        int cartCountBeforeLogin = cartPage.getCartItemCount();
        Assert.assertTrue(cartCountBeforeLogin >= 1, "Cart is empty before login");

        // Authenticate with previously registered account
        signupLoginPage = homePage.clickSignupLogin();
        homePage = signupLoginPage.login(email, password);
        Assert.assertTrue(homePage.isLoggedInAsVisible(), "'Logged in as username' header is not visible");

        // Re-visit cart and verify items persisted
        cartPage = homePage.clickCart();
        Assert.assertTrue(cartPage.getCartItemCount() >= cartCountBeforeLogin,
                "Cart items were not preserved after user login");

        // Cleanup: delete account
        accountPage = homePage.clickDeleteAccount();
        accountPage.clickDeleteContinue();
    }
}
