package com.risk.assessment.excecption;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationException extends AbstractException implements InitializingBean {
   
	private static final long serialVersionUID = 1L;
	
	@Value("${locale.key}")
	private String localeKey;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public AuthenticationException() {
		super();
	}
	
	public AuthenticationException(Exception e) {
		super(e);
	}
	
	public AuthenticationException(String message) {
        super(message);
    }
	
	public AuthenticationException(String message, Exception cause) {
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
