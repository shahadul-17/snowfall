package com.snowfall.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.snowfall.core.text.JsonSerializable;
import com.snowfall.core.text.JsonSerializer;
import com.snowfall.core.utilities.ObjectUtilities;
import com.snowfall.core.utilities.StringUtilities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Collections;

@JsonSerialize(using = DynamicObjectJacksonJsonSerializer.class)
@JsonDeserialize(using = DynamicObjectJacksonJsonDeserializer.class)
public interface DynamicObject extends JsonSerializable {

	float getLoadFactor();
	boolean isEmptyDynamicObject();
	boolean isImmutable();
	DynamicObject immutable();

	/**
	 * Returns the underlying mutable map.
	 * <p><b>Warning: USE WITH EXTREME CAUTION...!!!</b>
	 * <br />
	 * This is a backdoor for advanced use cases where performance matters.
	 * Mutations done directly on this map bypass {@link DynamicObject}'s
	 * safety checks (e.g., null key prevention). Even if the dynamic object
	 * is immutable, the underlying map can still be modified, and thus, the
	 * dynamic object will also get modified.
	 * <br />
	 * <br />
	 * If you need a live view of this dynamic object, use {@link #asLiveViewMap()} or,
	 * <br />
	 * If you need a snapshot view of this dynamic object, use {@link #asSnapshotViewMap()}.
	 */
	Map<String, Object> asMap();

	default int size() { return asMap().size(); }

	default boolean isEmpty() { return asMap().isEmpty(); }

	default Map<String, Object> get() { return copy().asMap(); }

	default <Type> Type get(final String key) { return get(key, null); }

	@SuppressWarnings(value = "unchecked")
	default <Type> Type get(final String key, final Type defaultValue) {
		final var value = get(key, defaultValue, Object.class);

		return (Type) value;
	}

	default <Type> Type get(final String key, final Type defaultValue, final Class<Type> classOfType) {
		if (isEmptyDynamicObject() || StringUtilities.isNull(key) || classOfType == null) { return defaultValue; }

		// retrieving the value associated with the key...
		final var value = ObjectUtilities.cast(asMap().get(key), classOfType);

		// if the value is null, we shall return the default value...
		return value == null ? defaultValue : value;
	}

	default Boolean getBoolean(final String key) { return getBoolean(key, null); }

	default Boolean getBoolean(final String key, final Boolean defaultValue) { return get(key, defaultValue, Boolean.class); }

	default Byte getByte(final String key) { return getByte(key, null); }

	default Byte getByte(final String key, final Byte defaultValue) { return get(key, defaultValue, Byte.class); }

	default Short getShort(final String key) { return getShort(key, null); }

	default Short getShort(final String key, final Short defaultValue) { return get(key, defaultValue, Short.class); }

	default Integer getInteger(final String key) { return getInteger(key, null); }

	default Integer getInteger(final String key, final Integer defaultValue) { return get(key, defaultValue, Integer.class); }

	default Long getLong(final String key) { return getLong(key, null); }

	default Long getLong(final String key, final Long defaultValue) { return get(key, defaultValue, Long.class); }

	default BigInteger getBigInteger(final String key) { return getBigInteger(key, null); }

	default BigInteger getBigInteger(final String key, final BigInteger defaultValue) { return get(key, defaultValue, BigInteger.class); }

	default Float getFloat(final String key) { return getFloat(key, null); }

	default Float getFloat(final String key, final Float defaultValue) { return get(key, defaultValue, Float.class); }

	default Double getDouble(final String key) { return getDouble(key, null); }

	default Double getDouble(final String key, final Double defaultValue) { return get(key, defaultValue, Double.class); }

	default BigDecimal getBigDecimal(final String key) { return getBigDecimal(key, null); }

	default BigDecimal getBigDecimal(final String key, final BigDecimal defaultValue) { return get(key, defaultValue, BigDecimal.class); }

	default Number getNumber(final String key) { return getNumber(key, null); }

	default Number getNumber(final String key, final Number defaultValue) { return get(key, defaultValue, Number.class); }

	default Character getCharacter(final String key) { return getCharacter(key, null); }

	default Character getCharacter(final String key, final Character defaultValue) { return get(key, defaultValue, Character.class); }

	default String getString(final String key) { return getString(key, null); }

	default String getString(final String key, final String defaultValue) { return get(key, defaultValue, String.class); }

	default <Type> DynamicObject set(final String key, final Type value) {
		if (isEmptyDynamicObject() || isImmutable() || StringUtilities.isNull(key)) { return this; }

		asMap().put(key, value);

		return this;
	}

	default DynamicObject set(final Map<String, Object> map) {
		if (map == null || map.isEmpty() || isEmptyDynamicObject() || isImmutable()) { return this; }

		final var entrySet = map.entrySet();

		for (final var entry : entrySet) {
			set(entry.getKey(), entry.getValue());
		}

		return this;
	}

	default DynamicObject set(final DynamicObject dynamicObject) {
		if (dynamicObject == null) { return this; }

		return set(dynamicObject.asMap());
	}

	default boolean containsKey(final String key) {
		if (isEmptyDynamicObject() || isEmpty() || StringUtilities.isNull(key)) { return false; }

		return asMap().containsKey(key);
	}

	default String[] keys() {
		final var emptyStringArray = StringUtilities.getEmptyStringArray();

		if (isEmptyDynamicObject() || isEmpty()) { return emptyStringArray; }

		return asMap().keySet().toArray(emptyStringArray);
	}

	default DynamicObject remove(final String key) {
		if (isEmptyDynamicObject() || isImmutable() || StringUtilities.isNull(key)) { return this; }

		asMap().remove(key);

		return this;
	}

	default DynamicObject clear() {
		if (isEmptyDynamicObject() || isImmutable()) { return this; }

		asMap().clear();

		return this;
	}

	default DynamicObject copy(final boolean deepCopy) {
		if (isEmptyDynamicObject()) { return empty(); }
		if (deepCopy) {
			final var json = toJson();

			return DynamicObject.fromJson(json);
		}

		final var contentMap = asMap();

		return DynamicObject
				.create(contentMap.size(), getLoadFactor())
				.set(contentMap);
	}

	default DynamicObject copy() { return copy(true); }

	default Map<String, Object> asLiveViewMap() { return Collections.unmodifiableMap(asMap()); }

	default Map<String, Object> asSnapshotViewMap() { return Map.copyOf(asMap()); }

	default byte[] getBytes() { return getBytes(StandardCharsets.UTF_8); }

	default byte[] getBytes(final Charset charset) { return toJson().getBytes(charset); }

	default byte[] toByteArray() { return getBytes(); }

	default byte[] toByteArray(final Charset charset) { return getBytes(charset); }

	static DynamicObject create() { return DynamicObjectImpl.create(); }

	static DynamicObject create(final int expectedElementCount) {
		return DynamicObjectImpl.create(expectedElementCount);
	}

	static DynamicObject create(final int expectedElementCount, final float loadFactor) {
		return DynamicObjectImpl.create(expectedElementCount, loadFactor);
	}

	static DynamicObject empty() { return create(-1); }

	static DynamicObject fromMap(final Map<String, Object> map) {
		if (map == null || map.isEmpty()) { return create(); }

		return create(map.size()).set(map);
	}

	static DynamicObject fromJson(final String json) {
		return JsonSerializer.deserialize(json, DynamicObject.class);
	}
}
