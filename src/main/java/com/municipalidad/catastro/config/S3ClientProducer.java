package com.municipalidad.catastro.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@ApplicationScoped
public class S3ClientProducer {

    @Produces
    @ApplicationScoped
    public S3Client createS3Client() {
        String region = System.getenv().getOrDefault("AWS_REGION", "us-east-1");
        
        return S3Client.builder()
            .region(Region.of(region))
            .build();
    }
}
