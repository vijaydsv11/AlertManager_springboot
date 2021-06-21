package com.risk.assessment.security.services;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.risk.assessment.dto.UserDTO;
import com.risk.assessment.excecption.AuthenticationException;
import com.risk.assessment.payload.request.JwtAuthenticationRequest;
import com.risk.assessment.payload.response.JwtAuthenticationResponse;
import com.risk.assessment.payload.response.TokenUserDTO;
import com.risk.assessment.security.jwt.JwtUtils;
import com.risk.assessment.service.UserService;
import com.risk.assessment.util.JwtTokenUtil;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Value("${jwt.expiration}")
	private Long expiration;

	@Autowired
	private UserService userService;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	public JwtTokenUtil jwtToken;

	@Override
	public JwtAuthenticationResponse authenticate(JwtAuthenticationRequest authRequest) throws AuthenticationException {

		UserDTO user = getUserByUserName(authRequest.getUsername());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtToken.generateToken(authRequest.getUsername());
		return getAuthResponse(token, user, expiration);
	}
	
	private UserDTO getUserByUserName(String userName) throws AuthenticationException {
		UserDTO user;
		try {
			user = userService.getUserByUserName(userName);
		} catch (Exception e) {
			throw new AuthenticationException(e.getMessage(), e);
		}
		if (user == null) {
			throw AuthenticationException.build("7001");
		}
		return user;
	}
	
	public JwtAuthenticationResponse getAuthResponse(String token, UserDTO user, Long tokenExpiryTime) {
		
		TokenUserDTO userDTO = new TokenUserDTO(user.getUsername(), user.getEmail(), user.getEmpId(), 
				user.getRoleName(), user.getMobileno(), user.getModules());
		return new JwtAuthenticationResponse(token, userDTO, tokenExpiryTime);
	}
	
	public void invalidate(HttpServletRequest request, HttpServletResponse response) {
		String actualToken = readTokenFromRequest(request);
		if (actualToken == null) {
			return;
		}
		Date expiryDate = jwtToken.getTokensExpiryDate(actualToken);
		OffsetDateTime expiryOffset = OffsetDateTime.ofInstant(Instant.ofEpochMilli(expiryDate.getTime()),
				ZoneId.systemDefault());
		if(expiryOffset.isBefore(OffsetDateTime.now())) { 
	        return; 
	    } 
	}
	
	public String readTokenFromRequest(HttpServletRequest request) {
		String token = request.getHeader(JwtTokenUtil.HEADER_STRING);
		if (token == null || !token.startsWith(JwtTokenUtil.HEADER_PREFIX) 
				|| StringUtils.countOccurrencesOf(token, ".") != 2) {
			return null;
		}
		return token.replace(JwtTokenUtil.HEADER_PREFIX, "");
	}
}
