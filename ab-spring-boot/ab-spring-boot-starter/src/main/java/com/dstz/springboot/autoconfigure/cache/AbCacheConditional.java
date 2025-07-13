package com.dstz.springboot.autoconfigure.cache;

import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.constats.StrPool;
import com.dstz.springboot.autoconfigure.cache.enums.AbCacheTypeEnum;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.util.StringUtils;

class AbCacheConditional extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String sourceClass = "";
        if (metadata instanceof ClassMetadata) {
            sourceClass = ((ClassMetadata) metadata).getClassName();
        }
        String value = context.getEnvironment().getProperty("ab.cache.type");
        if (!StringUtils.hasLength(value)) {
            return AbMemoryCacheConfiguration.class.getName().equals(sourceClass) ? ConditionOutcome.match() : ConditionOutcome.noMatch(value + " cache type");
        }
        AbCacheTypeEnum abCacheType = AbCacheTypeEnum.valueOf(StrUtil.replace(value, StrPool.DASHED, StrPool.UNDERLINE).toUpperCase());
        return abCacheType.getConfigClass().getName().equals(sourceClass) ? ConditionOutcome.match() : ConditionOutcome.noMatch(value + " cache type");
    }
}
