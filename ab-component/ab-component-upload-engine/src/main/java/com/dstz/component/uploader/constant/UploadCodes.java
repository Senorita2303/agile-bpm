package com.dstz.component.uploader.constant;

import com.dstz.base.common.codes.IBaseCode;

public enum UploadCodes implements IBaseCode {
    //minio
    MINIO_ERROR("minio_error", "minio exception:{}"),
    MINIO_BUCKET_EXISTS_ERROR("minio_bucket_exists_error", "Determine whether there is an interface error in minio bucket:{}"),

    MINIO_CONF_MISS("minio_conf_miss", "minio is missing configuration: {}"),
    MINIO_GET_FILE_ERROR("minio_get_file_error", "minio failed to obtain the file:{}"),
    MINIO_DEL_FILE_ERROR("minio_del_file_error", "minio failed to remove the file:{}"),
    CREATE_BUCKET_ERROR("create_bucket_error", "Error in creating minio bucket interface: {}"),
    MINIO_GET_ALL_BUCKET_FILE("minio_get_all_bucket_file", "Minio gets all objects in the bucket. Exception: {}"),
    MINIO_GET_OBJ_FILE_ERROR("minio_get_obj_file_error", "minio Error in getting object metadata:{}"),

    MINIO_GET_OBJ_URL_ERROR("minio_get_obj_url_error", "minio file access path error: {}"),


    ;

    private final String code;

    private final String message;

    UploadCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }


}
