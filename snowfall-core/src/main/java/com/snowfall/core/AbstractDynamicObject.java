package com.snowfall.core;

import com.snowfall.core.utilities.CollectionUtilities;
import com.snowfall.core.utilities.NumberUtilities;
import com.snowfall.core.utilities.ObjectUtilities;
import com.snowfall.core.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class AbstractDynamicObject implements DynamicObject {

	private boolean listsInitialized = false;
	private boolean immutable;

	private List<String> indices = Collections.emptyList();
	private List<Object> nextValues = Collections.emptyList();
	private List<Object> expanded = Collections.emptyList();
	private List<Object> currentValues = Collections.emptyList();

	private static final int LIST_INITIAL_CAPACITY = 64;

	private static final String KEY_SEPARATOR_REGULAR_EXPRESSION = "\\.";
	private static final String KEY_SEGMENT_REGULAR_EXPRESSION = "([^\\[\\]]+)|(\\[[^]]+])";
	private static final Pattern KEY_SEGMENT_PATTERN = Pattern.compile(KEY_SEGMENT_REGULAR_EXPRESSION);

	@Override
	public boolean isImmutable() { return immutable; }

	protected DynamicObject setImmutable(final boolean immutable) {
		this.immutable = immutable;

		return this;
	}

	@Override
	public DynamicObject immutable() { return setImmutable(true); }

	@Override
	public Object getValue(final String key) {
		if (isEmptyDynamicObject() || isEmpty() || StringUtilities.isNull(key)) { return null; }
		// if lists are not initialized yet...
		if (!listsInitialized) {
			// we shall initialize them...
			indices = new ArrayList<>(LIST_INITIAL_CAPACITY);
			nextValues = new ArrayList<>(LIST_INITIAL_CAPACITY);
			expanded = new ArrayList<>(LIST_INITIAL_CAPACITY);
			currentValues = new ArrayList<>(LIST_INITIAL_CAPACITY);

			// and mark that lists are initialized...
			listsInitialized = true;
		}

		currentValues.clear();
		currentValues.add(this);

		boolean failed;
		Object cursor;

		final var keySegments = key.split(KEY_SEPARATOR_REGULAR_EXPRESSION);

		for (var i = 0; i < keySegments.length; ++i) {
			final var keySegment = keySegments[i];
			final var matcher = KEY_SEGMENT_PATTERN.matcher(keySegment);
			var baseKey = StringUtilities.getEmptyString();

			indices.clear();		// <-- list of indices must be cleared...

			while (matcher.find()) {
				final var matchedPortionOfKeySegment = matcher.group();

				if (matchedPortionOfKeySegment.startsWith("[")) {
					final var index = matchedPortionOfKeySegment.substring(1, matchedPortionOfKeySegment.length() - 1).trim();

					indices.add(index);
				} else {
					baseKey = matchedPortionOfKeySegment.trim();
				}
			}

			nextValues.clear();

			for (var j = 0; j < currentValues.size(); ++j) {
				final var currentValue = currentValues.get(j);

				if (currentValue == null) { continue; }

				Object value;

				if (StringUtilities.isEmpty(baseKey)) { value = currentValue; }
				else if (currentValue instanceof Map<?, ?> currentValueAsMap) {
					value = currentValueAsMap.get(baseKey);
				} else if (currentValue instanceof DynamicObject currentValueAsDynamicObject) {
					value = currentValueAsDynamicObject.asMap().get(baseKey);
				} else { continue; }

				if (value == null) { continue; }

				failed = false;
				cursor = value;

				for (var k = 0; k < indices.size(); ++k) {
					final var index = indices.get(k);

					if (cursor == null) {
						failed = true;

						break;
					}

					if ("*".equals(index)) {
						expanded.clear();

						if (!addElementsToListIfCollectionOrArray(cursor, expanded)) {
							failed = true;

							break;
						}

						cursor = expanded.toArray(ObjectUtilities.getEmptyObjectArray());
					} else {
						final var numericIndex = NumberUtilities.cast(index, Integer.class);

						if (numericIndex == null) {
							failed = true;

							break;
						}

						final var result = getElementIfCollectionOrArray(numericIndex, cursor);
						failed = result.getBoolean(1);

						// checking if getting element at the specified index failed...
						if (failed) { break; }

						cursor = result.get(2);
					}
				}

				if (failed || addElementsToListIfCollectionOrArray(cursor, nextValues)) { continue; }

				nextValues.add(cursor);
			}

			currentValues.clear();
			currentValues.addAll(nextValues);

			if (currentValues.isEmpty()) { return null; }
		}

		final var value = currentValues.size() == 1
				? currentValues.get(0)
				: new ArrayList<>(currentValues);

		return value;
	}

	private static Tuple getElementIfCollectionOrArray(final int index, final Object object) {
		// NOTE: THE FIRST BOOLEAN VALUE OF THE TUPLE REPRESENTS FAILURE OF THE OPERATION...
		if (object == null) { return Tuple.of(true, null); }
		if (object instanceof Collection<?> collection) {
			if (index < 0 || index >= collection.size()) { return Tuple.of(true, null); }

			final var element = CollectionUtilities.find(index, collection);

			return Tuple.of(false, element);
		}
		if (object.getClass().isArray()) {
			final var length = java.lang.reflect.Array.getLength(object);

			if (index < 0 || index >= length) { return Tuple.of(true, null); }

			final var element = java.lang.reflect.Array.get(object, index);

			return Tuple.of(false, element);
		}
		if (object instanceof Tuple tuple) {
			final var element = tuple.get(index + 1);

			return Tuple.of(false, element);
		}

		return Tuple.of(true, null);
	}

	private static boolean addElementsToListIfCollectionOrArray(final Object object, final List<Object> list) {
		final var collection = object instanceof Tuple tuple
				? Arrays.asList(tuple.getElements())
				: CollectionUtilities.toList(object);

		if (collection == null) { return false; }

		list.addAll(collection);

		return true;
	}

	protected static int calculateInitialCapacity(final int elementCount, final float loadFactor) {
		return (int) Math.ceil(elementCount / loadFactor) + 1;
	}

	@Override
	public int hashCode() { return asMap().hashCode(); }

	@Override
	public boolean equals(final Object object) {
		if (this == object) { return true; }
		if (!(object instanceof DynamicObject dynamicObject)) { return false; }

		return asMap().equals(dynamicObject.asMap());
	}

	@Override
	public String toString() { return toJson(true); }
}
