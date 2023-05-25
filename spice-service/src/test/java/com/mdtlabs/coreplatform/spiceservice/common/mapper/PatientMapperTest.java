package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.BioDataDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.DiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientDetailDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * <p>
 * PatientMapperTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientMapper class.
 * </p>
 *
 * @author Jaganathan created on Mar 2, 2023
 */
@ExtendWith(MockitoExtension.class)
class PatientMapperTest {

    @InjectMocks
    private PatientMapper patientMapper;

    @Test
    @DisplayName("SetPatient Test")
    void setPatient() {
        //given
        BioDataDTO bioDataDTO = null;
        EnrollmentRequestDTO enrollmentRequestDTO = TestDataProvider.getEnrollmentRequestDto();
        enrollmentRequestDTO.setBioData(bioDataDTO);
        Patient patient = TestDataProvider.getPatient();
        //then
        Patient actualPatient = patientMapper.setPatient(enrollmentRequestDTO);
        Assertions.assertNull(bioDataDTO);
        Assertions.assertNull(actualPatient.getNationalId());
        Assertions.assertEquals(patient.getSiteId(), actualPatient.getSiteId());
        //given
        bioDataDTO = TestDataProvider.getBioData();
        enrollmentRequestDTO.setBioData(bioDataDTO);
        //then
        actualPatient = patientMapper.setPatient(enrollmentRequestDTO);
        Assertions.assertNotNull(bioDataDTO);
        Assertions.assertEquals(patient.getSiteId(), actualPatient.getSiteId());
        Assertions.assertEquals(patient.getNationalId().toUpperCase(), actualPatient.getNationalId());
    }

    @Test
    @DisplayName("SetPatientDaignosis Test")
    void setPatientDaignosis() {
        //given
        DiagnosisDTO diagnosisDTO = TestDataProvider.getDiagnosisDTO();
        PatientDiagnosis patientDiagnosis = TestDataProvider.getPatientDiagnosis();
        //then
        PatientDiagnosis actualDiagnosis = patientMapper.setPatientDiagnosis(diagnosisDTO);
        Assertions.assertEquals(patientDiagnosis.getHtnYearOfDiagnosis(), actualDiagnosis.getHtnYearOfDiagnosis());
        Assertions.assertEquals(patientDiagnosis.getHtnPatientType(), actualDiagnosis.getHtnPatientType());
        Assertions.assertEquals(patientDiagnosis.getIsHtnDiagnosis(), actualDiagnosis.getIsHtnDiagnosis());
        Assertions.assertEquals(patientDiagnosis.getIsDiabetesDiagnosis(), actualDiagnosis.getIsDiabetesDiagnosis());
    }

    @Test
    @DisplayName("SetBpLog Test")
    void setBpLog() {
        //given
        BpLog bpLog = TestDataProvider.getBpLog();
        bpLog.setId(null);
        EnrollmentRequestDTO enrollmentRequestDTO = TestDataProvider.getEnrollmentRequestDto();
        enrollmentRequestDTO.setBpLog(bpLog);
        bpLog.setCvdRiskLevel(enrollmentRequestDTO.getCvdRiskLevel());
        bpLog.setCvdRiskScore(enrollmentRequestDTO.getCvdRiskScore());
        bpLog.setIsRegularSmoker(enrollmentRequestDTO.getIsRegularSmoker());
        bpLog.setType(Constants.ENROLLMENT);
        bpLog.setLatest(Constants.BOOLEAN_TRUE);
        //then
        patientMapper.setBpLog(enrollmentRequestDTO, bpLog);
        //given
        enrollmentRequestDTO.getBpLog().setId(1L);
        bpLog.setId(bpLog.getId());
        bpLog.setUpdatedFromEnrollment(Constants.BOOLEAN_TRUE);
        //then
        BpLog response = patientMapper.setBpLog(enrollmentRequestDTO, bpLog);
        Assertions.assertTrue(response.getIsRegularSmoker());
        Assertions.assertNotNull(response.getBpLogDetails());
        Assertions.assertEquals(null, response.getBpLogId());
    }

    @Test
    @DisplayName("SetPatientDetailDTO Test")
    void setPatientDetailDTO() {
        //given
        Patient patient = TestDataProvider.getPatient();
        patient.setMiddleName(Constants.MIDDLE_NAME);
        patient.setLastName(Constants.LAST_NAME);
        PatientDetailDTO patientDetailDTO = TestDataProvider.getPatientDetailsDTO();
        patientDetailDTO.setFirstName(patient.getFirstName().toUpperCase());
        patientDetailDTO.setMiddleName(patient.getMiddleName().toUpperCase());
        patientDetailDTO.setLastName(patient.getLastName().toUpperCase());
        Long virtualId = 1l;
        patientDetailDTO.setPatientTrackId(null);
        patientDetailDTO.setSiteId(null);
        //then
        PatientDetailDTO actualPatientDetailDTO = patientMapper.setPatientDetailDto(patient);
        Assertions.assertEquals(patientDetailDTO.getVirtualId(), actualPatientDetailDTO.getVirtualId());
    }

    @Test
    @DisplayName("SetGlucoseLog Test")
    void setGlucoseLog() {
        EnrollmentRequestDTO enrollmentRequestDTO = TestDataProvider.getEnrollmentRequestDto();
        enrollmentRequestDTO.setGlucoseLog(TestDataProvider.getGlucoseLog());
        GlucoseLog glucoseLog = enrollmentRequestDTO.getGlucoseLog();
        glucoseLog.setUpdatedFromEnrollment(Constants.BOOLEAN_TRUE);
        glucoseLog.setLatest(Constants.BOOLEAN_TRUE);
        //then
        patientMapper.setGlucoseLog(enrollmentRequestDTO, glucoseLog);
        Assertions.assertTrue(glucoseLog.isLatest());
        Assertions.assertTrue(glucoseLog.isUpdatedFromEnrollment());
        //given
        GlucoseLog otherGlucoseLog = TestDataProvider.getGlucoseLog();
        otherGlucoseLog.setId(null);
        otherGlucoseLog.setType(Constants.ENROLLMENT);
        enrollmentRequestDTO.setGlucoseLog(otherGlucoseLog);
        glucoseLog.setLatest(Constants.BOOLEAN_TRUE);
        //then
        patientMapper.setGlucoseLog(enrollmentRequestDTO, otherGlucoseLog);
        Assertions.assertTrue(otherGlucoseLog.isLatest());
        Assertions.assertFalse(otherGlucoseLog.isUpdatedFromEnrollment());
    }


    @Test
    @DisplayName("SetPatientTracker Test")
    void setPatientTracker() {
        //given
        EnrollmentRequestDTO enrollmentRequestDTO = TestDataProvider.getEnrollmentRequestDto();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        Site site = TestDataProvider.getSite();
        Patient patient = TestDataProvider.getPatient();
        //then
        patientMapper.setPatientTracker(enrollmentRequestDTO,
                patientTracker, site);
        //given
        enrollmentRequestDTO.setGlucoseLog(TestDataProvider.getGlucoseLog());
        enrollmentRequestDTO.setProvisionalDiagnosis(List.of(Constants.PROVISIONALDIAGNOSIS));
        //then
        patientMapper.setPatientTracker(enrollmentRequestDTO,
                patientTracker, site);
        Assertions.assertNotNull(enrollmentRequestDTO);
        Assertions.assertNotNull(patientTracker);
        Assertions.assertNotNull(site);
    }
}
