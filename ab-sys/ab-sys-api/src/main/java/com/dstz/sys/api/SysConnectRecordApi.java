package com.dstz.sys.api;

import com.dstz.sys.api.dto.SysConnectRecordDTO;
import com.dstz.sys.api.vo.SysConnectRecordVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * @Name SysConnectRecordApi
 * @description: Public business association record interface
 */
@Validated
public interface SysConnectRecordApi {

    /**
     * Get the associated source by targetId
     *
     * @param targetId
     * @param type
     * @return
     */
    List<SysConnectRecordVO> getByTargetId(String targetId, String type);

    /**
     * Get related information by SourceID and type
     *
     * @param sourceIds Resource id collection
     * @param type
     * @return
     */
    List<SysConnectRecordVO> getBySourceId(List<String> sourceIds, String type);

    /**
     * Batch save
     *
     * @param records
     */
    void save(@Valid List<SysConnectRecordDTO> records);

    /**
     * Batch save
     *
     * @param records
     */
    void save(@Valid SysConnectRecordDTO records);

    /**
     * Delete by sourceId
     *
     * @param type
     * @param id
     */
    void removeBySourceId(String id, String type);

    /**
     * Check if it is associated
     *
     * @param targetId
     * @param type
     */
    void checkIsRelatedWithBusinessMessage(String targetId, String type);
}
