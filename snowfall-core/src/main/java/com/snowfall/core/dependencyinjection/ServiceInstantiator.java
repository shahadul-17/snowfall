package com.snowfall.core.dependencyinjection;

public interface ServiceInstantiator<Type> {
    Type instantiate();
}
