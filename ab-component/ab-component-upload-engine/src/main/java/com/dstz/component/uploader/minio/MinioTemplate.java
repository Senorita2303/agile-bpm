package com.dstz.component.uploader.minio;


import cn.hutool.core.date.DateUtil;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.ApiException;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.component.uploader.constant.UploadCodes;
import io.minio.*;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.dstz.component.uploader.constant.UploadCodes.*;

/**
 * <pre>
 * minio template
 * </pre>
 *
 */
public class MinioTemplate {


    private MinioClient client;

    private String endpoint, accessKey, secretKey;

    public MinioTemplate(String endpoint, String accessKey, String secretKey) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        if (client == null) {
            try {
                client = getMinioClient();
            } catch (InvalidPortException e) {
                e.getStackTrace();
            } catch (InvalidEndpointException e) {
                e.getStackTrace();
            }
        }
    }

    public MinioClient getMinioClient() throws InvalidPortException, InvalidEndpointException {
        return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }


    /**
     * Determine whether the bucket exists
     *
     * @param bucketName bucket
     * @return
     */

    public boolean bucketExists(String bucketName) {

        try {
            return client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new ApiException(UploadCodes.MINIO_BUCKET_EXISTS_ERROR.formatDefaultMessage(e.getMessage()), e);
        }

    }


    /**
     * Create a bucket
     *
     * @param bucketName bucket
     */

    public void makeBucket(String bucketName) {

        try {
            boolean isExist = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new ApiException(CREATE_BUCKET_ERROR.formatDefaultMessage(e.toString()), e);
        }

    }


    /**
     * File Upload
     *
     * @param bucketName bucket
     * @param objectName file name
     * @param stream     File Stream
     */

    public void putObject(String bucketName, String objectName, InputStream stream) {
        try {
            client.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, stream.available(), -1).build());
        } catch (Exception e) {
            throw new ApiException(MINIO_ERROR.formatDefaultMessage(e.toString(), e));
        }

    }


    /**
     * Delete files
     *
     * @param bucketName bucket
     * @param objectName file name
     * @return
     */

    public void removeObject(String bucketName, String objectName) {

        try {
            client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            throw new ApiException(MINIO_DEL_FILE_ERROR.formatDefaultMessage(e.toString()), e);
        }

    }


    /**
     * List all objects in a bucket
     *
     * @param bucketName Bucket Bucket name
     * @return All objects in the bucket
     */
    public Iterable<Result<Item>> listObjects(String bucketName) {
        try {
            boolean flag = bucketExists(bucketName);
            if (flag) {
                return client.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new ApiException(MINIO_GET_ALL_BUCKET_FILE.formatDefaultMessage(e.toString()), e);
        }
        return null;

    }

    /**
     * Get a file object as a stream
     *
     * @param bucketName Bucket Bucket name
     * @param objectName Filename The name of the object in the bucket.
     * @return File Stream
     */

    public InputStream getObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                InputStream stream = null;
                try {
                    //stream = client.getObject(bucketName, objectName);
                    stream = client.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
                } catch (Exception e) {
                    throw new ApiException(MINIO_GET_FILE_ERROR.formatDefaultMessage(e.toString()), e);
                }
                return stream;
            }
        }
        return null;
    }

    /**
     * Get metadata of an object
     *
     * @param bucketName Bucket Bucket name
     * @param objectName Filename The name of the object in the bucket.
     * @return Metadata
     */
    public ObjectStat statObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            ObjectStat statObject = null;
            try {
                statObject = client.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
            } catch (Exception e) {
                throw new ApiException(MINIO_GET_OBJ_FILE_ERROR.formatDefaultMessage(e.toString()), e);
            }
            return statObject;
        }
        return null;
    }

    /**
     * File access path
     *
     * @param bucketName Bucket Bucket name
     * @param objectName Filename The name of the object in the bucket.
     * @return File url
     */
    public String getObjectUrl(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            try {
                url = client.getObjectUrl(bucketName, objectName);
            } catch (Exception e) {
                throw new ApiException(MINIO_GET_OBJ_URL_ERROR.formatDefaultMessage(e.toString()), e);
            }
        }
        return url;
    }

}