package com.mdtlabs.coreplatform.notificationservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.model.entity.OutBoundEmail;

/**
 * <p>
 * OutBoundEmailRepository is a java interface that extends the `JpaRepository` interface.
 * It provides methods for performing CRUD operations on the `OutBoundEmail` entity.
 * </p>
 *
 * @author VigneshKumar created on Oct 16, 2020
 */
@Repository
public interface OutBoundEmailRepository extends JpaRepository<OutBoundEmail, Long> {

    String GET_OUTBOUND_MAILS = "select mail from OutBoundEmail as mail"
            + " where mail.isProcessed=false and mail.retryAttempts < :emailRetryAttempts";

    /**
     * <p>
     * This method is used to get all outbound emails with a specified number of email retry attempts.
     * </p>
     *
     * @param emailRetryAttempts The value that denotes the number of retry attempts made for sending
     *                           the emails is given
     * @return {@link List<OutBoundEmail>} A list of outbound emails is filtered for the given retry attempts
     * and retrieved from the database
     */
    @Query(value = GET_OUTBOUND_MAILS)
    List<OutBoundEmail> getAllMail(@Param("emailRetryAttempts") int emailRetryAttempts);
}
