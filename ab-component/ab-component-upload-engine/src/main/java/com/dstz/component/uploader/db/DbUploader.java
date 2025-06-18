package com.dstz.component.uploader.db;

import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.utils.IdGeneratorUtils;
import com.dstz.component.upload.api.IUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * <pre>
 * Description: Database uploader
 * Convert the stream into bytes and put it into the database
 * </pre>
 *
 */
@Service
public class DbUploader implements IUploader {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public String type() {
        return "db";
    }

    @Override
    public String upload(InputStream is, String name,String type) {
        try {
            final String id = IdGeneratorUtils.nextId();
            jdbcTemplate.update("INSERT INTO db_uploader VALUES (?,?)", ps -> {
                ps.setString(1, id);
                ps.setBinaryStream(2, is);
            });
            return id;
        } catch (Exception e) {
            if (e.getCause().toString().contains("You can change this value on the server by setting the 'max_allowed_packet' variable")) {
                throw new BusinessException(GlobalApiCodes.INTERNAL_ERROR.formatMessage("The attachment is too large (database level)"));
            }
            throw new BusinessException(GlobalApiCodes.INTERNAL_ERROR.formatDefaultMessage(e));
        }
    }

    @Override
    public InputStream take(String path) {
        return jdbcTemplate.queryForObject("SELECT bytes_ FROM db_uploader WHERE id_ = ?", (rs, rowNum) -> rs.getBinaryStream("bytes_"), path);
    }

    @Override
    public void remove(String path) {
        jdbcTemplate.update("delete from db_uploader where id_ = ?", path);
    }

}
