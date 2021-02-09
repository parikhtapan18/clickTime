package Util;

import org.openqa.selenium.By;

import java.awt.*;
import java.io.IOException;

public class testUtil extends Base.testBase {
	
	public static void getUrl() {
		driver.get(CONFIG.getProperty("url"));
	}
	
	public static void feedbackForm(String name, String email, String zipcode, String comments) throws Exception {
		getObject("WebEdit_Name").sendKeys(name);
		getObject("WebEdit_Email").sendKeys(email);
		getObject("WebEdit_Zip_Code").sendKeys(zipcode);
		getObject("WebEdit_Comments").sendKeys(comments);
		getObject("WebButton_Submit").click();
	  
	}

	public static void gettingUrl_WaitForPageToLoad() throws IOException, AWTException {
		getUrl();
		waitfn("WebElement_text_Give_us_your_feedback");
		checkPoint("Give us your feedback");
		checkPoint("all fields are required");
	}

	public static void clearForm() throws Exception {
		getObject("WebEdit_Name").clear();
		getObject("WebEdit_Email").clear();
		getObject("WebEdit_Zip_Code").clear();
		getObject("WebEdit_Comments").clear();
	}
	
	public static void quitBrowser() {
	  driver.quit();
	}

}
