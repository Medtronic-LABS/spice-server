package com.mdtlabs.coreplatform.spiceadminservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.ssm.SsmClient;

/**
 * <p>
 * This class has the test methods for Aws Config class.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class AwsConfigTest {

    @InjectMocks
    AwsConfig awsConfig;

    @Test
    void generateSsmClient() {
        //given
        ReflectionTestUtils.setField(awsConfig, "regionCloud", "us-east-1");

        //then
        SsmClient actualSsmClient = awsConfig.generateSsmClient();
        assertNotNull(actualSsmClient);
    }
}