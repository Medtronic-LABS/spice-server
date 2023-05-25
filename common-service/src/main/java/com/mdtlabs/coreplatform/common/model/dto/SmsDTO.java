package com.mdtlabs.coreplatform.common.model.dto;

import lombok.Data;

/**
 * <p>
 * This is an DTO containing necessary fields to SMS entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 08, 2023
 */
@Data
public class SmsDTO {
    private String body;

    private String subject;

    private String toPhoneNo;

    private String formName;

    private String userName;

    private long tenantId;

    private long formDataId;

    private long notificationId;

    /**
     * <p>
     * This is a constructor method for the SmsDTO class
     * </p>
     *
     * @param notificationId notificationId param is passed in the SmsDTO
     * @param toPhoneNo      toPhoneNo param is passed in the SmsDTO
     * @param tenantId       tenantId param is passed in the SmsDTO
     * @param formDataId     formDataId param is passed in the SmsDTO
     * @param userName       userName param is passed in the SmsDTO
     */
    public SmsDTO(String toPhoneNo, String userName, long tenantId, long formDataId, long notificationId) {
        this.toPhoneNo = toPhoneNo;
        this.userName = userName;
        this.tenantId = tenantId;
        this.formDataId = formDataId;
        this.notificationId = notificationId;
    }

    /**
     * <p>
     * This is a constructor method for the SmsDTO class
     * </p>
     */
    public SmsDTO() {
    }

    /**
     * <p>
     * This is a constructor method for the SmsDTO class
     * </p>
     *
     * @param body       body param is passed in the SmsDTO
     * @param toPhoneNo  toPhoneNo param is passed in the SmsDTO
     * @param tenantId   tenantId param is passed in the SmsDTO
     * @param formDataId formDataId param is passed in the SmsDTO
     */
    public SmsDTO(String body, String toPhoneNo, Long tenantId, Long formDataId) {
        super();
        this.body = body;
        this.toPhoneNo = toPhoneNo;
        this.tenantId = tenantId;
        this.formDataId = formDataId;
    }

}
