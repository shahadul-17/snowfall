package com.snowfall.core.web.http;

import com.snowfall.core.utilities.CollectionUtilities;
import com.snowfall.core.utilities.StringUtilities;

import java.util.Map;

public enum HttpResponseHeader {

	NONE(StringUtilities.getEmptyString()),

	// --- Common / Shared with requests ---
	CACHE_CONTROL("Cache-Control"),
	CONNECTION("Connection"),
	CONTENT_LENGTH("Content-Length"),
	CONTENT_TYPE("Content-Type"),
	DATE("Date"),
	PRAGMA("Pragma"),
	TRAILER("Trailer"),
	TRANSFER_ENCODING("Transfer-Encoding"),
	UPGRADE("Upgrade"),
	VIA("Via"),
	WARNING("Warning"),

	// --- Authentication ---
	WWW_AUTHENTICATE("WWW-Authenticate"),
	PROXY_AUTHENTICATE("Proxy-Authenticate"),

	// --- Caching / Conditional requests ---
	ETAG("ETag"),
	EXPIRES("Expires"),
	LAST_MODIFIED("Last-Modified"),
	VARY("Vary"),

	// --- Redirection ---
	LOCATION("Location"),
	REFRESH("Refresh"),

	// --- Security / Misc ---
	SERVER("Server"),
	SET_COOKIE("Set-Cookie"),
	STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"),
	CONTENT_ENCODING("Content-Encoding"),
	CONTENT_LANGUAGE("Content-Language"),
	CONTENT_LOCATION("Content-Location"),
	CONTENT_MD5("Content-MD5"),
	CONTENT_RANGE("Content-Range"),
	ACCEPT_RANGES("Accept-Ranges");

	private final String value;

	private static final HttpResponseHeader[] HTTP_RESPONSE_HEADERS = HttpResponseHeader.values();
	private static final Map<String, HttpResponseHeader> NAME_MAP;
	private static final Map<String, HttpResponseHeader> VALUE_MAP;

	static {
		final var httpResponseHeaders = getAll();
		NAME_MAP = CollectionUtilities.createHashMap(httpResponseHeaders.length);
		VALUE_MAP = CollectionUtilities.createHashMap(httpResponseHeaders.length);

		for (final var httpRequestHeader : httpResponseHeaders) {
			NAME_MAP.putIfAbsent(httpRequestHeader.name(), httpRequestHeader);
			VALUE_MAP.putIfAbsent(httpRequestHeader.value.toUpperCase(), httpRequestHeader);
		}
	}

	HttpResponseHeader(final String value) {
		this.value = value;
	}

	public String value() { return value; }

	public static HttpResponseHeader fromName(final String name) {
		// sanitizing the name...
		final var sanitizedName = StringUtilities.getDefaultIfNullOrWhiteSpace(
				name, StringUtilities.getEmptyString(), true).toUpperCase();

		// if the sanitized name is empty, we shall return NONE...
		if (StringUtilities.isEmpty(sanitizedName)) { return NONE; }

		final var httpResponseHeader = NAME_MAP.get(sanitizedName);

		return httpResponseHeader == null ? NONE : httpResponseHeader;
	}

	public static HttpResponseHeader fromValue(final String value) {
		// sanitizing the name...
		final var sanitizedName = StringUtilities.getDefaultIfNullOrWhiteSpace(
				value, StringUtilities.getEmptyString(), true).toUpperCase();

		// if the sanitized name is empty, we shall return NONE...
		if (StringUtilities.isEmpty(sanitizedName)) { return NONE; }

		final var httpResponseHeader = VALUE_MAP.get(sanitizedName);

		return httpResponseHeader == null ? NONE : httpResponseHeader;
	}

	public static HttpResponseHeader[] getAll() { return HTTP_RESPONSE_HEADERS; }
}
