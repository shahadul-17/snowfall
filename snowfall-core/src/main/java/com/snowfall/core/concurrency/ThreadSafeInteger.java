package com.snowfall.core.concurrency;

import com.snowfall.core.Tuple;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ThreadSafeInteger {

    private volatile int value = 0;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public ThreadSafeInteger() { }

    public ThreadSafeInteger(final int initialValue) {
        value = initialValue;
    }

    public int getUnsafe() {
        return value;
    }

    public int get() {
        return ThreadSafeExecutor.execute(readLock, this::getUnsafe);
    }

    public void setUnsafe(final int value) {
        this.value = value;
    }

    public void set(final int value) {
        setAndGet(value);
    }

    public int getAndSet(final int value) {
        return ThreadSafeExecutor.execute(writeLock, () -> {
            // storing the previous value into a temporary variable...
            final var previousValue = this.value;
            // assigning the new value...
            setUnsafe(value);

            return previousValue;
        });
    }

    public int setAndGet(final int value) {
        return ThreadSafeExecutor.execute(writeLock, () -> {
            // assigning the new value...
            setUnsafe(value);

            return this.value;
        });
    }

    public int addAndGet(final int valueToAdd) {
        return ThreadSafeExecutor.execute(writeLock, () -> {
            // adding and assigning the new value...
            setUnsafe(value + valueToAdd);

            return value;
        });
    }

    public int subtractAndGet(final int valueToSubtract) {
        return ThreadSafeExecutor.execute(writeLock, () -> {
            // subtracting and assigning the new value...
            setUnsafe(value - valueToSubtract);

            return value;
        });
    }

    public int multiplyAndGet(final int multiplyBy) {
        return ThreadSafeExecutor.execute(writeLock, () -> {
            // multiplying and assigning the new value...
            setUnsafe(value * multiplyBy);

            return value;
        });
    }

    public int divideAndGet(final int divideBy) {
        return ThreadSafeExecutor.execute(writeLock, () -> {
            // dividing and assigning the new value...
            setUnsafe(value / divideBy);

            return value;
        });
    }

    public boolean isLessThan(final int value) {
        return ThreadSafeExecutor.execute(readLock, () -> this.value < value);
    }

    public boolean isLessThanOrEqualTo(final int value) {
        return ThreadSafeExecutor.execute(readLock, () -> this.value <= value);
    }

    public boolean isGreaterThan(final int value) {
        return ThreadSafeExecutor.execute(readLock, () -> this.value > value);
    }

    public boolean isGreaterThanOrEqualTo(final int value) {
        return ThreadSafeExecutor.execute(readLock, () -> this.value >= value);
    }

    @Override
    public boolean equals(final Object valueAsObject) {
        if (!(valueAsObject instanceof Integer valueAsNumber)) { return false; }

        return ThreadSafeExecutor.execute(readLock, () -> value == valueAsNumber);
    }

    /**
     * This method first adds two values (global value and the value to add)
     * and checks if the result is less than the value to check.
     * If the result is less than the given value to check, it updates the global value
     * and returns true. Otherwise, it does not update the global value and returns false.
     * @implNote This method is synchronized.
     * @param valueToAdd Value to add to the global value.
     * @param valueToCheck Value to compare with.
     * @return A tuple containing a 'boolean' indicating if the value is less than
     * the value to compare and a 'int' containing the current value (after performing the operation).
     */
    public Tuple addIfAfterAdditionLessThan(final int valueToAdd, final int valueToCheck) {
        // acquiring write lock...
        return ThreadSafeExecutor.execute(writeLock, () -> {
            // first we shall add the two values...
            final var result = value + valueToAdd;

            // if after addition, the result is less than
            // the value to check...
            if (result < valueToCheck) {
                // we shall set the result to the value...
                setUnsafe(result);

                // we shall return a tuple containing 'true'...
                return Tuple.of(true, value);
            }

            // otherwise, we shall return a tuple containing 'false'...
            return Tuple.of(false, value);
        });
    }

    /**
     * This method first subtracts the given value from the global value
     * and checks if the result is greater than the value to check.
     * If the result is greater than the given value to check, it updates the global value
     * and returns true. Otherwise, it does not update the global value and returns false.
     * @implNote This method is synchronized.
     * @param valueToSubtract Value to subtract from the global value.
     * @param valueToCheck Value to compare with.
     * @return A tuple containing a 'boolean' indicating if the value is greater than
     * the value to compare and a 'int' containing the current value (after performing the operation).
     */
    public Tuple subtractIfAfterSubtractionGreaterThan(final int valueToSubtract, final int valueToCheck) {
        // acquiring write lock...
        return ThreadSafeExecutor.execute(writeLock, () -> {
            // first we shall subtract the given value from our current global value...
            final var result = value - valueToSubtract;

            // if after subtraction, the result is greater than
            // the value to check...
            if (result > valueToCheck) {
                // we shall set the result to the value...
                setUnsafe(result);

                // we shall return a tuple containing 'true'...
                return Tuple.of(true, value);
            }

            // otherwise, we shall return a tuple containing 'false'...
            return Tuple.of(false, value);
        });
    }
}
