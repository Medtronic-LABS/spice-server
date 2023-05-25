package com.mdtlabs.coreplatform.spiceservice.patientlabtest.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ReviewerDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PatientLabTestMapper;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository.PatientLabTestRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository.PatientLabTestResultRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.service.PatientLabTestService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * This class implements the PatientLabTestService class and contains business
 * logic for the operations of PatientLabTest Entity.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Service
public class PatientLabTestServiceImpl implements PatientLabTestService {

    @Autowired
    private AdminApiInterface adminApiInterface;

    @Autowired
    private PatientLabTestMapper patientLabTestMapper;

    @Autowired
    private PatientLabTestRepository patientLabTestRepository;

    @Autowired
    private PatientLabTestResultRepository patientLabTestResultRepository;

    @Autowired
    private PatientTrackerService patientTrackerService;

    @Autowired
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Autowired
    private PatientVisitService patientVisitService;

    @Autowired
    private UserApiInterface userApiInterface;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<PatientLabTest> createPatientLabTest(PatientLabTestRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestServiceImpl, create patient lab test");
        validateAndConstructPatientLabTests(requestData);
        PatientVisit patientVisit = patientVisitService.getPatientVisitById(requestData.getPatientVisitId());
        patientVisit.setInvestigation(Constants.BOOLEAN_TRUE);
        patientVisitService.updatePatientVisit(patientVisit);
        updatePatientTracker(requestData.getPatientTrackId(), requestData.getTenantId(), Constants.BOOLEAN_TRUE,
                Constants.BOOLEAN_TRUE);
        return patientLabTestRepository.saveAll(requestData.getLabTest());
    }

    /**
     * <p>
     * Sets unique lab test id with patient lab test request.
     * </p>
     *
     * @param requestData - patient lab test request dto
     */
    private void validateAndConstructPatientLabTests(PatientLabTestRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestServiceImpl, set unique lab test id");
        if (Objects.isNull(requestData)) {
            throw new BadRequestException(1003);
        }
        if (Objects.isNull(requestData.getLabTest()) || requestData.getLabTest().isEmpty()) {
            throw new BadRequestException(1306);
        }
        setOtherLabTest(requestData);
        Set<String> uniqueLabTestNames = new HashSet<>();
        Set<Long> uniqueLabTestIds = new HashSet<>();
        requestData.getLabTest().forEach(test -> {
            test.setTenantId(requestData.getTenantId());
            test.setPatientTrackId(requestData.getPatientTrackId());
            test.setPatientVisitId(requestData.getPatientVisitId());
            test.setIsReviewed(test.getIsReviewed());
            uniqueLabTestNames.add(test.getLabTestName());
            uniqueLabTestIds.add(test.getLabTestId());
        });
        if (uniqueLabTestNames.size() != requestData.getLabTest().size()) {
            throw new BadRequestException(1302);
        }
        List<LabTest> labTests = adminApiInterface
                .getLabTestsByIds(CommonUtil.getAuthToken(),
                        UserSelectedTenantContextHolder.get(), uniqueLabTestIds)
                .getBody();
        if (null == labTests || (uniqueLabTestIds.size() != labTests.size())) {
            throw new DataNotFoundException(1301);
        }
    }

    /**
     * {@inheritDoc}
     */
    public PatientLabTestResponseDTO getPatientLabTestList(GetRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestServiceImpl, get patient lab test list");
        if (Objects.isNull(requestData.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        Long patientVisitId = requestData.getPatientVisitId();
        PatientLabTestResponseDTO response = new PatientLabTestResponseDTO();
        setPatientLabTestDates(requestData, response);
        List<PatientLabTest> labTests = getPatientLabTests(requestData, patientVisitId);
        Set<Long> userIds = new HashSet<>();
        labTests
                .forEach(labTest -> Collections.addAll(userIds, labTest.getResultUpdateBy(), labTest.getReferredBy()));
        userIds.remove(null);
        Map<Long, UserDTO> userMap = userApiInterface
                .getUsers(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(), userIds).getBody();
        List<PatientLabTestDTO> labTestDTOs = constructPatientLabTestResponse(labTests, userMap);
        response.setPatientLabTest(labTestDTOs);
        return response;
    }

    /**
     * <p>
     * Adds lab test response to lab test dto.
     * </p>
     *
     * @param labTests - list of patient lab test
     * @param userMap  - user map
     * @return List - list of patient lab test dto
     */
    private List<PatientLabTestDTO> constructPatientLabTestResponse(List<PatientLabTest> labTests, Map<Long, UserDTO> userMap) {
        Logger.logDebug("In PatientLabTestServiceImpl, add patient lab test response");
        List<PatientLabTestDTO> labTestDTOs = new ArrayList<>();
        labTests.forEach(test -> {
            PatientLabTestDTO labTestResponse = new PatientLabTestDTO();
            labTestResponse = patientLabTestMapper.setPatientLabTestDto(test, labTestResponse);
            UserDTO referredBy = userMap.get(test.getReferredBy());
            labTestResponse.setReferredBy(new ReviewerDetailsDTO(referredBy.getFirstName(), referredBy.getLastName()));
            if (!Objects.isNull(test.getResultUpdateBy())) {
                UserDTO resultUpdatedBy = userMap.get(test.getResultUpdateBy());
                labTestResponse.setResultUpdateBy(
                        new ReviewerDetailsDTO(resultUpdatedBy.getFirstName(), resultUpdatedBy.getLastName()));
            }
            labTestDTOs.add(labTestResponse);
        });
        return labTestDTOs;
    }

    /**
     * <p>
     * Constructs lab tests values based on patient visit id.
     * </p>
     *
     * @param requestData    - get request dto
     * @param patientVisitId - patient visit id
     * @return List - list of patient lab test entity
     */
    private List<PatientLabTest> getPatientLabTests(GetRequestDTO requestData, Long patientVisitId) {
        Logger.logDebug("In PatientLabTestServiceImpl, construct lab test");
        List<PatientLabTest> labTests;
        if (!Objects.isNull(requestData.getRoleName())
                && requestData.getRoleName().equals(Constants.ROLE_LAB_TECHNICIAN)) {
            labTests = patientLabTestRepository.getPatientLabTestListWithCondition(requestData.getPatientTrackId(),
                    patientVisitId, Constants.BOOLEAN_FALSE);
        } else {
            labTests = patientLabTestRepository.getPatientLabTestList(requestData.getPatientTrackId(), patientVisitId,
                    Constants.BOOLEAN_FALSE);
        }
        return labTests;
    }

    /**
     * <p>
     * Sets patient lab test dates based on patient visit id.
     * </p>
     *
     * @param requestData - get request dto
     * @param response    - patient lab test response dto
     */
    private void setPatientLabTestDates(GetRequestDTO requestData,
                                        PatientLabTestResponseDTO response) {
        Logger.logDebug("In PatientLabTestServiceImpl, set patient lab test dates");
        if (requestData.isLatestRequired() && Objects.isNull(requestData.getPatientVisitId())) {
            List<PatientVisit> patientVisitList = patientVisitService.getPatientVisitDates(requestData.getPatientTrackId(),
                    Constants.BOOLEAN_TRUE, null, null);
            response.setPatientLabtestDates(patientVisitList.stream()
                    .map(visit -> Map.of(FieldConstants.ID, visit.getId(), "visitDate", visit.getVisitDate()))
                    .toList());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public boolean removePatientLabTest(GetRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestServiceImpl, remove patient lab test");
        PatientLabTest patientLabTest;
        if (!Objects.isNull(requestData.getId())) {
            patientLabTest = patientLabTestRepository.findByIdAndIsDeleted(requestData.getId(),
                    Constants.BOOLEAN_FALSE);
            if (Objects.isNull(patientLabTest)) {
                throw new DataNotFoundException(1303);
            }
            if (!Objects.isNull(patientLabTest.getResultDate())) {
                throw new DataNotAcceptableException(1305);
            }
            patientLabTest.setDeleted(Constants.BOOLEAN_TRUE);
            patientLabTest = patientLabTestRepository.save(patientLabTest);
            List<PatientLabTest> patientLabTests = patientLabTestRepository
                    .findAllByPatientVisitIdAndIsDeleted(patientLabTest.getPatientVisitId(), Constants.BOOLEAN_FALSE);
            if (patientLabTests.isEmpty()) {
                PatientVisit patientVisit = patientVisitService.getPatientVisitById(patientLabTest.getPatientVisitId());
                patientVisit.setInvestigation(false);
                patientVisitService.updatePatientVisit(patientVisit);
            }
            updatePatientTracker(patientLabTest.getPatientTrackId(), requestData.getTenantId(), Constants.BOOLEAN_FALSE,
                    Constants.BOOLEAN_TRUE);
        }
        return Constants.BOOLEAN_TRUE;
    }

    /**
     * {@inheritDoc}
     */
    public void reviewPatientLabTest(GetRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestServiceImpl, review patient lab test");
        if (Objects.isNull(requestData.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        PatientLabTest patientLabTest = patientLabTestRepository.findByIdAndTenantId(requestData.getId(),
                requestData.getTenantId());
        if (Objects.isNull(patientLabTest)) {
            throw new DataNotFoundException(1303);
        }
        patientLabTest.setIsReviewed(Constants.BOOLEAN_TRUE);
        patientLabTest
                .setComment(requestData.getComment());
        patientLabTestRepository.save(patientLabTest);
    }

    /**
     * <p>
     * Gets other Lab test from the database based on countryId.
     * </p>
     *
     * @return Long - lab test
     */
    private Long getOtherLabTest() {
        SearchRequestDTO requestEntity = new SearchRequestDTO();
        requestEntity.setCountryId(UserContextHolder.getUserDto().getCountry().getId());
        requestEntity.setSearchTerm(Constants.OTHER);
        LabTest labTest = adminApiInterface
                .getLabTestByName(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(), requestEntity)
                .getBody();
        if (Objects.isNull(labTest)) {
            throw new DataNotFoundException(1310);
        }
        return labTest.getId();
    }

    /**
     * <p>
     * Sets other lab test information.
     * </p>
     *
     * @param requestData - patient lab test request dto
     */
    private void setOtherLabTest(PatientLabTestRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestServiceImpl, set other lab test");
        boolean isOtherLabTest = requestData.getLabTest().stream()
                .anyMatch(data -> Constants.MINUS_ONE_LONG == data.getLabTestId());
        if (isOtherLabTest) {
            Long otherLabTestId = getOtherLabTest();
            requestData.getLabTest().forEach(entry -> {
                if (Constants.MINUS_ONE_LONG == entry.getLabTestId()) {
                    entry.setLabTestId(otherLabTestId);
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<PatientLabTestResult> createPatientLabTestResult(PatientLabTestResultRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestServiceImpl, create patient lab test result list");
        validateRequestData(requestData);
        PatientLabTest patientLabTest = patientLabTestRepository.findByIdAndIsDeleted(requestData.getPatientLabTestId(),
                Constants.BOOLEAN_FALSE);
        if (Objects.isNull(patientLabTest)) {
            throw new DataNotFoundException(1303);
        }
        List<Long> labTestResultIds = getLabTestResultsByLabTestId(patientLabTest.getLabTestId());
        boolean isValidIds = new HashSet<>(labTestResultIds).containsAll(requestData.getPatientLabTestResults().stream()
                .map(result -> Long.parseLong(result.getId())).toList());

        if (!isValidIds) {
            throw new DataNotAcceptableException(1307);
        }
        List<PatientLabTestResult> patientLabTestResults = constructPatientLabTestResults(requestData, patientLabTest);
        patientLabTestResults = patientLabTestResultRepository.saveAll(patientLabTestResults);
        patientLabTestRepository.save(patientLabTestMapper.setPatientLabTest(requestData, patientLabTest));
        boolean isLastReview = !Objects.isNull(requestData.getRoleName())
                ? Constants.ROLE_LAB_TECHNICIAN.equals(requestData.getRoleName())
                : Constants.BOOLEAN_FALSE;
        updatePatientTracker(patientLabTest.getPatientTrackId(), patientLabTest.getTenantId(), Constants.BOOLEAN_FALSE,
                isLastReview);
        return patientLabTestResults;
    }

    /**
     * <p>
     * Validates patient lab test reconstructPatientLabTestResult request dto values.
     * </p>
     *
     * @param requestData - patient lab test result request dto
     */
    private void validateRequestData(PatientLabTestResultRequestDTO requestData) {
        if (Objects.isNull(requestData.getPatientLabTestId())) {
            throw new BadRequestException(1004);
        }

        if (Objects.isNull(requestData.getIsEmptyRanges()) || Boolean.TRUE.equals(!requestData.getIsEmptyRanges())) {
            validatePatientLabTestResultRequest(requestData);
        }
    }

    /**
     * <p>
     * This method is used to construct PatientLabTestResult Data.
     * </p>
     *
     * @param requestData    - patient lab test result request dto
     * @param patientLabTest - entity
     * @return List - list of patient lab test result
     */
    private List<PatientLabTestResult> constructPatientLabTestResults(PatientLabTestResultRequestDTO requestData,
                                                                      PatientLabTest patientLabTest) {
        List<PatientLabTestResult> results = new ArrayList<>();
        for (PatientLabTestResultDTO result : requestData.getPatientLabTestResults()) {
            PatientLabTestResult patientLabTestResult = new PatientLabTestResult();
            patientLabTestResult = patientLabTestMapper.setPatientLabTestResult(
                    requestData, patientLabTest, result, patientLabTestResult);
            if (!Objects.isNull(result.getIsAbnormal()) && Boolean.TRUE.equals(result.getIsAbnormal())) {
                requestData.setIsAbnormal(Constants.BOOLEAN_TRUE);
            }
            results.add(patientLabTestResult);
        }
        return results;
    }

    /**
     * <p>
     * Validates the request data for PatientLabTestResult.
     * </p>
     *
     * @param requestData - patient lab test result request dto
     */
    private void validatePatientLabTestResultRequest(PatientLabTestResultRequestDTO requestData) {
        if (requestData.getPatientLabTestResults().isEmpty()) {
            throw new BadRequestException(1003);
        }
        for (PatientLabTestResultDTO result : requestData.getPatientLabTestResults()) {
            if (Objects.isNull(result.getDisplayName()) || Objects.isNull(result.getResultStatus())
                    || Objects.isNull(result.getIsAbnormal())) {
                throw new BadRequestException(1309);
            }
        }
    }

    /**
     * <p>
     * This method gets labTestResults using labTestId.
     * </p>
     *
     * @param labTestId - lab testId
     * @return List - list of lab test result id
     */
    private List<Long> getLabTestResultsByLabTestId(Long labTestId) {
        List<LabTestResultDTO> labTestResults = adminApiInterface.getLabtestResults(
                CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), labTestId);
        return labTestResults.stream().map(LabTestResultDTO::getId).toList();
    }

    /**
     * {@inheritDoc}
     */
    public PatientLabTestResultResponseDTO getPatientLabTestResults(PatientLabTestResultRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestServiceImpl, get patient lab test results");
        if (Objects.isNull(requestData.getPatientLabTestId())) {
            throw new BadRequestException(1308);
        }
        PatientLabTest patientLabTest = patientLabTestRepository.findByIdAndIsDeleted(requestData.getPatientLabTestId(),
                Constants.BOOLEAN_FALSE);

        PatientLabTestResultResponseDTO response = new PatientLabTestResultResponseDTO();
        if (!Objects.isNull(patientLabTest)) {
            List<PatientLabTestResult> results = patientLabTestResultRepository
                    .findAllByPatientLabTestIdAndIsDeletedAndTenantId(requestData.getPatientLabTestId(),
                            Constants.BOOLEAN_FALSE, requestData.getTenantId());
            if (!results.isEmpty()) {
                response.setPatientLabTestResults(results);
                response.setResultDate(patientLabTest.getResultDate());
                response.setComment(patientLabTest.getComment());
            }
        }
        return response;
    }

    /**
     * <p>
     * Updates the isLabTestReferred field if a labTest referred to a patient in
     * PatientTracker.
     * </p>
     *
     * @param patientTrackId    - patient tracker id
     * @param tenantId          - tenantId
     * @param isLabTestReferred - true or false
     */
    private void updatePatientTracker(long patientTrackId, Long tenantId, boolean isLabTestReferred,
                                      boolean isLastReview) {
        PatientTracker patientTracker = patientTrackerService.getPatientTrackerById(patientTrackId);
        if (!isLabTestReferred) {
            List<PatientLabTest> labTests = patientLabTestRepository.getPatientLabTestsWithoutResults(patientTrackId,
                    tenantId);
            if (labTests.isEmpty()) {
                patientTracker.setLabtestReferred(Constants.BOOLEAN_FALSE);
            }
        } else {
            patientTracker.setLabtestReferred(Constants.BOOLEAN_TRUE);
            patientTracker.setLastLabtestReferredDate(new Date());
        }
        if (isLastReview) {
            patientTracker.setLastReviewDate(new Date());
            PatientTreatmentPlan patientTreatmentPlan = patientTreatmentPlanService
                    .getPatientTreatmentPlan(patientTracker.getId());
            if (!Objects.isNull(patientTreatmentPlan)) {
                Date nextMedicalReviewDate = patientTreatmentPlanService.getTreatmentPlanFollowupDate(
                        patientTreatmentPlan.getMedicalReviewFrequency(), Constants.DEFAULT);
                patientTracker.setNextMedicalReviewDate(nextMedicalReviewDate);
            }
        }
        patientTrackerService.addOrUpdatePatientTracker(patientTracker);
    }

    /**
     * {@inheritDoc}
     */
    public List<PatientLabTest> getPatientLabTest(Long patientTrackId, Long patientVisitId) {
        List<PatientLabTest> patientLabTests = patientLabTestRepository.getPatientLabTestList(patientTrackId,
                patientVisitId, Constants.BOOLEAN_FALSE);
        return patientLabTests.isEmpty() ? new ArrayList<>() : patientLabTests;
    }

    /**
     * {@inheritDoc}
     */
    public int getLabTestCount(Long patientTrackId) {
        return patientLabTestRepository.getLabTestNoReviewedCount(patientTrackId);
    }

    /**
     * {@inheritDoc}
     */
    public List<LabTest> getLabTest(RequestDTO requestData) {
        return adminApiInterface.getLabtest(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                requestData);
    }

    /**
     * {@inheritDoc}
     */
    public List<LabTestResultDTO> getLabTestResults(Long labTestId) {
        return adminApiInterface.getLabtestResults(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                labTestId);
    }

    /**
     * {@inheritDoc}
     */
    public void removeLabTest(long trackerId) {
        List<PatientLabTest> patientLabTests = patientLabTestRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientLabTests)) {
            patientLabTests.forEach(labTest -> {
                labTest.setActive(false);
                labTest.setDeleted(true);
            });
            patientLabTestRepository.saveAll(patientLabTests);
        }

        List<PatientLabTestResult> labTestResults = patientLabTestResultRepository
                .findByPatientTrackId(trackerId);
        if (!Objects.isNull(labTestResults)) {
            labTestResults.forEach(labTestResult -> {
                labTestResult.setActive(false);
                labTestResult.setDeleted(true);
            });
            patientLabTestResultRepository.saveAll(labTestResults);
        }
    }
}
