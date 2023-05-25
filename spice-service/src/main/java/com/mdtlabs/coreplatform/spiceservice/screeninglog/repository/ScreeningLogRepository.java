package com.mdtlabs.coreplatform.spiceservice.screeninglog.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * ScreeningLogRepository is a Java interface that extends the JpaRepository interface and defines methods for
 * accessing and manipulating data in the ScreeningLog table of a database.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface ScreeningLogRepository extends JpaRepository<ScreeningLog, Long> {

    /**
     * This method is used to find a screening log by its ID, checking if it is deleted and the latest version.
     *
     * @param id        The ID of the screening log is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the screening log has been marked as deleted or not is given
     * @param isLatest  The  boolean value that indicates whether the ScreeningLog being searched
     *                  for is the latest version or not is given
     * @return {@link ScreeningLog} The screening log for the given ID is retrieved from the database
     */
    ScreeningLog findByIdAndIsDeletedAndIsLatest(long id, boolean isDeleted, boolean isLatest);


    /**
     * This method is used to find a screening log by its ID, ensuring that it is not deleted and is the latest version.
     *
     * @param id The ID of the screening log is given
     * @return {@link ScreeningLog} The screening log for the given ID is retrieved from the database
     */
    ScreeningLog findByIdAndIsDeletedFalseAndIsLatestTrue(long id);
}
