package com.risk.assessment.util;

public enum CraEnum {
	
	High_Regulatory(1);

	private Integer value;
	
	CraEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	
}
