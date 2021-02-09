package TestCases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;


public class WebFormScript extends Util.testUtil {
String fullName;
String email;
String comments;
String zipCode;
    @BeforeClass
    public void setUp() throws IOException {
        initialize();
        int zipCodeInt = randomNumber(5);
        zipCode = Integer.toString(zipCodeInt);
        fullName = "TapanTesting_"+zipCode;
        email = fullName+"@gmail.com";
        comments = "1234567890";
    }
//Positive scenarios
    @Test(priority = 1)
    public void allfieldsFilled_FeedbackForm() throws Exception {

        gettingUrl_WaitForPageToLoad();
        feedbackForm(fullName,email,zipCode,comments);
        checkPoint("Give us your feedback");
        checkPoint(fullName + ", your feedback has been submitted. Thanks for contacting us!");

    }

//Negative scenarios  --> 1. all fields blank
    @Test(priority = 2, dependsOnMethods = {"allfieldsFilled_FeedbackForm"})
    public void allblank_FeedbackForm() throws Exception {
        gettingUrl_WaitForPageToLoad();
        feedbackForm("","","","");
        checkPoint("Some fields were left blank. Please complete the form and try again.");
        feedbackForm(fullName,"","","");
        checkPoint("Some fields were left blank. Please complete the form and try again.");
        clearForm();
        feedbackForm("",email,"","");
        checkPoint("Some fields were left blank. Please complete the form and try again.");
        clearForm();
        feedbackForm("","",zipCode,"");
        checkPoint("Some fields were left blank. Please complete the form and try again.");
        clearForm();
        feedbackForm("","","",comments);
        checkPoint("Some fields were left blank. Please complete the form and try again.");
        clearForm();
        feedbackForm(fullName,email,zipCode,"");
        checkPoint("Some fields were left blank. Please complete the form and try again.");
        clearForm();
        feedbackForm("",email,zipCode,comments);
        checkPoint("Some fields were left blank. Please complete the form and try again.");
        clearForm();
        feedbackForm(fullName,"",zipCode,comments);
        checkPoint("Some fields were left blank. Please complete the form and try again.");
        clearForm();
    }

//Negative scenarios  --> 2. fields validation
    @Test(priority = 2, dependsOnMethods = {"allfieldsFilled_FeedbackForm"})
    public void fieldsValidations() throws Exception {
        String wrongEmailwithspecialChar = "!@#!@##@$%%^&*()@gmail.com";
        String wrongEmail = "tapanabc.com";
        String wrongEmail1 = "tapan@.com";
        String wrongEmail2 = "tapan@123";
        String wrongEmail3 = "tapan@#4.com";
        String correctEmail = "tapan.18@gmail.in";
        gettingUrl_WaitForPageToLoad();
        feedbackForm(fullName,wrongEmailwithspecialChar,zipCode,comments);
        Thread.sleep(3000);
        clearForm();
        feedbackForm(fullName,wrongEmail,zipCode,comments);
        Thread.sleep(3000);
        clearForm();
        feedbackForm(fullName,wrongEmail1,zipCode,comments);
        Thread.sleep(3000);
        clearForm();
        //Its failing because it could be potentially a defect... It needs to have .com or .countrydomain in the end.
        feedbackForm(fullName,wrongEmail2,zipCode,comments);
        Thread.sleep(3000);
        clearForm();
        feedbackForm(fullName,wrongEmail3,zipCode,comments);
        clearForm();
        feedbackForm(fullName,correctEmail,zipCode,comments);
        checkPoint(fullName + ", your feedback has been submitted. Thanks for contacting us!");

        //Just like this other field verification can be done. Boundary value testing can be done on Name , email, ZipCode, Comments as well.


    }

    // Refreshing the page should not save any data
    @Test(priority = 2, dependsOnMethods = {"allfieldsFilled_FeedbackForm"})
    public void refreshpage() throws Exception {
        gettingUrl_WaitForPageToLoad();
        getObject("WebEdit_Name").sendKeys(fullName);
        getObject("WebEdit_Email").sendKeys(email);
        getObject("WebEdit_Zip_Code").sendKeys(zipCode);
        getObject("WebEdit_Comments").sendKeys(comments);
        driver.navigate().refresh();
        getObject("WebButton_Submit").click();
        checkPoint("Some fields were left blank. Please complete the form and try again.");


    }


@AfterClass
    public void tearDown(){
    quitBrowser();
}


}

