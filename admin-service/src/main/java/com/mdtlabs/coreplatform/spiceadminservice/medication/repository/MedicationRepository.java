package com.mdtlabs.coreplatform.spiceadminservice.medication.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Medication;

/**
 * <p>
 * MedicationRepository is a Java interface for a Medication Repository that extends the JpaRepository interface.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    String MEDICATION_COUNTRY_DETAIL_BASE_QUERY = "select * from medication_country_detail as medication";
    String MEDICATION_BASE_QUERY = "select medication from Medication as medication";
    String GET_ALL_MEDICATIONS = MEDICATION_COUNTRY_DETAIL_BASE_QUERY + " where (:countryId is null or medication.country_id=:countryId) "
            + "and medication.tenant_id=:tenantId and medication.is_deleted=false AND medication.is_active=true "
            + "and (:searchTerm is null or lower(medication.medication_name) LIKE "
            + "CONCAT('%',lower(:searchTerm),'%'))";

    String GET_MEDICATION_BY_MEDICATION_NAME = MEDICATION_BASE_QUERY + " where lower(medication.medicationName) LIKE CONCAT('%',lower(:searchTerm),'%') "
            + " AND medication.countryId=:countryId AND medication.isDeleted=false AND medication.isActive=true"
            + " ORDER BY medication.medicationName ASC,medication.updatedAt DESC";

    String GET_MEDICATION_BY_MANDATORY_FIELDS = MEDICATION_BASE_QUERY + " where (:countryId is null or medication.countryId=:countryId)"
            + " AND (:medicationName is null or medication.medicationName=:medicationName) "
            + " AND (:classificationId is null or medication.classificationId=:classificationId)"
            + " AND (:brandId is null or medication.brandId=:brandId) AND (:dosageFormId is null or "
            + " medication.dosageFormId=:dosageFormId) AND medication.isDeleted=false AND medication.isActive=true"
            + " AND (:tenantId is null or medication.tenantId=:tenantId)";

    String GET_OTHER_MEDICATION = MEDICATION_COUNTRY_DETAIL_BASE_QUERY + " where medication.country_id = :countryId AND medication.brand_name = :brandName AND"
            + " medication.medication_name = :medicationName AND medication.classification_name = :classificationName "
            + " AND medication.dosage_form_name = :dosageFormName AND medication.is_deleted=false AND "
            + "medication.is_active=true limit 1";

    /**
     * <p>
     * This method is used to get page of non-deleted and active medications for the given search criteria.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param tenantId   {@link Long} The tenant ID for which the medication is being searched is given
     * @param countryId  {@link Long} The ID of the country associated with the medications that
     *                   are being searched is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<Medication>} A Page of non-deleted and active medications that match the search criteria with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_ALL_MEDICATIONS, nativeQuery = true)
    Page<Medication> getAllMedications(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                       @Param(Constants.COUNTRY_ID) Long countryId,
                                       @Param(Constants.TENANT_PARAMETER_NAME) Long tenantId, Pageable pageable);

    /**
     * <p>
     * This method is used to retrieve a medication for the given search criteria.
     * </p>
     *
     * @param classification {@link Long} The classification ID associated with the medication that
     *                       are being searched is given
     * @param brand          {@link Long} The brand ID associated with the medication that are being searched is given
     * @param dosageForm     {@link Long} The dosage form ID associated with the medication that
     *                       are being searched is given
     * @param country        {@link Long} The ID of the country associated with the medication that
     *                       are being searched is given
     * @param name           {@link String} The name of the medication that need to be searched is given
     * @return {@link Medication} The medication is returned for the given search criteria like brand ID, dosage form ID etc.
     */
    @Query(value = GET_MEDICATION_BY_MANDATORY_FIELDS)
    Medication getMedicationByFieldsAndTenantId(@Param("classificationId") long classification,
                                                @Param("brandId") long brand,
                                                @Param("dosageFormId") long dosageForm,
                                                @Param(Constants.COUNTRY_ID) long country,
                                                @Param("medicationName") String name,
                                                @Param("tenantId") Long tenantId);

    /**
     * <p>
     * This method is used to retrieve a non-deleted and active list of medications for the given search criteria.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param countryId  {@link Long} The ID of the country associated with the medication that
     *                   are being searched is given
     * @return {@link List<Medication>} The list of medications for the given search term and country ID is returned from the database
     */
    @Query(value = GET_MEDICATION_BY_MEDICATION_NAME)
    List<Medication> searchMedications(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                       @Param(Constants.COUNTRY_ID) Long countryId);

    /**
     * <p>
     * This method is used to retrieve a active medication for the given ID and tenant ID.
     * </p>
     *
     * @param id        {@link Long} The ID for which the medication is being searched is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the medication has been marked as deleted or not is given
     * @param tenantId  {@link Long} The tenant ID for which the medication is being searched is given
     * @return {@link Medication} The medication for the given ID and deletion status is returned from the database
     */
    Medication getMedicationByIdAndIsDeletedAndTenantIdAndIsActiveTrue(Long id, boolean isDeleted,
                                                                       Long tenantId);

    /**
     * <p>
     * This method is used to get non-deleted and active other medication details for the given search criteria.
     * </p>
     *
     * @param countryId          {@link Long} The ID of the country associated with the medication that
     *                           are being searched is given
     * @param classificationName {@link String} The classification name associated with the medication that
     *                           are being searched is given
     * @param brandName          {@link String} The brand name associated with the medication that are being searched is given
     * @param dosageFormName     {@link Long} The dosage form name associated with the medication that
     *                           are being searched is given
     * @param medicationName     {@link String} The name of the medication that need to be searched is given
     * @return {@link Medication} The medication for the given search criteria like country ID, medication name etc., is returned from the database
     */
    @Query(value = GET_OTHER_MEDICATION, nativeQuery = true)
    Medication getOtherMedication(@Param(Constants.COUNTRY_ID) long countryId,
                                  @Param("medicationName") String medicationName, @Param("brandName") String brandName,
                                  @Param("classificationName") String classificationName, @Param("dosageFormName") String dosageFormName);

    /**
     * <p>
     * This method is used to get a medication based on its id and tenantId.
     * </p>
     *
     * @param id       {@link Long} The ID for which the medication is being searched is given
     * @param tenantId {@link Long} The tenant ID for which the medication is being searched is given
     * @return The non-deleted medication for the given ID and tenant ID is retrieved and returned from the database
     */
    Medication findByIdAndIsDeletedFalseAndTenantId(Long id, Long tenantId);
}
