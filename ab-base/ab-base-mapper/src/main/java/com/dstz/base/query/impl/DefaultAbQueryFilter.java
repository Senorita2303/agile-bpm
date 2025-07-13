package com.dstz.base.query.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.constats.ThreadMapKeyConstant;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.ApiException;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.common.utils.ThreadMapUtil;
import com.dstz.base.query.AbPage;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.base.query.ConditionType;
import com.dstz.base.query.QuerySort;
import org.apache.ibatis.session.RowBounds;

import javax.validation.Valid;
import java.util.*;

/**
 * Default implementation of query
 *
 */
public class DefaultAbQueryFilter implements AbQueryFilter {

    /**
     * Query Parameters
     */
    private Map<String, Object> queryParam = null;

    /**
     * Paging Information
     */
    private AbPage page = null;

    /**
     * Query grouping, each group is combined with brackets
     */
    private DefaultQueryFieldGroup queryFieldGroup = null;

    /**
     * Query sorting
     */
    private List<QuerySort> querySortList = null;

    private List<String> selectColumnNames;


    @Override
    public AbPage getPage() {
        return this.page;
    }

    @Override
    public Map<String, Object> generateQuerySql() {
        // Generate whereSQL
        if (CollectionUtil.isNotEmpty(queryParam)) {
            this.queryParam.put(WHERE_SQL, this.queryFieldGroup.getWhereSql());
        }

        // Generate OrderBySql
        if (CollectionUtil.isNotEmpty(this.getQuerySortList())) {
            StringBuilder orderBySql = new StringBuilder();
            for (QuerySort fieldSort : this.getQuerySortList()) {
                orderBySql.append(fieldSort.getColumn()).append(" ").append(fieldSort.getAsc() ? "ASC" : "DESC ").append(",");
            }
            orderBySql.deleteCharAt(orderBySql.length() - 1);

            this.queryParam.put(ORDERBY_SQL, orderBySql.toString());
        }

        return queryParam;
    }

    public DefaultAbQueryFilter() {

    }

    /**
     * Constructor
     */
    public DefaultAbQueryFilter(QueryParamDTO queryParamDto) {
        initByQueryParamDto(queryParamDto, null, true);
    }

    /**
     * Constructor
     *
     * @param flag Whether to convert underline format by yourself
     */
    public DefaultAbQueryFilter(QueryParamDTO queryParamDto, boolean flag) {
        initByQueryParamDto(queryParamDto, null, flag);
    }

    /**
     * Constructor
     *
     * @param queryParamDto      DTO for query conditions
     * @param accessQueryFilters Access query conditions
     */
    public DefaultAbQueryFilter(@Valid QueryParamDTO queryParamDto, Set<String> accessQueryFilters) {
        initByQueryParamDto(queryParamDto, accessQueryFilters, true);
    }

    /**
     * Constructor
     *
     * @param queryParamDto      DTO for query conditions
     * @param accessQueryFilters Access query conditions
     * @param flag               是否自定转换下划线格式
     */
    public DefaultAbQueryFilter(@Valid QueryParamDTO queryParamDto, Set<String> accessQueryFilters, boolean flag) {
        initByQueryParamDto(queryParamDto, accessQueryFilters, flag);
    }

    private void initByQueryParamDto(QueryParamDTO queryParamDto, Set<String> accessQueryFilters, boolean flag) {
        //查询条件初始化
        queryParam = MapUtil.newHashMap(queryParamDto.getQueryParam().size() + 5, false);
        queryFieldGroup = new DefaultQueryFieldGroup();

        // 转换查询列
        this.selectColumnNames = CollUtil.map(queryParamDto.getColumnNames(), s -> {
            String to = StrUtil.removeSuffix(NamingCase.toUnderlineCase(s), StrPool.DOLLAR);
            return s.endsWith("$") ? to : to + "_";
        }, true);

        this.initQueryCondition(queryParamDto.getQueryParam(), accessQueryFilters, flag);

        // 排序条件初始化，QueryDTO 设计仅支持一个排序
        if (StrUtil.isNotEmpty(queryParamDto.getSortColumn())) {
            this.querySortList = Collections.singletonList(
                    //决定是否增加下划线
                    new QuerySort(flag ? handleDbFiledName(queryParamDto.getSortColumn()) : queryParamDto.getSortColumn(),
                            StrUtil.equalsIgnoreCase(QuerySort.ASC, queryParamDto.getSortOrder())
                    ));
        }
        //如果是否分页为false，则不分页。 分页对象不存在或者为true则分页
        if (null != queryParamDto.getEnablePage() && !queryParamDto.getEnablePage()) {
            this.page = null;
        } else {
            boolean searchCount = ObjectUtil.defaultIfNull(queryParamDto.getSearchCount(), Boolean.TRUE);
            if (queryParamDto.getOffset() != null && queryParamDto.getLimit() != null) {
                RowBounds rowBounds = new RowBounds(queryParamDto.getOffset(), queryParamDto.getLimit());
                this.page = new AbPage(rowBounds, searchCount);
            } else {
                long currentPage = ObjectUtil.defaultIfNull(queryParamDto.getCurrentPage(), 1L);
                long pageSize = ObjectUtil.defaultIfNull(queryParamDto.getPageSize(), 10L);
                this.page = new AbPage(currentPage, pageSize, searchCount);
                // 分页设置
            }
        }
    }


    /**
     * Process the request parameters coming in the page.
     *
     * <pre>
     * 	1.Parameter field naming rules.
     * 	a:Parameter name^parameter type+condition eg：a$VEQ It means that field a is of varchar type, provided that the first parameter after eq $ is of data type
     * 	b:Parameter name^parameter type  eg：b$VIt means, bThe field is of varchar type and is used for SQL parameters.
     * 	2.The logic constructed here is all and logic.
     * 3.Parameter Type:V :String varchar N:numbernumber D:Date
     * Conditional Parameters Enumeration: QueryOP
     *
     * </pre>
     *
     * @param accessQueryFilters
     * @param queryConditionGroupParam
     * @param accessQueryFilters
     * @param flag                     Whether to convert underline characters. The default is to convert underline characters.
     */
    private void initQueryCondition(Map<String, Object> queryConditionGroupParam, Set<String> accessQueryFilters, boolean flag) {
        // Set dynamic query parameters, compatible with data permission setting field query
        if (CollUtil.isNotEmpty(accessQueryFilters)) {
            Optional.ofNullable(ThreadMapUtil.get(ThreadMapKeyConstant.QueryFilter.ACCESS_QUERY_FILTERS))
                    .<Set<String>>map(CastUtils::cast)
                    .filter(CollUtil::isNotEmpty)
                    .map(accessQueryFilters::addAll);
        }
        queryConditionGroupParam.forEach((key, val) -> {
            // Whether queryFilter agrees on input parameters
            if (!StrUtil.contains(key, StrPool.DOLLAR)) {
                return;
            }

            // Skip the query condition if the value is empty (when the condition is empty or not empty, the value is allowed to be empty)
            if (ObjectUtil.isEmpty(val) && !key.endsWith(ConditionType.IS_NULL.key()) && !key.endsWith(ConditionType.NOTNULL.key())) {
                return;
            }

            // There cannot be spaces between parameters.
            if (key.indexOf(StrPool.C_SPACE) != -1) {
                throw new ApiException(GlobalApiCodes.PARAMETER_INVALID.formatMessage("参数不合法{},{}", key, val));
            }

            String[] aryParamKey = key.split("\\" + StrPool.DOLLAR);
            if (aryParamKey.length != 2) {
                return;
            }

            // The parameter contains an unallowed field.
            if (CollectionUtil.isNotEmpty(accessQueryFilters) && !accessQueryFilters.contains(aryParamKey[0])) {
                throw new ApiException(GlobalApiCodes.PARAMETER_UNALLOWED.formatDefaultMessage(aryParamKey[0]));
            }


            // Convert to underscore, database fields
            String fieldName = flag ? NamingCase.toUnderlineCase(aryParamKey[0]).concat(StrPool.UNDERLINE) : aryParamKey[0];
            String condition = aryParamKey[1];// Interception conditions
            String groupKey = "";
            if (condition.contains("_OR")) {// Group ID
                groupKey = condition.split("_")[1];
                condition = condition.substring(0, condition.indexOf("_"));
            }


            ConditionType conditionType = null;
            if (condition.length() > 1) {
                conditionType = ConditionType.formKey(condition.substring(1));
            }

            Object nativeValue = handelValue(conditionType, val, condition);

            if (ConditionType.LESS.equals(conditionType) || ConditionType.LESS_EQUAL.equals(conditionType)) {
                fieldName = fieldName + "-end";
            }
            this.queryParam.put(fieldName.replace(CharUtil.DOT, CharUtil.UNDERLINE), nativeValue);

            // Some conditions are null, just put the parameters in and use them yourself
            if (conditionType != null) {
                this.queryFieldGroup.addQueryField(groupKey, fieldName, conditionType, nativeValue);
            }
        });
    }

    /**
     * Process the Value
     *
     * @param conditionType
     * @param strValue
     * @param condition
     * @return
     */
    public Object handelValue(ConditionType conditionType, Object strValue, String condition) {

        Object nativeValue = null;

        // Direct processing of in types
        switch (conditionType) {
            case IN:
            case NOT_IN:
                return getInValueSql(strValue);

            case LEFT_LIKE:
                return CharSequenceUtil.addPrefixIfNot(Convert.toStr(strValue), "%");

            case RIGHT_LIKE:
                return CharSequenceUtil.addSuffixIfNot(Convert.toStr(strValue), "%");

            case LIKE:
                String likeValue = Convert.toStr(strValue);
                likeValue = CharSequenceUtil.addPrefixIfNot(likeValue, "%");
                return CharSequenceUtil.addSuffixIfNot(likeValue, "%");
        }

        // No need to convert data types when adding manually
        if (condition == null) return strValue;

        String columnType = condition.substring(0, 1);

        // Default other format operations
        if ("V".equals(columnType)) {
            nativeValue = Convert.toStr(strValue);
        } else if ("N".equals(columnType)) {
            nativeValue = Convert.toNumber(strValue);
        } else if ("D".equals(columnType)) {
            nativeValue = DateUtil.parse(String.valueOf(strValue));
        }

        return nativeValue;
    }

    /**
     * For the in query method, different processing is performed according to the type of the value passed in. If the value is a string, the string is separated and then merged into a string that conforms to the in specification.
     * If value is a list, it is directly merged into a string that conforms to the in specification
     *
     * @return
     */
    private String getInValueSql(Object value) {
        Iterable listValue = null;
        if (value instanceof String) {
            listValue = StrUtil.split((String) value, CharUtil.COMMA);
        } else if (value instanceof Iterable) {
            listValue = (Iterable) value;
        }

        if (CollectionUtil.isEmpty(listValue)) {
            return StrUtil.EMPTY;
        }

        StringBuilder inSqlStr = new StringBuilder();
        for (Object obj : listValue) {
            // If it is empty, it needs to be skipped. Fix the string index out of bounds problem caused by forced segmentation when the value is a comma
            if (ObjectUtil.isEmpty(obj)){
                continue;
            }
            String valueString = Objects.isNull(obj) ? null : obj.toString();
            if (StrUtil.isNotEmpty(valueString)) {
                inSqlStr.append(CharUtil.SINGLE_QUOTE)
                        .append(valueString)
                        .append(CharUtil.SINGLE_QUOTE)
                        .append(CharUtil.COMMA);
            }
        }
        if (inSqlStr.charAt(inSqlStr.length() - 1) == CharUtil.COMMA) {
            inSqlStr.deleteCharAt(inSqlStr.length() - 1);
        }
        return inSqlStr.toString();
    }

    public Map<String, Object> getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(Map<String, Object> queryParam) {
        this.queryParam = queryParam;
    }

    public DefaultQueryFieldGroup getQueryFieldGroup() {
        return queryFieldGroup;
    }

    public void setQueryFieldGroup(DefaultQueryFieldGroup queryFieldGroup) {
        this.queryFieldGroup = queryFieldGroup;
    }

    public List<QuerySort> getQuerySortList() {
        return querySortList;
    }

    public void setQuerySortList(List<QuerySort> querySortList) {
        this.querySortList = querySortList;
    }

    public void setPage(AbPage page) {
        this.page = page;
    }

    public static DefaultAbQueryFilter build() {
        DefaultAbQueryFilter filter = buildWithOutPage();
        filter.setPage(new AbPage());
        return filter;
    }

    public static DefaultAbQueryFilter buildWithOutPage() {
        DefaultAbQueryFilter filter = new DefaultAbQueryFilter();
        filter.setQueryParam(MapUtil.newHashMap(10));
        filter.setQueryFieldGroup(new DefaultQueryFieldGroup());
        return filter;
    }

    public static AbQueryFilter buildWithFilter(String fildName, Object value, ConditionType conditionType) {
        return build().addFilter(fildName, value, conditionType);
    }

    public static AbQueryFilter buildWithEqFilter(String fildName, Object value) {
        return build().addFilter(fildName, value, ConditionType.EQUAL);
    }

    @Override
    public AbQueryFilter noPage() {
        this.page = null;
        return this;
    }


    @Override
    public AbQueryFilter addFilter(String fieldName, Object value, ConditionType conditionType) {
        return this.addFilter(fieldName, value, conditionType, Boolean.TRUE, null);
    }

    private AbQueryFilter addFilter(String fieldName, Object value, ConditionType conditionType, Boolean underline, String groupKey) {
        if (StrUtil.isEmpty(fieldName) || value == null) {
            return this;
        }
        // Convert to underscore, database fields
        if (underline) {
            fieldName = handleDbFiledName(fieldName);
        }

        Object nativeValue = handelValue(conditionType, value, null);

        // TODO Change queryFiled to fieldName and paramName for storage
        this.queryParam.put(fieldName.replace(CharUtil.DOT, CharUtil.UNDERLINE), nativeValue);
        this.getQueryFieldGroup().addQueryField(groupKey, fieldName, conditionType, nativeValue);

        return this;
    }

    @Override
    public AbQueryFilter addFilterOriginal(String fieldName, Object value, ConditionType conditionType) {
        return this.addFilter(fieldName, value, conditionType, Boolean.FALSE, null);
    }

    @Override
    public AbQueryFilter addFilter(String fieldName, Object value, ConditionType conditionType, String groupKey) {
        return this.addFilter(fieldName, value, conditionType, Boolean.FALSE, groupKey);
    }

    // Convert to underscore, database fields
    private String handleDbFiledName(String filedName) {
        String dbName = NamingCase.toUnderlineCase(filedName);
        // Mandatory ending with _
        if (!dbName.endsWith(StrPool.UNDERLINE)) {
            dbName = dbName.concat(StrPool.UNDERLINE);
        }
        return dbName;
    }

    @Override
    public AbQueryFilter likeFilter(String fildName, Object value) {
        return this.addFilter(fildName, value, ConditionType.LIKE);

    }

    @Override
    public AbQueryFilter inFilter(String fildName, Object value) {
        return this.addFilter(fildName, value, ConditionType.IN);
    }

    @Override
    public AbQueryFilter notEqFilter(String fildName, Object value) {
        return this.addFilter(fildName, value, ConditionType.NOT_EQUAL);
    }

    @Override
    public AbQueryFilter addParam(String fildName, Object value) {
        this.getQueryParam().put(fildName, value);
        return this;
    }

    @Override
    public AbQueryFilter clearQuery() {
        this.getQueryParam().clear();
        this.getQuerySortList().clear();
        this.getQueryFieldGroup().getFieldGroupMap().clear();
        return this;
    }
    @Override
    public AbQueryFilter eqFilter(String fildName, Object value) {
        return this.addFilter(fildName, value, ConditionType.EQUAL);
    }

    @Override
    public List<String> getSelectColumnNames() {
        return selectColumnNames;
    }
}
