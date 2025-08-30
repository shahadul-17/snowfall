package com.snowfall.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class DynamicObjectImpl implements DynamicObject {

	private final boolean emptyDynamicObject;
	private boolean immutable;
	private final float loadFactor;
	private final Map<String, Object> contentMap;

	private static final int DEFAULT_EXPECTED_ELEMENT_COUNT = 8;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private DynamicObjectImpl() { this(DEFAULT_EXPECTED_ELEMENT_COUNT); }

	private DynamicObjectImpl(final int expectedElementCount) {
		this(expectedElementCount, DEFAULT_LOAD_FACTOR);
	}

	private DynamicObjectImpl(final int expectedElementCount, final float loadFactor) {
		if (expectedElementCount < 1) {
			emptyDynamicObject = true;
			immutable = true;
			this.loadFactor = 0.0f;
			contentMap = Collections.emptyMap();

			return;
		}

		final var initialCapacity = calculateInitialCapacity(expectedElementCount, loadFactor);

		emptyDynamicObject = false;
		immutable = false;
		this.loadFactor = loadFactor;
		contentMap = new HashMap<>(initialCapacity, loadFactor);
	}

	@Override
	public boolean isEmptyDynamicObject() { return emptyDynamicObject; }

	@Override
	public boolean isImmutable() { return immutable; }

	@Override
	public DynamicObject immutable() {
		immutable = true;

		return this;
	}

	@Override
	public float getLoadFactor() { return loadFactor; }

	@Override
	public Map<String, Object> asMap() { return contentMap; }

	@Override
	public int hashCode() { return contentMap.hashCode(); }

	@Override
	public boolean equals(final Object object) {
		if (this == object) { return true; }
		if (!(object instanceof DynamicObject dynamicObject)) { return false; }

		return contentMap.equals(dynamicObject.asMap());
	}

	@Override
	public String toString() { return toJson(true); }

	private static int calculateInitialCapacity(final int elementCount, final float loadFactor) {
		return (int) Math.ceil(elementCount / loadFactor) + 1;
	}

	static DynamicObject create() { return new DynamicObjectImpl(); }

	static DynamicObject create(final int expectedElementCount) {
		return new DynamicObjectImpl(expectedElementCount);
	}

	static DynamicObject create(final int expectedElementCount, final float loadFactor) {
		return new DynamicObjectImpl(expectedElementCount, loadFactor);
	}
}
