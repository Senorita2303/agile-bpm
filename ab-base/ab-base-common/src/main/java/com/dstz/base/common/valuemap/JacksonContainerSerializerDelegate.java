package com.dstz.base.common.valuemap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Jackson serialization proxy
 *
 */
public class JacksonContainerSerializerDelegate extends JsonSerializer<Object> {

    private final JsonSerializer<Object> delegateSerializer;

    public JacksonContainerSerializerDelegate(JsonSerializer<Object> delegateSerializer) {
        this.delegateSerializer = delegateSerializer;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        try {
            AbValueMapAnnotationProcessor.getInstance().prepareContainerMapValue(serializerProvider, value);
            delegateSerializer.serialize(value, gen, serializerProvider);
        } finally {
            AbValueMapAnnotationProcessor.getInstance().cleanContainerMapValue(serializerProvider);
        }
    }
}
