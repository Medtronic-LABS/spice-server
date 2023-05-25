package com.mdtlabs.coreplatform.spiceadminservice.medication.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Medication;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.medication.repository.MedicationRepository;
import com.mdtlabs.coreplatform.spiceadminservice.medication.service.impl.MedicationServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Medication service implementation.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MedicationServiceTest {

    @InjectMocks
    MedicationServiceImpl medicationService;

    @Mock
    MedicationRepository medicationRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(MedicationServiceImpl.class, "mapper", medicationService);
    }

    @Test
    void testAddMedication() {
        //given
        List<Medication> medications = TestDataProvider.getMedications();
        Set<Medication> medicationSet = new HashSet<>(medications);

        //when
        when(medicationRepository.saveAll(medicationSet)).thenReturn(medications);

        //then
        List<Medication> actualMedications = medicationService.addMedication(medications);
        assertNotNull(actualMedications);
        assertFalse(actualMedications.isEmpty());
        assertEquals(medications.size(), actualMedications.size());
        assertEquals(medications.get(0), actualMedications.get(0));
    }

    @Test
    void testUpdateMedication() {
        //given
        Medication medication = TestDataProvider.getMedication();

        //when
        when(medicationRepository.getMedicationByFieldsAndTenantId(
                medication.getClassificationId(), medication.getBrandId(), medication.getDosageFormId(),
                medication.getCountryId(), medication.getMedicationName(), medication.getTenantId())).thenReturn(medication);
        when(medicationRepository.getMedicationByIdAndIsDeletedAndTenantIdAndIsActiveTrue(medication.getId(),
                Boolean.FALSE, medication.getTenantId())).thenReturn(medication);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        doNothing().when(modelMapper).map(medication, medication);
        when(medicationRepository.save(medication)).thenReturn(medication);

        //then
        Medication actualMedication = medicationService.updateMedication(medication);
        assertNotNull(actualMedication);
        assertEquals(medication.getMedicationName(), actualMedication.getMedicationName());
    }

    @Test
    void testValidateMedication() {
        //given
        Medication medication = TestDataProvider.getMedication();

        //when
        when(medicationRepository.getMedicationByFieldsAndTenantId(medication.getClassificationId(), medication.getBrandId(),
                medication.getDosageFormId(), medication.getCountryId(), medication.getMedicationName(), medication.getTenantId()))
                .thenReturn(medication);

        //then
        Boolean actualMedication = medicationService.validateMedication(medication);
        assertNotNull(actualMedication);
        assertTrue(actualMedication);
    }

    @Test
    void testGetMedicationById() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        Medication medication = TestDataProvider.getMedication();

        //when
        when(medicationRepository.findByIdAndIsDeletedFalseAndTenantId(requestDTO.getId(),
                requestDTO.getTenantId())).thenReturn(medication);

        //then
        Medication actualMedication = medicationService.getMedicationById(requestDTO);
        assertNotNull(actualMedication);
        assertEquals(medication.getMedicationName(), actualMedication.getMedicationName());
        assertEquals(medication, actualMedication);
    }

    @Test
    void testGetAllMedications() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(TestConstants.MEDICATION_NAME,
                Constants.ZERO, TestConstants.TEN);
        requestDTO.setCountryId(TestConstants.ONE);
        requestDTO.setSortByAsc(true);
        List<Medication> medications = TestDataProvider.getMedications();
        Page<Medication> medicationPage = new PageImpl<>(medications);
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN,
                Sort.by(FieldConstants.UPDATED_AT).ascending());
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMockValidation(requestDTO.getSearchTerm());
        when(medicationRepository.getAllMedications(requestDTO.getSearchTerm(),
                requestDTO.getCountryId(), requestDTO.getTenantId(), pageable)).thenReturn(medicationPage);

        //then
        Map<String, Object> actualMedications = medicationService.getAllMedications(requestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualMedications);
        assertEquals(Constants.TWO, actualMedications.size());
        assertFalse(actualMedications.isEmpty());
        assertTrue(actualMedications.containsKey(Constants.MEDICATION_LIST));
        assertTrue(actualMedications.containsKey(Constants.TOTAL_COUNT));
    }

    @Test
    void getAllMedicationsInvalidSearch() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(TestConstants.MEDICATION_NAME,
                Constants.ZERO, TestConstants.TEN);
        requestDTO.setCountryId(TestConstants.ONE);
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMockValidationFalse(requestDTO.getSearchTerm());

        //then
        Map<String, Object> actualMedications = medicationService.getAllMedications(requestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualMedications);
        assertEquals(Constants.TWO, actualMedications.size());
        assertFalse(actualMedications.isEmpty());
        assertTrue(actualMedications.containsKey(Constants.MEDICATION_LIST));
        assertTrue(actualMedications.containsKey(Constants.TOTAL_COUNT));
        assertEquals(new ArrayList<>(), actualMedications.get(Constants.MEDICATION_LIST));
    }

    @Test
    void testDeleteMedicationById() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        requestDTO.setId(TestConstants.ONE);
        Medication medication = TestDataProvider.getMedication();
        medication.setDeleted(true);

        //when
        when(medicationRepository.findByIdAndIsDeletedFalseAndTenantId(requestDTO.getId(),
                requestDTO.getTenantId())).thenReturn(medication);
        when(medicationRepository.save(medication)).thenReturn(medication);

        //then
        Boolean actualMedication = medicationService.deleteMedicationById(requestDTO);
        assertNotNull(actualMedication);
        assertTrue(actualMedication);
    }

    @Test
    void testSearchMedications() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(TestConstants.MEDICATION_NAME,
                Constants.ZERO, TestConstants.TEN);
        List<Medication> medications = TestDataProvider.getMedications();
        UserDTO userDTO = TestDataProvider.getUserDTO();
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);

        //when
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        when(medicationRepository.searchMedications(requestDTO.getSearchTerm(),
                userDTO.getCountry().getId())).thenReturn(medications);

        //then
        List<Medication> actualMedications = medicationService.searchMedications(requestDTO);
        userContextHolder.close();
        assertNotNull(actualMedications);
        assertEquals(medications.get(0), actualMedications.get(0));
        assertEquals(medications.size(), actualMedications.size());
    }

    @Test
    void testGetOtherMedication() {
        //given
        Medication medication = TestDataProvider.getMedication();

        //when
        when(medicationRepository.getOtherMedication(TestConstants.ONE, Constants.OTHER,
                Constants.OTHER, Constants.OTHER, Constants.OTHER)).thenReturn(medication);

        //then
        Medication actualMedication = medicationService.getOtherMedication(TestConstants.ONE);
        assertNotNull(actualMedication);
        assertEquals(medication.getMedicationName(), actualMedication.getMedicationName());
        assertEquals(medication, actualMedication);
    }

    @Test
    void throwDataNotFoundException() {
        //given
        Medication medication = TestDataProvider.getMedication();
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);

        //when
        when(medicationRepository.getMedicationByFieldsAndTenantId(
                medication.getClassificationId(), medication.getBrandId(), medication.getDosageFormId(),
                medication.getCountryId(), medication.getMedicationName(), medication.getTenantId())).thenReturn(medication);
        when(medicationRepository
                .getMedicationByIdAndIsDeletedAndTenantIdAndIsActiveTrue(medication.getId(), false, medication.getTenantId())).thenReturn(null);
        when(medicationRepository.findByIdAndIsDeletedFalseAndTenantId(requestDTO.getId(),
                requestDTO.getTenantId())).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> medicationService.updateMedication(medication));
        assertThrows(DataNotFoundException.class, () -> medicationService.getMedicationById(requestDTO));
    }

    @Test
    void throwDataConflictException() {
        //given
        Medication medication = TestDataProvider.getMedication();
        Medication existingMedication = TestDataProvider.getMedication();
        existingMedication.setId(TestConstants.TWO);

        //when
        when(medicationRepository.getMedicationByFieldsAndTenantId(
                medication.getClassificationId(), medication.getBrandId(), medication.getDosageFormId(),
                medication.getCountryId(), medication.getMedicationName(), medication.getTenantId())).thenReturn(existingMedication);

        //then
        assertThrows(DataConflictException.class, () -> medicationService.updateMedication(medication));
    }

    @Test
    void throwDataNotAcceptableException() {
        //given
        Medication medicationValidation = new Medication();
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(null, Constants.ZERO, TestConstants.TEN);
        Medication medication = TestDataProvider.getMedication();
        medication.setId(TestConstants.ONE);
        medication.setCountryId(TestConstants.TWO);
        Medication existingMedication = TestDataProvider.getMedication();
        existingMedication.setMedicationName(TestConstants.SECOND_MEDICATION_NAME);
        Medication secondMedication = TestDataProvider.getMedication();
        secondMedication.setId(TestConstants.TWO);
        Medication existingSecondMedication = TestDataProvider.getMedication();
        existingSecondMedication.setId(TestConstants.TWO);
        existingSecondMedication.setCountryId(TestConstants.TWO);
        List<Medication> medications = new ArrayList<>();
        medications.add(medication);
        medications.add(medication);

        //when
        when(medicationRepository.getMedicationByFieldsAndTenantId(
                medication.getClassificationId(), medication.getBrandId(), medication.getDosageFormId(),
                medication.getCountryId(), medication.getMedicationName(), medication.getTenantId())).thenReturn(medication);
        when(medicationRepository
                .getMedicationByIdAndIsDeletedAndTenantIdAndIsActiveTrue(TestConstants.ONE, false, medication.getTenantId())).thenReturn(existingMedication);
        when(medicationRepository
                .getMedicationByIdAndIsDeletedAndTenantIdAndIsActiveTrue(TestConstants.TWO, false, medication.getTenantId())).thenReturn(existingSecondMedication);

        //then
        assertThrows(DataNotAcceptableException.class, () -> medicationService.getAllMedications(requestDTO));
        assertThrows(DataNotAcceptableException.class, () -> medicationService.deleteMedicationById(requestDTO));
        assertThrows(DataNotAcceptableException.class, () -> medicationService.searchMedications(requestDTO));
        assertThrows(DataNotAcceptableException.class, () -> medicationService.updateMedication(medication));
        assertThrows(DataNotAcceptableException.class, () -> medicationService.updateMedication(secondMedication));
        assertThrows(DataNotAcceptableException.class, () -> medicationService.updateMedication(medicationValidation));
        assertThrows(DataNotAcceptableException.class, () -> medicationService.addMedication(medications));
    }
}