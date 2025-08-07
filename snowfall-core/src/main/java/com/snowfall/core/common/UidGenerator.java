package com.snowfall.core.common;

import com.snowfall.core.dependencyinjection.ServiceProvider;

public interface UidGenerator extends UniqueIdGenerator {

    static UidGenerator getInstance() {
        return ServiceProvider.getSingleton()
                .get(UidGenerator.class, UidGeneratorImpl::new);
    }
}
