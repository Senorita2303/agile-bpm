package com.dstz.component.mq.engine.config;

import com.dstz.base.common.constats.StrPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.ClassMetadata;
/**
 * Load message type according to configuration
 *
 */
class AbMessageQueueConditional extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String sourceClass = "";
        if (metadata instanceof ClassMetadata) {
            sourceClass = ((ClassMetadata) metadata).getClassName();
        }
        String value = context.getEnvironment().getProperty("ab.simple-mq.message-queue-type");
        if (StringUtils.isEmpty(value)) {
            return sourceClass.equals(AbMessageQueueType.SYNCHRONOUS.getConfigurationClass().getName()) ? ConditionOutcome.match() : ConditionOutcome.noMatch(value + " cache type");
        }
        value = value.replace(StrPool.DASHED, StrPool.UNDERLINE);
        AbMessageQueueType abMessageQueueType = AbMessageQueueType.valueOf(value.toUpperCase());
        return sourceClass.equals(abMessageQueueType.getConfigurationClass().getName()) ? ConditionOutcome.match() : ConditionOutcome.noMatch(value + " cache type");
    }
}
