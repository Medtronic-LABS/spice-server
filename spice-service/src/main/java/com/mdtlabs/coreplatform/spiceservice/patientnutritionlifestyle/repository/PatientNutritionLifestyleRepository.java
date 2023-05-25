package com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientNutritionLifestyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * This is the repository class for communication link between the server and
 * database. This class is used to perform all the Patient Nutrition Lifestyle
 * actions in database. By default, value for the annotation - nativeQuery is
 * FALSE (@nativeQuery) and acts like an HQL query. If nativeQuery is explicitly
 * mentioned TRUE, its acts like an SQL query.
 * </p>
 *
 * @author Victor Jefferson
 */
@Repository
@Transactional
public interface PatientNutritionLifestyleRepository extends JpaRepository<PatientNutritionLifestyle, Long> {

    String GET_PATIENT_NUTRITION_LIFESTYLE_BY_IDS = "select patientNutritionLifestyle from PatientNutritionLifestyle as patientNutritionLifestyle where patientNutritionLifestyle.id in (:patientNutritionLifestyleIds) and patientNutritionLifestyle.patientTrackId = :patientTrackId and patientNutritionLifestyle.isDeleted=false";

    String GET_NUTRITION_LIFESTYLE_COUNT = "select count(id) from PatientNutritionLifestyle as lifestyle where lifestyle.isDeleted=false and lifestyle.assessedBy is not null and lifestyle.isViewed=false and lifestyle.patientTrackId=:patientTrackId";

    String GET_PATIENT_NUTRITION_LIFESTYLES = "select lifestyle from PatientNutritionLifestyle as lifestyle where lifestyle.patientTrackId=:patientTrackId AND "
            +
            "(:isNutritionHistoryRequired is null or lifestyle.assessedBy is not null) AND (:isNutritionist is null or lifestyle.assessedBy is null) AND (:isViewed is null or lifestyle.isViewed=:isViewed) AND lifestyle.tenantId=:tenantId AND lifestyle.isDeleted=false order by lifestyle.updatedBy desc";

    /**
     * <p>
     * This method used to get Patient Nutrition Lifestyles using List of ids.
     * </p>
     *
     * @param patientNutritionLifestyleIds {@link List<Long>} patientNutritionLifestyleIds
     * @return {@link List<PatientNutritionLifestyle>} List of PatientNutritionLifestyle Entity
     */
    @Query(value = GET_PATIENT_NUTRITION_LIFESTYLE_BY_IDS)
    List<PatientNutritionLifestyle> getPatientNutritionLifestyleByIds(
            @Param("patientNutritionLifestyleIds") List<Long> patientNutritionLifestyleIds,
            @Param("patientTrackId") Long patientTrackId);

    /**
     * <p>
     * This method used to get Patient Nutrition Lifestyles review count .
     * </p>
     *
     * @param patientTrackId {@link Long} patientTrackId
     * @return int {@link int} count
     */
    @Query(value = GET_NUTRITION_LIFESTYLE_COUNT)
    int getNutritionLifestyleReviewedCount(@Param("patientTrackId") Long patientTrackId);

    /**
     * <p>
     * Gets Patient Nutrition Lifestyles by id and patientTrackId
     * </p>
     *
     * @param id             {@link Long} id
     * @param patientTrackId {@link Long} patientTrackId
     * @return {@link PatientNutritionLifestyle} PatientNutritionLifestyle
     */
    PatientNutritionLifestyle findByIdAndPatientTrackId(Long id, Long patientTrackId);

    /**
     * <p>
     * Gets Patient Nutrition Lifestyles by patientTrackId and tenantId
     * </p>
     *
     * @param patientTrackId             {@link Long} patientTrackId
     * @param tenantId                   {@link Long} tenantId
     * @param isNutritionHistoryRequired {@link Boolean} isNutritionHistoryRequired
     * @param isNutritionist             {@link Boolean} isNutritionist
     * @param isViewed                   {@link Boolean} isViewed
     * @return {@link List<PatientNutritionLifestyle>} List of PatientNutritionLifestyle Entity
     */
    @Query(value = GET_PATIENT_NUTRITION_LIFESTYLES)
    List<PatientNutritionLifestyle> getPatientNutritionLifestyles(@Param("patientTrackId") Long patientTrackId,
                                                                  @Param("tenantId") Long tenantId, @Param("isNutritionHistoryRequired") Boolean isNutritionHistoryRequired,
                                                                  @Param("isNutritionist") Boolean isNutritionist, @Param("isViewed") Boolean isViewed);


    /**
     * <p>
     * Get list of PatientNutritionLifestyle by patientTracker.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id
     * @return {@link List<PatientNutritionLifestyle>} PatientNutritionLifestyle entity
     */
    List<PatientNutritionLifestyle> findByPatientTrackId(long patientTrackId);
}
