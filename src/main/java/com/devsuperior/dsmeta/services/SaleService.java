package com.devsuperior.dsmeta.services;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> getReport(Pageable pageable, String minDate, String maxDate, String sellerName) {
		LocalDate endDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate startDate = endDate.minusYears(1L);

		if (minDate != null && !minDate.isEmpty()) {
			startDate = LocalDate.parse(minDate);
		}

		if (maxDate != null && !maxDate.isEmpty()) {
			endDate = LocalDate.parse(maxDate);
		}


        return repository.getReport(startDate, endDate, sellerName, pageable);
	}

	public List<SaleSummaryDTO> getSummary(String minDate, String maxDate) {
		LocalDate endDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate startDate = endDate.minusYears(1L);

		if (minDate != null && !minDate.isEmpty()) {
			startDate = LocalDate.parse(minDate);
		}

		if (maxDate != null && !maxDate.isEmpty()) {
			endDate = LocalDate.parse(maxDate);
		}

		List<SaleSummaryProjection> list = repository.getSummary(startDate, endDate);

		return list.stream().map(SaleSummaryDTO::new).collect(Collectors.toList());
	}
}
