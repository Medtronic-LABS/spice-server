package com.mdtlabs.coreplatform.spiceservice.patienttransfer.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferUpdateRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTransfer;
import com.mdtlabs.coreplatform.common.model.enumeration.spice.PatientTransferStatus;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patienttransfer.service.PatientTransferService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Patient Transfer Controller Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 23, 2023
 */
@ExtendWith(MockitoExtension.class)
class PatientTransferControllerTest {

    @InjectMocks
    private PatientTransferController patientTransferController;

    @Mock
    private PatientTransferService patientTransferService;

    @Test
    void createPatientTransfer() {
        //given
        PatientTransferRequestDTO patientTransferDto = TestDataProvider.getPatientTransferRequestDTO();

        //when
        doNothing().when(patientTransferService).createPatientTransfer(patientTransferDto);

        //then
        patientTransferController.createPatientTransfer(patientTransferDto);
        verify(patientTransferService, atLeastOnce()).createPatientTransfer(patientTransferDto);
    }

    @Test
    void validatePatientTransfer() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();

        //when
        doNothing().when(patientTransferService).validatePatientTransfer(requestDTO);

        //then
        SuccessResponse<Map<String, String>> validatePatientTransfer = patientTransferController
                .validatePatientTransfer(requestDTO);

        Assertions.assertEquals(HttpStatus.OK, validatePatientTransfer.getStatusCode());
    }

    @Test
    void getPatientTransferList() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        Map<String, Object> patientTransferList = new HashMap<>();

        //when
        when(patientTransferService.getPatientTransferList(requestDTO)).thenReturn(patientTransferList);

        //then
        SuccessResponse<Map<String, Object>> getPatientTransferList = patientTransferController
                .getPatientTransferList(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, getPatientTransferList.getStatusCode());
    }

    @Test
    void updatePatientTransfer() {
        //given
        PatientTransferUpdateRequestDTO patientTransferUpdateRequestDTO = TestDataProvider
                .getPatientTransferUpdateRequestDTO();
        patientTransferUpdateRequestDTO.setTransferStatus(PatientTransferStatus.ACCEPTED);

        //when
        doNothing().when(patientTransferService).updatePatientTransfer(patientTransferUpdateRequestDTO);

        //then
        patientTransferController.updatePatientTransfer(patientTransferUpdateRequestDTO);
        verify(patientTransferService, atLeastOnce()).updatePatientTransfer(patientTransferUpdateRequestDTO);
        SuccessResponse<PatientTransfer> successResponse = patientTransferController
                .updatePatientTransfer(patientTransferUpdateRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void getPatientTransferCount() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        Map<String, Long> patientTransferCount = new HashMap<>();

        //when
        when(patientTransferService.getPatientTransferCount(requestDTO)).thenReturn(patientTransferCount);

        //then
        SuccessResponse<PatientTransfer> getPatientTransferCount = patientTransferController
                .getPatientTransferCount(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, getPatientTransferCount.getStatusCode());
    }
}