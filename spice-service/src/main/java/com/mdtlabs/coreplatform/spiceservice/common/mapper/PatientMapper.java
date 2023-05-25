package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.DiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientDetailDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * This class is a mapper to map Entity and POJO used for Patient level
 * operations.
 *
 * @author Niraimathi S created on Feb 07, 2023
 */
@Component
public class PatientMapper {

    /**
     * <p>
     * The function sets the properties of a Patient object based on the data provided in an
     * EnrollmentRequestDTO object.
     * </p>
     *
     * @param data {@link EnrollmentRequestDTO} an object of type EnrollmentRequestDTO that contains the data needed to create a new
     *             patient record is given
     * @return {@link Patient} An instance of the Patient class is being returned
     */
    public Patient setPatient(EnrollmentRequestDTO data) {
        Patient patient = new Patient();
        if (!Objects.isNull(data.getBioData())) {
            patient.setNationalId(data.getBioData().getNationalId());
            patient.setFirstName(data.getBioData().getFirstName());
            patient.setMiddleName(data.getBioData().getMiddleName());
            patient.setLastName(data.getBioData().getLastName());
            patient.setGender(data.getGender());
            patient.setPhoneNumber(data.getBioData().getPhoneNumber());
            patient.setPhoneNumberCategory(data.getBioData().getPhoneNumberCategory());
            patient.setCountryId(data.getBioData().getCountry());
            patient.setCountyId(data.getBioData().getCounty());
            patient.setSubCountyId(data.getBioData().getSubCounty());
            patient.setLandmark(data.getBioData().getLandmark());
            patient.setOccupation(data.getBioData().getOccupation());
            patient.setLevelOfEducation(data.getBioData().getLevelOfEducation());
            patient.setInsuranceStatus(data.getBioData().getInsuranceStatus());
            patient.setInsuranceType(data.getBioData().getInsuranceType());
            patient.setInsuranceId(data.getBioData().getInsuranceId());
            patient.setOtherInsurance(data.getBioData().getOtherInsurance());
            patient.setProgramId(data.getBioData().getProgramId());
            patient.setInitial(data.getBioData().getInitial());
        }
        patient.setSiteId(data.getSiteId());
        patient.setTenantId(data.getTenantId());
        patient.setIsRegularSmoker(data.getIsRegularSmoker());
        patient.setDateOfBirth(data.getDateOfBirth());
        patient.setAge(data.getAge());
        patient.setIsPregnant(data.getIsPregnant());
        return patient;
    }

    /**
     * <p>
     * This function sets various properties of a BpLog object based on data from an
     * EnrollmentRequestDTO object.
     * </p>
     *
     * @param data  {@link EnrollmentRequestDTO} an object of type EnrollmentRequestDTO which contains data related to an enrollment
     *              request is given
     * @param bpLog {@link BpLog} an object of type BpLog that contains blood pressure log data is given
     * @return {@link BpLog} The method is returning a BpLog object is returned
     */
    public BpLog setBpLog(EnrollmentRequestDTO data, BpLog bpLog) {
        if (!Objects.isNull(bpLog.getId())) {
            bpLog.setUpdatedFromEnrollment(Constants.BOOLEAN_TRUE);
        } else {
            bpLog.setType(Constants.ENROLLMENT);
        }
        bpLog.setTenantId(data.getTenantId());
        bpLog.setCvdRiskLevel(data.getCvdRiskLevel());
        bpLog.setCvdRiskScore(data.getCvdRiskScore());
        bpLog.setIsRegularSmoker(data.getIsRegularSmoker());
        bpLog.setLatest(Constants.BOOLEAN_TRUE);
        return bpLog;
    }

    /**
     * <p>
     * This function sets various properties of a PatientTracker object based on data from an
     * EnrollmentRequestDTO object and a Site object.
     * </p>
     *
     * @param requestData    {@link EnrollmentRequestDTO} an object of type EnrollmentRequestDTO that contains information about a
     *                       patient's enrollment request is given
     * @param patientTracker {@link PatientTracker} an object of the class PatientTracker that stores information about a
     *                       patient's health status and enrollment details is given
     * @param site           {@link Site} The site parameter is an object of the Site class, which contains information about
     *                       the site where the patient is being enrolled, such as the site ID, account ID, and operating
     *                       unit ID is returned
     */
    public void setPatientTracker(EnrollmentRequestDTO requestData,
                                  PatientTracker patientTracker, Site site) {
        patientTracker.setNationalId(requestData.getBioData().getNationalId());
        patientTracker.setFirstName(requestData.getBioData().getFirstName());
        patientTracker.setLastName(requestData.getBioData().getLastName());
        patientTracker.setAge(requestData.getAge());
        patientTracker.setGender(requestData.getGender());
        patientTracker.setIsRegularSmoker(requestData.getIsRegularSmoker());
        patientTracker.setPhoneNumber(requestData.getBioData().getPhoneNumber());
        patientTracker.setPatientStatus(Constants.ENROLLED);
        patientTracker.setCountryId(requestData.getBioData().getCountry());
        patientTracker.setSiteId(requestData.getSiteId());
        patientTracker.setTenantId(requestData.getTenantId());
        patientTracker.setEnrollmentAt(new Date());
        patientTracker.setIsPregnant(requestData.getIsPregnant());
        patientTracker.setDateOfBirth(requestData.getDateOfBirth());
        patientTracker.setHeight(requestData.getBpLog().getHeight());
        patientTracker.setWeight(requestData.getBpLog().getWeight());
        patientTracker.setAvgSystolic(requestData.getBpLog().getAvgSystolic());
        patientTracker.setAvgDiastolic(requestData.getBpLog().getAvgDiastolic());
        patientTracker.setAvgPulse(requestData.getBpLog().getAvgPulse());
        patientTracker.setBmi(requestData.getBpLog().getBmi());
        patientTracker.setCvdRiskLevel(requestData.getCvdRiskLevel());
        patientTracker.setCvdRiskScore(requestData.getCvdRiskScore());
        patientTracker.setIsScreening(!Objects.isNull(patientTracker.getScreeningLogId()));
        patientTracker.setAccountId(site.getAccountId());
        patientTracker.setOperatingUnitId(site.getOperatingUnit().getId());
        if (!Objects.isNull(requestData.getGlucoseLog())) {
            patientTracker.setGlucoseValue(requestData.getGlucoseLog().getGlucoseValue());
            patientTracker.setGlucoseUnit(requestData.getGlucoseLog().getGlucoseUnit());
            patientTracker.setGlucoseType(requestData.getGlucoseLog().getGlucoseType());
        }
        if (!Objects.isNull(requestData.getProvisionalDiagnosis())
                && !requestData.getProvisionalDiagnosis().isEmpty()) {
            patientTracker.setProvisionalDiagnosis(requestData.getProvisionalDiagnosis());

        }
    }

    /**
     * <p>
     * This function sets the properties of a GlucoseLog object based on an EnrollmentRequestDTO
     * object.
     * </p>
     *
     * @param  {@link EnrollmentRequestDTO} data an object of type EnrollmentRequestDTO which contains information about the
     * enrollment request, including the tenant ID.
     * @param {@link GlucoseLog} glucoseLog an instance of the GlucoseLog class that needs to be updated or created
     * @return {@link GlucoseLog} The method is returning a GlucoseLog object.
     */
    public GlucoseLog setGlucoseLog (EnrollmentRequestDTO data, GlucoseLog glucoseLog){
        if (!Objects.isNull(glucoseLog.getId())) {
            glucoseLog.setUpdatedFromEnrollment(Constants.BOOLEAN_TRUE);
        } else {
            glucoseLog.setType(Constants.ENROLLMENT);
        }
        glucoseLog.setTenantId(data.getTenantId());
        glucoseLog.setLatest(Constants.BOOLEAN_TRUE);
        return glucoseLog;
    }

    /**
     * <p>
     * Maps the patientDetailDTO from patient object.
     * </p>
     *
     * @param {@link Patient}  Patient details
     * @return {@link PatientDetailDTO}  PatientDetailDTO details
     */
    public PatientDetailDTO setPatientDetailDto (Patient patient){
        PatientDetailDTO patientDetailDto = new PatientDetailDTO();
        patientDetailDto.setFirstName(patient.getFirstName().toUpperCase());
        patientDetailDto.setMiddleName(null == patient.getMiddleName() ?
                null : patient.getMiddleName().toUpperCase());
        patientDetailDto.setId(patient.getId());
        patientDetailDto.setGender(patient.getGender());
        patientDetailDto.setAge(patient.getAge());
        patientDetailDto.setLastName(patient.getLastName().toUpperCase());
        patientDetailDto.setNationalId(patient.getNationalId().toUpperCase());
        patientDetailDto.setEnrollmentDate(patient.getCreatedAt());
        patientDetailDto.setProgramId(patient.getProgramId());
        patientDetailDto.setVirtualId(patient.getVirtualId());
        return patientDetailDto;
    }

    /**
     * <p>
     * Maps the Patient Diagnosis from diagnosis DTO.
     * </p>
     *
     * @param {@link DiagnosisDTO} diagnosisDto diagnosisDto
     * @return {@link PatientDiagnosis} PatientDiagnosis
     */
    public PatientDiagnosis setPatientDiagnosis (DiagnosisDTO diagnosisDto){
        PatientDiagnosis patientDiagnosis = new PatientDiagnosis();
        patientDiagnosis.setDiabetesDiagControlledType(diagnosisDto.getDiabetesDiagControlledType());
        patientDiagnosis.setDiabetesDiagnosis(diagnosisDto.getDiabetesDiagnosis());
        patientDiagnosis.setDiabetesPatientType(diagnosisDto.getDiabetesPatientType());
        patientDiagnosis.setDiabetesYearOfDiagnosis(diagnosisDto.getDiabetesYearOfDiagnosis());
        patientDiagnosis.setHtnPatientType(diagnosisDto.getHtnPatientType());
        patientDiagnosis.setHtnYearOfDiagnosis(diagnosisDto.getHtnYearOfDiagnosis());
        patientDiagnosis.setIsDiabetesDiagnosis(diagnosisDto.getIsDiabetesDiagnosis());
        patientDiagnosis.setIsHtnDiagnosis(diagnosisDto.getIsHtnDiagnosis());
        return patientDiagnosis;
    }
}
