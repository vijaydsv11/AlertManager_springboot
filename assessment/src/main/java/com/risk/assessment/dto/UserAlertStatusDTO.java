package com.risk.assessment.dto;

import lombok.Data;

@Data
public class UserAlertStatusDTO {

	private Long id;
	private String alertId;
	private String currentStatus;

}
