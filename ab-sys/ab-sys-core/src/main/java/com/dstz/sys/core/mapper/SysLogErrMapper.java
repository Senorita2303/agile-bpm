package com.dstz.sys.core.mapper;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.sys.core.entity.SysLogErr;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * System exception log Mapper interface
 * </p>
 *
 */
@Mapper
public interface SysLogErrMapper extends AbBaseMapper<SysLogErr> {

    /**
     * queryFilter Paginated list query
     * @param abQueryFilter Query parameters
     * @return
     */
    PageListDTO<SysLogErr> query(AbQueryFilter abQueryFilter);

}
