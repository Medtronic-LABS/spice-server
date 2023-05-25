package com.mdtlabs.coreplatform.spiceservice.mentalhealth.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.MentalHealthDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealthDetails;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.MentalHealthMapper;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.repository.MentalHealthRepository;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.service.MentalHealthService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * This class implements the mental health service class and contains business
 * logic for the operations of mental health entity.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 07, 2023
 */
@Service
public class MentalHealthServiceImpl implements MentalHealthService {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private MentalHealthMapper mentalHealthMapper;

    @Autowired
    private MentalHealthRepository mentalHealthRepository;

    @Autowired
    private PatientTrackerService patientTrackerService;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public MentalHealth createOrUpdateMentalHealth(MentalHealth mentalHealth) {
        if (Objects.isNull(mentalHealth.getPatientTrackId())) {
            throw new BadRequestException(1256);
        }
        PatientTracker patientTracker = patientTrackerService.getPatientTrackerById(mentalHealth.getPatientTrackId());
        setPhq4Score(mentalHealth);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        MentalHealth newMentalHealth;
        if (Objects.isNull(mentalHealth.getId())) {
            MentalHealth oldMentalHealth = mentalHealthRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(
                    mentalHealth.getPatientTrackId(), Constants.BOOLEAN_FALSE, Constants.BOOLEAN_TRUE);
            if (!Objects.isNull(oldMentalHealth)) {
                mapper.map(mentalHealth, oldMentalHealth);
                oldMentalHealth.setLatest(Constants.BOOLEAN_TRUE);
                newMentalHealth = mentalHealthRepository.save(oldMentalHealth);
            } else {
                newMentalHealth = createMentalHealth(mentalHealth, patientTracker, Constants.BOOLEAN_FALSE);
            }
        } else {
            Logger.logInfo("Updating mental health for ID " + mentalHealth.getId());
            MentalHealth existingMentalHealth = mentalHealthRepository.findByIdAndIsDeleted(mentalHealth.getId(),
                    Constants.BOOLEAN_FALSE);
            if (Objects.isNull(existingMentalHealth)) {
                throw new DataNotFoundException(1701);
            }
            boolean isLatest = existingMentalHealth.isLatest();
            mapper.map(mentalHealth, existingMentalHealth);
            existingMentalHealth.setLatest(isLatest);
            newMentalHealth = mentalHealthRepository.save(existingMentalHealth);
        }
        patientTrackerService
                .addOrUpdatePatientTracker(mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth));
        return newMentalHealth;
    }

    /**
     * {@inheritDoc}
     */
    public void setPhq4Score(MentalHealth mentalHealth) {
        mentalHealth.setPhq4FirstScore(Constants.ZERO);
        mentalHealth.setPhq4SecondScore(Constants.ZERO);
        if (!Objects.isNull(mentalHealth.getPhq4MentalHealth())) {
            for (MentalHealthDetails mentalHealthDetails : mentalHealth.getPhq4MentalHealth()) {
                if (Constants.ONE == mentalHealthDetails.getDisplayOrder()
                        || Constants.TWO == mentalHealthDetails.getDisplayOrder()) {
                    mentalHealth.setPhq4FirstScore(mentalHealth.getPhq4FirstScore() + mentalHealthDetails.getScore());
                } else if (Constants.THREE == mentalHealthDetails.getDisplayOrder()
                        || Constants.FOUR == mentalHealthDetails.getDisplayOrder()) {
                    mentalHealth.setPhq4SecondScore(mentalHealth.getPhq4SecondScore() + mentalHealthDetails.getScore());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public MentalHealthDTO getMentalHealthDetails(RequestDTO requestData) {
        if (Objects.isNull(requestData.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        MentalHealth mentalHealth = mentalHealthRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(
                requestData.getPatientTrackId(), Constants.BOOLEAN_FALSE, Constants.BOOLEAN_TRUE);
        MentalHealthDTO mentalHealthDto = new MentalHealthDTO();
        if (!Objects.isNull(mentalHealth)) {
            mentalHealthDto = mentalHealthMapper.setMentalHealthDTO(mentalHealthDto, requestData, mentalHealth);
        }
        return mentalHealthDto;
    }

    /**
     * {@inheritDoc}
     */
    public MentalHealth createMentalHealth(MentalHealth mentalHealth, PatientTracker patientTracker,
                                           boolean updateLatestStatus) {
        Logger.logInfo("Creating new mental health.");
        setPhq4Score(mentalHealth);
        if (updateLatestStatus) {
            MentalHealth existingMentalHealth = mentalHealthRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(
                    patientTracker.getId(), Constants.BOOLEAN_FALSE, Constants.BOOLEAN_TRUE);
            if (!Objects.isNull(existingMentalHealth)) {
                existingMentalHealth.setLatest(Constants.BOOLEAN_FALSE);
                mentalHealthRepository.save(existingMentalHealth);
            }
        }
        mentalHealth.setPatientTrackId(patientTracker.getId());
        mentalHealth.setLatest(Constants.BOOLEAN_TRUE);
        mentalHealth.setAssessmentTenantId(UserSelectedTenantContextHolder.get());
        return mentalHealthRepository.save(mentalHealth);
    }

    /**
     * {@inheritDoc}
     */
    public void removeMentalHealth(long trackerId) {
        List<MentalHealth> mentalHealths = mentalHealthRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(mentalHealths)) {
            mentalHealths.forEach(mentalHealth -> {
                mentalHealth.setActive(false);
                mentalHealth.setDeleted(true);
            });
            mentalHealthRepository.saveAll(mentalHealths);
        }
    }

}
