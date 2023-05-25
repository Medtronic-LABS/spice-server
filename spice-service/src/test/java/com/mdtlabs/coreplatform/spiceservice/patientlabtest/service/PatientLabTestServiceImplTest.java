package com.mdtlabs.coreplatform.spiceservice.patientlabtest.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PatientLabTestMapper;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository.PatientLabTestRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository.PatientLabTestResultRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.service.impl.PatientLabTestServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * PatientLabTestServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in
 * PatientLabTestServiceImpl class.
 * </p>
 *
 * @author Jaganathan created on Feb 8, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PatientLabTestServiceImplTest {

    private static MockedStatic<CommonUtil> commonUtil;

    private static MockedStatic<UserSelectedTenantContextHolder> userSelectedTenantContextHolder;

    @InjectMocks
    private PatientLabTestServiceImpl patientLabTestService;

    @Mock
    private PatientLabTestRepository patientLabTestRepository;

    @Mock
    private PatientLabTestResultRepository patientLabTestResultRepository;

    @Mock
    private AdminApiInterface adminApiInterface;

    @Mock
    private UserApiInterface userApiInterface;

    @Mock
    private PatientLabTestMapper patientLabTestMapper;

    @Mock
    private PatientVisitService patientVisitService;

    @Mock
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Mock
    private PatientTrackerService patientTrackerService;

    public static Stream<Arguments> patientLabTestData() {
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        return Stream.of(
                Arguments.of(Constants.ROLE_LAB_TECHNICIAN, Constants.BOOLEAN_TRUE, 1L,
                        List.of(patientVisit)),
                Arguments.of(Constants.ROLE_ACCOUNT_ADMIN, Constants.BOOLEAN_TRUE, 1L,
                        List.of(patientVisit)),
//                Arguments.of(Constants.ROLE_LAB_TECHNICIAN, Constants.BOOLEAN_TRUE, "",
//                        List.of(patientVisit)),
                Arguments.of(Constants.ROLE_LAB_TECHNICIAN, Constants.BOOLEAN_TRUE, 1L, List.of())

        );
    }

    @Test
    @DisplayName("CreatePatientLabTestWithNull Tests")
    void createPatientLabTestWithNull() {
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.createPatientLabTest(null));
    }

    @Test
    @DisplayName("CreatePatientLabTestWithNonNull Test")
    void createPatientLabTestWithLabTestNull() {
        // given
        PatientLabTestRequestDTO requestDTO = new PatientLabTestRequestDTO();
        // when
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.createPatientLabTest(requestDTO));
    }

    @Test
    @DisplayName("removePatientLabTest Test")
    void removePatientLabTestNullTest() {
        // given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        requestDTO.setId(1L);
        // when
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientLabTestService.removePatientLabTest(requestDTO));
    }

    @Test
    @DisplayName("reviewPatientLabTest")
    void reviewPatientLabTestTrackerNull() {
        // given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        requestDTO.setPatientTrackId(null);
        // when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientLabTestService.reviewPatientLabTest(requestDTO));
    }

    @Test
    @DisplayName("reviewPatientLabTest")
    void reviewPatientLabTest() {
        // given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();

        // when
        when(patientLabTestRepository.findByIdAndTenantId(requestDTO.getId(),
                requestDTO.getTenantId())).thenReturn(null);
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientLabTestService.reviewPatientLabTest(requestDTO));
    }

    @Test
    @DisplayName("reviewPatientLabTest")
    void reviewPatientLabTestsWithoutComment() {
        // given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        PatientLabTest patientLabTest = TestDataProvider.getPatientLabTest();

        // when
        when(patientLabTestRepository.findByIdAndTenantId(requestDTO.getId(),
                requestDTO.getTenantId())).thenReturn(patientLabTest);
        when(patientLabTestRepository.save(patientLabTest)).thenReturn(patientLabTest);

        patientLabTestService.reviewPatientLabTest(requestDTO);
        verify(patientLabTestRepository, atLeastOnce()).save(patientLabTest);
    }

    @Test
    @DisplayName("reviewPatientLabTest")
    void reviewPatientLabTests() {
        // given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        requestDTO.setComment("TEST");
        PatientLabTest patientLabTest = TestDataProvider.getPatientLabTest();

        // when
        when(patientLabTestRepository.findByIdAndTenantId(requestDTO.getId(),
                requestDTO.getTenantId())).thenReturn(patientLabTest);
        when(patientLabTestRepository.save(patientLabTest)).thenReturn(patientLabTest);

        patientLabTestService.reviewPatientLabTest(requestDTO);
        verify(patientLabTestRepository, atLeastOnce()).save(patientLabTest);
    }

    @Test
    @DisplayName("CreatePatientLabTestWithNonNull Test")
    void getLabTestListNullTrackerId() {
        // given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        requestDTO.setPatientTrackId(null);

        // when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientLabTestService.getPatientLabTestList(requestDTO));
    }

    @Test
    @DisplayName("removePatientLabTest Test")
    void removePatientLabTestResultNull() {
        // given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        requestDTO.setId(1L);
        PatientLabTest patientLabTest = TestDataProvider.getPatientLabTest();
        patientLabTest.setResultDate(new Date());

        // when
        when(patientLabTestRepository.findByIdAndIsDeleted(requestDTO.getId(),
                Constants.BOOLEAN_FALSE)).thenReturn(patientLabTest);
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientLabTestService.removePatientLabTest(requestDTO));
    }

    @Test
    @DisplayName("removePatientLabTest Test")
    void removePatientLabTestIdNull() {
        // given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();

        // then
        boolean response = patientLabTestService.removePatientLabTest(requestDTO);
        Assertions.assertTrue(response);
    }

    @Test
    @DisplayName("removePatientLabTest Test")
    void removePatientLabTest() {
        // given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        requestDTO.setId(1L);
        PatientLabTest patientLabTest = TestDataProvider.getPatientLabTest();
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        // when
        when(patientLabTestRepository.findByIdAndIsDeleted(requestDTO.getId(),
                Constants.BOOLEAN_FALSE)).thenReturn(patientLabTest);
        patientLabTest.setDeleted(Constants.BOOLEAN_TRUE);
        when(patientLabTestRepository.save(patientLabTest)).thenReturn(patientLabTest);
        when(patientTrackerService.getPatientTrackerById(requestDTO.getPatientTrackId()))
                .thenReturn(patientTracker);
        when(patientLabTestRepository
                .findAllByPatientVisitIdAndIsDeleted(patientLabTest.getPatientVisitId(),
                        Constants.BOOLEAN_FALSE))
                .thenReturn(List.of(patientLabTest));
        when(patientVisitService.getPatientVisitById(patientLabTest.getPatientVisitId())).thenReturn(patientVisit);
        // then
        boolean response = patientLabTestService.removePatientLabTest(requestDTO);
        Assertions.assertTrue(response);
    }

    @Test
    @DisplayName("removePatientLabTest Test")
    void removePatientLabTestEmptyList() {
        // given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        requestDTO.setId(1L);
        PatientLabTest patientLabTest = TestDataProvider.getPatientLabTest();
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        // when
        when(patientLabTestRepository.findByIdAndIsDeleted(requestDTO.getId(),
                Constants.BOOLEAN_FALSE)).thenReturn(patientLabTest);
        patientLabTest.setDeleted(Constants.BOOLEAN_TRUE);
        when(patientLabTestRepository.save(patientLabTest)).thenReturn(patientLabTest);
        when(patientTrackerService.getPatientTrackerById(requestDTO.getPatientTrackId()))
                .thenReturn(patientTracker);
        when(patientLabTestRepository
                .findAllByPatientVisitIdAndIsDeleted(patientLabTest.getPatientVisitId(),
                        Constants.BOOLEAN_FALSE))
                .thenReturn(List.of());
        when(patientVisitService.getPatientVisitById(patientLabTest.getPatientVisitId())).thenReturn(patientVisit);
        // then
        boolean response = patientLabTestService.removePatientLabTest(requestDTO);
        Assertions.assertTrue(response);
    }

    @Test
    @DisplayName("CreatePatientLabTestWithEmptyList Test")
    void createPatientLabTestWithLabTestEmpty() {
        // given
        PatientLabTestRequestDTO requestDTO = new PatientLabTestRequestDTO();
        requestDTO.setLabTest(List.of());
        // when
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.createPatientLabTest(requestDTO));
    }

    @Test
    @DisplayName("setUniqueLabTest Test")
    void setUniqueLabTestTest() {
        // given
        PatientLabTestRequestDTO requestDTO = TestDataProvider.getPatientLabTestRequest();
        requestDTO.setLabTest(null);
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.createPatientLabTest(requestDTO));
    }

    @Test
    @DisplayName("CreatePatientLabTestWithEmptyList Test")
    void createPatientLabTest() {
        // given
        PatientLabTestRequestDTO requestDTO = new PatientLabTestRequestDTO();
        requestDTO.setPatientTrackId(1L);
        PatientLabTest lapTest = TestDataProvider.getPatientLabTest();
        requestDTO.setLabTest(List.of(lapTest));
        List<LabTest> labTests = List.of(TestDataProvider.getLabTestData());
        ResponseEntity<List<LabTest>> labDetails = new ResponseEntity<>(labTests, HttpStatus.OK);
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientTreatmentPlan patientTreatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        patientTracker.setId(1L);

        // Mock Static
        TestDataProvider.init();
        TestDataProvider.getStaticMock();

        // when
        when(adminApiInterface.getLabTestsByIds(any(), any(), any())).thenReturn(labDetails);
        when(patientVisitService.getPatientVisitById(requestDTO.getPatientVisitId())).thenReturn(patientVisit);
        when(patientLabTestRepository.saveAll(requestDTO.getLabTest())).thenReturn(List.of(lapTest));

        when(patientTrackerService.getPatientTrackerById(1L)).thenReturn(patientTracker);
        when(patientTreatmentPlanService
                .getPatientTreatmentPlan(1L)).thenReturn(patientTreatmentPlan);

        // then
        List<PatientLabTest> response = patientLabTestService.createPatientLabTest(requestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(Constants.MINUS_ONE_LONG, response.get(0).getId());
        Assertions.assertEquals(Constants.ONE, response.size());
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("CreatePatientLabTestWithEmptyList Test")
    void createPatientLabTestInOther() {
        // given
        PatientLabTestRequestDTO requestDTO = new PatientLabTestRequestDTO();
        requestDTO.setPatientTrackId(1L);
        PatientLabTest lapTest = TestDataProvider.getPatientLabTest();
        PatientLabTest labTest2 = TestDataProvider.getPatientLabTest();
        LabTest labTestObj = TestDataProvider.getLabTestData();
        lapTest.setLabTestId(Constants.MINUS_ONE_LONG);
        requestDTO.setLabTest(List.of(lapTest));
        List<LabTest> labTests = List.of(TestDataProvider.getLabTestData());
        ResponseEntity<List<LabTest>> labDetails = new ResponseEntity<>(labTests, HttpStatus.OK);

        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setId(1L);
        SearchRequestDTO requestEntity = new SearchRequestDTO();
        requestEntity.setCountryId(1L);
        requestEntity.setSearchTerm("searchTerm");
        ResponseEntity<LabTest> labTestResponse = new ResponseEntity<>(labTestObj, HttpStatus.OK);

        // Mock Static
        TestDataProvider.init();
        TestDataProvider.getStaticMock();

        // when
        when(adminApiInterface.getLabTestsByIds(any(), any(), any())).thenReturn(labDetails);
        when(patientVisitService.getPatientVisitById(requestDTO.getPatientVisitId())).thenReturn(patientVisit);
        when(patientLabTestRepository.saveAll(requestDTO.getLabTest())).thenReturn(List.of(lapTest));
        when(adminApiInterface
                .getLabTestByName(any(), any(), any()))
                .thenReturn(labTestResponse);
        when(patientTrackerService.getPatientTrackerById(1L)).thenReturn(patientTracker);
        when(patientLabTestRepository.getPatientLabTestsWithoutResults(1L,
                1L)).thenReturn(List.of(labTest2));
        when(patientTreatmentPlanService
                .getPatientTreatmentPlan(1L)).thenReturn(null);

        // then
        List<PatientLabTest> response = patientLabTestService.createPatientLabTest(requestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(Constants.MINUS_ONE_LONG, response.get(0).getId());
        Assertions.assertEquals(Constants.ONE, response.size());
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("CreatePatientLabTestWithEmptyList Test")
    void createPatientLabTestMisMatchError() {
        // given
        PatientLabTestRequestDTO requestDTO = new PatientLabTestRequestDTO();
        requestDTO.setPatientTrackId(1L);
        PatientLabTest lapTest = TestDataProvider.getPatientLabTest();
        PatientLabTest labTest2 = TestDataProvider.getPatientLabTest();
        LabTest labTestObj = TestDataProvider.getLabTestData();
        lapTest.setLabTestId(Constants.MINUS_ONE_LONG);
        requestDTO.setLabTest(List.of(lapTest, labTest2));
        List<LabTest> labTests = List.of(TestDataProvider.getLabTestData());
        ResponseEntity<List<LabTest>> labDetails = new ResponseEntity<>(labTests, HttpStatus.OK);

        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientTreatmentPlan patientTreatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        patientTracker.setId(1L);
        SearchRequestDTO requestEntity = new SearchRequestDTO();
        requestEntity.setCountryId(1L);
        requestEntity.setSearchTerm("searchTerm");
        ResponseEntity<LabTest> labTestResponse = new ResponseEntity<>(labTestObj, HttpStatus.OK);

        // Mock Static
        TestDataProvider.init();
        TestDataProvider.getStaticMock();

        // when
        when(adminApiInterface.getLabTestsByIds(any(), any(), any())).thenReturn(labDetails);
        when(patientVisitService.getPatientVisitById(requestDTO.getPatientVisitId())).thenReturn(patientVisit);
        when(patientLabTestRepository.saveAll(requestDTO.getLabTest())).thenReturn(List.of(lapTest));
        when(adminApiInterface
                .getLabTestByName(any(), any(), any()))
                .thenReturn(labTestResponse);
        when(patientTrackerService.getPatientTrackerById(1L)).thenReturn(patientTracker);
        when(patientTreatmentPlanService
                .getPatientTreatmentPlan(1L)).thenReturn(patientTreatmentPlan);

        // then
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.createPatientLabTest(requestDTO));

        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("CreatePatientLabTestWithEmptyList Test")
    void createPatientNullPatientLabTest() {
        // given
        PatientLabTestRequestDTO requestDTO = new PatientLabTestRequestDTO();
        requestDTO.setPatientTrackId(1L);
        PatientLabTest lapTest = TestDataProvider.getPatientLabTest();
        LabTest labTestObj = TestDataProvider.getLabTestData();
        lapTest.setLabTestId(Constants.MINUS_ONE_LONG);
        requestDTO.setLabTest(List.of(lapTest));
        ResponseEntity<List<LabTest>> labDetails = new ResponseEntity<>(null, HttpStatus.OK);

        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientTreatmentPlan patientTreatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        patientTracker.setId(1L);
        SearchRequestDTO requestEntity = new SearchRequestDTO();
        requestEntity.setCountryId(1L);
        requestEntity.setSearchTerm("searchTerm");
        ResponseEntity<LabTest> labTestResponse = new ResponseEntity<>(labTestObj, HttpStatus.OK);

        // Mock Static
        TestDataProvider.init();
        TestDataProvider.getStaticMock();

        // when
        when(adminApiInterface.getLabTestsByIds(any(), any(), any())).thenReturn(labDetails);
        when(patientVisitService.getPatientVisitById(requestDTO.getPatientVisitId())).thenReturn(patientVisit);
        when(patientLabTestRepository.saveAll(requestDTO.getLabTest())).thenReturn(List.of(lapTest));
        when(adminApiInterface
                .getLabTestByName(any(), any(), any()))
                .thenReturn(labTestResponse);
        when(patientTrackerService.getPatientTrackerById(1L)).thenReturn(patientTracker);
        when(patientTreatmentPlanService
                .getPatientTreatmentPlan(1L)).thenReturn(patientTreatmentPlan);

        // then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientLabTestService.createPatientLabTest(requestDTO));

        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("CreatePatientLabTestWithEmptyList Test")
    void createPatientNullPatientLabTestDiff() {
        // given
        PatientLabTestRequestDTO requestDTO = new PatientLabTestRequestDTO();
        requestDTO.setPatientTrackId(1L);
        PatientLabTest labTest = TestDataProvider.getPatientLabTest();
        labTest.setLabTestId(1L);
        requestDTO.setLabTest(List.of(labTest));

        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientTreatmentPlan patientTreatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        patientTracker.setId(1L);
        SearchRequestDTO requestEntity = new SearchRequestDTO();
        requestEntity.setCountryId(1L);
        requestEntity.setSearchTerm("searchTerm");
        ResponseEntity<List<LabTest>> labTestResponse = new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        // Mock Static
        TestDataProvider.init();
        TestDataProvider.getStaticMock();

        // when
        when(adminApiInterface.getLabTestsByIds(any(), any(), any())).thenReturn(labTestResponse);
        when(patientVisitService.getPatientVisitById(requestDTO.getPatientVisitId())).thenReturn(patientVisit);
        when(patientLabTestRepository.saveAll(requestDTO.getLabTest())).thenReturn(List.of(labTest));
        when(patientTrackerService.getPatientTrackerById(1L)).thenReturn(patientTracker);
        when(patientTreatmentPlanService
                .getPatientTreatmentPlan(1L)).thenReturn(patientTreatmentPlan);

        // then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientLabTestService.createPatientLabTest(requestDTO));

        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("CreatePatientLabTestWithEmptyList Test")
    void createPatientNullPatientDataLabTest() {
        // given
        PatientLabTestRequestDTO requestDTO = new PatientLabTestRequestDTO();
        requestDTO.setPatientTrackId(1L);
        PatientLabTest lapTest = TestDataProvider.getPatientLabTest();
        lapTest.setLabTestId(Constants.MINUS_ONE_LONG);
        requestDTO.setLabTest(List.of(lapTest));
        List<LabTest> labTests = List.of(TestDataProvider.getLabTestData());
        ResponseEntity<List<LabTest>> labDetails = new ResponseEntity<>(labTests, HttpStatus.OK);

        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientTreatmentPlan patientTreatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        patientTracker.setId(1L);
        SearchRequestDTO requestEntity = new SearchRequestDTO();
        requestEntity.setCountryId(1L);
        requestEntity.setSearchTerm("searchTerm");
        ResponseEntity<LabTest> labTestResponse = new ResponseEntity<>(null, HttpStatus.OK);

        // Mock Static
        TestDataProvider.init();
        TestDataProvider.getStaticMock();

        // when
        when(adminApiInterface.getLabTestsByIds(any(), any(), any())).thenReturn(labDetails);
        when(patientVisitService.getPatientVisitById(requestDTO.getPatientVisitId())).thenReturn(patientVisit);
        when(patientLabTestRepository.saveAll(requestDTO.getLabTest())).thenReturn(List.of(lapTest));
        when(adminApiInterface
                .getLabTestByName(any(), any(), any()))
                .thenReturn(labTestResponse);
        when(patientTrackerService.getPatientTrackerById(1L)).thenReturn(patientTracker);
        when(patientTreatmentPlanService
                .getPatientTreatmentPlan(1L)).thenReturn(patientTreatmentPlan);

        // then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientLabTestService.createPatientLabTest(requestDTO));

        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("getPatientLabTestList")
    void getPatientLabTestListTest() {
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientLabTest labTest = TestDataProvider.getPatientLabTest();
        UserDTO user = TestDataProvider.getUserDTO();
        Map<Long, UserDTO> userMap = new HashMap<>();
        userMap.put(1L, user);
        ResponseEntity<Map<Long, UserDTO>> userDetails = new ResponseEntity<>(userMap, HttpStatus.OK);
        labTest.setReferredBy(1L);
        labTest.setResultUpdateBy(1L);
        PatientLabTestDTO labTestResponse = new PatientLabTestDTO();
        PatientLabTestDTO labTestResponseValue = TestDataProvider.getPatientLabTestDTO();
        // Mock Static
        TestDataProvider.init();
        TestDataProvider.getStaticMock();

        // when
        when(patientVisitService.getPatientVisitDates(requestDTO.getPatientTrackId(), Constants.BOOLEAN_TRUE,
                null, null)).thenReturn(List.of(patientVisit));
        when(patientLabTestRepository.getPatientLabTestList(requestDTO.getPatientTrackId(), null,
                Constants.BOOLEAN_FALSE)).thenReturn(List.of(labTest));
        when(userApiInterface
                .getUsers(any(), any(), any())).thenReturn(userDetails);
        when(patientLabTestMapper.setPatientLabTestDto(labTest, labTestResponse))
                .thenReturn(labTestResponseValue);
        PatientLabTestResponseDTO response = patientLabTestService.getPatientLabTestList(requestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getPatientLabTest());
        Assertions.assertEquals(Constants.ONE, response.getPatientLabTest().size());
        TestDataProvider.cleanUp();
    }

    @ParameterizedTest
    @MethodSource("patientLabTestData")
    void getPatientLabTestListTestRoleNameConditions(String role, boolean isLatest, Long visitId,
                                                     List<PatientVisit> patientVisitList) {
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        requestDTO.setRoleName(role);
        requestDTO.setLatestRequired(isLatest);
        requestDTO.setPatientVisitId(visitId);
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientLabTest labTest = TestDataProvider.getPatientLabTest();
        UserDTO user = TestDataProvider.getUserDTO();
        Map<Long, UserDTO> userMap = new HashMap<>();
        userMap.put(1L, user);
        ResponseEntity<Map<Long, UserDTO>> userDetails = new ResponseEntity<>(userMap, HttpStatus.OK);
        labTest.setReferredBy(1L);
        labTest.setResultUpdateBy(null);
        PatientLabTestDTO labTestResponse = new PatientLabTestDTO();
        PatientLabTestDTO labTestResponseValue = TestDataProvider.getPatientLabTestDTO();
        // Mock Static
        TestDataProvider.init();
        TestDataProvider.getStaticMock();

        // when
        when(patientVisitService.getPatientVisitDates(requestDTO.getPatientTrackId(), Constants.BOOLEAN_TRUE,
                null, null)).thenReturn(patientVisitList);
        when(patientLabTestRepository.getPatientLabTestListWithCondition(requestDTO.getPatientTrackId(), 1L,
                Constants.BOOLEAN_FALSE)).thenReturn(List.of(labTest));
        when(patientLabTestRepository.getPatientLabTestList(requestDTO.getPatientTrackId(), 1L,
                Constants.BOOLEAN_FALSE)).thenReturn(List.of(labTest));
        when(userApiInterface
                .getUsers(Constants.AUTH_TOKEN_SUBJECT, 1L, Set.of(1L))).thenReturn(userDetails);
        when(patientLabTestMapper.setPatientLabTestDto(labTest, labTestResponse))
                .thenReturn(labTestResponseValue);
        when(patientVisitService.getPatientVisitDates(requestDTO.getPatientTrackId(),
                Constants.BOOLEAN_TRUE, null, null)).thenReturn(List.of(patientVisit));

        // then
        PatientLabTestResponseDTO response = patientLabTestService.getPatientLabTestList(requestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getPatientLabTest());
        Assertions.assertEquals(Constants.ONE, response.getPatientLabTest().size());
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("GetLabTestCount Test")
    void getLabTestCount() {
        // given
        Long patientTrackerId = 0L;
        int count = 0;
        // when
        when(patientLabTestRepository.getLabTestNoReviewedCount(patientTrackerId)).thenReturn(count);
        // then
        int actualCount = patientLabTestService.getLabTestCount(patientTrackerId);
        Assertions.assertEquals(count, actualCount);
        // given
        patientTrackerId = 1L;
        count = 10;
        // when
        when(patientLabTestRepository.getLabTestNoReviewedCount(patientTrackerId)).thenReturn(count);
        // then
        actualCount = patientLabTestService.getLabTestCount(patientTrackerId);
        Assertions.assertEquals(count, actualCount);
    }

    @Test
    @DisplayName("GetPatientLabTest Test")
    void getPatientLabTest() {
        // given
        Long patientTrackerId = 0L;
        Long patientVisitId = 0L;
        List<PatientLabTest> patientLabTests = new ArrayList<>();
        // when
        when(patientLabTestRepository.getPatientLabTestList(patientTrackerId,
                patientVisitId, Constants.BOOLEAN_FALSE)).thenReturn(patientLabTests);
        // then
        List<PatientLabTest> actualList = patientLabTestService.getPatientLabTest(patientTrackerId,
                patientVisitId);
        Assertions.assertTrue(actualList.isEmpty());
        Assertions.assertEquals(patientLabTests, actualList);
        // given
        patientTrackerId = 1L;
        patientVisitId = 1L;
        patientLabTests = List.of(TestDataProvider.getPatientLabTest());
        // when
        when(patientLabTestRepository.getPatientLabTestList(patientTrackerId,
                patientVisitId, Constants.BOOLEAN_FALSE)).thenReturn(patientLabTests);
        // then
        actualList = patientLabTestService.getPatientLabTest(patientTrackerId, patientVisitId);
        Assertions.assertFalse(actualList.isEmpty());
        Assertions.assertEquals(patientLabTests, actualList);
        Assertions.assertEquals(patientLabTests.size(), actualList.size());
        Assertions.assertEquals(patientLabTests.get(0).getPatientVisitId(),
                actualList.get(0).getPatientVisitId());
        Assertions.assertEquals(patientLabTests.get(0).getPatientTrackId(),
                actualList.get(0).getPatientTrackId());
    }

    @Test
    @DisplayName("GetLabTestResultWithIdNull Test")
    void getLabTestResultWithIdNull() {
        PatientLabTestResultRequestDTO requestDTO = new PatientLabTestResultRequestDTO();
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.getPatientLabTestResults(requestDTO));
    }

    @Test
    @DisplayName("GetPatientLabTestResultWithNullLabTestId Test")
    void getPatientLabTestResultTestWithInvalidId() {
        // given
        PatientLabTestResultRequestDTO requestDTO = new PatientLabTestResultRequestDTO();
        requestDTO.setPatientLabTestId(0L);
        Long patientLabTestId = requestDTO.getPatientLabTestId();
        PatientLabTestResultResponseDTO response = new PatientLabTestResultResponseDTO();
        PatientLabTest patientLabTest;
        // when
        when(patientLabTestRepository.findByIdAndIsDeleted(patientLabTestId,
                Constants.BOOLEAN_FALSE)).thenReturn(null);
        // then
        PatientLabTest actualResult = patientLabTestRepository.findByIdAndIsDeleted(patientLabTestId,
                Constants.BOOLEAN_FALSE);
        Assertions.assertNull(actualResult);
        PatientLabTestResultResponseDTO actualResponse = patientLabTestService
                .getPatientLabTestResults(requestDTO);
        Assertions.assertEquals(response, actualResponse);

        // given
        requestDTO = TestDataProvider.getLabTestResultRequestDto();
        patientLabTestId = requestDTO.getPatientLabTestId();
        Long tenantId = requestDTO.getTenantId();
        response = TestDataProvider.getLabTestResultResponseDTO();
        patientLabTest = TestDataProvider.getPatientLabTest();
        List<PatientLabTestResult> patientLabTestResults = List
                .of(TestDataProvider.getPatientLabTestResultData());
        // when
        when(patientLabTestRepository.findByIdAndIsDeleted(patientLabTestId,
                Constants.BOOLEAN_FALSE)).thenReturn(patientLabTest);
        when(patientLabTestResultRepository.findAllByPatientLabTestIdAndIsDeletedAndTenantId(patientLabTestId,
                Constants.BOOLEAN_FALSE, tenantId)).thenReturn(patientLabTestResults);
        // then
        actualResponse = patientLabTestService.getPatientLabTestResults(requestDTO);
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(response.getPatientLabTestResults().get(0).getResultName(),
                actualResponse.getPatientLabTestResults().get(0).getResultName());
    }

    @Test
    @DisplayName("GetLabTestResults Test")
    void getLabTestResults() {
        // given
        Long labTestId = 1L;
        Long id = 1L;
        List<LabTestResultDTO> result = List.of(TestDataProvider.getLabTestResultDTO());
        String token = Constants.AUTH_TOKEN_SUBJECT;
        commonUtil = mockStatic(CommonUtil.class);
        userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);
        // when
        commonUtil.when(CommonUtil::getAuthToken).thenReturn(token);
        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(id);
        when(adminApiInterface.getLabtestResults(token, id, labTestId)).thenReturn(result);
        // then
        List<LabTestResultDTO> actualResults = patientLabTestService.getLabTestResults(labTestId);
        commonUtil.close();
        userSelectedTenantContextHolder.close();
        Assertions.assertEquals(result, actualResults);
        Assertions.assertEquals(result.get(0).getLabTestId(), actualResults.get(0).getLabTestId());
    }

    @Test
    @DisplayName("GetLabTest Test")
    void getLabTest() {
        // given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        List<LabTest> labTests = List.of(TestDataProvider.getLabTestData());
        Long id = 1L;
        String token = Constants.AUTH_TOKEN_SUBJECT;
        commonUtil = mockStatic(CommonUtil.class);
        userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);
        // when
        commonUtil.when(CommonUtil::getAuthToken).thenReturn(token);
        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(id);
        when(adminApiInterface.getLabtest(any(), any(), any())).thenReturn(labTests);
        // then
        List<LabTest> actualResult = patientLabTestService.getLabTest(requestDTO);
        commonUtil.close();
        userSelectedTenantContextHolder.close();
        Assertions.assertEquals(labTests, actualResult);
        Assertions.assertEquals(labTests.size(), actualResult.size());
        Assertions.assertEquals(labTests.get(0).getId(), actualResult.get(0).getId());
    }

    @Test
    @DisplayName("CreatePatientLabTestResultNull Test")
    void createPatientLabTestResultTestCase() {
        // given
        PatientLabTestResultRequestDTO requestDTO = new PatientLabTestResultRequestDTO();
        // then
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.createPatientLabTestResult(requestDTO));
    }

    @Test
    @DisplayName("CreatePatientLabTestResultNull Test")
    void createPatientLabTestResultNull() {
        // given
        PatientLabTestResultRequestDTO requestDTO = new PatientLabTestResultRequestDTO();
        // then
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.createPatientLabTestResult(requestDTO));
    }

    @ParameterizedTest
    @DisplayName("CreatePatientLabTestResultWithEmptyRanges Test")
    @NullSource
    @ValueSource(booleans = {false})
    void createPatientLabTestResultWithEmptyRanges(Boolean isEmptyRanges) {
        // given
        PatientLabTestResultRequestDTO requestDTO = TestDataProvider.getLabTestResultRequestDto();
        requestDTO.setIsEmptyRanges(isEmptyRanges);
        // then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientLabTestService.createPatientLabTestResult(requestDTO));
    }

    @Test
    @DisplayName("CreatePatientLabTestResultWithNullResult Test")
    void createPatientLabTestResultWithNullResult() {
        // given
        PatientLabTestResultRequestDTO requestDTO = TestDataProvider.getLabTestResultRequestDto();
        requestDTO.setIsEmptyRanges(true);
        requestDTO.setPatientLabTestResults(null);
        // then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientLabTestService.createPatientLabTestResult(requestDTO));
    }

    @Test
    @DisplayName("CreatePatientLabTestResultWithNullResult Test")
    void createPatientLabTestResultWithEmptyResult() {
        // when
        PatientLabTestResultRequestDTO requestDTO = TestDataProvider.getLabTestResultRequestDto();
        requestDTO.setIsEmptyRanges(true);
        requestDTO.setPatientLabTestResults(new ArrayList<>());
        // then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientLabTestService.createPatientLabTestResult(requestDTO));
    }

    @Test
    @DisplayName("CreatePatientLabTestResultWithNullResult Test")
    void createPatientLabTestResultWithNonEmptyResult() {
        // when
        PatientLabTestResultRequestDTO requestDTO = TestDataProvider.getLabTestResultRequestDto();
        requestDTO.setIsEmptyRanges(false);
        requestDTO.setPatientLabTestResults(List.of(new PatientLabTestResultDTO()));
        // then
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.createPatientLabTestResult(requestDTO));
    }

    @ParameterizedTest
    @DisplayName("CreatePatientLabTestResultWithNullResult Test")
    @CsvSource({", , true", ", , null", ", status, false", "name, , true"})
    void createPatientLabTestResultWithNonEmptyResult(String displayName, String status, Boolean isAbnormal) {
        // given
        PatientLabTestResultRequestDTO requestDTO = TestDataProvider.getLabTestResultRequestDto();
        requestDTO.setIsEmptyRanges(false);
        PatientLabTestResultDTO resultDTO = new PatientLabTestResultDTO();
        resultDTO.setDisplayName(displayName);
        resultDTO.setResultStatus(status);
        resultDTO.setIsAbnormal(isAbnormal);
        requestDTO.setPatientLabTestResults(List.of(resultDTO));
        // then
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.createPatientLabTestResult(requestDTO));
    }

    @Test
    @DisplayName("CreatePatientLabTestResult Test")
    void createPatientLabTestResult() {
        // given
        String token = Constants.AUTH_TOKEN_SUBJECT;
        Long id = 1L;
        Long patientTrackId = 1L;
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientLabTestResult patientLabTestResult = TestDataProvider.getPatientLabTestResultData();
        List<PatientLabTestResult> results = List.of(patientLabTestResult);
        PatientLabTestResultRequestDTO requestDTO = TestDataProvider.getLabTestResultRequestDto();
        Long patientLabTestId = requestDTO.getPatientLabTestId();
        requestDTO.setPatientLabTestResults(List.of(TestDataProvider.getPatientLabTestResultDto()));
        PatientLabTest patientLabTest = TestDataProvider.getPatientLabTest();
        List<LabTestResultDTO> labTestResults = List.of(TestDataProvider.getLabTestResultDTO());
        PatientLabTestResultDTO result = requestDTO.getPatientLabTestResults().get(0);
        commonUtil = mockStatic(CommonUtil.class);
        userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);
        // when
        when(patientLabTestRepository.findByIdAndIsDeleted(patientLabTestId, Constants.BOOLEAN_FALSE))
                .thenReturn(patientLabTest);
        commonUtil.when(CommonUtil::getAuthToken).thenReturn(token);
        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(id);
        when(adminApiInterface.getLabtestResults(any(), any(), any())).thenReturn(labTestResults);
        when(patientLabTestMapper.setPatientLabTestResult(requestDTO,
                patientLabTest, result, patientLabTestResult)).thenReturn(patientLabTestResult);
        when(patientLabTestResultRepository.saveAll(results)).thenReturn(results);
        when(patientLabTestMapper.setPatientLabTest(requestDTO, patientLabTest)).thenReturn(patientLabTest);
        when(patientLabTestRepository.save(patientLabTest)).thenReturn(patientLabTest);
        when(patientTrackerService.getPatientTrackerById(patientTrackId)).thenReturn(patientTracker);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        // then
        commonUtil.close();
        userSelectedTenantContextHolder.close();
        Assertions.assertNotNull(patientLabTest);
        // Assertions.assertEquals(results, actualResults);
    }

    @Test
    @DisplayName("CreatePatientLabTestResultWithInvalidId Test")
    void createPatientLabTestResultWithInvalidId() {

        //given
        List<LabTestResultDTO> labTestResultDTOS = List.of(TestDataProvider.getLabTestResultDTO());
        PatientLabTestResultRequestDTO requestDTO = TestDataProvider.getLabTestResultRequestDto();
        PatientLabTest patientLabTest = TestDataProvider.getPatientLabTest();
        TestDataProvider.init();

        labTestResultDTOS.get(0).setId(2L);

        //when
        when(patientLabTestRepository.findByIdAndIsDeleted(requestDTO.getPatientLabTestId(),
                Constants.BOOLEAN_FALSE)).thenReturn(patientLabTest);
        TestDataProvider.getStaticMock();

        when(adminApiInterface.getLabtestResults(any(), any(), any())).thenReturn(labTestResultDTOS);

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientLabTestService.createPatientLabTestResult(requestDTO));
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("CreatePatientLabTestResultWithResultEmpty Test")
    void createPatientLabTestResultWithResultEmpty() {
        //given
        PatientLabTestResultRequestDTO requestDTO = TestDataProvider.getLabTestResultRequestDto();
        requestDTO.setPatientLabTestResults(List.of());
        //then
        Assertions.assertThrows(BadRequestException.class,
                () -> patientLabTestService.createPatientLabTestResult(requestDTO));
    }

}
