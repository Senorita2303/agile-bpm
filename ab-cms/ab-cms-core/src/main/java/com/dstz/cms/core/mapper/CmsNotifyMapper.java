package com.dstz.cms.core.mapper;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.cms.core.entity.CmsNotify;
import com.dstz.cms.core.entity.vo.CmsNotifyListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * System Announcement Table Mapper Interface
 * </p>
 *
 */
@Mapper
public interface CmsNotifyMapper extends AbBaseMapper<CmsNotify> {

    List<CmsNotifyListVO> queryNotify(String userId);

    PageListDTO<CmsNotifyListVO> getNotifyPage(DefaultAbQueryFilter defaultAbQueryFilter);
}
