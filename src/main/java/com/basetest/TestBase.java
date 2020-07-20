package com.basetest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.utils.Xls_Reader;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class TestBase {

	public static AppiumDriver driver;
	AppiumDriverLocalService appiumservice;
	static String appiumServiceUrl;
	public Properties prop = null;

	public FileInputStream filein = null;

	public Xls_Reader xls = new Xls_Reader(
			System.getProperty("user.dir") + "\\src\\main\\java\\com\\testdata\\TestSuite1.xlsx");
	public static Logger logger = Logger.getLogger(TestBase.class);
	
	
	@BeforeClass
	public void lunchingApp() throws IOException {
		//to_Be_Start_Android_Device();
		appiumStarter();
		
		File appDir = new File(System.getProperty("user.dir")+"\\AppFiles\\");
		File app = new File(appDir, "Accommodation_v23.1_apkpure.apk");
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "Nexus6");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("noReset", false);
		capabilities.setCapability("automationName", "uiautomator1");
		capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		//driver = new AndroidDriver(new URL(appiumServiceUrl), capabilities);
		
		//"http://127.0.0.1:4723/wd/hub"
		
		try {
			System.out.println("APPIUM SERVICE URL IS : - "+appiumServiceUrl);
			driver = new AndroidDriver(new URL(appiumServiceUrl), capabilities);
			//driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
		driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
		
		
	}

	

	private void appiumStarter() {
		
		TestBase tb = new TestBase();
		PropertyConfigurator.configure("log4j.properties");
		appiumservice = new AppiumServiceBuilder().usingAnyFreePort()
                .withArgument( GeneralServerFlag.LOG_LEVEL, "info" )
                .withArgument( GeneralServerFlag.RELAXED_SECURITY )
                .withArgument( GeneralServerFlag.SESSION_OVERRIDE )
                .build();
		if(tb.checkIfServerIsRunnning(appiumservice.getUrl().getPort())) {
			appiumservice.stop(); 
		}
		else {
			appiumservice.start();
			appiumServiceUrl = appiumservice.getUrl().toString();
		}
		
		//System.out.println("Appium Service Address : - " + appiumServiceUrl);
		
	}
	public boolean checkIfServerIsRunnning(int port) {

		boolean isServerRunning = false;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.close();
		} catch (IOException e) {
			// If control comes here, then it means that the port is in use
			isServerRunning = true;
		} finally {
			serverSocket = null;
		}
		return isServerRunning;
	}
	
	@AfterClass
	public void closeApp() {
		log();
		((AppiumDriver)driver).resetApp();
		appiumservice.stop();
	}
	
	public void loadproperties() throws IOException {

		try {
			prop = new Properties();
			filein = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\main\\java\\com\\pompages\\searchlistingpage.properties");
			prop.load(filein);
			filein = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\main\\java\\com\\config\\config.properties");
			prop.load(filein);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public  WebElement getLocator(String locator) throws Exception {
		String[] split = locator.split(" ");
		String locatorType = split[0];
		String locatorValue = split[1];
		if (locatorType.toLowerCase().equals("id"))
			return driver.findElement(By.id(locatorValue));
		else if (locatorType.toLowerCase().equals("name"))
			return driver.findElement(By.name(locatorValue));
		else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
			return driver.findElement(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
			return driver.findElement(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
			return driver.findElement(By.linkText(locatorValue));
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return driver.findElement(By.partialLinkText(locatorValue));
		else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
			return driver.findElement(By.cssSelector(locatorValue));
		else if (locatorType.toLowerCase().equals("xpath"))
			return driver.findElement(By.xpath(locatorValue));
		else
			throw new Exception("Unknown locator type '" + locatorType + "'");
	}

	@SuppressWarnings("unchecked")
	public  List<MobileElement> getLocators(String locator) throws Exception {
		String[] split = locator.split(" ");
		String locatorType = split[0];
		String locatorValue = split[1];

		if (locatorType.toLowerCase().equals("id"))
			return driver.findElements(By.id(locatorValue));
		else if (locatorType.toLowerCase().equals("name"))
			return driver.findElements(By.name(locatorValue));
		else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
			return driver.findElements(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
			return driver.findElements(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
			return driver.findElements(By.linkText(locatorValue));
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return driver.findElements(By.partialLinkText(locatorValue));
		else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
			return driver.findElements(By.cssSelector(locatorValue));
		else if (locatorType.toLowerCase().equals("xpath"))
			return driver.findElements(By.xpath(locatorValue));
		else
			throw new Exception("Unknown locator type '" + locatorType + "'");
	}

	public WebElement getWebElement(String locator)  {
		try {
			return getLocator(prop.getProperty(locator));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public List<MobileElement> getWebElements(String locators)  {
		try {
			return getLocators(prop.getProperty(locators));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void log() {
		
		LogEntries entireLogBuffer = driver.manage().logs().get("server");
		Iterator<LogEntry> logIter = entireLogBuffer.iterator();
		while(logIter.hasNext()) {
		    LogEntry entry = logIter.next();
		    System.out.println(entry.getMessage());
		    logger.info(entry.getMessage());
		}
		
		
	}
	private void to_Be_Start_Android_Device() {
		
		try {
			Runtime.getRuntime().exec("emulator @Nexus6");
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
