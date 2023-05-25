package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientDetailDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * This is the mapper class to map the entity and POJO class.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 06, 2023
 */
@Component
public class PatientTrackerMapper {

    private static void setAdditionalPatientTrackerFromBpLog(PatientTracker patientTracker, BpLog bpLog) {
        if (!Objects.isNull(bpLog.getAvgDiastolic())) {
            patientTracker.setAvgDiastolic(bpLog.getAvgDiastolic());
        }
        if (!Objects.isNull(bpLog.getAvgPulse())) {
            patientTracker.setAvgPulse(bpLog.getAvgPulse());
        }
        if (!StringUtils.isEmpty(bpLog.getCvdRiskLevel())) {
            patientTracker.setCvdRiskLevel(bpLog.getCvdRiskLevel());
        }
        if (!Objects.isNull(bpLog.getCvdRiskScore())) {
            patientTracker.setCvdRiskScore(bpLog.getCvdRiskScore());
        }
    }

    /**
     * <p>
     * Constructs PatientTracker POJO from BpLog POJO
     * </p>
     *
     * @param bpLog          bpLog
     * @param patientTracker patientTracker
     * @return PatientTracker entity
     */
    public PatientTracker setPatientTrackerFromBpLog(BpLog bpLog, PatientTracker patientTracker) {
        setPatientTrackerFromBpLog(patientTracker, bpLog);
        patientTracker.setLastAssessmentDate(new Date());

        if (!StringUtils.isBlank(bpLog.getRiskLevel()) && !patientTracker.isRedRiskPatient()) {
            patientTracker.setRiskLevel(bpLog.getRiskLevel());
            if (Constants.HIGH.equals(bpLog.getRiskLevel())) {
                patientTracker.setRedRiskPatient(true);
            }
        }
        patientTracker.setTenantId(bpLog.getTenantId());
        patientTracker.setIsRegularSmoker(bpLog.getIsRegularSmoker());

        return patientTracker;
    }

    /**
     * <p>
     * This function sets the patient tracker from a glucose log and updates the next blood glucose
     * assessment date if provided.
     * </p>
     *
     * @param glucoseLog           {@link GlucoseLog} an object of type GlucoseLog that contains information about a patient's
     *                             glucose levels.
     * @param patientTracker       {@link PatientTracker} An object of the class PatientTracker that contains information about a
     *                             patient's glucose levels and other related data. This object will be updated with information
     *                             from the GlucoseLog and the nextBgAssessmentDate.
     * @param nextBgAssessmentDate {@link Date} nextBgAssessmentDate is a Date object representing the date for the
     *                             next blood glucose assessment for a patient.
     * @return {@link PatientTracker} The method is returning a `PatientTracker` object.
     */
    public PatientTracker setPatientTrackerFromGlucose(GlucoseLog glucoseLog, PatientTracker patientTracker,
                                                       Date nextBgAssessmentDate) {
        setPatientTrackerFromGlucoseLog(patientTracker, glucoseLog);
        if (!Objects.isNull(nextBgAssessmentDate)) {
            patientTracker.setNextBgAssessmentDate(nextBgAssessmentDate);
        }
        return patientTracker;
    }

    /**
     * <p>
     * This function sets various attributes of a patient tracker object based on the values of a blood
     * pressure log object.
     * </p>
     *
     * @param patientTracker {@link PatientTracker} an object of type PatientTracker that contains information about a
     *                       patient's health metrics.
     * @param bpLog          {@link BpLog} an object of type BpLog that contains information about a patient's blood pressure
     *                       log, including height, weight, BMI, and average systolic blood pressure.
     */
    public void setPatientTrackerFromBpLog(PatientTracker patientTracker, BpLog bpLog) {
        if (!Objects.isNull(bpLog)) {
            if (!Objects.isNull(bpLog.getHeight())) {
                patientTracker.setHeight(bpLog.getHeight());
            }
            if (!Objects.isNull(bpLog.getWeight())) {
                patientTracker.setWeight(bpLog.getWeight());
            }
            if (!Objects.isNull(bpLog.getBmi())) {
                patientTracker.setBmi(bpLog.getBmi());
            }
            if (!Objects.isNull(bpLog.getAvgSystolic())) {
                patientTracker.setAvgSystolic(bpLog.getAvgSystolic());
            }
            setAdditionalPatientTrackerFromBpLog(patientTracker, bpLog);
        }
    }

    /**
     * <p>
     * This function sets the patient tracker values based on the glucose log values, including the
     * risk level.
     * </p>
     *
     * @param patientTracker {@link PatientTracker} An object of type PatientTracker, which contains information about a
     *                       patient's glucose levels and risk level.
     * @param glucoseLog     {@link GlucoseLog} An object of the class GlucoseLog which contains information about a patient's
     *                       glucose level and related details.
     */
    public void setPatientTrackerFromGlucoseLog(PatientTracker patientTracker, GlucoseLog glucoseLog) {
        if (!Objects.isNull(glucoseLog.getGlucoseValue())) {
            patientTracker.setGlucoseValue(glucoseLog.getGlucoseValue());
        }
        if (!StringUtils.isEmpty(glucoseLog.getGlucoseType())) {
            patientTracker.setGlucoseType(glucoseLog.getGlucoseType());
        }
        if (!StringUtils.isEmpty(glucoseLog.getGlucoseUnit())) {
            patientTracker.setGlucoseUnit(glucoseLog.getGlucoseUnit());
        }
        if (!StringUtils.isBlank(glucoseLog.getRiskLevel()) && !patientTracker.isRedRiskPatient()) {
            patientTracker.setRiskLevel(glucoseLog.getRiskLevel());
            if (Constants.HIGH.equals(glucoseLog.getRiskLevel())) {
                patientTracker.setRedRiskPatient(true);
            }
        }
    }

    /**
     * <p>
     * The function sets the patient details by converting some fields to uppercase and copying values
     * from a patient tracker object to a patient detail DTO object.
     * </p>
     *
     * @param patientTracker {@link PatientTracker} an object of the class PatientTracker that contains details of a patient.
     * @param patientDetails {@link PatientDetailDTO} an object of type PatientDetailDTO that contains the details of a patient.
     */
    public void setPatientDetails(PatientTracker patientTracker, PatientDetailDTO patientDetails) {
        patientDetails.setFirstName(patientTracker.getFirstName().toUpperCase());
        patientDetails.setId(patientTracker.getId());
        patientDetails.setGender(patientTracker.getGender());
        patientDetails.setAge(patientTracker.getAge());
        patientDetails.setLastName(patientTracker.getLastName().toUpperCase());
        patientDetails.setNationalId(patientTracker.getNationalId().toUpperCase());
        patientDetails.setPatientStatus(patientTracker.getPatientStatus());
        patientDetails.setProgramId(patientTracker.getProgramId());
    }

}
