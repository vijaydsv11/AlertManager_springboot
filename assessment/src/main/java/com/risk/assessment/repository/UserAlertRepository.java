package com.risk.assessment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.risk.assessment.models.AlertReview;
import com.risk.assessment.models.User;
import com.risk.assessment.models.UserAlertReview;

@Repository
public interface UserAlertRepository extends PagingAndSortingRepository<UserAlertReview, Long> {

	@Query("SELECT t.alert FROM UserAlertReview t")
	//@Query("select t from alertReview t where t.id not in (select ua.alertId from UserAlertReview ua)")
	public Page<AlertReview> findAllAlert(Pageable pageable);

	@Query("SELECT t.alert FROM UserAlertReview t WHERE t.user = :user")
	public Page<AlertReview> findAllAlertByUser(@Param("user") User user, Pageable pageable);

	public List<UserAlertReview> findAllByUser(User user);

	public List<UserAlertReview> findAllByAlert(AlertReview alert);

	public UserAlertReview findByUserAndAlert(User user, AlertReview alert);
}
