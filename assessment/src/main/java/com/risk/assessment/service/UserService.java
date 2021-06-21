package com.risk.assessment.service;

import java.util.List;

import com.risk.assessment.dto.ChangePasswordDTO;
import com.risk.assessment.dto.UserDTO;
import com.risk.assessment.excecption.AbstractException;
import com.risk.assessment.excecption.RiskException;
import com.risk.assessment.models.User;

public interface UserService {

	public List<UserDTO> getUserList() throws AbstractException;

	public boolean createUser(UserDTO userDto) throws AbstractException;

	public boolean modifyUser(UserDTO userDto) throws AbstractException;

	public List<UserDTO> deleteUser(List<String> userName) throws AbstractException;

	public UserDTO getUserByUserName(String userName) throws AbstractException;

	public void handleSuccessAuthRequest(User user);

	public void handleFailureAuthRequest(User user);
	
	boolean changePassword(ChangePasswordDTO changePasswordDTO) throws RiskException;

}
