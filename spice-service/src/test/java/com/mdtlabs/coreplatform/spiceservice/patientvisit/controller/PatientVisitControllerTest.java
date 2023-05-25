package com.mdtlabs.coreplatform.spiceservice.patientvisit.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * <p>
 * PatientVisitControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientVisitController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class PatientVisitControllerTest {

    @InjectMocks
    private PatientVisitController patientVisitController;

    @Mock
    private PatientVisitService patientVisitService;

    private RequestDTO requestDTO = TestDataProvider.getRequestDto();

    private Map<String, Long> result = new HashMap<>();

    private SuccessResponse<Map<String, Long>> response;

    @Test
    @DisplayName("AddPatientVist Test")
    void addPatientVisit() {
        //given
        requestDTO.setPatientVisitId(1L);
        result.put("patientVisitId", 1l);
        //when
        when(patientVisitService.createPatientVisit(requestDTO)).thenReturn(result);
        //then
        response = patientVisitController.addPatientVisit(requestDTO);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(requestDTO.getPatientVisitId(),
                patientVisitService.createPatientVisit(requestDTO).get("patientVisitId"));
    }
}
