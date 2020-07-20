package com.utils;

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.basetest.TestBase;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;

public class Keywords extends TestBase {

	public String testCasesName = "";

	public void executeKeywords(String testcases, Hashtable<String, String> data) throws Exception {

		this.testCasesName = testcases;
		loadproperties();

		System.out.println("Executable test cases is :" + testcases);

		String keyword = null;
		String objectkeys = null;
		String datakeys = null;

		for (int rNum = 2; rNum <= xls.getRowCount("Test Steps"); rNum++) {
			if (testcases.equalsIgnoreCase(xls.getCellData("Test Steps", "TCID", rNum))) {
				keyword = xls.getCellData("Test Steps", "Keyword", rNum);
				objectkeys = xls.getCellData("Test Steps", "Object", rNum);
				datakeys = xls.getCellData("Test Steps", "Data", rNum);

				System.out.println(keyword + "--" + objectkeys + "--" + datakeys);

				String result = null;
				switch (keyword) {
				case "click":
					result = click(objectkeys);
					break;

				case "enter":
					if (data != null)
						result = enter(objectkeys, data.get(datakeys));
					else
						result = enter(objectkeys, datakeys);
					break;
					
				case "verifyElement":
					result =verifyElement(objectkeys);
					break;

				case "verifyText":
					if (data != null)
						result = verifyText(objectkeys, data.get(datakeys));
					else
						result = verifyText(objectkeys, datakeys);
					break;
				case "scroll_Down":
					result = scroll_Down(objectkeys);
					break;
				case "goback":
					result = goback();
					break;
				case "checkAndClick":
					result = checkAndClick(objectkeys);
					break;

				case "searchAndSelect":
					result = searchAndSelect(objectkeys, datakeys);
					break;
				case "selectDropdown":
					result = selectDropdown(objectkeys, datakeys);
					break;
				case "select_date":
					result = select_date(objectkeys,datakeys);
					break;
				
				default:
					System.out.println("Not Matching Keyword :" + keyword);
					break;
				}

				if (result != null) {
					if (result.equalsIgnoreCase("Pass"))
						xls.setCellData1("Test Steps", "Status", (rNum - 1), "Executed");
					else
						xls.setCellData1("Test Steps", "Status", (rNum - 1), "Not Executed");
				}

			}

		}

	}

	

	public String select_date(String object,String date) throws InterruptedException {
		
		 boolean bul = true;
		
		 while(bul!=false) {      
		    WebElement source = getWebElement(object.split("\\|")[0]);    
		    WebElement destination = getWebElement(object.split("\\|")[1]);    
		  
		    
		    TouchAction action = new TouchAction((PerformsTouchActions)driver);     
		    action.longPress(PointOption.point(source.getSize().height,source.getSize().width)).moveTo(PointOption.point(destination.getSize().getHeight(), destination.getSize().getWidth())).release().perform();   
		    Thread.sleep(500);
		    bul = driver.findElementsByXPath("//*[@content-desc='"+date.split("\\|")[0]+"']").isEmpty();
		 } 
		
		driver.findElement(By.xpath("//*[@content-desc='"+date.split("\\|")[0]+"']")).click();
		driver.findElement(By.xpath("//*[@content-desc='"+date.split("\\|")[1]+"']")).click();
		return "Pass";

	}
	private String selectDropdown(String objectkeys, String datakeys) {
		Select dropdown = new Select(getWebElement(objectkeys));
		dropdown.selectByValue(datakeys);
		return "Pass";
	}

	private String searchAndSelect(String objectkeys, String datakeys) {
		
		String searchbox = objectkeys.split("\\|")[0];
		String suggestionlist = objectkeys.split("\\|")[1];
		
		getWebElement(searchbox).sendKeys(datakeys);
		for(int i=0; i<getWebElements(suggestionlist).size();i++) {
			if(getWebElements(suggestionlist).get(i).getText().equalsIgnoreCase(datakeys)) {
				
				getWebElements(suggestionlist).get(i).click();
				break;
			}
		}
		return "Pass";
	}

	private String verifyText(String objectkeys, String string) {
		Assert.assertEquals(getWebElement(objectkeys).getText(), string);
		return "Pass";
	}

	private String enter(String objectkeys, String string) {
		getWebElement(objectkeys).sendKeys(string);
		return "Pass";
	}

	private String click(String objectkeys) {
		
		getWebElement(objectkeys).click();
		return "Pass";
	}
	private String goback() {
		driver.navigate().back();
		return "Pass";
	}

	private String checkAndClick(String objectkeys) {
		 
		if(getWebElements(objectkeys.split("\\|")[1]).size()==0)
			getWebElement(objectkeys.split("\\|")[0]).click();
		return "Pass";
	}

	private String verifyElement(String objectkeys) {
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOf(getWebElement(objectkeys)));
		return "Pass";
	}

	public String scroll_Down(String object) throws InterruptedException {
		
		 boolean bul = true;
		
		 while(bul!=false) {     
			 
		    TouchAction action = new TouchAction((PerformsTouchActions)driver);     
		    action.longPress(PointOption.point(300,350)).moveTo(PointOption.point(80,70)).release().perform();   
		    Thread.sleep(500);
		    bul = getWebElements(object).isEmpty();
		 } 
		
		getWebElement(object).click();
		 Thread.sleep(1000);
		return "Pass";

	}
}
