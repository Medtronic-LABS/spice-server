package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * The OrganizationDTO class is a data transfer object that contains information about an organization,
 * its users, roles, and other related data.
 * </p>
 */
@Data
public class OrganizationDTO {

    private Organization organization;

    private List<User> users;

    private List<String> roles;
    private boolean isSiteOrganization;
    private Long formDataId;
    private String formName;
    private String name;
    private Long sequence;
    private Long parentOrganizationId;
    private Long id;
    private Long tenantId;
    private Long createdBy = getUserValue();
    private Long updatedBy = getUserValue();
    private Date createdAt;
    private Date updatedAt;
    private boolean isActive;
    private boolean isDeleted;

    /**
     * <p>
     * This is a constructor for the OrganizationDTO class that takes in an Organization object, a list
     * of User objects, a list of String objects, and a Boolean value
     * </p>
     */
    public OrganizationDTO(Organization organization, List<User> users, List<String> roles, Boolean isSiteOrganization) {
        super();
        this.organization = organization;
        this.users = users;
        this.roles = roles;
        this.isSiteOrganization = isSiteOrganization;
    }

    public OrganizationDTO() {
    }

    /**
     * <p>
     * This method is used to get user value
     * </p>
     *
     * @return String - user value
     */
    @JsonIgnore
    public long getUserValue() {
        if (null != UserContextHolder.getUserDto()) {
            return UserContextHolder.getUserDto().getId();
        }
        return Constants.ZERO;
    }
}
