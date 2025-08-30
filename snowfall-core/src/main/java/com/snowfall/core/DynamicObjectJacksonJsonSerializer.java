package com.snowfall.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

class DynamicObjectJacksonJsonSerializer
		extends com.fasterxml.jackson.databind.JsonSerializer<DynamicObject> {

	@Override
	public void serialize(final DynamicObject dynamicObject,
						  final JsonGenerator jsonGenerator,
						  final SerializerProvider serializerProvider) throws IOException {
		serializerProvider.defaultSerializeValue(dynamicObject.asMap(), jsonGenerator);
	}
}
