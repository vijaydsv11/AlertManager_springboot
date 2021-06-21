package com.risk.assessment.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.risk.assessment.models.AlertReview;

/**
 * 
 * @author vijay ganesh
 *
 */
public class RiskMaangerUtil {

	public static List<AlertReview> removeDupfromtwoList(List<AlertReview> allList, List<AlertReview> assignedList) {
		
		Set<Long> unavailableItems = assignedList.stream().map(AlertReview::getId).collect(Collectors.toSet());
		return allList.stream()
			            .filter(e -> !unavailableItems.contains(e.getId()))
			            .collect(Collectors.toList());
	}
}
