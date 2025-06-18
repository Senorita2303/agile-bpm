package com.dstz.cms.core.helper;

import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.base.entity.IPersistentEntity;
import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.cms.core.entity.dto.CmsJsonDTO;
import com.dstz.sys.api.SysFileApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

import static com.dstz.cms.api.constant.CmsConstant.COMMENTS_NUM;
import static com.dstz.sys.api.constant.SysApiCodes.REFLEX_WARNING;

/**
 * CMS system tools
 *
 */
@Service
public class CmsHelper {

    @Autowired
    private AbBaseMapper<?> cmsNotifyMapper, cmsNewsMapper;
    @Autowired
    private SysFileApi sysFileApi;

    /**
     * Update the number of comments for a news or announcement
     *
     * @param commentType Business type (announcement or news)
     * @param id          Business ID (announcement ID or news ID)
     * @param flag        Increase quantity true Decrease quantity false
     * @return int   Number of changes
     */
    public int updateCommentsNum(int commentType, String id, boolean flag) {
        try {
            // Determine the mapper type based on the passed parameters
            AbBaseMapper<IPersistentEntity> mapper = (AbBaseMapper<IPersistentEntity>) getInformation(commentType);
            // Get the entity of news or announcement
            IPersistentEntity iPersistentEntity = mapper.selectById(id);
            if (iPersistentEntity == null) {
                return 0;
            }
            int commentsNum;
            commentsNum = Integer.parseInt(Objects.requireNonNull(getGetMethod(iPersistentEntity, COMMENTS_NUM)).toString());
            setValue(iPersistentEntity, iPersistentEntity.getClass(), COMMENTS_NUM, Integer.class,
                    flag ? commentsNum + 1 : commentsNum > 0 ? commentsNum - 1 : 0);
            return mapper.updateById(iPersistentEntity);
        } catch (Exception e) {
            throw new BusinessException(REFLEX_WARNING);
        }
    }

    /**
     * Determine which implementation should be called based on the comment type
     *
     * @param commentType Comment type (0 announcement, 1 news)
     * @return Operation parent class
     */
    private AbBaseMapper<?> getInformation(int commentType) {
        return commentType == 0 ? cmsNotifyMapper : cmsNewsMapper;
    }

    /**
     * Delete files via json
     *
     * @param json The associated file Json string
     */
    public void deleteFile(String json) {
        if (StrUtil.isNotBlank(json)) {
            sysFileApi.delete(Objects.requireNonNull(JsonUtils.parseArray(json, CmsJsonDTO.class))
                    .stream().map(CmsJsonDTO::getId).toArray(String[]::new));
        }
    }

    /**
     * Get the get method based on the attribute
     *
     * @param ob   Object
     * @param name Property name
     */
    public static Object getGetMethod(Object ob, String name) throws Exception {
        Method[] m = ob.getClass().getMethods();
        for (int i = m.length - 1; i >= 0; i--) {
            if (("get" + name).equalsIgnoreCase(m[i].getName())) {
                return m[i].invoke(ob);
            }
        }
        return null;
    }

    /**
     * According to the attribute, get the set method and set the value to the object
     *
     * @param obj       Object
     * @param clazz     The class of the object
     * @param filedName Need to set the value of the property
     */
    public static void setValue(Object obj, Class<?> clazz, String filedName, Class<?> typeClass, Object value) {
        filedName = removeLine(filedName);
        String methodName = "set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
        try {
            Method method = clazz.getDeclaredMethod(methodName, typeClass);
            method.invoke(obj, getClassTypeValue(typeClass, value));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get the value of the corresponding type by class type
     *
     * @param typeClass Class Type
     * @param value     value
     * @return Object
     */
    private static Object getClassTypeValue(Class<?> typeClass, Object value) {
        if (typeClass == int.class || value instanceof Integer) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == short.class) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == byte.class) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == double.class) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == long.class) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == String.class) {
            if (null == value) {
                return "";
            }
            return value;
        } else if (typeClass == boolean.class) {
            if (null == value) {
                return true;
            }
            return value;
        } else if (typeClass == BigDecimal.class) {
            if (null == value) {
                return new BigDecimal(0);
            }
            return new BigDecimal(value + "");
        } else {
            return typeClass.cast(value);
        }
    }


    /**
     * Processing strings such as:  abc_dex ---> abcDex
     */
    public static String removeLine(String str) {
        if (null != str && str.contains("_")) {
            int i = str.indexOf("_");
            char ch = str.charAt(i + 1);
            char newCh = (ch + "").substring(0, 1).toUpperCase().toCharArray()[0];
            String newStr = str.replace(str.charAt(i + 1), newCh);
            return newStr.replace("_", "");
        }
        return str;
    }


    /**
     * Get the file size string
     *
     * @param i number
     * @return The converted string
     */
    public static String getSize(long i) {
        String result = "";
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        DecimalFormat df = new DecimalFormat("#.00");
        if (i >= gb) {
            result = df.format((float) i / gb) + "GB";
        } else if (i >= mb) {
            result = df.format((float) i / mb) + "MB";
        } else if (i >= kb) {
            result = String.format("%.2f", (float) i / kb) + "KB";
        } else {
            result = i + "B";
        }
        return result;
    }

}