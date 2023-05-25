package com.mdtlabs.coreplatform.spiceadminservice.operatingunit.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import com.mdtlabs.coreplatform.common.Constants;
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
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service.impl.OperatingUnitServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Operating unit controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class OperatingUnitControllerTest {

    @InjectMocks
    private OperatingUnitController operatingUnitController;

    @Mock
    private OperatingUnitServiceImpl operatingUnitService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(OperatingUnitController.class, "modelMapper", operatingUnitController);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {Constants.ONE, Constants.ZERO})
    void testGetOperatingUnitList(Long totalCount) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        requestDTO.setSearchTerm(Constants.SEARCH_TERM);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        responseListDTO.setTotalCount(totalCount);

        //when
        when(operatingUnitService.getOperatingUnitList(requestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse<List<OperatingUnitListDTO>> operatingUnitList = operatingUnitController
                .getOperatingUnitList(requestDTO);
        assertNotNull(operatingUnitList);
        assertEquals(HttpStatus.OK, operatingUnitList.getStatusCode());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {Constants.ONE, Constants.ZERO})
    void testGetAllOperatingUnits(Long totalCount) {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(TestConstants.OU_NAME, Constants.ZERO,
                TestConstants.TEN);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        responseListDTO.setTotalCount(totalCount);

        //when
        when(operatingUnitService.getAllOperatingUnits(searchRequestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse<List<OperatingUnitDTO>> actualOperatingUnits = operatingUnitController
                .getAllOperatingUnits(searchRequestDTO);
        assertNotNull(actualOperatingUnits);
        assertEquals(HttpStatus.OK, actualOperatingUnits.getStatusCode());
    }

    @Test
    void testGetOperatingUnitDetails() {
        //given
        OperatingUnitDetailsDTO operatingUnitDetailsDTO = TestDataProvider.getOperatingUnitDetailsDTO();
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //when
        when(operatingUnitService.getOperatingUnitDetails(commonRequestDTO)).thenReturn(operatingUnitDetailsDTO);

        //then
        SuccessResponse<OperatingUnitDetailsDTO> actualOperatingUnits = operatingUnitController
                .getOperatingUnitDetails(commonRequestDTO);
        assertNotNull(actualOperatingUnits);
        assertEquals(HttpStatus.OK, actualOperatingUnits.getStatusCode());
    }

    @Test
    void testAddAccountAdmin() {
        //given
        User user = TestDataProvider.getUser();
        UserDetailsDTO userDTO = TestDataProvider.getUserDetailsDTO();

        //when
        when(operatingUnitService.addOperatingUnitAdmin(user)).thenReturn(user);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);

        //then
        SuccessResponse<User> addedAdmin = operatingUnitController.addAccountAdmin(userDTO);
        assertNotNull(addedAdmin);
        assertEquals(HttpStatus.CREATED, addedAdmin.getStatusCode());
    }

    @Test
    void testUpdateOperatingUnit() {
        //given
        Operatingunit operatingUnit = TestDataProvider.getOperatingUnit();
        OperatingUnitDTO operatingUnitDTO = TestDataProvider.getOperatingUnitDTO();

        //when
        when(operatingUnitService.updateOperatingUnit(operatingUnit)).thenReturn(operatingUnit);
        when(modelMapper.map(operatingUnitDTO, Operatingunit.class)).thenReturn(operatingUnit);

        //then
        SuccessResponse<Operatingunit> updatedOperatingUnit = operatingUnitController
                .updateOperatingUnit(operatingUnitDTO);
        assertNotNull(updatedOperatingUnit);
        assertEquals(HttpStatus.OK, updatedOperatingUnit.getStatusCode());
    }

    @Test
    void testUpdateOperatingUnitAdmin() {
        //given
        User user = TestDataProvider.getUser();
        UserDetailsDTO userDTO = TestDataProvider.getUserDetailsDTO();

        //when
        when(operatingUnitService.updateOperatingUnitAdmin(user)).thenReturn(user);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);

        //then
        SuccessResponse<User> updatedOperatingUnitAdmin = operatingUnitController.updateOperatingUnitAdmin(userDTO);
        assertNotNull(updatedOperatingUnitAdmin);
        assertEquals(HttpStatus.OK, updatedOperatingUnitAdmin.getStatusCode());
    }

    @Test
    void testDeleteOperatingUnitAdmin() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //when
        when(operatingUnitService.deleteOperatingUnitAdmin(commonRequestDTO)).thenReturn(Boolean.TRUE);

        //then
        SuccessResponse<User> deleteOperatingUnitAdmin = operatingUnitController
                .deleteOperatingUnitAdmin(commonRequestDTO);
        assertNotNull(deleteOperatingUnitAdmin);
        assertEquals(HttpStatus.OK, deleteOperatingUnitAdmin.getStatusCode());
    }

    @Test
    void testCreateOperatingUnit() {
        //given
        Operatingunit operatingUnit = TestDataProvider.getOperatingUnit();
        operatingUnit.setId(TestConstants.ONE);
        OperatingUnitDTO operatingUnitDTO = TestDataProvider.getOperatingUnitDTO();

        //when
        when(operatingUnitService.createOperatingUnit(operatingUnit)).thenReturn(operatingUnit);
        when(modelMapper.map(operatingUnitDTO, Operatingunit.class)).thenReturn(operatingUnit);

        //then
        Operatingunit createdOperatingUnit = operatingUnitController.createOperatingUnit(operatingUnitDTO);
        assertNotNull(createdOperatingUnit);
        assertEquals(operatingUnit.getId(), createdOperatingUnit.getId());
    }
}