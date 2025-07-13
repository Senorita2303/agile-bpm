package com.dstz.sys.api;

import com.dstz.sys.api.constant.RightsObjectConstants;
import com.dstz.sys.api.dto.SysAuthorizationDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysAuthorizationApi {

    Set<String> getUserRights(String userId);

    /**
     * Get authorization sql
     *
     * @param userId    User id
     * @param targetKey The default is Id_ The field name of the targetId associated with the authorization in the database
     * @return
     */
    Map<String, Object> getUserRightsSql(RightsObjectConstants rightsObject, String userId, String targetKey);


    /**
     * Get authorization collection through business
     *
     * @param rightsObject    Business type
     * @param rightsCode     Business ID
     * @return
     */
    List<SysAuthorizationDTO> getAuthorizationByRights(RightsObjectConstants rightsObject, String rightsCode);

    /**
     * Delete authorization by business type and business ID
     * @param document
     * @param id
     */
    void deleteAuthorizationByRights(RightsObjectConstants document, String id);

    /**
     * Import authorization information
     * Use the business type (rightsObject) and rightsTarget to determine whether it is a new addition or modification
     * @param authorizationDTOS
     */
    void importAuthorization(List<SysAuthorizationDTO> authorizationDTOS);
}
