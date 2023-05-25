package com.mdtlabs.coreplatform.userservice.message;

/**
 * <p>
 * Success code to fetch message from property. Property
 * file(application.property) present in resource folder.
 * </p>
 *
 * @author Vigneshkumar created on Jun 30, 2022
 */
public enum SuccessCode {

    USER_SAVE(1001),
    USER_UPDATE(1002),
    GET_USERS(1003),
    GET_USER(1004),
    USER_DELETE(1005),
    USER_NOT_SAVED(1007),
    USER_NOT_FOUND(1008),
    USER_UNLOCK(1017),
    API_PERMISSION_CLEARED(1015),
    GET_SITE(1150),
    ASSIGN_ROLE(1018),
    ROLE_SAVE_ERROR(2001),
    GET_ROLES_ERROR(2002),
    ROLE_UPDATE_ERROR(2003),
    ROLE_DELETE_ERROR(2004),
    ROLE_SAVE(2005),
    ROLE_UPDATE(2006),
    GET_ROLES(2007),
    GET_ROLE(2008),
    ROLE_DELETE(2009),
    SEND_EMAIL_USING_SMTP(3003),
    ORGANIZATION_UPDATE_ERROR(4003),
    ORGANIZATION_SAVE(4005),
    ORGANIZATION_UPDATE(4006),
    FORGOT_LIMIT_EXCEEDED(4010),
    GET_SUPER_ADMIN_USER(5001),
    REMOVE_SUPER_ADMIN_USER(5002),
    UPDATE_SUPER_ADMIN_USER(5003),
    SET_PASSWORD(5006),
    CREATE_SUPER_ADMIN_USER(5004),
    CHANGE_SITE_USER_PASSWORD(5010);

    private final int key;

    SuccessCode(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}
