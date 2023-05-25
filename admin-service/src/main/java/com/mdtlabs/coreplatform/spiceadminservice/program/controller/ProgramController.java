package com.mdtlabs.coreplatform.spiceadminservice.program.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.entity.Program;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.program.service.ProgramService;

/**
 * <p>
 * ProgramController class that defines REST API endpoints for creating, retrieving, updating,
 * and deleting programs, as well as getting a list of programs associated with
 * a list of site IDs.
 * </p>
 *
 * @author Karthick M created on Jun 30, 2022
 */
@RestController
@RequestMapping("/program")
@Validated
public class ProgramController {

    @Autowired
    private ProgramService programService;

    /**
     * <p>
     * This method is used to create a new program using the program request Dto.
     * </p>
     *
     * @param program {@link ProgramRequestDTO} The program request Dto that contains necessary information
     *                to create program is given
     * @return {@link SuccessResponse<Program>} Returns a success message and status after creating the program
     */
    @UserTenantValidation
    @PostMapping("/create")
    public SuccessResponse<Program> createProgram(@Valid @RequestBody ProgramRequestDTO program) {
        programService.createProgram(program);
        return new SuccessResponse<>(SuccessCode.PROGRAM_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is to get the program based on given request.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The common request contains necessary
     *                   information like ID to get the program is given
     * @return {@link SuccessResponse<Program>} Returns a success message and status with the retrieved program
     */
    @UserTenantValidation
    @PostMapping("/details")
    public SuccessResponse<Program> getProgramById(@RequestBody CommonRequestDTO requestDto) {
        return new SuccessResponse<>(SuccessCode.GET_PROGRAM,
                programService.getProgramDetails(requestDto), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get list of programs using the given request.
     * </p>
     *
     * @param request {@link RequestDTO} The request contains necessary information
     *                to get the list of programs is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved
     * list of programs and total count
     */
    @UserTenantValidation
    @PostMapping("/list")
    public SuccessResponse<List<ProgramListDTO>> getPrograms(@RequestBody RequestDTO request) {
        ResponseListDTO response = programService.getAllPrograms(request);
        return new SuccessResponse(SuccessCode.GET_PROGRAM, response.getData(), response.getTotalCount(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to remove a program based on the provided request.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The common request contains necessary information to remove
     *                   the program is given
     * @return {@link SuccessResponse<Boolean>} The success message with the status is returned if the program
     * is deleted
     */
    @UserTenantValidation
    @DeleteMapping("/remove")
    public SuccessResponse<Boolean> removeProgram(@RequestBody CommonRequestDTO requestDto) {
        programService.removeProgram(requestDto);
        return new SuccessResponse<>(SuccessCode.PROGRAM_DELETE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update a existing program using the program request Dto.
     * </p>
     *
     * @param program {@link ProgramRequestDTO} The program request Dto that contains necessary information
     *                to update program is given
     * @return {@link SuccessResponse<Program>} Returns a success message and status after updating
     * the program
     */
    @UserTenantValidation
    @PatchMapping("/update")
    public SuccessResponse<Program> updateProgram(@RequestBody ProgramRequestDTO program) {
        programService.updateProgram(program);
        return new SuccessResponse<>(SuccessCode.PROGRAM_STATUS_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get a list of programs associated to the given list of site IDs.
     * </p>
     *
     * @param siteIds {@link List<Long>} The list of site IDs for which the
     *                programs need to be retrieved is given
     * @return {@link List<Program>} The list of programs associated with the given list of site IDs is returned
     */
    @PostMapping("/get-by-site-ids")
    public List<Program> getProgramsBySiteIds(@RequestBody List<Long> siteIds) {
        return programService.getProgramsBySiteIds(siteIds);
    }
}
