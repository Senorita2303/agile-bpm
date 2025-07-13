package com.dstz.cms.core.entity.dto;

import com.dstz.base.api.dto.QueryParamDTO;

/**
 * Query condition input
 *
 */
public class CmsQueryParamDTO extends QueryParamDTO {
	
    /**
     * Query the page type of internal messages (0 notifications, 1 to-do)
     */
    private int type;


    public CmsQueryParamDTO(int type) {
        this.type = type;
    }

    public CmsQueryParamDTO() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
