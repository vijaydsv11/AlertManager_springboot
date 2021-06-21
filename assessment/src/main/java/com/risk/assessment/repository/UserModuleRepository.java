package com.risk.assessment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.risk.assessment.models.Module;
import com.risk.assessment.models.User;
import com.risk.assessment.models.UserModule;


@Repository
public interface UserModuleRepository extends JpaRepository<UserModule, Long> {
	
	public List<UserModule> findAllByUser(User user);
	
	public UserModule findAllByUserAndModule(User user, Module module);
	
	@Query("SELECT t.module FROM UserModule t WHERE t.user = :user")
	List<Module> findModuleByUser(@Param("user") User user);
}
