package com.mdtlabs.coreplatform.spiceadminservice.program.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Program;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.program.service.impl.ProgramServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Program controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class ProgramControllerTest {

    @InjectMocks
    private ProgramController programController;

    @Mock
    private ProgramServiceImpl programService;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ParameterizedTest
    @MethodSource("getProgramRequestDTO")
    void validateAccountWorkflowDTO(String name, Country country, int violationSize) {
        validate();
        ProgramRequestDTO programRequestDTO = new ProgramRequestDTO();
        programRequestDTO.setName(name);
        programRequestDTO.setCountry(country);
        Set<ConstraintViolation<ProgramRequestDTO>> violations = validator.validate(programRequestDTO);
        assertThat(violations).hasSize(violationSize);
    }

    @Test
    void testCreateProgram() {
        //given
        ProgramRequestDTO programRequestDTO = TestDataProvider.getProgramRequestDTO();
        Program program = TestDataProvider.getProgram();

        //when
        when(programService.createProgram(programRequestDTO)).thenReturn(program);

        //then
        SuccessResponse<Program> actualProgram = programController.createProgram(programRequestDTO);
        assertNotNull(actualProgram);
        assertEquals(HttpStatus.CREATED, actualProgram.getStatusCode());
    }

    @Test
    void testGetProgramById() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.TWO);
        Program program = TestDataProvider.getProgram();
        ModelMapper modelMapper = new ModelMapper();
        ProgramDetailsDTO programDetailsDTO = modelMapper.map(program, ProgramDetailsDTO.class);

        //when
        when(programService.getProgramDetails(commonRequestDTO)).thenReturn(programDetailsDTO);

        //then
        SuccessResponse<Program> actualProgram = programController.getProgramById(commonRequestDTO);
        assertNotNull(actualProgram);
        assertEquals(HttpStatus.OK, actualProgram.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource({"count, 2", ",", "count,"})
    void testGetPrograms(String key, Object count) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(Constants.EMPTY, Constants.ZERO, TestConstants.TEN);
        ResponseListDTO response = new ResponseListDTO(TestDataProvider.getPrograms(), 10l);
        //when
        when(programService.getAllPrograms(requestDTO)).thenReturn(response);

        //then
        SuccessResponse<List<ProgramListDTO>> actualPrograms = programController.getPrograms(requestDTO);
        assertNotNull(actualPrograms);
        assertEquals(HttpStatus.OK, actualPrograms.getStatusCode());
    }

    @Test
    void testGetEmptyPrograms() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(Constants.EMPTY, Constants.ZERO,
                TestConstants.TEN);

        //when
        when(programService.getAllPrograms(requestDTO)).thenReturn(new ResponseListDTO(new ArrayList<>(), 0L));

        //then
        SuccessResponse<List<ProgramListDTO>> actualPrograms = programController.getPrograms(requestDTO);
        assertNotNull(actualPrograms);
        assertEquals(HttpStatus.OK, actualPrograms.getStatusCode());
    }

    @Test
    void testRemoveProgram() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.TWO);

        //when
        when(programService.removeProgram(commonRequestDTO)).thenReturn(Boolean.TRUE);

        //then
        SuccessResponse<Boolean> deletedResponse = programController.removeProgram(commonRequestDTO);
        assertNotNull(deletedResponse);
        assertEquals(HttpStatus.OK, deletedResponse.getStatusCode());
    }

    @Test
    void testUpdateProgram() {
        //given
        ProgramRequestDTO programRequestDTO = TestDataProvider.getProgramRequestDTO();
        Program program = TestDataProvider.getProgram();

        //when
        when(programService.updateProgram(programRequestDTO)).thenReturn(program);

        //then
        SuccessResponse<Program> actualProgram = programController.updateProgram(programRequestDTO);
        assertNotNull(actualProgram);
        assertEquals(HttpStatus.OK, actualProgram.getStatusCode());
    }

    @Test
    void testGetProgramsBySiteIds() {
        //given
        List<Long> siteIds = List.of(1L, 2L);
        List<Program> programs = TestDataProvider.getPrograms();

        //when
        when(programService.getProgramsBySiteIds(siteIds)).thenReturn(programs);

        //then
        List<Program> actualPrograms = programController.getProgramsBySiteIds(siteIds);
        assertEquals(programs.size(), actualPrograms.size());
        assertFalse(actualPrograms.isEmpty());
        assertEquals(programs.get(0).getName(), actualPrograms.get(0).getName());
        assertEquals(programs.get(1).getName(), actualPrograms.get(1).getName());
    }

    private void validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> getProgramRequestDTO() {
        return Stream.of(
                Arguments.of(TestConstants.PROGRAM_NAME, new Country(), Constants.ZERO),
                Arguments.of(Constants.EMPTY, null, Constants.TWO),
                Arguments.of(Constants.SPACE, null, Constants.ONE),
                Arguments.of(null, null, Constants.TWO),
                Arguments.of(TestConstants.SECOND_PROGRAM_NAME, null, Constants.ONE)
        );
    }
}
