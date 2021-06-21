package com.risk.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.risk.assessment.models.UserComplainceRisk;


@Repository
public interface UserComplainceRiskRepository extends JpaRepository<UserComplainceRisk, Long> {
	
	
}
