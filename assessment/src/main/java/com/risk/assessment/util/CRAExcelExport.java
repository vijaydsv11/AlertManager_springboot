package com.risk.assessment.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.risk.assessment.models.ComplainceRisk;

public class CRAExcelExport {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	List<ComplainceRisk> compRisks;

	private static final String[] headers = { "S.No.", "Addition, Modification, Deletion", "Compliance Officer",
			"Circular Ref. No.", "Circular Date", "Circular Name", "Regulator", "Risk Source", "Cluster",
			"Regulatory Guidelines", "Control Description", "Risk Owner", "Risk Sub-Unit", "Control Owner",
			"Control Sub-Unit", "Product / Function", "Product Note", "Product Launch Date", "Product Testing",
			"Product Testing Date", "Board Approved Policy", "Relevant Para of Policy", "Process Note",
			"Likelihood Rating", "Impact (3-High Regulatory / 2-Medium Regulatory / 1 - Low Regulatory ",
			"Monitoring Mechanism (1-Maker/Checker 2 - No Maker/Checker 3-No Control/control failure",
			"Control Automation (1 - Automated 2 - Semi Automated 3 - Manual Process",
			"Control Effectiveness 1 - Satisfactory 2 - Improvement Needed 3 - Weak", "Compliance Risk", "Breach",
			"Process Improvement", "Details of Breach", "Details of Process Improvement", "Timeline", "SPOC", "Manco Member"};

	public CRAExcelExport(List<ComplainceRisk> compRisks) {
		workbook = new XSSFWorkbook();
		this.compRisks = compRisks;
	}

	private void writeHeaderLine(String sheetName) {

		sheet = workbook.createSheet(sheetName);
		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(11);
		style.setFont(font);

		int colCntHead = 0;
		for (String header : headers) {
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

	private void writeDataLines(boolean isProccessedReport) {

		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(11);
		style.setFont(font);

		for (ComplainceRisk risk : compRisks) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, risk.getSno(), style);
			createCell(row, columnCount++, risk.getAddModDel(), style);
			createCell(row, columnCount++, risk.getCompOfficer(), style);
			createCell(row, columnCount++, risk.getCircularRefNo(), style);
			createCell(row, columnCount++, risk.getCircularDate(), style);

			createCell(row, columnCount++, risk.getCircularName(), style);
			createCell(row, columnCount++, risk.getRegulator(), style);
			createCell(row, columnCount++, risk.getRiskSource(), style);
			createCell(row, columnCount++, risk.getCluster(), style);
			createCell(row, columnCount++, risk.getRegGuidlines(), style);
			createCell(row, columnCount++, risk.getControlDesc(), style);

			createCell(row, columnCount++, risk.getRiskOwner(), style);
			createCell(row, columnCount++, risk.getRiskSubUnit(), style);
			createCell(row, columnCount++, risk.getControlOwner(), style);
			createCell(row, columnCount++, risk.getControlSubUnit(), style);
			createCell(row, columnCount++, risk.getProdFunc(), style);

			createCell(row, columnCount++, risk.getProdName(), style);
			createCell(row, columnCount++, risk.getProdLaunchDt(), style);
			createCell(row, columnCount++, risk.getProdTesting(), style);
			createCell(row, columnCount++, risk.getProdTestingDt(), style);
			createCell(row, columnCount++, risk.getBoardAppPolicy(), style);

			createCell(row, columnCount++, risk.getRelevantPolicy(), style);
			createCell(row, columnCount++, risk.getProcessNote(), style);
			createCell(row, columnCount++, risk.getLikeRating(), style);
			createCell(row, columnCount++, risk.getImpact(), style);
			createCell(row, columnCount++, risk.getMonitorMechanism(), style);

			createCell(row, columnCount++, risk.getControlAutomation(), style);
			createCell(row, columnCount++, risk.getControlEffective(), style);
			createCell(row, columnCount++, risk.getCompRisk(), style);
			createCell(row, columnCount++, risk.getBreach(), style);
			createCell(row, columnCount++, risk.getProcessImprovement(), style);

			createCell(row, columnCount++, risk.getDetailsBreach(), style);
			createCell(row, columnCount++, risk.getDetailProcImprovement(), style);
			createCell(row, columnCount++, risk.getTimeline(), style);
			createCell(row, columnCount++, risk.getSpoc(), style);
			createCell(row, columnCount++, risk.getMancoMember(), style);
		}
	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine("Complaince Risk");
		writeDataLines(true);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	public void exportToFolder(String path, List<ComplainceRisk> compRisks) throws IOException  {
		this.compRisks = compRisks;
		writeHeaderLine("Complaince Risk");
		writeDataLines(false);

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String currentDateTime = dateFormatter.format(new Date());
		String excelName = "CRA_Automation_" + currentDateTime + ".xlsx";
		FileOutputStream outputStream = null;
		try {
			String currentFolder = Paths.get("").toAbsolutePath().toString();
			String folder = currentFolder + "\\backup\\download\\CRA\\";
			File file = new File(folder);
			if (!file.exists()) {
				file.mkdirs();
			}
			outputStream = new FileOutputStream(new File(folder + excelName));
			workbook.write(outputStream);
		} catch ( Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			outputStream.close();
		}
	}
	
}
