package com.risk.assessment.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.risk.assessment.dto.ChangePasswordDTO;
import com.risk.assessment.dto.UserDTO;
import com.risk.assessment.excecption.AbstractException;
import com.risk.assessment.excecption.RiskException;
import com.risk.assessment.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping(produces = "application/json")
	public List<UserDTO> getUsersList() throws AbstractException {
		return userService.getUserList();
	}

	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDto) throws AbstractException {
		return ResponseEntity.ok(userService.createUser(userDto));
	}

	@PutMapping(consumes = "application/json", produces = "application/json")
	public boolean modifyUser(@Valid @RequestBody UserDTO userDto) throws AbstractException {
		return userService.modifyUser(userDto);
	}

	@DeleteMapping(consumes = "application/json", produces = "application/json")
	public List<UserDTO> deleteUser(@RequestBody List<String> userNames) throws AbstractException {
		return userService.deleteUser(userNames);
	}
	
	@PutMapping(value = "/changepassword", consumes = "application/json", produces = "application/json")
	public boolean changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) throws RiskException {
		return userService.changePassword(changePasswordDTO);
	}
}
