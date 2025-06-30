package com.dstz.sys.core.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.sys.core.entity.SysLogErr;
import com.dstz.sys.core.manager.SysLogErrManager;
import org.springframework.stereotype.Service;

/**
 * System exception log General service implementation class
 *
 */
@Service("sysLogErrManager")
public class SysLogErrManagerImpl extends AbBaseManagerImpl<SysLogErr> implements SysLogErrManager {

    /**
     * The exception log only allows the status to be changed, and other fields can only be echoed and cannot be modified
     * @param entity Entity
     * @return
     */
    @Override
    public int update(SysLogErr entity) {
        return update(null, Wrappers.lambdaUpdate(SysLogErr.class)
                .eq(SysLogErr::getId, entity.getId())
                .set(SysLogErr::getStatus, entity.getStatus())
                .set(SysLogErr::getIpAddress,entity.getIpAddress()));
    }

}
