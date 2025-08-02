package com.snowfall.core;

import com.snowfall.core.dependencyinjection.ServiceProvider;

public interface ApplicationContext {

    int getExitCode();
    int getDefaultExitCode();
    void setExitCode(final int exitCode);

    default void resetExitCode() {
        setExitCode(getDefaultExitCode());
    }

    static ApplicationContext getInstance() {
        return ServiceProvider.getSingleton().get(ApplicationContextImpl.class);
    }
}
