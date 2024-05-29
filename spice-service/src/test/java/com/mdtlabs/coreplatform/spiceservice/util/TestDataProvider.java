package com.mdtlabs.coreplatform.spiceservice.util;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.UnitConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserTenantsContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.BioDataDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.BioMetricsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.BpLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ComplianceDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ConfirmDiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ContinuousMedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CultureDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CurrentMedicationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CurrentMedicationDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.DeviceDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.DiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.DiagnosisDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.FrequencyDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.InitialMedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LifestyleDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewStaticDataDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MentalHealthDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MetaFormDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MyPatientListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.NutritionLifestyleResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OldScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientBpLogsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientDetailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientFilterDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientNutritionLifestyleDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientNutritionLifestyleRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientSortDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTrackerDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferUpdateRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTreatmentPlanDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PregnancyDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriberDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionHistoryResponse;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ReviewerDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RiskAlgorithmDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.StaticDataDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SymptomDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserResponseDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.DeviceDetails;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Program;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountCustomization;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLogDetails;
import com.mdtlabs.coreplatform.common.model.entity.spice.ClassificationBrand;
import com.mdtlabs.coreplatform.common.model.entity.spice.Comorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complaints;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complication;
import com.mdtlabs.coreplatform.common.model.entity.spice.CountryClassification;
import com.mdtlabs.coreplatform.common.model.entity.spice.CultureValues;
import com.mdtlabs.coreplatform.common.model.entity.spice.CurrentMedication;
import com.mdtlabs.coreplatform.common.model.entity.spice.CustomizedModule;
import com.mdtlabs.coreplatform.common.model.entity.spice.Diagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.DosageForm;
import com.mdtlabs.coreplatform.common.model.entity.spice.DosageFrequency;
import com.mdtlabs.coreplatform.common.model.entity.spice.FormMetaUi;
import com.mdtlabs.coreplatform.common.model.entity.spice.Frequency;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResult;
import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.MedicalCompliance;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.ModelQuestions;
import com.mdtlabs.coreplatform.common.model.entity.spice.NutritionLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComplication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientCurrentMedication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalCompliance;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalReview;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientNutritionLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientPregnancyDetails;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTransfer;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.model.entity.spice.PhysicalExamination;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;
import com.mdtlabs.coreplatform.common.model.entity.spice.Reason;
import com.mdtlabs.coreplatform.common.model.entity.spice.RedRiskNotification;
import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;
import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.SideMenu;
import com.mdtlabs.coreplatform.common.model.entity.spice.Symptom;
import com.mdtlabs.coreplatform.common.model.entity.spice.Unit;
import com.mdtlabs.coreplatform.common.model.enumeration.spice.PatientTransferStatus;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.spiceservice.common.RiskAlgorithm;
import org.json.simple.JSONObject;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

public class TestDataProvider {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final ModelMapper mapper = new ModelMapper();

    private static MockedStatic<CommonUtil> commonUtil;

    private static MockedStatic<UserContextHolder> userContextHolder;

    private static MockedStatic<UserSelectedTenantContextHolder> userSelectedTenantContextHolder;

    private static MockedStatic<UserTenantsContextHolder> userTenantsContext;

    private static void validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static AssessmentDTO getAssessmentDto() {
        AssessmentDTO assessmentDTO = new AssessmentDTO();
        assessmentDTO.setBpLog(getBpLog());
        assessmentDTO.setIsRegularSmoker(false);
        assessmentDTO.setNationalId("123qwe456");
        assessmentDTO.setGlucoseLog(getGlucoseLog());
        assessmentDTO.setId(1L);
        assessmentDTO.setFirstName(Constants.FIRST_NAME);
        assessmentDTO.setAge(22);
        assessmentDTO.setSiteId(1L);
        assessmentDTO.setTenantId(4L);
        assessmentDTO.setPatientTrackId(1L);
        assessmentDTO.setPatientVisitId(1L);
        assessmentDTO.setCvdRiskLevel(Constants.CVD_RISK_MEDIUM);
        assessmentDTO.setCvdRiskScore(Constants.TWO);
        assessmentDTO.setGender(Constants.GENDER_MALE);
        assessmentDTO.setUnitMeasurement(UnitConstants.METRIC);
        assessmentDTO.setSymptoms(List.of(getSymptomDTO()));
        return assessmentDTO;
    }

    public static SymptomDTO getSymptomDTO() {
        return mapper.map(getSymptomData(), SymptomDTO.class);
    }

    @MethodSource("bpLogData")
    public static BpLog getBpLog(Integer avgSystolic,
                                 Integer avgDiastolic, List<BpLogDetails> bpLogDetails, int violationSize) {
        validate();
        BpLog bpLog = new BpLog();
        bpLog.setAvgSystolic(avgSystolic);
        bpLog.setAvgDiastolic(avgDiastolic);
        bpLog.setBpLogDetails(bpLogDetails);
        Set<ConstraintViolation<BpLog>> violations = validator.validate(bpLog);
        assertThat(violations).hasSize(violationSize);
        return bpLog;

    }

    private static Stream<Arguments> bpLogData() {
        return Stream.of(
                Arguments.of(80, 50, List.of(1L, 2L), 0),
                Arguments.of(80, null, null, 1),
                Arguments.of(null, 50, List.of(1L, 2L), 1),
                Arguments.of(null, null, List.of(1L, 2L), 2),
                Arguments.of(null, null, List.of(""), 2),
                Arguments.of(null, null, null, 2));
    }

    public static BpLogDTO getBpLogDTO() {
        return mapper.map(getBpLog(), BpLogDTO.class);

    }

    public static BpLog getBpLog() {
        BpLog bpLog = getBpLog(80, 50, List.of(), 0);
        bpLog.setId(1L);
        bpLog.setPatientTrackId(1L);
        bpLog.setRiskLevel(Constants.CVD_RISK_MEDIUM);
        bpLog.setIsRegularSmoker(Constants.BOOLEAN_TRUE);
        bpLog.setBmi(32.3d);
        bpLog.setWeight(78d);
        bpLog.setBpLogDetails(getBpLogDetails());
        return bpLog;
    }

    public static ClassificationBrand getClassificationBrand() {
        ClassificationBrand classificationBrand = new ClassificationBrand();
        classificationBrand.setClassificationId(1L);
        classificationBrand.setCountryId(1L);
        classificationBrand.setTenantId(10L);
        classificationBrand.setId(5L);
        return classificationBrand;
    }

    public static DeviceDetails getDeviceDetails() {
        DeviceDetails deviceDetails = new DeviceDetails();
        deviceDetails = new DeviceDetails();
        deviceDetails.setDeviceId("1L");
        deviceDetails.setUserId(1L);
        deviceDetails.setId(1L);
        return deviceDetails;
    }

    public static DeviceDetailsDTO getDeviceDetailsDTO() {
        return mapper.map(getDeviceDetails(), DeviceDetailsDTO.class);
    }

    public static GlucoseLog getGlucoseLog() {
        GlucoseLog glucoseLog = new GlucoseLog();
        glucoseLog.setId(1L);
        glucoseLog.setTenantId(4L);
        glucoseLog.setGlucoseLogId(1L);
        glucoseLog.setGlucoseValue(22d);
        glucoseLog.setPatientTrackId(1L);
        glucoseLog.setType(Constants.MEDICAL_REVIEW);
        glucoseLog.setLatest(true);
        glucoseLog.setAssessmentTenantId(4L);
        glucoseLog.setIsBeforeDiabetesDiagnosis(false);
        return glucoseLog;
    }

    public static GlucoseLogDTO getGlucoseLogDTO() {
        return mapper.map(getGlucoseLog(), GlucoseLogDTO.class);
    }

    public static AssessmentResponseDTO getAssessmentResponseDto() {
        AssessmentResponseDTO assessmentResponseDTO = new AssessmentResponseDTO();
        assessmentResponseDTO.setPatientDetails(getPatientDetailsDTO());
        assessmentResponseDTO.setBpLog(getBpLogDTO());
        assessmentResponseDTO.setPhq4(getMentalHealthDto());
        assessmentResponseDTO.setGlucoseLog(getGlucoseLogDTO());
        return assessmentResponseDTO;
    }

    public static PatientDetailDTO getPatientDetailsDTO() {
        PatientDetailDTO patientDetailDTO = new PatientDetailDTO();
        patientDetailDTO.setId(1L);
        patientDetailDTO.setFirstName(Constants.NAME.toUpperCase());
        patientDetailDTO.setCountryId(1L);
        patientDetailDTO.setPatientTrackId(1L);
        patientDetailDTO.setNationalId("123qwe456".toUpperCase());
        patientDetailDTO.setSiteId(1L);
        patientDetailDTO.setGender(Constants.GENDER_MALE);
        patientDetailDTO.setAge(32);
        patientDetailDTO.setCountryId(1L);
        patientDetailDTO.setProgramId(1L);
        patientDetailDTO.setVirtualId(1L);
        return patientDetailDTO;
    }

    public static Patient getPatient() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName(Constants.NAME);
        patient.setCountryId(1L);
        patient.setNationalId("123qwe456");
        patient.setSiteId(1L);
        patient.setGender(Constants.GENDER_MALE);
        patient.setAge(32);
        patient.setCountryId(1L);
        patient.setProgramId(1L);
        patient.setVirtualId(1L);
        return patient;
    }

    public static RequestDTO getRequestDto() {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setLatestRequired(Constants.BOOLEAN_FALSE);
        requestDTO.setSkip(0);
        requestDTO.setTenantId(4L);
        requestDTO.setPatientTrackId(1L);
        requestDTO.setPatientVisitId(1L);
        requestDTO.setSiteId(1L);
        requestDTO.setId(1L);
        return requestDTO;
    }

    public static PatientGlucoseLogDTO getPatientGlucoseLogDTO() {
        PatientGlucoseLogDTO patientGlucoseLogDTO = new PatientGlucoseLogDTO();
        patientGlucoseLogDTO.setTotal(10);
        patientGlucoseLogDTO.setLatestGlucoseLog(getGlucoseLogDTO());
        patientGlucoseLogDTO.setLimit(0);
        patientGlucoseLogDTO.setLimit(0);
        return patientGlucoseLogDTO;
    }

    public static PatientBpLogsDTO getPatientBpLogDTO() {
        PatientBpLogsDTO patientBpLogsDTO = new PatientBpLogsDTO();
        patientBpLogsDTO.setLatestBpLog(getBpLogDTO());
        patientBpLogsDTO.setSkip(0);
        patientBpLogsDTO.setLimit(0);
        patientBpLogsDTO.setTotal(10L);
        return patientBpLogsDTO;
    }

    public static Frequency getFrequency() {
        Frequency frequency = new Frequency();
        frequency.setId(1L);
        frequency.setName("Every 1 Week");
        frequency.setType(Constants.DEFAULT);
        frequency.setPeriod("03");
        frequency.setDuration(Constants.TWO);
        frequency.setRiskLevel(Constants.CVD_RISK_MEDIUM);
        frequency.setDisplayOrder(Constants.FOUR);
        return frequency;
    }

    public static FrequencyDTO getFrequencyDTO() {
        return mapper.map(getFrequency(), FrequencyDTO.class);
    }

    public static MedicalReviewDTO getMedicalReview() {
        MedicalReviewDTO medicalReviewDTO = new MedicalReviewDTO();
        medicalReviewDTO.setInitialMedicalReview(new InitialMedicalReviewDTO());
        medicalReviewDTO.setContinuousMedicalReview(new ContinuousMedicalReviewDTO());
        medicalReviewDTO.setId(1L);
        medicalReviewDTO.setTenantId(4L);
        medicalReviewDTO.setPatientTrackId(1L);
        medicalReviewDTO.setPatientVisitId(1L);
        return medicalReviewDTO;
    }

    public static MedicalReviewResponseDTO getMedicalResponseDto() {
        MedicalReviewResponseDTO medicalReviewResponseDTO = new MedicalReviewResponseDTO();
        medicalReviewResponseDTO.setPatientDetails(new DiagnosisDetailsDTO());
        medicalReviewResponseDTO.setIsSigned(true);
        medicalReviewResponseDTO.setReviewerDetails(getReviewerDetailsDto());
        medicalReviewResponseDTO.getPatientDetails().setIsConfirmDiagnosis(true);
        return medicalReviewResponseDTO;
    }

    public static ReviewerDetailsDTO getReviewerDetailsDto() {
        ReviewerDetailsDTO reviewerDetailsDTO = new ReviewerDetailsDTO();
        reviewerDetailsDTO.setFirstName("reviewer");
        reviewerDetailsDTO.setUserName("provider");
        return reviewerDetailsDTO;
    }

    public static MentalHealth getMentalHealth() {
        MentalHealth mentalHealth = new MentalHealth();
        mentalHealth.setAssessmentTenantId(1L);
        mentalHealth.setTenantId(1L);
        mentalHealth.setPatientTrackId(1L);
        mentalHealth.setTenantId(4L);
        mentalHealth.setId(1L);
        return mentalHealth;
    }

    public static MentalHealthDTO getMentalHealthDto() {
        return mapper.map(getMentalHealth(), MentalHealthDTO.class);
    }

    public static EnrollmentRequestDTO getEnrollmentRequestDto() {
        EnrollmentRequestDTO enrollmentRequestDTO = new EnrollmentRequestDTO();
        enrollmentRequestDTO.setGender(Constants.GENDER_MALE);
        enrollmentRequestDTO.setBioData(getBioData());
        enrollmentRequestDTO.setAge(23);
        enrollmentRequestDTO.setIsRegularSmoker(Constants.BOOLEAN_TRUE);
        enrollmentRequestDTO.setBpLog(getBpLog());
        enrollmentRequestDTO.setSiteId(1L);
        enrollmentRequestDTO.setPatientTrackId(1L);

        return enrollmentRequestDTO;
    }

    public static EnrollmentResponseDTO getEnrollmentResponseDTO() {
        EnrollmentResponseDTO enrollmentResponseDTO = new EnrollmentResponseDTO();
        enrollmentResponseDTO.setEnrollment(getPatientDetailsDTO());
        enrollmentResponseDTO.setBpLog(getBpLogDTO());
        enrollmentResponseDTO.setGlucoseLog(getGlucoseLogDTO());
        enrollmentResponseDTO.setIsConfirmDiagnosis(true);
        enrollmentResponseDTO.setPhoneNumber("9876543210");
        enrollmentResponseDTO.getEnrollment().setPatientTrackId(1L);
        return enrollmentResponseDTO;
    }

    public static PregnancyDetailsDTO getPregnancyDetails() {
        PregnancyDetailsDTO pregnancyRequestDTO = new PregnancyDetailsDTO();
        pregnancyRequestDTO.setId(1L);
        pregnancyRequestDTO.setPatientTrackId(1L);
        pregnancyRequestDTO.setTenantId(4L);
        pregnancyRequestDTO.setTemperature(33.3d);
        pregnancyRequestDTO.setUnitMeasurement(UnitConstants.METRIC);
        pregnancyRequestDTO.setIsOnTreatment(true);
        pregnancyRequestDTO.setPregnancyFetusesNumber(5);
        pregnancyRequestDTO.setPatientPregnancyId(1L);
        return pregnancyRequestDTO;
    }

    public static PatientPregnancyDetails getPatientPregnancyDetails() {
        return mapper.map(getPregnancyDetails(), PatientPregnancyDetails.class);
    }

    public static PatientGetRequestDTO getPatientGetRequestDTO() {
        PatientGetRequestDTO requestDTO = new PatientGetRequestDTO();
        requestDTO.setId(1L);
        requestDTO.setAssessmentRequired(true);
        requestDTO.setRedRiskPatient(true);
        requestDTO.setPregnant(false);
        return requestDTO;
    }

    public static PatientTrackerDTO getPatientTrackerDTO() {
        PatientTrackerDTO patientTrackerDTO = new PatientTrackerDTO();
        patientTrackerDTO.setProgramId(1L);
        patientTrackerDTO.setPatientId(1L);
        patientTrackerDTO.setAvgSystolic(80);
        patientTrackerDTO.setAvgDiastolic(50);
        patientTrackerDTO.setWeight(65d);
        patientTrackerDTO.setBmi(32.2d);
        patientTrackerDTO.setHeight(5.5d);
        patientTrackerDTO.setPatientStatus(Constants.ENROLLED);
        patientTrackerDTO.setGender(Constants.GENDER_MALE);
        patientTrackerDTO.setCvdRiskLevel(Constants.CVD_RISK_MEDIUM);
        patientTrackerDTO.setCvdRiskScore(Constants.TWO);
        patientTrackerDTO.setFirstName(Constants.NAME.toUpperCase());
        patientTrackerDTO.setTenantId(4L);
        patientTrackerDTO.setId(1L);
        patientTrackerDTO.setSiteId(1L);
        patientTrackerDTO.setNationalId("123qwe456".toUpperCase());
        patientTrackerDTO.setAge(0);
        patientTrackerDTO.setIsRegularSmoker(Constants.BOOLEAN_FALSE);
        patientTrackerDTO.setCreatedAt(new Date());
        patientTrackerDTO.setActive(true);
        return patientTrackerDTO;
    }

    public static GetRequestDTO getGetRequestDTO() {
        GetRequestDTO getRequestDTO = new GetRequestDTO();
        getRequestDTO.setPatientPregnancyId(1L);
        getRequestDTO.setIsActive(true);
        getRequestDTO.setDeleted(false);
        getRequestDTO.setPatientTrackId(1L);
        getRequestDTO.setTenantId(4L);
        return getRequestDTO;
    }

    public static PatientRequestDTO getPatientRequestDTO() {
        PatientRequestDTO patientRequestDTO = new PatientRequestDTO();
        patientRequestDTO.setIsLabtestReferred(true);
        patientRequestDTO.setSearchId("1L");
        patientRequestDTO.setPatientTrackId(1L);
        patientRequestDTO.setId(1L);
        patientRequestDTO.setTenantId(4L);
        patientRequestDTO.setLimit(10);
        patientRequestDTO.setSkip(5);
        patientRequestDTO.setIsMedicationPrescribed(true);
        return patientRequestDTO;
    }

    public static PatientLabTestRequestDTO getPatientLabTestRequest() {
        PatientLabTestRequestDTO requestDTO = new PatientLabTestRequestDTO();
        requestDTO.setTenantId(4L);
        requestDTO.setPatientTrackId(1L);
        requestDTO.setPatientVisitId(1L);
        requestDTO.setLabTest(List.of(getPatientLabTest()));
        return requestDTO;
    }

    public static PatientLabTest getPatientLabTest() {
        PatientLabTest patientLabTest = new PatientLabTest();
        patientLabTest.setLabTestId(1L);
        patientLabTest.setLabTestName("Bp Test");
        patientLabTest.setPatientTrackId(1L);
        patientLabTest.setIsReviewed(Constants.BOOLEAN_TRUE);
        patientLabTest.setTenantId(4L);
        patientLabTest.setId(1L);
        return patientLabTest;
    }

    public static PatientLabTestDTO getPatientLabTestDTO() {
        return mapper.map(getPatientLabTest(), PatientLabTestDTO.class);
    }

    public static PatientLabTestResponseDTO getLabTestResponse() {
        PatientLabTestResponseDTO responseDTO = new PatientLabTestResponseDTO();
        responseDTO.setPatientLabTest(List.of(getPatientLabTestDTO()));
        return responseDTO;
    }

    public static PatientLabTestResultRequestDTO getLabTestResultRequestDto() {
        PatientLabTestResultRequestDTO requestDTO = new PatientLabTestResultRequestDTO();
        requestDTO.setPatientLabTestId(1L);
        requestDTO.setComment("is in abnormal");
        requestDTO.setIsReviewed(true);
        requestDTO.setTenantId(4L);
        requestDTO.setPatientLabTestResults(List.of(getPatientLabTestResultDto()));

        return requestDTO;
    }

    public static PatientLabTestResultDTO getPatientLabTestResultDto() {
        PatientLabTestResultDTO resultDTO = new PatientLabTestResultDTO();
        resultDTO.setResultStatus("positive");
        resultDTO.setName("mentalIllness");
        resultDTO.setIsAbnormal(Constants.BOOLEAN_TRUE);
        resultDTO.setUnit(UnitConstants.METRIC);
        resultDTO.setResultValue("55");
        resultDTO.setDisplayName("(23-28 mEq/L)");
        resultDTO.setId("1");
        return resultDTO;
    }

    public static PatientLabTestResult getPatientLabTestResultData() {
        return mapper.map(getPatientLabTestResultDto(), PatientLabTestResult.class);
    }

    public static PatientLabTestResultResponseDTO getLabTestResultResponseDTO() {
        PatientLabTestResultResponseDTO result = new PatientLabTestResultResponseDTO();
        result.setPatientLabTestResults(List.of(getPatientLabTestResultData()));
        result.setComment("review daily");
        return result;
    }

    public static LabTestResult getLabTestResult() {
        LabTestResult result = new LabTestResult();
        result.setLabTestId(1L);
        result.setTenantId(4L);
        result.setId(1L);
        result.setName("Bp Test");
        return result;
    }

    public static LabTest getLabTestData() {
        LabTest labTest = new LabTest();
        labTest.setTenantId(4L);
        labTest.setId(1L);
        labTest.setName("Bp Test");
        labTest.setLabTestResults(Set.of(getLabTestResult()));
        return labTest;
    }

    public static Symptom getSymptomData() {
        Symptom symptom = new Symptom();
        symptom.setId(1L);
        symptom.setName(Constants.NAME);
        symptom.setType(Constants.DIABETES);
        return symptom;
    }

    public static ScreeningLogDTO getScreeningLogDTO() {
        ScreeningLogDTO screeningLog = new ScreeningLogDTO();
        screeningLog.setBpLog(getBpLog());
        screeningLog.setCategory("Facility");
        screeningLog.setLongitude("32");
        screeningLog.setLatitude("32");
        screeningLog.setIsReferAssessment(true);
        screeningLog.setSiteId(1L);
        screeningLog.setUnitMeasurement(UnitConstants.METRIC);
        screeningLog.setBioData(getBioData());
        screeningLog.setPhq4(getMentalHealth());
        screeningLog.setGlucoseLog(getGlucoseLog());
        screeningLog.setBioMetrics(getBioMetricDTO());
        screeningLog.setDeviceInfoId(1L);
        screeningLog.setSiteId(1L);
        screeningLog.setCountryId(3L);
        screeningLog.setDeviceInfoId(1L);
        return screeningLog;
    }

    public static OldScreeningLogDTO getOldScreeningLogDto() {
        return mapper.map(getScreeningLogDTO(), OldScreeningLogDTO.class);
    }

    public static ScreeningLog getScreeningLog() {
        ScreeningLog screeningLog = new ScreeningLog();
        screeningLog.setAvgDiastolic(50);
        screeningLog.setAvgSystolic(80);
        screeningLog.setNationalId("123qwe456");
        screeningLog.setCategory("Facility");
        screeningLog.setLongitude("32");
        screeningLog.setLatitude("0");
        screeningLog.setIsReferAssessment(Constants.BOOLEAN_TRUE);
        screeningLog.setLongitude("32");
        screeningLog.setLatitude("32");
        screeningLog.setGender(Constants.GENDER_MALE);
        screeningLog.setAge(33);
        screeningLog.setDeviceInfoId(1L);
        screeningLog.setSiteId(1L);
        screeningLog.setTenantId(4L);
        screeningLog.setId(1L);
        screeningLog.setCountryId(3L);
        return screeningLog;
    }

    public static ScreeningResponseDTO getScreeningResponseDTO() {
        return mapper.map(getScreeningLog(), ScreeningResponseDTO.class);
    }

    public static BioDataDTO getBioData() {
        BioDataDTO bioDataDTO = new BioDataDTO();
        bioDataDTO.setNationalId("123qwe456");
        bioDataDTO.setFirstName(Constants.FIRST_NAME);
        bioDataDTO.setLastName(Constants.LAST_NAME);
        bioDataDTO.setInitial("S");
        bioDataDTO.setPhoneNumber(Constants.PHONE_NUMBER);
        bioDataDTO.setPhoneNumberCategory(Constants.PHONE_NUMBER_CATEGORY);
        bioDataDTO.setCounty(2L);
        bioDataDTO.setCountry(3L);
        bioDataDTO.setSubCounty(5L);

        return bioDataDTO;
    }

    public static BioMetricsDTO getBioMetricDTO() {
        BioMetricsDTO bioMetricsDTO = new BioMetricsDTO();
        bioMetricsDTO.setGender(Constants.GENDER_MALE);
        bioMetricsDTO.setWeight(200.22d);
        bioMetricsDTO.setAge(33);
        bioMetricsDTO.setHeight(77.88d);
        bioMetricsDTO.setBmi(32.28d);
        return bioMetricsDTO;
    }

    public static PatientTreatmentPlan getPatientTreatmentPlan() {
        PatientTreatmentPlan patientTreatmentPlan = new PatientTreatmentPlan();
        patientTreatmentPlan.setPatientTrackId(1L);
        patientTreatmentPlan.setPatientVisitId(1L);
        patientTreatmentPlan.setRiskLevel(Constants.CVD_RISK_MEDIUM);
        patientTreatmentPlan.setBgCheckFrequency(Constants.FREQUENCY_BG_CHECK);
        patientTreatmentPlan.setBpCheckFrequency(Constants.FREQUENCY_BP_CHECK);
        patientTreatmentPlan.setMedicalReviewFrequency(Constants.FREQUENCY_MEDICAL_REVIEW);
        return patientTreatmentPlan;
    }

    public static PatientTreatmentPlanDTO getPatientTreatmentPlanDTO() {
        return mapper.map(getPatientTreatmentPlan(), PatientTreatmentPlanDTO.class);
    }

    public static PatientNutritionLifestyle getPatientNutritionLifeStyle() {
        PatientNutritionLifestyle nutritionLifestyle = new PatientNutritionLifestyle();
        nutritionLifestyle.setPatientVisitId(1L);
        nutritionLifestyle.setPatientTrackId(1L);
        nutritionLifestyle.setTenantId(4L);
        nutritionLifestyle.setId(1L);
        nutritionLifestyle.setLifestyles(Set.of(getNutritionLifestyle()));
        nutritionLifestyle.setClinicianNote("nothing");
        return nutritionLifestyle;

    }

    public static PatientNutritionLifestyleDTO getPatientNutritionLifestyleDTO() {
        return mapper.map(getPatientNutritionLifeStyle(), PatientNutritionLifestyleDTO.class);
    }

    public static NutritionLifestyle getNutritionLifestyle() {
        NutritionLifestyle nutritionLifestyle = new NutritionLifestyle();
        nutritionLifestyle.setName("Stress Management");
        nutritionLifestyle.setDisplayOrder(1);
        nutritionLifestyle.setId(1L);
        return nutritionLifestyle;
    }

    public static PatientNutritionLifestyleRequestDTO getNutritionLifestyleRequestDto() {
        PatientNutritionLifestyleRequestDTO requestDTO = new PatientNutritionLifestyleRequestDTO();
        requestDTO.setReferredFor("Nutrition, Diabetes Education," +
                " Hypertension Education, Alcohol Counseling, Stress Management");
        requestDTO.setPatientTrackId(1L);
        requestDTO.setPatientVisitId(1L);
        requestDTO.setTenantId(4L);
        requestDTO.setLifestyleAssessment(Constants.ASSESSMENT);
        requestDTO.setLifestyles(List.of(getPatientNutritionLifeStyle()));
        requestDTO.setClinicianNote("nothing");
        return requestDTO;
    }

    public static NutritionLifestyleResponseDTO getNutritionLifestyleResponseDTO() {
        NutritionLifestyleResponseDTO responseDTO = new NutritionLifestyleResponseDTO();
        responseDTO.setLifestyle(List.of(Map.of(FieldConstants.NAME, "Lifestyle Test1", Constants.CULTURE_VALUE, "Lifestyle Test2")));
        responseDTO.setId(1L);
        responseDTO.setClinicianNote("nothing");
        responseDTO.setLifestyleAssessment("LifestyleAssessment Test");
        return responseDTO;
    }

    public static CommonRequestDTO getCommonRequestDTO() {
        CommonRequestDTO requestDTO = new CommonRequestDTO();
        requestDTO.setTenantId(4L);
        requestDTO.setPatientVisitId(1L);
        requestDTO.setPatientTrackId(1L);
        requestDTO.setId(1L);
        return requestDTO;
    }

    public static PatientTracker getPatientTracker() {
        PatientTracker patientTracker = new PatientTracker();
        patientTracker.setProgramId(1L);
        patientTracker.setPatientId(1L);
        patientTracker.setAvgSystolic(80);
        patientTracker.setAvgDiastolic(50);
        patientTracker.setWeight(65d);
        patientTracker.setBmi(32.2d);
        patientTracker.setHeight(5.5d);
        patientTracker.setPatientStatus(Constants.ENROLLED);
        patientTracker.setGender(Constants.GENDER_MALE);
        patientTracker.setCvdRiskLevel(Constants.CVD_RISK_MEDIUM);
        patientTracker.setCvdRiskScore(Constants.TWO);
        patientTracker.setFirstName(Constants.NAME);
        patientTracker.setTenantId(4L);
        patientTracker.setId(1L);
        patientTracker.setSiteId(1L);
        patientTracker.setNationalId("123qwe456");
        patientTracker.setAge(0);
        patientTracker.setIsRegularSmoker(Constants.BOOLEAN_FALSE);
        patientTracker.setCreatedAt(new Date());
        return patientTracker;
    }

    public static ConfirmDiagnosisDTO getConfirmDiagnosisDTO() {
        ConfirmDiagnosisDTO confirmDiagnosisDTO = new ConfirmDiagnosisDTO();
        confirmDiagnosisDTO.setId(1L);
        confirmDiagnosisDTO.setPatientTrackId(1L);
        confirmDiagnosisDTO.setPatientVisitId(1L);
        confirmDiagnosisDTO.setTenantId(4L);
        confirmDiagnosisDTO.setIsConfirmDiagnosis(true);
        confirmDiagnosisDTO.setDiagnosisComments("DiagnosisComment Test");
        return confirmDiagnosisDTO;
    }

    public static PrescriptionRequestDTO getPrescriptionRequestDTO() {
        PrescriptionRequestDTO requestDTO = new PrescriptionRequestDTO();
        requestDTO.setTenantId(4L);
        requestDTO.setPatientVisitId(1L);
        requestDTO.setPatientTrackId(1L);
        return requestDTO;
    }

    public static Prescription getPrescription() {
        Prescription prescription = new Prescription();
        prescription.setMedicationId(1L);
        prescription.setDosageFrequencyId(1L);
        prescription.setDosageUnitId(1L);
        prescription.setPatientTrackId(1L);
        prescription.setPatientVisitId(1L);
        prescription.setTenantId(4L);
        prescription.setSignature("signatureFile");
        prescription.setId(1L);
        return prescription;
    }

    public static PrescriptionDTO getPrescriptionDTO() {

        return mapper.map(getPrescription(), PrescriptionDTO.class);
    }

    public static PrescriptionHistory getPrescriptionHistory() {
        PrescriptionHistory history = new PrescriptionHistory();
        history.setPrescriptionId(1L);
        history.setPatientTrackId(1L);
        history.setPatientVisitId(1L);
        history.setTenantId(4L);
        history.setSignature("signature");
        return history;
    }

    public static PrescriptionHistoryResponse getPrescriptionHistoryResponse() {
        PrescriptionHistoryResponse response = new PrescriptionHistoryResponse();
        response.setPatientPrescription(List.of(getPrescriptionHistory()));
        return response;
    }

    public static FillPrescriptionRequestDTO getPrescriptionResponseDTO() {
        FillPrescriptionRequestDTO requestDTO = new FillPrescriptionRequestDTO();
        requestDTO.setId(1L);
        requestDTO.setTenantId(4L);
        requestDTO.setPatientVisitId(1L);
        requestDTO.setPatientTrackId(1L);
        requestDTO.setPrescriptions(List.of(getPrescription()));
        return requestDTO;
    }

    public static FillPrescriptionResponseDTO getFillPrescriptionDTO() {
        FillPrescriptionResponseDTO responseDTO = new FillPrescriptionResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTenantId(4L);
        responseDTO.setRemainingPrescriptionDays(4);
        return responseDTO;
    }

    public static SearchRequestDTO getSearchRequestDto() {
        SearchRequestDTO data = new SearchRequestDTO();
        data.setTenantId(4L);
        data.setSiteId(1L);
        data.setCountryId(1L);
        data.setPatientTrackId(1L);
        data.setLastRefillVisitId(1L);
        return data;
    }

    public static PatientSymptom getPatientSymptom() {
        PatientSymptom data = new PatientSymptom();
        data.setSymptomId(1L);
        data.setAssessmentLogId(1L);
        data.setGlucoseLogId(1L);
        data.setPatientTrackId(1L);
        data.setType(Constants.DIABETES);
        data.setName(Constants.NAME);
        return data;
    }

    public static RedRiskNotification getRedRiskNotification() {
        RedRiskNotification notification = new RedRiskNotification();
        notification.setStatus(Constants.NEW);
        notification.setGlucoseLogId(1L);
        notification.setBpLogId(1L);
        notification.setPatientTrackId(1L);
        notification.setTenantId(4L);
        notification.setAssessmentLogId(1L);
        notification.setId(1L);
        return notification;
    }

    public static Site getSite() {
        Site site = new Site();
        site.setId(1L);
        site.setName("testName");
        site.setCountryId(1L);
        site.setAccountId(1L);
        site.setOperatingUnit(getOperatingunit());
        return site;
    }

    public static Operatingunit getOperatingunit() {
        Operatingunit operatingunit = new Operatingunit();
        operatingunit.setId(1L);
        return operatingunit;
    }

    public static ComplianceDTO getComplianceDTO() {
        ComplianceDTO complianceDTO = new ComplianceDTO();
        complianceDTO.setComplianceId(1L);
        complianceDTO.setName(Constants.NAME);
        complianceDTO.setId(1L);
        return complianceDTO;
    }

    public static RiskAlgorithmDTO getRiskAlgorithmDto() {
        RiskAlgorithmDTO algorithmDTO = new RiskAlgorithmDTO();
        algorithmDTO.setRiskLevel(Constants.CVD_RISK_MEDIUM);
        algorithmDTO.setGlucoseValue(32.3d);
        algorithmDTO.setIsPregnant(true);
        algorithmDTO.setAvgSystolic(50);
        algorithmDTO.setAvgDiastolic(80);
        algorithmDTO.setPatientTrackId(1L);
        algorithmDTO.setGlucoseType(Constants.DIABETES);
        algorithmDTO.setSymptoms(Set.of((1L)));
        return algorithmDTO;
    }

    public static RiskAlgorithm getRiskAlgorithm() {
        RiskAlgorithm algorithm = new RiskAlgorithm();
        algorithm.getRiskLevelInAssessmentDbm(getRiskAlgorithmDto());
        return algorithm;
    }

    public static UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setCultureId(1L);
        userDTO.setAuthorization(Constants.AUTHORIZATION);
        userDTO.setGender(Constants.GENDER_MALE);
        userDTO.setFirstName(Constants.FIRST_NAME);
        userDTO.setTimezone(getTimezone());
        userDTO.setCountry(getCountry());
        userDTO.setTenantId(4L);
        userDTO.setCountry(getCountry());
        return userDTO;
    }

    public static Country getCountry() {
        Country country = new Country();
        country.setCountryCode("123456");
        country.setName(Constants.NAME);
        country.setId(1L);
        country.setUnitMeasurement(UnitConstants.METRIC);
        return country;
    }

    public static ResponseListDTO getResponseListDTO() {
        ResponseListDTO response = new ResponseListDTO();
        response.setTotalCount(1L);
        response.setData(List.of(TestDataProvider.getPatient()));
        return response;
    }

    public static Timezone getTimezone() {
        Timezone timezone = new Timezone();
        timezone.setId(1L);
        timezone.setOffset("+10:00");
        timezone.setAbbreviation("Abbreviation");
        return timezone;
    }

    public static PatientVisit getPatientVisit() {
        PatientVisit patientVisit = new PatientVisit();
        patientVisit.setPatientTrackId(1L);
        patientVisit.setTenantId(4L);
        patientVisit.setId(1L);
        patientVisit.setVisitDate(new Date());
        patientVisit.setInvestigation(true);
        patientVisit.setMedicalReview(true);
        patientVisit.setPrescription(true);
        return patientVisit;
    }

    public static ContinuousMedicalReviewDTO getContinuousMedicalReviewDTO() {
        ContinuousMedicalReviewDTO reviewDTO = new ContinuousMedicalReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setPatientTrackId(1L);
        reviewDTO.setCountryId(1L);
        reviewDTO.setPatientVisitId(1L);
        reviewDTO.setComplaints(Set.of(1L, 2L));
        reviewDTO.setPhysicalExams(Set.of(1L, 2L));
        reviewDTO.setComorbidities(Set.of(getPatientComorbidity()));
        reviewDTO.setComplications(Set.of(getPatientComplication()));
        return reviewDTO;
    }

    public static PatientMedicalReview getPatientMedicalReview() {
        PatientMedicalReview medicalReview = new PatientMedicalReview();
        medicalReview.setPatientTrackId(1L);
        medicalReview.setPatientVisitId(1L);
        medicalReview.setTenantId(4L);
        medicalReview.setId(1L);

        return medicalReview;
    }

    public static PatientComorbidity getPatientComorbidity() {
        PatientComorbidity medicalComorbidity = new PatientComorbidity();
        medicalComorbidity.setId(1L);
        medicalComorbidity.setPatientTrackId(1L);
        medicalComorbidity.setPatientVisitId(1L);
        medicalComorbidity.setComorbidityId(1L);
        return medicalComorbidity;
    }

    public static MedicalReviewDTO getMedicalReviewDTO() {
        MedicalReviewDTO medicalReviewDTO = new MedicalReviewDTO();
        medicalReviewDTO.setId(1L);
        medicalReviewDTO.setPatientVisitId(1L);
        medicalReviewDTO.setTenantId(4L);
        medicalReviewDTO.setPatientTrackId(1L);
        medicalReviewDTO.setContinuousMedicalReview(getContinuousMedicalReviewDTO());
        return medicalReviewDTO;
    }

    public static Comorbidity getComorbidity() {
        Comorbidity comorbidity = new Comorbidity();
        comorbidity.setId(1L);
        return comorbidity;
    }

    public static Complaints getComplaints() {
        Complaints complaints = new Complaints();
        complaints.setId(1L);
        return complaints;
    }

    public static Complication getComplication() {
        Complication complication = new Complication();
        complication.setId(1L);
        return complication;
    }

    public static CountryClassification getCountryClassification() {
        CountryClassification countryClassification = new CountryClassification();
        countryClassification.setId(1L);
        return countryClassification;
    }

    public static CurrentMedication getCurrentMedication() {
        CurrentMedication currentMedication = new CurrentMedication();
        currentMedication.setId(1L);
        return currentMedication;
    }

    public static Diagnosis getDiagnosis() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1L);
        return diagnosis;
    }

    public static DosageForm getDosageForm() {
        DosageForm dosageForm = new DosageForm();
        dosageForm.setId(1L);
        dosageForm.setName("testName");
        return dosageForm;
    }

    public static FormMetaUi getFormMetaUi() {
        FormMetaUi formMetaUi = new FormMetaUi();
        formMetaUi.setId(1L);
        formMetaUi.setFormName("testFormName");
        return formMetaUi;
    }

    public static Lifestyle getLifestyle() {
        Lifestyle lifestyle = new Lifestyle();
        lifestyle.setId(1L);
        lifestyle.setName(Constants.NAME);
        lifestyle.setType(Constants.DIABETES);
        lifestyle.setAnswerDependent(Constants.BOOLEAN_TRUE);
        return lifestyle;
    }

    public static MedicalCompliance getMedicalCompliance() {
        MedicalCompliance medicalCompliance = new MedicalCompliance();
        medicalCompliance.setId(1L);
        return medicalCompliance;
    }

    public static ModelQuestions getModelQuestions() {
        ModelQuestions modelQuestions = new ModelQuestions();
        modelQuestions.setId(1L);
        return modelQuestions;
    }

    public static PhysicalExamination getPhysicalExamination() {
        PhysicalExamination physicalExamination = new PhysicalExamination();
        physicalExamination.setId(1L);
        return physicalExamination;
    }

    public static Reason getReason() {
        Reason reason = new Reason();
        reason.setId(1L);
        return reason;
    }

    public static SideMenu getSideMenu() {
        SideMenu sideMenu = new SideMenu();
        sideMenu.setId(1L);
        sideMenu.setRoleName("testUserRole");
        return sideMenu;
    }

    public static com.mdtlabs.coreplatform.common.model.entity.spice.RiskAlgorithm getMetaRiskAlgorithm() {
        com.mdtlabs.coreplatform.common.model.entity.spice.RiskAlgorithm riskAlgorithm = new com.mdtlabs.coreplatform.common.model.entity.spice.RiskAlgorithm();
        riskAlgorithm.setId(1L);
        riskAlgorithm.setCountryId(1L);
        return riskAlgorithm;
    }

    public static PatientTransfer getPatientTransfer() {
        PatientTransfer patientTransfer = new PatientTransfer();
        patientTransfer.setId(1L);
        patientTransfer.setPatientTracker(getPatientTracker());
        patientTransfer.setTransferSite(new Site(getPatientTransferRequestDTO().getTransferSite()));
        patientTransfer.setTransferSite(new Site(getPatientTransferRequestDTO().getOldSite()));
        patientTransfer.setTransferTo(new User(getPatientTransferRequestDTO().getTransferTo()));
        patientTransfer.setTransferReason(getPatientTransferRequestDTO().getTransferReason());
        patientTransfer.setTransferStatus(PatientTransferStatus.PENDING);
        patientTransfer.setTenantId(1L);
        patientTransfer.setTransferBy(getUser());
        patientTransfer.setOldSite(getSite());
        return patientTransfer;
    }

    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setPassword("testPassword");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setPhoneNumber("9876543210");
        user.setUsername("testUserName");
        user.setForgetPasswordCount(1);
        user.setForgetPasswordTime(new Date());
        user.setInvalidLoginAttempts(1);
        return user;
    }

    public static PatientTransferRequestDTO getPatientTransferRequestDTO() {
        PatientTransferRequestDTO patientTransferDto = new PatientTransferRequestDTO();
        patientTransferDto.setPatientTrackId(1L);
        patientTransferDto.setTransferSite(1L);
        patientTransferDto.setOldSite(1L);
        patientTransferDto.setTransferSite(1L);
        patientTransferDto.setTransferTo(1L);
        patientTransferDto.setTransferReason("testReason");
        return patientTransferDto;
    }

    public static PatientTransferUpdateRequestDTO getPatientTransferUpdateRequestDTO() {
        PatientTransferUpdateRequestDTO patientTransferUpdateRequestDTO = new PatientTransferUpdateRequestDTO();
        patientTransferUpdateRequestDTO.setId(1L);
        return patientTransferUpdateRequestDTO;
    }

    public static CustomizedModule getCustomizedModule() {
        CustomizedModule customizedModule = new CustomizedModule();
        customizedModule.setId(1L);
        customizedModule.setPatientTrackId(1L);
        customizedModule.setTenantId(4L);
        customizedModule.setClinicalworkflowId(1L);
        customizedModule.setScreenType(Constants.TYPE);
        return customizedModule;
    }

    public static PatientLifestyle getPatientLifestyle() {
        PatientLifestyle lifestyle = new PatientLifestyle();
        lifestyle.setPatientTrackId(1L);
        lifestyle.setPatientVisitId(1L);
        lifestyle.setLifestyleId(1L);
        lifestyle.setId(1L);
        return lifestyle;
    }

    public static PatientComplication getPatientComplication() {
        PatientComplication complication = new PatientComplication();
        complication.setId(1L);
        complication.setComplicationId(1L);
        complication.setPatientVisitId(1L);
        complication.setPatientTrackId(1L);
        return complication;
    }

    public static PatientCurrentMedication getPatientCurrentMedication() {
        PatientCurrentMedication currentMedication = new PatientCurrentMedication();
        currentMedication.setId(1L);
        currentMedication.setPatientTrackId(1L);
        currentMedication.setPatientVistId(1L);
        currentMedication.setTenantId(4L);
        return currentMedication;
    }

    public static PatientDiagnosis getPatientDiagnosis() {
        PatientDiagnosis patientDiagnosis = new PatientDiagnosis();
        patientDiagnosis.setPatientVisitId(1L);
        patientDiagnosis.setPatientTrackId(1L);
        patientDiagnosis.setId(1L);
        patientDiagnosis.setTenantId(4L);
        patientDiagnosis.setIsHtnDiagnosis(Constants.BOOLEAN_TRUE);
        patientDiagnosis.setIsDiabetesDiagnosis(Constants.BOOLEAN_TRUE);
        patientDiagnosis.setHtnYearOfDiagnosis(2020);
        patientDiagnosis.setHtnPatientType(Constants.DIABETES_MELLITUS_TYPE_1);
        patientDiagnosis.setDiabetesYearOfDiagnosis(2020);
        patientDiagnosis.setDiabetesDiagnosis(Constants.DIABETES);
        return patientDiagnosis;
    }

    public static PatientMedicalCompliance getPatientMedicalCompliance() {
        PatientMedicalCompliance medicalCompliance = new PatientMedicalCompliance();
        medicalCompliance.setBpLogId(1L);
        medicalCompliance.setPatientTrackId(1L);
        medicalCompliance.setTenantId(4L);
        medicalCompliance.setAssessmentLogId(1L);
        medicalCompliance.setId(1L);
        return medicalCompliance;
    }

    public static PatientSortDTO getPatientSortDTO() {
        PatientSortDTO patientSortDTO = new PatientSortDTO();
        patientSortDTO.setIsUpdated(true);
        patientSortDTO.setIsRedRisk(true);
        patientSortDTO.setIsLatestAssessment(true);
        patientSortDTO.setIsMedicalReviewDueDate(true);
        patientSortDTO.setIsHighLowBg(true);
        patientSortDTO.setIsHighLowBp(true);
        patientSortDTO.setIsAssessmentDueDate(true);
        patientSortDTO.setIsCvdRisk(true);
        return patientSortDTO;
    }

    public static PatientFilterDTO getPatientFilterDTO() {
        PatientFilterDTO patientFilterDTO = new PatientFilterDTO();
        patientFilterDTO.setScreeningReferral(true);
        patientFilterDTO.setPatientStatus("testPatientStatus");
        patientFilterDTO.setMedicalReviewDate("testMedicalReview");
        patientFilterDTO.setAssessmentDate("testAssessmentDate");
        return patientFilterDTO;
    }

    public static void init() {
        userContextHolder = mockStatic(UserContextHolder.class);
        userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);
        commonUtil = mockStatic(CommonUtil.class);
        userTenantsContext = mockStatic(UserTenantsContextHolder.class);
    }

    public static void getStaticMock() {
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.setId(1L);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(1L);
        commonUtil.when(CommonUtil::getAuthToken).thenReturn(Constants.AUTH_TOKEN_SUBJECT);
        commonUtil.when(() -> CommonUtil.validatePatientSearchData(Arrays.asList("", "", "")))
                .thenReturn(Constants.BOOLEAN_FALSE);
        userTenantsContext.when(UserTenantsContextHolder::get).thenReturn(List.of(1L));
    }

    public static void getStaticMockNull() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
    }

    public static void cleanUp() {
        userSelectedTenantContextHolder.close();
        commonUtil.close();
        userContextHolder.close();
        userTenantsContext.close();
    }

    public static void getGlucoseLogMock(GlucoseLog glucoseLog) {
        commonUtil.when(() -> CommonUtil.isGlucoseLogGiven(glucoseLog))
                .thenReturn(Boolean.TRUE);
    }

    public static Role getRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName("testName");
        role.setLevel("testLevel");
        return role;
    }


    public static AccountWorkflow getAccountWorkflow() {
        AccountWorkflow accountWorkflow = new AccountWorkflow();
        accountWorkflow.setViewScreens(List.of());
        accountWorkflow.setCountryId(1L);
        return accountWorkflow;
    }

    public static JSONObject getBioDataJson() {
        JSONObject bioData = new JSONObject();
        bioData.put(Constants.NATIONAL_ID, "123qwe456");
        bioData.put(Constants.PHONE_NUMBER_CATEGORY, "other");
        bioData.put(Constants.PHONE_NUMBER, "123434566");
        bioData.put(Constants.LAST_NAME, "last");
        bioData.put(Constants.MIDDLE_NAME, "nandu");
        bioData.put(Constants.LANDMARK, "null");
        bioData.put(Constants.FIRST_NAME, "name");
        return bioData;
    }

    public static JSONObject getBioMetricsJson() {
        JSONObject bioMetrics = new JSONObject();
        bioMetrics.put(Constants.AGE, "17");
        bioMetrics.put(Constants.GENDER, "male");
        bioMetrics.put(Constants.IS_REGULAR_SMOKER, "true");
        bioMetrics.put(Constants.HEIGHT, 77d);
        bioMetrics.put(Constants.WEIGHT, 32.2d);
        bioMetrics.put(Constants.BMI, 32.2d);
        return bioMetrics;
    }

    public static List<BpLogDetails> getBpLogDetails() {
        List<BpLogDetails> bpLogDetails = new ArrayList<>();
        BpLogDetails bpLogDetailsFirst = new BpLogDetails();
        bpLogDetailsFirst.setSystolic(80);
        bpLogDetailsFirst.setDiastolic(90);
        BpLogDetails bpLogDetailsSecond = new BpLogDetails();
        bpLogDetailsSecond.setSystolic(80);
        bpLogDetailsSecond.setDiastolic(90);
        bpLogDetails.add(bpLogDetailsSecond);
        bpLogDetails.add(bpLogDetailsFirst);
        return bpLogDetails;
    }

    public static JSONObject getBpLogJson() {
        JSONObject bpLog = new JSONObject();
        bpLog.put(FieldConstants.AVG_SYSTOLIC, "80");
        bpLog.put(Constants.AVG_DIASTOLIC, "90");
        bpLog.put(Constants.BPLOG_DETAILS, getBpLogDetails());
        return bpLog;
    }

    public static JSONObject getGlucoseLogJson() {
        JSONObject glucoseLog = new JSONObject();
        glucoseLog.put(FieldConstants.GLUCOSE_VALUE, "1.234");
        glucoseLog.put(Constants.GLUCOSE_TYPE, "type");
        glucoseLog.put(Constants.BLOOD_GLUCOSE_UNIT, "unit");
        glucoseLog.put(Constants.GLUCOSE_DATE_TIME, "2022-06-07T14:15:40+00:00");
        return glucoseLog;
    }

    public static Site getSiteObject() {
        Site site = new Site();
        site.setId(1L);
        site.setCountryId(3L);
        return site;
    }

    public static ScreeningLogDTO getScreeningLogDTOForOldLogConstruct() {
        ScreeningLogDTO screeningLogDTO = TestDataProvider.getScreeningLogDTO();
        JSONObject bioData = TestDataProvider.getBioDataJson();
        JSONObject bioMetrics = TestDataProvider.getBioMetricsJson();
        JSONObject bpLog = TestDataProvider.getBpLogJson();
        JSONObject glucoseLog = TestDataProvider.getGlucoseLogJson();
        BioDataDTO bioDataObj = new BioDataDTO();
        BioMetricsDTO bioMetricsDTO = new BioMetricsDTO();
        BpLog bpLogData = new BpLog();
        GlucoseLog glucoseLogData = new GlucoseLog();

        bioDataObj.setNationalId("1");
        bioDataObj.setPhoneNumberCategory(bioData.get(Constants.PHONE_NUMBER_CATEGORY).toString());
        bioDataObj.setLastName(bioData.get(Constants.LAST_NAME).toString());
        bioDataObj.setPhoneNumber(bioData.get(Constants.PHONE_NUMBER).toString());
        bioDataObj.setMiddleName(bioData.get(Constants.MIDDLE_NAME).toString());
        bioDataObj.setLandmark(bioData.get(Constants.LANDMARK).toString());
        bioDataObj.setFirstName(bioData.get(Constants.FIRST_NAME).toString());

        bioMetricsDTO.setAge((int) Math.round(Double.parseDouble(bioMetrics.get(Constants.AGE).toString())));
        bioMetricsDTO.setIsRegularSmoker(Boolean.parseBoolean(bioMetrics.get(Constants.IS_REGULAR_SMOKER).toString()));
        bioMetricsDTO.setGender(bioMetrics.get(Constants.GENDER).toString());
        bioMetricsDTO.setHeight(Double.parseDouble(bioMetrics.get(Constants.HEIGHT).toString()));
        bioMetricsDTO.setWeight(Double.parseDouble(bioMetrics.get(Constants.WEIGHT).toString()));
        bioMetricsDTO.setBmi(Double.parseDouble(bioMetrics.get(Constants.BMI).toString()));

        bpLogData.setAvgSystolic((int) Math.round(Double.parseDouble(bpLog.get(FieldConstants.AVG_SYSTOLIC).toString())));
        bpLogData.setAvgDiastolic(
                (int) Math.round(Double.parseDouble(bpLog.get(Constants.AVG_DIASTOLIC).toString())));
        List<BpLogDetails> bpLogDetails = (List<BpLogDetails>) bpLog.get(Constants.BPLOG_DETAILS);
        bpLogData.setBpLogDetails(bpLogDetails);

        DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT_OLD_SCREENING_LOG);
        Date result;
        try {
            result = df.parse(glucoseLog.get(Constants.GLUCOSE_DATE_TIME).toString());
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.JSON_DATE_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone(Constants.GMT));
            glucoseLogData.setGlucoseDateTime(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        glucoseLogData.setGlucoseValue(Double.parseDouble(glucoseLog.get(FieldConstants.GLUCOSE_VALUE).toString()));
        glucoseLogData.setGlucoseType(glucoseLog.get(Constants.GLUCOSE_TYPE).toString());
        glucoseLogData.setGlucoseUnit(glucoseLog.get(Constants.BLOOD_GLUCOSE_UNIT).toString());

        screeningLogDTO.setCategory(null);
        screeningLogDTO.setLongitude(null);
        screeningLogDTO.setLatitude(null);
        screeningLogDTO.setIsReferAssessment(null);
        screeningLogDTO.setUnitMeasurement(null);
        screeningLogDTO.setPhq4(null);
        screeningLogDTO.setBioData(bioDataObj);
        screeningLogDTO.setBioMetrics(bioMetricsDTO);
        screeningLogDTO.setBpLog(bpLogData);
        screeningLogDTO.setGlucoseLog(glucoseLogData);

        return screeningLogDTO;
    }

    public static UserResponseDTO getUserResponseDTO() {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(1L);
        user.setPhoneNumber(Constants.PHONE_NUMBER);
        user.setTenantId(4L);
        user.setCountryCode(Constants.COUNTRY);
        user.setUsername(Constants.NAME);
        return user;
    }

    public static PatientAssessment getPatientAssessment() {
        PatientAssessment patientAssessment = new PatientAssessment();
        patientAssessment.setPatientTrackId(1L);
        patientAssessment.setBpLogId(1L);
        patientAssessment.setGlucoseLogId(1L);
        patientAssessment.setType(Constants.ASSESSMENT);
        patientAssessment.setId(1L);
        patientAssessment.setTenantId(4L);
        return patientAssessment;
    }

    public static SmsDTO getSmsDTO() {
        SmsDTO user = new SmsDTO();
        user.setTenantId(4L);
        user.setUserName(Constants.NAME);
        user.setNotificationId(1L);
        user.setFormDataId(1L);
        user.setFormName(Constants.FORM_NAME);
        return user;
    }

    public static DiagnosisDTO getDiagnosisDTO() {
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO();
        diagnosisDTO.setIsHtnDiagnosis(Constants.BOOLEAN_TRUE);
        diagnosisDTO.setIsDiabetesDiagnosis(Constants.BOOLEAN_TRUE);
        diagnosisDTO.setHtnYearOfDiagnosis(2020);
        diagnosisDTO.setHtnPatientType(Constants.DIABETES_MELLITUS_TYPE_1);
        diagnosisDTO.setDiabetesYearOfDiagnosis(2020);
        diagnosisDTO.setDiabetesDiagnosis(Constants.DIABETES);
        return diagnosisDTO;
    }

    public static InitialMedicalReviewDTO getInitialMedicalReviewDTO() {
        InitialMedicalReviewDTO medicalReviewDTO = new InitialMedicalReviewDTO();
        medicalReviewDTO.setDiagnosis(getPatientDiagnosis());
        medicalReviewDTO.setLifestyle(Set.of(getPatientLifestyle()));
        medicalReviewDTO.setComplications(Set.of(getPatientComplication()));
        medicalReviewDTO.setComorbidities(Set.of(getPatientComorbidity()));
        medicalReviewDTO.setCurrentMedications(getMedicationDetailsDTO());
        return medicalReviewDTO;
    }

    private static CurrentMedicationDetailsDTO getMedicationDetailsDTO() {
        CurrentMedicationDetailsDTO data = new CurrentMedicationDetailsDTO();
        data.setMedications(Set.of(getCurrentMedicationDTO()));
        return data;
    }

    private static CurrentMedicationDTO getCurrentMedicationDTO() {
        return mapper.map(getCurrentMedication(), CurrentMedicationDTO.class);
    }

    public static Organization getOrganization() {
        Organization organization = new Organization();
        organization.setFormDataId(1L);
        organization.setId(1L);
        organization.setParentOrganizationId(1L);
        organization.setName(Constants.NAME);
        return organization;
    }

    public static LifestyleDTO getLifestyleDTO() {
        return mapper.map(getLifestyle(), LifestyleDTO.class);
    }

    public static LabTestResultDTO getLabTestResultDTO() {
        LabTestResultDTO result = new LabTestResultDTO();
        result.setLabTestId(1L);
        result.setTenantId(4L);
        result.setId(1L);
        result.setName("Bp Test");
        result.setLabTestResultRanges(List.of(getLabTestResultRange()));
        return result;
    }

    public static LabTestResultRangeDTO getLabTestResultRange() {
        LabTestResultRangeDTO result = new LabTestResultRangeDTO();
        result.setLabTestId(1L);
        result.setId(1L);
        result.setMaximumValue(10.0);
        result.setMinimumValue(1.0);
        return result;
    }

    public static StaticDataDTO getStaticDataDTO() {
        return new StaticDataDTO();
    }

    public static MedicalReviewStaticDataDTO getMedicalReviewStaticDataDTO() {
        return new MedicalReviewStaticDataDTO();
    }

    public static MetaFormDTO getMetaFormDTO() {
        return new MetaFormDTO();
    }

    public static Culture getCulture() {
        Culture culture = new Culture();
        culture.setId(1L);
        culture.setName("English - India");
        culture.setCode("code");
        return culture;
    }

    public static CultureDTO getCultureDTO() {
        return mapper.map(getCulture(), CultureDTO.class);
    }

    public static CultureValues getCultureValues() {
        CultureValues cultureValues = new CultureValues();
        cultureValues.setFormDataId(1L);
        cultureValues.setFormName(Constants.FORM_NAME);
        cultureValues.setCultureId(1L);
        cultureValues.setCultureValue("testCultureValue");
        return cultureValues;
    }

    public static DosageFrequency getDosageFrequency() {
        DosageFrequency dosageFrequency = new DosageFrequency();
        dosageFrequency.setId(1L);
        dosageFrequency.setName("testDosageFrequency");
        return dosageFrequency;
    }

    public static UserListDTO getUserListDTO() {
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setFirstName(Constants.FIRST_NAME);
        userListDTO.setLastName(Constants.LAST_NAME);
        userListDTO.setCountryCode(Constants.COUNTRY);
        userListDTO.setPhoneNumber(Constants.PHONE_NUMBER);
        userListDTO.setId(1L);
        userListDTO.setCountry(getCountry());
        userListDTO.setCultureId(1L);
        userListDTO.setTenantId(4L);
        userListDTO.setDeviceInfoId(1L);
        return userListDTO;
    }

    public static ModelMapper setUp(Class<?> injectClass, String map, Object mockedObject) throws NoSuchFieldException, IllegalAccessException {
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        Field mapper = injectClass.getDeclaredField(map);
        mapper.setAccessible(true);
        mapper.set(mockedObject, modelMapper);
        return modelMapper;
    }

    public static PrescriberDTO getPrescriberDTO() {
        PrescriberDTO prescriberDTO = new PrescriberDTO();
        prescriberDTO.setCountryCode("+133");
        prescriberDTO.setFirstName(Constants.FIRST_NAME);
        prescriberDTO.setPhoneNumber(Constants.PHONE_NUMBER);
        prescriberDTO.setLastName(Constants.LAST_NAME);
        prescriberDTO.setLastRefillVisitId(1L);
        return prescriberDTO;
    }

    public static MyPatientListDTO getMyPatientListDTO() {
        MyPatientListDTO listDTO = new MyPatientListDTO();
        listDTO.setFirstName(Constants.FIRST_NAME);
        listDTO.setAge(32);
        listDTO.setLastName(Constants.LAST_NAME);
        listDTO.setGender(Constants.GENDER_MALE);
        listDTO.setInitialReview(false);
        listDTO.setId(1L);
        listDTO.setPatientId(1L);
        listDTO.setPatientStatus(Constants.ENROLLED);
        listDTO.setEnrollmentAt(new Date());
        listDTO.setScreeningLogId(1L);
        listDTO.setCreatedAt(null);
        return listDTO;
    }

    public static List<AccountWorkflow> getClinicalWorKflows() {
        AccountWorkflow accountWorkflow = new AccountWorkflow();
        AccountWorkflow secondAccountWorkflow = new AccountWorkflow();
        accountWorkflow.setWorkflow(Constants.DIABETES);
        accountWorkflow.setId(1L);
        secondAccountWorkflow.setId(2L);
        secondAccountWorkflow.setWorkflow(Constants.HYPERTENSION);
        accountWorkflow.setModuleType(Constants.MODULE_TYPE);
        secondAccountWorkflow.setModuleType(Constants.MODULE_TYPE);
        return List.of(accountWorkflow, secondAccountWorkflow);
    }

    public static Account getAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setClinicalWorkflows(getClinicalWorKflows());
        return account;
    }

    public static Program getProgram() {
        Program program = new Program();
        program.setName("Novo Nordisk");
        return program;
    }

    public static List<Program> getPrograms() {
        Program program = getProgram();
        program.setSites(Set.of(new Site(1L)));
        Program secondProgram = getProgram();
        secondProgram.setName("KMTC Medi Camp");
        program.setSites(Set.of(new Site(2L)));
        return List.of(program, secondProgram);
    }

    public static List<SiteResponseDTO> getSiteResponseDTOs() {
        SiteResponseDTO siteResponseDTO = new SiteResponseDTO();
        siteResponseDTO.setId(1L);
        siteResponseDTO.setCulture(getCulture());
        return List.of(siteResponseDTO);
    }

    public static Unit getUnit() {
        Unit unit = new Unit();
        unit.setName(FieldConstants.CC);
        return unit;
    }

    public static Subcounty getSubCounty() {
        Subcounty subcounty = new Subcounty();
        subcounty.setCountyId(1L);
        return subcounty;
    }

    public static County getCounty() {
        County county = new County();
        county.setId(1L);
        county.setCountryId(1L);
        return county;
    }

    public static AccountCustomization getAccountCustomization() {
        AccountCustomization accountCustomization = new AccountCustomization();
        accountCustomization.setTenantId(1L);
        accountCustomization.setAccountId(1L);
        accountCustomization.setCategory(Constants.CONSENT_FORM);
        accountCustomization.setType(Constants.MODULE);
        accountCustomization.setClinicalWorkflowId(1L);
        accountCustomization.setFormInput(Constants.FORM_INPUT);
        return accountCustomization;
    }

    public static RegionCustomization getRegionCustomization() {
        RegionCustomization regionCustomization = new RegionCustomization();
        regionCustomization.setCountryId(1L);
        regionCustomization.setCategory(Constants.INPUT_FORM);
        regionCustomization.setFormInput("{formLayout:[{view:input}]}");
        return regionCustomization;
    }

    public static List<AccountWorkflow> getAccountWorkflows() {
        AccountWorkflow accountWorkflow = new AccountWorkflow();
        accountWorkflow.setWorkflow(Constants.PREGNANCYDETAILS);
        accountWorkflow.setId(1L);
        accountWorkflow.setModuleType(Constants.MODULE_TYPE);
        return List.of(accountWorkflow);
    }

    public static List<Site> getSites() {
        return List.of(getSite());
    }

    public static CustomizationRequestDTO getCustomizationRequestDTO() {
        CustomizationRequestDTO customizationRequestDTO = new CustomizationRequestDTO();
        customizationRequestDTO.setCountryId(1L);
        customizationRequestDTO.setType(Constants.SCREENING);
        customizationRequestDTO.setCategory(Constants.CONSENT_FORM);
        customizationRequestDTO.setAccountId(1L);
        customizationRequestDTO.setTenantId(1L);
        return customizationRequestDTO;
    }

    public static SMSTemplate getSMSTemplate() {
        SMSTemplate smsTemplate = new SMSTemplate();
        smsTemplate.setId(1);
        smsTemplate.setType(Constants.TYPE);
        smsTemplate.setBody(Constants.BODY);
        return smsTemplate;
    }
}