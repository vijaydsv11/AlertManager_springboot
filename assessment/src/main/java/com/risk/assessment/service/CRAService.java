package com.risk.assessment.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.risk.assessment.dto.CraDTO;
import com.risk.assessment.dto.CraDTO.assesmentDto;
import com.risk.assessment.excecption.AbstractException;
import com.risk.assessment.models.ComplainceRisk;
import com.risk.assessment.payload.response.MessageResponse;

public interface CRAService {

	public MessageResponse importCRAToDb(MultipartFile file) throws AbstractException;

	public assesmentDto getAllCRA(int pageNum, int pageSize, String department) throws AbstractException;
	
	public List<ComplainceRisk> exportCRA() throws AbstractException;
	
	public assesmentDto createCRA(CraDTO craDto) throws AbstractException;
	
	public boolean updateCRA(CraDTO craDto) throws AbstractException;
	
	public assesmentDto getPendingCRA(int pageNum, int pageSize) throws AbstractException;
	
	public assesmentDto approveCRA(List<Long> craIds) throws AbstractException;
	
	public assesmentDto deleteCRA(List<Long> craIds) throws AbstractException;
	
	public Set<String> getAllDepartment() throws AbstractException;

}
