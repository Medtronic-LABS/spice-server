package com.mdtlabs.coreplatform.spiceservice;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * This AwsConfig class generates an Amazon S3 client with a specified region using the AmazonS3ClientBuilder.
 * </p>
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
@Configuration
public class AwsConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * <p>
     * This method is used to generate an Amazon S3 client with a specified region.
     * </p>
     *
     * @return {@link AmazonS3} An instance of the AmazonS3 client generated using the AmazonS3ClientBuilder
     * with the specified region is returned
     */
    @Bean
    public AmazonS3 generateS3client() {
        return AmazonS3ClientBuilder.standard().withRegion(region).build();
    }
}
