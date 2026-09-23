package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AccountInformationPage;
import pages.HomePage;
import pages.SignupLoginPage;

/**
 * Test suite verifying user registration, valid/invalid logins, logout, and duplicate email validations.
 */
public class AccountTest extends BaseTest {

    private String generateUniqueEmail() {
        return "qa_user_" + System.currentTimeMillis() + "@testmail.com";
    }

    /**
     * Verifies successful user account registration, profile completion, session validation, and account deletion.
     */
    @Test(description = "Test Case 1: Register User and delete account")
    public void testRegisterUser_TC01() {
        String email = generateUniqueEmail();
        String name = "Test User";

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        SignupLoginPage signupLoginPage = homePage.clickSignupLogin();
        Assert.assertTrue(signupLoginPage.isSignupHeaderVisible(), "'New User Signup!' header is not visible");

        AccountInformationPage accountPage = signupLoginPage.signup(name, email);
        Assert.assertTrue(accountPage.isEnterAccountInformationVisible(), "'ENTER ACCOUNT INFORMATION' header is not visible");

        accountPage.fillDefaultAccountDetails("P@ssword123!");
        accountPage.clickCreateAccount();
        Assert.assertTrue(accountPage.isAccountCreatedVisible(), "'ACCOUNT CREATED!' banner is not visible");

        homePage = accountPage.clickContinue();
        Assert.assertTrue(homePage.isLoggedInAsVisible(), "'Logged in as username' header is not visible");
        Assert.assertTrue(homePage.getLoggedInUserText().contains(name), "Logged-in user name does not match expected");

        accountPage = homePage.clickDeleteAccount();
        Assert.assertTrue(accountPage.isAccountDeletedVisible(), "'ACCOUNT DELETED!' banner is not visible");
        accountPage.clickDeleteContinue();
    }

    /**
     * Verifies successful user login with valid credentials followed by account deletion cleanup.
     */
    @Test(description = "Test Case 2: Login User with correct email and password")
    public void testLoginUserCorrect_TC02() {
        String email = generateUniqueEmail();
        String name = "Valid User";
        String password = "SecurePassword123!";

        // Precondition: register a fresh account
        HomePage homePage = new HomePage(driver);
        SignupLoginPage signupLoginPage = homePage.clickSignupLogin();
        AccountInformationPage accountPage = signupLoginPage.signup(name, email);
        accountPage.fillDefaultAccountDetails(password);
        accountPage.clickCreateAccount();
        homePage = accountPage.clickContinue();
        signupLoginPage = homePage.clickLogout();

        // Perform login verification
        Assert.assertTrue(signupLoginPage.isLoginHeaderVisible(), "'Login to your account' header is not visible");
        homePage = signupLoginPage.login(email, password);
        Assert.assertTrue(homePage.isLoggedInAsVisible(), "'Logged in as username' header is not visible");
        Assert.assertTrue(homePage.getLoggedInUserText().contains(name), "Logged in text does not contain user name");

        // Teardown: delete account
        accountPage = homePage.clickDeleteAccount();
        Assert.assertTrue(accountPage.isAccountDeletedVisible(), "'ACCOUNT DELETED!' banner is not visible");
        accountPage.clickDeleteContinue();
    }

    /**
     * Verifies that attempting login with incorrect credentials displays the expected error message.
     */
    @Test(description = "Test Case 3: Login User with incorrect email and password")
    public void testLoginUserIncorrect_TC03() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        SignupLoginPage signupLoginPage = homePage.clickSignupLogin();
        Assert.assertTrue(signupLoginPage.isLoginHeaderVisible(), "'Login to your account' header is not visible");

        signupLoginPage.loginExpectingFailure("nonexistent_qa_" + System.currentTimeMillis() + "@test.com", "WrongPassword!");
        Assert.assertEquals(signupLoginPage.getLoginErrorMessage(), "Your email or password is incorrect!", "Error message mismatch");
    }

    /**
     * Verifies that a logged-in user can log out successfully and is redirected to the login page.
     */
    @Test(description = "Test Case 4: Logout User")
    public void testLogoutUser_TC04() {
        String email = generateUniqueEmail();
        String name = "Logout User";
        String password = "Password123!";

        // Precondition: register user
        HomePage homePage = new HomePage(driver);
        SignupLoginPage signupLoginPage = homePage.clickSignupLogin();
        AccountInformationPage accountPage = signupLoginPage.signup(name, email);
        accountPage.fillDefaultAccountDetails(password);
        accountPage.clickCreateAccount();
        homePage = accountPage.clickContinue();
        Assert.assertTrue(homePage.isLoggedInAsVisible(), "'Logged in as username' header is not visible");

        // Perform logout
        signupLoginPage = homePage.clickLogout();
        Assert.assertTrue(signupLoginPage.isLoginHeaderVisible(), "User was not redirected to the login page after logout");

        // Cleanup: login back to delete account
        homePage = signupLoginPage.login(email, password);
        accountPage = homePage.clickDeleteAccount();
        accountPage.clickDeleteContinue();
    }

    /**
     * Verifies that attempting registration with an existing email displays an error message.
     */
    @Test(description = "Test Case 5: Register User with existing email")
    public void testRegisterUserWithExistingEmail_TC05() {
        String email = generateUniqueEmail();
        String name = "Existing User";
        String password = "Password123!";

        // Register initial user
        HomePage homePage = new HomePage(driver);
        SignupLoginPage signupLoginPage = homePage.clickSignupLogin();
        AccountInformationPage accountPage = signupLoginPage.signup(name, email);
        accountPage.fillDefaultAccountDetails(password);
        accountPage.clickCreateAccount();
        homePage = accountPage.clickContinue();
        signupLoginPage = homePage.clickLogout();

        // Attempt registering with duplicate email
        Assert.assertTrue(signupLoginPage.isSignupHeaderVisible(), "'New User Signup!' header is not visible");
        signupLoginPage.signupExpectingFailure("Duplicate Name", email);
        Assert.assertEquals(signupLoginPage.getSignupErrorMessage(), "Email Address already exist!", "Duplicate email error mismatch");

        // Cleanup: login and delete the created user
        homePage = signupLoginPage.login(email, password);
        accountPage = homePage.clickDeleteAccount();
        accountPage.clickDeleteContinue();
    }
}
