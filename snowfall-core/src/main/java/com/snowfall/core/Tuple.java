package com.snowfall.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.snowfall.core.text.JsonSerializable;
import com.snowfall.core.text.JsonSerializer;
import com.snowfall.core.utilities.CollectionUtilities;
import com.snowfall.core.utilities.ObjectUtilities;
import com.snowfall.core.utilities.StringUtilities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

@JsonSerialize(using = TupleJacksonJsonSerializer.class)
@JsonDeserialize(using = TupleJacksonJsonDeserializer.class)
public final class Tuple implements JsonSerializable {

    private String json = StringUtilities.getEmptyString();
    private final Object[] elements;

    private static final Tuple EMPTY_TUPLE = new Tuple();

    private Tuple() {
        this(ObjectUtilities.getEmptyObjectArray());
    }

    private Tuple(final Object[] elements) {
        this.elements = elements;
    }

    private Object getElement(final int elementPosition) {
        return elementPosition < 1 || elementPosition > elements.length
                ? null
                : elements[elementPosition - 1];
    }

    Object[] getElements() { return elements; }

    @SuppressWarnings(value = "unchecked")
    public <Type> Type get(final int elementPosition) { return (Type) getElement(elementPosition); }

    public <Type> Type get(final int elementPosition, final Type defaultValue) {
        final Type element = get(elementPosition);

        // if the element is null, we shall return the default value...
        return element == null ? defaultValue : element;
    }

    public <Type> Type get(final int elementPosition, final Type defaultValue, final Class<Type> classOfType) {
        if (classOfType == null) { return get(elementPosition, defaultValue); }

        final var element = ObjectUtilities.cast(getElement(elementPosition), classOfType);

        // if the element is null, we shall return the default value...
        return element == null ? defaultValue : element;
    }

    public Boolean getBoolean(final int elementPosition) { return getBoolean(elementPosition, null); }

    public Boolean getBoolean(final int elementPosition, final Boolean defaultValue) { return get(elementPosition, defaultValue, Boolean.class); }

    public Byte getByte(final int elementPosition) { return getByte(elementPosition, null); }

    public Byte getByte(final int elementPosition, final Byte defaultValue) { return get(elementPosition, defaultValue, Byte.class); }

    public Short getShort(final int elementPosition) { return getShort(elementPosition, null); }

    public Short getShort(final int elementPosition, final Short defaultValue) { return get(elementPosition, defaultValue, Short.class); }

    public Integer getInteger(final int elementPosition) { return getInteger(elementPosition, null); }

    public Integer getInteger(final int elementPosition, final Integer defaultValue) { return get(elementPosition, defaultValue, Integer.class); }

    public Long getLong(final int elementPosition) { return getLong(elementPosition, null); }

    public Long getLong(final int elementPosition, final Long defaultValue) { return get(elementPosition, defaultValue, Long.class); }

    public BigInteger getBigInteger(final int elementPosition) { return getBigInteger(elementPosition, null); }

    public BigInteger getBigInteger(final int elementPosition, final BigInteger defaultValue) { return get(elementPosition, defaultValue, BigInteger.class); }

    public Float getFloat(final int elementPosition) { return getFloat(elementPosition, null); }

    public Float getFloat(final int elementPosition, final Float defaultValue) { return get(elementPosition, defaultValue, Float.class); }

    public Double getDouble(final int elementPosition) { return getDouble(elementPosition, null); }

    public Double getDouble(final int elementPosition, final Double defaultValue) { return get(elementPosition, defaultValue, Double.class); }

    public BigDecimal getBigDecimal(final int elementPosition) { return getBigDecimal(elementPosition, null); }

    public BigDecimal getBigDecimal(final int elementPosition, final BigDecimal defaultValue) { return get(elementPosition, defaultValue, BigDecimal.class); }

    public Number getNumber(final int elementPosition) { return getNumber(elementPosition, null); }

    public Number getNumber(final int elementPosition, final Number defaultValue) { return get(elementPosition, defaultValue, Number.class); }

    public Character getCharacter(final int elementPosition) { return getCharacter(elementPosition, null); }

    public Character getCharacter(final int elementPosition, final Character defaultValue) { return get(elementPosition, defaultValue, Character.class); }

    public String getString(final int elementPosition) { return getString(elementPosition, null); }

    public String getString(final int elementPosition, final String defaultValue) { return get(elementPosition, defaultValue, String.class); }

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
            json = toJson(true);
        }

        // and return the JSON...
        return json;
    }

    static Tuple of(final Object[] elements) { return new Tuple(elements); }

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

        return of(elements);
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

    public static Tuple fromJson(final String json) {
        return JsonSerializer.deserialize(json, Tuple.class);
    }
}
