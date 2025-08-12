package com.snowfall.core.concurrency;

import java.util.concurrent.atomic.AtomicBoolean;

final class LockFreeThreadSafeBoolean implements ThreadSafeBoolean {

    private final AtomicBoolean atomicBoolean;

    LockFreeThreadSafeBoolean() {
        this(false);
    }

    LockFreeThreadSafeBoolean(final boolean initialValue) {
        atomicBoolean = new AtomicBoolean(initialValue);
    }

    @Override
    public boolean get() { return atomicBoolean.get(); }

    @Override
    public void set(final boolean value) { atomicBoolean.set(value); }

    @Override
    public boolean getAndSet(final boolean value) {
        return atomicBoolean.getAndSet(value);
    }

    @Override
    public boolean compareAndSet(final boolean expectedValue, final boolean newValue) {
        return atomicBoolean.compareAndSet(expectedValue, newValue);
    }
}
