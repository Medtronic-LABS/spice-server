package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.google.common.reflect.TypeToken;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;

/**
 * <p>
 * This class is a Data transfer object for Account Entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Data
public class AccountDetailsDTO {
    private Long id;

    private String name;

    private int maxNoOfUsers;

    private Long tenantId;

    private CountryDTO country;

    private List<AccountWorkflow> clinicalWorkflow;

    private List<AccountWorkflow> customizedWorkflow;

    private List<UserOrganizationDTO> users;

    private String countryCode;

    /**
     * <p>
     * This Java function returns a list of AccountWorkflowDetailsDTO objects mapped from a
     * clinicalWorkflow object using ModelMapper.
     * </p>
     *
     * @return {@link List<AccountWorkflowDetailsDTO>} objects that are mapped from the `clinicalWorkflow`
     * object using a `ModelMapper`.
     */
    public List<AccountWorkflowDetailsDTO> getClinicalWorkflow() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(clinicalWorkflow, new TypeToken<List<AccountWorkflowDetailsDTO>>() {
        }.getType());
    }

    /**
     * <p>
     * This Java function returns a list of AccountWorkflowDetailsDTO objects that are mapped from a
     * customizedWorkflow object using ModelMapper.
     * </p>
     *
     * @return {@link List<AccountWorkflowDetailsDTO>} objects that are mapped from the
     * `customizedWorkflow` object using the `ModelMapper` library.
     */
    public List<AccountWorkflowDetailsDTO> getCustomizedWorkflow() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(customizedWorkflow, new TypeToken<List<AccountWorkflowDetailsDTO>>() {
        }.getType());
    }

}
