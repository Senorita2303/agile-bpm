package com.dstz.component.mq.msg.engine.core.mapper;

import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.component.mq.msg.engine.core.entity.AbMessageLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Third-party message sending log Mapper interface
 * </p>
 *
 */
@Mapper
public interface AbMessageLogMapper extends AbBaseMapper<AbMessageLog> {

}
