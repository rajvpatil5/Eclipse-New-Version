package demo.websocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	static Workbook wb;

	LinkedList<String> email = readExcel(0);
	LinkedList<String> phone = readExcel(1);
	LinkedList<String> amount = readExcel(2);
	LinkedList<String> cardNumbers = readExcel(3);
	LinkedList<String> cardExp = readExcel(4);
	LinkedList<String> cardHolderName = readExcel(5);
	LinkedList<String> cardCVV = readExcel(6);

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
			System.out.println(rows);
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
}
