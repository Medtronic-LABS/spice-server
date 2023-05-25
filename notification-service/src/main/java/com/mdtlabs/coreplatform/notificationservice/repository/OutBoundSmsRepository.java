package com.mdtlabs.coreplatform.notificationservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;

/**
 * <p>
 * OutBoundSmsRepository is a java interface that extends the `JpaRepository`
 * interface for the `OutBoundSMS` entity.
 * </p>
 *
 * @author VigneshKumar created on Oct 16, 2020
 */
@Repository
public interface OutBoundSmsRepository extends JpaRepository<OutBoundSMS, Long> {

    String GET_OUTBOUND_SMS = "From OutBoundSMS outBoundSMS where "
            + "outBoundSMS.isProcessed=false and outBoundSMS.retryAttempts < :smsRetryAttempts";

    /**
     * <p>
     * This method is used to get all outbound SMS messages with a specified number of retry attempts.
     * </p>
     *
     * @param smsRetryAttempts The value that denotes the number of retry attempts made for sending the SMS is given
     * @return {@link List<OutBoundSMS>} A list of OutBoundSMS is filtered for the given retry attempts and retrieved
     * from the database
     */
    @Query(value = GET_OUTBOUND_SMS)
    List<OutBoundSMS> getAllSms(@Param("smsRetryAttempts") int smsRetryAttempts);
}
