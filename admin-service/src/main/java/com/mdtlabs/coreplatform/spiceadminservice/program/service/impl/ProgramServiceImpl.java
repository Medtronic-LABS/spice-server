package com.mdtlabs.coreplatform.spiceadminservice.program.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.entity.Program;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceadminservice.program.repository.ProgramRepository;
import com.mdtlabs.coreplatform.spiceadminservice.program.service.ProgramService;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.SiteService;

/**
 * <p>
 * ProgramServiceImpl class that implements various methods for managing programs, including creating,
 * updating, and retrieving program details.
 * </p>
 *
 * @author Karthick M created on Jun 30, 2022
 */
@Service
public class ProgramServiceImpl implements ProgramService {

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private SiteService siteService;

    private final ModelMapper mapper = new ModelMapper();

    /**
     * {@inheritDoc}
     */
    public Program createProgram(ProgramRequestDTO programDto) {
        Program existingProgram = programRepository.findByNameAndTenantIdAndIsDeleted(programDto.getName(),
                programDto.getTenantId(), Boolean.FALSE);
        if (!Objects.isNull(existingProgram)) {
            throw new DataConflictException(13002);
        }
        Program program = new Program(programDto.getName(), programDto.getTenantId(), programDto.getCountry());
        if (!Objects.isNull(programDto.getSites()) && !programDto.getSites().isEmpty()) {
            Set<Site> sites = siteService.getSiteByIds(programDto.getSites());
            if (!Objects.isNull(sites)) {
                program.setSites(sites);
            }
        }
        return programRepository.save(program);
    }

    /**
     * {@inheritDoc}
     */
    public Program getProgramById(Long id, Long tenantId, Boolean isDeleted) {
        Program program = programRepository.findByIdAndIsDeletedAndTenantId(id, Boolean.FALSE, tenantId);
        if (Objects.isNull(program)) {
            throw new DataNotFoundException(13001);
        }
        return program;
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeProgram(CommonRequestDTO requestDto) {
        Program program = getProgramById(requestDto.getId(), requestDto.getTenantId(), Boolean.FALSE);
        program.setDeleted(Boolean.TRUE);
        program.setActive(Boolean.FALSE);
        return !Objects.isNull(programRepository.save(program));
    }

    /**
     * {@inheritDoc}
     */
    public ProgramDetailsDTO getProgramDetails(CommonRequestDTO requestDto) {
        if (Objects.isNull(requestDto.getId()) || Objects.isNull(requestDto.getTenantId())) {
            throw new DataNotAcceptableException(12012);
        }
        Program program = getProgramById(requestDto.getId(), requestDto.getTenantId(), Boolean.FALSE);

        return mapper.map(program, ProgramDetailsDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    public Program updateProgram(ProgramRequestDTO updatedProgram) {
        if (Objects.isNull(updatedProgram)) {
            throw new BadRequestException(13009);
        }
        if (!Objects.isNull(updatedProgram.getName())) { // check name from request data
            throw new DataNotAcceptableException(13004);
        }
        Program existingProgram = programRepository.findByIdAndIsDeleted(updatedProgram.getId(), false);
        if (Objects.isNull(existingProgram)) {
            throw new DataNotFoundException(13001);
        }
        if (!Objects.isNull(updatedProgram.getSites()) && !updatedProgram.getSites().isEmpty()) {
            Set<Site> sites = siteService.getSiteByIds(updatedProgram.getSites());
            if (!Objects.isNull(sites)) {
                existingProgram.setSites(sites);
            }
        }
        if (!Objects.isNull(updatedProgram.getDeletedSites())) {
            Set<Site> sites = siteService.getSiteByIds(updatedProgram.getDeletedSites());
            if (!Objects.isNull(sites)) {
                existingProgram.setDeletedSites(sites);
            }
        }
        existingProgram.setActive(updatedProgram.isActive());
        return programRepository.save(existingProgram);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getAllPrograms(RequestDTO requestDTO) {
        if (Objects.isNull(requestDTO.getTenantId())) {
            throw new DataNotAcceptableException(13008);
        }
        long totalCount = 0l;
        String searchTerm = requestDTO.getSearchTerm();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)) {
            return new ResponseListDTO(new ArrayList<>(), totalCount);
        }
        Pageable pageable = Pagination.setPagination(requestDTO.getSkip(), requestDTO.getLimit());
        Page<Program> programs = programRepository.getAllProgram(searchTerm, requestDTO.getCountryId(),
                requestDTO.getTenantId(), pageable);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<ProgramListDTO> programDTOs = null;
        if (!Objects.isNull(programs)) {
            programDTOs = programs.stream().map(program ->
                    mapper.map(program, ProgramListDTO.class)
            ).toList();
        }
        long totalElements = Objects.nonNull(programs) ? programs.getTotalElements() : Constants.ZERO;
        totalCount = requestDTO.getSearchTerm().isBlank()
                ? programRepository.countByCountryIdAndTenantId(requestDTO.getCountryId(), requestDTO.getTenantId())
                : totalElements;

        return new ResponseListDTO(programDTOs, totalCount);
    }

    /**
     * {@inheritDoc}
     */
    public List<Program> getProgramsBySiteIds(List<Long> siteIds) {
        return programRepository.findProgramsBySiteIds(siteIds);
    }
}
