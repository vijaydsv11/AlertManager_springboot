package com.risk.assessment.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.risk.assessment.dto.AlertReviewDTO;
import com.risk.assessment.dto.UserAlertStatusDTO;
import com.risk.assessment.excecption.AbstractException;
import com.risk.assessment.excecption.RiskException;
import com.risk.assessment.models.AlertReview;
import com.risk.assessment.models.User;
import com.risk.assessment.models.UserAlertReview;
import com.risk.assessment.payload.response.MessageResponse;
import com.risk.assessment.repository.AlertRepository;
import com.risk.assessment.repository.UserAlertRepository;
import com.risk.assessment.repository.UserRepository;
import com.risk.assessment.util.AlertExcelExport;
import com.risk.assessment.util.AlertExcelImport;

@Service
public class AlertServiceImpl implements AlertService {

	@Autowired
	AlertRepository alertRepo;

	@Autowired
	UserAlertRepository usrAlertRepo;

	@Autowired
	UserRepository usrRepo;

	@Value("${upload.success}")
	private String uploadSuccess;

	@Value("${pageSize}")
	private int defaultPageSize;

	@Value("${alert.downloadFilePath}")
	private String downloadFilePath;

	private static final int pageNum = 1;

	public MessageResponse saveAlertDetailsFromExcelToDb(MultipartFile file) throws AbstractException {

		if (AlertExcelImport.hasExcelFormat(file)) {
			List<UserAlertReview> usrAlerts = getAllAlertsWithOutPagination();
			if (!usrAlerts.isEmpty()) {
				AlertExcelExport excelExporter = new AlertExcelExport(new ArrayList<UserAlertReview>());
				try {
					excelExporter.exportToFolderWithUnAssignedUser(downloadFilePath, usrAlerts);
					excelExporter.exportToFolder(downloadFilePath, exportAlertsByTeam());
				} catch (IOException e1) {
					throw RiskException.build("1009");
				}
			}
			
			try {
				InputStream inputFile = file.getInputStream();
				List<AlertReview> alerts = AlertExcelImport.getExcelDataAsList(inputFile);
				if (!alerts.isEmpty()) {
					usrAlertRepo.deleteAll();
					alertRepo.deleteAll();
					alertRepo.saveAll(alerts);
				}
			} catch (IOException e) {
				throw RiskException.build("1008");
			}
			return new MessageResponse(uploadSuccess + file.getOriginalFilename());
		} else {
			throw RiskException.build("1006");
		}
	}

	public List<UserAlertReview> getAllAlertsWithOutPagination() throws AbstractException {
		Sort sort = Sort.by("riskScore").and(Sort.by("score").descending()).and(Sort.by("created").ascending());
		List<AlertReview> alertReview = IteratorUtils.toList(alertRepo.findAll(sort).iterator());
		List<UserAlertReview> usrAlerts = new ArrayList<UserAlertReview>();

		for (AlertReview alert : alertReview) {
			UserAlertReview userAlert = new UserAlertReview();
			userAlert.setAlert(alert);
			usrAlerts.add(userAlert);
		}
		return usrAlerts;
	}

	@Override
	public List<AlertReview> getAllAlerts(int pageNum, int pageSize) throws AbstractException {
		Pageable paging = PageRequest.of(pageNum, pageSize,
				Sort.by("riskScore").and(Sort.by("score").descending()).and(Sort.by("created").ascending()));
		Page<AlertReview> pagedResult = alertRepo.findAll(paging);
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<AlertReview>();
		}
	}

	public AlertReviewDTO getAllUnAssignedAlerts(int pageNum, int pageSize) throws AbstractException {

		AlertReviewDTO alertDto = new AlertReviewDTO();
		Pageable paging = PageRequest.of(pageNum - 1, pageSize,
				Sort.by("riskScore").and(Sort.by("score").descending()).and(Sort.by("created").ascending()));

		Page<AlertReview> pagedResult = alertRepo.findAllAlert(paging);
		if (pagedResult.hasContent()) {
			alertDto.setTotalRecordCount(pagedResult.getTotalElements());
			alertDto.setTotalPage(pagedResult.getTotalPages());
			alertDto.setRemainingAlertAssignToUsr(pagedResult.getContent());
		}
		return alertDto;
	}

	public List<AlertReview> getAlertsByCustomerId(String customerId) throws AbstractException {

		Sort sort = Sort.by("riskScore").and(Sort.by("score").descending()).and(Sort.by("created").ascending());
		return alertRepo.findAllByCustomerId(customerId, sort);
	}

	public AlertReviewDTO assignAlertsToUser(List<Long> alertIds, String username) throws AbstractException {

		for (Long id : alertIds) {
			Optional<AlertReview> alertOpt = alertRepo.findById(id);
			Optional<User> userOpt = usrRepo.findByUsername(username);

			if (alertOpt.isPresent() && userOpt.isPresent()) {
				User user = userOpt.get();
				AlertReview alert = alertOpt.get();
				UserAlertReview usrAlert = usrAlertRepo.findByUserAndAlert(user, alertOpt.get());
				alert.setEmployeeName(username);
				if (usrAlert == null) {
					usrAlert = new UserAlertReview();
					usrAlert.setAlert(alert);
					usrAlert.setUser(user);
					usrAlert.setStartTime(OffsetDateTime.now());
					usrAlert.setProcessedBy(user.getUsername());
					usrAlertRepo.save(usrAlert);
				}
			}
		}
		return getAllUnAssignedAlerts(pageNum, defaultPageSize);
	}

	public AlertReviewDTO updateAlertStatusByUser(List<UserAlertStatusDTO> statusDtos) throws AbstractException {

		for (UserAlertStatusDTO statusDto : statusDtos) {
			Optional<AlertReview> alertOpt = alertRepo.findById(statusDto.getId());
			if (alertOpt.isPresent()) {
				List<UserAlertReview> userAlerts = usrAlertRepo.findAllByAlert(alertOpt.get());
				if (!userAlerts.isEmpty()) {
					for (UserAlertReview alertRev : userAlerts) {
						alertRev.setStatus(statusDto.getCurrentStatus());
						alertRev.getAlert().setStatus(statusDto.getCurrentStatus());
						alertRev.setEndTime(OffsetDateTime.now());
						usrAlertRepo.save(alertRev);
					}
				}
			}
		}
		return getAllUnAssignedAlerts(pageNum, defaultPageSize);
	}

	public List<UserAlertReview> exportAlertsByTeam() throws AbstractException {
		return IteratorUtils.toList(usrAlertRepo.findAll().iterator());
	}

	public List<UserAlertReview> findAlertsByUser(String userName) throws AbstractException {

		Optional<User> user = usrRepo.findByUsername(userName);
		if (user.isPresent()) {
			return usrAlertRepo.findAllByUser(user.get());
		}
		return new ArrayList<UserAlertReview>();
	}

	public AlertReviewDTO getAllAlertsByUser(String userName, int pageNum, int pageSize) throws AbstractException {
		AlertReviewDTO alertDto = new AlertReviewDTO();
		Optional<User> user = usrRepo.findByUsername(userName);
		Pageable paging = PageRequest.of(pageNum - 1, pageSize, Sort.by("alert.riskScore")
				.and(Sort.by("alert.score").descending()).and(Sort.by("alert.created").ascending()));

		Page<AlertReview> pagedResult = usrAlertRepo.findAllAlertByUser(user.get(), paging);
		if (pagedResult.hasContent()) {
			alertDto.setTotalRecordCount(pagedResult.getTotalElements());
			alertDto.setTotalPage(pagedResult.getTotalPages());
			alertDto.setAssignedAlerts(pagedResult.getContent());
		}
		return alertDto;
	}

	public Set<String> getAllCustomerId() throws AbstractException {
		return alertRepo.findAllCustomerId();
	}
}
