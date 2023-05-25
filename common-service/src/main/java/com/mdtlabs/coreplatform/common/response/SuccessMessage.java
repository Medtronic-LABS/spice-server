package com.mdtlabs.coreplatform.common.response;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * Generic success message object.
 * </p>
 *
 * @param <T> -
 * @author Vigneshkumar created on Jun 30, 2022
 */
@Data
public class SuccessMessage<T> {
    private String message;
    private Object entity;
    private boolean status;
    private List<T> entityList;
    private Integer responseCode;
    private Long totalCount;

    /**
     * <p>
     * Success message.
     * </p>
     *
     * @param status       status is passed in this attribute.
     * @param message       Message to be displayed to the user is passed in this
     *                      attribute.
     * @param entity        object is passed in this attribute.
     * @param entityList    {@link List<T>}list is passed in this attribute.
     * @param responseCode  response code is passed in this attribute.
     * @param totalCount    total count is passed in this attribute.
     */
    public SuccessMessage(boolean status, String message, Object entity, List<T> entityList, Integer responseCode,
                          Long totalCount) {
        this.setMessage(message);
        this.setEntity(entity);
        this.setEntityList(entityList);
        this.setStatus(status);
        this.setResponseCode(responseCode);
        this.setTotalCount(totalCount);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setEntityList(List<T> entityList) {
        this.entityList = entityList;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

}
