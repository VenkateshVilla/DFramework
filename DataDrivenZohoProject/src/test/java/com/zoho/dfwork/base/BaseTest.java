package com.zoho.dfwork.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.asserts.SoftAssert;

import com.zoho.dfwork.util.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import junit.framework.Assert;

public class BaseTest {
	
	public WebDriver driver = null;
	public Properties prop = null;
	public ExtentReports eReport = ExtentManager.getInstance();
	public ExtentTest eTest;
	public SoftAssert soft = new SoftAssert();
	
	boolean gridRun = false;
	
	  //Initialization
	  public void initialise(){
		  
		  if(prop==null){
				
				prop = new Properties();
				
				File projectConfigFile = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\projectconfig.properties");
				
				FileInputStream fis = null;
				
				try {
					
					 fis = new FileInputStream(projectConfigFile);
					 prop.load(fis);
					
				} catch (Exception e) {
			
					e.printStackTrace();
					
				}
				
			}
		  
	  }
	
	   //Selenium Operations
	
		public void openBrowser(String browserType){
			
			eTest.log(LogStatus.INFO, "Opening Browser "+browserType);
			
			if(!gridRun){
			
				//prop.getProperty(browserType).equalsIgnoreCase("firefox")
				if(browserType.equalsIgnoreCase("firefox")){
	
					driver = new FirefoxDriver();
	
				}else if(browserType.equalsIgnoreCase("chrome")){
					
					String chromeDriverPath = System.getProperty("user.dir")+prop.getProperty("chromeDriverPath");
					System.setProperty("webdriver.chrome.driver", chromeDriverPath);
					driver = new ChromeDriver();
					
				}else if(browserType.equalsIgnoreCase("ie")){
					
					String ieDriverPath = System.getProperty("user.dir")+prop.getProperty("ieDriverPath");
					System.setProperty("webdriver.ie.driver", ieDriverPath);
					driver = new InternetExplorerDriver();
				}
			}else {
				
				DesiredCapabilities cap = null;
				
				if(browserType.equalsIgnoreCase("firefox")){
					
					cap = DesiredCapabilities.firefox();
					
					cap.setBrowserName("firefox");
					cap.setJavascriptEnabled(true);
					cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				
				}else if(browserType.equalsIgnoreCase("chrome")){
					
					cap = DesiredCapabilities.chrome();
					cap.setBrowserName("chrome");
					cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
					
				}else if(browserType.equalsIgnoreCase("ie")){
					
					cap = DesiredCapabilities.internetExplorer();
					cap.setBrowserName("ie");
					cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
					
				}
			
				try {
					
					driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);
				
				} catch (Exception e) {
					
					e.printStackTrace();
				}
	
				
			}
				
			eTest.log(LogStatus.INFO, "Browser opened Successfully "+browserType);
			
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			
		}
		
		public void navigate(String urlKey){
			eTest.log(LogStatus.INFO, "Navigating to "+prop.getProperty(urlKey));
			driver.get(prop.getProperty(urlKey));			
			
		}
		
		public void type(String locatorKey,String data){
			eTest.log(LogStatus.INFO, "Typing into "+locatorKey+" with data "+data);
			getElement(locatorKey).sendKeys(data);
			eTest.log(LogStatus.INFO, "Successfully Typed into "+locatorKey);
		}
		
		public void click(String locatorKey){
			eTest.log(LogStatus.INFO, "Clicking on "+locatorKey);
			getElement(locatorKey).click();
			eTest.log(LogStatus.INFO, "Successfully clicked on "+locatorKey);
		}
		
		public void clickAndWait(String locatorKey,String locatorPresent){
			eTest.log(LogStatus.INFO, "Clicking on "+locatorKey);
			int count = 5;
			for(int i=0;i<count;i++){
				
				getElement(locatorKey).click();
				wait(2);
				
				if(isElementPresent(locatorPresent)){
					eTest.log(LogStatus.INFO, "Successfully clicked on "+locatorKey);
					break;
					
				}
				
			}
			
		}
		
		//finding element on the page
		public WebElement getElement(String locatorKey){
			
			WebElement element = null;
			
			try{
			
				if(locatorKey.endsWith("_id")){
					
					element = driver.findElement(By.id(prop.getProperty(locatorKey)));
					
				}else if(locatorKey.endsWith("_name")){
					
					element = driver.findElement(By.name(prop.getProperty(locatorKey)));
					
				}else if(locatorKey.endsWith("_classname")){
					
					element = driver.findElement(By.className(prop.getProperty(locatorKey)));
					
				}else if(locatorKey.endsWith("_linktext")){
					
					element = driver.findElement(By.linkText(prop.getProperty(locatorKey)));
					
				}else if(locatorKey.endsWith("_cssselector")){
					
					element = driver.findElement(By.cssSelector(prop.getProperty(locatorKey)));
					
				}else if(locatorKey.endsWith("_xpath")){
					
					element = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
					
				}else{
					
					reportFail("The provided Locator - "+locatorKey+" is not found");
					Assert.fail("The provided Locator - "+locatorKey+" is not found");
				}
			
			}catch(Exception e){
				
				reportFail(e.getMessage());
				e.printStackTrace();
				Assert.fail(e.getMessage());
				
			}
			
			return element;
			
		}
		
		
	//Verification Operations
	
		public boolean verifyTitle(String title){
			
			String actualTitle = driver.getTitle().trim();
			
			String expectedTitle = prop.getProperty(title);
			
			if(actualTitle.equals(expectedTitle))
				return true;
			else
				return false;
		}
		
		public boolean verifyText(String locatorKey, String text){
			
			String actualText = getElement(locatorKey).getText().trim();
			
			String expectedText = prop.getProperty(text);
			
			if(actualText.equals(expectedText))
				return true;
			else
				return false;
			
		}
		
		public boolean isElementPresent(String locatorKey){
			
			List<WebElement> list = null;
			
			if(locatorKey.endsWith("_id")){
				
				list = driver.findElements(By.id(prop.getProperty(locatorKey)));
				
			}else if(locatorKey.endsWith("_name")){
				
				list = driver.findElements(By.name(prop.getProperty(locatorKey)));
				
			}else if(locatorKey.endsWith("_classname")){
				
				list = driver.findElements(By.className(prop.getProperty(locatorKey)));
				
			}else if(locatorKey.endsWith("_linktext")){
				
				list = driver.findElements(By.linkText(prop.getProperty(locatorKey)));
				
			}else if(locatorKey.endsWith("_cssselector")){
				
				list = driver.findElements(By.cssSelector(prop.getProperty(locatorKey)));
				
			}else if(locatorKey.endsWith("_xpath")){
				
				list = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
				
			}else{
				
				reportFail("The provided Locator - "+locatorKey+" is not found");
				Assert.fail("The provided Locator - "+locatorKey+" is not found");
			}
			
			if(list.size()>0)
				
				return true;
			else
				return false;
				
		}
		
	//Report Operations
	
		public void reportPass(String message){
			
			eTest.log(LogStatus.PASS, message);
			
		}
		
		public void reportFail(String message){
			
			eTest.log(LogStatus.FAIL, message);
			takeScreenshot();
			Assert.fail(message);
		}
		
		public void takeScreenshot(){
			
			// fileName of the screenshot
			Date d=new Date();
			String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
			// store screenshot in that file
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//put screenshot file in reports
			eTest.log(LogStatus.INFO,"Screenshot-> "+ eTest.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
			
		}
		
		/*
		public void waitForPageToLoad(){
			
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			String state = (String)jse.executeScript("return document.readyState");
			while(!state.equals("complete")){
				
				try {
					wait(2);
					state = (String)jse.executeScript("return document.readyState");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}*/
		
		public void wait(int timeToWait){
			
			try {
				Thread.sleep(timeToWait * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		
		/* -------------------- APP Functions ---------------------- */
		public boolean doLogin(String username, String password){
			eTest.log(LogStatus.INFO, "Trying to login with username "+username+" and password "+password);
			click("LoginLink_xpath");
			//wait(1);
			//waitForPageToLoad();
			driver.switchTo().frame("zohoiam");
			type("LoginUsername_id",username);
			type("LoginPassword_id",password);
			click("SignInButton_id");
			if(isElementPresent("CRMLink_xpath"))
				return true;
			else
				return false;
		}
		
		public int getLeadRowNumber(String leadName){
			
			eTest.log(LogStatus.INFO, "Finding the Lead "+leadName);
			
			List<WebElement> leadNames = driver.findElements(By.xpath(prop.getProperty("LeadNameColumnValues_xpath")));
			
			for(int i=0;i<leadNames.size();i++){
				
				System.out.println(leadNames.get(i).getText());
				
				if(leadNames.get(i).getText().trim().equals(leadName)){
					eTest.log(LogStatus.INFO, "Lead Found - "+leadName);
					return (i+1);
				}
				
			}
			eTest.log(LogStatus.INFO, "Lead not found "+leadName);	
			return -1;
		}
		

}
