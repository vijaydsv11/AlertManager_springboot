package com.risk.assessment.security.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.risk.assessment.excecption.AuthenticationException;
import com.risk.assessment.payload.request.JwtAuthenticationRequest;
import com.risk.assessment.payload.response.JwtAuthenticationResponse;

public interface AuthenticationService {

	JwtAuthenticationResponse authenticate(JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException;
	
	public void invalidate(HttpServletRequest request, HttpServletResponse response);

}
