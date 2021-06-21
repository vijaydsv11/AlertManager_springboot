package com.risk.assessment.excecption;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;

public abstract class AbstractException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static ResourceBundleMessageSource messageSource;
	private static Locale locale;
	
	private String errorCode;
	private String message;
	
	public AbstractException() {
		super();
	}
	
	public AbstractException(String message) {
		super(message);
		this.message = message;
	}
	
	public AbstractException(Exception e) {
		super(e);
	}
	
	public AbstractException(String message, Exception e) {
		super(message, e);
		this.message = message;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void buildEx(String code, Object... messageArgs) {
		if(code == null) {
			return;
		}
		try {
			String msg = messageSource.getMessage(code, messageArgs, locale);
			this.setMessage(msg);
		} catch (org.springframework.context.NoSuchMessageException e) {
			// Ignore
		}
		this.setErrorCode(code);
	}
	
	protected void init(ApplicationContext context, String localeKey) {
		if(messageSource == null) {
			messageSource = context.getBean(ResourceBundleMessageSource.class);
		}
		if(locale == null) {
			locale = new Locale(localeKey);
		}
	}

}
