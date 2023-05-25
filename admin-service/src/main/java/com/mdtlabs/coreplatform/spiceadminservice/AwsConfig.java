package com.mdtlabs.coreplatform.spiceadminservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;

/**
 * <p>
 * This AwsConfig class generates an AWS Systems Manager client with a specified region.
 * </p>
 *
 * @author Nandhakumar Created on 23 Feb 2023
 */
@Configuration
public class AwsConfig {

    @Value("${cloud.aws.region.static}")
    private String regionCloud;

    /**
     * <p>
     * This method is used to generate an AWS Systems Manager client with a specified region.
     * </p>
     *
     * @return AmazonS3 {@link SsmClient}  The SsmClient which is built using the specified region is returned
     */
    @Bean
    public SsmClient generateSsmClient() {
        Region region = Region.of(regionCloud);
        return SsmClient.builder().region(region).build();
    }
}
