package com.mdtlabs.coreplatform.spiceadminservice.labtest.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResult;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceadminservice.labtest.repository.LabTestRepository;
import com.mdtlabs.coreplatform.spiceadminservice.labtest.service.LabTestService;
import com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.service.LabTestResultRangesService;

/**
 * <p>
 * LabTestServiceImpl class implements various methods for managing lab tests, including adding, updating,
 * and retrieving lab tests, as well as validating lab test results.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class LabTestServiceImpl implements LabTestService {

    @Autowired
    private LabTestRepository labTestRepository;

    @Autowired
    private LabTestResultRangesService labTestResultRangesService;

    private final ModelMapper mapper = new ModelMapper();

    /**
     * {@inheritDoc}
     */
    public LabTest addLabTest(LabTest labTest) {
        if (Objects.isNull(labTest)) {
            Logger.logError(ErrorConstants.REQUEST_NOT_EMPTY);
            throw new BadRequestException(1003);
        }
        validateLabTest(labTest);
        if (!Objects.isNull(labTest.getLabTestResults()) && !labTest.getLabTestResults().isEmpty()) {
            labTest.setResultTemplate(Boolean.TRUE);
            labTest.getLabTestResults().forEach(result -> result.setTenantId(labTest.getTenantId()));
        }
        labTest.setActive(Boolean.TRUE);
        return labTestRepository.save(labTest);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getAllLabTests(RequestDTO requestDto) {
        Page<LabTest> labTests;
        ResponseListDTO response = new ResponseListDTO();
        String searchTerm = requestDto.getSearchTerm();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)) {
            response.setTotalCount(Constants.LONG_ZERO);
            return response;
        }

        Pageable pageable = Pagination.setPagination(requestDto.getSkip(), requestDto.getLimit(),
                Sort.by(Constants.UPDATED_AT).descending());
        labTests = labTestRepository.getAllLabTests(searchTerm, requestDto.getCountryId(),
                requestDto.getTenantId(), pageable);

        if (!labTests.isEmpty()) {
            response.setData(labTests.stream().map(
                    labtest -> mapper.map(labtest, LabTestDTO.class)
            ).toList());
            response.setTotalCount(labTests.getTotalElements());
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    public List<Map> searchLabTests(RequestDTO request) {
        String searchTerm = request.getSearchTerm();
        if (Objects.isNull(searchTerm) || Constants.ZERO == searchTerm.length()) {
            Logger.logError("Search Term should not be empty.");
            throw new DataNotAcceptableException(18008);
        }
        List<LabTest> labTests = labTestRepository.searchLabTests(searchTerm, request.getCountryId(),
                request.getIsActive());
        return labTests
                .stream().map(labTest -> Map.of(FieldConstants.ID, labTest.getId(), FieldConstants.NAME,
                        labTest.getName(), FieldConstants.COUNTRY, labTest.getCountryId()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeLabTest(RequestDTO request, boolean status) {
        Logger.logDebug("Remove the labTest meta data for id - " + request.getId());
        LabTest labTest = labTestRepository.findByIdAndIsDeletedAndTenantId(request.getId(), Constants.BOOLEAN_FALSE,
                request.getTenantId());
        if (Objects.isNull(labTest)) {
            Logger.logError(ErrorConstants.LAB_TEST_NOT_FOUND + request.getId());
            throw new DataNotFoundException(18013, request.getId().toString());
        }
        labTest.setDeleted(Constants.BOOLEAN_TRUE);
        if (!labTest.getLabTestResults().isEmpty()) {
            labTest.getLabTestResults().forEach(result -> result.setDeleted(status));
        }
        return !Objects.isNull(labTestRepository.save(labTest));
    }

    /**
     * {@inheritDoc}
     */
    public LabTest updateLabTest(LabTest labTest) {
        LabTest existingLabTest = validateLabTestRequest(labTest);
        Set<LabTestResult> labTestResults = new HashSet<>();
        if (!Objects.isNull(labTest.getLabTestResults()) && !labTest.getLabTestResults().isEmpty()) {
            labTestResults = validateLabTestResultsAndUpdate(labTest, existingLabTest);
        }
        if (Constants.ZERO != labTest.getDisplayOrder()) {
            existingLabTest.setDisplayOrder(labTest.getDisplayOrder());
        }
        existingLabTest.setActive(labTest.isActive());
        if (labTestResults.isEmpty()) {
            existingLabTest.setResultTemplate(Constants.BOOLEAN_FALSE);
        } else {
            existingLabTest.setLabTestResults(labTestResults);
        }
        return labTestRepository.save(existingLabTest);
    }

    /**
     * {@inheritDoc}
     */
    public LabTest getLabTestById(RequestDTO requestDto) {
        LabTest labTest = labTestRepository.findByIdAndIsDeletedAndTenantIdOrderByDisplayOrderAsc(requestDto.getId(),
                Constants.BOOLEAN_FALSE, requestDto.getTenantId());
        if (Objects.isNull(labTest)) {
            Logger.logError(ErrorConstants.LAB_TEST_NOT_FOUND + requestDto.getId());
            throw new DataNotFoundException(18013, requestDto.getId().toString());
        }

        labTest.setLabTestResults(labTest.getLabTestResults().stream().filter(labTestResult -> !labTestResult.isDeleted())
                .sorted(Comparator.comparingInt(LabTestResult::getDisplayOrder))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        return labTest;
    }

    /**
     * {@inheritDoc}
     */
    public void validateLabTest(LabTest labTest) {
        LabTest labTestCountryDetail = labTestRepository.findByCountryIdAndNameAndIsDeletedAndTenantId(labTest.getCountryId(),
                labTest.getName(), Boolean.FALSE, labTest.getTenantId());
        if (!Objects.isNull(labTestCountryDetail) && !Objects.equals(labTest.getId(), labTestCountryDetail.getId())) {
            Logger.logError(labTest.getName() + " already exist(s) in the regional database.");
            throw new DataConflictException(18000, labTest.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void validateLabTestResults(LabTest labTest) {
        if (!labTest.getLabTestResults().isEmpty()) {
            labTest.getLabTestResults().forEach(result -> {
                if (Constants.ZERO != result.getId() && result.isDeleted()) {
                    result.setActive(Boolean.TRUE);
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<LabTestResultDTO> getLabTestResultsById(long labTestId) {
        Set<LabTestResult> labTestResults = labTestRepository.findByIdAndIsDeleted(labTestId, Boolean.FALSE)
                .getLabTestResults();
        List<LabTestResultDTO> labTestResultDTOs = new ArrayList<>();
        if (!Objects.isNull(labTestResults) && !labTestResults.isEmpty()) {
            Map<Long, List<LabTestResultRangeDTO>> labTestResultRanges = labTestResultRangesService.getLabTestResultRange(labTestResults.stream().map(BaseEntity::getId).toList());
            labTestResults.forEach(result -> {
                if (!result.isDeleted()) {
                    LabTestResultDTO resultDTO = mapper.map(result, LabTestResultDTO.class);
                    resultDTO.setLabTestResultRanges(labTestResultRanges.get(result.getId()));
                    labTestResultDTOs.add(resultDTO);
                }
            });
        }
        return labTestResultDTOs;
    }

    /**
     * {@inheritDoc}
     */
    public LabTest getLabTestByName(String searchTerm, long countryId) {
        return labTestRepository.findByNameIgnoreCaseAndCountryId(searchTerm, countryId);
    }

    /**
     * {@inheritDoc}
     */
    public List<LabTest> getLabTestsById(Set<Long> uniqueLabTestIds) {
        List<LabTest> labTests = labTestRepository.findByIdInAndIsDeleted(uniqueLabTestIds, Constants.BOOLEAN_FALSE);
        return Objects.isNull(labTests) ? new ArrayList<>() : labTests;
    }

    /**
     * <p>
     * This method is used to validates and updates lab test results based on the input parameters.
     * </p>
     *
     * @param labTest         The LabTest that contains the new LabTestResults to be validated and
     *                        updated is given
     * @param existingLabTest the lab test that already exists in the system and needs to be updated with new lab test results.
     * @return A set of updated lab test results is returned
     */
    private Set<LabTestResult> validateLabTestResultsAndUpdate(LabTest labTest, LabTest existingLabTest) {
        Set<String> resultNamesToAddAndUpdate = new HashSet<>();
        Map<Long, String> deletedResultMap = new HashMap<>();
        Set<LabTestResult> resultsToUpdate = new HashSet<>();
        for (LabTestResult labTestResult : labTest.getLabTestResults()) {
            if (Objects.isNull(labTestResult.getId()) || !labTestResult.isDeleted()) {
                labTestResult.setLabTestId(existingLabTest.getId());
                labTestResult.setTenantId(existingLabTest.getTenantId());
                resultNamesToAddAndUpdate.add(labTestResult.getName());
            } else {
                labTestResult.setDeleted(true);
                deletedResultMap.put(labTestResult.getId(), labTestResult.getName());
            }
            resultsToUpdate.add(labTestResult);
        }
        if (labTest.getLabTestResults().size() != (deletedResultMap.size() + resultNamesToAddAndUpdate.size())) {
            Logger.logError("You are not allowed to add a lab test result with same name");
            throw new DataConflictException(18007);
        }

        return resultsToUpdate;
    }

    /**
     * <p>
     * This method is used to validate the given lab test.
     * </p>
     *
     * @param labTest {@link LabTest} The lab test needs to validate is given
     * @return {@link LabTest} The validated lab test is returned
     */
    private LabTest validateLabTestRequest(LabTest labTest) {
        if (Objects.isNull(labTest.getId())) {
            Logger.logError("Invalid or Empty LabTest ID.");
            throw new DataNotAcceptableException(18009);
        }
        LabTest existingLabTest = labTestRepository.findByIdAndIsDeletedAndTenantId(labTest.getId(), Constants.BOOLEAN_FALSE, labTest.getTenantId());
        if (Objects.isNull(existingLabTest)) {
            Logger.logError(ErrorConstants.LAB_TEST_NOT_FOUND + labTest.getId());
            throw new DataNotFoundException(18013, labTest.getId().toString());
        }
        if (!Objects.isNull(labTest.getName()) && !existingLabTest.getName().equals(labTest.getName())) {
            Logger.logError("Lab Test name should not be changed.");
            throw new DataNotAcceptableException(18010);
        }
        if (!Objects.isNull(labTest.getCountryId())
                && !Objects.equals(labTest.getCountryId(), existingLabTest.getCountryId())) {
            Logger.logError("Country should not be changed.");
            throw new DataNotAcceptableException(18011);
        }
        return existingLabTest;
    }
}
