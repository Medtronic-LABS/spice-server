package com.mdtlabs.coreplatform.spiceservice.staticdata.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewStaticDataDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MetaFormDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.StaticDataDTO;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.staticdata.service.impl.StaticDataServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StaticDataControllerTest {

    @InjectMocks
    private StaticDataController staticDataController;

    @Mock
    private StaticDataServiceImpl staticDataService;

    @Test
    void testGetStaticData() {
        //given
        StaticDataDTO staticDataDTO = TestDataProvider.getStaticDataDTO();

        //when
        when(staticDataService.getStaticData(1L)).thenReturn(staticDataDTO);

        //then
        SuccessResponse<StaticDataDTO> actualResponse = staticDataController.getStaticData(1L);
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    void testGetMedicalReviewStaticData() {
        //given
        MedicalReviewStaticDataDTO medicalReviewStaticDataDTO = TestDataProvider.getMedicalReviewStaticDataDTO();

        //when
        when(staticDataService.getMedicalReviewStaticData(1L)).thenReturn(medicalReviewStaticDataDTO);

        //then
        SuccessResponse<MedicalReviewStaticDataDTO> actualResponse = staticDataController.getMedicalReviewStaticData(1L);
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    void testGetMetaFormData() {
        //given
        MetaFormDTO metaFormDTO = TestDataProvider.getMetaFormDTO();

        //when
        when(staticDataService.getMetaFormData(Constants.CONSENT_FORM)).thenReturn(metaFormDTO);

        //then
        SuccessResponse<MetaFormDTO> actualResponse = staticDataController.getMetaFormData(Constants.CONSENT_FORM);
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    void clearStaticData() {
        //when
        doNothing().when(staticDataService).clearStaticData();

        //then
        staticDataController.clearStaticData();
        verify(staticDataService).clearStaticData();
    }

    @Test
    void testCheckAppVersion() {
        //given
        String appVersion = "1.0.6";

        //when
        ReflectionTestUtils.setField(staticDataController, "appVersion", "1.0.5");
        when(staticDataService.checkAppVersion(appVersion)).thenReturn(false);

        //then
        SuccessResponse<Boolean> actualResponse = staticDataController.checkAppVersion(appVersion);
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    void testCheckAppVersionTest() {
        //given
        String appVersion = "1.0.5";

        //when
        ReflectionTestUtils.setField(staticDataController, "appVersion", "1.0.5");
        when(staticDataService.checkAppVersion(appVersion)).thenReturn(true);

        //then
        SuccessResponse<Boolean> actualResponse = staticDataController.checkAppVersion(appVersion);
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }
}