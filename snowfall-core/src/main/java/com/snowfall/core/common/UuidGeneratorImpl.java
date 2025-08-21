package com.snowfall.core.common;

import java.util.UUID;

class UuidGeneratorImpl implements UuidGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
