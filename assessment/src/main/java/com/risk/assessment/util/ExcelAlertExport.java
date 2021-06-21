package com.risk.assessment.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.risk.assessment.models.AlertReview;
import com.risk.assessment.models.UserAlertReview;

public class ExcelAlertExport {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private static final String[] alertHeaders = { "Alert ID", "Score", "Focus Type", "Focus", "Customer Name",
			"Created", "Status", "Threshold Name", "Risk Score", "Employee Name", "Customer ID", "Processed By", "Start Time",
			"End Time", "Current Status", "Assigned to" };
	List<UserAlertReview> userAlerts;

	public ExcelAlertExport(List<UserAlertReview> userAlerts) {
		workbook = new XSSFWorkbook();
		this.userAlerts = userAlerts;
	}

	private void writeHeaderLine() {

		sheet = workbook.createSheet("Users");
		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(11);
		style.setFont(font);

		int colCntHead = 0;
		for (String header : alertHeaders) {
			createCell(row, colCntHead++, header, style);
		}
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {

		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(11);
		style.setFont(font);

		for (UserAlertReview userAlert : userAlerts) {
			AlertReview alert = userAlert.getAlert();
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, alert.getAlertId(), style);
			createCell(row, columnCount++, alert.getScore(), style);
			createCell(row, columnCount++, alert.getFocusType(), style);
			createCell(row, columnCount++, alert.getFocus().toString(), style);
			createCell(row, columnCount++, alert.getCustomerName(), style);

			createCell(row, columnCount++, alert.getCreated(), style);
			createCell(row, columnCount++, alert.getStatus(), style);
			createCell(row, columnCount++, alert.getThresholdName(), style);
			createCell(row, columnCount++, alert.getRiskScore().toString(), style);
			createCell(row, columnCount++, alert.getEmployeeName(), style);
			createCell(row, columnCount++, alert.getCustomerId(), style);
			
			createCell(row, columnCount++, userAlert.getProcessedBy(), style);
			createCell(row, columnCount++, userAlert.getStartTime().toString(), style);
			createCell(row, columnCount++, userAlert.getEndTime().toString(), style);
			createCell(row, columnCount++, userAlert.getStatus(), style);
			createCell(row, columnCount++, userAlert.getAssignedTo(), style);
		}

	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

}
