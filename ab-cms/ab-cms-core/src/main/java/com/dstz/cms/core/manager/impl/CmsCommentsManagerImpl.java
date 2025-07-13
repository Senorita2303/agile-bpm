package com.dstz.cms.core.manager.impl;

import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.cms.core.entity.CmsComments;
import com.dstz.cms.core.helper.CmsHelper;
import com.dstz.cms.core.manager.CmsCommentsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Comment form General service implementation class
 *
 */
@Service("cmsCommentsManager")
public class CmsCommentsManagerImpl extends AbBaseManagerImpl<CmsComments> implements CmsCommentsManager {

    @Autowired
    private CmsHelper cmsManager;

    /**
     * Add a comment
     *
     * @param comments Comment object
     */
    @Override
    public int create(CmsComments comments) {
        super.create(comments);
        return cmsManager.updateCommentsNum(comments.getCommentType(), comments.getMsgId(), true);
    }

    /**
     * Delete a comment
     *
     * @param ids Comment ID collection
     */
    @Override
    public int removeByIds(Collection<? extends Serializable> ids) {
        List<CmsComments> cmsCommentsList = getBaseMapper().selectBatchIds(ids.stream().map(Object::toString).collect(Collectors.toList()));
        cmsCommentsList.forEach(s -> cmsManager.updateCommentsNum(s.getCommentType(), s.getMsgId(), false));
        return super.removeByIds(ids);
    }
}