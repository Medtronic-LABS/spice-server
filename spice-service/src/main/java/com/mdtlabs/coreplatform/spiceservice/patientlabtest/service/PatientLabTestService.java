package com.mdtlabs.coreplatform.spiceservice.patientlabtest.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;

import java.util.List;

/**
 * <p>
 * This an interface class for user module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface PatientLabTestService {

    /**
     * <p>
     * This method is to save a list of Patient lab test entities.
     * </p>
     *
     * @param patientLabTest {@link PatientLabTestRequestDTO} patient lab test request dto
     * @return List {@link List<PatientLabTest>} list  of patient lab test entity
     */
    List<PatientLabTest> createPatientLabTest(PatientLabTestRequestDTO patientLabTest);

    /**
     * <p>
     * Fetches the patient lab test information.
     * </p>
     *
     * @param requestData {@link GetRequestDTO} get request dto
     * @return PatientLabTestResponseDTO {@link PatientLabTestResponseDTO} patient lab test response dto
     */
    PatientLabTestResponseDTO getPatientLabTestList(GetRequestDTO requestData);

    /**
     * <p>
     * This method is used to remove the patientLabTest from the database.
     * </p>
     *
     * @param requestData {@link GetRequestDTO} get request dto
     * @return Boolean {@link boolean} true or false
     */
    boolean removePatientLabTest(GetRequestDTO requestData);

    /**
     * <p>
     * Reviews the patient lab test information.
     * </p>
     *
     * @param requestData {@link GetRequestDTO} get request dto
     */
    void reviewPatientLabTest(GetRequestDTO requestData);

    /**
     * <p>
     * Create patient lab test results information.
     * </p>
     *
     * @param requestData {@link PatientLabTestResultRequestDTO} patient lab test result request dto
     * @return List {@link List<PatientLabTestResult>} list of patient lab test result
     */
    List<PatientLabTestResult> createPatientLabTestResult(PatientLabTestResultRequestDTO requestData);

    /**
     * <p>
     * Get patient lab test details with result.
     * </p>
     *
     * @param requestData {@link PatientLabTestResultRequestDTO} patient lab test result request dto
     * @return PatientLabTestResultResponseDTO {@link PatientLabTestResultResponseDTO} patient lab test result response dto
     */
    PatientLabTestResultResponseDTO getPatientLabTestResults(PatientLabTestResultRequestDTO requestData);

    /**
     * <p>
     * Get list of Patient lab test.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @param patientVisitId {@link Long} patient visit id
     * @return List {@link List<PatientLabTest>} patient lab test list
     */
    List<PatientLabTest> getPatientLabTest(Long patientTrackId, Long patientVisitId);

    /**
     * <p>
     * Used to get lab test count.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @return int {@link int} count
     */
    int getLabTestCount(Long patientTrackId);

    /**
     * <p>
     * Gets list of lab test information.
     * </p>
     *
     * @param requestData {@link RequestDTO} request dto
     * @return List {@link List<LabTest>} list of lab test
     */
    List<LabTest> getLabTest(RequestDTO requestData);

    /**
     * <p>
     * Gets list of lab test results information.
     * </p>
     *
     * @param labTestId {@link long} lab test id
     * @return List {@link List<LabTestResultDTO>} list of lab test result
     */
    List<LabTestResultDTO> getLabTestResults(Long labTestId);

    /**
     * <p>
     * To remove lab test details based on tracker id.
     * </p>
     *
     * @param trackerId {@link long} trackerId
     */
    void removeLabTest(long trackerId);
}
