package com.snowfall.core;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

class DynamicObjectImpl extends AbstractDynamicObject {

	private final boolean emptyDynamicObject;
	private final float loadFactor;
	private final Map<String, Object> contentMap;

	private static final int DEFAULT_EXPECTED_ELEMENT_COUNT = 8;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private static final DynamicObject EMPTY_DYNAMIC_OBJECT = DynamicObject.create(-1);

	private DynamicObjectImpl() { this(DEFAULT_EXPECTED_ELEMENT_COUNT); }

	private DynamicObjectImpl(final int expectedElementCount) {
		this(expectedElementCount, DEFAULT_LOAD_FACTOR);
	}

	private DynamicObjectImpl(final int expectedElementCount, final float loadFactor) {
		if (expectedElementCount < 1) {
			emptyDynamicObject = true;
			setImmutable(true);
			this.loadFactor = 0.0f;
			contentMap = Collections.emptyMap();

			return;
		}

		final var initialCapacity = calculateInitialCapacity(expectedElementCount, loadFactor);

		emptyDynamicObject = false;
		setImmutable(false);
		this.loadFactor = loadFactor;
		contentMap = new HashMap<>(initialCapacity, loadFactor);
	}

	@Override
	public boolean isEmptyDynamicObject() { return emptyDynamicObject; }

	@Override
	public float getLoadFactor() { return loadFactor; }

	@Override
	public Map<String, Object> asMap() { return contentMap; }

	static DynamicObject create() { return new DynamicObjectImpl(); }

	static DynamicObject create(final int expectedElementCount) {
		return new DynamicObjectImpl(expectedElementCount);
	}

	static DynamicObject create(final int expectedElementCount, final float loadFactor) {
		return new DynamicObjectImpl(expectedElementCount, loadFactor);
	}

	static DynamicObject empty() { return EMPTY_DYNAMIC_OBJECT; }
}
