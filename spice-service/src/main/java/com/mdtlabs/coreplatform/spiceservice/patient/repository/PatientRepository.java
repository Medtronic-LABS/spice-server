package com.mdtlabs.coreplatform.spiceservice.patient.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the user module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface PatientRepository extends GenericRepository<Patient> {

    /**
     * The function finds a patient by their ID and checks if they have been deleted.
     *
     * @param id        {@link Long} The unique identifier of the patient that we want to retrieve from the database.
     * @param isDeleted {@link boolean} The "isDeleted" parameter is a boolean value that indicates whether the patient
     *                  record has been marked as deleted or not
     * @return {@link Patient} The method is returning a single instance of the `Patient` class that matches the given
     * `id` and `isDeleted` boolean value is returned
     */
    Patient findByIdAndIsDeleted(Long id, boolean isDeleted);

    /**
     * This function finds a patient by their ID, whether they are marked as deleted or not, and their
     * tenant ID.
     *
     * @param id        {@link Long} The unique identifier of the patient that we want to retrieve from the database.
     * @param isDeleted {@link boolean} The "isDeleted" parameter is a boolean value that indicates whether the patient
     *                  record has been marked as deleted or not
     * @param tenantId  {@link Long} The tenantId parameter is a Long value that represents the unique identifier of
     *                  the tenant for which the patient is associated with
     * @return {@link Patient} The method is returning a single object of type "Patient" that matches the given "id",
     * "isDeleted" and "tenantId" parameters is returned
     */
    Patient findByIdAndIsDeletedAndTenantId(Long id, boolean isDeleted, Long tenantId);

}
