package com.dstz.auth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
/**
 * Custom Serialization
 *
 */
public class CustomizeOauthExceptionSerializer extends StdSerializer<MyOAuth2Exception> {
    public CustomizeOauthExceptionSerializer() {
        super(MyOAuth2Exception.class);
    }


    @Override
    public void serialize(MyOAuth2Exception value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeFieldName("isOk");
        jgen.writeBoolean(Boolean.FALSE);
        jgen.writeFieldName("code");
        jgen.writeString(value.getOAuth2ErrorCode());
        jgen.writeFieldName("message");
        jgen.writeString(value.getMessage());
        jgen.writeFieldName("msg");
        jgen.writeString(value.getMessage());
        jgen.writeEndObject();
    }
}
