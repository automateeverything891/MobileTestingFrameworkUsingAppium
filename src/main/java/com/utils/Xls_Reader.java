package com.utils;


import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddressBase.CellPosition;
import org.apache.poi.xssf.usermodel.*;


import org.apache.poi.ss.usermodel.DataFormatter;

import java.awt.Color;
import java.io.*;
import java.util.Calendar;
/*
 * JaKay xcel reader this xcel i modified for my project automation.
 */

public class Xls_Reader {
	public static String filename = System.getProperty("user.dir")+"\\src\\main\\java\\com\\atlasqa\\testdata\\TestSuite1.xlsx";
	public  String path;
	public  FileInputStream fis = null;
	public  FileOutputStream fileOut =null;
	public  XSSFWorkbook workbook = null;
	public XSSFSheet sheet = null;
	public XSSFRow row   =null;
	public XSSFCell cell = null;
	
	public Xls_Reader(String path) {
		
		this.path=path;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	// returns the row count in a sheet
	public int getRowCount(String sheetName){
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1)
			return 0;
		else{
		sheet = workbook.getSheetAt(index);
		int number=sheet.getLastRowNum()+1;
		return number;
		}
		
	}
	
	// returns the data from a cell
	public String getCellData(String sheetName,String colName,int rowNum){
		DataFormatter formatter = new DataFormatter();
		try{
			if(rowNum <=0)
				return "";
		
		int index = workbook.getSheetIndex(sheetName);
		int col_Num=-1;
		if(index==-1)
			return "";
		
		sheet = workbook.getSheetAt(index);
		row=sheet.getRow(0);
		//Iterator rowItr = sheet.rowIterator();

	   // while ( rowItr.hasNext() ) {
	    //    row = (XSSFRow) rowItr.next();
	    
		if(row== null){
			return"";
		}
		
		//System.out.println(sheet.getFirstRowNum());
				//System.out.println(sheet.getLastRowNum());
				//System.out.println(sheet.getRow(1).getLastCellNum());
				for(int j=0; j<=sheet.getLastRowNum();j++){
				
					if(sheet.getRow(j)!=null){
						for(int i=0;i<=sheet.getRow(j).getLastCellNum();i++){
					//System.out.println(sheet.getRow(j).getCell(i).getStringCellValue());
					if(sheet.getRow(j).getCell(i)!=null)
					{
						
					//System.out.println(sheet.getRow(j).getCell(i).getStringCellValue().trim());
						String text = formatter.formatCellValue(sheet.getRow(j).getCell(i));
					if(text.trim().equalsIgnoreCase(colName.trim()))
					{
						col_Num=i;
					    break;
					}   
					
					else{
						//System.out.println();
					}
					}
					
					}
						
					
				}
				}
		
		if(col_Num==-1)
			return "";
		
		sheet = workbook.getSheetAt(index);
		row = sheet.getRow(rowNum-1);
		if(row==null)
			return "";
		cell = row.getCell(col_Num);
		
		if(cell==null)
			return "";
		//System.out.println(cell.getCellType());
		if(cell.getCellType()==CellType.STRING)
			  return cell.getStringCellValue();
		else if(cell.getCellType()==CellType.NUMERIC || cell.getCellType()==CellType.FORMULA){
			  
			  String cellText  = String.valueOf(cell.getNumericCellValue());
			  if (DateUtil.isCellDateFormatted(cell)) {
		           // format in form of M/D/YY
				  double d = cell.getNumericCellValue();

				  Calendar cal =Calendar.getInstance();
				  cal.setTime(DateUtil.getJavaDate(d));
		            cellText =
		             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
		           cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" +
		                      cal.get(Calendar.MONTH)+1 + "/" + 
		                      cellText;
		           
		           //System.out.println(cellText);

		         }

			  
			  
			  return cellText;
		  }else if(cell != null || cell.getCellType()==CellType.BLANK)
		      return ""; 
		  else 
			  return String.valueOf(cell.getBooleanCellValue());
		
		}
	    
		catch(Exception e){
			
			e.printStackTrace();
			return "row "+rowNum+" or column "+colName +" does not exist in xls";
		}
	
	}
	
	// returns the data from a cell
	public String getCellData(String sheetName,int colNum,int rowNum){
		try{
			if(rowNum <=0)
				return "";
		
		int index = workbook.getSheetIndex(sheetName);

		if(index==-1)
			return "";
		
	
		sheet = workbook.getSheetAt(index);
		row = sheet.getRow(rowNum-1);
		if(row==null)
			return "";
		cell = row.getCell(colNum);
		if(cell==null)
			return "";
		
	  if(cell.getCellType()==CellType.STRING)
		  return cell.getStringCellValue();
	  else if(cell.getCellType()==CellType.NUMERIC || cell.getCellType()==CellType.FORMULA ){
		  
		  String cellText  = String.valueOf(cell.getNumericCellValue());
		  if (DateUtil.isCellDateFormatted(cell)) {
	           // format in form of M/D/YY
			  double d = cell.getNumericCellValue();

			  Calendar cal =Calendar.getInstance();
			  cal.setTime(DateUtil.getJavaDate(d));
	            cellText =
	             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
	           cellText = cal.get(Calendar.MONTH)+1 + "/" +
	                      cal.get(Calendar.DAY_OF_MONTH) + "/" +
	                      cellText;
	           
	          // System.out.println(cellText);

	         }

		  
		  
		  return cellText;
	  }else if(cell.getCellType()==CellType.BLANK)
	      return "";
	  else 
		  return String.valueOf(cell.getBooleanCellValue());
		}
		catch(Exception e){
			
			e.printStackTrace();
			return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
		}
	}
	
	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data){
		try{
		fis = new FileInputStream(path); 
		//System.out.println(path);
		workbook = new XSSFWorkbook(fis);

		if(rowNum<=0)
			return false;
		
		int index = workbook.getSheetIndex(sheetName);
		int col_Num=-1;
		if(index==-1)
			return false;
		
		sheet = workbook.getSheetAt(index);

		row=sheet.getRow(0);
		
		for(int j=0; j<=sheet.getLastRowNum();j++){
			
			if(sheet.getRow(j)!=null){
			for(int i=0;i<=sheet.getRow(j).getLastCellNum();i++){
			//System.out.println(sheet.getRow(j).getCell(i).getStringCellValue());
			if(sheet.getRow(j).getCell(i)!=null)
			{
				
			//System.out.println(sheet.getRow(j).getCell(i).getStringCellValue().trim());
			if(sheet.getRow(j).getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName.trim()))
			{
				//System.out.println(i);
				col_Num=i-1;
			    break;
			}   
			
			else{
				//System.out.println();
			}
			}
			
			}
				
			
		}
		}
		if(col_Num==-1)
			return false;

		sheet.autoSizeColumn(col_Num); 
		row = sheet.getRow(rowNum);
		if (row == null)
			row = sheet.createRow(rowNum);
		
		cell = row.getCell(col_Num);	
		if (cell == null)
	        cell = row.createCell(col_Num);

	    // cell style
	    //CellStyle cs = workbook.createCellStyle();
	    //cs.setWrapText(true);
	    //cell.setCellStyle(cs);
	    cell.setCellValue(data);

	    fileOut = new FileOutputStream(path);

		workbook.write(fileOut);

	    fileOut.close();	

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data,String url){
		//System.out.println("setCellData setCellData******************");
		try{
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);

		if(rowNum<=0)
			return false;
		
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		if(index==-1)
			return false;
		
		
		sheet = workbook.getSheetAt(index);
		//System.out.println("A");
		row=sheet.getRow(0);
		for(int i=0;i<row.getLastCellNum();i++){
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
				colNum=i;
		}
		
		if(colNum==-1)
			return false;
		sheet.autoSizeColumn(colNum); 
		row = sheet.getRow(rowNum-1);
		if (row == null)
			row = sheet.createRow(rowNum-1);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);
			
	    cell.setCellValue(data);
	    XSSFCreationHelper createHelper = workbook.getCreationHelper();

	    //cell style for hyperlinks
	    //by default hypelrinks are blue and underlined
	    CellStyle hlink_style = workbook.createCellStyle();
	    XSSFFont hlink_font = workbook.createFont();
	    hlink_font.setUnderline(Font.U_SINGLE);
	    hlink_font.setColor(IndexedColors.BLUE.getIndex());
	    hlink_style.setFont(hlink_font);
	    //hlink_style.setWrapText(true);

	    XSSFHyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
	    link.setAddress(url);
	    cell.setHyperlink(link);
	    cell.setCellStyle(hlink_style);
	      
	    fileOut = new FileOutputStream(path);
		workbook.write(fileOut);

	    fileOut.close();	

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	// returns true if sheet is created successfully else false
	public boolean addSheet(String  sheetname){		
		
		FileOutputStream fileOut;
		try {
			 workbook.createSheet(sheetname);	
			 fileOut = new FileOutputStream(path);
			 workbook.write(fileOut);
		     fileOut.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// returns true if sheet is removed successfully else false if sheet does not exist
	public boolean removeSheet(String sheetName){		
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1)
			return false;
		
		FileOutputStream fileOut;
		try {
			workbook.removeSheetAt(index);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		    fileOut.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	// returns true if column is created successfully
	public boolean addColumn(String sheetName,String colName){
		//System.out.println("**************addColumn*********************");
		
		try{				
			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if(index==-1)
				return false;
			
		XSSFCellStyle style = workbook.createCellStyle();
		//style.setFillForegroundColor(XSSFColor.from(Color.BLACK, index));
		//style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		sheet=workbook.getSheetAt(index);
		
		row = sheet.getRow(0);
		if (row == null)
			row = sheet.createRow(0);
		
		//cell = row.getCell();	
		//if (cell == null)
		//System.out.println(row.getLastCellNum());
		if(row.getLastCellNum() == -1)
			cell = row.createCell(0);
		else
			cell = row.createCell(row.getLastCellNum());
	        
	        cell.setCellValue(colName);
	        cell.setCellStyle(style);
	        
	        fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		    fileOut.close();		    

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
		
		
	}
	// removes a column and all the contents
	public boolean removeColumn(String sheetName, int colNum) {
		try{
		if(!isSheetExist(sheetName))
			return false;
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);
		sheet=workbook.getSheet(sheetName);
		XSSFCellStyle style = workbook.createCellStyle();
		//style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		//style.setFillPattern(Patt);
		
	    
	
		for(int i =0;i<getRowCount(sheetName);i++){
			row=sheet.getRow(i);	
			if(row!=null){
				cell=row.getCell(colNum);
				if(cell!=null){
					cell.setCellStyle(style);
					row.removeCell(cell);
				}
			}
		}
		fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
	    fileOut.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
  // find whether sheets exists	
	public boolean isSheetExist(String sheetName){
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1){
			index=workbook.getSheetIndex(sheetName.toUpperCase());
				if(index==-1)
					return false;
				else
					return true;
		}
		else
			return true;
	}
	
	// returns number of columns in a sheet	
	public int getColumnCount(String sheetName){
		// check if sheet exists
		if(!isSheetExist(sheetName))
		 return -1;
		
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);
		
		if(row==null)
			return -1;
		
		return row.getLastCellNum();
		
		
		
	}
	//String sheetName, String testCaseName,String keyword ,String URL,String message
	public boolean addHyperLink(String sheetName,String screenShotColName,String testCaseName,int index,String url,String message){
		//System.out.println("ADDING addHyperLink******************");
		
		url=url.replace('\\', '/');
		if(!isSheetExist(sheetName))
			 return false;
		
	    sheet = workbook.getSheet(sheetName);
	    
	    for(int i=2;i<=getRowCount(sheetName);i++){
	    	if(getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)){
	    		//System.out.println("**caught "+(i+index));
	    		setCellData(sheetName, screenShotColName, i+index, message,url);
	    		break;
	    	}
	    }


		return true; 
	}
	public int getCellRowNum(String sheetName,String colName,String cellValue){
		
		for(int i=2;i<=getRowCount(sheetName);i++){
			System.out.println(getCellData(sheetName, colName, i));
	    	if(getCellData(sheetName,colName , i).equalsIgnoreCase(cellValue)){
	    		return i;
	    	}
	    }
		return -1;
		
	}
		
	public boolean setCellDataInparticularCell(String testcases,String sheetName,String colName, String data){
		
		
		try{
		
		fis = new FileInputStream(path); 
		System.out.println(path);
		workbook = new XSSFWorkbook(fis);
		int testStartsRowNum = 0;
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		if(index==-1)
			return false;
		
		
		sheet = workbook.getSheetAt(index);
		//System.out.println("A");
		row=sheet.getRow(0);
		for (int rNum = 1; rNum <= getRowCount("Test Data"); rNum++) {
			if (testcases.equals(getCellData("Test Data", 0, rNum))) {
				//System.out.println(xls.getCellData("Test Data", 0, rNum));
				testStartsRowNum = rNum;
				break;
			}
		}
		if(testStartsRowNum<=0)
			return false;
		int columnStartNum = testStartsRowNum + 1;
		int cols = 0;

		while (!getCellData("Test Data", cols, columnStartNum).equals("")) {
			//System.out.println(cols);
			cols++;
		}
		
		int rowStartNum = testStartsRowNum + 2;
		int rows = 0;

		while (!getCellData("Test Data", 0, (rowStartNum + rows)).equals("")) {
			//System.out.println(rows);
			rows++;
		}
		
		row=sheet.getRow(testStartsRowNum);
			/*
			 * System.out.println(testStartsRowNum); System.out.println(rows);
			 * System.out.println(columnStartNum); System.out.println(rowStartNum);
			 */
		for (int rNum = rowStartNum; rNum < (rowStartNum + rows); rNum++) {
			for (int cNum = 0; cNum < cols; cNum++) {
				//System.out.print(xls.getCellData("Test Data", cNum, columnStartNum) + "--"+ xls.getCellData("Test Data", cNum, rNum) + " --");
				//System.out.println(getCellData("Test Data", cNum, columnStartNum));
				//System.out.println(getCellData("Test Data", cNum, rNum) + " --");
				if(getCellData("Test Data", cNum, columnStartNum).equalsIgnoreCase(colName)){
					
					int rw =rNum;
					while(rw<(rowStartNum + rows)){
						if(getCellData("Test Data", cNum, rw).equals("")){
						    sheet.autoSizeColumn(cNum); 
							row = sheet.getRow(rw-1);
							if (row == null)
								row = sheet.createRow(rw-1);
							cell = row.getCell(cNum);	
							if (cell == null)
						        cell = row.createCell(cNum);

						    // cell style
						    //CellStyle cs = workbook.createCellStyle();
						    //cs.setWrapText(true);
						    //cell.setCellStyle(cs);
						    cell.setCellValue(data);

						    fileOut = new FileOutputStream(path);

							workbook.write(fileOut);

						    fileOut.close();	
						    workbook = new XSSFWorkbook(new FileInputStream(path));
						    break;
						}
						rw++;
					}
						
						 
					}
				}
			break;
			}
					
					
		
		
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean setCellData1(String sheetName, String colName,int rowNum, String data){
		try{
		fis = new FileInputStream(path); 
		//System.out.println(path);
		workbook = new XSSFWorkbook(fis);

		if(rowNum<=0)
			return false;
		
		int index = workbook.getSheetIndex(sheetName);
		int col_Num=-1;
		if(index==-1)
			return false;
		
		sheet = workbook.getSheetAt(index);

		row=sheet.getRow(0);
		
		for(int j=0; j<=sheet.getLastRowNum();j++){
			
			if(sheet.getRow(j)!=null){
			for(int i=0;i<=sheet.getRow(j).getLastCellNum();i++){
			//System.out.println(sheet.getRow(j).getCell(i).getStringCellValue());
			if(sheet.getRow(j).getCell(i)!=null)
			{
				
			//System.out.println(sheet.getRow(j).getCell(i).getStringCellValue().trim()+"---"+col_Num);
			if(sheet.getRow(j).getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName.trim()))
			{
				
				//System.out.println(i);
				col_Num=i;
			    break;
			}   
			
			else{
				//System.out.println();
			}
			}
			
			}
				
			
		}
		}
		if(col_Num==-1)
			return false;

		sheet.autoSizeColumn(col_Num); 
		row = sheet.getRow(rowNum);
		if (row == null)
			row = sheet.createRow(rowNum);
		
		cell = row.getCell(col_Num);	
		if (cell == null)
	        cell = row.createCell(col_Num);

	    // cell style
	    //CellStyle cs = workbook.createCellStyle();
	    //cs.setWrapText(true);
	    //cell.setCellStyle(cs);
	    cell.setCellValue(data);

	    fileOut = new FileOutputStream(path);

		workbook.write(fileOut);

	    fileOut.close();	

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	// to run this on stand alone
	public static void main(String arg[]) throws IOException{
		
	}
	
	
}