package com.mdtlabs.coreplatform.spiceadminservice.program.service;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.program.repository.ProgramRepository;
import com.mdtlabs.coreplatform.spiceadminservice.program.service.impl.ProgramServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.SiteService;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Program service implementation.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProgramServiceTest {

    @InjectMocks
    private ProgramServiceImpl programService;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private SiteService siteService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(ProgramServiceImpl.class, "mapper", programService);
    }

    @Test
    void testCreateProgram() {
        //given
        ProgramRequestDTO programRequestDTO = TestDataProvider.getProgramRequestDTO();
        Program program = TestDataProvider.getProgram();
        programRequestDTO.setSites(Set.of(TestConstants.ONE, TestConstants.TWO));
        Set<Site> sites = TestDataProvider.getSites();
        program.setSites(sites);

        //when
        when(programRepository.findByNameAndTenantIdAndIsDeleted(programRequestDTO.getName(), programRequestDTO.getTenantId(), Boolean.FALSE)).thenReturn(null);
        when(siteService.getSiteByIds(programRequestDTO.getSites())).thenReturn(sites);
        when(programRepository.save(program)).thenReturn(program);

        //then
        Program actualProgram = programService.createProgram(programRequestDTO);
        assertEquals(program.getName(), actualProgram.getName());
        assertNotNull(actualProgram);
    }

    @Test
    void testCreateProgramWithSites() {
        //given
        ProgramRequestDTO programRequestDTO = TestDataProvider.getProgramRequestDTO();
        Program program = TestDataProvider.getProgram();

        //when
        when(programRepository.findByNameAndTenantIdAndIsDeleted(programRequestDTO.getName(), programRequestDTO.getTenantId(), Boolean.FALSE)).thenReturn(null);
        when(programRepository.save(program)).thenReturn(program);

        //then
        Program actualProgram = programService.createProgram(programRequestDTO);
        assertEquals(program.getName(), actualProgram.getName());
        assertNotNull(actualProgram);
    }

    @Test
    void testGetProgramById() {
        //given
        Program program = TestDataProvider.getProgram();
        program.setId(TestConstants.ONE);

        //when
        when(programRepository.findByIdAndIsDeletedAndTenantId(TestConstants.ONE, Boolean.FALSE, TestConstants.ONE))
                .thenReturn(program);

        //then
        Program actualProgram = programService.getProgramById(TestConstants.ONE, TestConstants.ONE, Boolean.FALSE);
        assertNotNull(actualProgram);
        assertEquals(program.getId(), actualProgram.getId());
        assertEquals(program.getName(), actualProgram.getName());
    }

    @Test
    void testRemoveProgram() {
        //given
        Program program = TestDataProvider.getProgram();
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //when
        when(programRepository.findByIdAndIsDeletedAndTenantId(commonRequestDTO.getId(), Boolean.FALSE,
                TestConstants.ONE)).thenReturn(program);
        when(programRepository.save(program)).thenReturn(program);

        //then
        boolean actualResponse = programService.removeProgram(commonRequestDTO);
        assertTrue(actualResponse);
    }

    @Test
    void testRemoveProgramNull() {
        //given
        Program program = TestDataProvider.getProgram();
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //when
        when(programRepository.findByIdAndIsDeletedAndTenantId(commonRequestDTO.getId(), Boolean.FALSE,
                TestConstants.ONE)).thenReturn(program);
        when(programRepository.save(program)).thenReturn(null);

        //then
        boolean actualResponse = programService.removeProgram(commonRequestDTO);
        assertFalse(actualResponse);
    }

    @Test
    void testUpdateProgram() {
        //given
        Set<Site> sites = TestDataProvider.getSites();
        ProgramRequestDTO programRequestDTO = TestDataProvider.getProgramRequestDTO();
        programRequestDTO.setId(TestConstants.ONE);
        programRequestDTO.setName(null);
        Program program = TestDataProvider.getProgram();
        programRequestDTO.setSites(Set.of(TestConstants.ONE, TestConstants.TWO));
        Set<Site> deletedSites = Set.of(new Site(Constants.THREE));
        programRequestDTO.setDeletedSites(Set.of(Constants.THREE));

        //when
        when(programRepository.findByIdAndIsDeleted(programRequestDTO.getId(), Boolean.FALSE)).thenReturn(program);
        when(siteService.getSiteByIds(Set.of(TestConstants.ONE, TestConstants.TWO))).thenReturn(sites);
        when(siteService.getSiteByIds(Set.of(Constants.THREE))).thenReturn(deletedSites);
        when(programRepository.save(program)).thenReturn(program);

        //then
        Program updatedProgram = programService.updateProgram(programRequestDTO);
        assertNotNull(updatedProgram);
        assertEquals(program.getName(), updatedProgram.getName());
        assertEquals(programRequestDTO.getSites().size(), updatedProgram.getSites().size());
        assertEquals(programRequestDTO.getDeletedSites().size(), updatedProgram.getDeletedSites().size());
        assertFalse(updatedProgram.getSites().isEmpty());
        assertFalse(updatedProgram.getDeletedSites().isEmpty());
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {"", "a"})
    void testGetAllPrograms(String searchTerm) {
        //given
        ModelMapper mapper = new ModelMapper();
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(searchTerm, Constants.ZERO,
                TestConstants.TEN);
        requestDTO.setTenantId(TestConstants.ONE);
        String formattedSearchTerm = "a".replaceAll(Constants.SEARCH_TERM, Constants.EMPTY);

        Page<Program> programs = new PageImpl<>(TestDataProvider.getPrograms());
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN);

        List<ProgramListDTO> programListDTOs = programs.stream().map(program -> mapper.map(program,
                ProgramListDTO.class)).toList();

        //when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(programRepository.getAllProgram(formattedSearchTerm, requestDTO.getCountryId(), requestDTO.getTenantId(),
                pageable)).thenReturn(programs);
        when(programRepository.getAllProgram(Constants.EMPTY, requestDTO.getCountryId(), requestDTO.getTenantId(),
                pageable)).thenReturn(programs);
        when(modelMapper.map(programs.stream().toList(), new TypeToken<List<ProgramListDTO>>() {
        }.getType())).thenReturn(programListDTOs);
        when(programRepository.countByCountryIdAndTenantId(requestDTO.getCountryId(), requestDTO.getTenantId()))
                .thenReturn(Constants.TWO);

        //then
        ResponseListDTO actualResponse = programService.getAllPrograms(requestDTO);
        assertNotNull(actualResponse);
        List<Object> actualPrograms = actualResponse.getData();
        assertEquals(programs.getTotalElements(), actualPrograms.size());
    }

    @Test
    void testGetProgramDetails() {
        //given
        ModelMapper mapper = new ModelMapper();
        Program program = TestDataProvider.getProgram();
        program.setId(TestConstants.ONE);
        ProgramDetailsDTO programDetailsDTO = mapper.map(program, ProgramDetailsDTO.class);
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //when
        when(programRepository.findByIdAndIsDeletedAndTenantId(TestConstants.ONE, Boolean.FALSE, TestConstants.ONE))
                .thenReturn(program);
        when(modelMapper.map(program, ProgramDetailsDTO.class)).thenReturn(programDetailsDTO);

        //then
        ProgramDetailsDTO actualProgramDetailsDTO = programService.getProgramDetails(commonRequestDTO);
        assertNotNull(actualProgramDetailsDTO);
        assertEquals(programDetailsDTO.getName(), actualProgramDetailsDTO.getName());
        assertEquals(TestConstants.ONE, actualProgramDetailsDTO.getId());
    }

    @Test
    void testGetProgramsBySiteIds() {
        //given
        List<Long> siteIds = List.of(1L, 2L);
        List<Program> programs = TestDataProvider.getPrograms();

        //when
        when(programRepository.findProgramsBySiteIds(siteIds)).thenReturn(programs);

        //then
        List<Program> actualPrograms = programService.getProgramsBySiteIds(siteIds);
        assertEquals(programs.size(), actualPrograms.size());
        assertFalse(actualPrograms.isEmpty());
        assertEquals(programs.get(0).getName(), actualPrograms.get(0).getName());
        assertEquals(programs.get(1).getName(), actualPrograms.get(1).getName());
    }

    @Test
    void checkNull() {
        assertThrows(BadRequestException.class, () -> programService.updateProgram(null));
    }

    @Test
    void throwDataNotFoundException() {
        //given
        ProgramRequestDTO programRequestDTO = TestDataProvider.getProgramRequestDTO();
        programRequestDTO.setId(TestConstants.ONE);
        programRequestDTO.setName(null);

        //when
        when(programRepository.findByIdAndIsDeletedAndTenantId(TestConstants.ONE, Boolean.FALSE, TestConstants.ONE))
                .thenReturn(null);
        when(programRepository.findByIdAndIsDeleted(programRequestDTO.getId(), Boolean.FALSE)).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> programService.updateProgram(programRequestDTO));
        assertThrows(DataNotFoundException.class, () -> programService.getProgramById(TestConstants.ONE,
                TestConstants.ONE, Boolean.FALSE));
    }

    @Test
    void throwDataNotAcceptableException() {
        //given
        ProgramRequestDTO programRequestDTO = TestDataProvider.getProgramRequestDTO();
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        commonRequestDTO.setTenantId(null);
        RequestDTO requestDTO = new RequestDTO();

        //then
        assertThrows(DataNotAcceptableException.class, () -> programService.getProgramDetails(commonRequestDTO));
        commonRequestDTO.setId(null);
        assertThrows(DataNotAcceptableException.class, () -> programService.getProgramDetails(commonRequestDTO));
        assertThrows(DataNotAcceptableException.class, () -> programService.updateProgram(programRequestDTO));
        assertThrows(DataNotAcceptableException.class, () -> programService.getAllPrograms(requestDTO));
    }

    @Test
    void testDataConflictException() {
        //given
        ProgramRequestDTO programRequestDTO = TestDataProvider.getProgramRequestDTO();

        //when
        when(programRepository.findByNameAndTenantIdAndIsDeleted(programRequestDTO.getName(), programRequestDTO.getTenantId(), Boolean.FALSE)).thenReturn(new Program());

        //then
        assertThrows(DataConflictException.class, () -> programService.createProgram(programRequestDTO));
    }
}


