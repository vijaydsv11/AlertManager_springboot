package com.risk.assessment;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Example {

	public static void main(String[] args) {

		Integer compRisk = 0;
		String ctrlEffect = "";
		String montiroMech = "2";
		String ctrlAuto = "4";
		String impactStr = "3";
		Long impact = impactStr != "" ? Long.valueOf(impactStr) : null;
		
		List<String> list =  Arrays.asList(ctrlEffect,montiroMech,ctrlAuto);  
		List<String> sortedList=list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());  
		Long maxValue = sortedList.get(0) != "" ? Long.valueOf(sortedList.get(0)) : null;
		System.out.println(maxValue);
	}

}
