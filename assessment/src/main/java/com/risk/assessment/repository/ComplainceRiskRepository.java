package com.risk.assessment.repository;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.risk.assessment.models.ComplainceRisk;


@Repository
public interface ComplainceRiskRepository extends PagingAndSortingRepository<ComplainceRisk, Long> {
	
	
	public Page<ComplainceRisk> findAllByRemarkStatusAndStatus(Pageable pageable, String remark, String status);
	
	public Page<ComplainceRisk> findAllByRiskOwner(Pageable pageable, String riskOwner);
	
	@Query("SELECT t.riskOwner FROM ComplainceRisk t")
	public Set<String> findAllRiskOwner();
	
	@Query("SELECT AVG(t.compRisk) as avgRisk  FROM ComplainceRisk t where t.riskOwner =:riskOwner group by t.riskOwner")
	public Double findByAvgCompRisk(String riskOwner);
	
}
