package com.snowfall.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

class TupleJacksonJsonSerializer
		extends com.fasterxml.jackson.databind.JsonSerializer<Tuple> {

	@Override
	public void serialize(final Tuple tuple,
						  final JsonGenerator jsonGenerator,
						  final SerializerProvider serializerProvider) throws IOException {
		serializerProvider.defaultSerializeValue(tuple.getElements(), jsonGenerator);
	}
}
