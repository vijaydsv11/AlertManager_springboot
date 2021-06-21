package com.risk.assessment.excecption;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RiskException extends AbstractException implements InitializingBean {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Value("${locale.key}")
	private String localeKey;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public RiskException() {
		super();
	}
	
	public RiskException(String message) {
		super(message);
	}
	
	public RiskException(Exception e) {
		super(e);
	}

	public RiskException(String message, Exception e) {
		super(message, e);
	}

	public static RiskException build(String code, Object... messageArgs) {
		RiskException ex = new RiskException();
		ex.buildEx(code, messageArgs);
		return ex;
	}
	
	public static RiskException build(Exception e, String code, Object... messageArgs) {
		RiskException ex = new RiskException(e);
		ex.buildEx(code, messageArgs);
		return ex;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		init(applicationContext, localeKey);
	}
}
