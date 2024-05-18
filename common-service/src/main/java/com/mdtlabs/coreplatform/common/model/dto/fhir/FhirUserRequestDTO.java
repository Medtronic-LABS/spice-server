package com.mdtlabs.coreplatform.common.model.dto.fhir;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

import com.mdtlabs.coreplatform.common.model.entity.User;

@Data
public class FhirUserRequestDTO {
    private @NotNull(
            message = "Type should not be null"
    ) String type;
    private List<User> users;
}
