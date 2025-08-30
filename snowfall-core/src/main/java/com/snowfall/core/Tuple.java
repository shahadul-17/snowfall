package com.snowfall.core;

import com.snowfall.core.text.JsonSerializer;
import com.snowfall.core.utilities.CollectionUtilities;
import com.snowfall.core.utilities.ObjectUtilities;
import com.snowfall.core.utilities.StringUtilities;

import java.util.Arrays;

public final class Tuple {

    private String json = StringUtilities.getEmptyString();
    private final Object[] elements;

    private static final Tuple EMPTY_TUPLE = new Tuple();

    private Tuple() {
        this(ObjectUtilities.getEmptyObjectArray());
    }

    private Tuple(final Object[] elements) {
        this.elements = elements;
    }

    public <Type> Type get(final int elementPosition) {
        return get(elementPosition, null);
    }

    @SuppressWarnings(value = "unchecked")
    public <Type> Type get(final int elementPosition, final Type defaultValue) {
        final var element = get(elementPosition, defaultValue, Object.class);

        return (Type) element;
    }

    public <Type> Type get(final int elementPosition, final Type defaultValue, final Class<Type> classOfType) {
        if (elementPosition < 1 || elementPosition > elements.length) { return defaultValue; }

        final var element = ObjectUtilities.cast(elements[elementPosition - 1], classOfType);

        return element == null ? defaultValue : element;
    }

    public int size() { return elements.length; }

    public boolean isEmpty() { return size() == 0; }

    public Tuple copy() { return copy(this); }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public boolean equals(final Object otherObject) {
        if (this == otherObject) { return true; }
        if (!(otherObject instanceof Tuple otherTuple)) { return false; }

        return CollectionUtilities.sequenceEqual(elements, otherTuple.elements);
    }

    @Override
    public String toString() {
        // if the JSON is an empty string...
        if (StringUtilities.isEmpty(json)) {
            // we shall serialize the elements as JSON...
            json = JsonSerializer.serialize(elements);
        }

        // and return the JSON...
        return json;
    }

    /**
     * Creates a tuple of items.
     * @implNote A tuple must contain at least two items.
     * @param firstElement The first element of the tuple.
     * @param secondElement The second element of the tuple.
     * @param restOfTheElements Rest of the elements of the tuple (optional).
     * @return A tuple containing all the elements provided.
     */
    public static Tuple of(final Object firstElement,
                           final Object secondElement,
                           final Object... restOfTheElements) {
        final var elements = new Object[restOfTheElements.length + 2];
        elements[0] = firstElement;
        elements[1] = secondElement;

        System.arraycopy(restOfTheElements, 0, elements, 2, restOfTheElements.length);

        return new Tuple(elements);
    }

    /**
     * Create a deep copy of the given tuple.
     * @param tuple Tuple to be copied.
     * @return A deep copy of the given tuple.
     */
    public static Tuple copy(final Tuple tuple) {
        final var elements = new Object[tuple.size()];

        System.arraycopy(tuple.elements, 0, elements, 0, tuple.size());

        return new Tuple(elements);
    }

    /**
     * Creates a tuple that contains no elements.
     * @return An empty tuple.
     */
    public static Tuple empty() {
        return EMPTY_TUPLE;
    }
}
