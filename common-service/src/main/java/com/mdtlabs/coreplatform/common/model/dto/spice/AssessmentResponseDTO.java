package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalCompliance;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;

/**
 * <p>
 * Assessment Response DTO class is used to get the Assessment response DTO information.
 * This is the response structure of Assessment information
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Data
public class AssessmentResponseDTO {

    private PatientDetailDTO patientDetails;

    private GlucoseLogDTO glucoseLog;

    private BpLogDTO bpLog;

    private MentalHealthDTO phq4;

    private List<PatientSymptomDTO> symptoms;

    private List<PatientComplianceDTO> medicalCompliance;

    private String riskLevel;

    private String riskMessage;

    private List<String> confirmDiagnosis;

    /**
     * <p>
     * This function sets the symptoms of a patient by mapping a list of PatientSymptom objects to a
     * list of PatientSymptomDTO objects using ModelMapper.
     * </p>
     *
     * @param symptoms {@link List<PatientSymptom>} objects that needs to be mapped to a list of
     *                 PatientSymptomDTO objects.
     */
    public void setSymptoms(List<PatientSymptom> symptoms) {
        ModelMapper mapper = new ModelMapper();
        this.symptoms = mapper.map(symptoms, new TypeToken<List<PatientSymptomDTO>>() {
        }.getType());
    }

    /**
     * <p>
     * This function sets the medical compliance of a patient by mapping a list of PatientMedicalCompliance
     * objects to a list of PatientComplianceDTO objects using ModelMapper.
     * </p>
     *
     * @param medicalCompliance {@link List<PatientMedicalCompliance>} objects of type PatientMedicalCompliance, which is being mapped
     *                          to a list of objects of type PatientComplianceDTO using the ModelMapper library.
     */
    public void setMedicalCompliance(List<PatientMedicalCompliance> medicalCompliance) {
        ModelMapper mapper = new ModelMapper();
        this.medicalCompliance = mapper.map(medicalCompliance, new TypeToken<List<PatientComplianceDTO>>() {
        }.getType());
    }
}
