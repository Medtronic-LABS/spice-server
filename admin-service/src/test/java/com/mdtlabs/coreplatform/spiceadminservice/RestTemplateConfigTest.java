package com.mdtlabs.coreplatform.spiceadminservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * This class has the test methods for Rest Template Config class.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class RestTemplateConfigTest {
    @InjectMocks
    RestTemplateConfig restTemplateConfig;

    @Test
    void getRestTemplate() {
        //given
        RestTemplateResponseErrorHandler restTemplateResponseErrorHandler = new RestTemplateResponseErrorHandler();
        ReflectionTestUtils.setField(restTemplateConfig, "connectionTimeOut", 1L);
        ReflectionTestUtils.setField(restTemplateConfig, "readTimeOut", 1L);

        //then
        RestTemplate restTemplate = restTemplateConfig.getRestTemplate(restTemplateResponseErrorHandler);
        assertNotNull(restTemplate);
    }
}