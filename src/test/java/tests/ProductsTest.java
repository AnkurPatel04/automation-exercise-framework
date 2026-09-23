package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.ProductsPage;

import java.util.List;

/**
 * Test suite verifying product catalog browsing, details validation, search queries, filters, reviews, and recommended items.
 */
public class ProductsTest extends BaseTest {

    /**
     * Verifies that the products catalog is displayed and product specifications are populated on detail view.
     */
    @Test(description = "Test Case 8: Verify All Products and product detail page")
    public void testVerifyProductsAndDetail_TC08() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        Assert.assertTrue(productsPage.isAllProductsVisible(), "'ALL PRODUCTS' page is not visible");
        Assert.assertTrue(productsPage.isProductListVisible(), "Product list is not visible");

        ProductDetailPage detailPage = productsPage.clickFirstViewProduct();
        Assert.assertTrue(detailPage.isProductInformationVisible(), "Product detail information container is not visible");
        Assert.assertFalse(detailPage.getProductName().isEmpty(), "Product name is empty");
        Assert.assertTrue(detailPage.getProductCategory().contains("Category"), "Category is not displayed properly");
        Assert.assertTrue(detailPage.getProductPrice().contains("Rs."), "Price is not displayed properly");
        Assert.assertTrue(detailPage.getProductAvailability().contains("Availability"), "Availability is not displayed");
        Assert.assertTrue(detailPage.getProductCondition().contains("Condition"), "Condition is not displayed");
        Assert.assertTrue(detailPage.getProductBrand().contains("Brand"), "Brand is not displayed");
    }

    /**
     * Verifies product search functionality and checks that search results correspond to the query keyword.
     */
    @Test(description = "Test Case 9: Search Product")
    public void testSearchProduct_TC09() {
        String searchKeyword = "Top";

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        Assert.assertTrue(productsPage.isAllProductsVisible(), "'ALL PRODUCTS' page is not visible");

        productsPage.searchProduct(searchKeyword);
        Assert.assertTrue(productsPage.isSearchedProductsHeadingVisible(), "'SEARCHED PRODUCTS' heading is not visible");

        List<String> productNames = productsPage.getDisplayedProductNames();
        Assert.assertFalse(productNames.isEmpty(), "No products found for search term: " + searchKeyword);
        for (String name : productNames) {
            Assert.assertTrue(name.toLowerCase().contains(searchKeyword.toLowerCase()),
                    "Product '" + name + "' does not match search keyword '" + searchKeyword + "'");
        }
    }

    /**
     * Verifies filtering catalog products by parent category and sub-category.
     */
    @Test(description = "Test Case 18: View Category Products")
    public void testViewCategoryProducts_TC18() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        productsPage.selectCategory("Women", "Dress");
        Assert.assertTrue(productsPage.getPageTitleText().toUpperCase().contains("WOMEN - DRESS PRODUCTS")
                        || productsPage.getPageTitleText().toUpperCase().contains("DRESS"),
                "Women - Dress category page title mismatch: " + productsPage.getPageTitleText());

        productsPage.selectCategory("Men", "Tshirts");
        Assert.assertTrue(productsPage.getPageTitleText().toUpperCase().contains("MEN - TSHIRTS PRODUCTS")
                        || productsPage.getPageTitleText().toUpperCase().contains("TSHIRTS"),
                "Men category subcategory page title mismatch: " + productsPage.getPageTitleText());
    }

    /**
     * Verifies browsing and filtering products by brand from the Brands sidebar.
     */
    @Test(description = "Test Case 19: View & Cart Brand Products")
    public void testViewBrandProducts_TC19() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        productsPage.selectBrand("Polo");
        Assert.assertTrue(productsPage.getPageTitleText().toUpperCase().contains("BRAND - POLO PRODUCTS")
                        || productsPage.getPageTitleText().toUpperCase().contains("POLO"),
                "Brand Polo page title mismatch: " + productsPage.getPageTitleText());
        Assert.assertTrue(productsPage.isProductListVisible(), "No products displayed for Polo brand");

        productsPage.selectBrand("Madame");
        Assert.assertTrue(productsPage.getPageTitleText().toUpperCase().contains("BRAND - MADAME PRODUCTS")
                        || productsPage.getPageTitleText().toUpperCase().contains("MADAME"),
                "Brand Madame page title mismatch: " + productsPage.getPageTitleText());
        Assert.assertTrue(productsPage.isProductListVisible(), "No products displayed for Madame brand");
    }

    /**
     * Verifies submitting a customer review on a product detail page and confirming the acknowledgment.
     */
    @Test(description = "Test Case 21: Add review on product")
    public void testAddReviewOnProduct_TC21() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        ProductsPage productsPage = homePage.clickProducts();
        ProductDetailPage detailPage = productsPage.clickFirstViewProduct();
        Assert.assertTrue(detailPage.isProductInformationVisible(), "Product detail page is not visible");

        detailPage.submitReview("QA Reviewer", "reviewer@testmail.com", "Excellent material and true to size! Highly recommended.");
        Assert.assertEquals(detailPage.getReviewSuccessMessage(), "Thank you for your review.", "Review success message mismatch");
    }

    /**
     * Verifies that adding an item from the home page Recommended Items carousel successfully populates the cart.
     */
    @Test(description = "Test Case 22: Add to cart from Recommended items")
    public void testAddToCartFromRecommendedItems_TC22() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        homePage.scrollToBottom();
        Assert.assertTrue(homePage.isRecommendedItemsVisible(), "'RECOMMENDED ITEMS' section is not visible");

        CartPage cartPage = homePage.addRecommendedItemToCartAndViewCart();
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page is not displayed");
        Assert.assertTrue(cartPage.getCartItemCount() >= 1, "Recommended product was not added to cart");
    }
}
