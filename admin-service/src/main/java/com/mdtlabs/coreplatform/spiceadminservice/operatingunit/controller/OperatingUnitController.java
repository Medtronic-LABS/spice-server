package com.mdtlabs.coreplatform.spiceadminservice.operatingunit.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service.OperatingUnitService;

/**
 * <p>
 * OperatingUnitController class that defines REST API endpoints for managing operating units,
 * including getting lists of operating units, adding and updating operating unit admins,
 * and creating new operating units.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@RestController
@RequestMapping("/operating-unit")
@Validated
public class OperatingUnitController {

    @Autowired
    private OperatingUnitService operatingUnitService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to get list of operating unit details using the given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The search request contains necessary information
     *                   to get the list of operating units is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved
     * list of operating units and total count
     */
    @UserTenantValidation
    @PostMapping("/list")
    public SuccessResponse<List<OperatingUnitListDTO>> getOperatingUnitList(
            @RequestBody RequestDTO requestDto) {
        Logger.logInfo("In Operating unit controller, getting operating unit list");
        ResponseListDTO response = operatingUnitService.getOperatingUnitList(requestDto);
        if (Objects.isNull(response.getTotalCount()) || 0L == response.getTotalCount()) {
            return new SuccessResponse(SuccessCode.GOT_OU, response.getData(), HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GOT_OU, response.getData(), response.getTotalCount(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get list of operating unit DTOs using the given request.
     * </p>
     *
     * @param requestDto {@link SearchRequestDTO} The search request contains necessary information
     *                   to get the list of operating unit DTOs is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved
     * list of operating units DTOs and total count
     */
    @UserTenantValidation
    @PostMapping("/all")
    public SuccessResponse<List<OperatingUnitDTO>> getAllOperatingUnits(@RequestBody SearchRequestDTO requestDto) {
        Logger.logInfo("In Operating unit controller, getting operating unit list");
        ResponseListDTO response = operatingUnitService.getAllOperatingUnits(requestDto);
        if (Objects.isNull(response.getTotalCount()) || 0L == response.getTotalCount()) {
            return new SuccessResponse(SuccessCode.GOT_OU, response.getData(), HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GOT_OU, response.getData(), response.getTotalCount(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get list of operating unit details based on given request
     * </p>
     *
     * @param commonRequestDto {@link CommonRequestDTO} The search request contains necessary information
     *                         to get the list of operating unit details is given
     * @return {@link SuccessResponse<OperatingUnitDetailsDTO>} Returns a success message and status with the retrieved
     * list of operating unit details
     */
    @UserTenantValidation
    @PostMapping("/details")
    public SuccessResponse<OperatingUnitDetailsDTO> getOperatingUnitDetails(
            @RequestBody CommonRequestDTO commonRequestDto) {
        Logger.logInfo("In Operating unit controller, getting operating unit details");
        return new SuccessResponse<>(SuccessCode.GOT_OU,
                operatingUnitService.getOperatingUnitDetails(commonRequestDto), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update an operating unit admin with the provided details.
     * </p>
     *
     * @param userDto {@link UserDetailsDTO} The operating unit admin need to be added is given
     * @return {@link SuccessResponse<User>} The user is added and a success message with the
     * status is returned
     */
    @UserTenantValidation
    @PostMapping("/user-add")
    public SuccessResponse<User> addAccountAdmin(@RequestBody @Valid UserDetailsDTO userDto) {
        operatingUnitService.addOperatingUnitAdmin(modelMapper.map(userDto, User.class));
        Logger.logInfo("In Operating unit controller, adding Operating unit admin");
        return new SuccessResponse<>(SuccessCode.OU_ADMIN_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to update a existing operating unit using the operating unit Dto.
     * </p>
     *
     * @param operatingUnitDto {@link OperatingUnitDTO} The operating unit dto that contains necessary information
     *                         to update operating unit is given
     * @return {@link SuccessResponse<Operatingunit>} Returns a success message and status after updating
     * the operating unit
     */
    @UserTenantValidation
    @PutMapping("/update")
    public SuccessResponse<Operatingunit> updateOperatingUnit(@RequestBody OperatingUnitDTO operatingUnitDto) {
        Logger.logInfo("In Operating unit controller, updating Operating unit");
        operatingUnitService.updateOperatingUnit(modelMapper.map(operatingUnitDto, Operatingunit.class));
        return new SuccessResponse<>(SuccessCode.OU_UPDATE, HttpStatus.OK);
    }


    /**
     * <p>
     * This method is used to update an operating unit admin with the provided details.
     * </p>
     *
     * @param userDto {@link UserDetailsDTO} The operating unit admin need to be updated is given
     * @return {@link SuccessResponse<User>} The user is updated and a success message and status is returned
     */
    @UserTenantValidation
    @PutMapping("/user-update")
    public SuccessResponse<User> updateOperatingUnitAdmin(@RequestBody @Valid UserDetailsDTO userDto) {
        Logger.logInfo("In Operating unit controller, adding Operating unit admin");
        operatingUnitService.updateOperatingUnitAdmin(modelMapper.map(userDto, User.class));
        return new SuccessResponse<>(SuccessCode.OU_ADMIN_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to delete an operating unit admin for the given request
     * </p>
     *
     * @param commonRequestDTO {@link CommonRequestDTO} The parameters required to delete the
     *                         admin is given
     * @return {@link SuccessResponse<User>} The user for the given request is
     * deleted and success message with status is returned
     */
    @UserTenantValidation
    @DeleteMapping("/user-remove")
    public SuccessResponse<User> deleteOperatingUnitAdmin(@RequestBody CommonRequestDTO commonRequestDTO) {
        Logger.logInfo("In Operating unit controller, removing Operating unit admin");
        operatingUnitService.deleteOperatingUnitAdmin(commonRequestDTO);
        return new SuccessResponse<>(SuccessCode.OU_ADMIN_DELETE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to create a new operating unit using the operating unit data.
     * </p>
     *
     * @param operatingUnitDto {@link OperatingUnitDTO} The operating unit dto that contains
     *                         necessary information to create operating unit is given
     * @return {@link Operatingunit} The operating unit which is created for given operating unit details is returned
     */
    @PostMapping("/create")
    public Operatingunit createOperatingUnit(@RequestBody @Valid OperatingUnitDTO operatingUnitDto) {
        Logger.logInfo("In Operating unit controller, creating Operating unit");
        return operatingUnitService.createOperatingUnit(modelMapper.map(operatingUnitDto, Operatingunit.class));
    }
}
