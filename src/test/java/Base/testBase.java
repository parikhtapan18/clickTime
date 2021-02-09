

/*
 * Author : Tapan P Parikh 
 * Created: 08/20/2015
 * Updated: 10/16/2015
 * @Copy rights - :D
 * Name: Selenium Test Base class
 * 
 */
package Base;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



public class testBase {

	public static Properties CONFIG = null;
	public static Properties OR = null;
	public static Properties VALUES = null;
	public static WebDriver dr = null;
	public static EventFiringWebDriver driver = null;

	public void initialize() throws IOException {
		System.out.println(System.getProperty("sun.arch.data.model"));
		if (driver == null) {
			CONFIG = new Properties();
			FileInputStream fn = new FileInputStream(System.getProperty("user.dir") + "//src//test//java//config//config.properties");
			CONFIG.load(fn);
			OR = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "//src//test//java//config//object_repository.properties");
			OR.load(ip);
			VALUES = new Properties();
			FileInputStream vl = new FileInputStream(System.getProperty("user.dir") + "//src//test//java//config//values.properties");
			VALUES.load(vl);

			if (CONFIG.getProperty("browser").equals("Chrome")) {

				ChromeOptions options = new ChromeOptions();
				options.addArguments("disable-infobars");
				options.addArguments("test-type");
				options.addArguments("start-maximized"); // open Browser in maximized mode
//        	   options.addArguments("--disable-extensions"); // disabling extensions
//        	   options.addArguments("--disable-gpu"); // applicable to windows os only
//        	   options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
//        	   options.addArguments("--no-sandbox"); // Bypass OS security model

//              dr = new ChromeDriver(options);
				dr = new ChromeDriver(options);
			} else if (CONFIG.getProperty("browser").equals("Firefox")) {
				File pathToBinary = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
				FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
				FirefoxProfile firefoxProfile = new FirefoxProfile();
				WebDriver driver = new FirefoxDriver();
				//dr = new FirefoxDriver();
			} else if (CONFIG.getProperty("browser").equals("IE")) {
				File file = new File("Browsers//IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
				//Handles the IE security settings error when all zones don't have the same setting
				DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
				caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				dr = new InternetExplorerDriver(caps);
			} else {
				System.out.println("something wrong typed!!");
			}

			//load the excel file here:
			System.out.println("Inside Base method");
			driver = new EventFiringWebDriver(dr);
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		}
	}

	public static String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	public static String screenshot(String filename) throws IOException, HeadlessException, AWTException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		filename = filename + dateTime(" MMddhhmmss") + ".JPEG";
		FileUtils.copyFileToDirectory(scrFile, new File(CONFIG.getProperty("screenshots") + filename));
		return filename;
	}

	public static void waitfn(String element) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(element))));
	}

	public static void waitfncss(String element) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(OR.getProperty(element))));
	}

	public static WebElement getObject(String xpath) throws Exception {
		try {
			return driver.findElement(By.xpath(OR.getProperty(xpath)));
		} catch (Throwable t) {
			screenshot(xpath);
			Assert.assertTrue(xpath + " is not present..see the screenshot", false);
			return null;
		}
	}





	public static boolean isObjectPresent(String xpath) throws Exception {
		try {
			driver.findElement(By.xpath(OR.getProperty(xpath)));
			return true;
		} catch (Throwable t) {
			screenshot(xpath);
			Assert.assertTrue(xpath + " is not present as expected", true);
			return false;
		}
	}

	public static Boolean checkPoint(String text) throws IOException, HeadlessException, AWTException {
		if (driver.getPageSource().contains(text)) {
			Assert.assertTrue(text + " is  present", true);
			return true;
		} else {
			System.out.println(text + " is not present on the page, check the screenshot");
			screenshot(text);
			Assert.assertTrue(text + " is not present", false);
			return false;

		}
	}


	//example use: dateTime("MMddhhmmss");
	public static String dateTime(String dateTimeFormat) {
		SimpleDateFormat ft = new SimpleDateFormat(dateTimeFormat);
		Calendar cal = Calendar.getInstance();
		System.out.println(ft.format(cal.getTime()));
		return ft.format(cal.getTime());


	}

	public static int randomNumber(int digitrange) {
		String minRange;
		String maxRange;
		int l;
		int h, diff;
		if (digitrange == 0) {
			minRange = "0";
			maxRange = "0";

		} else {
			minRange = "1";
			maxRange = "9";
			for (int i = 0; i < digitrange - 1; i++) {
				minRange = minRange + "0";
				maxRange = maxRange + "9";
			}

			//System.out.println(minRange);
			//System.out.println(maxRange);
			l = Integer.parseInt(minRange);
			h = Integer.parseInt(maxRange);
			diff = h - l;
			int n = (int) (Math.round(Math.random() * diff) + l);
			return n;
		}
		return digitrange;
	}

	public static String reverseString(String straightstring) {
		StringBuilder string1 = new StringBuilder();
		StringBuilder reversedString;
		reversedString = string1.append(straightstring).reverse();
		String finalreversedString = reversedString.toString();
		return finalreversedString;

	}

	// in format of mm-dd-yyyy

	public static boolean verifyText(WebElement object, String expectedValue) {
		if (object.getText().equalsIgnoreCase(expectedValue)) {
			return true;
		}
		return false;
	}

	public static void getObjectwithJavaScript(String element) {
		System.out.println("");
		driver.get("javascript:document.getElementById(" + OR.getProperty(element) + ").click()");
	}

	public static boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		}   // try
		catch (NoAlertPresentException Ex) {
			return false;
		}   // catch
	}   // isAlertPresent()

	public static void alertokcancel(String yesNo) throws InterruptedException {
		Alert alert = driver.switchTo().alert();
		Thread.sleep(2000);
		if (yesNo.equalsIgnoreCase("Y")) {
			alert.accept();
		} else {
			alert.dismiss();
		}
	}

}



	
