package com.risk.assessment.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.risk.assessment.dto.AlertReviewDTO;
import com.risk.assessment.dto.UserAlertStatusDTO;
import com.risk.assessment.excecption.AbstractException;
import com.risk.assessment.models.AlertReview;
import com.risk.assessment.models.UserAlertReview;
import com.risk.assessment.payload.response.MessageResponse;
import com.risk.assessment.service.AlertService;
import com.risk.assessment.util.AlertExcelExport;

@RestController
@RequestMapping("/alert")
public class AlertController {

	@Autowired
	public AlertService alertService;

	@PostMapping("/upload")
	public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file)
			throws AbstractException {
		return ResponseEntity.status(HttpStatus.OK).body(alertService.saveAlertDetailsFromExcelToDb(file));
	}

	@GetMapping("/export")
	public void exportAllAlertsToExcel(HttpServletResponse response) throws IOException, AbstractException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Alert Review_Teams_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		AlertExcelExport excelExporter = new AlertExcelExport(alertService.exportAlertsByTeam());
		excelExporter.export(response);
	}

	@GetMapping("/export/{username}/user")
	public void exportAlertToExcelByUser(HttpServletResponse response,
			@PathVariable(value = "username", required = true) String userName) throws IOException, AbstractException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Alert Review_users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<UserAlertReview> alerts = alertService.findAlertsByUser(userName);
		AlertExcelExport excelExporter = new AlertExcelExport(alerts);
		excelExporter.export(response);
	}

	@GetMapping(value = "/user", produces = "application/json")
	public AlertReviewDTO getAllAlertByUser(@RequestParam String user,
			@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
			throws AbstractException {
		return alertService.getAllAlertsByUser(user, pageNum, pageSize);
	}

	@GetMapping(produces = "application/json")
	public AlertReviewDTO getAllUnAssignedAlerts(@RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "10") int pageSize) throws AbstractException {
		return alertService.getAllUnAssignedAlerts(pageNum, pageSize);
	}

	@GetMapping(value = "/customer", produces = "application/json")
	public List<AlertReview> getAlertsByCustomerId(
			@RequestParam String customerId) throws AbstractException {
		return alertService.getAlertsByCustomerId(customerId);
	}

	@PostMapping(value = "/{username}/assign", consumes = "application/json", produces = "application/json")
	public AlertReviewDTO assignAlertsToUser(@PathVariable(value = "username") String username,
			@RequestBody List<Long> alertIds) throws AbstractException {
		return alertService.assignAlertsToUser(alertIds, username);
	}

	@PutMapping(consumes = "application/json", produces = "application/json")
	public AlertReviewDTO updateAlertDetailsByUser(@RequestBody List<UserAlertStatusDTO> statusDtos)
			throws AbstractException {
		return alertService.updateAlertStatusByUser(statusDtos);
	}

	@GetMapping(value = "/customerId", produces = "application/json")
	public Set<String> getAllCustomerId() throws AbstractException {
		return alertService.getAllCustomerId();
	}
}
