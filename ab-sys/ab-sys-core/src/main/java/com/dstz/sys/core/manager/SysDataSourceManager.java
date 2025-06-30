package com.dstz.sys.core.manager;

import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.rest.model.dto.SysDataSourceSaveDTO;
import com.dstz.sys.core.entity.SysDataSource;

import java.util.List;

/**
 * <p>
 * Data source General business class
 * </p>
 *
 */
public interface SysDataSourceManager extends AbBaseManager<SysDataSource> {

    /**
     * Determine whether an alias exists
     *
     * @param alias
     * @param id
     * @return
     */
    boolean isExist(String alias, String id);

    /**
     * Save data source
     *
     * @param saveDTO
     * @return
     */
    int save(SysDataSourceSaveDTO saveDTO);


    List<SysDataSource> query(QueryParamDTO dto);
}
