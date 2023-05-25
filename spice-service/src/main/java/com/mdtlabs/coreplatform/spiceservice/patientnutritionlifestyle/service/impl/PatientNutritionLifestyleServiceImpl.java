package com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.exception.SpiceValidation;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.NutritionLifestyleResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientNutritionLifestyleRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ReviewerDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientNutritionLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.NutritionLifestyleService;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.repository.PatientNutritionLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service.PatientNutritionLifestyleService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * This class implements the Patient Nutrition Lifestyle Service interface and
 * contains actual business logic to perform operations on
 * Patient Nutrition Lifestyle entity.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
@Service
public class PatientNutritionLifestyleServiceImpl implements PatientNutritionLifestyleService {

    @Autowired
    private NutritionLifestyleService nutritionLifestyleService;

    @Autowired
    private PatientNutritionLifestyleRepository patientNutritionLifestyleRepository;

    @Autowired
    private UserApiInterface userApiInterface;

    @Autowired
    private CultureService cultureService;

    @Autowired
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Autowired
    private PatientTrackerService patientTrackerService;

    /**
     * {@inheritDoc}
     */
    public PatientNutritionLifestyle addPatientNutritionLifestyle(PatientNutritionLifestyleRequestDTO request) {
        if (Objects.isNull(request.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        PatientNutritionLifestyle patientNutritionLifestyle = new PatientNutritionLifestyle();
        patientNutritionLifestyle.setPatientTrackId(request.getPatientTrackId());
        patientNutritionLifestyle.setPatientVisitId(request.getPatientVisitId());
        patientNutritionLifestyle.setTenantId(request.getTenantId());
        if (!Objects.isNull(request.getLifestyle())) {
            patientNutritionLifestyle
                    .setLifestyles(nutritionLifestyleService.getNutritionLifestyleByIds(request.getLifestyle()));
        }
        patientNutritionLifestyle.setClinicianNote(request.getClinicianNote());
        patientNutritionLifestyle.setReferredBy(UserContextHolder.getUserDto().getId());
        patientNutritionLifestyle.setReferredDate(request.getReferredDate());
        if (request.isNutritionist()) {
            patientNutritionLifestyle.setAssessedBy(UserContextHolder.getUserDto().getId());
            patientNutritionLifestyle.setAssessedDate(new Date());
            patientNutritionLifestyle.setLifestyleAssessment(request.getLifestyleAssessment());
            patientNutritionLifestyle.setOtherNote(request.getOtherNote());
        }
        if (!Constants.ROLE_NUTRITIONIST.equalsIgnoreCase(request.getRoleName())) {
            updatePatientTracker(request.getPatientTrackId());
        }
        return patientNutritionLifestyleRepository.save(patientNutritionLifestyle);
    }

    /**
     * {@inheritDoc}
     */
    public List<NutritionLifestyleResponseDTO> getPatientNutritionLifeStyleList(RequestDTO request) {
        if (Objects.isNull(request.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        Set<Long> userIds = new HashSet<>();
        Boolean isNutritionHistoryRequired = null;
        Boolean isNutritionist = null;
        if (request.isNutritionHistoryRequired()) {
            isNutritionHistoryRequired = true;
        } else if (request.isNutritionist()) {
            isNutritionist = true;
        }
        List<PatientNutritionLifestyle> patientNutritionLifestyles = patientNutritionLifestyleRepository
                .getPatientNutritionLifestyles(request.getPatientTrackId(), request.getTenantId(),
                        isNutritionHistoryRequired, isNutritionist, null);

        patientNutritionLifestyles.forEach(nutritionLifestyle -> Collections.addAll(userIds,
                nutritionLifestyle.getAssessedBy(), nutritionLifestyle.getReferredBy()));
        userIds.remove(null);
        Map<Long, UserDTO> userMap = userApiInterface
                .getUsers(Constants.BEARER + UserContextHolder.getUserDto().getAuthorization(),
                        UserSelectedTenantContextHolder.get(), userIds)
                .getBody();
        List<NutritionLifestyleResponseDTO> nutritionLifestyleList = new ArrayList<>();
        if (!patientNutritionLifestyles.isEmpty() && Objects.nonNull(userMap)) {
            nutritionLifestyleList = constructNutritionLifestyleResponse(patientNutritionLifestyles, userMap);
        }
        return nutritionLifestyleList;
    }

    /**
     * <p>
     * This method to construct create DTO object for nutrition lifestyle.
     * </p>
     *
     * @param patientNutritionLifestyles patientNutritionLifestyles
     * @param userMap                    userMap
     * @return List of NutritionLifestyleResponseDTO
     */
    private List<NutritionLifestyleResponseDTO> constructNutritionLifestyleResponse(List<PatientNutritionLifestyle> patientNutritionLifestyles,
                                                                                    Map<Long, UserDTO> userMap) {
        List<NutritionLifestyleResponseDTO> nutritionLifestyleList = new ArrayList<>();
        Long cultureId = cultureService.loadCultureValues();
        Map<Long, String> cultureMap = Constants.CULTURE_VALUES_MAP.get(cultureId).get(Constants.NUTRITION_LIFESTYLE);
        for (PatientNutritionLifestyle patientNutritionLifestyle : patientNutritionLifestyles) {
            NutritionLifestyleResponseDTO nutritionLifestyleResponse = new NutritionLifestyleResponseDTO();
            nutritionLifestyleResponse.setId(patientNutritionLifestyle.getId());
            nutritionLifestyleResponse.setLifestyle(patientNutritionLifestyle.getLifestyles().stream()
                    .map(lifestyle -> Map.of(FieldConstants.NAME, lifestyle.getName(), Constants.CULTURE_VALUE,
                            cultureMap.get(lifestyle.getId()))).toList());
            nutritionLifestyleResponse.setReferredDate(patientNutritionLifestyle.getReferredDate());
            nutritionLifestyleResponse.setAssessedDate(patientNutritionLifestyle.getAssessedDate());
            nutritionLifestyleResponse.setClinicianNote(patientNutritionLifestyle.getClinicianNote());
            nutritionLifestyleResponse.setLifestyleAssessment(patientNutritionLifestyle.getLifestyleAssessment());
            nutritionLifestyleResponse.setOtherNote(patientNutritionLifestyle.getOtherNote());
            UserDTO referredBy = userMap.get(patientNutritionLifestyle.getReferredBy());
            if (!Objects.isNull(patientNutritionLifestyle.getAssessedBy())) {
                UserDTO assessedBy = userMap.get(patientNutritionLifestyle.getAssessedBy());
                nutritionLifestyleResponse
                        .setAssessedBy(new ReviewerDetailsDTO(assessedBy.getFirstName(), assessedBy.getLastName()));
            }
            nutritionLifestyleResponse
                    .setReferredBy(new ReviewerDetailsDTO(referredBy.getFirstName(), referredBy.getLastName()));
            nutritionLifestyleList.add(nutritionLifestyleResponse);
        }
        return nutritionLifestyleList;
    }

    /**
     * {@inheritDoc}
     */
    public List<PatientNutritionLifestyle> updatePatientNutritionLifestyle(
            PatientNutritionLifestyleRequestDTO patientNutritionLifestyleRequestDTO) {
        if (Objects.isNull(patientNutritionLifestyleRequestDTO)) {
            throw new SpiceValidation(1454);
        }
        if (Objects.isNull(patientNutritionLifestyleRequestDTO.getPatientTrackId())
                || Objects.isNull(patientNutritionLifestyleRequestDTO.getPatientVisitId())) {
            throw new DataNotAcceptableException(1502);
        }

        boolean isInvalidIds = patientNutritionLifestyleRequestDTO.getLifestyles().stream()
                .anyMatch(lifestyle -> Objects.isNull(lifestyle.getId()));
        if (isInvalidIds) {
            throw new DataNotAcceptableException(1453);
        }

        boolean isInvalidLifestyleAssessment = patientNutritionLifestyleRequestDTO.getLifestyles().stream()
                .anyMatch(lifestyle -> Objects.isNull(lifestyle.getLifestyleAssessment()));
        if (isInvalidLifestyleAssessment) {
            throw new DataNotAcceptableException(1451);
        }
        List<PatientNutritionLifestyle> updatedPatientNutritionLifestyles = constructUpdateNutritionDTO(patientNutritionLifestyleRequestDTO);
        return patientNutritionLifestyleRepository.saveAll(updatedPatientNutritionLifestyles);
    }

    /**
     * <p>
     * This method to construct update DTO object for nutrition lifestyle.
     * </p>
     *
     * @param patientNutritionLifestyleRequestDTO patientNutritionLifestyleRequestDTO
     * @return List of PatientNutritionLifestyle
     */
    private List<PatientNutritionLifestyle> constructUpdateNutritionDTO(
            PatientNutritionLifestyleRequestDTO patientNutritionLifestyleRequestDTO) {
        List<Long> ids = patientNutritionLifestyleRequestDTO.getLifestyles().stream()
                .map(BaseEntity::getId).toList();
        Map<Long, PatientNutritionLifestyle> updatedRequest = new HashMap<>();
        for (PatientNutritionLifestyle lifestyle : patientNutritionLifestyleRequestDTO.getLifestyles()) {
            updatedRequest.put(lifestyle.getId(), lifestyle);
        }

        List<PatientNutritionLifestyle> patientNutritionLifestyles = patientNutritionLifestyleRepository
                .getPatientNutritionLifestyleByIds(ids, patientNutritionLifestyleRequestDTO.getPatientTrackId());
        List<PatientNutritionLifestyle> updatedPatientNutritionLifestyles = new ArrayList<>();
        for (PatientNutritionLifestyle patientNutritionLifestyle : patientNutritionLifestyles) {
            PatientNutritionLifestyle lifestyle = updatedRequest.get(patientNutritionLifestyle.getId());
            patientNutritionLifestyle.setPatientVisitId(patientNutritionLifestyleRequestDTO.getPatientVisitId());
            patientNutritionLifestyle.setAssessedBy(UserContextHolder.getUserDto().getId());
            patientNutritionLifestyle.setAssessedDate(new Date());
            patientNutritionLifestyle.setLifestyleAssessment(lifestyle.getLifestyleAssessment());
            patientNutritionLifestyle.setOtherNote(lifestyle.getOtherNote());
            updatedPatientNutritionLifestyles.add(patientNutritionLifestyle);
        }
        return updatedPatientNutritionLifestyles;
    }

    /**
     * {@inheritDoc}
     */
    public boolean updatePatientNutritionLifestyleView(CommonRequestDTO request) {
        if (Objects.isNull(request.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        List<PatientNutritionLifestyle> patientNutritionLifestyleList = patientNutritionLifestyleRepository
                .getPatientNutritionLifestyles(request.getPatientTrackId(), request.getTenantId(),
                        Constants.BOOLEAN_TRUE, null, Constants.BOOLEAN_FALSE);
        List<PatientNutritionLifestyle> lifestyleToUpdate = new ArrayList<>();
        for (PatientNutritionLifestyle patientNutritionLifestyle : patientNutritionLifestyleList) {
            if (!Objects.isNull(patientNutritionLifestyle.getAssessedDate())) {
                patientNutritionLifestyle.setViewed(Constants.BOOLEAN_TRUE);
                patientNutritionLifestyle.setTenantId(UserSelectedTenantContextHolder.get());
                lifestyleToUpdate.add(patientNutritionLifestyle);
            }
        }
        patientNutritionLifestyleRepository.saveAll(lifestyleToUpdate);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean removePatientNutritionLifestyle(PatientNutritionLifestyle patientNutritionLifestyle) {
        if (Objects.isNull(patientNutritionLifestyle.getId())) {
            throw new DataNotFoundException(1453);
        }
        if (Objects.isNull(patientNutritionLifestyle.getPatientTrackId())
                || Objects.isNull(patientNutritionLifestyle.getPatientVisitId())) {
            throw new DataNotAcceptableException(1502);
        }
        PatientNutritionLifestyle existingPatientNutritionLifestyle = patientNutritionLifestyleRepository
                .findByIdAndPatientTrackId(patientNutritionLifestyle.getId(),
                        patientNutritionLifestyle.getPatientTrackId());
        existingPatientNutritionLifestyle.setDeleted(Constants.BOOLEAN_TRUE);
        existingPatientNutritionLifestyle.setPatientVisitId(patientNutritionLifestyle.getPatientVisitId());
        patientNutritionLifestyleRepository.save(existingPatientNutritionLifestyle);
        updatePatientTracker(patientNutritionLifestyle.getPatientTrackId());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public int getNutritionLifestyleReviewedCount(Long patientTrackId) {
        return patientNutritionLifestyleRepository.getNutritionLifestyleReviewedCount(patientTrackId);
    }

    /**
     * Updates patientTracker for last and next medical review dates
     *
     * @param id id
     */
    private void updatePatientTracker(long id) {
        Date nextMedicalReviewDate = patientTreatmentPlanService.getNextFollowUpDate(id,
                Constants.MEDICAL_REVIEW_FREQUENCY);
        PatientTracker patientTracker = patientTrackerService.getPatientTrackerById(id);
        patientTracker.setLastReviewDate(new Date());
        if (!Objects.isNull(nextMedicalReviewDate)) {
            patientTracker.setNextMedicalReviewDate(nextMedicalReviewDate);
        }
        patientTrackerService.addOrUpdatePatientTracker(patientTracker);
    }

    /**
     * {@inheritDoc}
     */
    public void removeNutritionLifestyleByTrackerId(long trackerId) {
        List<PatientNutritionLifestyle> patientNutritionLifeStyles = patientNutritionLifestyleRepository
                .findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientNutritionLifeStyles)) {
            patientNutritionLifeStyles.forEach(lifeStyle -> {
                lifeStyle.setActive(false);
                lifeStyle.setDeleted(true);
            });
            patientNutritionLifestyleRepository.saveAll(patientNutritionLifeStyles);
        }
    }
}
