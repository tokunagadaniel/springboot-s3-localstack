package br.com.tokunaga.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${aws.region}")
    private String region;

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.access.key}")
    private String secretKey;

    @Value("${aws.s3.endpoint}")
    private String endpoint;

    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration())
                .withCredentials(credentialsProvider())
                .build();
    }

    private AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    }

    private AWSStaticCredentialsProvider credentialsProvider() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(accessKey, secretKey)
        );
    }
}