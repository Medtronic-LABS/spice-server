package com.mdtlabs.coreplatform.spiceservice.patientsymptom.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.repository.PatientSymptomRepository;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * <p>
 * PatientSymptomServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in
 * PatientSymptomServiceImpl class.
 * </p>
 *
 * @author Nandhakumar Karthikeyan created on Feb 10, 2023
 */
@ExtendWith(MockitoExtension.class)
class PatientSymptomServiceImplTest {

    @InjectMocks
    PatientSymptomServiceImpl patientSymptomServiceImpl;

    @Mock
    PatientSymptomRepository patientSymptomRepository;

    @Test
    @DisplayName("getSymptomsByPatientTracker Test")
    void getSymptomsByBpLogId() {
        PatientSymptom patientSymptom = TestDataProvider.getPatientSymptom();
        patientSymptom.setPatientTrackId(2L);
        List<PatientSymptom> patientSymptomList = new ArrayList<>();
        patientSymptomList.add(patientSymptom);

        // when
        when(patientSymptomRepository
                .findByPatientTrackIdAndBpLogIdOrderByUpdatedAtDesc(1L, 2L)).thenReturn(patientSymptomList);

        // then
        List<PatientSymptom> patientSymptomListResponse = patientSymptomServiceImpl.getSymptomsByBpLogId(1L, 2L);
        Assertions.assertNotNull(patientSymptomListResponse);
        Assertions.assertEquals(patientSymptomList.size(), patientSymptomListResponse.size());
        Assertions.assertEquals(patientSymptomList.get(0).getPatientTrackId(),
                patientSymptomListResponse.get(0).getPatientTrackId());
    }

    @Test
    @DisplayName("getSymptomsByPatientTracker Test")
    void getSymptomsByGlucoseLogId() {
        PatientSymptom patientSymptom = TestDataProvider.getPatientSymptom();
        patientSymptom.setPatientTrackId(2L);
        List<PatientSymptom> patientSymptomList = new ArrayList<>();
        patientSymptomList.add(patientSymptom);

        // when
        when(patientSymptomRepository
                .findByPatientTrackIdAndGlucoseLogIdOrderByUpdatedAtDesc(1L, 2L)).thenReturn(patientSymptomList);

        // then
        List<PatientSymptom> patientSymptomListResponse = patientSymptomServiceImpl.getSymptomsByGlucoseLogId(1L, 2L);
        Assertions.assertNotNull(patientSymptomListResponse);
        Assertions.assertEquals(patientSymptomList.size(), patientSymptomListResponse.size());
        Assertions.assertEquals(patientSymptomList.get(0).getPatientTrackId(),
                patientSymptomListResponse.get(0).getPatientTrackId());
    }


    @Test
    @DisplayName("AddPatientSymptoms Test")
    void addPatientSymptoms() {
        PatientSymptom patientSymptom = TestDataProvider.getPatientSymptom();
        List<PatientSymptom> patientSymptomList = new ArrayList<>();
        patientSymptomList.add(patientSymptom);

        // when
        when(patientSymptomRepository.saveAll(patientSymptomList)).thenReturn(patientSymptomList);

        // then
        List<PatientSymptom> patientSymptomListResponse = patientSymptomServiceImpl
                .addPatientSymptoms(patientSymptomList);
        Assertions.assertNotNull(patientSymptomListResponse);
        Assertions.assertEquals(patientSymptomList.size(), patientSymptomListResponse.size());
    }

    @Test
    @DisplayName("AddPatientSymptoms Empty List Test")
    void addPatientSymptomsEmptyTest() {
        List<PatientSymptom> patientSymptomList = new ArrayList<>();
        // then
        Assertions.assertThrows(DataNotAcceptableException.class, () -> patientSymptomServiceImpl.addPatientSymptoms(patientSymptomList));
    }


    @Test
    @DisplayName("RemovePatientSymptom Test")
    void removePatientSymptom() {
        List<PatientSymptom> patientSymptomList = List.of(TestDataProvider.getPatientSymptom());
        when(patientSymptomRepository.findByPatientTrackId(anyLong())).thenReturn(patientSymptomList);
        patientSymptomServiceImpl.removePatientSymptom(1L);
        Assertions.assertTrue(patientSymptomList.get(Constants.ZERO).isDeleted());
    }
}
