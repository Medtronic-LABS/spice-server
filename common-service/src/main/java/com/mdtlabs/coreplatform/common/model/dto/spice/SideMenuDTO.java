package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Map;

/**
 * <p>
 * This class is an DTO class for side menu entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 17, 2023
 */
@Data
public class SideMenuDTO {
    private String roleName;

    private Map<String, String> menus;
}
