package com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <p>
 * This repository class is responsible for communication between database and server side.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
public interface PatientLabTestResultRepository extends JpaRepository<PatientLabTestResult, Long> {

    /**
     * <p>
     * This function finds all patient lab test results by patient lab test ID, isDeleted flag, and
     * tenant ID.
     * </p>
     *
     * @param patientLabTestId {@link Long} This parameter is of type Long and represents the unique identifier of a
     *                         patient lab test is given
     * @param isDeleted        {@link Boolean } isDeleted is a boolean parameter that indicates whether the patient lab test
     *                         result has been marked as deleted or not. If the value is true, it means that the patient lab
     *                         test result has been deleted and should not be included in the search results
     * @param tenantId         {@link Long} The tenantId parameter is a Long value that represents the unique identifier of
     *                         the tenant or organization that the patient lab test result belongs to
     * @return {@link List<PatientLabTestResult>} This method returns a list of PatientLabTestResult objects that match the given
     * patientLabTestId, isDeleted, and tenantId parameters.
     */
    List<PatientLabTestResult> findAllByPatientLabTestIdAndIsDeletedAndTenantId(Long patientLabTestId,
                                                                                Boolean isDeleted, Long tenantId);


    /**
     * <p>
     * This function finds patient lab test results by their track ID and whether they have been marked
     * as deleted.
     * </p>
     *
     * @param patientTrackId {@link long} patientTrackId is a unique identifier assigned to a patient's medical
     *                       record or history
     * @param isDeleted      {@link boolean} isDeleted is a boolean parameter that indicates whether the patient lab test
     *                       result has been marked as deleted or not is given
     * @return {@link List<PatientLabTestResult>} The method `findByPatientTrackIdAndIsDeleted` is returning a list of
     * `PatientLabTestResult` objects that match the given `patientTrackId` and `isDeleted` boolean
     * value.
     */
    List<PatientLabTestResult> findByPatientTrackIdAndIsDeleted(long patientTrackId, boolean isDeleted);

    /**
     * <p>
     * This function finds a list of lab test results for a given patient track ID.
     * </p>
     *
     * @param patientTrackId {@link long} patientTrackId is a unique identifier assigned to a patient's medical
     *                       record or history
     * @return {@link List<PatientLabTestResult>} The method is returning a list of PatientLabTestResult objects that match the given
     * patientTrackId.
     */
    List<PatientLabTestResult> findByPatientTrackId(long patientTrackId);
}
