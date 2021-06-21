package com.risk.assessment.excecption;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

public class MaxUploadSizeExceededException extends AbstractException implements InitializingBean {

private static final long serialVersionUID = 1L;
	
	@Value("${locale.key}")
	private String localeKey;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public MaxUploadSizeExceededException() {
		super();
	}
	
	public MaxUploadSizeExceededException(Exception e) {
		super(e);
	}
	
	public MaxUploadSizeExceededException(String message) {
        super(message);
    }
	
	public MaxUploadSizeExceededException(String message, Exception cause) {
        super(message, cause);
    }
	
	public static AuthenticationException build(String code, Object... messageArgs) {
		AuthenticationException ex = new AuthenticationException();
		ex.buildEx(code, messageArgs);
		return ex;
	}
	
	public static AuthenticationException build(Exception e, String code, Object... messageArgs) {
		AuthenticationException ex = new AuthenticationException(e);
		ex.buildEx(code, messageArgs);
		return ex;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init(applicationContext, localeKey);
	}
}
