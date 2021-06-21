package com.risk.assessment.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "complaince_risk")
public class ComplainceRisk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "sno")
	private String sno;

	@Column(name = "add_mod_del")
	private String addModDel;

	@Column(name = "comp_officer")
	private String compOfficer;

	@Column(name = "circular_Ref_No")
	private String circularRefNo;

	@Column(name = "circular_date")
	private String circularDate;

	@Column(name = "circular_name")
	private String circularName;

	@Column(name = "regulator")
	private String regulator;

	@Column(name = "risk_score")
	private String riskSource;

	@Column(name = "cluster")
	private String cluster;

	@Column(name = "reg_guidlines")
	private String regGuidlines;

	@Column(name = "control_desc")
	private String controlDesc;

	@Column(name = "risk_owner")
	private String riskOwner;

	@Column(name = "risk_sub_unit")
	private String riskSubUnit;

	@Column(name = "control_owner")
	private String controlOwner;

	@Column(name = "control_sub_unit")
	private String controlSubUnit;

	@Column(name = "prod_func")
	private String prodFunc;

	@Column(name = "prod_name")
	private String prodName;

	@Column(name = "prod_launch_dt")
	private String prodLaunchDt;

	@Column(name = "prod_testing")
	private String prodTesting;

	@Column(name = "prod_testing_dt")
	private String prodTestingDt;

	@Column(name = "board_app_policy")
	private String boardAppPolicy;

	@Column(name = "relevant_policy")
	private String relevantPolicy;

	@Column(name = "process_note")
	private String processNote;

	@Column(name = "like_rating")
	private String likeRating;

	@Column(name = "impact")
	private String impact;

	@Column(name = "monitor_mechanism")
	private String monitorMechanism;

	@Column(name = "control_automation")
	private String controlAutomation;

	@Column(name = "control_effective")
	private String controlEffective;

	@Column(name = "comp_risk")
	private String compRisk;

	@Column(name = "breach")
	private String breach;

	@Column(name = "process_improvement")
	private String processImprovement;

	@Column(name = "details_breach")
	private String detailsBreach;

	@Column(name = "details_process_mprovement")//spelling mistake
	private String detailProcImprovement;

	@Column(name = "timeline")
	private String timeline;

	@Column(name = "spoc")
	private String spoc;

	@Column(name = "manco_member")
	private String mancoMember;
	
	private String status;
	
	@Column(name = "remark_status")
	private String remarkStatus;
	
//	@Column(name = "average_risk")
//	private String avgRisk;

	@Column(name = "datecreated")
	private String datecreated;

}
