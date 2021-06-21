package com.risk.assessment.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.risk.assessment.models.AlertReview;


@Repository
public interface AlertRepository extends PagingAndSortingRepository<AlertReview, Long> {
	
	public AlertReview findByAlertId(String alertId);
	
	public List<AlertReview> findAllByCustomerId(String customerId, Sort sort);
	
	@Query("SELECT t.customerId FROM AlertReview t")
	public Set<String> findAllCustomerId();
	
	@Query("select t from AlertReview t where t.id not in (select ua.alert from UserAlertReview ua)")
	public Page<AlertReview> findAllAlert(Pageable pageable);
	
}
