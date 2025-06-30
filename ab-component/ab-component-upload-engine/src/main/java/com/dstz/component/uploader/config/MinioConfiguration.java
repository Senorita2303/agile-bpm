package com.dstz.component.uploader.config;

import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.ApiException;
import com.dstz.component.uploader.minio.MinioProperties;
import com.dstz.component.uploader.minio.MinioTemplate;
import com.dstz.component.uploader.minio.MinioUploader;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * minio configuration
 */
@Configuration
@ConditionalOnMissingBean(MinioTemplate.class)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfiguration {

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    @ConditionalOnProperty(name = "uploader.default",havingValue = "minio")
    public MinioTemplate minioTemplate() {
            return new MinioTemplate(minioProperties.getEndpoint(),minioProperties.getAccessKey(),minioProperties.getSecretKey());
    }

    @Bean
    @ConditionalOnProperty(name = "uploader.default",havingValue = "minio")
    public MinioUploader minioUploader() {
        return new MinioUploader();
    }

}
