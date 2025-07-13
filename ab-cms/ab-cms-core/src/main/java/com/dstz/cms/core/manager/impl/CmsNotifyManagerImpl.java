package com.dstz.cms.core.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.common.constats.InnerMsgEnum;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.cms.api.constant.CmsStatusCode;
import com.dstz.cms.api.model.FreemarkerData;
import com.dstz.cms.core.entity.CmsComments;
import com.dstz.cms.core.entity.CmsNotify;
import com.dstz.cms.core.entity.CmsNotifyShare;
import com.dstz.cms.core.entity.CmsNotifyUser;
import com.dstz.cms.core.entity.dto.CmsNotifyDTO;
import com.dstz.cms.core.entity.vo.CmsNotifyListVO;
import com.dstz.cms.core.entity.vo.CmsNotifyVO;
import com.dstz.cms.core.helper.CmsHelper;
import com.dstz.cms.core.manager.*;
import com.dstz.cms.core.mapper.CmsNotifyMapper;
import com.dstz.component.mq.api.constants.JmsTypeEnum;
import com.dstz.component.mq.api.model.DefaultJmsDTO;
import com.dstz.component.msg.api.MsgApi;
import com.dstz.component.msg.api.dto.MsgDTO;
import com.dstz.org.api.model.IUser;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.dstz.base.common.constats.InnerMsgEnum.NOTIFY;
import static com.dstz.cms.api.constant.CmsConstant.*;
import static com.dstz.cms.api.constant.CmsStatusCode.NOTIFY_DELETE_DISABLED;
import static com.dstz.cms.api.constant.CmsStatusCode.NOTIFY_READ_ONLY;
import static com.dstz.component.mq.api.constants.JmsTypeEnum.INNER;
import static com.dstz.sys.api.constant.SysApiCodes.NAME_DUPLICATE;
import static java.util.stream.Collectors.toList;


/**
 * System Announcement Table General Service Implementation Class
 *
 */
@Service("cmsNotifyManager")
public class CmsNotifyManagerImpl extends AbBaseManagerImpl<CmsNotify> implements CmsNotifyManager {

    @Autowired
    private CmsNotifyUserManager cmsNotifyUserManager;
    @Autowired
    private CmsNotifyShareManager cmsNotifyShareManager;
    @Autowired
    private CmsCommentsManager cmsCommentsManager;
    @Autowired
    private CmsNotifyMapper cmsNotifyMapper;
    @Autowired
    private CmsHelper cmsHelper;
    @Autowired
    private MsgApi msgApi;

    /**
     * Announcement paging query
     *
     * @param queryParamDto Query condition object
     * @return PageListDTO<CmsNotifyDTO> Announcement query result paging Page object
     */
    @Override
    public PageListDTO<CmsNotifyVO> page(QueryParamDTO queryParamDto) {
        PageListDTO<CmsNotify> result = query(new DefaultAbQueryFilter(queryParamDto, null));
        List<CmsNotifyVO> cmsNotifyVOList = result.getRows().stream().map(this::fillingNotify).collect(toList());
        return new PageListDTO<>(result.getPageSize(), result.getPage(), result.getTotal(), cmsNotifyVOList);
    }

    /**
     * Query the announcement content of the specified ID
     *
     * @param id Announcement ID
     * @return cmsNotifyDto Announcement Dto details object
     */
    @Override
    public CmsNotifyVO details(String id) {
        CmsNotify cmsNotify = super.getById(id);
        if (STATUS_PUBLISHED == cmsNotify.getStatus()) {
            relationOperate(cmsNotify);
        }
        CmsNotifyVO notify = fillingNotify(cmsNotify);
        // Populate the list of associated comments
        notify.setCmsCommentsList(cmsCommentsManager.selectByWrapper(Wrappers.<CmsComments>lambdaQuery()
                .eq(CmsComments::getCommentType, COMMENT_TYPE_NOTIFY).eq(CmsComments::getMsgId, cmsNotify.getId())));
        return notify;
    }

    /**
     * New announcement
     *
     * @param cmsNotifyDTO Announcement DTO object
     */
    @Override
    public void save(CmsNotifyDTO cmsNotifyDTO) {
        validate(null, cmsNotifyDTO);
        try {
            create(cmsNotifyDTO);
        } catch (DataIntegrityViolationException e) {
            String errorStr = e.getLocalizedMessage();
            if (errorStr.contains("Data too long for column")) {
                String str = errorStr.substring(errorStr.indexOf("'") + 1);
                String column = str.substring(0, str.indexOf("'"));
                throw new BusinessException(CmsStatusCode.COLUMN_TOO_LONG.formatMessage("The field [{}] is too long and exceeds the database field limit!", column));
            } else {
                throw new BusinessException(CmsStatusCode.SAVE_ERROR);
            }
        }
        cmsNotifyShareManager.saveRelation(cmsNotifyDTO);
    }

    /**
     * Modification Announcement
     *
     * @param cmsNotifyDTO Modified announcement DTO object
     */
    @Override
    public void update(CmsNotifyDTO cmsNotifyDTO) {
        CmsNotify notify = super.getById(cmsNotifyDTO.getId());
        validate(notify, cmsNotifyDTO);
        try {
            super.update(cmsNotifyDTO);
        } catch (DataIntegrityViolationException e) {
            String errorStr = e.getLocalizedMessage();
            if (errorStr.contains("Data too long for column")) {
                String str = errorStr.substring(errorStr.indexOf("'") + 1);
                String column = str.substring(0, str.indexOf("'"));
                throw new BusinessException(CmsStatusCode.COLUMN_TOO_LONG.formatMessage("The field [{}] is too long and exceeds the database field limit!", column));
            } else {
                throw new BusinessException(CmsStatusCode.SAVE_ERROR);
            }
        }
        // Delete the old department association, file association, etc. and save the new associated department
        cmsNotifyShareManager.deleteByNotifyId(notify.getId());
        cmsHelper.deleteFile(cmsNotifyDTO.getAttachments());
        cmsNotifyShareManager.saveRelation(cmsNotifyDTO);
    }

    /**
     * View the list of announcements to which you belong (filter the organizations associated with the announcement)
     */
    @Override
    public PageListDTO<CmsNotifyListVO> getNotifyPage(QueryParamDTO paramDTO) {
        DefaultAbQueryFilter queryFilter = new DefaultAbQueryFilter(paramDTO);
        queryFilter.addParam("userId", UserContextUtils.getUserId());
        return cmsNotifyMapper.getNotifyPage(queryFilter);
    }

    /**
     * Query the number of unread announcements for a user
     */
    @Override
    public int queryUnReadCount() {
        // The collection of announcements associated with the current user
        List<String> notifyIdList = cmsNotifyShareManager.getNotifyListByCurrentUser();
        // Remove the read announcement collection
        notifyIdList.removeAll(cmsNotifyUserManager.getNotifyListByCurrentUser());
        if (CollectionUtil.isEmpty(notifyIdList)) {
            return 0;
        }
        long count = selectCount(Wrappers.<CmsNotify>lambdaQuery().eq(CmsNotify::getStatus, STATUS_PUBLISHED).in(CmsNotify::getId, notifyIdList));
        return Math.toIntExact(count);
    }

    /**
     * Release announcement
     *
     * @param id Announcement ID
     */
    @Override
    public void releaseNotify(String id) {
        // Change announcement information
        IUser iUser = UserContextUtils.getUser().get();
        CmsNotify cmsNotify = super.getById(id);
        cmsNotify.releaseNotify(iUser.getUserId(), iUser.getFullName());
        update(cmsNotify);
        // Create an associated in-site message (seven parameters, 1 title, 2 template number, 3 receiving user set, 4 business ID, 5 message type, 6 sending channel set, 7 carrying parameters)
        msgApi.sendMsg(new MsgDTO(
                TITLE,
                NOTIFY_MESSAGE_TEMPLATE,
                cmsNotifyShareManager.getUserListByNotifyId(id),
                id,
                InnerMsgEnum.NOTIFY.getKey(),
                Collections.singletonList(INNER.getType()),
                ImmutableMap.of(
                        "title", cmsNotify.getTitle(),
                        "subject", cmsNotify.getTitle(),
                        "senderName", cmsNotify.getReleaseName(),
                        "sendTime", DateUtil.formatDateTime(cmsNotify.getReleaseTime())))
        );
    }

    /**
     * Delisting Notice
     *
     * @param id Announcement ID
     */
    @Override
    public void withdrawNotify(String id) {
        // Change announcement information
        IUser iUser = UserContextUtils.getUser().get();
        CmsNotify cmsNotify = super.getById(id);
        cmsNotify.withdrawNotify(iUser.getUserId(), iUser.getFullName());
        update(cmsNotify);
        // Delete the read mark after delisting, and delete the messages in the site synchronously
        cmsNotifyUserManager.deleteByNotifyId(cmsNotify.getId());
        // Set the associated in-site message to expire
       // cmsInnerMsgManager.expiredRead(NOTIFY, id);
    }

    /**
     * Delete announcement collection
     *
     * @param ids Announcement ids collection
     */
    @Override
    public int removeByIds(Collection<? extends Serializable> ids) {
        for (CmsNotify cmsNotify : selectByIds(ids.stream().map(Object::toString).collect(Collectors.toList()))) {
            if (STATUS_PUBLISHED == cmsNotify.getStatus()) {
                throw new BusinessException(NOTIFY_DELETE_DISABLED);
            }
            // Delete associated organization information, user read data, bound file information, site messages, etc.
            cmsNotifyShareManager.deleteByNotifyId(cmsNotify.getId());
            cmsNotifyUserManager.deleteByNotifyId(cmsNotify.getId());
            cmsHelper.deleteFile(cmsNotify.getAttachments());
            //  cmsInnerMsgManager.expiredRead(NOTIFY, cmsNotify.getId());
        }
        // Batch delete announcements
        return super.removeByIds(ids);
    }

    /**
     * Supplement the contents of other tables and fill in the announcement DTO
     *
     * @param cmsNotify Announcement Entity Object
     * @return CmsNotifyDTO Filled DTO object
     */
    private CmsNotifyVO fillingNotify(CmsNotify cmsNotify) {
        CmsNotifyVO notifyVO = BeanCopierUtils.transformBean(cmsNotify, CmsNotifyVO.class);
        notifyVO.setCmsNotifyShareList(cmsNotifyShareManager.selectByWrapper(Wrappers.<CmsNotifyShare>lambdaQuery()
                .eq(CmsNotifyShare::getNotifyId, cmsNotify.getId())));
        return notifyVO;
    }

    /**
     * Related operations associated with announcements, determine whether it has been read, create read objects, and update the number of reads
     *
     * @param cmsNotify Announcement Entity Object
     */
    private void relationOperate(CmsNotify cmsNotify) {
        String userId = UserContextUtils.getUserId();
        // If the current user has not read the announcement, create an associated read relationship
        if (cmsNotifyUserManager.getNotifyUser(cmsNotify.getId(), userId) == null) {
            cmsNotifyUserManager.create(new CmsNotifyUser(cmsNotify.getId(), userId));
        }
        //     cmsInnerMsgManager.updateRead(NOTIFY, cmsNotify.getId());
        // Regardless of whether it has been read, the reading volume +1
        cmsNotify.setVisitNum(cmsNotify.getVisitNum() + 1);
        update(cmsNotify);
    }

    /**
     * Check for duplicate operations
     *
     * @param old:       The old object
     * @param cmsNotify: The new object after modification
     **/
    private void validate(CmsNotify old, CmsNotify cmsNotify) {
        // Filter announcements with a status of Published
        if (null != old && old.getStatus() == 1) {
            throw new BusinessException(NOTIFY_READ_ONLY);
        }
        // Name Duplicate Check
        if (old == null || !StrUtil.equals(old.getTitle(), cmsNotify.getTitle())) {
            if (selectCount(Wrappers.lambdaQuery(CmsNotify.class).eq(CmsNotify::getTitle, cmsNotify.getTitle())) > 0) {
                throw new BusinessMessage(NAME_DUPLICATE);
            }
        }
    }

}