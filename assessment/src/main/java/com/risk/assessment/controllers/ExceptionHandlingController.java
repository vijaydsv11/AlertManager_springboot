package com.risk.assessment.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.risk.assessment.excecption.AbstractException;
import com.risk.assessment.excecption.AuthenticationException;
import com.risk.assessment.excecption.ExceptionResponse;
import com.risk.assessment.excecption.MaxUploadSizeExceededException;
import com.risk.assessment.excecption.RiskException;

@ControllerAdvice
@RestController
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(RiskException.class)
	public final ResponseEntity<Object> handleRiskException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(getExResponse(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public final ResponseEntity<Object> handleAuthenticationException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(getExResponse(ex), HttpStatus.UNAUTHORIZED);
	}
	
	  @ExceptionHandler(MaxUploadSizeExceededException.class)
	  public ResponseEntity<Object> handleMaxSizeException(Exception ex, WebRequest request) {
	    return new ResponseEntity<>(getExResponse(ex), HttpStatus.EXPECTATION_FAILED);
	  }
	
	private ExceptionResponse getExResponse(Exception ex) {
		AbstractException absEx = (ex instanceof AbstractException) ? (AbstractException) ex : null;
		String message = "";
		String details = "";
		String code = "";
		if(absEx != null) {
			message = absEx.getMessage();
			details = absEx.toString();
			code = absEx.getErrorCode();
		}
		return new ExceptionResponse(new Date(), message, details, code);
	}

}
