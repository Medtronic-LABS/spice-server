package com.mdtlabs.coreplatform.spiceadminservice.medication.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OtherMedicationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Medication;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.medication.service.impl.MedicationServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Medication controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class MedicationControllerTest {

    @InjectMocks
    private MedicationController medicationController;

    @Mock
    private MedicationServiceImpl medicationService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(MedicationController.class, "modelMapper", medicationController);
    }

    @Test
    void testAddMedication() {
        //given
        List<Medication> medications = TestDataProvider.getMedications();

        //when
        when(medicationService.addMedication(medications)).thenReturn(medications);

        //then
        SuccessResponse<List<MedicationDTO>> actualMedication = medicationController.addMedication(medications);
        assertNotNull(actualMedication);
        assertEquals(HttpStatus.CREATED, actualMedication.getStatusCode());
    }

    @Test
    void testUpdateMedication() {
        //given
        Medication medication = TestDataProvider.getMedication();
        MedicationDTO medicationDTO = TestDataProvider.getMedicationDTOs().get(0);

        //when
        when(medicationService.updateMedication(medication)).thenReturn(medication);
        when(modelMapper.map(medicationDTO, Medication.class)).thenReturn(medication);

        //then
        SuccessResponse<Medication> actualMedication = medicationController.updateMedication(medicationDTO);
        assertNotNull(actualMedication);
        assertEquals(HttpStatus.OK, actualMedication.getStatusCode());
    }

    @Test
    void testGetMedicationById() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        Medication medication = TestDataProvider.getMedication();

        //when
        when(medicationService.getMedicationById(requestDTO)).thenReturn(medication);

        //then
        SuccessResponse<Medication> actualMedication = medicationController.getMedicationById(requestDTO);
        assertNotNull(actualMedication);
        assertEquals(HttpStatus.OK, actualMedication.getStatusCode());
    }

    @Test
    void testGetAllMedicationsEmpty() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        Map<String, Object> medications = new HashMap<>();

        //when
        when(medicationService.getAllMedications(requestDTO)).thenReturn(medications);

        //then
        SuccessResponse<List<MedicationDTO>> actualMedications = medicationController.getAllMedications(requestDTO);
        assertNotNull(actualMedications);
        assertEquals(HttpStatus.OK, actualMedications.getStatusCode());
    }

    @Test
    void testGetAllMedications() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        List<MedicationDTO> medicationListDTOs = TestDataProvider.getMedicationDTOs();
        List<Medication> medicationsList = TestDataProvider.getMedications();
        Map<String, Object> medications = Map.of(Constants.MEDICATION_LIST, medicationsList, Constants.TOTAL_COUNT, Constants.ONE);

        //when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(medicationService.getAllMedications(requestDTO)).thenReturn(medications);
        when(modelMapper.map(medicationsList, new TypeToken<List<MedicationDTO>>() {
        }.getType())).thenReturn(medicationListDTOs);

        //then
        SuccessResponse<List<MedicationDTO>> actualMedications = medicationController.getAllMedications(requestDTO);
        assertNotNull(actualMedications);
        assertEquals(HttpStatus.OK, actualMedications.getStatusCode());
    }

    @Test
    void testDeleteMedicationById() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);

        //when
        when(medicationService.deleteMedicationById(requestDTO)).thenReturn(Boolean.TRUE);

        //then
        SuccessResponse<Boolean> deleteMedicationById = medicationController.deleteMedicationById(requestDTO);
        assertNotNull(deleteMedicationById);
        assertEquals(HttpStatus.OK, deleteMedicationById.getStatusCode());
    }

    @Test
    void testSearchMedications() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        List<Medication> medications = TestDataProvider.getMedications();

        //when
        when(medicationService.searchMedications(requestDTO)).thenReturn(medications);
        when(modelMapper.map(medications, new TypeToken<List<MedicationDTO>>() {
        }.getType())).thenReturn(medications);

        //then
        SuccessResponse<List<MedicationDTO>> actualMedications = medicationController.searchMedications(requestDTO);
        assertNotNull(actualMedications);
        assertEquals(HttpStatus.OK, actualMedications.getStatusCode());
    }

    @Test
    void testSearchMedicationsEmpty() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);

        //when
        when(medicationService.searchMedications(requestDTO)).thenReturn(new ArrayList<>());

        //then
        SuccessResponse<List<MedicationDTO>> actualMedications = medicationController.searchMedications(requestDTO);
        assertNotNull(actualMedications);
        assertEquals(HttpStatus.OK, actualMedications.getStatusCode());
    }

    @Test
    void testValidateMedication() {
        //given
        Medication medication = TestDataProvider.getMedication();
        MedicationDTO medicationDTO = TestDataProvider.getMedicationDTOs().get(0);

        //when
        when(medicationService.validateMedication(medication)).thenReturn(Boolean.TRUE);
        when(modelMapper.map(medicationDTO, Medication.class)).thenReturn(medication);

        //then
        SuccessResponse<Boolean> validatedMedication = medicationController.validateMedication(medicationDTO);
        assertNotNull(validatedMedication);
        assertEquals(HttpStatus.OK, validatedMedication.getStatusCode());
    }

    @Test
    void testGetOtherMedications() {
        //given
        Medication medication = TestDataProvider.getMedication();
        OtherMedicationDTO otherMedicationDTO = new OtherMedicationDTO();

        //when
        when(medicationService.getOtherMedication(TestConstants.ONE)).thenReturn(medication);
        when(modelMapper.map(medication, OtherMedicationDTO.class)).thenReturn(otherMedicationDTO);

        //then
        ResponseEntity<OtherMedicationDTO> actualOtherMedications = medicationController.getOtherMedications(TestConstants.ONE);
        assertNotNull(actualOtherMedications);
        assertEquals(HttpStatus.OK, actualOtherMedications.getStatusCode());
    }

    @Test
    void testGetOtherMedicationsNull() {
        //when
        when(medicationService.getOtherMedication(TestConstants.ONE)).thenReturn(null);

        //then
        assertThrows(DataNotAcceptableException.class, () -> medicationController.getOtherMedications(TestConstants.ONE));
    }
}