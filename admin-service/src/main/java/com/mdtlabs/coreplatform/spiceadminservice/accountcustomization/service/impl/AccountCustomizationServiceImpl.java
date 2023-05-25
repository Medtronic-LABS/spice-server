package com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountCustomization;
import com.mdtlabs.coreplatform.common.repository.OrganizationRepository;
import com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.repository.AccountCustomizationRepository;
import com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.service.AccountCustomizationService;

/**
 * <p>
 * AccountCustomizationServiceImpl class implements methods for creating, retrieving, updating, and removing
 * account customizations, as well as getting a list of account customizations based on certain criteria.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 08, 2023
 */
@Service
public class AccountCustomizationServiceImpl implements AccountCustomizationService {

    @Autowired
    private AccountCustomizationRepository accountCustomizationRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private OrganizationRepository organizationRepository;

    /**
     * {@inheritDoc}
     */
    public AccountCustomization createAccountCustomization(AccountCustomization accountCustomization) {
        if (Objects.isNull(accountCustomization)) {
            Logger.logError(ErrorConstants.REQUEST_NOT_EMPTY);
            throw new BadRequestException(1003);
        }
        return accountCustomizationRepository.save(accountCustomization);
    }

    /**
     * {@inheritDoc}
     */
    public AccountCustomization getCustomization(CustomizationRequestDTO customizationRequestDto) {
        if (Objects.isNull(customizationRequestDto)) {
            Logger.logError(ErrorConstants.REQUEST_NOT_EMPTY);
            throw new DataNotAcceptableException(1003);
        }
        List<AccountCustomization> accountCustomizations = accountCustomizationRepository.getAccountCustomization(
                customizationRequestDto.getCountryId(), customizationRequestDto.getAccountId(),
                customizationRequestDto.getCategory(), customizationRequestDto.getType(),
                customizationRequestDto.getClinicalWorkflowId(), Constants.BOOLEAN_FALSE,
                customizationRequestDto.getTenantId());
        return Objects.isNull(accountCustomizations) || accountCustomizations.isEmpty() ? null
                : accountCustomizations.get(Constants.ZERO);
    }

    /**
     * {@inheritDoc}
     */
    public AccountCustomization updateCustomization(AccountCustomization accountCustomization) {
        if (Objects.isNull(accountCustomization)) {
            Logger.logError(ErrorConstants.REQUEST_NOT_EMPTY);
            throw new BadRequestException(1003);
        }
        AccountCustomization existingAccountCustomization = accountCustomizationRepository
                .findByIdAndIsDeletedAndTenantId(accountCustomization.getId(), Constants.BOOLEAN_FALSE,
                        accountCustomization.getTenantId());
        if (Objects.isNull(existingAccountCustomization)) {
            Logger.logError("No Account Customization forms found for the request.");
            throw new DataNotFoundException(2151);
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(accountCustomization, existingAccountCustomization);
        return accountCustomizationRepository.save(existingAccountCustomization);
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeCustomization(CommonRequestDTO requestData) {
        if (Objects.isNull(requestData.getId())) {
            Logger.logError(ErrorConstants.REQUEST_NOT_EMPTY);
            throw new BadRequestException(1003);
        }
        AccountCustomization accountCustomization = accountCustomizationRepository.findByIdAndIsDeletedAndTenantId(requestData.getId(), false, requestData.getTenantId());
        if (Objects.isNull(accountCustomization)) {
            Logger.logError("No Account Customization forms found for the request.");
            throw new DataNotFoundException(2151);
        }
        accountCustomization.setDeleted(true);
        return accountCustomizationRepository.save(accountCustomization).isDeleted();
    }

    /**
     * {@inheritDoc}
     */
    public List<AccountCustomization> getAccountCustomizations(Map<String, Object> requestData) {
        Long countryId = Long.parseLong(requestData.get(Constants.COUNTRY_ID).toString());
        List<String> screenTypes = (List<String>) requestData.get(Constants.SCREEN_TYPES);
        List<String> categories = (List<String>) requestData.get(FieldConstants.CATEGORY);
        return accountCustomizationRepository.findByCountryIdAndCategoryInAndTypeInAndIsDeletedFalse(countryId, categories, screenTypes);
    }
}
