package com.risk.assessment.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.risk.assessment.dto.AlertReviewDTO;
import com.risk.assessment.dto.UserAlertStatusDTO;
import com.risk.assessment.excecption.AbstractException;
import com.risk.assessment.models.AlertReview;
import com.risk.assessment.models.UserAlertReview;
import com.risk.assessment.payload.response.MessageResponse;

public interface AlertService {

	public MessageResponse saveAlertDetailsFromExcelToDb(MultipartFile file) throws AbstractException;

	public List<AlertReview> getAllAlerts(int page, int pageSize) throws AbstractException;

	public AlertReviewDTO getAllUnAssignedAlerts(int pageNum, int pageSize) throws AbstractException;

	public List<UserAlertReview> findAlertsByUser(String userName) throws AbstractException;

	public List<AlertReview> getAlertsByCustomerId(String customerId) throws AbstractException;

	public List<UserAlertReview> exportAlertsByTeam() throws AbstractException;

	public AlertReviewDTO assignAlertsToUser(List<Long> alertIds, String username) throws AbstractException;

	public AlertReviewDTO updateAlertStatusByUser(List<UserAlertStatusDTO> statusDtos) throws AbstractException;

	public AlertReviewDTO getAllAlertsByUser(String userName, int pageNum, int pageSize) throws AbstractException;

	public Set<String> getAllCustomerId() throws AbstractException;

}
