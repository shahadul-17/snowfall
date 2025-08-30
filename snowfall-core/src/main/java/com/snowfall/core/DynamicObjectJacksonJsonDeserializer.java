package com.snowfall.core;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;
import java.util.Map;

class DynamicObjectJacksonJsonDeserializer
		extends com.fasterxml.jackson.databind.JsonDeserializer<DynamicObject> {

	private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() { };

	@Override
	public DynamicObject deserialize(final JsonParser jsonParser,
									 final DeserializationContext deserializationContext) throws IOException, JacksonException {
		final var codec = jsonParser.getCodec();
		final var mapContent = codec.readValue(jsonParser, MAP_TYPE_REFERENCE);

		return DynamicObject.fromMap(mapContent);
	}
}
