package com.dstz.component.mq.engine.config;

import cn.hutool.extra.mail.MailAccount;
import com.dstz.component.mq.engine.constants.MqEngineConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * Cache related configuration
 *
 */
@Configuration
@Import(AbMessageQueueAutoConfiguration.AbCacheConfigurationSelector.class)
@EnableConfigurationProperties({ AbSimpleMessageQueueProperties.class})
public class AbMessageQueueAutoConfiguration {


    public static class AbCacheConfigurationSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            AbMessageQueueType[] types = AbMessageQueueType.values();
            String[] imports = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                imports[i] = types[i].getConfigurationClass().getName();
            }
            return imports;
        }
    }

}
