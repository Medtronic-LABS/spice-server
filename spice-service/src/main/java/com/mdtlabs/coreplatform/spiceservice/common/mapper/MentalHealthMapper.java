package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.MentalHealthDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * This is the mapper class to map the entity and POJO class.
 *
 * @author Jeyaharini T A created on Feb 06, 2023
 */
@Component
public class MentalHealthMapper {

    /**
     * <p>
     * This function sets the mental health scores and risk levels of a patient in a patient tracker
     * object.
     * </p>
     *
     * @param patientTracker {@link PatientTracker} an object of type PatientTracker that contains information about a
     *                       patient's mental health tracking is given
     * @param mentalHealth   {@link MentalHealth} an object representing the mental health information of a patient, which
     *                       includes PHQ-4, PHQ-9, and GAD-7 scores and risk levels is given
     * @return {@link PatientTracker} The method is returning a PatientTracker object is returned
     */
    public PatientTracker setPatientTracker(PatientTracker patientTracker, MentalHealth mentalHealth) {
        patientTracker.setTenantId(mentalHealth.getTenantId());
        if (!Objects.isNull(mentalHealth.getPhq4MentalHealth()) && !mentalHealth.getPhq4MentalHealth().isEmpty()) {
            patientTracker.setPhq4FirstScore(mentalHealth.getPhq4FirstScore());
            patientTracker.setPhq4SecondScore(mentalHealth.getPhq4SecondScore());
            patientTracker.setPhq4Score(mentalHealth.getPhq4Score());
            patientTracker.setPhq4RiskLevel(mentalHealth.getPhq4RiskLevel());
            patientTracker.setPhq9Score(null);
            patientTracker.setPhq9RiskLevel(null);
            patientTracker.setGad7Score(null);
            patientTracker.setGad7RiskLevel(null);
        }
        if (!Objects.isNull(mentalHealth.getPhq9MentalHealth()) && !mentalHealth.getPhq9MentalHealth().isEmpty()) {
            patientTracker.setPhq9Score(mentalHealth.getPhq9Score());
            patientTracker.setPhq9RiskLevel(mentalHealth.getPhq9RiskLevel());
        }
        if (!Objects.isNull(mentalHealth.getGad7MentalHealth()) && !mentalHealth.getGad7MentalHealth().isEmpty()) {
            patientTracker.setGad7Score(mentalHealth.getGad7Score());
            patientTracker.setGad7RiskLevel(mentalHealth.getGad7RiskLevel());
        }
        return patientTracker;
    }

    /**
     * <p>
     * This function sets the values of a MentalHealthDTO object based on the type of mental health
     * assessment requested and the corresponding values in a MentalHealth object.
     * </p>
     *
     * @param mentalHealthDto {@link MentalHealthDTO} an object of type MentalHealthDTO that will be updated with data from the
     *                        MentalHealth object is given
     * @param requestData     {@linik RequestDTO} An object of type RequestDTO that contains information about the type of
     *                        mental health assessment being performed is given
     * @param mentalHealth    {@link MentalHealth} an object of type MentalHealth, which contains information about a user's
     *                        mental health assessment scores is given
     * @return {@link MentalHealthDTO} The method is returning a MentalHealthDTO object is returned
     */
    public MentalHealthDTO setMentalHealthDTO(MentalHealthDTO mentalHealthDto, RequestDTO requestData,
                                              MentalHealth mentalHealth) {

        mentalHealthDto.setId(mentalHealth.getId());
        if (requestData.getType().equals(Constants.PHQ4)) {
            mentalHealthDto.setPhq4MentalHealth(mentalHealth.getPhq4MentalHealth());
            mentalHealthDto.setPhq4RiskLevel(mentalHealth.getPhq4RiskLevel());
            mentalHealthDto.setPhq4Score(mentalHealth.getPhq4Score());
        }
        if (requestData.getType().equals(Constants.PHQ9)) {
            mentalHealthDto.setPhq9MentalHealth(mentalHealth.getPhq9MentalHealth());
            mentalHealthDto.setPhq9RiskLevel(mentalHealth.getPhq9RiskLevel());
            mentalHealthDto.setPhq9Score(mentalHealth.getPhq9Score());
        }
        if (requestData.getType().equals(Constants.GAD7)) {
            mentalHealthDto.setGad7MentalHealth(mentalHealth.getGad7MentalHealth());
            mentalHealthDto.setGad7RiskLevel(mentalHealth.getGad7RiskLevel());
            mentalHealthDto.setGad7Score(mentalHealth.getGad7Score());
        }

        return mentalHealthDto;
    }

}
