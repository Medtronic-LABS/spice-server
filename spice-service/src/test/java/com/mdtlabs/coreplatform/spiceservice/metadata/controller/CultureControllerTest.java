package com.mdtlabs.coreplatform.spiceservice.metadata.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.CultureDTO;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CultureControllerTest {

    @InjectMocks
    private CultureController cultureController;

    @Mock
    private CultureService cultureService;

    @Test
    @DisplayName("GetCulture Test")
    void getCulture() {
        //given
        SuccessResponse<List<CultureDTO>> response;
        CultureDTO cultureDTO = TestDataProvider.getCultureDTO();
        List<CultureDTO> cultureDTOList = List.of(cultureDTO);
        //when
        when(cultureService.getAllCultures()).thenReturn(cultureDTOList);
        //then
        response = cultureController.getCultures();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
