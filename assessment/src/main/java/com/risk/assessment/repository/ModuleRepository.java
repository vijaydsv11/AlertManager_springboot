package com.risk.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.risk.assessment.models.Module;


@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
	
	Module findByName(String mouduleName);
	
}
