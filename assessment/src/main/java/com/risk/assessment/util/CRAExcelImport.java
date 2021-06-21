package com.risk.assessment.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.risk.assessment.models.ComplainceRisk;

/**
 * 
 * @author vijay ganesh
 *
 */
public class CRAExcelImport {

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static List<ComplainceRisk> getExcelDataAsList(InputStream is) {

		Workbook workbook = null;
		DataFormatter dataFormatter = new DataFormatter();
		List<ComplainceRisk> craInputs = new ArrayList<ComplainceRisk>();

		try {
			workbook = new XSSFWorkbook(is);
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}

		Sheet sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			if (row.getRowNum() != 0) {
				setCellValueToAlertInput(getCellValue(row, dataFormatter), craInputs);
			}
		}
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return craInputs;
	}

	private static List<String> getCellValue(Row row, DataFormatter dataFormatter) {

		List<String> cellValues = new ArrayList<String>();
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cellValues.add(dataFormatter.formatCellValue(cell));
		}
		return cellValues;
	}

	private static List<ComplainceRisk> setCellValueToAlertInput(List<String> cellValues, List<ComplainceRisk> craInputs) {

		if (!cellValues.isEmpty() && cellValues.size() == 36) {
			ComplainceRisk userRisk = new ComplainceRisk();
			userRisk.setSno(cellValues.get(0));
			userRisk.setAddModDel(cellValues.get(1));
			userRisk.setCompOfficer(cellValues.get(2));
			userRisk.setCircularRefNo(cellValues.get(3));
			userRisk.setCircularDate(cellValues.get(4));
			userRisk.setCircularName(cellValues.get(5));
			userRisk.setRegulator(cellValues.get(6));
			userRisk.setRiskSource(cellValues.get(7));
			userRisk.setCluster(cellValues.get(8));
			userRisk.setRegGuidlines(cellValues.get(9));
			userRisk.setControlDesc(cellValues.get(10));
			userRisk.setRiskOwner(cellValues.get(11));
			userRisk.setRiskSubUnit(cellValues.get(12));
			userRisk.setControlOwner(cellValues.get(13));
			userRisk.setControlSubUnit(cellValues.get(14));
			userRisk.setProdFunc(cellValues.get(15));
			userRisk.setProdName(cellValues.get(16));
			userRisk.setProdLaunchDt(cellValues.get(17));
			userRisk.setProdTesting(cellValues.get(18));
			userRisk.setProdTestingDt(cellValues.get(19));
			userRisk.setBoardAppPolicy(cellValues.get(20));
			userRisk.setRelevantPolicy(cellValues.get(21));
			userRisk.setProcessNote(cellValues.get(22));
			userRisk.setStatus("NO");
			
			//Calculation Max value of below 3 into impact as ComplianceRisk
			Long compRisk = null;
			String ctrlEffect = cellValues.get(27);
			String montiroMech = cellValues.get(25);
			String ctrlAuto = cellValues.get(26);
			String impactStr = cellValues.get(24);
			Long impact = impactStr != "" ? Long.valueOf(impactStr) : null;
			
			List<String> list =  Arrays.asList(ctrlEffect,montiroMech,ctrlAuto);  
			List<String> sortedList=list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());  
			Long maxValue = sortedList.get(0) != "" ? Long.valueOf(sortedList.get(0)) : null;
			
			if (maxValue != null && impact != null) {
				compRisk = maxValue * impact;
			} else if (maxValue == null  && impact != null)  {
				compRisk = impact;
			} else if (impact == null  && maxValue != null)  {
				compRisk = maxValue;
			}
			
			userRisk.setLikeRating(maxValue  != null ? String.valueOf(maxValue) : null);
			userRisk.setImpact(impactStr);
			userRisk.setMonitorMechanism(montiroMech);
			userRisk.setControlAutomation(ctrlAuto);
			userRisk.setControlEffective(ctrlEffect);
			userRisk.setCompRisk(compRisk != null ? String.valueOf(compRisk) : null);
			
			
			userRisk.setBreach(cellValues.get(29));
			userRisk.setProcessImprovement(cellValues.get(30));
			userRisk.setDetailsBreach(cellValues.get(31));
			userRisk.setDetailProcImprovement(cellValues.get(32));
			userRisk.setTimeline(cellValues.get(33));
			userRisk.setSpoc(cellValues.get(34));
			userRisk.setMancoMember(cellValues.get(35));
			craInputs.add(userRisk);
		}
		return craInputs;
	}

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}
}
