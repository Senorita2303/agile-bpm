package com.dstz.sys.core.manager.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.IdGeneratorUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.component.upload.api.IUploader;
import com.dstz.component.upload.api.UploaderFactory;
import com.dstz.sys.api.constant.SysApiCodes;
import com.dstz.sys.core.entity.SysFile;
import com.dstz.sys.core.manager.SysFileManager;
import com.dstz.sys.core.mapper.SysFileMapper;
import com.dstz.sys.rest.model.dto.UpdateFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * System attachments General service implementation class
 *
 */
@Service("sysFileManager")
public class SysFileManagerImpl extends AbBaseManagerImpl<SysFile> implements SysFileManager {
    @Autowired
    SysFileMapper sysFileMapper;

    @Override
    public SysFile upload(InputStream is, String fileName, String type) {
        return upload(is, IdGeneratorUtils.nextId(), fileName, type);
    }

    @Override
    public SysFile upload(InputStream is, String fileId, String fileName, String type) {
        String ext = "";
        if (fileName.contains(StrPool.DOT)) {
            ext = fileName.substring(fileName.lastIndexOf('.'));
        }

        // 1 Upload the file first, use the id as the file name to ensure no duplication
        IUploader uploader = UploaderFactory.getDefault();
        String path = uploader.upload(is, fileId + ext, type);

        // 2 Create SysFile data
        SysFile sysFile = new SysFile();
        sysFile.setId(fileId);
        sysFile.setName(fileName);
        sysFile.setUploader(uploader.type());
        sysFile.setPath(path);

        sysFile.setCreateOrgId(UserContextUtils.getGroupId());
        sysFile.setCreateBy(UserContextUtils.getUserId());
        sysFile.setTypeCode(type);
        create(sysFile);

        return sysFile;
    }

    @Override
    public String uploader(InputStream is, String fileId, String fileName, String type) {
        String ext = "";
        if (fileName.contains(StrPool.DOT)) {
            ext = fileName.substring(fileName.lastIndexOf(StrPool.DOT));
        }
        return UploaderFactory.getDefault().upload(is, fileId + ext, type);
    }

    @Override
    public int update(UpdateFileDTO updateDTO) {
        SysFile sysFile = getById(updateDTO.getFileId());
        if (sysFile == null) {
            throw new BusinessMessage(SysApiCodes.FILE_NOT_FOUND_ERROR.formatDefaultMessage(updateDTO.getFileId()));
        }

        String fileName = sysFile.getName();
        String ext = "";
        if (fileName.contains(StrPool.DOT)) {
            ext = fileName.substring(fileName.lastIndexOf('.'));
        }

        // 1 Upload the file first, and use id+timestamp as the file name to ensure no duplication
        IUploader uploader = UploaderFactory.getDefault();

        // Create a new file path with the old id + time
        String idDate = sysFile.getId() + "-" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
        String path = uploader.upload(updateDTO.getFileStream(), idDate + ext, sysFile.getTypeCode());

        // 2 Update SysFile data, update only
        return super.update(null, Wrappers.lambdaUpdate(SysFile.class)
                .eq(SysFile::getId, sysFile.getId())
                .set(SysFile::getPath, path)
                .set(SysFile::getUpdateBy, updateDTO.getUserId())
                .set(SysFile::getUpdater, updateDTO.getUserName())
                .set(SysFile::getUpdateTime, new Date())
                .set(SysFile::getUploader, uploader.type()));
    }

    @Override
    public InputStream download(String fileId) {
        SysFile sysFile = sysFileMapper.selectById(fileId);
        if (sysFile == null) {
            throw new BusinessMessage(SysApiCodes.FILE_NOT_FOUND_ERROR.formatDefaultMessage(sysFile.getName()));
        }
        IUploader uploader = UploaderFactory.getUploader(sysFile.getUploader());
        InputStream is = uploader.take(sysFile.getPath());
        if (is == null) {
            throw new BusinessMessage(SysApiCodes.FILE_NOT_FOUND_ERROR.formatDefaultMessage(sysFile.getName()));
        }
        return is;
    }

    @Override
    public void delete(String fileId) {
        SysFile sysFile = sysFileMapper.selectById(fileId);

        if (sysFile != null) {
            // Do logical deletion
            sysFile.setDelete(1);
            update(sysFile);
        }
    }
}
