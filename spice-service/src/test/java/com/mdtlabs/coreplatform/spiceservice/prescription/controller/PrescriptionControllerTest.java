package com.mdtlabs.coreplatform.spiceservice.prescription.controller;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import com.mdtlabs.coreplatform.common.exception.SpiceValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionHistoryResponse;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.prescription.service.PrescriptionService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;

/**
 * <p>
 * PrescriptionControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PrescriptionController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PrescriptionControllerTest {

    @InjectMocks
    private PrescriptionController prescriptionController;

    @Mock
    private PrescriptionService prescriptionService;

    @Mock
    private ObjectMapper mapper;

    private RequestDTO requestDTO = TestDataProvider.getRequestDto();

    private PrescriptionRequestDTO prescriptionRequestDTO = TestDataProvider.getPrescriptionRequestDTO();

    private Prescription prescription = TestDataProvider.getPrescription();

    private List<Prescription> prescriptions = List.of(prescription);

    private PrescriptionDTO prescriptionDTO = TestDataProvider.getPrescriptionDTO();

    private List<PrescriptionDTO> prescriptionRequestDTOList = List.of(prescriptionDTO);

    private PrescriptionHistoryResponse historyResponse = TestDataProvider.getPrescriptionHistoryResponse();

    private SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto();

    private FillPrescriptionRequestDTO fillPrescriptionRequestDTO = TestDataProvider.getPrescriptionResponseDTO();

    private SuccessResponse<List<PrescriptionDTO>> listDtoSuccessResponse;

    private SuccessResponse<String> stringSuccessResponse;

    private SuccessResponse<PrescriptionHistoryResponse> prescriptionHistoryResponse;

    private SuccessResponse<List<FillPrescriptionResponseDTO>> fillListResponse;

    private SuccessResponse<List<PrescriptionHistory>> prescriptionHistoryResponseList;

    @Test
    @DisplayName("ListPrescription Test")
    void listPrescription() {
        //given
        requestDTO.setPrescriptionId(1L);
        //when
        when(prescriptionService.getPrescriptions(requestDTO)).thenReturn(prescriptionRequestDTOList);
        //then
        listDtoSuccessResponse = prescriptionController.listPrescription(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, listDtoSuccessResponse.getStatusCode());
        Assertions.assertEquals(requestDTO.getPrescriptionId(),
                prescriptionService.getPrescriptions(requestDTO).get(0).getId());
    }

    @Test
    @DisplayName("listPrescriptionHistoryData Test")
    void listPrescriptionHistoryData() {
        //given
        requestDTO.setPrescriptionId(1L);
        //when
        when(prescriptionService.listPrescriptionHistoryData(requestDTO)).thenReturn(historyResponse);
        //then
        prescriptionHistoryResponse = prescriptionController.listPrescriptionHistoryData(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, prescriptionHistoryResponse.getStatusCode());
        Assertions.assertEquals(requestDTO.getPrescriptionId(),
                prescriptionService.listPrescriptionHistoryData(requestDTO)
                        .getPatientPrescription().get(0).getPrescriptionId());
    }

    @Test
    @DisplayName("RemovePrescription Test")
    void removePrescription() {
        //when
        doNothing().when(prescriptionService).removePrescription(requestDTO);
        stringSuccessResponse = prescriptionController.removePrescription(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, stringSuccessResponse.getStatusCode());
    }

    @Test
    @DisplayName("ListFillPrescription Test")
    void listFillPrescription() {
        //given
        List<FillPrescriptionResponseDTO> prescriptionList = List.of(TestDataProvider.getFillPrescriptionDTO());
        List<FillPrescriptionResponseDTO> list = List.of();
        //when
        when(prescriptionService.getFillPrescriptions(searchRequestDTO)).thenReturn(prescriptionList);
        //then
        fillListResponse = prescriptionController.listFillPrescription(searchRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, fillListResponse.getStatusCode());
        Assertions.assertNotNull(prescriptionList);
        Assertions.assertEquals(prescriptionList.size(),
                prescriptionService.getFillPrescriptions(searchRequestDTO).size());
        //when
        when(prescriptionService.getFillPrescriptions(new SearchRequestDTO())).thenReturn(list);
        Assertions.assertNotNull(prescriptionService.getFillPrescriptions(new SearchRequestDTO()));
        SuccessResponse<List<FillPrescriptionResponseDTO>> fillListSuccess = prescriptionController
                .listFillPrescription(new SearchRequestDTO());
        Assertions.assertEquals(HttpStatus.OK, fillListSuccess.getStatusCode());
        Assertions.assertEquals(0,
                prescriptionService.getFillPrescriptions(new SearchRequestDTO()).size());
        Assertions.assertEquals(list,
                prescriptionService.getFillPrescriptions(new SearchRequestDTO()));
    }

    @Test
    @DisplayName("UpdateFillPrescription Test")
    void updateFillPrescription() {
        //given
        fillPrescriptionRequestDTO.setPrescriptions(List.of(prescription));
        //when
        when(prescriptionService.updateFillPrescription(fillPrescriptionRequestDTO)).thenReturn(prescriptions);
        //then
        stringSuccessResponse = prescriptionController.updateFillPrescription(fillPrescriptionRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, stringSuccessResponse.getStatusCode());
        Assertions.assertEquals(fillPrescriptionRequestDTO.getPrescriptions().get(0),
                prescriptionService.updateFillPrescription(fillPrescriptionRequestDTO).get(0));
        Assertions.assertEquals(fillPrescriptionRequestDTO.getPrescriptions().size(),
                prescriptionService.updateFillPrescription(fillPrescriptionRequestDTO).size());
    }

    @Test
    @DisplayName("GetReFillPrescriptionHistory Test")
    void getReFillPrescriptionHistory() {
        //given
        List<PrescriptionHistory> prescriptionHistories = List.of(TestDataProvider.getPrescriptionHistory());
        List<PrescriptionHistory> prescriptionHistoryList = List.of();
        //when
        when(prescriptionService.getRefillPrescriptionHistory(new SearchRequestDTO()))
                .thenReturn(prescriptionHistoryList);
        when(prescriptionService.getRefillPrescriptionHistory(searchRequestDTO)).thenReturn(prescriptionHistories);
        //then
        prescriptionHistoryResponseList = prescriptionController.getReFillPrescriptionHistory(searchRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, prescriptionHistoryResponseList.getStatusCode());
        Assertions.assertNotNull(prescriptionService.getRefillPrescriptionHistory(searchRequestDTO));
        Assertions.assertNotNull(prescriptionService.getRefillPrescriptionHistory(new SearchRequestDTO()));
        Assertions.assertEquals(searchRequestDTO.getPatientTrackId(),
                prescriptionService.getRefillPrescriptionHistory(searchRequestDTO).get(0).getPatientTrackId());
        Assertions.assertEquals(prescriptionHistoryList.size(),
                prescriptionService.getRefillPrescriptionHistory(new SearchRequestDTO()).size());
        Assertions.assertEquals(prescriptionHistoryList,
                prescriptionService.getFillPrescriptions(new SearchRequestDTO()));
    }

    @Test
    @DisplayName("PrescriptionListEmpty Test")
    void setFillPrescriptionHistoryEmpty() {
        List<PrescriptionHistory> prescriptionHistoryList = List.of();
        when(prescriptionService.getRefillPrescriptionHistory(new SearchRequestDTO()))
                .thenReturn(prescriptionHistoryList);
        SuccessResponse<List<PrescriptionHistory>> listSuccessResponse = prescriptionController
                .getReFillPrescriptionHistory(new SearchRequestDTO());
        Assertions.assertTrue(prescriptionService.getRefillPrescriptionHistory(new SearchRequestDTO()).isEmpty());
        Assertions.assertEquals(HttpStatus.OK, listSuccessResponse.getStatusCode());
    }

    @Test
    @DisplayName("PrescriptionUpdateTest")
    void updatePrescription() {
        //given
        String prescriptionRequest = "";
        MultipartFile signatureFile = null;
        //then
        Assertions.assertThrows(SpiceValidation.class,
                () -> prescriptionController.addPrescription(prescriptionRequest, signatureFile));

        //given
        String object = "";
        MultipartFile signature = null;
        MockedConstruction<ObjectMapper> objectMapperMockedConstruction =
                Mockito.mockConstruction(ObjectMapper.class, (objectMapper, context) -> {
                    when(objectMapper.readValue(object, PrescriptionRequestDTO.class))
                            .thenThrow(JsonProcessingException.class);
                });
        //then
        Assertions.assertThrows(SpiceValidation.class,
                () ->prescriptionController.addPrescription(object, signature));
        objectMapperMockedConstruction.close();

        //given
        String prescriptionDto = "";
        MultipartFile multipartFile = null;
        SuccessResponse<String> response = new SuccessResponse<>(SuccessCode.PRESCRIPTION_SAVE, HttpStatus.CREATED);
        PrescriptionRequestDTO requestDTOs = TestDataProvider.getPrescriptionRequestDTO();
        //when
        MockedConstruction<ObjectMapper> objectMapperMocked =
                Mockito.mockConstruction(ObjectMapper.class, (objectMapper, context) -> {
                    when(objectMapper.readValue(prescriptionDto, PrescriptionRequestDTO.class))
                            .thenReturn(requestDTOs);
                });
        //then
        SuccessResponse<String> result = prescriptionController.addPrescription(prescriptionRequest, multipartFile);
        objectMapperMocked.close();
        Assertions.assertEquals(response, result);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }
}

