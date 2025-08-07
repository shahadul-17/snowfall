package com.snowfall.core.common;

import com.snowfall.core.dependencyinjection.ServiceProvider;

public interface UuidGenerator extends UniqueIdGenerator {

    static UuidGenerator getInstance() {
        return ServiceProvider.getSingleton()
                .get(UuidGenerator.class, UuidGeneratorImpl::new);
    }
}
