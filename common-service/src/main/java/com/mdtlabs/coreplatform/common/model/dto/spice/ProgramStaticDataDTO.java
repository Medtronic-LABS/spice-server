package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.google.common.reflect.TypeToken;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Set;

/**
 * <p>
 * This class is an DTO class for Program entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 17, 2023
 */
@Data
public class ProgramStaticDataDTO {
    private Long id;

    private String name;

    private Set<Site> sites;

    private Long tenantId;

    /**
     * <p>
     * This Java function maps a set of sites to a set of ParentOrganizationDTO objects using
     * ModelMapper.
     * </p>
     *
     * @return {@link Set<ParentOrganizationDTO>} is being returned. The method uses a ModelMapper
     * to map the sites to a set of ParentOrganizationDTO objects.
     */
    public Set<ParentOrganizationDTO> getSites() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(sites, new TypeToken<Set<ParentOrganizationDTO>>() {
        }.getType());
    }
}
