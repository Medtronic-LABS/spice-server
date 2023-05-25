package com.mdtlabs.coreplatform.spiceadminservice.medication.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Medication;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceadminservice.medication.repository.MedicationRepository;
import com.mdtlabs.coreplatform.spiceadminservice.medication.service.MedicationService;

/**
 * <p>
 * MedicationServiceImpl class that implements a MedicationService interface and provides methods
 * for adding, updating, validating, getting, and deleting medication data from a repository.
 * </p>
 *
 * @author Niraimathi created on Jun 30, 2022
 */
@Service
public class MedicationServiceImpl implements MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    private final ModelMapper mapper = new ModelMapper();

    /**
     * {@inheritDoc}
     */
    public List<Medication> addMedication(List<Medication> medications) {
        Set<Medication> medicationSet = new HashSet<>(medications);
        if (medications.size() != medicationSet.size()) {
            throw new DataNotAcceptableException(12013);
        }
        return medicationRepository.saveAll(medicationSet);
    }

    /**
     * {@inheritDoc}
     */
    public Medication updateMedication(Medication medication) {
        validateMedication(medication);
        Medication existingMedication = medicationRepository.getMedicationByIdAndIsDeletedAndTenantIdAndIsActiveTrue(
                medication.getId(), Boolean.FALSE, medication.getTenantId());

        if (Objects.isNull(existingMedication)) {
            throw new DataNotFoundException(12008);
        }
        if (!(existingMedication.getMedicationName()).equals(medication.getMedicationName())) {
            throw new DataNotAcceptableException(12015);
        }
        if (!Objects.equals((existingMedication.getCountryId()), (medication.getCountryId()))) {
            throw new DataNotAcceptableException(12010);
        }
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(medication, existingMedication);
        return medicationRepository.save(existingMedication);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validateMedication(Medication medication) {
        if (Objects.isNull(medication.getClassificationId()) && Objects.isNull(medication.getBrandId())
                && Objects.isNull(medication.getDosageFormId()) && Objects.isNull(medication.getMedicationName())
                && Objects.isNull(medication.getCountryId())) {
            throw new DataNotAcceptableException(12014);
        }

        Medication medicationCountryDetail = medicationRepository.getMedicationByFieldsAndTenantId(
                medication.getClassificationId(), medication.getBrandId(), medication.getDosageFormId(),
                medication.getCountryId(), medication.getMedicationName(), medication.getTenantId());

        if (!Objects.isNull(medicationCountryDetail)
                && !Objects.equals(medication.getId(), medicationCountryDetail.getId())) {
            throw new DataConflictException(12009, medication.getMedicationName());
        }
        return Objects.isNull(medicationCountryDetail)
                || (Objects.equals(medication.getId(), medicationCountryDetail.getId()));
    }

    /**
     * {@inheritDoc}
     */
    public Medication getMedicationById(RequestDTO requestDto) {
        Medication medication = medicationRepository.findByIdAndIsDeletedFalseAndTenantId(requestDto.getId(),
                requestDto.getTenantId());
        if (Objects.isNull(medication)) {
            throw new DataNotFoundException();
        }
        return medication;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getAllMedications(RequestDTO requestObject) {
        if (Objects.isNull(requestObject.getCountryId())) {
            throw new DataNotAcceptableException(1001);
        }
        Map<String, Object> responseMap = new HashMap<>();
        String searchTerm = requestObject.getSearchTerm();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.REGEX_SEARCH_PATTERN)) {
            return Map.of(Constants.TOTAL_COUNT, Constants.ZERO, Constants.MEDICATION_LIST, new ArrayList<>());
        }
        String sortField = Objects.isNull(requestObject.getSortField()) || requestObject.getSortField().isBlank()
                ? FieldConstants.UPDATED_AT : requestObject.getSortField();
        Pageable pageable = Pagination.setPagination(requestObject.getSkip(), requestObject.getLimit(), sortField,
                requestObject.isSortByAsc());
        Page<Medication> medications = medicationRepository.getAllMedications(searchTerm,
                requestObject.getCountryId(), requestObject.getTenantId(), pageable);
        responseMap.put(Constants.MEDICATION_LIST, medications.stream().toList());
        responseMap.put(Constants.TOTAL_COUNT, medications.getTotalElements());
        return responseMap;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean deleteMedicationById(RequestDTO requestDto) {
        if (Objects.isNull(requestDto.getId()) || Objects.isNull(requestDto.getTenantId())) {
            throw new DataNotAcceptableException(12012);
        }
        Medication medication = getMedicationById(requestDto);
        medication.setDeleted(Constants.BOOLEAN_TRUE);
        return (null != medicationRepository.save(medication));
    }

    /**
     * {@inheritDoc}
     */
    public List<Medication> searchMedications(RequestDTO requestObject) {
        String searchTerm = requestObject.getSearchTerm();
        if (Objects.isNull(searchTerm) || 0 == searchTerm.length()) {
            throw new DataNotAcceptableException(18008);
        }
        return medicationRepository.searchMedications(requestObject.getSearchTerm(),
                UserContextHolder.getUserDto().getCountry().getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Medication getOtherMedication(long countryId) {
        return medicationRepository.getOtherMedication(countryId, Constants.OTHER,
                Constants.OTHER, Constants.OTHER, Constants.OTHER);
    }
}
