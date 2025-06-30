package com.dstz.springboot.autoconfigure.web;

import cn.hutool.core.util.ObjectUtil;
import com.dstz.base.common.jackson.OnlyFilterProvider;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.common.valuemap.BeanPropertyAnnotationValueMapFilter;
import com.dstz.base.common.valuemap.JacksonContainerSerializerDelegate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Jackson customized configuration
 *
 */
@Configuration
public class AbJackson2ObjectConfiguration {


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(JacksonProperties jacksonProperties) {
        return jacksonObjectMapperBuilder -> {
            // JDK 8 Date Conversion Processing
            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(jacksonProperties.getDateFormat(), ObjectUtil.defaultIfNull(jacksonProperties.getLocale(), Locale.getDefault()));
            jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
            jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
            
            jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
            jacksonObjectMapperBuilder.filters(new OnlyFilterProvider(new BeanPropertyAnnotationValueMapFilter()));
            jacksonObjectMapperBuilder.postConfigurer(this::postObjectMapper);
        };
    }

    private void postObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new BeanSerializerModifier() {
            @Override
            public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                return serializer.withFilterId(ObjectUtils.NULL);
            }

            @Override
            public JsonSerializer<?> modifyCollectionSerializer(SerializationConfig config, CollectionType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                return new JacksonContainerSerializerDelegate(CastUtils.cast(serializer));
            }
        }));
    }

}
