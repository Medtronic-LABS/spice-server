package com.mdtlabs.coreplatform.spiceservice;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AwsConfigTest {

    @InjectMocks
    AwsConfig awsConfig;

    @Test
    void generateS3client() {
        //given
        ReflectionTestUtils.setField(awsConfig, "region", "us-east-1");

        //then
        AmazonS3 actualAmazonS3 = awsConfig.generateS3client();
        assertNotNull(actualAmazonS3);
    }
}