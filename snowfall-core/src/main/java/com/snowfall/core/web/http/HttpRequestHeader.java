package com.snowfall.core.web.http;

import com.snowfall.core.utilities.CollectionUtilities;
import com.snowfall.core.utilities.StringUtilities;

import java.util.Map;

public enum HttpRequestHeader {

	NONE(StringUtilities.getEmptyString()),

	// --- Standard / Common ---
	ACCEPT("Accept"),
	ACCEPT_CHARSET("Accept-Charset"),
	ACCEPT_ENCODING("Accept-Encoding"),
	ACCEPT_LANGUAGE("Accept-Language"),
	AUTHORIZATION("Authorization"),
	CACHE_CONTROL("Cache-Control"),
	CONNECTION("Connection"),
	COOKIE("Cookie"),
	CONTENT_LENGTH("Content-Length"),
	CONTENT_TYPE("Content-Type"),
	EXPECT("Expect"),
	FROM("From"),
	HOST("Host"),
	IF_MATCH("If-Match"),
	IF_MODIFIED_SINCE("If-Modified-Since"),
	IF_NONE_MATCH("If-None-Match"),
	IF_RANGE("If-Range"),
	IF_UNMODIFIED_SINCE("If-Unmodified-Since"),
	MAX_FORWARDS("Max-Forwards"),
	ORIGIN("Origin"),
	PRAGMA("Pragma"),
	PROXY_AUTHORIZATION("Proxy-Authorization"),
	RANGE("Range"),
	REFERER("Referer"),
	TE("TE"),
	USER_AGENT("User-Agent"),
	UPGRADE("Upgrade"),
	VIA("Via");

	private final String value;

	private static final HttpRequestHeader[] HTTP_REQUEST_HEADERS = HttpRequestHeader.values();
	private static final Map<String, HttpRequestHeader> NAME_MAP;
	private static final Map<String, HttpRequestHeader> VALUE_MAP;

	static {
		final var httpRequestHeaders = getAll();
		NAME_MAP = CollectionUtilities.createHashMap(httpRequestHeaders.length);
		VALUE_MAP = CollectionUtilities.createHashMap(httpRequestHeaders.length);

		for (final var httpRequestHeader : httpRequestHeaders) {
			NAME_MAP.putIfAbsent(httpRequestHeader.name(), httpRequestHeader);
			VALUE_MAP.putIfAbsent(httpRequestHeader.value.toUpperCase(), httpRequestHeader);
		}
	}

	HttpRequestHeader(final String value) {
		this.value = value;
	}

	public String value() { return value; }

	@Override
	public String toString() { return value(); }

	public static HttpRequestHeader fromName(final String name) {
		// sanitizing the name...
		final var sanitizedName = StringUtilities.getDefaultIfNullOrWhiteSpace(
				name, StringUtilities.getEmptyString(), true).toUpperCase();

		// if the sanitized name is empty, we shall return NONE...
		if (StringUtilities.isEmpty(sanitizedName)) { return NONE; }

		final var httpRequestHeader = NAME_MAP.get(sanitizedName);

		return httpRequestHeader == null ? NONE : httpRequestHeader;
	}

	public static HttpRequestHeader fromValue(final String value) {
		// sanitizing the name...
		final var sanitizedName = StringUtilities.getDefaultIfNullOrWhiteSpace(
				value, StringUtilities.getEmptyString(), true).toUpperCase();

		// if the sanitized name is empty, we shall return NONE...
		if (StringUtilities.isEmpty(sanitizedName)) { return NONE; }

		final var httpRequestHeader = VALUE_MAP.get(sanitizedName);

		return httpRequestHeader == null ? NONE : httpRequestHeader;
	}

	public static HttpRequestHeader[] getAll() { return HTTP_REQUEST_HEADERS; }
}
