package com.risk.assessment.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.risk.assessment.excecption.AuthenticationException;
import com.risk.assessment.payload.request.JwtAuthenticationRequest;
import com.risk.assessment.payload.response.JwtAuthenticationResponse;
import com.risk.assessment.security.services.AuthenticationService;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	AuthenticationService authenticationService;

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	@PostMapping
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody JwtAuthenticationRequest authRequest)
			throws AuthenticationException {
		 logger.trace("This is a TRACE message.");
		    logger.debug("This is a DEBUG message.");
		    logger.info("This is an INFO message.");
		    logger.warn("This is a WARN message.");
		    logger.error("You guessed it, an ERROR message.");
		    logger.trace("This is a TRACE message.");

		JwtAuthenticationResponse response = authenticationService.authenticate(authRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/invalidate")
	public ResponseEntity<Boolean> tokenInvalidate(HttpServletRequest request, HttpServletResponse response) {
		authenticationService.invalidate(request, response);
		return ResponseEntity.ok(true);
	}

}
