package com.mdtlabs.coreplatform.spiceservice.prescription.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionHistoryResponse;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;
import com.mdtlabs.coreplatform.common.util.DateUtil;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PrescriptionMapper;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;
import com.mdtlabs.coreplatform.spiceservice.prescription.repository.PrescriptionHistoryRepository;
import com.mdtlabs.coreplatform.spiceservice.prescription.repository.PrescriptionRepository;
import com.mdtlabs.coreplatform.spiceservice.prescription.service.impl.PrescriptionServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * PrescriptionServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in
 * PrescriptionServiceImpl class.
 * </p>
 *
 * @author Jaganathan created on Feb 8, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PrescriptionServiceImplTest {

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private PrescriptionHistoryRepository historyRepository;

    @Mock
    private PrescriptionMapper prescriptionMapper;

    @Mock
    private PatientVisitService patientVisitService;

    @Mock
    private PatientTrackerService patientTrackerService;

    @Mock
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Mock
    private ModelMapper mapper;

    private Prescription prescription;

    private PrescriptionRequestDTO prescriptionRequestDTO;

    private List<Prescription> prescriptions = new ArrayList<>();

    private List<PrescriptionHistory> prescriptionHistories = new ArrayList<>();

    @Mock
    private PrescriptionRequestDTO prescriptionRequestDTOMocked;

    public static Stream<Arguments> prescriptionHistoryData() {
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        return Stream.of(
                Arguments.of(Constants.BOOLEAN_TRUE, List.of(patientVisit), null, Constants.ZERO),
                Arguments.of(Constants.BOOLEAN_TRUE, null, 1L, Constants.ONE),
                Arguments.of(Constants.BOOLEAN_TRUE, List.of(), null, Constants.ONE),
                Arguments.of(Constants.BOOLEAN_FALSE, List.of(patientVisit), 1L, Constants.ONE)
        );
    }

    @Test
    @DisplayName("createOrUpdatePrescription Test")
    void createOrUpdatePrescription() {
        //given
        prescriptionRequestDTO = null;

        //then
        Assertions.assertThrows(BadRequestException.class,
                () -> prescriptionService.createOrUpdatePrescription(prescriptionRequestDTO));

        Assertions.assertThrows(NullPointerException.class, () -> prescriptionService.createOrUpdatePrescription(prescriptionRequestDTOMocked));


    }

    @Test
    @DisplayName("GetPrescription Test")
    void getPrescription() {
        //given
        Long patientVisitId = 0L;

        //when
        when(historyRepository.getPrescriptions(patientVisitId)).thenReturn(prescriptionHistories);

        //then
        List<PrescriptionHistory> actualList = prescriptionService.getPrescriptions(patientVisitId);
        Assertions.assertTrue(prescriptionHistories.isEmpty());
        Assertions.assertEquals(prescriptionHistories, actualList);

        //given
        patientVisitId = 1L;
        prescriptionHistories = List.of(TestDataProvider.getPrescriptionHistory());

        //when
        when(historyRepository.getPrescriptions(patientVisitId)).thenReturn(prescriptionHistories);

        //then
        actualList = prescriptionService.getPrescriptions(patientVisitId);
        Assertions.assertNotNull(prescriptionHistories);
        Assertions.assertEquals(prescriptionHistories, actualList);
        Assertions.assertEquals(prescriptionHistories.size(), actualList.size());
        Assertions.assertEquals(prescriptionHistories.get(0), actualList.get(0));
    }

    @Test
    @DisplayName("GetPrescriptionCount Test")
    void getPrescriptionCount() {
        //given
        Date endDate = new Date();
        Long patientTrackId = 0L;

        //when
        when(prescriptionRepository.getPrecriptionCount(endDate, patientTrackId)).thenReturn(0);

        //then
        int actualCount = prescriptionService.getPrescriptionCount(endDate, patientTrackId);
        Assertions.assertEquals(0, actualCount);

        //given
        endDate = new Date(12 / 12 / 22);
        patientTrackId = 1L;

        //when
        when(prescriptionRepository.getPrecriptionCount(endDate, patientTrackId)).thenReturn(10);

        //then
        actualCount = prescriptionService.getPrescriptionCount(endDate, patientTrackId);
        Assertions.assertEquals(10, actualCount);
    }

    @Test
    @DisplayName("GetPrescriptionsBYTrackAndVisitId Test")
    void findByPatientTrackAndVisitId() {
        //given
        Long patientTrackId = 0L;
        Long patientVisitId = 0L;

        //when
        when(prescriptionRepository.findByPatientTrackIdAndPatientVisitIdAndIsDeleted(patientTrackId,
                patientVisitId, false)).thenReturn(prescriptions);

        //then
        List<Prescription> actualPrescriptions = prescriptionService
                .findByPatientTrackIdAndPatientVisitIdAndIsDeleted(patientTrackId,
                        patientVisitId, false);
        Assertions.assertTrue(actualPrescriptions.isEmpty());
        Assertions.assertEquals(prescriptions.size(), actualPrescriptions.size());

        //given
        prescriptions = List.of(TestDataProvider.getPrescription());
        patientVisitId = 1L;
        patientTrackId = 1L;

        //when
        when(prescriptionRepository.findByPatientTrackIdAndPatientVisitIdAndIsDeleted(patientTrackId,
                patientVisitId, false)).thenReturn(prescriptions);

        //then
        actualPrescriptions = prescriptionService.findByPatientTrackIdAndPatientVisitIdAndIsDeleted(patientTrackId,
                patientVisitId, false);
        Assertions.assertFalse(actualPrescriptions.isEmpty());
        Assertions.assertEquals(prescriptions.size(), actualPrescriptions.size());
        Assertions.assertEquals(prescriptions.get(0).getPatientTrackId(),
                actualPrescriptions.get(0).getPatientTrackId());
        Assertions.assertEquals(prescriptions.get(0).getPatientVisitId(),
                actualPrescriptions.get(0).getPatientVisitId());
    }

    @Test
    @DisplayName("GetRefillPrescriptionHistory Test")
    void getRefillPrescriptionHistory() {
        //given
        prescriptionHistories = List.of(TestDataProvider.getPrescriptionHistory());
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto();

        //when
        when(historyRepository.getFillPrescriptionHistory(searchRequestDTO.getPatientTrackId(),
                searchRequestDTO.getLastRefillVisitId(), Constants.ZERO)).thenReturn(prescriptionHistories);

        //then
        List<PrescriptionHistory> actualList = prescriptionService
                .getRefillPrescriptionHistory(searchRequestDTO);
        Assertions.assertEquals(prescriptionHistories, actualList);
        Assertions.assertEquals(prescriptionHistories.get(0).getPrescriptionId(),
                actualList.get(0).getPrescriptionId());
        Assertions.assertEquals(prescriptionHistories.get(0).getPatientVisitId(),
                actualList.get(0).getPatientVisitId());
    }

    @ParameterizedTest
    @DisplayName("GetRefillPrescriptionHistoryNull Test")
    @CsvSource({" , ", "1, ", ", 1"})
    void getRefillPrescriptionHistoryNull(Long patientVisitId, Long patientTrackId) {
        //given
        SearchRequestDTO searchRequestDTO = new SearchRequestDTO();
        searchRequestDTO.setPatientTrackId(patientVisitId);
        searchRequestDTO.setPatientTrackId(patientTrackId);

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.getRefillPrescriptionHistory(searchRequestDTO));

    }

    @ParameterizedTest
    @DisplayName("UpdateFillPrescription Test")
    @CsvSource({",", "1,", ", 1"})
    void updateFillPrescription(Long patientVisitId, Long patientTrackId) {
        //given
        FillPrescriptionRequestDTO fillPrescriptionRequestDTO = new FillPrescriptionRequestDTO();
        fillPrescriptionRequestDTO.setPatientTrackId(patientTrackId);
        fillPrescriptionRequestDTO.setPatientVisitId(patientVisitId);

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.updateFillPrescription(fillPrescriptionRequestDTO));
    }

    @Test
    @DisplayName("NonNullWithEmptyList Test")
    void testNonNullWithEmptyList() {
        //given
        FillPrescriptionRequestDTO fillPrescriptionRequestDTO = TestDataProvider.getPrescriptionResponseDTO();
        fillPrescriptionRequestDTO.setPrescriptions(new ArrayList<>());

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.updateFillPrescription(fillPrescriptionRequestDTO));
    }

    @Test
    @DisplayName("NonNullWithNullList Test")
    void testNonNullWithNullList() {
        //given
        FillPrescriptionRequestDTO fillPrescriptionRequestDTO = TestDataProvider.getPrescriptionResponseDTO();
        fillPrescriptionRequestDTO.setPrescriptions(List.of());

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.updateFillPrescription(fillPrescriptionRequestDTO));
    }

    @Test
    @DisplayName("NonNullWithNullList Test")
    void testNonNullWithNullLists() {
        //given
        FillPrescriptionRequestDTO fillPrescriptionRequestDTO = TestDataProvider.getPrescriptionResponseDTO();
        fillPrescriptionRequestDTO.setPrescriptions(null);

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.updateFillPrescription(fillPrescriptionRequestDTO));
    }

    @Test
    @DisplayName("UpdateFillPrescriptionWithNonNull Test")
    void updateFillPrescriptionWithNonNull() {
        //given
        prescription = TestDataProvider.getPrescription();
        prescription.setRemainingPrescriptionDays(5);
        prescription.setPrescriptionFilledDays(5);
        FillPrescriptionRequestDTO fillPrescriptionRequestDTO = TestDataProvider.getPrescriptionResponseDTO();
        PrescriptionHistory prescriptionHistories = TestDataProvider.getPrescriptionHistory();
        prescriptions = List.of(prescription);
        List<Long> prescriptIds = List.of(fillPrescriptionRequestDTO.getId());
        fillPrescriptionRequestDTO.setPrescriptions(prescriptions);

        //when
        when(prescriptionRepository.getActivePrescriptions(prescriptIds, Constants.BOOLEAN_FALSE))
                .thenReturn(prescriptions);
        when(prescriptionRepository.saveAll(prescriptions)).thenReturn(prescriptions);
        when(mapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(prescriptionMapper.setPrescription(prescription, prescription, fillPrescriptionRequestDTO))
                .thenReturn(prescription);
        when(mapper.map(List.of(prescription), new TypeToken<List<PrescriptionHistory>>() {
        }.getType())).thenReturn(List.of(prescriptionHistories));
        when(prescriptionRepository
                .findByPatientTrackIdAndRemainingPrescriptionDaysGreaterThanAndIsDeletedFalse(fillPrescriptionRequestDTO
                        .getPatientTrackId(), Constants.ZERO)).thenReturn(new ArrayList<>());
        doNothing().when(patientTrackerService).updateForFillPrescription(fillPrescriptionRequestDTO
                .getPatientTrackId(), Constants.BOOLEAN_FALSE, null, null);

        //then
        prescriptionService.updateFillPrescription(fillPrescriptionRequestDTO);
        Assertions.assertNotNull(prescription);
    }

    @Test
    @DisplayName("NonNullWithNullList Test")
    void testNonNullWithoutNullDate() {
        //given
        prescription = TestDataProvider.getPrescription();
        prescription.setRemainingPrescriptionDays(5);
        FillPrescriptionRequestDTO fillPrescriptionRequestDTO = TestDataProvider.getPrescriptionResponseDTO();
        PrescriptionHistory prescriptionHistories = TestDataProvider.getPrescriptionHistory();
        prescription.setPrescriptionFilledDays(11);
        prescriptions = List.of(prescription);
        List<Long> prescriptIds = List.of(fillPrescriptionRequestDTO.getId());
        fillPrescriptionRequestDTO.setPrescriptions(prescriptions);

        //when
        when(prescriptionRepository.getActivePrescriptions(prescriptIds, Constants.BOOLEAN_FALSE))
                .thenReturn(prescriptions);
        when(prescriptionRepository.saveAll(prescriptions)).thenReturn(prescriptions);
        when(mapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(prescriptionMapper.setPrescription(prescription, prescription, fillPrescriptionRequestDTO))
                .thenReturn(prescription);
        when(mapper.map(List.of(prescription), new TypeToken<List<PrescriptionHistory>>() {
        }.getType())).thenReturn(List.of(prescriptionHistories));
        when(prescriptionRepository
                .findByPatientTrackIdAndRemainingPrescriptionDaysGreaterThanAndIsDeletedFalse(fillPrescriptionRequestDTO
                        .getPatientTrackId(), Constants.ZERO)).thenReturn(new ArrayList<>());
        doNothing().when(patientTrackerService).updateForFillPrescription(fillPrescriptionRequestDTO
                .getPatientTrackId(), Constants.BOOLEAN_FALSE, null, null);

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.updateFillPrescription(fillPrescriptionRequestDTO));
    }

    @Test
    @DisplayName("UpdateFillPrescriptionWithNonNull Test")
    void updateFillPrescriptionWithNonNullEmpty() {
        //given
        prescription = TestDataProvider.getPrescription();
        prescription.setRemainingPrescriptionDays(5);
        prescription.setPrescriptionFilledDays(5);
        FillPrescriptionRequestDTO fillPrescriptionRequestDTO = TestDataProvider.getPrescriptionResponseDTO();
        PrescriptionHistory prescriptionHistories = TestDataProvider.getPrescriptionHistory();
        prescriptions = List.of(prescription);
        List<Long> prescriptIds = List.of(fillPrescriptionRequestDTO.getId());
        fillPrescriptionRequestDTO.setPrescriptions(prescriptions);

        //when
        when(prescriptionRepository.getActivePrescriptions(prescriptIds, Constants.BOOLEAN_FALSE))
                .thenReturn(prescriptions);
        when(prescriptionRepository.saveAll(prescriptions)).thenReturn(prescriptions);
        when(mapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(prescriptionMapper.setPrescription(prescription, prescription, fillPrescriptionRequestDTO))
                .thenReturn(prescription);
        when(mapper.map(List.of(prescription), new TypeToken<List<PrescriptionHistory>>() {
        }.getType())).thenReturn(List.of(prescriptionHistories));
        when(prescriptionRepository
                .findByPatientTrackIdAndRemainingPrescriptionDaysGreaterThanAndIsDeletedFalse(fillPrescriptionRequestDTO
                        .getPatientTrackId(), Constants.ZERO)).thenReturn(List.of(prescription));
        doNothing().when(patientTrackerService).updateForFillPrescription(fillPrescriptionRequestDTO
                        .getPatientTrackId(), Constants.BOOLEAN_FALSE,
                null, null);

        //then
        prescriptionService.updateFillPrescription(fillPrescriptionRequestDTO);
        Assertions.assertNotNull(prescription);
    }

    @Test
    @DisplayName("GetFillPrescriptionsWithInvalidId Test")
    void testGetFillPrescriptionsWithInvalidId() {
        //given
        SearchRequestDTO requestDTO = new SearchRequestDTO();

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.getFillPrescriptions(requestDTO));
    }

    @Test
    @DisplayName("GetFillPrescriptionsWithValidId Test")
    void testGetFillPrescriptions() {
        //given
        List<FillPrescriptionResponseDTO> actualList;
        prescription = TestDataProvider.getPrescription();
        FillPrescriptionResponseDTO responseDTO = TestDataProvider.getFillPrescriptionDTO();
        responseDTO.setRemainingPrescriptionDays(0);
        List<FillPrescriptionResponseDTO> responseDTOList = List.of(responseDTO);
        prescriptions = List.of(prescription);
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto();

        //when
        when(prescriptionRepository.getRefillPrescriptions(searchRequestDTO.getPatientTrackId(),
                Constants.ZERO, Constants.BOOLEAN_FALSE)).thenReturn(prescriptions);
        when(mapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(mapper.map(prescriptions, new TypeToken<List<FillPrescriptionResponseDTO>>() {
        }.getType())).thenReturn(responseDTOList);

        //then
        actualList = prescriptionService.getFillPrescriptions(searchRequestDTO);
        Assertions.assertEquals(responseDTOList, actualList);
        Assertions.assertEquals(responseDTOList.get(0), actualList.get(0));
    }

    @Test
    @DisplayName("RemovePrescriptionWithNullVisitId Test")
    void removePrescriptionWithNullVisitId() {
        //given
        RequestDTO requestDTO = new RequestDTO();

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.removePrescription(requestDTO));
    }

    @Test
    @DisplayName("RemovePrescriptionWithNullTrackerId Test")
    void removePrescriptionWithNullTrackerId() {
        //given
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setPatientVisitId(1L);

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.removePrescription(requestDTO));
    }

    @Test
    @DisplayName("RemovePrescriptionWithValidData Test")
    void removePrescription() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        prescription = TestDataProvider.getPrescription();

        //when
        when(prescriptionRepository.findById(requestDTO.getId())).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(prescription)).thenReturn(prescription);

        //then
        Assertions.assertNotNull(requestDTO.getPatientVisitId());
        Assertions.assertNotNull(requestDTO.getPatientTrackId());

    }

    @Test
    void listPrescriptionHistoryDataNullTracker() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setPatientTrackId(null);

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.listPrescriptionHistoryData(requestDTO));
    }

    @ParameterizedTest
    @MethodSource("prescriptionHistoryData")
    void listPrescriptionHistoryData(Boolean isLatest, List<PatientVisit> patientVisits, Long visitId, int testValue) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setLatestRequired(isLatest);
        requestDTO.setPatientVisitId(visitId);

        //when
        when(patientVisitService.getPatientVisitDates(requestDTO.getPatientTrackId(), null,
                null, Constants.BOOLEAN_TRUE)).thenReturn(patientVisits);
        PrescriptionHistory prescriptionHistories = TestDataProvider.getPrescriptionHistory();
        when(historyRepository.getPrescriptionHistory(
                requestDTO.getPrescriptionId(), visitId,
                requestDTO.getPatientTrackId())).thenReturn(List.of(prescriptionHistories));

        //then
        PrescriptionHistoryResponse prescriptionHistoryResponse = prescriptionService
                .listPrescriptionHistoryData(requestDTO);
        Assertions.assertNotNull(prescriptionHistoryResponse);
        Assertions.assertEquals(testValue, prescriptionHistoryResponse.getPatientPrescription().size());
        Assertions.assertEquals(testValue == Constants.ZERO ? Constants.ONE : Constants.ZERO,
                prescriptionHistoryResponse.getPrescriptionHistoryDates().size());
    }

    @Test
    void removePrescriptionNull() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        prescription = TestDataProvider.getPrescription();
        Prescription prescriptionData = TestDataProvider.getPrescription();
        prescriptionData.setDeleted(Constants.BOOLEAN_TRUE);
        prescriptionData.setDiscontinuedOn(new Date());
        prescriptionData.setDiscontinuedReason(requestDTO.getDiscontinuedReason());
        prescriptionData.setTenantId(requestDTO.getTenantId());
        Date date = new Date();

        //when
        when(prescriptionRepository.findById(requestDTO.getId())).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(prescriptionData)).thenReturn(prescriptionData);
        when(patientTreatmentPlanService.getNextFollowUpDate(requestDTO.getPatientTrackId(),
                Constants.MEDICAL_REVIEW_FREQUENCY)).thenReturn(new Date());
        when(prescriptionRepository.findByPatientTrackIdAndRemainingPrescriptionDaysGreaterThanAndIsDeletedFalse(requestDTO
                .getPatientTrackId(), Constants.ZERO)).thenReturn(new ArrayList<>());
        doNothing().when(patientTrackerService).updateForFillPrescription(requestDTO.getPatientTrackId(),
                Constants.BOOLEAN_FALSE, date, date);

        //then
        prescriptionService.removePrescription(requestDTO);
        verify(prescriptionRepository, atLeastOnce())
                .findByPatientTrackIdAndRemainingPrescriptionDaysGreaterThanAndIsDeletedFalse(requestDTO.getPatientTrackId(),
                        Constants.ZERO);
    }

    @Test
    @DisplayName("GetPrescriptionsWithNull Test")
    void getPrescriptionsWithNull() {
        //given
        RequestDTO requestDTO = new RequestDTO();
        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> prescriptionService.getPrescriptions(requestDTO));
    }

    @Test
    @DisplayName("GetPrescriptions Test")
    void getPrescriptions() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setIsDeleted(Constants.BOOLEAN_TRUE);
        //when
        when(prescriptionRepository.findByPatientTrackIdAndIsDeleted(requestDTO.getPatientTrackId(),
                requestDTO.getIsDeleted())).thenReturn(prescriptions);
        //then
        List<PrescriptionDTO> actualPrescriptions = prescriptionService.getPrescriptions(requestDTO);
        Assertions.assertTrue(actualPrescriptions.isEmpty());
        //given
        prescription = TestDataProvider.getPrescription();
        prescription.setPrescribedDays(4);
        prescription.setEndDate(new Date(12 / 12 / 25));
        prescriptions = List.of(prescription);
        PrescriptionDTO prescriptionDTO = TestDataProvider.getPrescriptionDTO();
        prescriptionDTO.setPrescriptionRemainingDays(4);
        prescriptionDTO.setEndDate(new Date(12 / 12 / 25));
        prescriptionDTO.setPrescribedSince(new Date());
        MockedStatic<DateUtil> dateUtil = mockStatic(DateUtil.class);
        //when
        when(prescriptionRepository.findByPatientTrackIdAndIsDeleted(requestDTO.getPatientTrackId(),
                requestDTO.getIsDeleted())).thenReturn(prescriptions);
        when(mapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        dateUtil.when(() -> DateUtil.subtractDates(prescription.getEndDate(),
                prescription.getPrescribedDays() - 1)).thenReturn(new Date());
        dateUtil.when(() -> DateUtil.isSameDate(new Date(), prescription.getEndDate(),  Calendar.DATE)).thenReturn(false);
        doNothing().when(mapper).map(prescription, prescriptionDTO);
        //then
        actualPrescriptions = prescriptionService.getPrescriptions(requestDTO);
        dateUtil.close();
        Assertions.assertEquals(0,
                actualPrescriptions.get(0).getPrescriptionRemainingDays());
    }

    @Test
    @DisplayName("UpdatePrescriptionVisit Test")
    void updatePrescriptionVisit() {
        //given
        prescriptionRequestDTO = TestDataProvider.getPrescriptionRequestDTO();
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        patientVisit.setPrescription(Constants.BOOLEAN_TRUE);
        //when
        when(patientVisitService.getPatientVisitById(prescriptionRequestDTO.getPatientTrackId()))
                .thenReturn(patientVisit);
        when(patientVisitService.updatePatientVisit(patientVisit)).thenReturn(patientVisit);
        //then
        prescriptionService.updatePrescriptionPatientVisit(prescriptionRequestDTO);
        verify(patientVisitService, atLeastOnce()).updatePatientVisit(patientVisit);

    }
}