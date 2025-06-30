package com.dstz.sys.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.core.entity.SysFile;
import com.dstz.sys.rest.model.dto.UpdateFileDTO;

import java.io.InputStream;

/**
 * <p>
 * System accessories General business category
 * </p>
 *
 */
public interface SysFileManager extends AbBaseManager<SysFile> {
    /**
     * <pre>
     * Upload attachments
     * </pre>
     *
     * @param is
     * @param fileName
     * @param type
     * @return
     */
    SysFile upload(InputStream is, String fileName, String type);

    /**
     * <pre>
     * Upload the attachment with the specified ID
     * </pre>
     *
     * @param is
     * @param fileId
     * @param fileName
     * @param type
     * @return
     */
    SysFile upload(InputStream is, String fileId, String fileName, String type);

    /**
     * <pre>
     * Only upload attachments in the uploader, no data for attachment information is created.
     * </pre>
     *
     * @param is
     * @param fileId
     * @param fileName
     * @param type
     * @return
     */
    String uploader(InputStream is, String fileId, String fileName, String type);

    /**
     * <pre>
     * Download attachment
     * Return flow
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
    void delete(String fileId);

    /**
     * <pre>
     * Update attachments
     * Update only updates the data. The path id in the sys_file data will not change.
     * Will keep adding db_upload Data in the table
     * </pre>
     *
     * @param updateDTO
     * @return
     */
    int update(UpdateFileDTO updateDTO);
}
