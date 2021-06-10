package com.syscort.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	static Workbook wb;

	public LinkedList<String> email = readExcel(0);
	public LinkedList<String> phone = readExcel(1);
	public LinkedList<String> amount = readExcel(2);
	public LinkedList<String> cardNumbers = readExcel(3);
	public LinkedList<String> cardExp = readExcel(4);
	public LinkedList<String> cardHolderName = readExcel(5);
	public LinkedList<String> cardCVV = readExcel(6);
	public LinkedList<String> cardPin = readExcel(7);

	public LinkedList<String> readExcel(int colnum) {
		LinkedList<String> test = new LinkedList<String>();
		try {
			File fs = new File(".//userdata.xlsx");
			FileInputStream fis = new FileInputStream(fs);
			wb = new XSSFWorkbook(fis);
			Sheet sheet = wb.getSheetAt(0);
			Row row;
			Cell cell;
			DataFormatter fmt = new DataFormatter();

			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();
//			System.out.println(rows);
			int cols = 0; // No of columns
			int tmp = 0;

			// This loop handles if user by mistakenly insert empty rows at starting of
			// excel
			for (int i = 0; i < 10 || i < rows; i++) {
				row = sheet.getRow(i);
				if (row != null) {
					tmp = sheet.getRow(i).getPhysicalNumberOfCells();
					if (tmp > cols)
						cols = tmp;
				}
			}

			// Actual logic for reading data
			for (int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				if (row != null) {
					for (int c = colnum; c < colnum + 1; c++) {
						cell = row.getCell(c);
						if (cell != null) {
							// Your code here
							String valueAsSeenInExcel = fmt.formatCellValue(cell);
							test.add(valueAsSeenInExcel);
						}
					}
				}
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		System.out.println(test);
		return test;
	}

	public static XSSFSheet createSheet(XSSFWorkbook wb, String prefix, boolean isHidden) {
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

	public static void writeExcelData(ArrayList<Object[]> data) throws IOException {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = createSheet(workbook, "Sheet 1", false);
			XSSFRow row1 = sheet.createRow(0);
			XSSFCell r1c2 = row1.createCell(0);

			r1c2.setCellValue("Email");
			XSSFCell r1c3 = row1.createCell(1);
			r1c3.setCellValue("Phone");
			XSSFCell r1c4 = row1.createCell(2);
			r1c4.setCellValue("Amount");
			XSSFCell r1c5 = row1.createCell(3);
			r1c5.setCellValue("Card Number");
			XSSFCell r1c6 = row1.createCell(4);
			r1c6.setCellValue("Card Expiry");
			XSSFCell r1c7 = row1.createCell(5);
			r1c7.setCellValue("Card Holder's Name");
			XSSFCell r1c8 = row1.createCell(6);
			r1c8.setCellValue("Card CVV");
			XSSFCell r1c9 = row1.createCell(7);
			r1c9.setCellValue("Card Pin");
			XSSFCell r1c10 = row1.createCell(8);
			r1c10.setCellValue("Transaction Message");

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
				}
			}
			// Save excel to HDD Drive
			String currentDirectory = System.getProperty("user.dir");
			File pathToFile = new File(currentDirectory + "/TransactionDetails.xlsx");
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
}
