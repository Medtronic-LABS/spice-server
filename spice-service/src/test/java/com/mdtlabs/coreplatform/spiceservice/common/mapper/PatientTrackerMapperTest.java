package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

/**
 * <p>
 * PatientTrackerMapperTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientTrackerMapper class.
 * </p>
 *
 * @author Jaganathan created on Mar 2, 2023
 */
@ExtendWith(MockitoExtension.class)
class PatientTrackerMapperTest {

    @InjectMocks
    private PatientTrackerMapper trackerMapper;

    @ParameterizedTest
    @DisplayName("SetPatientTrackerFromBpLog Test")
    @CsvSource({", , , , , , , ,", "176.3, 56.0, 32.2, 80, 50, 32, Medium, 50, low",
            ",56.0, 32.2, 80, 50, 32, Medium, 50, low", "176.3, 56.0, , 80, 50, 32, Medium, 50, low",
            "176.3, 56.0, 32.2, , 50, 32, Medium, 50, low", "176.3, 56.0, 32.2, 80, , 32, Medium, 50, low",
            "176.3, 56.0, 32.2, 80, 50, , Medium, 50, low", "176.3, 56.0, 32.2, 80, 50, 32, , 50, low",
            "176.3, 56.0, 32.2, 80, 50, 32, Medium, , low", "176.3, 56.0, 32.2, 80, 50, 32, Medium, 50, ",
            ", , 32.2, 80, 50, 32, Medium, 50, low", "176.3, , , 80, 50, 32, Medium, 50, low",
            "176.3, 56.0, , , 50, 32, Medium, 50, low", "176.3, 56.0, 32.2, , , 32, Medium, 50, low",
            "176.3, 56.0, 32.2, 80, , , Medium, 50, low", "176.3, 56.0, 32.2, 80, 50, , , 50, low",
            "176.3, 56.0, 32.2, 80, 50, 32, , , low", "176.3, 56.0, 32.2, 80, 50, 32, Medium, , ",
            "176.3, 56.0, 32.2, 80, 50, 32, Medium, 50, ", ", , , , , , , , low"})
    void setPatientTrackerFromBpLog(Double height, Double weight, Double bmi, Integer avgSystolic, Integer avgDiastolic,
                                    Integer avgPulse, String cvdRiskLevel, Integer cvdRiskScoure, String riskLevel) {
        //given
        BpLog bplog = TestDataProvider.getBpLog();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        Date nextBpAssessmentDate = new Date(12 / 03 / 23);
        bplog.setHeight(height);
        bplog.setWeight(weight);
        bplog.setBmi(bmi);
        bplog.setAvgDiastolic(avgDiastolic);
        bplog.setAvgSystolic(avgSystolic);
        bplog.setAvgPulse(avgPulse);
        bplog.setCvdRiskLevel(cvdRiskLevel);
        bplog.setCvdRiskScore(cvdRiskScoure);
        bplog.setRiskLevel(riskLevel);
        //then
        PatientTracker actualTracker = trackerMapper.setPatientTrackerFromBpLog(bplog, patientTracker);
        Assertions.assertEquals(patientTracker, actualTracker);

    }

    @Test
    void toVerifySetPatientTrackerFromBpLog() {
        //given
        BpLog bplog = new BpLog();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        //then
        PatientTracker actualTracker = trackerMapper.setPatientTrackerFromBpLog(bplog, patientTracker);
        Assertions.assertEquals(patientTracker, actualTracker);
    }

    @ParameterizedTest
    @DisplayName("SetPatientTrackerFromGlucose Test")
    @CsvSource({" , , ", "80, type, unit", "80, type, ", "80, , unit", ", type, unit", ", , unit", "80, type, ",
            "80, , ", ", type, "})
    void setPatientTrackerFromGlucose(Double glucoseValue, String type, String unit) {
        //given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        Date nextBpAssessmentDate = new Date(12 / 03 / 23);
        glucoseLog.setGlucoseValue(glucoseValue);
        glucoseLog.setGlucoseType(type);
        glucoseLog.setGlucoseUnit(unit);
        //then
        PatientTracker actualTracker = trackerMapper.setPatientTrackerFromGlucose(glucoseLog,
                patientTracker, nextBpAssessmentDate);
        Assertions.assertEquals(patientTracker, actualTracker);
        nextBpAssessmentDate = null;
        glucoseLog.setGlucoseValue(glucoseValue);
        glucoseLog.setGlucoseType(type);
        glucoseLog.setGlucoseUnit(unit);
        //then
        actualTracker = trackerMapper.setPatientTrackerFromGlucose(glucoseLog,
                patientTracker, nextBpAssessmentDate);
        Assertions.assertEquals(patientTracker, actualTracker);
    }

}
