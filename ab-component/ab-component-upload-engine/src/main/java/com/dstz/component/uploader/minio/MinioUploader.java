package com.dstz.component.uploader.minio;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.component.upload.api.IUploader;
import com.dstz.component.uploader.constant.UploadCodes;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import static com.dstz.component.uploader.constant.UploadCodes.*;

/**
 * <pre>
 * Minio Implementation
 * </pre>
 *
 */
public class MinioUploader implements IUploader {

    @Autowired
    private MinioTemplate minIoTemplate;

    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String type() {
        return "minio";
    }

    @Override
    public String upload(InputStream is, String name, String type) {
        String id = IdUtil.randomUUID();
        try {
            String bucketName = minioProperties.getBucketName();
            if (StrUtil.isEmpty(bucketName)) {
                throw new BusinessException(MINIO_CONF_MISS.formatDefaultMessage("ab.minio.bucketName"));
            }
            // Determine whether the bucket exists
            if (!minIoTemplate.bucketExists(bucketName)) {
                minIoTemplate.makeBucket(bucketName);
            }
            String pathStr = getPath(name, type);
            minIoTemplate.putObject(bucketName, pathStr, is);
            return pathStr;
        } catch (Exception e) {
            throw new BusinessException(UploadCodes.MINIO_ERROR.formatDefaultMessage(e.getMessage()), e);
        }
    }


    @Override
    public InputStream take(String path) {
        try {
            return minIoTemplate.getObject(minioProperties.getBucketName(), path);
        } catch (Exception e) {
            throw new BusinessException(MINIO_GET_FILE_ERROR.formatDefaultMessage(e.getMessage()), e);
        }
    }

    @Override
    public void remove(String path) {
        try {
            minIoTemplate.removeObject(minioProperties.getBucketName(), path);
        } catch (Exception e) {
            throw new BusinessException(MINIO_DEL_FILE_ERROR.formatDefaultMessage(e.getMessage()), e);
        }
    }

    private String getPath(String name, String type) {
        return type + StrPool.SLASH + DateUtil.format(new Date(), "yyyyMMdd") + StrPool.SLASH + name;
    }

}
