package com.mdtlabs.coreplatform.notificationservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;

/**
 * <p>
 * SmsTemplateRepository is a Java interface for a repository that communicates
 * with a database to perform CRUD operations on `SMSTemplate` entities.
 * </p>
 *
 * @author VigneshKumar created on Oct 16, 2020
 */
@Repository
public interface SmsTemplateRepository extends JpaRepository<SMSTemplate, Long> {

    String GET_SMS_TEMPLATE = "FROM SMSTemplate as st WHERE st.type in :list";

    /**
     * <p>
     * This method is used to get a list of SMS templates based on a list of given SMSTemplate types.
     * </p>
     *
     * @param list {@link List<String>} The list of SMS template types that need to be found is given
     * @return {@link List<SMSTemplate>} A list of SMSTemplate for the given list of SMS template
     * types is retrieved from the database and returned
     */
    @Query(value = GET_SMS_TEMPLATE)
    List<SMSTemplate> getSMSTemplates(List<String> list);

    /**
     * <p>
     * This method is used to get an SMSTemplate by its type.
     * </p>
     *
     * @param templateType {@link String} The type of SMS template that needs to be found is given
     * @return {@link SMSTemplate} The SMSTemplate for the given template type is retrieved from the
     * database and returned
     */
    SMSTemplate findByType(String templateType);
}
