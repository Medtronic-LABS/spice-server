package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * The ProgramDetailsDTO class contains information about a program, including its ID, name, country,
 * tenant ID, and sets of sites and deleted sites.
 * </p>
 */
@Data
public class ProgramDetailsDTO {

    private Long id;

    private boolean isDeleted;

    private boolean isActive;

    private String name;

    private Country country;

    private Long tenantId;

    private Set<Site> sites;

    private Set<Site> deletedSites;

    /**
     * <p>
     * This function returns a set of SiteDTO objects by mapping each site object in the input stream
     * using ModelMapper.
     * </p>
     *
     * @return {@link Set<SiteDTO>} objects is being returned.
     */
    public Set<SiteDTO> getSites() {
        return sites.stream().map(site ->
                new ModelMapper().map(site, SiteDTO.class)
        ).collect(Collectors.toSet());
    }

    /**
     * This function returns a set of IDs of deleted sites by mapping the IDs of deleted sites from a
     * list of BaseEntity objects.
     *
     * @return {@link Set<Long>} deleted sites, obtained by mapping the IDs of the deleted sites to a
     * stream of BaseEntity objects and then collecting them into a set using the Collectors.toSet()
     * method.
     */
    public Set<Long> getDeletedSites() {
        return deletedSites.stream().map(
                BaseEntity::getId
        ).collect(Collectors.toSet());
    }

    /**
     * <p>
     * The function returns a CountryDTO object mapped from a country object using ModelMapper.
     * </p>
     *
     * @return A {@link CountryDTO} object that is created by mapping the properties of a country object to the
     * corresponding properties of the CountryDTO class using ModelMapper.
     */
    public CountryDTO getCountry() {
        return new ModelMapper().map(country, CountryDTO.class);
    }

}
