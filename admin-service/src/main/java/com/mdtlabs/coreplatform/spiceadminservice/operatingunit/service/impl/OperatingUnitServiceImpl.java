package com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ParentOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceadminservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceadminservice.operatingunit.repository.OperatingUnitRepository;
import com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service.OperatingUnitService;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.SiteService;

/**
 * <p>
 * This Class contains the business logic for operating unit entity.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Service
public class OperatingUnitServiceImpl implements OperatingUnitService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private OperatingUnitRepository operatingUnitRepository;

    @Autowired
    private SiteService siteService;

    @Autowired
    private UserApiInterface userApiInterface;

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getOperatingUnitList(RequestDTO requestDto) {
        Logger.logInfo("Fetching Operating unit List");
        String searchTerm = requestDto.getSearchTerm();
        Pageable pageable = Pagination.setPagination(requestDto.getSkip(), requestDto.getLimit());
        if (StringUtils.isNotEmpty(searchTerm)) {
            searchTerm = searchTerm.replaceAll(Constants.SEARCH_TERM, Constants.EMPTY);
        }
        Organization organization = userApiInterface.getOrganizationById(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), requestDto.getTenantId()).getBody();
        ResponseListDTO response = new ResponseListDTO();
        if (!Objects.isNull(organization)) {
            Long countryId = organization.getFormName().equals(Constants.COUNTRY) ? organization.getFormDataId() : null;
            Long accountId = Objects.isNull(countryId) ? organization.getFormDataId() : null;
            Page<Operatingunit> operatingUnits = operatingUnitRepository
                    .findOperatingUnits(searchTerm, countryId, accountId, pageable);
            response.setTotalCount(operatingUnits.getTotalElements());
            List<OperatingUnitListDTO> operatingUnitListDTOs = new ArrayList<>();
            if (!operatingUnits.isEmpty()) {
                constructOperatingUnitListResponse(operatingUnitListDTOs, response, operatingUnits);
            }
        }
        return response;
    }

    /**
     * <p>
     * Constructs the response object for Operating unit List.
     * </p>
     *
     * @param operatingUnitListDTOs - Operating unit DTO list
     * @param response              - response object with Operating unit list and its count.
     * @param operatingUnits        - Operating units
     */
    private void constructOperatingUnitListResponse(List<OperatingUnitListDTO> operatingUnitListDTOs,
                                                    ResponseListDTO response, Page<Operatingunit> operatingUnits) {
        Map<Long, Long> noOfSites = siteService.getSiteCountByOperatingUnitIds(
                operatingUnits.stream().map(BaseEntity::getId).toList());
        for (Operatingunit operatingunit : operatingUnits) {
            OperatingUnitListDTO operatingUnitListDto = new OperatingUnitListDTO(operatingunit.getId(),
                    operatingunit.getName(), operatingunit.getTenantId());
            operatingUnitListDto.setSiteCount(noOfSites.get(operatingunit.getId()));
            operatingUnitListDTOs.add(operatingUnitListDto);
        }
        response.setData(operatingUnitListDTOs);
    }

    /**
     * {@inheritDoc}
     */
    public User addOperatingUnitAdmin(User user) {
        Role role = new Role();
        role.setName(Constants.ROLE_OPERATING_UNIT_ADMIN);
        user.setRoles(Set.of(role));
        return userApiInterface.addAdminUser(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                modelMapper.map(user, UserDetailsDTO.class), Boolean.FALSE).getBody();
    }

    /**
     * {@inheritDoc}
     */
    public User updateOperatingUnitAdmin(User user) {
        return userApiInterface.updateAdminUser(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                modelMapper.map(user, UserDTO.class)).getBody();
    }

    /**
     * {@inheritDoc}
     */
    public Boolean deleteOperatingUnitAdmin(CommonRequestDTO requestDto) {
        return userApiInterface
                .deleteAdminUser(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(), requestDto).getBody();
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getAllOperatingUnits(SearchRequestDTO requestDto) {
        String searchTerm = requestDto.getSearchTerm();
        ResponseListDTO response = new ResponseListDTO();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)) {
            response.setTotalCount(0L);
            return response;
        }
        searchTerm = StringUtils.isNotBlank(searchTerm) ? searchTerm.strip() : null;
        Pageable pageable = null;
        if (0 < requestDto.getLimit()) {
            pageable = Pagination.setPagination(requestDto.getSkip(), requestDto.getLimit(), Constants.UPDATED_AT,
                    Constants.BOOLEAN_FALSE);
        }
        Organization organization = userApiInterface.getOrganizationById(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), requestDto.getTenantId()).getBody();
        if (!Objects.isNull(organization)) {
            Long countryId = organization.getFormName().equals(Constants.COUNTRY) ? organization.getFormDataId() : null;
            Long accountId = Objects.isNull(countryId) ? organization.getFormDataId() : null;
            Page<Operatingunit> operatingUnits = operatingUnitRepository.getOperatingUnits(
                    searchTerm, countryId, accountId, pageable);
            if (!Objects.isNull(operatingUnits) && !operatingUnits.isEmpty()) {
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                response.setData(operatingUnits.stream().map(
                        operatingUnit -> modelMapper.map(operatingUnit, OperatingUnitDTO.class)
                ).toList());
                response.setTotalCount(operatingUnits.getTotalElements());
            }
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    public List<Long> activateOrDeactivateOperatingUnits(Long countryId, Long accountId, boolean isActive) {
        List<Operatingunit> operatingUnitList = operatingUnitRepository.findByCountryIdAndAccountIdAndIsActive(
                countryId, accountId, !isActive);
        List<Long> tenantIds = new ArrayList<>();
        if (!operatingUnitList.isEmpty()) {
            operatingUnitList.forEach(ou -> {
                ou.setActive(isActive);
                tenantIds.add(ou.getTenantId());
            });
            operatingUnitRepository.saveAll(operatingUnitList);
        }
        return tenantIds;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void activateOrDeactivateOperatingUnit(Long id, boolean isActive) {
        Operatingunit operatingunit = operatingUnitRepository.findByIdAndIsDeletedFalseAndIsActive(id, isActive);
        if (Objects.isNull(operatingunit)) {
            Logger.logError(ErrorConstants.OU_NOT_FOUND + id);
            throw new DataNotFoundException(29010, id.toString());
        }
        operatingunit.setActive(isActive);
        operatingUnitRepository.save(operatingunit);
        List<Long> tenantIds = new ArrayList<>(siteService.activateOrDeactivateSites(null, null, operatingunit.getId(), isActive));
        userApiInterface.activateOrDeactivateUser(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                tenantIds, isActive);
    }

    /**
     * {@inheritDoc}
     */
    public OperatingUnitDetailsDTO getOperatingUnitDetails(CommonRequestDTO requestDto) {
        if (Objects.isNull(requestDto.getId()) || Objects.isNull(requestDto.getTenantId())) {
            Logger.logError("ID and Tenant ID should not be empty.");
            throw new DataNotAcceptableException(20004);
        }
        Operatingunit operatingunit = operatingUnitRepository.findByIdAndIsActiveAndIsDeletedAndTenantId(
                requestDto.getId(), Constants.BOOLEAN_TRUE, Constants.BOOLEAN_FALSE, requestDto.getTenantId());
        if (Objects.isNull(operatingunit)) {
            Logger.logError(ErrorConstants.OU_NOT_FOUND + requestDto.getId());
            throw new DataNotFoundException(29010, requestDto.getId().toString());
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OperatingUnitDetailsDTO ouDto = modelMapper.map(operatingunit, OperatingUnitDetailsDTO.class);
        ouDto.setUsers(userApiInterface.getUsersByTenantIds(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), List.of(operatingunit.getTenantId())));
        if (!Objects.isNull(operatingunit.getAccount().getId())) {
            ouDto.setAccount(modelMapper.map(operatingunit.getAccount(), ParentOrganizationDTO.class));
        }
        return ouDto;
    }

    /**
     * {@inheritDoc}
     */
    public Operatingunit createOperatingUnit(Operatingunit operatingUnit) {
        Operatingunit existingOperatingUnit = operatingUnitRepository
                .findByNameIgnoreCaseAndIsDeletedFalse(operatingUnit.getName().strip());
        if (!Objects.isNull(existingOperatingUnit)) {
            Logger.logError(operatingUnit.getName() + " already exist(s) in the regional database");
            throw new DataConflictException(29011, operatingUnit.getName());
        }
        operatingUnit.setName(operatingUnit.getName().strip());
        return operatingUnitRepository.save(operatingUnit);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Operatingunit updateOperatingUnit(Operatingunit operatingunit) {
        Operatingunit existingOperatingunit = operatingUnitRepository
                .findByIdAndIsDeletedFalseAndIsActive(operatingunit.getId(), Constants.BOOLEAN_TRUE);

        if (Objects.isNull(existingOperatingunit)) {
            Logger.logError(ErrorConstants.OU_NOT_FOUND + operatingunit.getId());
            throw new DataNotFoundException(29010, operatingunit.getId().toString());
        }
        if (!Objects.isNull(operatingunit.getName())) {
            boolean exists = operatingUnitRepository.existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(operatingunit.getName(), operatingunit.getId());
            if (exists) {
                Logger.logError(operatingunit.getName() + " already exist(s) in the regional database");
                throw new DataConflictException(29011, operatingunit.getName());
            }
            existingOperatingunit.setName(operatingunit.getName());
            Organization organization = new Organization(operatingunit.getTenantId(), operatingunit.getName());
            userApiInterface.updateOrganization(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                    modelMapper.map(organization, OrganizationDTO.class));
        }
        return operatingUnitRepository.save(existingOperatingunit);
    }

    /**
     * {@inheritDoc}
     */
    public Map<Long, Long> getOperatingUnitCountByAccountIds(List<Long> accountIds) {
        Map<Long, Long> ouCountsMap = new HashMap<>();
        List<Map<String, Object>> ouCounts = operatingUnitRepository.getOperatingUnitCountByAccountIds(accountIds);
        ouCounts.forEach(count ->
                ouCountsMap.put((Long) count.get(Constants.ACCOUNT_ID), (Long) count.get(Constants.COUNT)));
        return ouCountsMap;
    }

    /**
     * {@inheritDoc}
     */
    public Map<Long, Long> getOperatingUnitCountByCountryIds(List<Long> countryIds) {
        Map<Long, Long> ouCountsMap = new HashMap<>();
        List<Map<String, Object>> ouCounts = operatingUnitRepository.getOperatingUnitCountByCountryIds(countryIds);
        ouCounts.forEach(count ->
                ouCountsMap.put((Long) count.get(Constants.COUNTRY_ID), (Long) count.get(Constants.COUNT)));
        return ouCountsMap;
    }
}
