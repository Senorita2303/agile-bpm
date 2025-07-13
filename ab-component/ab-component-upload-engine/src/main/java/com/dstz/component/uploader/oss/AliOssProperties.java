package com.dstz.component.uploader.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * oss Property Configuration
 *
 */
@ConfigurationProperties(prefix = "spring.cloud.alicloud.oss")
public class AliOssProperties {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    /**
     * bucket name
     */

    private String bucketName;

    /**
     * base dir
     */
    private String baseDir;

    public String getBucketName() {
        return bucketName;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
