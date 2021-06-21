package com.risk.assessment.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.risk.assessment.models.AlertReview;
/**
 * 
 * @author vijay ganesh
 *
 */
public class ExcelPOIHelper {

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static List<AlertReview> getExcelDataAsList(InputStream is) {

		Workbook workbook = null;
		DataFormatter dataFormatter = new DataFormatter();
		List<AlertReview> alertinputs = new ArrayList<AlertReview>();

		try {
			workbook = new XSSFWorkbook(is);
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}

		Sheet sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			if(row.getRowNum() != 0) {
				setCellValueToAlertInput(getCellValue(row, dataFormatter), alertinputs);
			}
		}
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return alertinputs;
	}

	private static List<String> getCellValue(Row row, DataFormatter dataFormatter) {

		List<String> cellValues = new ArrayList<String>();
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cellValues.add(dataFormatter.formatCellValue(cell));
		}
		return cellValues;
	}

	private static List<AlertReview> setCellValueToAlertInput(List<String> cellValues, List<AlertReview> alertinputs) {

		if (!cellValues.isEmpty() && cellValues.size() == 11) {
			AlertReview alert = new AlertReview();
			alert.setAlertId(cellValues.get(0));
			alert.setScore(cellValues.get(1));
			alert.setFocusType(cellValues.get(2));
			alert.setFocus(cellValues.get(3));
			alert.setCustomerName(cellValues.get(4));
			alert.setCreated(cellValues.get(5));
			alert.setStatus(cellValues.get(6));
			alert.setThresholdName(cellValues.get(7));
			alert.setRiskScore(cellValues.get(8));
			alert.setEmployeeName(cellValues.get(9));
			alert.setCustomerId(cellValues.get(10));
			alertinputs.add(alert);
		}
		return alertinputs;
	}

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}
}
