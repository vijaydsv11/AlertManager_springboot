package com.risk.assessment.models;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "alert_review")
public class AlertReview implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "alert_review_id")
	private String alertId;

	@Column(name = "score")
	private String score;

	@Column(name = "focus_type")
	private String focusType;

	@Column(name = "focus")
	private String focus;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "created")
	private String created;

	@Column(name = "status")
	private String status;

	@Column(name = "threshold_name")
	private String thresholdName;

	@Column(name = "risk_score")
	private String riskScore;

	@Column(name = "employee_name")
	private String employeeName;

	@Column(name = "customer_id")
	private String customerId;
	
	@Column(name = "datecreated")
	private OffsetDateTime dateCreated;

}
