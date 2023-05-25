package com.mdtlabs.coreplatform.spiceservice.patienttracker.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.ConfirmDiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MyPatientListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientFilterDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientSortDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchPatientListDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PatientTrackerMapper;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.repository.PatientTrackerRepository;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Patient Tracker Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 24, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatientTrackerServiceTest {

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    private PatientTrackerServiceImpl patientTrackerService;

    @Mock
    private PatientTrackerRepository patientTrackerRepository;

    @Mock
    private PatientTrackerMapper patientTrackerMapper;

    public static Stream<Arguments> SortValue() {
        return Stream.of(
                Arguments.of(1, "1L", null, null, null));
//                Arguments.of(1, "1L", 1L, Constants.BOOLEAN_TRUE, 4L),
//                Arguments.of(1, "1L", 1L, Constants.BOOLEAN_FALSE, null));
    }

    @Test
    void addOrUpdatePatientTracker() {
        // given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();

        // when
        when(patientTrackerRepository.save(patientTracker)).thenReturn(patientTracker);

        // then
        PatientTracker patientTrackerResponse = patientTrackerService.addOrUpdatePatientTracker(patientTracker);
        Assertions.assertEquals(patientTracker.getFirstName(), patientTrackerResponse.getFirstName());
    }

    @Test
    void BadRequestException() {
        // then
        assertThrows(BadRequestException.class, () -> patientTrackerService.addOrUpdatePatientTracker(null));
    }

    @Test
    void getPatientTrackerById() {
        // given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();

        // when
        when(patientTrackerRepository.findByIdAndIsDeleted(1L, Boolean.FALSE)).thenReturn(patientTracker);

        // then
        PatientTracker patientTrackerResponse = patientTrackerService.getPatientTrackerById(1L);
        Assertions.assertEquals(patientTracker.getPatientId(), patientTrackerResponse.getPatientId());
    }

    @Test
    void DataNotFoundException() {
        //when
        when(patientTrackerRepository.findByIdAndIsDeleted(1L, Boolean.FALSE)).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> patientTrackerService.getPatientTrackerById(1L));
    }

    @Test
    void updatePatientTrackerLabTestReferral() {
        // given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();

        // when
        when(patientTrackerRepository.findById(1L)).thenReturn(Optional.of(patientTracker));
        when(patientTrackerRepository.save(patientTracker)).thenReturn(patientTracker);

        // then
        patientTrackerService.updatePatientTrackerLabtestReferral(1L, 1L, Boolean.TRUE);
        verify(patientTrackerRepository, atLeastOnce()).save(patientTracker);
    }

    @Test
    void updatePatientTrackerForBpLog() {
        // given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        BpLog bpLog = TestDataProvider.getBpLog();

        // when
        when(patientTrackerRepository.findById(1L)).thenReturn(Optional.of(patientTracker));
        when(patientTrackerMapper.setPatientTrackerFromBpLog(bpLog, patientTracker)).thenReturn(patientTracker);
        when(patientTrackerRepository.save(patientTracker)).thenReturn(patientTracker);

        // then
        patientTrackerService.updatePatientTrackerForBpLog(1L, bpLog, new Date(2021 - 11 - 11));
        verify(patientTrackerRepository, atLeastOnce()).save(patientTracker);
    }

    @Test
    void updatePatientTrackerForGlucoseLog() {
        // given
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();

        // when
        when(patientTrackerRepository.save(patientTracker)).thenReturn(patientTracker);
        when(patientTrackerRepository.findById(1L)).thenReturn(Optional.of(patientTracker));
        when(patientTrackerMapper.setPatientTrackerFromGlucose(glucoseLog, patientTracker,
                new Date(2021 - 11 - 11))).thenReturn(patientTracker);

        // then
        patientTrackerService.updatePatientTrackerForGlucoseLog(1L, glucoseLog, new Date(2021 - 11 - 11));
        verify(patientTrackerRepository, atLeastOnce()).save(patientTracker);
    }

    @Test
    void findByNationalIdIgnoreCase() {
        // given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();

        //when
        when(patientTrackerRepository.findByNationalIdAndCountryIdAndIsDeleted("testNationalId".toUpperCase(),
                1L, Boolean.FALSE)).thenReturn(patientTracker);

        // then
        PatientTracker patientTrackerResponse = patientTrackerService
                .findByNationalIdIgnoreCaseAndCountryIdAndIsDeleted("testNationalId", 1L,
                        Boolean.FALSE);
        Assertions.assertNotNull(patientTrackerResponse);
        Assertions.assertEquals(patientTracker.getPatientId(),
                patientTrackerResponse.getPatientId());
        Assertions.assertEquals(patientTracker.getAge(), patientTrackerResponse.getAge());
    }

    @Test
    void updateRedRiskPatientStatus() {
        // given
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();

        // when
        when(patientTrackerRepository.findById(1L)).thenReturn(Optional.of(patientTracker));
        when(patientTrackerRepository.save(patientTracker)).thenReturn(patientTracker);

        // then
        patientTrackerService.updateRedRiskPatientStatus(1L, Boolean.TRUE);
        verify(patientTrackerRepository, atLeastOnce()).save(patientTracker);
    }

    @Test
    void updateConfirmDiagnosis() {
        // given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        ConfirmDiagnosisDTO confirmDiagnosisDTO = TestDataProvider.getConfirmDiagnosisDTO();

        // when
        when(patientTrackerRepository.findByIdAndIsDeleted(1L, Boolean.FALSE)).thenReturn(patientTracker);
        when(patientTrackerRepository.save(patientTracker)).thenReturn(patientTracker);

        // then
        ConfirmDiagnosisDTO confirmDiagnosisDTOResponse = patientTrackerService
                .updateConfirmDiagnosis(confirmDiagnosisDTO);
        Assertions.assertEquals(confirmDiagnosisDTO.getId(), confirmDiagnosisDTOResponse.getId());
    }

    @Test
    void updateForFillPrescription() {
        // given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();

        // when
        when(patientTrackerRepository.findById(1L)).thenReturn(Optional.of(patientTracker));
        when(patientTrackerRepository.save(patientTracker)).thenReturn(patientTracker);

        // then
        patientTrackerService.updateForFillPrescription(1L, Boolean.TRUE, new Date(2022 - 11 - 11),
                new Date(2022 - 11 - 11));
        verify(patientTrackerRepository, atLeastOnce()).save(patientTracker);
    }

    @Test
    void getPatientTrackerByIdAndStatus() {
        // given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();

        // when
        when(patientTrackerRepository.getPatientTracker(1L, "testStatus", 1L))
                .thenReturn(patientTracker);

        // then
        PatientTracker patientTrackerResponse = patientTrackerService.getPatientTrackerByIdAndStatus(1L,
                1L, "testStatus");
        Assertions.assertEquals(patientTracker.getPatientId(), patientTrackerResponse.getPatientId());
    }

    @Test
    void listMyPatients() {
        // given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        patientRequestDTO.setPatientSort(TestDataProvider.getPatientSortDTO());
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.IS_RED_RISK_PATIENT));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.LAST_ASSESSMENT_DATE));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.NEXT_MEDICAL_REVIEW_DATE));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.AVG_SYSTOLIC));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.GLUCOSE_VALUE));
        sorts.add(new Sort.Order(Sort.Direction.ASC, Constants.NEXT_BP_ASSESSMENT_DATE));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.UPDATED_AT));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.CVD_RISK_SCORE));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.ID));
        Pageable pageable = Pagination.setPagination(0, 10,
                sorts);
        MyPatientListDTO myPatientListDTO = new MyPatientListDTO();
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerPage.stream().toList(), new TypeToken<List<MyPatientListDTO>>() {
        }.getType())).thenReturn(List.of(myPatientListDTO));
        when(patientTrackerRepository.getPatientsListWithPagination(
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), 4L, pageable)).thenReturn(patientTrackerPage);
        // then
        ResponseListDTO response = patientTrackerService.listMyPatients(patientRequestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getTotalCount());
        Assertions.assertEquals(Constants.ONE, response.getData().size());
    }

    @Test
    void listMyPatientsWithFilter() {
        // given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        patientRequestDTO.setPatientSort(new PatientSortDTO());
        PatientFilterDTO patientFilterDTO = TestDataProvider.getPatientFilterDTO();
        patientRequestDTO.setPatientFilter(patientFilterDTO);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.UPDATED_AT));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.ID));
        Pageable pageable = Pagination.setPagination(0, 10,
                sorts);
        MyPatientListDTO myPatientListDTO = new MyPatientListDTO();
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));
        filterMap.put(Constants.PATIENT_STATUS_NOT_ENROLLED, Constants.ENROLLED);
        filterMap.put(Constants.PATIENT_STATUS_NOT_SCREENED, Constants.SCREENED);
        // when
        when(patientTrackerRepository.getPatientsListWithPagination(
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), 4L, pageable))
                .thenReturn(patientTrackerPage);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerPage.stream().toList(), new TypeToken<List<MyPatientListDTO>>() {
        }.getType())).thenReturn(List.of(myPatientListDTO));

        // then
        ResponseListDTO response = patientTrackerService.listMyPatients(patientRequestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getTotalCount());
        Assertions.assertEquals(Constants.ONE, response.getData().size());
    }

    @Test
    void listMyPatientsWithFilterAsc() {
        // given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        PatientFilterDTO patientFilterDTO = TestDataProvider.getPatientFilterDTO();
        patientFilterDTO.setScreeningReferral(true);
        patientFilterDTO.setPatientStatus(Constants.ENROLLED);
        patientFilterDTO.setMedicalReviewDate(null);
        patientFilterDTO.setAssessmentDate(Constants.TOMORROW);
        patientRequestDTO.setPatientFilter(patientFilterDTO);
        Page<PatientTracker> patientTrackerList = null;
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
        MyPatientListDTO myPatientListDTO = new MyPatientListDTO();
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));
        filterMap.put(Constants.PATIENT_STATUS_ENROLLED, Constants.ENROLLED);
        filterMap.put(Constants.PATIENT_STATUS_NOT_SCREENED, Constants.SCREENED);
        Map<String, String> dateMap = patientTrackerService.getTodayAndTomorrowDate();
        filterMap.put(Constants.ASSESSMENT_START_DATE, dateMap.get(Constants.TOMORROW_START_DATE));
        filterMap.put(Constants.ASSESSMENT_END_DATE, dateMap.get(Constants.TOMORROW_END_DATE));
        patientRequestDTO.getPatientFilter().setCvdRiskLevel(Constants.CVD_RISK_LOW);

        // when
        when(patientTrackerRepository.getPatientsListWithPagination(
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), 4L, pageable))
                .thenReturn(patientTrackerPage);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerPage.stream().toList(), new TypeToken<List<MyPatientListDTO>>() {
        }.getType())).thenReturn(List.of(myPatientListDTO));

        // then
        ResponseListDTO response = patientTrackerService.listMyPatients(patientRequestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getTotalCount());
        Assertions.assertEquals(Constants.ONE, response.getData().size());
    }

    //    @Test
    void listMyPatientsWithFilterAscNull() {
        // given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        patientRequestDTO.setPatientSort(TestDataProvider.getPatientSortDTO());
        PatientFilterDTO patientFilterDTO = TestDataProvider.getPatientFilterDTO();
        patientFilterDTO.setScreeningReferral(null);
        patientFilterDTO.setPatientStatus(null);
        patientFilterDTO.setMedicalReviewDate(Constants.TOMORROW);
        patientFilterDTO.setAssessmentDate(Constants.TOMORROW);
        patientRequestDTO.setPatientFilter(patientFilterDTO);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setTenantId(4L);
        patientTracker.setMedicationPrescribed(true);
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
        MyPatientListDTO myPatientListDTO = new MyPatientListDTO();
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));
        Map<String, String> dateMap = patientTrackerService.getTodayAndTomorrowDate();
        filterMap.put(Constants.MEDICAL_REVIEW_START_DATE, dateMap.get(Constants.TOMORROW_START_DATE));
        filterMap.put(Constants.MEDICAL_REVIEW_END_DATE, dateMap.get(Constants.TOMORROW_END_DATE));
        filterMap.put(Constants.ASSESSMENT_START_DATE, dateMap.get(Constants.TOMORROW_START_DATE));
        filterMap.put(Constants.ASSESSMENT_END_DATE, dateMap.get(Constants.TOMORROW_END_DATE));

        // when
        when(patientTrackerRepository.getPatientsListWithPagination(
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), 4L, pageable))
                .thenReturn(patientTrackerPage);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerPage.stream().toList(), new TypeToken<List<MyPatientListDTO>>() {
        }.getType())).thenReturn(List.of(myPatientListDTO));

        // then
        ResponseListDTO response = patientTrackerService.listMyPatients(patientRequestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getTotalCount());
        Assertions.assertEquals(Constants.ONE, response.getData().size());
    }

    //@Test
    void listMyPatientsWithFilterAscFalse() {
        // given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        patientRequestDTO.setPatientSort(new PatientSortDTO());
        PatientFilterDTO patientFilterDTO = TestDataProvider.getPatientFilterDTO();
        patientFilterDTO.setScreeningReferral(false);
        patientFilterDTO.setMedicalReviewDate(Constants.TODAY);
        patientFilterDTO.setAssessmentDate(Constants.TODAY);
        patientRequestDTO.setPatientFilter(patientFilterDTO);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
        MyPatientListDTO myPatientListDTO = new MyPatientListDTO();
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));
        filterMap.put(Constants.PATIENT_STATUS_NOT_ENROLLED, Constants.ENROLLED);
        Map<String, String> dateMap = patientTrackerService.getTodayAndTomorrowDate();
        filterMap.put(Constants.MEDICAL_REVIEW_START_DATE, dateMap.get(Constants.TODAY_START_DATE));
        filterMap.put(Constants.MEDICAL_REVIEW_END_DATE, dateMap.get(Constants.TODAY_END_DATE));
        filterMap.put(Constants.ASSESSMENT_START_DATE, dateMap.get(Constants.TODAY_START_DATE));
        filterMap.put(Constants.ASSESSMENT_END_DATE, dateMap.get(Constants.TODAY_END_DATE));

        // when
        when(patientTrackerRepository.getPatientsListWithPagination(
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), 4L, pageable))
                .thenReturn(patientTrackerPage);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerPage.stream().toList(), new TypeToken<List<MyPatientListDTO>>() {
        }.getType())).thenReturn(List.of(myPatientListDTO));

        // then
        ResponseListDTO response = patientTrackerService.listMyPatients(patientRequestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getTotalCount());
        Assertions.assertEquals(Constants.ONE, response.getData().size());
    }

    @Test
    void listMyPatientsWithSort() {
        // given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        PatientSortDTO patientSortDTO = new PatientSortDTO();
        MyPatientListDTO myPatientListDTO = new MyPatientListDTO();
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.UPDATED_AT));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.ID));
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        Pageable pageable = Pagination.setPagination(0, 10,
                sorts);
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));
        patientRequestDTO.setPatientSort(patientSortDTO);

        // when
        when(patientTrackerRepository.getPatientsListWithPagination(
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), 4L, pageable))
                .thenReturn(patientTrackerPage);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerPage.stream().toList(), new TypeToken<List<MyPatientListDTO>>() {
        }.getType())).thenReturn(List.of(myPatientListDTO));

        // then
        ResponseListDTO response = patientTrackerService.listMyPatients(patientRequestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getTotalCount());
        Assertions.assertEquals(Constants.ONE, response.getData().size());
    }

    @Test
    void listMyPatientsWithSortAsc() {
        // given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        PatientSortDTO patientSortDTO = TestDataProvider.getPatientSortDTO();
        MyPatientListDTO myPatientListDTO = new MyPatientListDTO();
        patientSortDTO.setIsCvdRisk(Constants.BOOLEAN_FALSE);
        patientSortDTO.setIsRedRisk(Constants.BOOLEAN_FALSE);
        patientSortDTO.setIsLatestAssessment(Constants.BOOLEAN_FALSE);
        patientSortDTO.setIsMedicalReviewDueDate(Constants.BOOLEAN_FALSE);
        patientSortDTO.setIsHighLowBp(Constants.BOOLEAN_FALSE);
        patientSortDTO.setIsHighLowBg(Constants.BOOLEAN_TRUE);
        patientSortDTO.setIsAssessmentDueDate(Constants.BOOLEAN_FALSE);
        patientSortDTO.setIsUpdated(Constants.BOOLEAN_FALSE);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.ASC, Constants.IS_RED_RISK_PATIENT));
        sorts.add(new Sort.Order(Sort.Direction.ASC, Constants.LAST_ASSESSMENT_DATE));
        sorts.add(new Sort.Order(Sort.Direction.ASC, Constants.NEXT_MEDICAL_REVIEW_DATE));
        sorts.add(new Sort.Order(Sort.Direction.ASC, Constants.AVG_SYSTOLIC));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.GLUCOSE_VALUE));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.NEXT_BP_ASSESSMENT_DATE));
        sorts.add(new Sort.Order(Sort.Direction.ASC, Constants.UPDATED_AT));
        sorts.add(new Sort.Order(Sort.Direction.ASC, Constants.CVD_RISK_SCORE));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.ID));

        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        Pageable pageable = Pagination.setPagination(0, 10,
                sorts);
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));
        patientRequestDTO.setPatientSort(patientSortDTO);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerPage.stream().toList(), new TypeToken<List<MyPatientListDTO>>() {
        }.getType())).thenReturn(List.of(myPatientListDTO));
        when(patientTrackerRepository.getPatientsListWithPagination(
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), 4L, pageable)).thenReturn(patientTrackerPage);
        // then
        ResponseListDTO response = patientTrackerService.listMyPatients(patientRequestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getTotalCount());
        Assertions.assertEquals(Constants.ONE, response.getData().size());
    }

    @Test
    void listMyPatientsWithSortNull() {
        // given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        patientRequestDTO.setSkip(Constants.ZERO);
        PatientSortDTO patientSortDTO = TestDataProvider.getPatientSortDTO();
        MyPatientListDTO myPatientListDTO = new MyPatientListDTO();
        patientSortDTO.setIsCvdRisk(null);
        patientSortDTO.setIsRedRisk(null);
        patientSortDTO.setIsLatestAssessment(null);
        patientSortDTO.setIsMedicalReviewDueDate(null);
        patientSortDTO.setIsHighLowBp(null);
        patientSortDTO.setIsHighLowBg(null);
        patientSortDTO.setIsAssessmentDueDate(null);
        patientSortDTO.setIsUpdated(null);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.UPDATED_AT));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.ID));
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        Pageable pageable = Pagination.setPagination(0, 0,
                sorts);
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));
        patientRequestDTO.setPatientSort(patientSortDTO);

        // when
        when(patientTrackerRepository.getPatientsListWithPagination(
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), 4L, pageable))
                .thenReturn(patientTrackerPage);

        // when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerPage.stream().toList(), new TypeToken<List<MyPatientListDTO>>() {
        }.getType())).thenReturn(List.of(myPatientListDTO));

        // then
        ResponseListDTO response = patientTrackerService.listMyPatients(patientRequestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.ONE, response.getTotalCount());
        Assertions.assertEquals(Constants.ONE, response.getData().size());
    }

    @Test
    void searchPatientsIdNullTest() {
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();

        patientRequestDTO.setSkip(Constants.ZERO);
        patientRequestDTO.setSearchId("1");
        patientRequestDTO.setIsSearchUserOrgPatient(true);
        patientRequestDTO.setTenantId(1L);
        patientRequestDTO.setOperatingUnitId(1L);
        PatientFilterDTO patientFilterDTO = TestDataProvider.getPatientFilterDTO();
        patientFilterDTO.setScreeningReferral(false);
        patientFilterDTO.setMedicalReviewDate(Constants.TODAY);
        patientFilterDTO.setAssessmentDate(Constants.TODAY);
        patientFilterDTO.setCvdRiskLevel("LOW");
        patientFilterDTO.setIsRedRiskPatient(true);

        patientRequestDTO.setPatientFilter(patientFilterDTO);

        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        List<PatientTracker> patientTrackerList = List.of(patientTracker);

        Pageable pageable = PageRequest.of(0, 10);
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(patientTrackerList);
        when(patientTrackerRepository.searchPatientsWithPagination(
                anyLong(), anyLong(),
                any(), any(),
                any(), any(),
                any(), any(),
                any(), any(),
                any(), any(),
                any(), any(),
                any(), any(),
                any(), any(),
                any(), anyLong(),
                any())).thenReturn(patientTrackerPage);

        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(any(), any())).thenReturn(patientTrackerList);

        patientTrackerService.searchPatients(patientRequestDTO);

        patientRequestDTO.setSearchId(null);
        assertThrows(DataNotAcceptableException.class, () -> patientTrackerService.searchPatients(patientRequestDTO));
    }

    //@ParameterizedTest
    @MethodSource("SortValue")
    void searchPatientsTest(int sort, String searchId, Long programId, Boolean isOrg, Long tenantId) {
        //given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        patientRequestDTO.setPatientSort(new PatientSortDTO());
        patientRequestDTO.setIsSearchUserOrgPatient(isOrg);
        patientRequestDTO.setSearchId(searchId);
        patientRequestDTO.setSkip(sort);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        List<PatientTracker> patientTrackerList = List.of(patientTracker);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.UPDATED_AT));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.ID));
        Pageable pageable = Pagination.setPagination(0, 0,
                sorts);
        //when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerList.stream().toList(), new TypeToken<List<SearchPatientListDTO>>() {
        }.getType())).thenReturn(null);
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));

        when(patientTrackerRepository.searchPatientsWithPagination(tenantId, patientRequestDTO.getOperatingUnitId(),
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), "1L", programId, pageable))
                .thenReturn(patientTrackerPage);

        //then
        patientTrackerService.searchPatients(patientRequestDTO);
    }

    //    @ParameterizedTest
    @CsvSource({"null", "LOW"})
    void searchPatientsTestWithFilter(String cvdValue) {
        //given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        PatientFilterDTO patientFilterDTO = TestDataProvider.getPatientFilterDTO();
        patientRequestDTO.setPatientFilter(patientFilterDTO);
        PatientSortDTO patientSortDTO = new PatientSortDTO();
        patientRequestDTO.setPatientSort(patientSortDTO);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        List<PatientTracker> patientTrackerList = List.of(patientTracker);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.UPDATED_AT));
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.ID));
        patientRequestDTO.getPatientFilter().setIsRedRiskPatient(Constants.BOOLEAN_TRUE);
        patientRequestDTO.getPatientFilter().setCvdRiskLevel(cvdValue);
        Pageable pageable = Pagination.setPagination(0, 10,
                sorts);

        //when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerList.stream().toList(), new TypeToken<List<SearchPatientListDTO>>() {
        }.getType())).thenReturn(null);
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));
        when(patientTrackerRepository.searchPatientsWithPagination(4L, patientRequestDTO.getOperatingUnitId(),
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                patientRequestDTO.getIsLabtestReferred(), patientRequestDTO.getIsMedicationPrescribed(),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), "1L", 1L, pageable))
                .thenReturn(patientTrackerPage);

        //then
        patientTrackerService.searchPatients(patientRequestDTO);
    }

    @Test
    void patientAdvanceSearchFailedTest() {
        //given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        patientRequestDTO.setFirstName("token");
        patientRequestDTO.setLastName("token");
        patientRequestDTO.setPhoneNumber("token");
        MockedStatic<CommonUtil> commonUtil = mockStatic(CommonUtil.class);
        //when
        commonUtil.when(() -> CommonUtil.validatePatientSearchData(Arrays.asList(patientRequestDTO.getFirstName(),
                        patientRequestDTO.getLastName(), patientRequestDTO.getPhoneNumber())))
                .thenReturn(Constants.BOOLEAN_TRUE);

        //then
        assertThrows(DataNotAcceptableException.class, () -> patientTrackerService.patientAdvanceSearch(patientRequestDTO));
        commonUtil.close();
    }

    //    @Test
    void patientAdvanceSearch() {
        //given
        PatientRequestDTO patientRequestDTO = TestDataProvider.getPatientRequestDTO();
        patientRequestDTO.setPatientSort(new PatientSortDTO());
        patientRequestDTO.setFirstName("token");
        patientRequestDTO.setLastName("token");
        patientRequestDTO.setPhoneNumber("token");
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        List<PatientTracker> patientTrackerList = List.of(patientTracker);
        List<MyPatientListDTO> myPatientListDTOS = List.of(TestDataProvider.getMyPatientListDTO());
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

        TestDataProvider.init();
        //when
        TestDataProvider.getStaticMock();
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(patientTrackerList.stream().toList(), new TypeToken<List<MyPatientListDTO>>() {
        }.getType())).thenReturn(myPatientListDTOS);
        Map<String, String> filterMap = new HashMap<>();
        Page<PatientTracker> patientTrackerPage = new PageImpl<>(List.of(patientTracker));
        when(patientTrackerRepository.getPatientsWithAdvanceSearch(
                patientRequestDTO.getFirstName().toUpperCase(), patientRequestDTO.getLastName().toUpperCase(), patientRequestDTO.getPhoneNumber(),
                filterMap.get(Constants.MEDICAL_REVIEW_START_DATE), filterMap.get(Constants.MEDICAL_REVIEW_END_DATE),
                filterMap.get(Constants.ASSESSMENT_START_DATE), filterMap.get(Constants.ASSESSMENT_END_DATE),
                filterMap.get(Constants.MEDICATION_PRESCRIBED_START_DATE), filterMap.get(Constants.MEDICATION_PRESCRIBED_END_DATE),
                filterMap.get(Constants.LABTEST_REFERRED_START_DATE), filterMap.get(Constants.LABTEST_REFERRED_END_DATE),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getIsRedRiskPatient() : null),
                (!filterMap.isEmpty() && !Objects.isNull(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        ? Constants.CVD_RISK.get(patientRequestDTO.getPatientFilter().getCvdRiskLevel())
                        : null),
                (!filterMap.isEmpty() ? patientRequestDTO.getPatientFilter().getScreeningReferral() : null),
                filterMap.get(Constants.PATIENT_STATUS_NOT_SCREENED), filterMap.get(Constants.PATIENT_STATUS_ENROLLED),
                filterMap.get(Constants.PATIENT_STATUS_NOT_ENROLLED), patientRequestDTO.getIsLabtestReferred(),
                patientRequestDTO.getIsMedicationPrescribed(), 1L, patientRequestDTO.getOperatingUnitId(),
                Constants.BOOLEAN_FALSE, 4L, pageable)).thenReturn(patientTrackerPage);

        //then
        patientTrackerService.patientAdvanceSearch(patientRequestDTO);
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("GetLabTestDetails Test")
    void getLabTestDetails() {
        PatientFilterDTO patientFilterDTO = TestDataProvider.getPatientFilterDTO();
        patientFilterDTO.setLabTestReferredDate(Constants.YESTERDAY);
        Map<String, String> dateMap = patientTrackerService.getTodayAndTomorrowDate();
        Map<String, String> map = new HashMap<>();

        PatientTrackerServiceImpl.getLabTestDetails(patientFilterDTO, map, dateMap);
        assertNotNull(map.get(Constants.LABTEST_REFERRED_START_DATE));

        patientFilterDTO.setLabTestReferredDate(Constants.TODAY);
        PatientTrackerServiceImpl.getLabTestDetails(patientFilterDTO, map, dateMap);
        assertNotNull(map.get(Constants.LABTEST_REFERRED_START_DATE));
    }

    @Test
    @DisplayName("GetMedicationDetails Test")
    void getMedicationDetails() {
        PatientFilterDTO patientFilterDTO = TestDataProvider.getPatientFilterDTO();
        patientFilterDTO.setMedicationPrescribedDate(Constants.YESTERDAY);
        Map<String, String> dateMap = patientTrackerService.getTodayAndTomorrowDate();
        Map<String, String> map = new HashMap<>();

        PatientTrackerServiceImpl.getMedicationDetails(patientFilterDTO, map, dateMap);
        assertNotNull(map.get(Constants.MEDICATION_PRESCRIBED_START_DATE));

        patientFilterDTO.setMedicationPrescribedDate(Constants.TODAY);
        PatientTrackerServiceImpl.getMedicationDetails(patientFilterDTO, map, dateMap);
        assertNotNull(map.get(Constants.MEDICATION_PRESCRIBED_START_DATE));
    }

}
