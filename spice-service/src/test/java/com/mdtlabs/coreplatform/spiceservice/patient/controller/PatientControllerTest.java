package com.mdtlabs.coreplatform.spiceservice.patient.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LifestyleDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MyPatientListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientDetailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTrackerDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PregnancyDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchPatientListDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientPregnancyDetails;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patient.service.PatientService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * <p>
 * PatientControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatientControllerTest {

    @InjectMocks
    private PatientController patientController;

    @Mock
    private PatientService patientService;

    @Mock
    private PatientTrackerService patientTrackerService;

    @Mock
    private ModelMapper mapper;

    private SuccessResponse<EnrollmentResponseDTO> enrollmentResponse;

    private SuccessResponse<PregnancyDetailsDTO> pregnancyResponse;

    private SuccessResponse<PatientTrackerDTO> patientTrackerResponse;

    private SuccessResponse<PatientPregnancyDetails> pregnancySuccessResponse;

    private SuccessResponse<List<MyPatientListDTO>> successResponse;

    private SuccessResponse<List<SearchPatientListDTO>> searchSuccessResponse;

    private SuccessResponse<List<LifestyleDTO>> lifeStyleResponse;

    private SuccessResponse<PatientDetailDTO> patientDetailDTOSuccessResponse;

    //Request data
    private ResponseListDTO response = new ResponseListDTO();

    private EnrollmentRequestDTO enrollmentRequestDTO = TestDataProvider.getEnrollmentRequestDto();

    private EnrollmentResponseDTO enrollmentResponseDTO = TestDataProvider.getEnrollmentResponseDTO();

    private PatientTrackerDTO patientTrackerDTO = TestDataProvider.getPatientTrackerDTO();

    private PregnancyDetailsDTO pregnancyRequestDTO = TestDataProvider.getPregnancyDetails();

    private PatientPregnancyDetails patientDetails = TestDataProvider.getPatientPregnancyDetails();

    private PatientGetRequestDTO patientRequest = TestDataProvider.getPatientGetRequestDTO();

    private GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();

    private CommonRequestDTO request = TestDataProvider.getRequestDto();

    private PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();

    private Patient patient = TestDataProvider.getPatient();

    private PatientDetailDTO patientDetailDTO = TestDataProvider.getPatientDetailsDTO();

    @Test
    @DisplayName("patientCreate Test")
    void createPatient() {
        //when
        when(patientService.createPatient(enrollmentRequestDTO)).thenReturn(enrollmentResponseDTO);
        //then
        assertNotNull(enrollmentRequestDTO);
        enrollmentResponse = patientController.createPatient(enrollmentRequestDTO);
        assertEquals(HttpStatus.CREATED,
                enrollmentResponse.getStatusCode(), "SuccessStatus Code Should be Same");
        assertEquals(enrollmentResponseDTO.getBpLog(),
                patientService.createPatient(enrollmentRequestDTO).getBpLog());
        assertEquals(enrollmentRequestDTO.getPatientTrackId(),
                patientService.createPatient(enrollmentRequestDTO).getEnrollment().getPatientTrackId());
    }

    @Test
    @DisplayName("GetPatientDetails Test")
    void getPatientDetails() {
        //when
        when(patientService.getPatientDetails(patientRequest)).thenReturn(patientTrackerDTO);
        //then
        patientTrackerResponse = patientController.getPatientDetails(patientRequest);
        assertEquals(HttpStatus.OK, patientTrackerResponse.getStatusCode());
        assertEquals(patientTrackerDTO, patientService.getPatientDetails(patientRequest));
        patientRequest.setId(0l);
        Assertions.assertNotEquals(patientRequest.getId(), patientService.getPatientDetails(patientRequest).getId());
    }

    @Test
    @DisplayName("CreatePregnancyDetails Test")
    void createPregnancyDetails() {
        //when
        when(patientService.createPregnancyDetails(pregnancyRequestDTO)).thenReturn(pregnancyRequestDTO);
        //then
        assertNotNull(pregnancyRequestDTO);
        pregnancyResponse = patientController.createPregnancyDetails(pregnancyRequestDTO);
        assertEquals(HttpStatus.CREATED,
                pregnancyResponse.getStatusCode(), "SuccessCode Must be Same");
        assertEquals(patientDetails.getId(),
                patientService.createPregnancyDetails(pregnancyRequestDTO).getId(),
                "PregnancyDetails Id Should be same");

    }

    @Test
    @DisplayName("GetPregnancyDetails Test")
    void getPregnancyDetails() {
        //when
        when(patientService.getPregnancyDetails(requestDTO)).thenReturn(pregnancyRequestDTO);
        //then
        pregnancyResponse = patientController.getPregnancyDetails(requestDTO);
        assertNotNull(patientService.getPregnancyDetails(requestDTO));
        assertEquals(patientDetails.getId(), patientService.getPregnancyDetails(requestDTO).getId());
        requestDTO.setId(2l);
        Assertions.assertNotEquals(requestDTO.getId(), patientService.getPregnancyDetails(requestDTO).getId());
    }

    @Test
    @DisplayName("UpdatePregnancyDetails Test")
    void updatePregnancyDetails() {
        //given
        pregnancyRequestDTO.setPatientPregnancyId(1L);
        pregnancyRequestDTO.setPregnancyFetusesNumber(5);
        //when
        when(patientService.updatePregnancyDetails(pregnancyRequestDTO)).thenReturn(patientDetails);
        //then
        pregnancySuccessResponse = patientController.updatePregnancyDetails(pregnancyRequestDTO);
        assertEquals(HttpStatus.OK, pregnancySuccessResponse.getStatusCode());
        assertEquals(pregnancyRequestDTO.getPatientPregnancyId(),
                patientService.updatePregnancyDetails(pregnancyRequestDTO).getId());
        assertEquals(pregnancyRequestDTO.getPregnancyFetusesNumber(),
                patientService.updatePregnancyDetails(pregnancyRequestDTO).getPregnancyFetusesNumber());
    }

    @Test
    @DisplayName("GetMyPatientList Test")
    void getMyPatientList() {
        //given
        response = TestDataProvider.getResponseListDTO();
        //when
        when(patientService.listMyPatients(patientRequestDTO)).thenReturn(response);
        //then
        successResponse = patientController.getMyPatientsList(patientRequestDTO);
        ResponseListDTO actualResponse = patientService.listMyPatients(patientRequestDTO);
        assertNotNull(successResponse);
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        assertEquals(response.getTotalCount(), actualResponse.getTotalCount());
        assertEquals(response.getData(), actualResponse.getData());

        //given
        response = new ResponseListDTO();
        //when
        when(patientService.listMyPatients(patientRequestDTO)).thenReturn(response);
        assertNull(response.getTotalCount());
        successResponse = patientController.getMyPatientsList(patientRequestDTO);
        actualResponse = patientService.listMyPatients(patientRequestDTO);
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        assertEquals(response.getTotalCount(), actualResponse.getTotalCount());
    }

    @Test
    @DisplayName("SearchPatient Test")
    void searchPatients() {
        //given
        response = TestDataProvider.getResponseListDTO();
        //when
        when(patientService.searchPatients(patientRequestDTO)).thenReturn(response);
        //then
        assertNotNull(response.getTotalCount());
        ResponseListDTO actualResponse = patientService.searchPatients(patientRequestDTO);
        searchSuccessResponse = patientController.searchPatients(patientRequestDTO);
        assertEquals(HttpStatus.OK, searchSuccessResponse.getStatusCode());
        assertEquals(response.getTotalCount(), actualResponse.getTotalCount());
        assertEquals(response.getData(), actualResponse.getData());
        //given
        response = new ResponseListDTO();
        //when
        when(patientService.searchPatients(patientRequestDTO)).thenReturn(response);
        //then
        assertNull(response.getTotalCount());
        actualResponse = patientService.searchPatients(patientRequestDTO);
        searchSuccessResponse = patientController.searchPatients(patientRequestDTO);
        assertEquals(HttpStatus.OK, searchSuccessResponse.getStatusCode());
        assertEquals(response.getTotalCount(), actualResponse.getTotalCount());
        assertEquals(response.getData(), actualResponse.getData());
    }

    @Test
    @DisplayName("GetCountryWisePatient Test")
    void getCountryWisePatientsWithAdvanceSearch() {
        //given
        assertNull(patientRequestDTO.getOperatingUnitId());
        patientRequestDTO.setGlobally(Constants.BOOLEAN_TRUE);
        //when
        when(patientService.patientAdvanceSearch(patientRequestDTO)).thenReturn(response);
        //then
        assertNull(response.getTotalCount());
        searchSuccessResponse = patientController.getCountryWisePatientsWithAdvanceSearch(patientRequestDTO);
        ResponseListDTO actualResponse = patientService.patientAdvanceSearch(patientRequestDTO);
        assertEquals(HttpStatus.OK, searchSuccessResponse.getStatusCode());
        assertEquals(response, actualResponse);
        //given
        response = TestDataProvider.getResponseListDTO();
        patientRequestDTO.setOperatingUnitId(1L);
        assertNotNull(patientRequestDTO.getOperatingUnitId());
        //when
        when(patientService.patientAdvanceSearch(patientRequestDTO)).thenReturn(response);
        //then
        assertNotNull(response.getTotalCount());
        searchSuccessResponse = patientController.getCountryWisePatientsWithAdvanceSearch(patientRequestDTO);
        actualResponse = patientService.patientAdvanceSearch(patientRequestDTO);
        assertEquals(HttpStatus.OK, searchSuccessResponse.getStatusCode());
        assertEquals(response, actualResponse);
        assertEquals(response.getTotalCount(), actualResponse.getTotalCount());
    }

    @Test
    @DisplayName("GetPatientsWithAdvanceSearch Test")
    void getPatientsWithAdvanceSearch() {
        //given
        assertNotNull(patientRequestDTO);
        //when
        when(patientService.patientAdvanceSearch(patientRequestDTO)).thenReturn(response);
        //then
        assertNull(response.getTotalCount());
        searchSuccessResponse = patientController.getPatientsWithAdvanceSearch(patientRequestDTO);
        ResponseListDTO actualResponse = patientService.patientAdvanceSearch(patientRequestDTO);
        assertEquals(HttpStatus.OK, searchSuccessResponse.getStatusCode());
        assertEquals(response.getTotalCount(), actualResponse.getTotalCount());
        assertEquals(response, actualResponse);
        //given
        response = TestDataProvider.getResponseListDTO();
        //when
        when(patientService.patientAdvanceSearch(patientRequestDTO)).thenReturn(response);
        //then
        assertNotNull(response.getTotalCount());
        searchSuccessResponse = patientController.getPatientsWithAdvanceSearch(patientRequestDTO);
        actualResponse = patientService.patientAdvanceSearch(patientRequestDTO);
        assertEquals(HttpStatus.OK, searchSuccessResponse.getStatusCode());
        assertEquals(response.getTotalCount(), actualResponse.getTotalCount());
        assertEquals(response, actualResponse);
    }

    @Test
    @DisplayName("GetPatientLifeStyleDetails Test")
    void getPatientLifeStyleDetails() {
        //given
        List<LifestyleDTO> response = new ArrayList<>();
        //when
        when(patientService.getPatientLifeStyleDetails(request)).thenReturn(response);
        //then
        lifeStyleResponse = patientController.getPatientLifeStyleDetails(request);
        assertEquals(HttpStatus.OK, lifeStyleResponse.getStatusCode());
        Assertions.assertEquals(response.size(), patientService.getPatientLifeStyleDetails(request).size());
    }

    @Test
    @DisplayName("UpdatePatient Test")
    void updatePatient() {
        //when
        when(patientService.updatePatientDetails(patientDetailDTO)).thenReturn(patientDetailDTO);
        //then
        patientDetailDTOSuccessResponse = patientController.updatePatient(patientDetailDTO);
        assertEquals(HttpStatus.OK, patientDetailDTOSuccessResponse.getStatusCode());
        assertEquals(patient.getId(), patientService.updatePatientDetails(patientDetailDTO).getId());
    }

    @Test
    @DisplayName("GetPatientBasicDetails Test")
    void getPatientBasicDetails() {
        //when
        when(patientService.getPatientBasicDetails(request)).thenReturn(patientDetailDTO);
        //then
        patientDetailDTOSuccessResponse = patientController.getPatientBasicDetails(request);
        Assertions.assertEquals(HttpStatus.OK, patientDetailDTOSuccessResponse.getStatusCode());
        Assertions.assertEquals(patientDetailDTO, patientService.getPatientBasicDetails(request));
        Assertions.assertEquals(patientDetailDTO.getNationalId(), patientService.getPatientBasicDetails(request).getNationalId());

    }

    @Test
    @DisplayName("RemovePatient Test")
    void removePatient() {
        when(patientService.removePatientDetails(patientRequestDTO)).thenReturn(patient);
        when(mapper.map(patient, PatientDetailDTO.class)).thenReturn(patientDetailDTO);
        patientDetailDTOSuccessResponse = patientController.removePatient(patientRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, patientDetailDTOSuccessResponse.getStatusCode());
    }
}