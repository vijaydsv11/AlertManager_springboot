package com.risk.assessment.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
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

public class AlertExcelExport {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	List<UserAlertReview> userAlerts;

	private static final String[] headers_unassignedUser = { "Alert ID", "Score", "Focus Type", "Focus",
			"Customer Name", "Created", "Status", "Threshold Name", "Risk Score", "Employee Name", "Customer ID" };

	private static final String[] headers_assignedUser = { "Alert ID", "Score", "Focus Type", "Focus", "Customer Name",
			"Created", "Status", "Threshold Name", "Risk Score", "Employee Name", "Customer ID", "Processed By",
			"Start Time", "End Time", "Current Status", "Assigned to" };

	public AlertExcelExport(List<UserAlertReview> userAlerts) {
		workbook = new XSSFWorkbook();
		this.userAlerts = userAlerts;
	}

	private void writeHeaderLine(String[] alertHeaders, String sheetName) {

		sheet = workbook.createSheet(sheetName);
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
		} else if (value instanceof OffsetDateTime) {
			cell.setCellValue(value.toString());
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines(boolean isProccessedReport) {

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
			createCell(row, columnCount++, alert.getFocus(), style);
			createCell(row, columnCount++, alert.getCustomerName(), style);

			createCell(row, columnCount++, alert.getCreated(), style);
			createCell(row, columnCount++, alert.getStatus(), style);
			createCell(row, columnCount++, alert.getThresholdName(), style);
			createCell(row, columnCount++, alert.getRiskScore(), style);
			createCell(row, columnCount++, alert.getEmployeeName(), style);
			createCell(row, columnCount++, alert.getCustomerId(), style);

			if (isProccessedReport) {
				createCell(row, columnCount++, userAlert.getProcessedBy(), style);
				createCell(row, columnCount++, userAlert.getStartTime(), style);
				createCell(row, columnCount++, userAlert.getEndTime(), style);
				createCell(row, columnCount++, userAlert.getStatus(), style);
				createCell(row, columnCount++, userAlert.getAssignedTo(), style);
			}
		}
	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine(headers_assignedUser, "Processed Report");
		writeDataLines(true);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	public void exportToFolderWithUnAssignedUser(String path, List<UserAlertReview> alerts) throws IOException {
		this.userAlerts = alerts;
		writeHeaderLine(headers_unassignedUser, "Outstanding Summary");
		writeDataLines(false);
	}

	public void exportToFolder(String path, List<UserAlertReview> alerts) throws IOException {
		this.userAlerts = alerts;
		writeHeaderLine(headers_assignedUser, "Processed Report");
		writeDataLines(true);

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String currentDateTime = dateFormatter.format(new Date());
		String excelName = "Alert_Review_" + currentDateTime + ".xlsx";
		FileOutputStream outputStream = null;
		try {
			String currentFolder = Paths.get("").toAbsolutePath().toString();
			// System.out.println(System.getProperty("user.dir"));
			String folder = currentFolder + "\\backup\\download\\alert\\";
			File file = new File(folder);
			if (!file.exists()) {
				file.mkdirs();
			}
			outputStream = new FileOutputStream(new File(folder + excelName));
			workbook.write(outputStream);
			workbook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (outputStream != null) {
			outputStream.close();
		}

	}
}
