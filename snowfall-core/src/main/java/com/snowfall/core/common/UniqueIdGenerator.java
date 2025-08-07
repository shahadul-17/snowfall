package com.snowfall.core.common;

public interface UniqueIdGenerator {

	String generate(final boolean dashed);

	default String generate() { return generate(true); }

	default UniqueIdGenerator asUniqueIdGenerator() { return this; }
}
