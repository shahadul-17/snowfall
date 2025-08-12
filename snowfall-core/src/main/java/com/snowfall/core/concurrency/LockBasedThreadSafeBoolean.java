package com.snowfall.core.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

final class LockBasedThreadSafeBoolean implements ThreadSafeBoolean {

    private volatile boolean value;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    LockBasedThreadSafeBoolean() { this(false); }

    LockBasedThreadSafeBoolean(final boolean initialValue) {
        value = initialValue;
    }

    @Override
    public boolean get() {
        return performThreadSafeOperation(false, () -> this.value);
    }

    @Override
    public void set(final boolean value) {
        performThreadSafeOperation(true, () -> this.value = value);
    }

    @Override
    public boolean getAndSet(final boolean value) {
        return performThreadSafeOperation(true, () -> {
            // storing the previous value into a temporary variable...
            final var previousValue = this.value;
            // assigning the new value...
            this.value = value;

            return previousValue;
        });
    }

    @Override
    public boolean setAndGet(final boolean value) {
        return performThreadSafeOperation(true, () -> {
            // assigning the new value...
            this.value = value;

            return value;
        });
    }

    @Override
    public boolean compareAndSet(final boolean expectedValue, final boolean newValue) {
        return performThreadSafeOperation(true, () -> {
            // if the expected value does not match the current value,
            // we shall return false...
            if (expectedValue != this.value) { return false; }

            // otherwise, we'll assign the new value...
            this.value = newValue;

            // and return true...
            return true;
        });
    }

    @Override
    public <Type> Type performThreadSafeOperation(final boolean write, final ThreadSafeAction<Type> action) {
        return ThreadSafeExecutor.execute(write ? writeLock : readLock, action);
    }
}
