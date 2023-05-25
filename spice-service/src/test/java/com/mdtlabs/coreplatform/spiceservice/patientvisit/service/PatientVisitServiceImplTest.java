package com.mdtlabs.coreplatform.spiceservice.patientvisit.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.util.DateUtil;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.repository.PatientVisitRepository;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.impl.PatientVisitServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatientVisitServiceImplTest {

    @InjectMocks
    private PatientVisitServiceImpl patientVisitService;

    @Mock
    private PatientVisitRepository patientVisitRepository;

    private final CommonRequestDTO patientVisitRequest = TestDataProvider.getCommonRequestDTO();

    private PatientVisit patientVisit;

    @Test
    @DisplayName("addPatientVisitWithExitingData Test")
    void addPatientVisitWithInvalidData() {
        //given
        patientVisit = new PatientVisit();
        patientVisit.setId(1L);
        patientVisit.setTenantId(4L);
        patientVisit.setPatientTrackId(1L);
        patientVisit.setVisitDate(new Date());
        Timezone timezone = TestDataProvider.getTimezone();
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.setTimezone(timezone);
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        MockedStatic<DateUtil> dateUtil = mockStatic(DateUtil.class);
        dateUtil.when(DateUtil::getStartOfDay).thenReturn("+10:00");
        dateUtil.when(DateUtil::getEndOfDay).thenReturn("+10:00");

        //when
        when(patientVisitRepository.getPatientVisitByTrackId(1L, "+10:00", "+10:00")).thenReturn(null);
        when(patientVisitRepository.save(any())).thenReturn(patientVisit);


        //then
        Map<String, Long> actualPatientVisit = patientVisitService.createPatientVisit(patientVisitRequest);
        dateUtil.close();
        userContextHolder.close();
        Assertions.assertTrue(actualPatientVisit.containsValue(1L));
        Assertions.assertNotNull(actualPatientVisit);

    }

    @Test
    @DisplayName("AddPatientVisit Test")
    void addPatientVisit() {
        patientVisit = TestDataProvider.getPatientVisit();
        Timezone timezone = TestDataProvider.getTimezone();
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.setTimezone(timezone);
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        MockedStatic<DateUtil> dateUtil = mockStatic(DateUtil.class);
        dateUtil.when(DateUtil::getStartOfDay).thenReturn("+10:00");
        dateUtil.when(DateUtil::getEndOfDay).thenReturn("+10:00");

        when(patientVisitRepository.getPatientVisitByTrackId(1L, "+10:00", "+10:00")).thenReturn(patientVisit);
        Map<String, Long> actualPatientVisit = patientVisitService.createPatientVisit(patientVisitRequest);
        dateUtil.close();
        userContextHolder.close();
        Assertions.assertNotNull(actualPatientVisit);
    }

    @Test
    @DisplayName("GetPatientVisitDatesNull Test")
    void getPatientVisitDatesNull() {
        //given
        List<PatientVisit> visitDates;
        //when
        when(patientVisitRepository.getPatientVisitDates(patientVisitRequest.getPatientTrackId()))
                .thenReturn(null);
        //then
        List<PatientVisit> actualList = patientVisitService
                .getPatientVisitDates(patientVisitRequest.getPatientTrackId());
        Assertions.assertNull(null);
        visitDates = new ArrayList<>();
        Assertions.assertEquals(visitDates, actualList);
    }

    @Test
    @DisplayName("GetPatientVisitDates Test")
    void getPatientVisitDates() {
        //given
        List<PatientVisit> visits = List.of(TestDataProvider.getPatientVisit(), TestDataProvider.getPatientVisit());
        //when
        when(patientVisitRepository.getPatientVisitDates(patientVisitRequest.getPatientTrackId()))
                .thenReturn(visits);
        //then
        List<PatientVisit> actualVisits = patientVisitService
                .getPatientVisitDates(patientVisitRequest.getPatientTrackId());
        Assertions.assertEquals(visits, actualVisits);
        Assertions.assertEquals(visits.size(), actualVisits.size());
        Assertions.assertEquals(visits.get(0).getPatientTrackId(), actualVisits.get(0).getPatientTrackId());

    }

    @Test
    @DisplayName("GetPatientVisitByInvalidId Test")
    void getPatientVisitByInvalidId() {
        //given
        Long id = 0L;
        //when
        when(patientVisitRepository.findByIdAndIsDeleted(id, Constants.BOOLEAN_FALSE)).thenReturn(patientVisit);
        //then
        Assertions.assertThrows(DataNotFoundException.class, () -> patientVisitService.getPatientVisitById(1L));
    }

    @Test
    @DisplayName("GetPatientVisitById Test")
    void getPatientVisitById() {
        //given
        patientVisit = TestDataProvider.getPatientVisit();
        //when
        when(patientVisitRepository.findByIdAndIsDeleted(patientVisit.getId(), Constants.BOOLEAN_FALSE))
                .thenReturn(patientVisit);
        PatientVisit actualPatientVisit = patientVisitService.getPatientVisitById(patientVisit.getId());
        Assertions.assertEquals(patientVisit, actualPatientVisit);
        Assertions.assertEquals(patientVisit.getPatientTrackId(), actualPatientVisit.getPatientTrackId());
    }

    @Test
    @DisplayName("GetPatientVisitByInvalidTenantId Test")
    void getPatientVisitByInvalidTenantId() {
        //given
        Long id = 1L;
        //when
        when(patientVisitRepository.findByIdAndIsDeleted(id, Constants.BOOLEAN_FALSE)).thenReturn(patientVisit);
        //then
        Assertions.assertThrows(DataNotFoundException.class, () -> patientVisitService.getPatientVisitById(1L));
    }

    @Test
    @DisplayName("GetPatientVisitByTenantId Test")
    void getPatientVisitByTenantId() {
        //given
        Long id = 1L;
        patientVisit = TestDataProvider.getPatientVisit();
        //when
        when(patientVisitRepository.findByIdAndIsDeleted(id, Constants.BOOLEAN_FALSE)).thenReturn(patientVisit);
        //then
        PatientVisit actualPatientVisit = patientVisitService.getPatientVisitById(id);
        assertNotNull(actualPatientVisit);
        Assertions.assertEquals(patientVisit, actualPatientVisit);
        Assertions.assertEquals(patientVisit.getTenantId(), actualPatientVisit.getTenantId());
        Assertions.assertEquals(patientVisit.getPatientTrackId(), actualPatientVisit.getPatientTrackId());

    }

    @Test
    @DisplayName("UpdateValidPatientVisit Test")
    void updateValidPatientVisit() {
        //given
        patientVisit = TestDataProvider.getPatientVisit();
        patientVisit.setVisitDate(new Date());
        //when
        when(patientVisitRepository.save(patientVisit)).thenReturn(patientVisit);
        //then
        assertNotNull(patientVisit);
        PatientVisit actualVisit = patientVisitService.updatePatientVisit(patientVisit);
        Assertions.assertEquals(patientVisit, actualVisit);
        Assertions.assertEquals(patientVisit.getVisitDate(), actualVisit.getVisitDate());
        Assertions.assertEquals(patientVisit.getPatientTrackId(), actualVisit.getPatientTrackId());
    }

    @Test
    @DisplayName("GetPatientVisitDatesByNoVisits Test")
    void getPatientVisitDatesByNoVisits() {
        //given
        Long id = 0L;
        boolean isInvestigation = Constants.BOOLEAN_FALSE;
        boolean isMedicalReview = Constants.BOOLEAN_FALSE;
        boolean isPrescription = Constants.BOOLEAN_FALSE;
        List<PatientVisit> patientVisits;
        //when
        when(patientVisitRepository.getPatientVisitDates(id, isInvestigation, isMedicalReview, isPrescription))
                .thenReturn(null);
        //then
        List<PatientVisit> actualPatientVisit = patientVisitService
                .getPatientVisitDates(id, isInvestigation, isMedicalReview, isPrescription);
        Assertions.assertNull(null);
        patientVisits = new ArrayList<>();
        Assertions.assertEquals(patientVisits, actualPatientVisit);
        Assertions.assertEquals(patientVisits.size(), actualPatientVisit.size());
    }

    @Test
    @DisplayName("GetPatientVisitDates Test")
    void getPatientVisitDatesByValidData() {
        //given
        patientVisit = TestDataProvider.getPatientVisit();
        List<PatientVisit> patientVisits = List.of(patientVisit);
        //when
        when(patientVisitRepository.getPatientVisitDates(patientVisit.getPatientTrackId(),
                patientVisit.isInvestigation(), patientVisit.isMedicalReview(), patientVisit.isPrescription()))
                .thenReturn(patientVisits);
        List<PatientVisit> actualPatientVisit = patientVisitService.getPatientVisitDates(patientVisit
                        .getPatientTrackId(), patientVisit.isInvestigation(), patientVisit.isMedicalReview(),
                patientVisit.isPrescription());
        Assertions.assertEquals(patientVisits, actualPatientVisit);
        Assertions.assertEquals(patientVisits.size(), actualPatientVisit.size());
        Assertions.assertEquals(patientVisits.get(Constants.ZERO).getPatientTrackId(),
                actualPatientVisit.get(Constants.ZERO).getPatientTrackId());
    }

    @Test
    @DisplayName("RemovePatientVisit Test")
    void removePatientVisit() {
        //given
        patientVisit = TestDataProvider.getPatientVisit();
        List<PatientVisit> patientVisits = List.of(patientVisit);
        //when
        when(patientVisitRepository.findByPatientTrackId(anyLong()))
                .thenReturn(patientVisits);

        when(patientVisitRepository.save(any()))
                .thenReturn(patientVisits);
        patientVisitService.removePatientVisit(anyLong());
        assertNotNull(patientVisits);
    }
}
