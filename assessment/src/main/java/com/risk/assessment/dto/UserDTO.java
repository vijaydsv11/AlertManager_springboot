package com.risk.assessment.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserDTO {

	private String username;
	private String firstname;
	private String lastname;
	private String middlename;
	private String empId;
	private String department;
	private String mobileno;
	private String email;
	private String roleName;
	private List<String> modules;

}
