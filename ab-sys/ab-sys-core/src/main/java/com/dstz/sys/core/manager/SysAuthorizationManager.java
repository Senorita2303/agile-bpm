package com.dstz.sys.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.api.constant.RightsObjectConstants;
import com.dstz.sys.core.entity.SysAuthorization;
import com.dstz.sys.rest.model.dto.AuthorizationDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * General resource authorization configuration General business class
 * </p>
 *
 */
public interface SysAuthorizationManager extends AbBaseManager<SysAuthorization> {
    Set<String> getUserRights(String userId);

    /**
     * Get authorization sql
     *
     * @param userId    User id
     * @param targetKey The default is Id_ The field name of the targetId associated with the authorization in the database.
     * @return
     */
    Map<String, Object> getUserRightsSql(RightsObjectConstants rightsObject, String userId, String targetKey);

    List<SysAuthorization> getByTarget(RightsObjectConstants rightsObject, String rightsTarget);

    void createAll(AuthorizationDTO saveDto);

    void deleteAuthorizationByRights(RightsObjectConstants rightsObject, String rightsTarget);
}
