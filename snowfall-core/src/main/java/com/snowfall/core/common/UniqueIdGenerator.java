package com.snowfall.core.common;

public interface UniqueIdGenerator {

	String generate();

	default UniqueIdGenerator asUniqueIdGenerator() { return this; }
}
