package com.risk.assessment.models;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
@Table(name = "user_data")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@Column(name = "user_firstname")
	private String firstname;
	
	@Column(name = "user_lastname")
	private String lastname;
	
	@Column(name = "user_middlename")
	private String middlename;
	
	@Column(name = "employee_id")
	private String empId;
	
	@Column(name = "department")
	private String department;
	
	@Column(name = "phone_no")
	private String mobileno;
	
	@Column(name = "datecreated")
	private OffsetDateTime dateCreated;
	
	@Column(name = "user_email")
	private String email;
	
	@Column(name = "invalid_auth_req_count")
	private Integer invalidAuthRequestCount;

	@Column(name = "last_password_reset")
	private OffsetDateTime lastPasswordReset;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "role_id")
	private Role role;


}
