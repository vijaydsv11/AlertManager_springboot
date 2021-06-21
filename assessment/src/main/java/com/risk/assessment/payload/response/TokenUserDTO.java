package com.risk.assessment.payload.response;

import java.io.Serializable;
import java.util.List;

public class TokenUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	private String email;
	private String empId;
	private String roleName;
	private String phoneNumber;
	private List<String> modules;

	public TokenUserDTO(String name, String email, String empId, String roleName,
			String phoneNumber, List<String> modules) {
		this.userName = name;
		this.email = email;
		this.empId = empId;
		this.roleName = roleName;
		this.phoneNumber = phoneNumber;
		this.modules = modules;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}
}
