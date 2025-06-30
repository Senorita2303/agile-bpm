package com.dstz.cms.core.manager.impl;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.auth.authentication.api.SysResourceApi;
import com.dstz.auth.authentication.api.model.ISysResource;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.cms.core.entity.CmsFastMenu;
import com.dstz.cms.core.entity.vo.CmsFastMenuVO;
import com.dstz.cms.core.manager.CmsFastMenuManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Quick menu management general service implementation class
 *
 */
@Service("cmsFastMenuManager")
public class CmsFastMenuManagerImpl extends AbBaseManagerImpl<CmsFastMenu> implements CmsFastMenuManager {

    @Autowired
    private SysResourceApi sysResourceApi;

    @Override
    public List<CmsFastMenuVO> getCmsFastMenuVO(int type) {
        List<CmsFastMenu> cmsFastMenus = selectByWrapper(Wrappers.<CmsFastMenu>lambdaQuery()
                .eq(CmsFastMenu::getUserId, UserContextUtils.getUserId())
                .eq(CmsFastMenu::getMobile, type));
        List<CmsFastMenuVO> result = new ArrayList<>();
        for (CmsFastMenu fastMenu : cmsFastMenus) {
            ISysResource resource = sysResourceApi.getResourceById(fastMenu.getResourceId());
            if (resource != null) {
                CmsFastMenuVO vo = new CmsFastMenuVO(fastMenu.getId(), fastMenu.getUserId(), fastMenu.getResourceId(), resource.getName(), resource.getUrl(), resource.getIcon());
                result.add(vo);
            }
        }
        // If it is a mobile terminal, you need to determine whether the quick application is included in the commonly used applications. If not, add it.
        if (type == 1) {
            ISysResource todo = sysResourceApi.getTodoResource();
            if (result.stream().noneMatch(s -> StrUtil.equals(s.getResourceId(), todo.getId()))) {
                result.add(new CmsFastMenuVO(todo.getId(), todo.getName(), todo.getUrl(), todo.getIcon()));
            }
        }
        return result;
    }

    @Override
    public String saveOne(String id) {
        CmsFastMenu cmsFastMenu = new CmsFastMenu(id);
        create(cmsFastMenu);
        return cmsFastMenu.getId();
    }

    /**
     * Add shortcut menus in batches
     *
     * @param resourceIdList Menu ID collection
     */
    @Override
    public void saveBatch(List<String> resourceIdList) {
        removeAll();
        Iterable<CmsFastMenu> cmsFastMenuIterable = IterUtil.asIterable(resourceIdList.stream().map(s ->
                new CmsFastMenu(UserContextUtils.getUserId(), s)).iterator());
        bulkCreate(cmsFastMenuIterable);
    }

    /**
     * Clear the shortcut menu for the current user
     */
    @Override
    public void removeAll() {
        remove(Wrappers.<CmsFastMenu>lambdaQuery().eq(CmsFastMenu::getUserId, UserContextUtils.getUserId()));
    }

}
