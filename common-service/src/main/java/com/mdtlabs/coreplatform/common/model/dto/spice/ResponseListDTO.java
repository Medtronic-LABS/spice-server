package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * This class is a Data transfer Object for List of Objects.
 * </p>
 *
 * @author Niraimathi S created on Feb 07, 2023
 */
@Data
public class ResponseListDTO {

    private Object data;

    private Long totalCount;

    public ResponseListDTO(Object data, Long totalCount) {
        this.data = data;
        this.totalCount = totalCount;
    }

    public ResponseListDTO() {

    }

    public List<Object> getData() {
        return (List<Object>) data;
    }


}
