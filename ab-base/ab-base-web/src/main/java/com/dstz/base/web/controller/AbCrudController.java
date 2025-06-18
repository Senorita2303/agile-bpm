package com.dstz.base.web.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.AbRequestUtils;
import com.dstz.base.entity.IPersistentEntity;
import com.dstz.base.manager.AbBaseManager;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * Add, delete, modify and check controller
 *
 * @param <T>
 */
public abstract class AbCrudController<T extends IPersistentEntity> extends AbBaseController {

    @Autowired
    protected AbBaseManager<T> abBaseManager;

    /**
     * Get entity description
     *
     * @return Entity Description
     */
    protected abstract String getEntityDesc();
    
    
    /**
     * Get access filtering method
     * @return
     */
    public Set<String> getAccessQueryFilters() {
		return null;
	}

	/**
     * Paginated List
     */
    @RequestMapping(value = "listJson", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> listJson(@Valid @RequestBody QueryParamDTO queryParamDto){
    	PageListDTO<T> pageList = abBaseManager.query(new DefaultAbQueryFilter(queryParamDto,getAccessQueryFilters()));
    	return ApiResponse.success(pageList);
    }

    /**
     * Get entity record by entity ID
     *
     * @param id Entity ID
     * @return Interface Response-Entity Record
     */
    @RequestMapping(value = "get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<T> get(@RequestParam(name = "id") String id) {
        T entity = null;
        if (StringUtils.hasLength(id)) {
            entity = abBaseManager.getById(id);
        }
        return ApiResponse.success(entity);
    }

    /**
     * Saving entity data
     *
     * @param entity entity
     * @return Interface Response-Entity ID
     */
    @RequestMapping(value = "save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> save(@Valid @RequestBody T entity) {
        invokeBeforeCheck("saveCheck", entity);
        String desc;
        if (StringUtils.hasLength(entity.getId())) {
            desc = "Update %s successfully";
            Assert.isTrue(abBaseManager.update(entity) > 0, () -> new BusinessMessage(GlobalApiCodes.DATA_VERSION_OLD));
        } else {
            desc = "Add %s successfully";
            abBaseManager.create(entity);
        }
        return ApiResponse.success(entity.getId()).withMessage(String.format(desc, getEntityDesc()));
    }

    /**
     * Pre-execution check
     *
     * @param methodName Method Name
     * @param param      Method Parameters
     */
    protected void invokeBeforeCheck(String methodName, Object param) {
        Method saveCheck = ReflectUtil.getMethodByName(getClass(), methodName);
        if (saveCheck != null) {
            ReflectUtil.invoke(this, saveCheck, param);
        }
    }

    /**
     * Batch Delete Entities
     *
     * @param id Entity ID, multiple, separated
     * @return Interface response
     */
    @RequestMapping(value = "remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> remove(@RequestParam(name = "id") String id) {
    	checkIsDemoEnvironment();
        List<String> ids = StrUtil.split(id, CharUtil.COMMA);
        invokeBeforeCheck("removeCheck", ids);
        abBaseManager.removeByIds(ids);
        final String message = String.format("Delete %s successfully", getEntityDesc());
        return ApiResponse.<String>success().withMessage(message);
    }
    
    // Deletion is prohibited in the demo environment
    protected void checkIsDemoEnvironment() {
    	if("demoa5.tongzhouyun.com".equals(AbRequestUtils.getHttpServletRequest().getServerName())) {
        	 throw new BusinessException(GlobalApiCodes.REMOTE_CALL_ERROR.formatDefaultMessage("The current operation is prohibited in the demonstration environment, and the access information has been counted!"));
        }
    }
    
}
