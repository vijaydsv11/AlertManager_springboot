package com.risk.assessment.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -3301605591108950415L;

	public static final String HEADER_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	
	private Clock clock = DefaultClock.INSTANCE;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, username);
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	private Date calculateExpirationDate(Date createdDate) {
		return new Date(createdDate.getTime() + expiration * 1000);
	}

	public boolean validateToken(String token, Date lastPasswordReset) {
		Claims claims = getClaims(token);
		final Date created = claims.getIssuedAt();
		final Date tokenExpiration = claims.getExpiration();
		return (!isExpired(tokenExpiration) && !isCreatedBeforeLastPasswordReset(created, lastPasswordReset));
	}

	private boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null) ? created.before(lastPasswordReset) : false;
	}

	private Boolean isExpired(Date expiration) {
		return expiration.before(clock.now());
	}

	public String getUserFromToken(String token) {
		return getClaims(token).getSubject();
	}

	public Date getTokensExpiryDate(String token) {
		return getClaims(token).getExpiration();
	}
	
	private Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getClaims(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }
}
