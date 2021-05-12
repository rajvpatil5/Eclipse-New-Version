package com.src.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelData {
	

//	public static ArrayList<String> readExcelData() throws IOException {
//		String currentDirectory = System.getProperty("user.dir");
//		File pathToFile = new File(currentDirectory + "/ExecutionData.xlsx");
//		FileInputStream fis = new FileInputStream(pathToFile);
//		Workbook workbook = new XSSFWorkbook(fis);
//		Sheet sheet = workbook.getSheetAt(0);
//		int lastRow = sheet.getLastRowNum();
//		ArrayList<String> data = new ArrayList<String>();
//		// Looping over entire row
//		try{
//		for (int i = 0; i <= lastRow; i++) {
//			Row row = sheet.getRow(i);
//			if (row.getCell(0) == null) {
//				break;
//			} else {
//				// 1st Cell as Value
//				Cell valueCell = row.getCell(1);
//				// 0th Cell as Key
//				// Cell keyCell = row.getCell(0);
//				try {
//					data.add(valueCell.getStringCellValue().trim());
//				} catch (Exception e) {
//					int xyz = (int) valueCell.getNumericCellValue();
//					String xy = "" + xyz;
//					data.add(xy);
//				}
//			}
//		}
//		}catch(Exception e)
//		{
//			System.out.print("\nException in readExcelData() of ExcelData class..\n");
//			e.printStackTrace();
//		}
//		return data;
//
//	}

	public static void writeExcelData(ArrayList<Object[]> data) throws IOException {
		try {
			 String currentDirectory = System.getProperty("user.dir");
			 File pathToFile = new File(currentDirectory + "/MeetingDetails.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = createSheet(workbook, "Sheet 1", false);
			XSSFRow row1 = sheet.createRow(0);
			XSSFCell r1c2 = row1.createCell(0);
			r1c2.setCellValue("MeetingName");
			XSSFCell r1c3 = row1.createCell(1);
			r1c3.setCellValue("UserName");
			XSSFCell r1c4 = row1.createCell(2);
			r1c4.setCellValue("NodeIP");
			XSSFCell r1c5 = row1.createCell(3);
			r1c5.setCellValue("MeetingURL");
			XSSFCell r1c6 = row1.createCell(4);
			r1c6.setCellValue("MeetingStatus");
			// Iterate over data and write to sheet
			ArrayList<Object[]> keyIds = data;
			// ArrayList<Object[]> sessionIds = SessionUrl;
			int rowid = 1;

			for (Object[] key : keyIds) {

				Row row = sheet.createRow(rowid++);
				int cellnum = 0;
				for (Object obj : key) {
					Cell cell = row.createCell(cellnum++);
					if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof String)
						cell.setCellValue((String) obj);
				}
			}
			// Save excel to HDD Drive

			if (!pathToFile.exists()) {
				pathToFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(pathToFile);
			workbook.write(fos);
			fos.close();
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static XSSFSheet createSheet(XSSFWorkbook wb, String prefix, boolean isHidden) {
		XSSFSheet sheet = null;
		int count = 0;

		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			String sName = wb.getSheetName(i);
			if (sName.startsWith(prefix))
				count++;
		}

		if (count > 0) {
			sheet = wb.createSheet(prefix + count);
		} else
			sheet = wb.createSheet(prefix);

		if (isHidden)
			wb.setSheetHidden(wb.getNumberOfSheets() - 1, true);

		return sheet;
	}

}
