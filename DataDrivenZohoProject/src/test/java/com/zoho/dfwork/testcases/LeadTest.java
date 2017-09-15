package com.zoho.dfwork.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;
import com.zoho.dfwork.base.BaseTest;
import com.zoho.dfwork.util.DataUtil;
import com.zoho.dfwork.util.MyXLSReader;

public class LeadTest extends BaseTest {
	
	String testCaseName = "CreateLeadTest";
	
	MyXLSReader xls;
	
	@Test(priority=1,dataProvider="getData")
	public void createLeadTest(Hashtable<String,String> data){
		
		eTest = eReport.startTest("CreateLeadTest");
		
		if(!DataUtil.isRunnable(xls, testCaseName, "Testcases") || data.get("Runmode").equals("N")){
			eTest.log(LogStatus.SKIP, "Skipping the test as the run mode is set to N");
			throw new SkipException("Skipping the test as the run mode is set to N");
			
		}
		
		openBrowser(data.get("Browser"));		
		navigate("appURL");
		
		boolean actualResult = doLogin(prop.getProperty("username"),prop.getProperty("password"));
	
		if(actualResult==false)
			reportFail("Login test failed");
		
		click("CRMLink_xpath");
		click("Leads_id");
		click("CreateLead_xpath");
		type("LeadCompany_id",data.get("LeadCompany"));
		type("LeadLastName_id",data.get("LeadLastName"));
		click("SaveLead_id");
		clickAndWait("Leads_id","CreateLead_xpath");
		//Validate whether the lead creation
		int rowNum = getLeadRowNumber(data.get("LeadLastName"));
		if(rowNum==-1)
			reportFail("Lead not found in the Lead table "+data.get("LeadLastName"));
		
		reportPass("Lead found in the Lead table "+data.get("LeadLastName"));
		
		
	}
	
	
	@AfterMethod
	public void quit(){
		
		if(eReport!=null){
			eReport.endTest(eTest);
			eReport.flush();
		}
		
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
