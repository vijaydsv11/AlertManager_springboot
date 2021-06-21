package com.risk.assessment.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.risk.assessment.dto.ChangePasswordDTO;
import com.risk.assessment.dto.UserDTO;
import com.risk.assessment.excecption.AbstractException;
import com.risk.assessment.excecption.RiskException;
import com.risk.assessment.models.Module;
import com.risk.assessment.models.Role;
import com.risk.assessment.models.User;
import com.risk.assessment.models.UserAlertReview;
import com.risk.assessment.models.UserModule;
import com.risk.assessment.repository.ModuleRepository;
import com.risk.assessment.repository.RoleRepository;
import com.risk.assessment.repository.UserAlertRepository;
import com.risk.assessment.repository.UserModuleRepository;
import com.risk.assessment.repository.UserRepository;
import com.risk.assessment.util.RoleEnum;

@Component
public class UserServiceImpl implements UserService, InitializingBean {
	
	static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepo;

	@Value("${superadmin.username}")
	public String username;

	@Value("${superadmin.password}")
	public String superAdminPassword;
	
	@Value("${user.default.password}")
	public String userPassword;

	@Value("${superadmin.firstname}")
	public String firstName;

	@Value("${superadmin.email}")
	public String email;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserModuleRepository userModRepo;
	
	@Autowired
	private UserAlertRepository userAlertRepo;
	
	@Autowired
	private ModuleRepository modRepo;

	private final static String SUPER_ADMIN = "-1";

	public UserDTO getUserByUserName(String userName) throws AbstractException {

		Optional<User> user = userRepo.findByUsername(userName);
		if (user != null && user.isPresent()) {
			return convertEntityToDTO(user.get());
		}
		return null;
	}

	@Override
	public List<UserDTO> getUserList() throws AbstractException {

		List<UserDTO> usersDtos = new ArrayList<UserDTO>();
		List<User> users = userRepo.findAll(Sort.by("firstname"));
		for (User user : users) {
			if (!RoleEnum.SUPER_ADMIN.name().equals(user.getRole().getName()) ) {
				usersDtos.add(convertEntityToDTO(user));
			}
		}
		return usersDtos;
	}

	@Override
	public boolean createUser(UserDTO userDto) throws AbstractException {

		String userName = userDto.getUsername();
		if (getUserByUserName(userName) != null) {
			throw RiskException.build("1001", userName);
		} else {
			return saveUser(new User(), userDto, false);
		}
	}

	@Override
	public boolean modifyUser(UserDTO userDto) throws AbstractException {
		
		Optional<User> user = userRepo.findByUsername(userDto.getUsername());
		if (user.isPresent()) {
			saveUser(user.get(), userDto, true);
		} else {
			throw RiskException.build("4009");
		}
		return true;
	}

	@Override
	public List<UserDTO> deleteUser(List<String> userNames) throws AbstractException {

		for (String userName : userNames) {
			Optional<User> userOpt = userRepo.findByUsername(userName);
			if (userOpt.isPresent()) {
				User user = userOpt.get();
				List<UserModule> modules = userModRepo.findAllByUser(user);
				List<UserAlertReview> userAlert = userAlertRepo.findAllByUser(user);
				userAlertRepo.deleteAll(userAlert);
				userModRepo.deleteAll(modules);
				userRepo.delete(user);
			}
		}
		return getUserList();
	}
	
	private boolean saveUser(User user, UserDTO userDto, boolean isModify ) throws AbstractException{
		
		String empId = userDto.getEmpId();
		if (SUPER_ADMIN.equals(empId)) {
			throw RiskException.build("1002", empId);
		}
		if (userDto.getModules() == null) {
			throw RiskException.build("1003", empId);
		}
		if (!isModify) {
			user.setUsername(userDto.getUsername());
			user.setLastPasswordReset(OffsetDateTime.now());
			user.setInvalidAuthRequestCount(0);
			user.setPassword(passwordEncoder.encode(userPassword));
			Optional<Role> roleOpt = roleRepo.findByName(userDto.getRoleName());
			if (roleOpt.isPresent()) {
				user.setRole(roleOpt.get());
			}
		} else {
			List<UserModule> userModules = userModRepo.findAllByUser(user);
			if (!userModules.isEmpty()) {
				userModRepo.deleteAll(userModules);
			}
		}
		
		user.setEmpId((userDto.getEmpId()));
		user.setEmail(userDto.getEmail());
		user.setDepartment(userDto.getDepartment());
		user.setFirstname(userDto.getFirstname());
		user.setLastname(userDto.getLastname());
		user.setMiddlename(userDto.getMiddlename());
		user.setMobileno(userDto.getMobileno());
		userRepo.save(user);
		
		for (String modName : userDto.getModules() ) {
			Module module = modRepo.findByName(modName);
			UserModule usrMod = new UserModule();
			usrMod.setModule(module);
			usrMod.setUser(user);
			userModRepo.save(usrMod);
		}
		return true;
	}
	
	private UserDTO convertEntityToDTO(User user) {

		UserDTO userDto = new UserDTO();
		userDto.setUsername(user.getUsername());
		userDto.setEmail(user.getEmail());
		userDto.setFirstname(user.getFirstname());
		userDto.setLastname(user.getLastname() != null ? user.getLastname() : "");
		userDto.setEmpId(user.getEmpId());
		userDto.setMiddlename(user.getMiddlename());
		userDto.setDepartment(user.getDepartment());
		userDto.setMobileno(user.getMobileno());
		userDto.setRoleName(user.getRole().getName());
		List<String> modules = new ArrayList<String>();
		for (Module module : userModRepo.findModuleByUser(user)) {
			modules.add(module.getName());
		}
		userDto.setModules(modules);
		return userDto;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {

		Optional<User> userOpt = userRepo.findByEmpId(SUPER_ADMIN);
		if (!userOpt.isPresent()) {
			userOpt = userRepo.findByUsername("admin");
		}
		
		if (!userOpt.isPresent()) {
			User user = new User();
			user.setUsername(username);
			user.setPassword(passwordEncoder.encode(superAdminPassword));
			user.setFirstname(firstName);
			user.setEmail(email);
			user.setEmpId(SUPER_ADMIN);
			user.setInvalidAuthRequestCount(0);
			user.setLastPasswordReset(OffsetDateTime.now());
			Optional<Role> roleOpt = roleRepo.findByName(RoleEnum.SUPER_ADMIN.name());
			if (roleOpt.isPresent()) {
				user.setRole(roleOpt.get());
			}
			userRepo.save(user);
			List<Module> modules = modRepo.findAll();
			for (Module module : modules ) {
				UserModule usrMod = new UserModule();
				usrMod.setModule(module);
				usrMod.setUser(user);
				userModRepo.save(usrMod);
			}
		}
	}
	
	@Override
	public void handleSuccessAuthRequest(User user) {

		if (user.getInvalidAuthRequestCount() != null && user.getInvalidAuthRequestCount() > 0) {
			user.setInvalidAuthRequestCount(0);
			userRepo.save(user);
		}
	}

	public void handleFailureAuthRequest(User user) {

	}

	@Override
	public boolean changePassword(ChangePasswordDTO changePasswordDTO) throws RiskException {
	
		log.debug("Changing user password...");
		Optional<User> dbUserOpt = userRepo.findByUsername(changePasswordDTO.getUsername());
		log.trace("User Name : {}", changePasswordDTO.getUsername());

		if (dbUserOpt.isPresent()) {
			User user = dbUserOpt.get();
			if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
				throw RiskException.build("4021");
			}
			user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
			user.setLastPasswordReset(OffsetDateTime.now());
			userRepo.save(user);
			return true;
		}
		log.debug("User not found for given user name");
		return false;
	}
}
