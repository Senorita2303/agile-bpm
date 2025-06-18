package com.dstz.component.uploader.config;

import com.dstz.component.uploader.oss.AliOssProperties;
import com.dstz.component.uploader.oss.AliOssUploader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Ali OSS configuration
 */
@Configuration
@ConditionalOnClass(com.aliyun.oss.OSS.class)
@EnableConfigurationProperties(AliOssProperties.class)
public class AliOssConfiguration {


    @Bean
    public AliOssUploader aliOssUploader(){
        return new AliOssUploader();
    }
}
