package com.mdtlabs.coreplatform.spiceservice.patienttracker.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.ConfirmDiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MyPatientListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientFilterDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientSortDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchPatientListDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PatientTrackerMapper;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.repository.PatientTrackerRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class implements the patient tracker service class and contains business
 * logic for the operations of patient tracker entity.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
@Service
public class PatientTrackerServiceImpl implements PatientTrackerService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PatientTrackerMapper patientTrackerMapper;

    @Autowired
    private PatientTrackerRepository patientTrackerRepository;

    /**
     * {@inheritDoc}
     */
    private static void getMedicalReviewDetails(PatientFilterDTO patientFilterDTO, Map<String, String> map, Map<String, String> dateMap) {
        if (!Objects.isNull(patientFilterDTO.getMedicalReviewDate())) {
            if (patientFilterDTO.getMedicalReviewDate().equalsIgnoreCase(Constants.TODAY)) {
                map.put(Constants.MEDICAL_REVIEW_START_DATE, dateMap.get(Constants.TODAY_START_DATE));
                map.put(Constants.MEDICAL_REVIEW_END_DATE, dateMap.get(Constants.TODAY_END_DATE));
            } else if (patientFilterDTO.getMedicalReviewDate().equalsIgnoreCase(Constants.TOMORROW)) {
                map.put(Constants.MEDICAL_REVIEW_START_DATE, dateMap.get(Constants.TOMORROW_START_DATE));
                map.put(Constants.MEDICAL_REVIEW_END_DATE, dateMap.get(Constants.TOMORROW_END_DATE));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    private static void getAssessmentDetails(PatientFilterDTO patientFilterDTO, Map<String, String> map, Map<String, String> dateMap) {
        if (!Objects.isNull(patientFilterDTO.getAssessmentDate())) {
            if (patientFilterDTO.getAssessmentDate().equalsIgnoreCase(Constants.TODAY)) {
                map.put(Constants.ASSESSMENT_START_DATE, dateMap.get(Constants.TODAY_START_DATE));
                map.put(Constants.ASSESSMENT_END_DATE, dateMap.get(Constants.TODAY_END_DATE));
            } else if (patientFilterDTO.getAssessmentDate().equalsIgnoreCase(Constants.TOMORROW)) {
                map.put(Constants.ASSESSMENT_START_DATE, dateMap.get(Constants.TOMORROW_START_DATE));
                map.put(Constants.ASSESSMENT_END_DATE, dateMap.get(Constants.TOMORROW_END_DATE));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected static void getMedicationDetails(PatientFilterDTO patientFilterDTO, Map<String, String> map, Map<String, String> dateMap) {
        if (!Objects.isNull(patientFilterDTO.getMedicationPrescribedDate())) {
            if (patientFilterDTO.getMedicationPrescribedDate().equalsIgnoreCase(Constants.YESTERDAY)) {
                map.put(Constants.MEDICATION_PRESCRIBED_START_DATE, dateMap.get(Constants.YESTERDAY_START_DATE));
                map.put(Constants.MEDICATION_PRESCRIBED_END_DATE, dateMap.get(Constants.YESTERDAY_END_DATE));
            } else if (patientFilterDTO.getMedicationPrescribedDate().equalsIgnoreCase(Constants.TODAY)) {
                map.put(Constants.MEDICATION_PRESCRIBED_START_DATE, dateMap.get(Constants.TODAY_START_DATE));
                map.put(Constants.MEDICATION_PRESCRIBED_END_DATE, dateMap.get(Constants.TODAY_END_DATE));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected static void getLabTestDetails(PatientFilterDTO patientFilterDTO, Map<String, String> map, Map<String, String> dateMap) {
        if (!Objects.isNull(patientFilterDTO.getLabTestReferredDate())) {
            if (patientFilterDTO.getLabTestReferredDate().equalsIgnoreCase(Constants.YESTERDAY)) {
                map.put(Constants.LABTEST_REFERRED_START_DATE, dateMap.get(Constants.YESTERDAY_START_DATE));
                map.put(Constants.LABTEST_REFERRED_END_DATE, dateMap.get(Constants.YESTERDAY_END_DATE));
            } else if (patientFilterDTO.getLabTestReferredDate().equalsIgnoreCase(Constants.TODAY)) {
                map.put(Constants.LABTEST_REFERRED_START_DATE, dateMap.get(Constants.TODAY_START_DATE));
                map.put(Constants.LABTEST_REFERRED_END_DATE, dateMap.get(Constants.TODAY_END_DATE));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public PatientTracker addOrUpdatePatientTracker(PatientTracker patientTracker) {
        if (Objects.isNull(patientTracker)) {
            throw new BadRequestException(1000);
        }
        patientTracker.setNationalId(patientTracker.getNationalId().toUpperCase());
        return patientTrackerRepository.save(patientTracker);
    }

    /**
     * {@inheritDoc}
     */
    public PatientTracker getPatientTrackerById(Long patientTrackerId) {
        PatientTracker patientTracker = patientTrackerRepository.findByIdAndIsDeleted(patientTrackerId,
                Constants.BOOLEAN_FALSE);
        if (Objects.isNull(patientTracker)) {
            Logger.logError(("Patient not found for this Id - ").concat(patientTrackerId.toString()));
            throw new DataNotFoundException(1252);
        }
        return patientTracker;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO listMyPatients(PatientRequestDTO patientRequestDTO) {
        Pageable pageable = getSortingForPatients(patientRequestDTO);
        Map<String, String> filterMap = getFiltersForPatients(patientRequestDTO);
        Page<PatientTracker> patientTrackerList = getPatientsListWithPagination(filterMap, patientRequestDTO, pageable);
        Long totalCount = (Constants.ZERO == patientRequestDTO.getSkip()) ? patientTrackerList.getTotalElements()
                : null;
        return new ResponseListDTO(constructMyPatientList(patientTrackerList), totalCount);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO searchPatients(PatientRequestDTO patientRequestDTO) {
        if (StringUtils.isEmpty(patientRequestDTO.getSearchId())) {
            throw new DataNotAcceptableException(1005);
        }
        if (CommonUtil.validatePatientSearchData(List.of(patientRequestDTO.getSearchId()))) {
            return new ResponseListDTO();
        }
        String nationalId = patientRequestDTO.getSearchId().toUpperCase();
        Long programId = patientRequestDTO.getSearchId().matches(Constants.NUMBER_REGEX)
                ? Long.parseLong(patientRequestDTO.getSearchId())
                : null;
        if (Objects.isNull(programId)) {
            nationalId = patientRequestDTO.getSearchId().toUpperCase();
        }
        Long tenantId = (!Objects.isNull(patientRequestDTO.getIsSearchUserOrgPatient())
                && patientRequestDTO.getIsSearchUserOrgPatient()) ? patientRequestDTO.getTenantId() : null;
        Pageable pageable = getSortingForPatients(patientRequestDTO);
        Map<String, String> filterMap = getFiltersForPatients(patientRequestDTO);
        Page<PatientTracker> patientTrackerList = searchPatientsWithPagination(filterMap, patientRequestDTO, tenantId,
                nationalId, programId, pageable);
        Long totalCount = (Constants.ZERO == patientRequestDTO.getSkip()) ? patientTrackerList.getTotalElements()
                : null;
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return new ResponseListDTO(modelMapper.map(patientTrackerList.stream().toList(), new TypeToken<List<SearchPatientListDTO>>() {
        }.getType()), totalCount);
    }

    /**
     * <p>
     * Sorting conditions for patient list.
     * </p>
     *
     * @param patientRequestDTO patientRequestDTO
     * @return Pageable
     */
    public Pageable getSortingForPatients(PatientRequestDTO patientRequestDTO) {
        List<Sort.Order> sorts = new ArrayList<>();
        PatientSortDTO patientSortDTO = patientRequestDTO.getPatientSort();
        if (!Objects.isNull(patientSortDTO)) {
            sortByRedRiskAndDueDates(sorts, patientSortDTO);
            sortByBp(sorts, patientSortDTO);
            sortByAssessment(sorts, patientSortDTO);
            if (!Objects.isNull(patientSortDTO.getIsCvdRisk())) {
                sorts.add((Objects.equals(Constants.BOOLEAN_TRUE, patientSortDTO.getIsCvdRisk()))
                        ? new Sort.Order(Sort.Direction.DESC, Constants.CVD_RISK_SCORE)
                        : new Sort.Order(Sort.Direction.ASC, Constants.CVD_RISK_SCORE));
            }

            if (sorts.isEmpty()) {
                sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.UPDATED_AT));
            }
            sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.ID));
        }

        return Pagination.setPagination(patientRequestDTO.getSkip(), patientRequestDTO.getLimit(), sorts);
    }

    /**
     * <p>
     * Arranges by assessment values.
     * </p>
     *
     * @param sorts          - list of sort
     * @param patientSortDTO - patient sort dto
     */
    private void sortByAssessment(List<Sort.Order> sorts, PatientSortDTO patientSortDTO) {
        if (!Objects.isNull(patientSortDTO.getIsAssessmentDueDate())) {
            sorts.add((Objects.equals(Constants.BOOLEAN_TRUE, patientSortDTO.getIsAssessmentDueDate()))
                    ? new Sort.Order(Sort.Direction.ASC, Constants.NEXT_BP_ASSESSMENT_DATE)
                    : new Sort.Order(Sort.Direction.DESC, Constants.NEXT_BP_ASSESSMENT_DATE));
        }

        if (!Objects.isNull(patientSortDTO.getIsUpdated())) {
            sorts.add((Objects.equals(Constants.BOOLEAN_TRUE, patientSortDTO.getIsUpdated()))
                    ? new Sort.Order(Sort.Direction.DESC, Constants.UPDATED_AT)
                    : new Sort.Order(Sort.Direction.ASC, Constants.UPDATED_AT));
        }
    }

    /**
     * <p>
     * Arranges with respect to bp values
     * </p>
     *
     * @param sorts          - list of sort
     * @param patientSortDTO - patient sort dto
     */
    private void sortByBp(List<Sort.Order> sorts, PatientSortDTO patientSortDTO) {
        if (!Objects.isNull(patientSortDTO.getIsHighLowBp())) {
            sorts.add((Objects.equals(Constants.BOOLEAN_TRUE, patientSortDTO.getIsHighLowBp()))
                    ? new Sort.Order(Sort.Direction.DESC, Constants.AVG_SYSTOLIC)
                    : new Sort.Order(Sort.Direction.ASC, Constants.AVG_SYSTOLIC));
        }

        if (!Objects.isNull(patientSortDTO.getIsHighLowBg())) {
            sorts.add((Objects.equals(Constants.BOOLEAN_TRUE, patientSortDTO.getIsHighLowBg()))
                    ? new Sort.Order(Sort.Direction.DESC, Constants.GLUCOSE_VALUE)
                    : new Sort.Order(Sort.Direction.ASC, Constants.GLUCOSE_VALUE));
        }
    }

    /**
     * <p>
     * Arranges by red risk and due dates.
     * </p>
     *
     * @param sorts          - list of sort
     * @param patientSortDTO - patient sort dto
     */
    private void sortByRedRiskAndDueDates(List<Sort.Order> sorts, PatientSortDTO patientSortDTO) {
        if (!Objects.isNull(patientSortDTO.getIsRedRisk())) {
            sorts.add((Objects.equals(Constants.BOOLEAN_TRUE, patientSortDTO.getIsRedRisk()))
                    ? new Sort.Order(Sort.Direction.DESC, Constants.IS_RED_RISK_PATIENT)
                    : new Sort.Order(Sort.Direction.ASC, Constants.IS_RED_RISK_PATIENT));
        }

        if (!Objects.isNull(patientSortDTO.getIsLatestAssessment())) {
            sorts.add((Objects.equals(Constants.BOOLEAN_TRUE, patientSortDTO.getIsLatestAssessment()))
                    ? new Sort.Order(Sort.Direction.DESC, Constants.LAST_ASSESSMENT_DATE)
                    : new Sort.Order(Sort.Direction.ASC, Constants.LAST_ASSESSMENT_DATE));
        }

        if (!Objects.isNull(patientSortDTO.getIsMedicalReviewDueDate())) {
            sorts.add((Objects.equals(Constants.BOOLEAN_TRUE, patientSortDTO.getIsMedicalReviewDueDate()))
                    ? new Sort.Order(Sort.Direction.DESC, Constants.NEXT_MEDICAL_REVIEW_DATE)
                    : new Sort.Order(Sort.Direction.ASC, Constants.NEXT_MEDICAL_REVIEW_DATE));
        }
    }

    /**
     * <p>
     * Filter conditions for patients.
     * </p>
     *
     * @param patientRequestDTO patientRequestDTO
     * @return Map
     */
    public Map<String, String> getFiltersForPatients(PatientRequestDTO patientRequestDTO) {
        Map<String, String> filter = new HashMap<>();
        if (!Objects.isNull(patientRequestDTO.getPatientFilter())) {
            PatientFilterDTO patientFilterDTO = patientRequestDTO.getPatientFilter();
            if (!Objects.isNull(patientFilterDTO.getScreeningReferral())
                    && Objects.equals(Constants.BOOLEAN_TRUE, patientFilterDTO.getScreeningReferral())) {
                filter.put(Constants.PATIENT_STATUS_NOT_SCREENED, Constants.SCREENED);
            }

            if (!StringUtils.isEmpty(patientFilterDTO.getPatientStatus())) {
                filter.put(patientFilterDTO.getPatientStatus().equalsIgnoreCase(Constants.ENROLLED)
                        ? Constants.PATIENT_STATUS_ENROLLED
                        : Constants.PATIENT_STATUS_NOT_ENROLLED, Constants.ENROLLED);
            }

            if (!Objects.isNull(patientFilterDTO.getMedicalReviewDate())
                    || !Objects.isNull(patientFilterDTO.getAssessmentDate())
                    || !Objects.isNull(patientFilterDTO.getMedicationPrescribedDate())
                    || !Objects.isNull(patientFilterDTO.getLabTestReferredDate())) {
                filter.putAll(getDatesFilter(patientFilterDTO, filter));
            }
        }
        return filter;
    }

    /**
     * <p>
     * Dates filter for patients.
     * </p>
     *
     * @param patientFilterDTO patientFilterDTO
     * @param map              map
     * @return map
     */
    private Map<String, String> getDatesFilter(PatientFilterDTO patientFilterDTO, Map<String, String> map) {
        Map<String, String> dateMap = getTodayAndTomorrowDate();
        getMedicalReviewDetails(patientFilterDTO, map, dateMap);

        getAssessmentDetails(patientFilterDTO, map, dateMap);

        getMedicationDetails(patientFilterDTO, map, dateMap);

        getLabTestDetails(patientFilterDTO, map, dateMap);

        return map;
    }

    /**
     * <p>
     * Get today and tomorrow date in UTC time zone.
     * </p>
     *
     * @return Map
     */
    public Map<String, String> getTodayAndTomorrowDate() {
        HashMap<String, String> datesMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_UTC_FORMAT);

        LocalDate today = LocalDate.now();
        ZonedDateTime zonedDateTime = today.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of(Constants.TIMEZONE_UTC));
        datesMap.put(Constants.TODAY_START_DATE, zonedDateTimeUTC.format(formatter));

        zonedDateTime = zonedDateTime.plusDays(Constants.ONE).minusSeconds(Constants.ONE);
        zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of(Constants.TIMEZONE_UTC));
        datesMap.put(Constants.TODAY_END_DATE, zonedDateTimeUTC.format(formatter));

        LocalDate yesterday = today.minusDays(Constants.ONE);
        zonedDateTime = yesterday.atStartOfDay(ZoneId.systemDefault());
        zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of(Constants.TIMEZONE_UTC));
        datesMap.put(Constants.YESTERDAY_START_DATE, zonedDateTimeUTC.format(formatter));

        zonedDateTime = zonedDateTime.plusDays(Constants.ONE).minusSeconds(Constants.ONE);
        zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of(Constants.TIMEZONE_UTC));
        datesMap.put(Constants.YESTERDAY_END_DATE, zonedDateTimeUTC.format(formatter));

        LocalDate tomorrow = today.plusDays(Constants.ONE);
        zonedDateTime = tomorrow.atStartOfDay(ZoneId.systemDefault());
        zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of(Constants.TIMEZONE_UTC));
        datesMap.put(Constants.TOMORROW_START_DATE, zonedDateTimeUTC.format(formatter));

        zonedDateTime = zonedDateTime.plusDays(Constants.ONE).minusSeconds(Constants.ONE);
        zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of(Constants.TIMEZONE_UTC));
        datesMap.put(Constants.TOMORROW_END_DATE, zonedDateTimeUTC.format(formatter));
        Logger.logInfo("dates map: " + datesMap);

        return datesMap;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO patientAdvanceSearch(PatientRequestDTO patientRequestDTO) {
        boolean isInvalidData = CommonUtil.validatePatientSearchData(Arrays.asList(patientRequestDTO.getFirstName(),
                patientRequestDTO.getLastName(), patientRequestDTO.getPhoneNumber()));

        if (isInvalidData) {
            throw new DataNotAcceptableException(1255);
        }
        Pageable pageable = getSortingForPatients(patientRequestDTO);
        Map<String, String> filterMap = getFiltersForPatients(patientRequestDTO);
        Long countryId = patientRequestDTO.isGlobally() ? UserContextHolder.getUserDto().getCountry().getId() : null;
        Long tenantId = patientRequestDTO.isGlobally() || !Objects.isNull(patientRequestDTO.getOperatingUnitId()) ? null : UserSelectedTenantContextHolder.get();
        Page<PatientTracker> patientTrackerList = getPatientsWithAdvanceSearch(filterMap, patientRequestDTO, countryId, tenantId, pageable);
        Long totalCount = (Constants.ZERO == patientRequestDTO.getSkip()) ? patientTrackerList.getTotalElements() : null;
        return new ResponseListDTO(constructMyPatientList(patientTrackerList), totalCount);

    }

    /**
     * {@inheritDoc}
     */
    public void updatePatientTrackerLabtestReferral(long patientTrackId, Long tenantId, boolean isLabTestReferred) {
        PatientTracker patientTracker = findPatientTrackerById(patientTrackId);
        patientTracker.setTenantId(tenantId);
        patientTracker.setLabtestReferred(isLabTestReferred);
        patientTracker.setNationalId(patientTracker.getNationalId().toUpperCase());
        patientTrackerRepository.save(patientTracker);
    }

    /**
     * {@inheritDoc}
     */
    public void updatePatientTrackerForBpLog(long patientTrackerId, BpLog bpLog, Date nextBpAssessmentDate) {
        PatientTracker patientTracker = findPatientTrackerById(patientTrackerId);
        patientTracker.setNextBpAssessmentDate(nextBpAssessmentDate);
        patientTracker.setNationalId(patientTracker.getNationalId().toUpperCase());
        patientTrackerRepository
                .save(patientTrackerMapper.setPatientTrackerFromBpLog(bpLog, patientTracker));
    }

    /**
     * {@inheritDoc}
     */
    public void updatePatientTrackerForGlucoseLog(long patientTrackerId, GlucoseLog glucoseLog,
                                                  Date nextBgAssessmentDate) {
        PatientTracker patientTracker = patientTrackerMapper.setPatientTrackerFromGlucose(glucoseLog, findPatientTrackerById(patientTrackerId), nextBgAssessmentDate);
        patientTracker.setNationalId(patientTracker.getNationalId().toUpperCase());
        patientTrackerRepository.save(patientTracker);
    }

    /**
     * {@inheritDoc}
     */
    public PatientTracker findByNationalIdIgnoreCaseAndCountryIdAndIsDeleted(String nationalId, Long country, boolean booleanFalse) {
        return patientTrackerRepository.findByNationalIdAndCountryIdAndIsDeleted(nationalId.toUpperCase(), country, booleanFalse);
    }

    /**
     * {@inheritDoc}
     */
    public void updateRedRiskPatientStatus(long patientTrackerId, boolean status) {
        PatientTracker patientTracker = findPatientTrackerById(patientTrackerId);
        patientTracker.setRedRiskPatient(status);
        patientTracker.setNationalId(patientTracker.getNationalId().toUpperCase());
        patientTrackerRepository.save(patientTracker);
    }

    /**
     * {@inheritDoc}
     */
    public ConfirmDiagnosisDTO updateConfirmDiagnosis(ConfirmDiagnosisDTO confirmDiagnosis) {
        PatientTracker patientTracker = getPatientTrackerById(confirmDiagnosis.getPatientTrackId());
        patientTracker.setConfirmDiagnosis(confirmDiagnosis.getConfirmDiagnosis());
        patientTracker.setDiagnosisComments(confirmDiagnosis.getDiagnosisComments());
        patientTracker.setIsConfirmDiagnosis(Constants.BOOLEAN_TRUE);
        patientTracker.setTenantId(UserSelectedTenantContextHolder.get());
        patientTracker.setNationalId(patientTracker.getNationalId().toUpperCase());
        patientTrackerRepository.save(patientTracker);
        confirmDiagnosis.setIsConfirmDiagnosis(Constants.BOOLEAN_TRUE);
        return confirmDiagnosis;
    }

    /**
     * <p>
     * Update fill prescription details in patient tracker.
     * </p>
     *
     * @param id                     PatientTrackId
     * @param isMedicationPrescribed isMedicationPrescribed field
     */
    public void updateForFillPrescription(Long id, boolean isMedicationPrescribed, Date lastReviewDate,
                                          Date nextMedicalReviewDate) {
        Logger.logInfo("Updating fill prescription for patient tracker id: " + id);
        PatientTracker patientTracker = findPatientTrackerById(id);
        patientTracker.setMedicationPrescribed(isMedicationPrescribed);
        if (!Objects.isNull(lastReviewDate)) {
            patientTracker.setLastReviewDate(lastReviewDate);
        }
        if (!Objects.isNull(nextMedicalReviewDate)) {
            patientTracker.setNextMedicalReviewDate(nextMedicalReviewDate);
        }
        if (isMedicationPrescribed) {
            patientTracker.setLastMedicationPrescribedDate(new Date());
        }
        patientTracker.setNationalId(patientTracker.getNationalId().toUpperCase());
        patientTrackerRepository.save(patientTracker);
    }

    /**
     * {@inheritDoc}
     */
    public PatientTracker getPatientTrackerByIdAndStatus(Long id, Long tenantId, String status) {
        return patientTrackerRepository.getPatientTracker(id, status, tenantId);
    }

    /**
     * <p>
     * Constructs my patient list.
     * </p>
     *
     * @param patientTrackerList patientTrackerList
     * @return List of MyPatientListDTO
     */ 
    private List<MyPatientListDTO> constructMyPatientList(Page<PatientTracker> patientTrackerList) {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());    
        return modelMapper.map(patientTrackerList.stream().toList(), new TypeToken<List<MyPatientListDTO>>() {
        }.getType());
    }

    /**
     * <p>
     * To get the patients list based on filters like status, cvd risk etc.,
     * </p>
     *
     * @param filterMap         filterMap
     * @param patientRequestDTO patientRequestDTO
     * @param pageable          pageable
     * @return Patient trackers with pagination
     */
    private Page<PatientTracker> getPatientsListWithPagination(Map<String, String> filterMap, PatientRequestDTO patientRequestDTO, Pageable pageable) {
        Long tenantId = Objects.isNull(patientRequestDTO.getTenantId()) ? UserSelectedTenantContextHolder.get() : patientRequestDTO.getTenantId();
        return patientTrackerRepository.getPatientsListWithPagination(
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), tenantId, pageable);
    }

    /**
     * <p>
     * To get the list of patients based on filters like national id, program id etc.,
     * </p>
     *
     * @param filterMap         filterMap
     * @param patientRequestDTO patientRequestDTO
     * @param tenantId          tenantId
     * @param nationalId        nationalId
     * @param programId         programId
     * @param pageable          pageable
     * @return Patient trackers with pagination
     */
    protected Page<PatientTracker> searchPatientsWithPagination(Map<String, String> filterMap,
                                                              PatientRequestDTO patientRequestDTO, Long tenantId, String nationalId, Long programId,
                                                              Pageable pageable) {
        return patientTrackerRepository.searchPatientsWithPagination(tenantId, patientRequestDTO.getOperatingUnitId(),
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), nationalId, programId, pageable);
    }

    /**
     * <p>
     * To get the list of patients based on filters like country id, operating unit id etc.,
     * </p>
     *
     * @param filterMap         filterMap
     * @param patientRequestDTO patientRequestDTO
     * @param countryId         countryId
     * @param pageable          pageable
     * @return Patient trackers with pagination
     */
    private Page<PatientTracker> getPatientsWithAdvanceSearch(Map<String, String> filterMap,
                                                              PatientRequestDTO patientRequestDTO, Long countryId, Long tenantId, Pageable pageable) {
        return patientTrackerRepository.getPatientsWithAdvanceSearch(
                (null == patientRequestDTO.getFirstName() ? null :
                        patientRequestDTO.getFirstName().toUpperCase()),
                (null == patientRequestDTO.getLastName() ? null :
                        patientRequestDTO.getLastName().toUpperCase()), patientRequestDTO.getPhoneNumber(),
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), patientRequestDTO.getIsLabtestReferred(),
                patientRequestDTO.getIsMedicationPrescribed(), countryId, patientRequestDTO.getOperatingUnitId(),
                Constants.BOOLEAN_FALSE, tenantId, pageable);
    }

    /**
     * <p>
     * Finds patient tracker by it's id.
     * </p>
     *
     * @param patientTrackerId patientTrackerId
     * @return PatientTracker entity
     */
    private PatientTracker findPatientTrackerById(Long patientTrackerId) {
        return patientTrackerRepository.findById(patientTrackerId).orElseThrow(() -> new DataNotFoundException(1252));
    }
}
