package com.snowfall.core.concurrency;

public interface ThreadSafeAction<ReturnType> {
    ReturnType execute() throws Throwable;
}
