package com.risk.assessment.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.risk.assessment.models.AlertReview;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AlertReviewDTO {

	List<AlertReview> assignedAlerts;

	List<AlertReview> remainingAlertAssignToUsr;

	private long totalPage;

	private Long totalRecordCount;

}
