package com.risk.assessment.payload.response;

public class JwtAuthenticationResponse {

	private String token;
	private Long tokenExpiryTime;
	private final TokenUserDTO user;

	public JwtAuthenticationResponse(String token, TokenUserDTO user, Long tokenExpiryTime) {
		this.token = token;
		this.user = user;
		this.tokenExpiryTime = tokenExpiryTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getTokenExpiryTime() {
		return tokenExpiryTime;
	}

	public void setTokenExpiryTime(Long tokenExpiryTime) {
		this.tokenExpiryTime = tokenExpiryTime;
	}

	public TokenUserDTO getUser() {
		return user;
	}

}
