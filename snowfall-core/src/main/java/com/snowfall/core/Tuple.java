package com.snowfall.core;

import com.snowfall.core.text.JsonSerializer;
import com.snowfall.core.utilities.ObjectUtilities;
import com.snowfall.core.utilities.StringUtilities;

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

    @SuppressWarnings(value = "unchecked")
    public <Type> Type get(final int elementPosition) {
        final var element = get(elementPosition, Object.class);

        return (Type) element;
    }

    @SuppressWarnings(value = "unchecked")
    public <Type> Type get(final int elementPosition, final Class<Type> classOfType) {
        final var elementIndex = elementPosition - 1;
        final var element = elementIndex > -1 && elementIndex < elements.length
                ? elements[elementIndex] : null;

        // if the element is null or the types mismatch, we shall return null...
        if (element == null || !classOfType.isAssignableFrom(element.getClass())) { return null; }

        return (Type) element;
    }

    public int size() { return elements.length; }

    public boolean isEmpty() { return size() == 0; }

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

        for (int i = 2, j = 0; i < elements.length || j < restOfTheElements.length; ++i, ++j) {
            elements[i] = restOfTheElements[j];
        }

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
