package com.snowfall.core.utilities;

import com.snowfall.core.text.JsonSerializer;

import java.util.Map;

public final class ObjectUtilities {

    private static final Object EMPTY_OBJECT = new Object();
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public static Object getEmptyObject() { return EMPTY_OBJECT; }

    public static Object[] getEmptyObjectArray() { return EMPTY_OBJECT_ARRAY; }

    /**
     * Prepares a map that contains all the attributes of an object.
     * @implNote This method has a serious performance penalty.
     * Performance could be improved using reflection.
     * @param object Object from which the map shall be prepared.
     * @return A map containing all the attributes of an object.
     */
    public static Map<String, Object> toMap(Object object) {
        // first, we need to serialize the object as JSON...
        var json = JsonSerializer.serialize(object);
        // then we shall deserialize the JSON as map...
        Map<String, Object> map = JsonSerializer.deserializeAsMap(json);

        // finally, we shall return the map...
        return map;
    }

    /**
     * Prepares an object (of a specified type) that contains all the attributes of the map.
     * @implNote This method has a serious performance penalty.
     * Performance could be improved using reflection.
     * @param map Map from which the object shall be prepared.
     * @param classOfType Class of the desired object type.
     * @param <Type> Type of the object.
     * @return An object of the specified type containing all the attributes of the map.
     */
    public static <Type> Type fromMap(Map<String, Object> map, Class<Type> classOfType) {
        // first, we need to serialize the map as JSON...
        final var json = JsonSerializer.serialize(map);
        // then we shall deserialize the JSON as the specified class object...
        final Type object = JsonSerializer.deserialize(json, classOfType);

        // finally, we shall return the object...
        return object;
    }

    public static <Type> Type cast(final Object value, final Class<Type> classOfType) {
        // if the value is null, we shall return the default value...
        if (value == null) { return null; }
        // performing a type cast if the value is an instance of the class...
        if (classOfType.isInstance(value)) { return classOfType.cast(value); }

        // if the value is a number, we shall attempt to cast the value
        // to the expected type...
        final var valueAsNumber = NumberUtilities.cast(value, classOfType);

        // if the value is successfully cast to the expected type, we shall return it...
        if (valueAsNumber != null) { return valueAsNumber; }
        // performing a type cast if the expected type is boolean...
        if (classOfType == Boolean.class) { return convertBoolean(value, classOfType); }
        // if the expected type is string, we shall attempt to convert the value to a string and return it as a string...
        if (classOfType == String.class) { return classOfType.cast(value.toString()); }

        return null;
    }

    private static <Type> Type convertBoolean(final Object value, final Class<Type> classOfType) {
        if (value instanceof Boolean) { return classOfType.cast(value); }
        if (value instanceof String valueAsString) {
            final var sanitizedValueAsString = StringUtilities.getDefaultIfNullOrWhiteSpace(
                    valueAsString, StringUtilities.getEmptyString(), true);

            if ("true".equalsIgnoreCase(sanitizedValueAsString)
                    || "false".equalsIgnoreCase(sanitizedValueAsString)) {
                return classOfType.cast(Boolean.valueOf(sanitizedValueAsString));
            }

            final var valueAsNumber = NumberUtilities.cast(valueAsString, Number.class);

            // checking if the value is successfully cast as a number...
            if (valueAsNumber != null) {
                // NOTE: ANY VALUE LESS THAN OR EQUAL TO ZERO (0) IS CONSIDERED FALSE AND NON-ZERO IS CONSIDERED TRUE...!!!
                return classOfType.cast(valueAsNumber.intValue() > 0);
            }
        }
        if (value instanceof Number valueAsNumber) {
            // NOTE: ANY VALUE LESS THAN OR EQUAL TO ZERO (0) IS CONSIDERED FALSE AND NON-ZERO IS CONSIDERED TRUE...!!!
            return classOfType.cast(valueAsNumber.intValue() > 0);
        }

        return classOfType.cast(value);
    }
}
