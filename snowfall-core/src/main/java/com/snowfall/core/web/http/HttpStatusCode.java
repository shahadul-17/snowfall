package com.snowfall.core.web.http;

import com.snowfall.core.utilities.CollectionUtilities;
import com.snowfall.core.utilities.StringUtilities;

import java.util.Map;

public enum HttpStatusCode {

    // --- Fallback ---
    NONE(0),

    // --- 1xx Informational ---
    CONTINUE(100),
    SWITCHING_PROTOCOLS(101),
    PROCESSING(102),
    EARLY_HINTS(103),

    // --- 2xx Success ---
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NON_AUTHORITATIVE_INFORMATION(203),
    NO_CONTENT(204),
    RESET_CONTENT(205),
    PARTIAL_CONTENT(206),
    MULTI_STATUS(207),
    ALREADY_REPORTED(208),
    IM_USED(226),

    // --- 3xx Redirection ---
    AMBIGUOUS(300),
    MULTIPLE_CHOICES(AMBIGUOUS),               // alias...
    MOVED_PERMANENTLY(301),
    MOVED(MOVED_PERMANENTLY),                  // alias...
    FOUND(302),
    REDIRECT(FOUND),                           // alias...
    SEE_OTHER(303),
    REDIRECT_METHOD(SEE_OTHER),                // alias...
    NOT_MODIFIED(304),
    USE_PROXY(305),
    UNUSED(306),
    TEMPORARY_REDIRECT(307),
    REDIRECT_KEEP_VERB(TEMPORARY_REDIRECT),    // alias...
    PERMANENT_REDIRECT(308),

    // --- 4xx Client Error ---
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    PAYMENT_REQUIRED(402),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),
    PROXY_AUTHENTICATION_REQUIRED(407),
    REQUEST_TIMEOUT(408),
    CONFLICT(409),
    GONE(410),
    LENGTH_REQUIRED(411),
    PRECONDITION_FAILED(412),
    REQUEST_ENTITY_TOO_LARGE(413),
    REQUEST_URI_TOO_LONG(414),
    UNSUPPORTED_MEDIA_TYPE(415),
    REQUESTED_RANGE_NOT_SATISFIABLE(416),
    EXPECTATION_FAILED(417),
    MISDIRECTED_REQUEST(421),
    UNPROCESSABLE_ENTITY(422),
    UNPROCESSABLE_CONTENT(UNPROCESSABLE_ENTITY),       // alias...
    LOCKED(423),
    FAILED_DEPENDENCY(424),
    UPGRADE_REQUIRED(426),
    PRECONDITION_REQUIRED(428),
    TOO_MANY_REQUESTS(429),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431),
    UNAVAILABLE_FOR_LEGAL_REASONS(451),

    // --- 5xx Server Error ---
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504),
    HTTP_VERSION_NOT_SUPPORTED(505),
    VARIANT_ALSO_NEGOTIATES(506),
    INSUFFICIENT_STORAGE(507),
    LOOP_DETECTED(508),
    NOT_EXTENDED(510),
    NETWORK_AUTHENTICATION_REQUIRED(511);

    private final int value;
    private final HttpStatusCode aliasOf;

    private static final HttpStatusCode[] HTTP_STATUS_CODES = HttpStatusCode.values();
    private static final Map<String, HttpStatusCode> NAME_MAP;
    private static final Map<Integer, HttpStatusCode> VALUE_MAP;

    static {
        final var httpStatusCodes = getAll();
        NAME_MAP = CollectionUtilities.createHashMap(httpStatusCodes.length);
        VALUE_MAP = CollectionUtilities.createHashMap(httpStatusCodes.length);

        for (final var httpStatusCode : httpStatusCodes) {
            NAME_MAP.putIfAbsent(httpStatusCode.name(), httpStatusCode);

            if (httpStatusCode.isAlias()) { continue; }

            VALUE_MAP.putIfAbsent(httpStatusCode.value, httpStatusCode);
        }
    }

    HttpStatusCode(final int value) {
        this.value = value;
        this.aliasOf = null;
    }

    HttpStatusCode(final HttpStatusCode aliasOf) {
        this.value = aliasOf.value;
        this.aliasOf = aliasOf;
    }

    public boolean isCanonical() { return aliasOf == null; }

    public boolean isAlias() { return !isCanonical(); }

    public HttpStatusCode canonical() { return isCanonical() ? this : aliasOf; }

    public int value() { return value; }

    public static HttpStatusCode fromName(final String name) {
        // sanitizing the name...
        final var sanitizedName = StringUtilities.getDefaultIfNullOrWhiteSpace(
                name, StringUtilities.getEmptyString(), true).toUpperCase();

        // if the sanitized name is empty, we shall return NONE...
        if (StringUtilities.isEmpty(sanitizedName)) { return NONE; }

        final var httpStatusCode = NAME_MAP.get(sanitizedName);

        return httpStatusCode == null ? NONE : httpStatusCode;
    }

    public static HttpStatusCode fromValue(final int value) {
        final var httpStatusCode = VALUE_MAP.get(value);

        return httpStatusCode == null
                ? NONE
                : httpStatusCode;
    }

    public static HttpStatusCode[] getAll() { return HTTP_STATUS_CODES; }
}
