package com.risk.assessment.excecption;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ExceptionResponse {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Date date;
	
	private String message;
	private String details;

	private String code;
	
	public ExceptionResponse() {
	}

	public ExceptionResponse(Date date, String message, String details) {
		super();
		this.date = date;
		this.message = message;
		this.details = details;
	}

	public ExceptionResponse(Date date, String message, String details, String code) {
		super();
		this.date = date;
		this.message = message;
		this.details = details;
		this.code = code;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String errorCode) {
		this.code = errorCode;
	}

	@Override
	public String toString() {
		return "ExceptionResponse [date=" + date + ", message=" + message + ", details=" + details + ", code="
				+ code + "]";
	}
	
}
