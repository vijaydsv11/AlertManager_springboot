package com.risk.assessment.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CraDTO {

	private Long id;
	private String sNo;
	private String remarks;
	private String circularRefNo;
	private String circularDate;
	private String regGuidelines;
	private String controlDesc;
	private String breach;
	private String processImprovement;
	private String prodName;
	private String policyDetails;
	private String relavantPolicy;
	private String processNote;
	private Long controlEffectiveness;
	private Long monitorMechanism;
	private Long controlAutomation;
	private Long regulatoryImpact;
	private Long regulationRisk;
	private Double averageRisk;
	private String status;
	

	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class assesmentDto {
		private long totalPage;
		private Long totalRecordCount;
		List<CraDTO> cras = new ArrayList<CraDTO>();
	}
}


