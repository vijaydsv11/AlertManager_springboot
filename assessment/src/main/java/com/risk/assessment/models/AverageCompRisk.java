package com.risk.assessment.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class AverageCompRisk implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Double avgRisk;
	
	private String compRisk;
}
