package com.snowfall.core;

public interface Action<Type> {
	void invoke(final Type object) throws RuntimeException;
}
