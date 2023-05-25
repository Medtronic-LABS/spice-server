package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 * This class is for mapping the patient lab test related information based on
 * the required condition check and set to entity.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Component
public class PatientLabTestMapper {

    /**
     * <p>
     * The function sets the values of a PatientLabTestDTO object based on the values of a
     * PatientLabTest object.
     * </p>
     *
     * @param test            {@link PatientLabTest} instance of the PatientLabTest class, which contains information about a
     *                        patient's lab test is given
     * @param labTestResponse {@link PatientLabTestDTO} an object of type PatientLabTestDTO that will be populated with data from
     *                        the test object is given
     * @return {@link PatientLabTestDTO} The method is returning a PatientLabTestDTO object is returned
     */
    public PatientLabTestDTO setPatientLabTestDto(PatientLabTest test, PatientLabTestDTO labTestResponse) {
        labTestResponse.setId(test.getId());
        labTestResponse.setReferredDate(test.getCreatedAt());
        labTestResponse.setIsAbnormal(test.getIsAbnormal());
        labTestResponse.setIsReviewed(test.getIsReviewed());
        labTestResponse.setResultComments(test.getComment());
        labTestResponse.setLabTestId(test.getLabTestId());
        labTestResponse.setLabTestName(test.getLabTestName());
        labTestResponse.setPatientVisitId(test.getPatientVisitId());
        labTestResponse.setResultDate(test.getResultDate());
        return labTestResponse;
    }

    /**
     * <p>
     * This function sets the values of a PatientLabTestResult object based on the values of other
     * objects and DTOs.
     * </p>
     *
     * @param requestData          {@link PatientLabTestResultRequestDTO} an object of type PatientLabTestResultRequestDTO which contains the request
     *                             data for setting the patient lab test result is given
     * @param patientLabTest       {@link PatientLabTest} an object of type PatientLabTest which contains information about the lab
     *                             test performed on the patient is given
     * @param result               {@link PatientLabTestResultDTO} The "result" parameter is an object of type "PatientLabTestResultDTO" which
     *                             contains the details of the lab test result for a patient is given
     * @param patientLabTestResult {@link PatientLabTestResult} An object of type PatientLabTestResult that needs to be updated with
     *                             the values from the other parameters is given
     * @return {@link PatientLabTestResult} The method is returning a PatientLabTestResult object is returned
     */
    public PatientLabTestResult setPatientLabTestResult(PatientLabTestResultRequestDTO requestData,
                                                        PatientLabTest patientLabTest, PatientLabTestResultDTO result, PatientLabTestResult patientLabTestResult) {
        patientLabTestResult.setPatientLabTestId(requestData.getPatientLabTestId());
        patientLabTestResult.setLabTestId(patientLabTest.getLabTestId());
        patientLabTestResult.setPatientVisitId(patientLabTest.getPatientVisitId());
        patientLabTestResult.setPatientTrackId(patientLabTest.getPatientTrackId());
        patientLabTestResult.setDisplayName(result.getDisplayName());
        patientLabTestResult.setResultName(result.getName());
        patientLabTestResult.setUnit(result.getUnit());
        patientLabTestResult.setTenantId(requestData.getTenantId());
        patientLabTestResult.setIsAbnormal(
                Objects.isNull(result.getIsAbnormal()) ? Constants.BOOLEAN_FALSE : result.getIsAbnormal());
        patientLabTestResult.setLabTestResultId(Long.parseLong(result.getId()));
        patientLabTestResult.setDisplayOrder(result.getDisplayOrder());
        patientLabTestResult.setResultStatus(result.getResultStatus());
        patientLabTestResult.setResultValue(result.getResultValue());
        return patientLabTestResult;
    }

    /**
     * <p>
     * The function sets various properties of a PatientLabTest object based on the values provided in
     * a PatientLabTestResultRequestDTO object.
     * </p>
     *
     * @param requestData    {@link PatientLabTestResultRequestDTO} an object of type PatientLabTestResultRequestDTO that contains the data
     *                       needed to update a patient's lab test result is given
     * @param patientLabTest {@link PatientLabTest} an instance of the PatientLabTest class that needs to be updated with the
     *                       values from the requestData object is given
     * @return {@link PatientLabTest} The method is returning a PatientLabTest object is returned
     */
    public PatientLabTest setPatientLabTest(PatientLabTestResultRequestDTO requestData, PatientLabTest patientLabTest) {
        patientLabTest.setResultDate(requestData.getTestedOn());
        patientLabTest.setIsReviewed(requestData.getIsReviewed());
        patientLabTest.setComment(requestData.getComment());
        patientLabTest.setTenantId(requestData.getTenantId());
        patientLabTest.setResultUpdateBy(UserContextHolder.getUserDto().getId());
        patientLabTest.setIsAbnormal(requestData.getIsAbnormal());
        return patientLabTest;
    }
}
