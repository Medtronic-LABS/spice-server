package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * This class is a Data transfer object for Site entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Data
public class SiteDTO {

    private List<String> roleName;

    private List<String> displayName;

    private Culture culture;

    private Long id;

    private Long tenantId;

    private boolean isDeleted;

    private String name;

    private String email;

    private String addressType;

    private String addressUse;

    private String address1;

    private String address2;

    private String latitude;

    private String longitude;

    private String city;

    private String phoneNumber;

    private Float workingHours;

    private String postalCode;

    private String siteLevel;

    private String siteType;

    private Long countryId;

    private Long countyId;

    private Long subCountyId;

    private Long accountId;

    private Operatingunit operatingUnit;

    private String refId;

    public MedicalReviewStaticDataListDTO getCulture() {
        return new MedicalReviewStaticDataListDTO(culture.getId());
    }
}
