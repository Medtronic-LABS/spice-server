package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.Culture;
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
public class SiteResponseDTO {
    private Long id;

    private String name;

    private Long tenantId;

    private List<String> roleName;

    private List<String> displayName;

    private Culture culture;

    public MedicalReviewStaticDataListDTO getCulture() {
        return new MedicalReviewStaticDataListDTO(culture.getId());
    }
}
