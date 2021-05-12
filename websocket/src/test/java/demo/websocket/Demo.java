package demo.websocket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Demo {
	public static void main(String args[]) throws IOException {
		ArrayList<Object[]> sessionUrl = new ArrayList<Object[]>();
		sessionUrl.add(new String[] { "meetingName", "userName", "node", "Null", "Fail", "Rajat", "Patil" });
		writeExcelData(sessionUrl);
	}

	static XSSFSheet createSheet(XSSFWorkbook wb, String prefix, boolean isHidden) {
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

	static void writeExcelData(ArrayList<Object[]> data) throws IOException {
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
