package com.zoho.dfwork.testcases;

import java.util.Hashtable;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zoho.dfwork.util.DataUtil;
import com.zoho.dfwork.util.MyXLSReader;


import com.relevantcodes.extentreports.LogStatus;
import com.zoho.dfwork.base.BaseTest;

public class LoginTest extends BaseTest {
	
	String testCaseName = "LoginTest";
	
	MyXLSReader xls;
	
	@Test(dataProvider="getData")
	public void doLoginTest(Hashtable<String,String> data){
		
		eTest = eReport.startTest("LoginTest");
		eTest.log(LogStatus.INFO, "Starting the test LoginTest with the below data ");
		
		if(!DataUtil.isRunnable(xls, testCaseName, "Testcases") || data.get("Runmode").equals("N")){
			System.out.println(!DataUtil.isRunnable(xls, testCaseName, "Testcases"));
			System.out.println(data.get("Runmode").equals("N"));
			eTest.log(LogStatus.SKIP, "Skipping the test as the run mode is set to N");
			throw new SkipException("Skipping the test as the run mode is set to N");
			
		}
		
		openBrowser(data.get("Browser"));
		navigate("appURL");
		
		boolean actualResult = doLogin(data.get("Username"),data.get("Password"));
		
		boolean expectedResult=false;
		
		if(data.get("ExpectedResult").equals("Success"))
			expectedResult=true;
		else
			expectedResult=false;
		
		if(expectedResult!=actualResult)
			reportFail("Login test failed");
		
		reportPass("Login test passed");
		
	}

	
	@AfterMethod
	public void quit(){
		
		eReport.endTest(eTest);
		eReport.flush();
		
		if(driver!=null)
			driver.quit();
		
	}
	
	@DataProvider
	public Object[][] getData() throws Exception{
		
		String filePath = prop.getProperty("XlsxFilePath");
		
		String workSpacePath = System.getProperty("user.dir");
		
		String fullFilePath = workSpacePath+filePath;
		
		xls = new MyXLSReader(fullFilePath);
		
		return DataUtil.getTestData(xls,testCaseName,"Data");
		
	}
	
	@BeforeClass
	public void init(){
		
		initialise();
		
	}
	
	
}
