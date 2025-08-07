package com.snowfall.core.common;

import com.snowfall.core.utilities.StringUtilities;

import java.util.UUID;

class UuidGeneratorImpl implements UuidGenerator {

    private static final String DASH = "-";

    @Override
    public String generate(final boolean dashed) {
        final var uuid = UUID.randomUUID().toString();

        if (dashed) { return uuid; }

        return uuid.replace(DASH, StringUtilities.getEmptyString());
    }
}
