package ru.kpfu.itis.kononenko.gtree2.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kpfu.itis.kononenko.gtree2.config.property.MinioProperties;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties properties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(properties.getUrl())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }

    //TOdo создавать с доступом access
//    @Bean
//    public CommandLineRunner ensureBucketExists(MinioClient minioClient) {
//        return args -> {
//            boolean exists = minioClient.bucketExists(
//                    BucketExistsArgs.builder()
//                            .bucket(properties.getBucket())
//                            .build()
//            );
//            if (!exists) {
//                minioClient.makeBucket(
//                        MakeBucketArgs.builder()
//                                .bucket(properties.getBucket())
//                                .build()
//                );
//                System.out.printf("Bucket '%s' created.%n", properties.getBucket());
//            } else {
//                System.out.printf("Bucket '%s' already exists.%n", properties.getBucket());
//            }
//        };
//    }
}