package com.dstz.component.upload.api;

import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.property.PropertyEnum;

import java.util.Map;
import java.util.Map.Entry;

/**
 * <pre>
 * Description: Uploader factory
 * </pre>
 *
 */
public class UploaderFactory {
    private UploaderFactory() {

    }

    /**
     * <pre>
     * Get Uploader
     * </pre>
     *
     * @param type
     * @return
     */
    public static IUploader getUploader(String type) {
        Map<String, IUploader> map = SpringUtil.getBeansOfType(IUploader.class);
        for (Entry<String, IUploader> entry : map.entrySet()) {
            if (entry.getValue().type().equals(type)) {
                return entry.getValue();
            }
        }
        throw new BusinessException(GlobalApiCodes.INTERNAL_ERROR.formatMessage("Unable to find implementation class for uploader of type [{}]", type));
    }

    /**
     * <pre>
     * Returns the default uploader
     * </pre>
     *
     * @return
     */
    public static IUploader getDefault() {
        return getUploader(PropertyEnum.UPLOADER_DEFAULT.getYamlValue(String.class));
    }
}
