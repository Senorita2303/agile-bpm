package com.dstz.sys.opreatelog;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import com.dstz.base.common.requestlog.AbRequestLog;
import com.dstz.base.common.utils.AuditLogVariableUtil;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.rest.model.dto.TestExpressionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Name OperateLogCachedExpressionEvaluator
 */
public class AuditLogCachedExpressionEvaluator {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogCachedExpressionEvaluator.class);

    private final Cache<String, Expression> expressionCache;

    private final ExpressionParser expressionParser;

    private final BeanFactoryResolver beanFactoryResolver;

    private final BeanExpressionContextAccessor beanExpressionContextAccessor;

    private final Method isEmptyMethod;

    private final Method dateFormat;

    private final Method strContains;

    public AuditLogCachedExpressionEvaluator(BeanFactory beanFactory) {
        this(new SpelExpressionParser(), beanFactory);
    }

    public AuditLogCachedExpressionEvaluator(ExpressionParser expressionParser, BeanFactory beanFactory) {
        this.expressionParser = expressionParser;
        this.beanFactoryResolver = new BeanFactoryResolver(beanFactory);
        this.beanExpressionContextAccessor = new BeanExpressionContextAccessor();
        this.expressionCache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofMinutes(10)).build();
        try {
            this.isEmptyMethod = ObjectUtils.class.getMethod("isEmpty", Object.class);
            this.dateFormat = DateFormatUtils.class.getMethod("format", Date.class, String.class);
            this.strContains = StringUtils.class.getDeclaredMethod("contains", CharSequence.class, CharSequence.class);
        } catch (java.lang.NoSuchMethodException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    /**
     * Get expression
     *
     * @param expressionString expression string
     * @param writeCache       Write cache
     * @return The parsed expression
     */
    public Expression getExpression(String expressionString, boolean writeCache) {
        if (writeCache) {
            try {
                return expressionCache.get(expressionString, () -> expressionParser.parseExpression(expressionString, ParserContext.TEMPLATE_EXPRESSION));
            } catch (ExecutionException e) {
                LOG.warn(e.getMessage(), e);
            }
        }
        return expressionParser.parseExpression(expressionString, ParserContext.TEMPLATE_EXPRESSION);
    }

    /**
     * Create an execution expression environment
     *
     * @param abRequestLog abRequestLog
     * @return Execute expression environment
     */
    public EvaluationContext createEvaluationContext(AbRequestLog abRequestLog, IUser currentUser) {
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setBeanResolver(beanFactoryResolver);
        standardEvaluationContext.addPropertyAccessor(beanExpressionContextAccessor);
        standardEvaluationContext.registerFunction("isEmpty", this.isEmptyMethod);
        standardEvaluationContext.registerFunction("dateFormat", this.dateFormat);
        standardEvaluationContext.registerFunction("strContains", this.strContains);


        standardEvaluationContext.setVariable("currentUser", currentUser);
        standardEvaluationContext.setVariable("requestParam", abRequestLog.getRequestParameterMap());
        standardEvaluationContext.setVariable("requestBody", abRequestLog.getAttribute(AbRequestLog.REQUEST_BODY_RAW));
        standardEvaluationContext.setVariable("responseBody", abRequestLog.getResponseBody());
        standardEvaluationContext.setVariable("requestMethod", abRequestLog.getPathPattern());
        standardEvaluationContext.setVariable("requestHeader", abRequestLog.getRequestHeaderMap());
        standardEvaluationContext.setVariable("requestTime", abRequestLog.getRequestTime());
        standardEvaluationContext.setVariable("responseTime", abRequestLog.getRequestTime());
        standardEvaluationContext.setVariable("durationMs", abRequestLog.getDurationMs());
        
        // Get audit log record variables
        Map<String, Object> variableMap = AuditLogVariableUtil.getVariableMap(abRequestLog);
        if(MapUtil.isNotEmpty(variableMap)){
            variableMap.forEach(standardEvaluationContext::setVariable);
        }
        
        return standardEvaluationContext;
    }


    /**
     * Create an execution expression environment
     *
     * @param testExpressionDTO testExpressionDTO
     * @return Execute expression environment
     */
    public EvaluationContext createTestEvaluationContext(TestExpressionDTO testExpressionDTO) {

        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setBeanResolver(beanFactoryResolver);
        standardEvaluationContext.addPropertyAccessor(beanExpressionContextAccessor);
        standardEvaluationContext.registerFunction("isEmpty", this.isEmptyMethod);
        standardEvaluationContext.registerFunction("dateFormat", this.dateFormat);
        standardEvaluationContext.registerFunction("strContains", this.strContains);

        IUser user = UserContextUtils.getUser().get();

        standardEvaluationContext.setVariable("currentUser", user);
        standardEvaluationContext.setVariable("requestParam", JsonUtils.parseObject(testExpressionDTO.getRequestParam(), new TypeReference<Map<String, String[]>>() {
        }));
        standardEvaluationContext.setVariable("requestBody", JsonUtils.toMap(testExpressionDTO.getRequestBody()));
        standardEvaluationContext.setVariable("responseBody", JsonUtils.toMap(testExpressionDTO.getResponseBody()));
        standardEvaluationContext.setVariable("requestMethod", "POST");
        standardEvaluationContext.setVariable("requestHeader", null);
        standardEvaluationContext.setVariable("requestTime", new Date());
        standardEvaluationContext.setVariable("responseTime", new Date());
        standardEvaluationContext.setVariable("durationMs", 0L);
        return standardEvaluationContext;
    }
}
