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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.risk.assessment.dto.CraDTO;
import com.risk.assessment.dto.CraDTO.assesmentDto;
import com.risk.assessment.excecption.AbstractException;
import com.risk.assessment.payload.response.MessageResponse;
import com.risk.assessment.service.AlertService;
import com.risk.assessment.service.CRAService;
import com.risk.assessment.util.CRAExcelExport;

@RestController
@RequestMapping("/CRA")
public class CRAController {

	@Autowired
	public AlertService alertService;

	@Autowired
	public CRAService craService;

	@PostMapping("/upload")
	public ResponseEntity<MessageResponse> uploadCRAFile(@RequestParam("file") MultipartFile file)
			throws AbstractException {
		return ResponseEntity.status(HttpStatus.OK).body(craService.importCRAToDb(file));
	}

	@GetMapping("/export")
	public void exportCRAToExcel(HttpServletResponse response) throws IOException, AbstractException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=CRA - Automation_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		CRAExcelExport excelExporter = new CRAExcelExport(craService.exportCRA());
		excelExporter.export(response);
	}

	@GetMapping(produces = "application/json")
	public assesmentDto getAllCRA(@RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "ALL") String department) throws AbstractException {
		return craService.getAllCRA(pageNum, pageSize, department);
	}
	
	@GetMapping(value = "/pending" , produces = "application/json")
	public assesmentDto getPendingCRA(@RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "10") int pageSize) throws AbstractException {
		return craService.getPendingCRA(pageNum, pageSize);
	}
	
	@GetMapping(value = "/department" , produces = "application/json")
	public Set<String> getAllDepartment() throws AbstractException {
		return craService.getAllDepartment();
	}

	@PostMapping
	public assesmentDto createCRA(@RequestBody CraDTO craDto) throws AbstractException {
		return craService.createCRA(craDto);
	}

	@PutMapping
	public boolean updateCRA(@RequestBody CraDTO craDto) throws AbstractException {
		return craService.updateCRA(craDto);
	}

	@PostMapping(value = "/approve" , produces = "application/json")
	public assesmentDto approveCRA(@RequestBody List<Long> craIds) throws AbstractException {
		return craService.approveCRA(craIds);
	}
	
	@DeleteMapping(produces = "application/json")
	public assesmentDto deleteCRA(@RequestBody List<Long> craIds) throws AbstractException {
		return craService.deleteCRA(craIds);
	}
}
