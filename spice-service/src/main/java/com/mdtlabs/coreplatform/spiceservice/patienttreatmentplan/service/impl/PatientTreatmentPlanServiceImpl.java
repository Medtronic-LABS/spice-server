package com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.FrequencyDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Frequency;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.util.DateUtil;
import com.mdtlabs.coreplatform.spiceservice.frequency.service.FrequencyService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.FrequencyTypeService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.repository.PatientTreatmentPlanRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;

/**
 * <p>
 * PatientTreatmentPlanServiceImpl class implements various methods for managing patient treatment plan,
 * including adding, updating, removing and retrieving patient treatment plan.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class PatientTreatmentPlanServiceImpl implements PatientTreatmentPlanService {

    @Autowired
    private CultureService cultureService;

    @Autowired
    private FrequencyService frequencyService;

    @Autowired
    private PatientTreatmentPlanRepository patientTreatmentPlanRepository;

    @Autowired
    private FrequencyTypeService frequencyTypeService;

    /**
     * {@inheritDoc}
     */
    public PatientTreatmentPlan getPatientTreatmentPlan(Long patientTrackId) {
        Logger.logDebug("In PatientTreatmentPlanServiceImpl, gets patient treatment plan");
        List<PatientTreatmentPlan> treatmentPlan = patientTreatmentPlanRepository
                .findByPatientTrackIdAndIsDeletedOrderByUpdatedAtDesc(patientTrackId, Constants.BOOLEAN_FALSE);
        return treatmentPlan.isEmpty() ? null : treatmentPlan.get(Constants.ZERO);
    }

    /**
     * {@inheritDoc}
     */
    public PatientTreatmentPlan getPatientTreatmentPlanDetails(Long id) {
        Logger.logDebug("In PatientTreatmentPlanServiceImpl, gets patient treatment plan with id");
        return patientTreatmentPlanRepository.findFirstByPatientTrackIdAndTenantIdAndIsDeletedOrderByUpdatedAtDesc(id,
                UserSelectedTenantContextHolder.get(), Constants.BOOLEAN_FALSE);
    }

    /**
     * {@inheritDoc}
     */
    public PatientTreatmentPlan getPatientTreatmentPlanDetails(Long id, String cvdRiskLevel, Long tenantId) {
        Logger.logDebug("In PatientTreatmentPlanServiceImpl, gets patient treatment plan with tenant id");
        return patientTreatmentPlanRepository.findByPatientTrackIdAndTenantId(id, tenantId);
    }

    /**
     * {@inheritDoc}
     */
    public List<Map<String, String>> createProvisionalTreatmentPlan(PatientTracker patientTracker, String cvdRiskLevel,
                                                                    Long tenantId) {
        Logger.logDebug("In PatientTreatmentPlanServiceImpl, create provisional Treatment plan");
        PatientTreatmentPlan treatmentPlan = getPatientTreatmentPlanDetails(patientTracker.getId(), cvdRiskLevel,
                tenantId);
        List<Frequency> frequencyList = frequencyService.getFrequencyListByRiskLevel(cvdRiskLevel);
        List<Map<String, String>> treatmentPlanResponse;
        Long cultureId = cultureService.loadCultureValues();
        Map<Long, String> frequencyMap = new HashMap<>();
        if (!Objects.isNull(Constants.CULTURE_VALUES_MAP.get(cultureId).get(Constants.CULTURE_VALUE_FREQUENCY))) {
            frequencyMap = Constants.CULTURE_VALUES_MAP.get(cultureId).get(Constants.CULTURE_VALUE_FREQUENCY);
        }
        Map<String, String> frequencyTypeMap = frequencyTypeService.getFrequencyTypesByCultureId(cultureId);
        if (Objects.isNull(treatmentPlan)) {
            treatmentPlan = createPatientTreatmentPlan(tenantId, patientTracker.getId(), cvdRiskLevel, frequencyList);
            treatmentPlanResponse = updateNextFollowupDetails(treatmentPlan, frequencyList, patientTracker, frequencyMap, frequencyTypeMap);
        } else {
            treatmentPlanResponse = updateNextFollowupDetailsForExisting(treatmentPlan, patientTracker, frequencyMap, frequencyTypeMap);
        }
        return treatmentPlanResponse;
    }

    /**
     * <p>
     * This method is used to update the next follow-up details for an existing patient treatment plan and returns a
     * list of response maps.
     * </p>
     *
     * @param treatmentPlan    {@link PatientTreatmentPlan} The treatment plan containing the treatment plan details for
     *                         a patient is given
     * @param patientTracker   {@link PatientTracker} The PatientTracker contains information about the patient's
     *                         treatment plan and next follow-up dates for various assessments such as blood pressure
     *                         check, blood glucose check, and medical review. is given
     * @param frequencyMap     {@link Map} The map containing the frequency values for different medical assessments
     *                         is given
     * @param frequencyTypeMap {@link Map} The map that contains the frequency type (e.g. days, weeks, months) for each
     *                         frequency ID is given
     * @return {@link List<Map>} The list of map of next followUp details for the given treatmentPlan,
     * patientTracker, frequencyMap, and frequencyTypeMap is returned
     */
    public List<Map<String, String>> updateNextFollowupDetailsForExisting(PatientTreatmentPlan treatmentPlan,
                                                                          PatientTracker patientTracker, Map<Long, String> frequencyMap, Map<String, String> frequencyTypeMap) {
        Logger.logDebug("In PatientTreatmentPlanServiceImpl, updates next follow up details");
        List<Map<String, String>> responseList = new ArrayList<>();
        Date bpFollowUpDate = getTreatmentPlanForPatient(Constants.FREQUENCY_BP_CHECK, treatmentPlan.getBpCheckFrequency(),
                responseList, frequencyMap, frequencyTypeMap);
        patientTracker.setNextBpAssessmentDate(bpFollowUpDate);
        Date bgFollowUpDate = getTreatmentPlanForPatient(Constants.FREQUENCY_BG_CHECK, treatmentPlan.getBgCheckFrequency(),
                responseList, frequencyMap, frequencyTypeMap);
        patientTracker.setNextBgAssessmentDate(bgFollowUpDate);
        Date mrFollowUpDate = getTreatmentPlanForPatient(Constants.FREQUENCY_MEDICAL_REVIEW, treatmentPlan.getMedicalReviewFrequency(),
                responseList, frequencyMap, frequencyTypeMap);
        patientTracker.setNextMedicalReviewDate(mrFollowUpDate);
        responseList.add(Map.of(Constants.LABEL, frequencyTypeMap.get(Constants.FREQUENCY_HBA1C_CHECK), Constants.VALUE, Constants.HYPHEN));

        return responseList;
    }

    /**
     * <p>
     * This method is used to set the frequencies of various medical checks and reviews in a patient's treatment plan
     * based on a list of frequencies.
     * </p>
     *
     * @param frequencyList {@link List<Frequency>}The list of Frequency objects that contain information about how
     *                      often certain medical checks or reviews should be conducted for a patient is given
     * @param treatmentPlan {@link PatientTreatmentPlan} The PatientTreatmentPlan, which is being updated with the
     *                      frequencies from the frequencyList is given
     */
    private void setFrequencies(List<Frequency> frequencyList, PatientTreatmentPlan treatmentPlan) {
        if (!frequencyList.isEmpty()) {
            for (Frequency frequency : frequencyList) {
                switch (frequency.getType()) {
                    case Constants.FREQUENCY_BP_CHECK -> treatmentPlan.setBpCheckFrequency(frequency.getName());
                    case Constants.FREQUENCY_HBA1C_CHECK -> treatmentPlan.setHba1cCheckFrequency(frequency.getName());
                    case Constants.FREQUENCY_MEDICAL_REVIEW ->
                            treatmentPlan.setMedicalReviewFrequency(frequency.getName());
                }
            }
        }
    }

    /**
     * <p>
     * This method is used to update the next follow-up details for a patient's treatment plan based on frequency
     * information and returns a list of frequency DTOs.
     * </p>
     *
     * @param treatmentPlan      {@link PatientTreatmentPlan} The PatientTreatmentPlan contains details about the
     *                           treatment plan for a patient is given
     * @param frequencyList      {@link List<Frequency>} The list of Frequency contains information about the frequency
     *                           of medical review, BP check, and BG assessment is given
     * @param patientTracker     {@link PatientTracker} The patientTracker contains information about a
     *                           patient's treatment plan and follow-up details. is given
     * @param staticFrequencyMap {@link Map} The map contains the names of the frequencies as values
     *                           and their corresponding IDs as keys is given
     * @param frequencyTypeMap   {@link Map} The map ontains the frequency type id as key and frequency
     *                           type name as value is given
     * @return {@link List<Map>} The list of map of next followUp details for the given treatmentPlan,
     * frequencyList, patientTracker, staticFrequencyMap and frequencyTypeMap is returned
     */
    public List<Map<String, String>> updateNextFollowupDetails(PatientTreatmentPlan treatmentPlan,
                                                               List<Frequency> frequencyList, PatientTracker patientTracker, Map<Long, String> staticFrequencyMap, Map<String, String> frequencyTypeMap) {
        Logger.logDebug("In PatientTreatmentPlanServiceImpl, updates next follow up details");
        Map<String, Frequency> frequencyMap = new HashMap<>();
        if (!frequencyList.isEmpty()) {
            frequencyList.forEach(frequency -> frequencyMap.put(frequency.getType(), frequency));
            if (!Objects.isNull(frequencyMap.get(Constants.FREQUENCY_MEDICAL_REVIEW))) {
                patientTracker.setNextMedicalReviewDate(DateUtil.getTreatmentPlanFollowupDate(
                        frequencyMap.get(Constants.FREQUENCY_MEDICAL_REVIEW).getPeriod(),
                        frequencyMap.get(Constants.FREQUENCY_MEDICAL_REVIEW).getDuration()));
            }
            if (!Objects.isNull(frequencyMap.get(Constants.FREQUENCY_BP_CHECK))) {
                patientTracker.setNextBpAssessmentDate(DateUtil.getTreatmentPlanFollowupDate(
                        frequencyMap.get(Constants.FREQUENCY_BP_CHECK).getPeriod(),
                        frequencyMap.get(Constants.FREQUENCY_BP_CHECK).getDuration()));
            }
            if (!Constants.BG_PROVISIONAL_FREQUENCY_NAME.equals(treatmentPlan.getBgCheckFrequency())
                    && !Objects.isNull(frequencyMap.get(Constants.BG_PROVISIONAL_FREQUENCY_NAME))) {
                patientTracker.setNextBgAssessmentDate(DateUtil.getTreatmentPlanFollowupDate(
                        frequencyMap.get(Constants.BG_PROVISIONAL_FREQUENCY_NAME).getPeriod(),
                        frequencyMap.get(Constants.BG_PROVISIONAL_FREQUENCY_NAME).getDuration()));
            }
        }
        patientTracker.setLastAssessmentDate(new Date());
        ModelMapper mapper = new ModelMapper();
        List<FrequencyDTO> frequencyDTOs = new ArrayList<>();
        for (Frequency frequency : frequencyList) {
            FrequencyDTO frequencyDTO = mapper.map(frequency, FrequencyDTO.class);
            frequencyDTO.setName(staticFrequencyMap.get(frequency.getId()));
            frequencyDTOs.add(frequencyDTO);
        }
        return constructTreatmentPlanResponse(frequencyDTOs, frequencyTypeMap);
    }

    /**
     * {@inheritDoc}
     */
    public Date getTreatmentPlanFollowupDate(String frequencyName, String frequencyType) {
        Logger.logDebug("In PatientTreatmentPlanServiceImpl, gets treatment plan follow up date");
        Frequency frequency = frequencyService.getFrequencyByFrequencyNameAndType(frequencyName, frequencyType);
        return Objects.isNull(frequency) ? null
                : DateUtil.getTreatmentPlanFollowupDate(frequency.getPeriod(), frequency.getDuration());
    }

    /**
     * <p>
     * This method is used to construct a treatment plan response by mapping frequency data to labels and values.
     * </p>
     *
     * @param frequencies      {@link List<FrequencyDTO>} The list of frequency is given
     * @param frequencyTypeMap {@link Map} The map that maps frequency IDs to their corresponding names
     *                         or labels is given
     * @return {@link List<Map>} The list of map of treatment plan for the given frequencies and
     * frequencyTypeMap is returned
     */
    public List<Map<String, String>> constructTreatmentPlanResponse(List<FrequencyDTO> frequencies, Map<String, String> frequencyTypeMap) {
        Logger.logDebug("In PatientTreatmentPlanServiceImpl, construct patient treatment plan response");
        List<Map<String, String>> responseList = new ArrayList<>();
        for (FrequencyDTO frequency : frequencies) {
            Map<String, String> responseAsMap = new HashMap<>();
            if (!Constants.FREQUENCY_BG_CHECK.equals(frequency.getType())) {
                responseAsMap.put(Constants.LABEL, frequencyTypeMap.get(frequency.getType()));
                responseAsMap.put(Constants.VALUE, frequency.getName());
            } else {
                responseAsMap.put(Constants.LABEL, frequencyTypeMap.get(frequency.getType()));
                responseAsMap.put(Constants.VALUE, Constants.BG_PROVISIONAL_FREQUENCY_NAME);
            }
            responseList.add(responseAsMap);
        }
        return responseList;
    }

    /**
     * {@inheritDoc}
     */
    public boolean updateTreatmentPlanData(PatientTreatmentPlan patientTreatmentPlan) {
        Logger.logDebug("In PatientTreatmentPlanServiceImpl, update patient treatment plan data");
        PatientTreatmentPlan response;
        if (Objects.isNull(patientTreatmentPlan.getId())) {
            patientTreatmentPlan.setLatest(Constants.BOOLEAN_TRUE);
            response = patientTreatmentPlanRepository.save(patientTreatmentPlan);
        } else {
            PatientTreatmentPlan existPatientTreatmentPlan = patientTreatmentPlanRepository
                    .findByIdAndIsDeleted(patientTreatmentPlan.getId(), Constants.BOOLEAN_FALSE);
            if (Objects.isNull(existPatientTreatmentPlan)) {
                throw new DataNotFoundException(1351);
            }
            existPatientTreatmentPlan.setBgCheckFrequency(patientTreatmentPlan.getBgCheckFrequency());
            existPatientTreatmentPlan.setBpCheckFrequency(patientTreatmentPlan.getBpCheckFrequency());
            existPatientTreatmentPlan.setHba1cCheckFrequency(patientTreatmentPlan.getHba1cCheckFrequency());
            existPatientTreatmentPlan.setMedicalReviewFrequency(patientTreatmentPlan.getMedicalReviewFrequency());
            response = patientTreatmentPlanRepository.save(existPatientTreatmentPlan);
        }

        return !Objects.isNull(response);
    }

    /**
     * {@inheritDoc}
     */
    public Date getNextFollowUpDate(long patientTrackId, String freqName) {
        Logger.logDebug("In PatientTreatmentPlanServiceImpl, gets next follow up date");
        PatientTreatmentPlan patientTreatmentPlan = getPatientTreatmentPlan(patientTrackId);
        if (!Objects.isNull(patientTreatmentPlan)) {
            String freqType = Constants.DEFAULT;
            if (freqName.equalsIgnoreCase(Constants.FREQUENCY_MEDICAL_REVIEW)) {
                freqName = patientTreatmentPlan.getMedicalReviewFrequency();
            } else if (freqName.equalsIgnoreCase(Constants.FREQUENCY_BP_CHECK)) {
                freqName = patientTreatmentPlan.getBpCheckFrequency();
            } else if (freqName.equalsIgnoreCase(Constants.FREQUENCY_BG_CHECK)) {
                freqName = patientTreatmentPlan.getBgCheckFrequency();
            }
            return getTreatmentPlanFollowupDate(freqName, freqType);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void removePatientTreatmentPlan(long trackerId) {
        List<PatientTreatmentPlan> existingPatientTreatmentPlan = patientTreatmentPlanRepository
                .findByPatientTrackId(trackerId);
        if (!Objects.isNull(existingPatientTreatmentPlan)) {
            existingPatientTreatmentPlan.forEach(treatmentPlan -> {
                treatmentPlan.setActive(false);
                treatmentPlan.setDeleted(true);
            });
            patientTreatmentPlanRepository.saveAll(existingPatientTreatmentPlan);
        }
    }

    /**
     * <p>
     * This method is used to retrieve a treatment plan for a patient based on frequency type, frequency name, and other
     * parameters.
     * </p>
     *
     * @param frequencyType     {@link String} The frequencyType representing the type of frequency
     *                          (e.g. daily, weekly, monthly). is given
     * @param frequencyName     {@link String}The frequencyName of the frequency being used for the treatment plan.
     *                          is given
     * @param frequencyResponse {@link List<Map>} The list of maps that will be populated with the
     *                          response data. is given
     * @param frequencyMap      {@link Map} The map contains the IDs and names of frequencies.
     *                          is given
     * @param frequencyTypeMap  {@link Map} The map contains the frequency type as key and the
     *                          corresponding frequency type name as value. is given
     * @return {@link Date} The Date for patient treatment plan for the given details is returned
     */
    private Date getTreatmentPlanForPatient(String frequencyType, String frequencyName, List<Map<String, String>> frequencyResponse,
                                            Map<Long, String> frequencyMap, Map<String, String> frequencyTypeMap) {
        Date followUpDate = null;
        if (!Objects.isNull(frequencyTypeMap)) {
            Map<String, String> responseAsMap = new HashMap<>();
            Frequency frequency = frequencyService
                    .getFrequencyByFrequencyNameAndType(frequencyName, Constants.DEFAULT);
            if (!Objects.isNull(frequency)) {
                followUpDate = DateUtil.getTreatmentPlanFollowupDate(frequency.getPeriod(), frequency.getDuration());
                responseAsMap.put(Constants.LABEL, frequencyTypeMap.get(frequencyType));
                responseAsMap.put(Constants.VALUE, frequencyMap.get(frequency.getId()));
                frequencyResponse.add(responseAsMap);
            } else if (Constants.BG_PROVISIONAL_FREQUENCY_NAME.equals(frequencyName)) {
                responseAsMap.put(Constants.LABEL, frequencyTypeMap.get(frequencyType));
                responseAsMap.put(Constants.VALUE, Constants.BG_PROVISIONAL_FREQUENCY_NAME);
                frequencyResponse.add(responseAsMap);
            }
        }
        return followUpDate;
    }

    /**
     * <p>
     * This method is used to create a patient treatment plan with specified parameters.
     * </p>
     *
     * @param tenantId       {@link Long} The ID of the tenant that the patient belongs to
     *                       is given
     * @param patientTrackId {@link Long} patientTrackId is used to retrieve the treatment plan for a specific patient
     *                       is given
     * @param cvdRiskLevel   {@link String} The CVD risk level of a patient is given
     * @param frequencyList  {@link List<Frequency>} The list of Frequency objects that specify the frequency of certain
     *                       medical tests or procedures for the patient is given
     * @return {@link PatientTreatmentPlan} The PatientTreatmentPlan for the given details is returned
     */
    private PatientTreatmentPlan createPatientTreatmentPlan(Long tenantId, Long patientTrackId,
                                                            String cvdRiskLevel, List<Frequency> frequencyList) {
        PatientTreatmentPlan treatmentPlan = new PatientTreatmentPlan();
        treatmentPlan.setTenantId(tenantId);
        treatmentPlan.setPatientTrackId(patientTrackId);
        treatmentPlan.setRiskLevel(cvdRiskLevel);
        treatmentPlan.setBgCheckFrequency(Constants.BG_PROVISIONAL_FREQUENCY_NAME);
        treatmentPlan.setIsProvisional(Constants.BOOLEAN_TRUE);
        setFrequencies(frequencyList, treatmentPlan);
        treatmentPlan = patientTreatmentPlanRepository.save(treatmentPlan);

        return treatmentPlan;
    }
}
