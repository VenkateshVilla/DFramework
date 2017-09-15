package com.zoho.dfwork.util;

import java.util.Hashtable;

import org.testng.annotations.DataProvider;

public class DataUtil {
	
	
	/*
	@DataProvider(name="dataProviderOne")
	public static Object[][] getDataOne() throws Exception{
		
		String fullFilePath = System.getProperty("user.dir")+"\\src\\test\\resources\\SampleData.xlsx";
		
		MyXLSReader xls = new MyXLSReader(fullFilePath);
	
		String testCaseName = "TestOne";
		
		String testDataSheet = "Testdata";
		
		
		int testStartRowNumber=1;		
		
		while(!(xls.getCellData(testDataSheet, 1, testStartRowNumber).equals(testCaseName))){
			
			testStartRowNumber++;
			
		}
		
		System.out.println("Test start row number - "+testStartRowNumber);
		
		int columnStartRowNumber = testStartRowNumber+1;
		int dataStartRowNumber = testStartRowNumber+2;
		
		//Total number of rows in the required test
		int rows=0;
		while(!(xls.getCellData(testDataSheet, 1, dataStartRowNumber+rows).equals(""))){
			
			rows++;
			
		}
		
		//System.out.println("Total rows are - "+rows);
		
		//Total number of columns in the required test
		int columns=1;
		
		while(!(xls.getCellData(testDataSheet, columns, columnStartRowNumber).equals(""))){
			
			columns++;
			
		}
		
		//System.out.println("Total number of columns - "+(columns-1));
		
		Object[][] obj = new Object[rows][columns-1];
		
		//Reading the data in the test
		for(int i=0,row=dataStartRowNumber;row<dataStartRowNumber+rows;row++,i++){
			
			for(int j=0,column=1;column<columns;column++,j++){
				
				obj[i][j]=xls.getCellData(testDataSheet, column, row);
				
			}
		
		}	
		
		return obj;
	
	}	
	*/
	
	@DataProvider(name="hashDataProvider")
	public static Object[][] getTestData(MyXLSReader xls_received, String testName, String sheetName) throws Exception{
		
		//String fullFilePath = System.getProperty("user.dir")+"\\src\\test\\resources\\SampleData.xlsx";
		
		MyXLSReader xls = xls_received;
	
		String testCaseName = testName;
		
		String testDataSheet = sheetName;
		
		int testStartRowNumber=1;		
		
		while(!(xls.getCellData(testDataSheet, 1, testStartRowNumber).equals(testCaseName))){
			
			testStartRowNumber++;
			
		}
		
		//System.out.println("Test start row number - "+testStartRowNumber);
		
		int columnStartRowNumber = testStartRowNumber+1;
		int dataStartRowNumber = testStartRowNumber+2;
		
		//Total number of rows in the required test
		int rows=0;
		while(!(xls.getCellData(testDataSheet, 1, dataStartRowNumber+rows).equals(""))){
			
			rows++;
			
		}
		
		//System.out.println("Total rows are - "+rows);
		
		//Total number of columns in the required test
		int columns=1;
		
		while(!(xls.getCellData(testDataSheet, columns, columnStartRowNumber).equals(""))){
			
			columns++;
			
		}
		
		//System.out.println("Total number of columns - "+(columns-1));
		
		Object[][] obj = new Object[rows][1];
		
		Hashtable<String,String> table = null;
		
		//Reading the data in the test
		for(int i=0,row=dataStartRowNumber;row<dataStartRowNumber+rows;row++,i++){
			
			table = new Hashtable<String,String>();
			
			for(int j=0,column=1;column<columns;column++,j++){
				
				String key=xls.getCellData(testDataSheet, column, columnStartRowNumber);
				
				String value=xls.getCellData(testDataSheet, column, row);
				
				table.put(key,value);
				
			}
			
			obj[i][0]=table;
		
		}	
		
		return obj;
	
	}
	
	public static boolean isRunnable(MyXLSReader xls_received, String tName, String sheet){
		
		String sheetName = sheet;

		MyXLSReader xls = xls_received;
		
		int rows = xls.getRowCount(sheetName);
		
		for(int r=2;r<=rows;r++){
			
			String testName = xls.getCellData(sheetName, 1, r);
			
			if(testName.equals(tName)){
				
				String runmode = xls.getCellData(sheetName, "Runmode", r);
				
				if(runmode.equals("Y"))					
					return true;
				else
					return false;
				
			}
			
		}
		
		return false;
		
	}
	

}
