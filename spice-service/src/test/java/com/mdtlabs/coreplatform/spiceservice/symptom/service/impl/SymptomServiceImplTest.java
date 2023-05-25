package com.mdtlabs.coreplatform.spiceservice.symptom.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Symptom;
import com.mdtlabs.coreplatform.spiceservice.symptom.repository.SymptomRepository;
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

import static org.mockito.Mockito.when;

/**
 * <p>
 * SymptomServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in SymptomServiceImpl
 * class.
 * </p>
 *
 * @author Nandhakumar Karthikeyan created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class SymptomServiceImplTest {

    @InjectMocks
    SymptomServiceImpl symptomServiceImpl;

    @Mock
    SymptomRepository symptomRepository;

    @Test
    @DisplayName("GetAllSymptom Test")
    void getAllSymptoms() {
        //given
        Symptom symptom = TestDataProvider.getSymptomData();
        List<Symptom> symptomList = new ArrayList<Symptom>();
        Constants.SYMPTOMS = new ArrayList<Symptom>();
        symptomList.add(symptom);

        //when
        when(symptomRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(symptomList);

        //then
        List<Symptom> symptoms = symptomServiceImpl.getSymptoms();
        Assertions.assertNotNull(symptoms);
        Assertions.assertEquals(symptomList.size(), symptoms.size());
    }

    @Test
    @DisplayName("GetAllSymptom Test")
    void getAllSymptomsEmptyTest() {
        //given
        Symptom symptom = TestDataProvider.getSymptomData();
        List<Symptom> symptomList = new ArrayList<Symptom>();
        symptomList.add(symptom);
        Constants.SYMPTOMS = symptomList;

        //then
        List<Symptom> symptoms = symptomServiceImpl.getSymptoms();
        Assertions.assertNotNull(symptoms);
        Assertions.assertEquals(symptomList.size(), symptoms.size());
    }

    @Test
    @DisplayName("getSymptomsByCulture Test")
    void getSymptomsByCulture() {
        //given
        Constants.SYMPTOMS = new ArrayList<Symptom>();
        Symptom symptomOne = TestDataProvider.getSymptomData();
        Symptom symptomTwo = TestDataProvider.getSymptomData();
        List<Symptom> symptomList = new ArrayList<Symptom>();
        symptomList.add(symptomOne);
        symptomList.add(symptomTwo);

        //when
        when(symptomRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(symptomList);

        //then
        List<Symptom> symptoms = symptomServiceImpl.getSymptoms();
        Assertions.assertNotNull(symptoms);
        Assertions.assertEquals(symptoms.size(), symptomList.size());
        Assertions.assertEquals(Constants.TWO, symptoms.size());
    }

    @Test
    @DisplayName("getSymptomsByCultureEmpty Test")
    void getSymptomsByCultureEmptyTest() {
        //given
        Symptom symptomOne = TestDataProvider.getSymptomData();
        Symptom symptomTwo = TestDataProvider.getSymptomData();
        List<Symptom> symptomList = new ArrayList<Symptom>();
        symptomList.add(symptomOne);
        symptomList.add(symptomTwo);
        Constants.SYMPTOMS = symptomList;

        //then
        List<Symptom> symptoms = symptomServiceImpl.getSymptoms();
        Assertions.assertNotNull(symptoms);
        Assertions.assertEquals(symptoms.size(), symptomList.size());
        Assertions.assertEquals(Constants.TWO, symptoms.size());
    }
}
