package com.risk.assessment.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.risk.assessment.dto.CraDTO;
import com.risk.assessment.dto.CraDTO.assesmentDto;
import com.risk.assessment.excecption.AbstractException;
import com.risk.assessment.excecption.RiskException;
import com.risk.assessment.models.ComplainceRisk;
import com.risk.assessment.payload.response.MessageResponse;
import com.risk.assessment.repository.ComplainceRiskRepository;
import com.risk.assessment.util.CRAExcelExport;
import com.risk.assessment.util.CRAExcelImport;

@Service
public class CRAServiceImpl implements CRAService {

	@Autowired
	ComplainceRiskRepository craRepo;

	@Value("${upload.success}")
	private String uploadSuccess;

	@Value("${pageSize}")
	private int defaultPageSize;

	@Value("${CRA.downloadFilePath}")
	private String downloadFilePath;
	
	public MessageResponse importCRAToDb(MultipartFile file) throws AbstractException {

		if (CRAExcelImport.hasExcelFormat(file)) {
			List<ComplainceRisk> risksFromDb = exportCRA();
			if (!risksFromDb.isEmpty()) {
				CRAExcelExport excelExporter = new CRAExcelExport(new ArrayList<ComplainceRisk>());
				try {
					excelExporter.exportToFolder(downloadFilePath, exportCRA());
				} catch (IOException e1) {
					throw RiskException.build("1009");
				}
			}
			try {
				InputStream inputFile = file.getInputStream();
				List<ComplainceRisk> craRisks = CRAExcelImport.getExcelDataAsList(inputFile);
				if (!craRisks.isEmpty()) {
					craRepo.deleteAll();
					craRepo.saveAll(craRisks);
				}
			} catch (IOException e) {
				throw RiskException.build("1008");
			}
			return new MessageResponse(uploadSuccess + file.getOriginalFilename());
		} else {
			throw RiskException.build("1006");
		}
	}

	public List<ComplainceRisk> exportCRA() throws AbstractException {
		return IteratorUtils.toList(craRepo.findAll().iterator());
	}

	public assesmentDto createCRA(CraDTO craDto) throws AbstractException {

		if (craDto != null) {
			ComplainceRisk riskToDb = new ComplainceRisk();
			riskToDb.setStatus("NO");
			craRepo.save(convertDTOToCompRisk(riskToDb, craDto));
		}
		return getAllCRA(1, defaultPageSize, "ALL");
	}

	@Override
	public assesmentDto getAllCRA(int pageNum, int pageSize, String department) throws AbstractException {
		Pageable paging = PageRequest.of(pageNum - 1, pageSize);
		
		Page<ComplainceRisk> pagedResult = null;
		if ("ALL".equals(department)) {
			pagedResult = craRepo.findAll(paging);
		} else {
			pagedResult = craRepo.findAllByRiskOwner(paging, department);
		}
		
		if (pagedResult.hasContent()) {
			return complainceRiskToDTO(pagedResult);
		} else {
			return new assesmentDto();
		}
	}
	
	@Override
	public assesmentDto getPendingCRA(int pageNum, int pageSize) throws AbstractException {
		Pageable paging = PageRequest.of(pageNum - 1, pageSize);
		Page<ComplainceRisk> pagedResult = craRepo.findAllByRemarkStatusAndStatus(paging, "M", "NO");
		if (pagedResult.hasContent()) {
			return complainceRiskToDTO(pagedResult);
		} else {
			return new assesmentDto();
		}
	}

	public boolean updateCRA(CraDTO craDto) throws AbstractException {

		Optional<ComplainceRisk> craOPT = craRepo.findById(craDto.getId());
		if (craOPT.isPresent()) {
			ComplainceRisk craDb = craOPT.get();
			craRepo.save(convertDTOToCompRisk(craDb, craDto));
		}
		return true;
	}

	private ComplainceRisk convertDTOToCompRisk(ComplainceRisk riskToDb, CraDTO craDto) {

		if (!"M".equals(riskToDb.getAddModDel())) {
			riskToDb.setAddModDel(craDto.getRemarks());
		}
		
		if ( !"M".equals(riskToDb.getRemarkStatus())) {
			riskToDb.setRemarkStatus("NO");
		}
		
		riskToDb.setSno(craDto.getSNo() != null ? String.valueOf(craDto.getSNo()) : "");
		if (StringUtils.hasText(riskToDb.getRegGuidlines()) && StringUtils.hasText(craDto.getRegGuidelines())) {
			if (!riskToDb.getRegGuidlines().equalsIgnoreCase(craDto.getRegGuidelines())) {
				riskToDb.setAddModDel("M");
			}
		} 
		
		if (StringUtils.hasText(riskToDb.getControlDesc()) && StringUtils.hasText(craDto.getControlDesc())) {
			if (!riskToDb.getControlDesc().equalsIgnoreCase(craDto.getControlDesc())) {
				if ( !"M".equals(riskToDb.getRemarkStatus())) {
					riskToDb.setRemarkStatus("M");
				}
			}
		}
		
		if (StringUtils.hasText(craDto.getStatus())) {
			riskToDb.setStatus(craDto.getStatus());
		}
		
		riskToDb.setCircularRefNo(craDto.getCircularRefNo());
		riskToDb.setCircularDate(craDto.getCircularDate());
		riskToDb.setRegGuidlines(craDto.getRegGuidelines());
		riskToDb.setControlDesc(craDto.getControlDesc());
		riskToDb.setBreach(craDto.getBreach());
		riskToDb.setProcessImprovement(craDto.getProcessImprovement());
		riskToDb.setProdName(craDto.getProdName());
		riskToDb.setBoardAppPolicy(craDto.getPolicyDetails()); // TODO Need to check the mapping with xls
		riskToDb.setRelevantPolicy(craDto.getRelavantPolicy());
		riskToDb.setProcessNote(craDto.getProcessNote());

		// Calculation Max value of below 3 into impact as ComplianceRisk
		Long compRisk = null;
		Long ctrlEffect = craDto.getControlEffectiveness();
		Long montiroMech = craDto.getMonitorMechanism();
		Long ctrlAuto = craDto.getControlAutomation();
		Long impact = craDto.getRegulatoryImpact();

		List<Long> list = Arrays.asList(ctrlEffect, montiroMech, ctrlAuto);
		List<Long> sortedList = list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		Long maxValue = sortedList.get(0);

		if (maxValue != null && impact != null) {
			compRisk = maxValue * impact;
		} else if (maxValue == null && impact != null) {
			compRisk = impact;
		} else if (impact == null && maxValue != null) {
			compRisk = maxValue;
		}

		riskToDb.setLikeRating(maxValue != null ? String.valueOf(maxValue) : null);
		riskToDb.setControlEffective(ctrlEffect != null ? String.valueOf(ctrlEffect) : null);
		riskToDb.setMonitorMechanism(montiroMech != null ? String.valueOf(montiroMech) : null);
		riskToDb.setControlAutomation(ctrlAuto != null ? String.valueOf(ctrlAuto) : null);
		riskToDb.setImpact(impact != null ? String.valueOf(impact) : null);
		riskToDb.setCompRisk(compRisk != null ? String.valueOf(compRisk) : null);
//		riskToDb.setAvgRisk(compRisk != null ? String.valueOf(compRisk) : null);// TODO Need to check the mapping
		// with xls
		
		return riskToDb;
	}

	private assesmentDto complainceRiskToDTO(Page<ComplainceRisk> pagedResult) {
		
		List<ComplainceRisk> risks = pagedResult.getContent();
		List<CraDTO> craDtos = new ArrayList<CraDTO>();
		Map<String, Double> departs = new HashMap<>();
		for (ComplainceRisk craFromDB : risks) {
			CraDTO craDto = new CraDTO();
			craDto.setId(craFromDB.getId());
			craDto.setSNo(craFromDB.getSno());
			craDto.setRemarks(craFromDB.getAddModDel());
			craDto.setCircularRefNo(craFromDB.getCircularRefNo());
			craDto.setCircularDate(craFromDB.getCircularDate());
			craDto.setRegGuidelines(craFromDB.getRegGuidlines());
			craDto.setControlDesc(craFromDB.getControlDesc());
			craDto.setBreach(craFromDB.getBreach());
			craDto.setProcessImprovement(craFromDB.getProcessImprovement());
			craDto.setProdName(craFromDB.getProdName());
			craDto.setPolicyDetails(craFromDB.getBoardAppPolicy()); // TODO Need to check the mapping with xls
			craDto.setRelavantPolicy(craFromDB.getRelevantPolicy());
			craDto.setProcessNote(craFromDB.getProcessNote());

			craDto.setControlEffectiveness(
					StringUtils.hasText(craFromDB.getControlEffective()) ? Long.valueOf(craFromDB.getControlEffective()) : null);
			craDto.setMonitorMechanism(
					StringUtils.hasText(craFromDB.getMonitorMechanism()) ? Long.valueOf(craFromDB.getMonitorMechanism()) : null);
			craDto.setControlAutomation(
					StringUtils.hasText(craFromDB.getControlAutomation()) ? Long.valueOf(craFromDB.getControlAutomation()) : null);
			craDto.setRegulatoryImpact(craFromDB.getImpact() != null ? Long.valueOf(craFromDB.getImpact()) : null);

			// TODO Need to check the mapping with xls
			craDto.setRegulationRisk(StringUtils.hasText(craFromDB.getCompRisk()) ? Long.valueOf(craFromDB.getCompRisk()) : null);
			// TODO Need to check the mapping with xls
			String riskOwner = craFromDB.getRiskOwner();
			Double avgRisk = 0.0;
			if (departs != null && departs.containsKey(riskOwner)) {
				avgRisk = departs.get(riskOwner);
			} else {
				avgRisk = craRepo.findByAvgCompRisk(riskOwner);
				BigDecimal bd = new BigDecimal(avgRisk).setScale(2, RoundingMode.HALF_UP);
				if (avgRisk != null ) {
					avgRisk = bd.doubleValue();
				} else {
					avgRisk = 0.0;
				}
				departs.put(riskOwner, avgRisk);
			}
			
			craDto.setAverageRisk(avgRisk);
			craDto.setStatus(craFromDB.getStatus());
			craDtos.add(craDto);
		}
		
		assesmentDto dtos = new assesmentDto();
		dtos.setTotalPage(pagedResult.getTotalPages());
		dtos.setTotalRecordCount(pagedResult.getTotalElements());
		dtos.setCras(craDtos);
		return dtos;
	}
	
	public assesmentDto approveCRA(List<Long> craIds) throws AbstractException {
		
		for (Long id : craIds) {
			Optional<ComplainceRisk> craOpt = craRepo.findById(id);
			if (craOpt.isPresent()) {
				ComplainceRisk risk = craOpt.get();
				risk.setStatus("YES");
				craRepo.save(risk);
			}
		}
		return getPendingCRA(1, defaultPageSize);
	}
	
	public assesmentDto deleteCRA(List<Long> craIds) throws AbstractException {
		
		for (Long id : craIds) {
			Optional<ComplainceRisk> craOpt = craRepo.findById(id);
			if (craOpt.isPresent()) {
				ComplainceRisk risk = craOpt.get();
				risk.setAddModDel("D");
				risk.setStatus("YES");
				craRepo.save(risk);
			}
		}
		return getPendingCRA(1, defaultPageSize);
	}
	
	public Set<String> getAllDepartment() throws AbstractException {
		
		Set<String> depts =  craRepo.findAllRiskOwner();
		depts.remove(null);
		depts.remove("");
		return depts;
	}
}
