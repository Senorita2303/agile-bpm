package com.dstz.component.uploader.oss;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectResult;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.upload.api.IUploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.Date;

/**
 * alibaba oss Upload Implementation
 *
 */
public class AliOssUploader implements IUploader {

    private static final Logger logger = LoggerFactory.getLogger(AliOssUploader.class);

    @Autowired
    private OSS ossClient;

    @Autowired
    private AliOssProperties aliOssProperties;

    @Override
    public String type() {
        return "oss";
    }

    @Override
    public String upload(InputStream inputStream, String s, String type) {
        final String key = StrUtil.join("/", new String[]{aliOssProperties.getBaseDir(), type, DateUtil.format(new Date(), "yyyyMMdd"), s});
        logger.debug("aliyun oss upload file {}", key);
        PutObjectResult result = ossClient.putObject(aliOssProperties.getBucketName(), key, inputStream);
        if (logger.isDebugEnabled()) {
            logger.debug("aliyun oss upload response {}", JsonUtils.toJSONString(result));
        }
        return key;
    }

    @Override
    public InputStream take(String s) {
        logger.debug("aliyun oss get file content {}", s);
        return ossClient.getObject(aliOssProperties.getBucketName(), s).getObjectContent();
    }

    @Override
    public void remove(String s) {
        ossClient.deleteObject(aliOssProperties.getBucketName(), s);
    }
}
