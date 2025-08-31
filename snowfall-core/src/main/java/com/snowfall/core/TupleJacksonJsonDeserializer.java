package com.snowfall.core;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;

class TupleJacksonJsonDeserializer
		extends com.fasterxml.jackson.databind.JsonDeserializer<Tuple> {

	private static final TypeReference<Object[]> ARRAY_TYPE_REFERENCE = new TypeReference<>() { };

	@Override
	public Tuple deserialize(final JsonParser jsonParser,
									 final DeserializationContext deserializationContext) throws IOException, JacksonException {
		final var codec = jsonParser.getCodec();
		final var elements = codec.readValue(jsonParser, ARRAY_TYPE_REFERENCE);

		// NOTE: A TUPLE MUST HAVE AT-LEAST TWO ELEMENTS...!!!
		return elements.length < 2 ? Tuple.empty() : Tuple.of(elements);
	}
}
