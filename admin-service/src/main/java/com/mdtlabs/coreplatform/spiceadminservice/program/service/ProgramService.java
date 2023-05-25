package com.mdtlabs.coreplatform.spiceadminservice.program.service;

import java.util.List;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.entity.Program;

/**
 * <p>
 * ProgramService is a Java interface for a service that performs actions on Program entities.
 * It contains methods for creating, retrieving, updating, and removing programs, as well as
 * getting program details and lists of programs associated with site IDs.
 * </p>
 *
 * @author Karthick M created on Jun 30, 2022
 */
public interface ProgramService {

    /**
     * <p>
     * This method is used to create a new program using the program request Dto.
     * </p>
     *
     * @param program {@link ProgramRequestDTO} The program request Dto that contains necessary information
     *                to create program is given
     * @return {@link Program} The program is created for given details and returned
     */
    Program createProgram(ProgramRequestDTO program);

    /**
     * <p>
     * This method is to get the account customization data details based on given request.
     * </p>
     *
     * @param id        {@link Long} The ID for which the program is being searched is given
     * @param tenantId  {@link Long} The tenant ID for which the program is being searched is given
     * @param isDeleted {@link Boolean} The boolean value that is used to filter the results of the query based
     *                  on whether the program has been marked as deleted or not is given
     * @return {@link Program} The retrieved program for the given ID, tenant ID and deletion status is returned
     */
    Program getProgramById(Long id, Long tenantId, Boolean isDeleted);

    /**
     * <p>
     * This method is used to update a existing program using the program request Dto.
     * </p>
     *
     * @param program {@link ProgramRequestDTO} The program request Dto that contains necessary information
     *                to update program is given
     * @return {@link Program} A program is updated using the given program details and
     */
    Program updateProgram(ProgramRequestDTO program);

    /**
     * <p>
     * This method is used to remove a program based on the provided request.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The common request contains necessary information to remove
     *                   the program is given
     * @return The program for the given request is deleted and corresponding boolean value is returned
     */
    boolean removeProgram(CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is used to get a map contains both list of programs and its count based on given request.
     * </p>
     *
     * @param requestObject {@link RequestDTO} The request contains necessary information
     *                      to get the list of programs and total count is given
     * @return {@link ResponseListDTO} A Object containing the retrieved list of programs and total count of programs
     * for the given request is returned
     */
    ResponseListDTO getAllPrograms(RequestDTO requestObject);

    /**
     * <p>
     * This method is used to get a list of programs associated to the given list of site IDs.
     * </p>
     *
     * @param siteIds {@link List<Long>} The list of site IDs for which the
     *                programs need to be retrieved is given
     * @return {@link List<Program>} The list of programs associated with the given list of site IDs is returned
     */
    List<Program> getProgramsBySiteIds(List<Long> siteIds);

    /**
     * <p>
     * This method is to get the program details based on given request.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The common request contains necessary
     *                   information like ID to get the program is given
     * @return {@link ProgramDetailsDTO} The retrieved program for the given request is returned
     */
    ProgramDetailsDTO getProgramDetails(CommonRequestDTO requestDto);
}
