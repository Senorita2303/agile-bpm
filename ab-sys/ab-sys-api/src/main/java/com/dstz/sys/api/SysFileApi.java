package com.dstz.sys.api;

import com.dstz.sys.api.dto.SysFileDTO;

import java.io.InputStream;

/**
 * @Name SysFileApi
 * @description:File attachment service
 */
public interface SysFileApi {

    /**
     * <pre>
     * Upload attachments
     * </pre>
     *
     * @param is
     * @param fileName
     * @return
     */
    SysFileDTO upload(InputStream is, String fileName);

    /**
     * <pre>
     * Upload or modify an attachment (if the attachment ID exists, modify it; if it does not exist, upload it)
     * </pre>
     *
     * @param is       File stream
     * @param fileId   File ID
     * @param fileName File name
     * @return
     */
    void upload(InputStream is, String fileId, String fileName);

    /**
     * <pre>
     * Download attachment
     * Return Stream
     * </pre>
     *
     * @param fileId
     * @return
     */
    InputStream download(String fileId);

    /**
     * <pre>
     * Delete attachment
     * Includes flow information
     * </pre>
     *
     * @param fileId
     */
    void delete(String... fileId);

	SysFileDTO getById(String fileId);

}
