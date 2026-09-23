package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ContactUsPage;
import pages.HomePage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Test suite verifying customer inquiry submissions and file attachments via the Contact Us form.
 */
public class ContactUsTest extends BaseTest {

    /**
     * Verifies submitting the Contact Us form with file upload, alert confirmation, and returning to home.
     */
    @Test(description = "Test Case 6: Contact Us Form submission with file attachment")
    public void testContactUsForm_TC06() throws IOException {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");

        ContactUsPage contactUsPage = homePage.clickContactUs();
        Assert.assertTrue(contactUsPage.isGetInTouchVisible(), "'GET IN TOUCH' heading is not visible");

        File tempUploadFile = File.createTempFile("qa_sample_upload_", ".txt");
        tempUploadFile.deleteOnExit();
        Files.writeString(tempUploadFile.toPath(), "Sample automated test inquiry file upload content.");

        contactUsPage.fillContactForm("Quality Specialist", "qa_specialist@testmail.com",
                "Automated Framework Inquiry",
                "Hello, this is an automated inquiry test verifying file upload.",
                tempUploadFile.getAbsolutePath());

        contactUsPage.clickSubmit();
        contactUsPage.acceptConfirmationAlert();

        Assert.assertEquals(contactUsPage.getSuccessMessage(),
                "Success! Your details have been submitted successfully.",
                "Submission success message mismatch");

        homePage = contactUsPage.clickHomeButton();
        Assert.assertTrue(homePage.isHomePageVisible(), "Landed page is not home page after clicking Home");
    }
}
