package com.snowfall.core.text;

public interface JsonSerializable {

    default String toJson() {
        return toJson(false);
    }

    default String toJson(final boolean prettyPrint) {
        return JsonSerializer.serialize(this, prettyPrint);
    }
}
