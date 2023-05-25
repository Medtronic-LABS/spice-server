package com.mdtlabs.coreplatform.spiceservice.common;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.RedRiskDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComorbidityRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientDiagnosisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * This an interface class for red risk module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class RedRiskService {

    @Autowired
    private PatientDiagnosisRepository patientDiagnosisRepository;

    @Autowired
    private PatientComorbidityRepository patientComorbidityRepository;

    @Autowired
    private RiskAlgorithm riskAlgorithm;

    /**
     * <p>
     * This function calculates the risk level of a patient based on their diabetes diagnosis and
     * comorbidities.
     * </p>
     *
     * @param patientTracker   {@link PatientTracker} An object that tracks information about a patient, such as their ID and
     *                         current status is given
     * @param patientDiagnosis {@link PatientDiagnosis} An object containing the diagnosis information of a patient is given
     * @param redRiskDto       {@link RedRiskDTO} A DTO (Data Transfer Object) containing information related to the patient's
     *                         risk level, including their diabetes patient type, diabetes diagnosis control type, and number
     *                         of comorbidities is given
     * @return {@link String} The method is returning a String which is the risk level of a patient calculated using a
     * risk algorithm is returned
     */
    public String getPatientRiskLevel(PatientTracker patientTracker, PatientDiagnosis patientDiagnosis,
                                      RedRiskDTO redRiskDto) {
        if (!Objects.isNull(redRiskDto.getDiabetesPatientType())
                && !redRiskDto.getDiabetesPatientType().equals(Constants.KNOWN_DIABETES_PATIENT)) {
            if (!Objects.isNull(patientDiagnosis)) {
                patientDiagnosis = patientDiagnosisRepository
                        .findByPatientTrackIdAndIsActiveAndIsDeleted(
                                patientTracker.getId(), true, false);
            }

            List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository
                    .getByTrackerId(patientTracker.getId(), Constants.BOOLEAN_TRUE,
                            Constants.BOOLEAN_FALSE);
            redRiskDto.setDiabetesDiagControlledType(!Objects.isNull(patientDiagnosis)
                    && !Objects.isNull(patientDiagnosis.getDiabetesDiagControlledType())
                    ? patientDiagnosis.getDiabetesDiagControlledType() : Constants.EMPTY);
            redRiskDto.setComorbiditiesCount(patientComorbidityList.size());

        }
        return riskAlgorithm.getRiskLevelForNewPatient(patientTracker, redRiskDto);
    }
}