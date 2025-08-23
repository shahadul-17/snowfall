package com.snowfall.core.web.http;

import com.snowfall.core.utilities.StringUtilities;

public enum HttpMethod {

    NONE,
    OPTIONS,
    HEAD,
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    TRACE;

    private static final HttpMethod[] HTTP_METHODS = HttpMethod.values();

    public static HttpMethod fromName(final String name) {
        // sanitizing the name...
        final var sanitizedName = StringUtilities.getDefaultIfNullOrWhiteSpace(
                name, StringUtilities.getEmptyString(), true).toUpperCase();

        // if the sanitized name is empty, we shall return NONE...
        if (StringUtilities.isEmpty(sanitizedName)) { return NONE; }

		for (final var httpMethod : HTTP_METHODS) {
			if (sanitizedName.equals(httpMethod.name())) { return httpMethod; }
		}

        return NONE;
    }

    public static HttpMethod[] getAll() { return HTTP_METHODS; }
}
