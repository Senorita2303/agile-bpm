package com.dstz.cms.core.manager.impl;

import static com.dstz.base.common.constats.InnerMsgEnum.NEWS;
import static com.dstz.cms.api.constant.CmsConstant.COMMENT_TYPE_NEWS;
import static com.dstz.cms.api.constant.CmsConstant.NEWS_MESSAGE_TEMPLATE;
import static com.dstz.cms.api.constant.CmsConstant.TITLE;
import static com.dstz.cms.api.constant.CmsStatusCode.NEWS_DELETE_DISABLED;
import static com.dstz.cms.api.constant.CmsStatusCode.NEWS_READ_ONLY;
import static com.dstz.component.mq.api.constants.JmsTypeEnum.INNER;
import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.common.constats.InnerMsgEnum;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.identityconvert.SysIdentity;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.cms.core.entity.CmsComments;
import com.dstz.cms.core.entity.CmsNews;
import com.dstz.cms.core.entity.dto.CmsNewsDTO;
import com.dstz.cms.core.helper.CmsHelper;
import com.dstz.cms.core.manager.CmsCommentsManager;
import com.dstz.cms.core.manager.CmsNewsManager;
import com.dstz.cms.core.mapper.CmsNewsMapper;
import com.dstz.component.msg.api.MsgApi;
import com.dstz.component.msg.api.dto.MsgDTO;
import com.dstz.org.api.UserApi;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.constant.SysApiCodes;
import com.google.common.collect.ImmutableMap;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * News table General service implementation class
 *
 */
@Service("cmsNewsManager")
public class CmsNewsManagerImpl extends AbBaseManagerImpl<CmsNews> implements CmsNewsManager {

    @Autowired
    private CmsNewsMapper cmsNewsMapper;
    @Autowired
    private CmsCommentsManager cmsCommentsManager;
    @Autowired
    private UserApi userApi;
    @Autowired
    private CmsHelper cmsHelper;
    @Autowired
    private MsgApi msgApi;

    /**
     * Paginated List
     */
    @Override
    public PageListDTO<CmsNews> listJson(QueryParamDTO paramDTO) {
        return cmsNewsMapper.listJson(new DefaultAbQueryFilter(paramDTO));
    }

    /**
     * Get two fixed, streamlined news on the homepage (excluding attachments and other useless fields)
     */
    @Override
    public PageListDTO<CmsNews> getNewsPage(QueryParamDTO paramDTO) {
        return cmsNewsMapper.getNewsPage(new DefaultAbQueryFilter(paramDTO));
    }


    /**
     * Get an entity by its ID
     *
     * @param id Entity ID
     * @return CmsNewsDTO Entity Dto Record
     */
    @Override
    public CmsNewsDTO details(String id) {
        CmsNews cmsNews = super.getById(id);
        if (cmsNews.getStatus() == 1) {
            // The number of visits increases by 1
            cmsNews.setVisitNum(cmsNews.getVisitNum() + 1);
            super.update(cmsNews);
          //  cmsInnerMsgManager.updateRead(NEWS, id);
        }
        return fillingNew(cmsNews);
    }

    /**
     * Add or modify news
     *
     * @param cmsNews News Object
     */
    @Override
    public void saveOrUpdate(CmsNews cmsNews) {
        validate(cmsNews);
        super.createOrUpdate(cmsNews);
    }

    /**
     * Release News
     *
     * @param id News ID
     */
    @Override
    public void releaseNews(String id) {
        final IUser currentUser = UserContextUtils.getValidUser();
        
        // Update news status
        CmsNews cmsNews = cmsNewsMapper.selectById(id);
        cmsNews.setId(id);
        cmsNews.releaseNews(currentUser.getUserId(), currentUser.getFullName());
        cmsNewsMapper.updateById(cmsNews);

        // Get all users
        QueryParamDTO queryParamDTO = new QueryParamDTO();
        queryParamDTO.setLimit(500);
        queryParamDTO.setOffset(0);
        queryParamDTO.setSearchCount(Boolean.FALSE);
        List<SysIdentity> sysIdentityList = new LinkedList<>();
        PageListDTO<? extends IUser> pageListDTO;
        for (; CollUtil.isNotEmpty(pageListDTO = userApi.queryFilter(queryParamDTO)); queryParamDTO.setOffset(queryParamDTO.getOffset() + queryParamDTO.getLimit())) {
            pageListDTO.stream().map(SysIdentity::new).forEach(sysIdentityList::add);
            if (pageListDTO.size() != queryParamDTO.getLimit()) {
                break;
            }
        }
        CollUtil.clear(pageListDTO);
        
        // Create an associated in-site message (seven parameters, 1 title, 2 template number, 3 receiving user set, 4 business ID, 5 message type, 6 sending channel set, 7 carrying parameters)
        if (CollUtil.isNotEmpty(sysIdentityList)) {
            msgApi.sendMsg(new MsgDTO(
                    TITLE,
                    NEWS_MESSAGE_TEMPLATE,
                    sysIdentityList,
                    id,
                    InnerMsgEnum.NEWS.getKey(),
                    Collections.singletonList(INNER.getType()),
                    ImmutableMap.of(
                            "title", cmsNews.getTitle(),
                            "subject", cmsNews.getTitle(),
                            "senderName", cmsNews.getReleaseName(),
                            "sendTime", DateUtil.formatDateTime(cmsNews.getReleaseTime())))
            );
        }
    }

    /**
     * Removed news
     *
     * @param id News ID
     */
    @Override
    public void withdrawNews(String id) {
        // Change news information
        IUser iUser = UserContextUtils.getUser().get();
        CmsNews cmsNews = super.getById(id);
        cmsNews.withdrawNews(iUser.getUserId(), iUser.getFullName());
        update(cmsNews);
        // After the news is removed, the status of the synchronized messages in the station is expired
      //  cmsInnerMsgManager.expiredRead(NEWS, id);
    }


    /**
     * Delete news collection
     *
     * @param ids News ids collection
     */
    @Override
    public int removeByIds(Collection<? extends Serializable> ids) {
        List<CmsNews> cmsNews = selectByIds(ids.stream().map(Object::toString).collect(toList()));
        for (CmsNews cmsNew : cmsNews) {
            // If the news has been published, do not delete it
            if (cmsNew.getStatus() == 1) {
                throw new BusinessMessage(NEWS_DELETE_DISABLED);
            }
            // Delete attachment information
            cmsHelper.deleteFile(cmsNew.getAttachments());
            // Delete picture information
            cmsHelper.deleteFile(cmsNew.getImages());
            // After the news is removed, the synchronized messages in the station will be expired
         //   cmsInnerMsgManager.expiredRead(NEWS, cmsNew.getId());
        }
        //批量删除新闻
        return super.removeByIds(ids);
    }

    /**
     * Supplement the content of other tables and fill in the news DTO
     *
     * @param cmsNews News entity object
     * @return CmsNewsDTO Filled DTO object
     */
    public CmsNewsDTO fillingNew(CmsNews cmsNews) {
        CmsNewsDTO cmsNewsDTO = new CmsNewsDTO(cmsCommentsManager.selectByWrapper(Wrappers.<CmsComments>lambdaQuery()
                .eq(CmsComments::getCommentType, COMMENT_TYPE_NEWS).eq(CmsComments::getMsgId, cmsNews.getId())));
        BeanCopierUtils.copyProperties(cmsNews, cmsNewsDTO);
        return cmsNewsDTO;
    }

    /**
     * Check for duplicate operations
     *
     * @param cmsNews: The new object after modification
     **/
    private void validate(CmsNews cmsNews) {
        // Filter news with status Published
        if (StrUtil.isNotEmpty(cmsNews.getId()) && getById(cmsNews.getId()).getStatus() == 1) {
            throw new BusinessException(NEWS_READ_ONLY);
        }
        // Name Duplicate Check
        if (StrUtil.isEmpty(cmsNews.getId()) || !StrUtil.equals(getById(cmsNews.getId()).getTitle(), cmsNews.getTitle())) {
            if (selectCount(Wrappers.lambdaQuery(CmsNews.class).eq(CmsNews::getTitle, cmsNews.getTitle())) > 0) {
                throw new BusinessMessage(SysApiCodes.NAME_DUPLICATE);
            }
        }
    }

}
