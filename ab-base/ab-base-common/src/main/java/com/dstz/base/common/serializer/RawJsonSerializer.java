package com.dstz.base.common.serializer;

import java.io.IOException;

import com.dstz.base.common.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Used to serialize string object fields into JSON fields
 * 
 *
 */
public class RawJsonSerializer extends JsonSerializer<String> {

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeObject(JsonUtils.toJSONNode(value));
	}
}