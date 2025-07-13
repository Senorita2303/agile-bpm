package com.dstz.springboot.autoconfigure.idgen;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.dstz.base.common.idgen.IdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ID Generator Auto-Configuration
 *
 */
@EnableConfigurationProperties(IdGeneratorProperties.class)
@Configuration
public class IdGeneratorAutoConfiguration {

    private final IdGeneratorProperties idGeneratorProperties;

    public IdGeneratorAutoConfiguration(IdGeneratorProperties idGeneratorProperties) {
        this.idGeneratorProperties = idGeneratorProperties;
    }

    /**
     * Snowflake Algorithm Generator
     *
     * @return Snowflake Algorithm Generator
     */
    @ConditionalOnProperty(prefix = "ab.id-generator", name = "type", havingValue = "snowflake", matchIfMissing = true)
    @Bean
    public IdGenerator snowflakeGenerator() {
        IdGeneratorProperties.Snowflake snowflakeProperties = idGeneratorProperties.getSnowflake();
        if (snowflakeProperties.getDataCenterId() == null) {
            snowflakeProperties.setDataCenterId(IdUtil.getDataCenterId(31));
        }
        if (snowflakeProperties.getWorkerId() == null) {
            snowflakeProperties.setWorkerId(IdUtil.getWorkerId(snowflakeProperties.getDataCenterId(), 31));
        }
        Snowflake snowflake = new Snowflake(snowflakeProperties.getEpochDate(), snowflakeProperties.getWorkerId(), snowflakeProperties.getDataCenterId(), snowflakeProperties.getUseSystemClock(), snowflakeProperties.getTimeOffset());
        return snowflake::nextIdStr;
    }


}
