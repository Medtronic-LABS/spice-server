package com.mdtlabs.coreplatform.spiceservice.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PhysicalExamination;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This is the repository which acts as link between server side and database.
 * This class is used to perform all the complaints module action in database.
 * In query annotation (nativeQuery = true), the below query perform like SQL.
 * Otherwise it performs like HQL.
 * </p>
 *
 * @author Karthick Murugesan
 * @since Jun 30, 2022
 */
@Repository
public interface PhysicalExaminationRepository extends GenericRepository<PhysicalExamination> {

    /**
     * <p>
     * This function returns a list of active physical examinations that have not been deleted.
     * </p>
     * 
     * @return {@link List<PhysicalExamination>} This method is returning a list of objects of type PhysicalExamination that have the
     * "isDeleted" property set to false and the "isActive" property set to true.
     */
    List<PhysicalExamination> findByIsDeletedFalseAndIsActiveTrue();

    /**
     * <p>
     * This function retrieves a set of active and non-deleted physical examinations by their IDs.
     * </p>
     * 
     * @param ids ids is a Set of Long values representing the IDs of PhysicalExamination objects that
     * we want to retrieve from the database
     * @return {@link Set<PhysicalExamination>} This method is returning a Set of PhysicalExamination objects that match the given set
     * of ids, are not marked as deleted (isDeleted is false), and are marked as active (isActive is
     * true).
     */
    Set<PhysicalExamination> findByIdInAndIsDeletedFalseAndIsActiveTrue(Set<Long> ids);
}
