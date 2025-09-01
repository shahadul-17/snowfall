package com.snowfall.core.utilities;

public final class ExceptionUtilities {

	public static RuntimeException toRuntimeException(final Throwable throwable) {
		return toRuntimeException(null, throwable);
	}

	public static RuntimeException toRuntimeException(final String message, final Throwable throwable) {
		if (throwable instanceof final RuntimeException exception) { return exception; }

		return new RuntimeException(StringUtilities.getDefaultIfNullOrWhiteSpace(
				message, throwable.getMessage(), true), throwable);
	}
}
