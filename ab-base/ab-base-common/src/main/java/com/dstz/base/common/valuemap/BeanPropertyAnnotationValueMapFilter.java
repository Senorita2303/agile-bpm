package com.dstz.base.common.valuemap;

import com.dstz.base.common.jackson.PropertyFilterAdapter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

public class BeanPropertyAnnotationValueMapFilter extends PropertyFilterAdapter {

    @Override
    public void serializeAsField(Object pojo, JsonGenerator gen, SerializerProvider prov, PropertyWriter writer) throws Exception {
        writer.serializeAsField(pojo, gen, prov);
        AbValueMapAnnotationProcessor.getInstance().serialFieldMapped(pojo, gen, prov, writer);
    }
}
