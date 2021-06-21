package com.risk.assessment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.risk.assessment.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmpId(String empId);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	
}
