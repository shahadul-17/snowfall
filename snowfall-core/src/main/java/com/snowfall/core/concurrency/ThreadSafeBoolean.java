package com.snowfall.core.concurrency;

public interface ThreadSafeBoolean {

	/**
	 * Retrieves the current boolean value in a thread-safe manner.
	 * @return The current value of the boolean, which could be either {@code true} or {@code false}.
	 */
	boolean get();

	/**
	 * Sets the boolean value in a thread-safe manner.
	 * @param value The new boolean value to set. Passing {@code true} sets the value to true,
	 *              and passing {@code false} sets the value to false.
	 */
	void set(final boolean value);

	/**
	 * Atomically sets the value to the given new value and returns the previous value.
	 * This operation is performed in a thread-safe manner, ensuring that no other
	 * thread can access the value between the retrieval of the old value and the setting
	 * of the new value.
	 * @param value The new boolean value to be set. Passing {@code true} sets the value to true,
	 *              and passing {@code false} sets the value to false.
	 * @return The original value of the boolean before the operation.
	 */
	boolean getAndSet(final boolean value);

	/**
	 * Atomically updates the boolean value to the given {@code newValue} if the current value
	 * equals the specified {@code expectedValue}. This operation ensures thread safety during
	 * the comparison and update process.
	 * @param expectedValue The value expected to match the current value.
	 * @param newValue The new value to set if the current value matches the {@code expectedValue}.
	 * @return {@code true} if the value was successfully updated to {@code newValue}, {@code false} otherwise.
	 */
	boolean compareAndSet(final boolean expectedValue, final boolean newValue);

	/**
	 * Atomically sets the boolean value to the provided value and returns the new value.
	 * This method ensures thread safety during the operation.
	 * @param value The new boolean value to set. Passing {@code true} sets the value to true,
	 *              and passing {@code false} sets the value to false.
	 * @return The newly set value of the boolean, which will be either {@code true} or {@code false}.
	 */
	default boolean setAndGet(final boolean value) {
		getAndSet(value);

		return value;
	}

	/**
	 * Atomically sets the value to {@code false} if it is currently {@code true}.
	 * This method shall always return the previous value. Which means if the value
	 * was previously false, this method shall return true. But if the value
	 * was previously false, this method shall return false.
	 * @return The original value of the boolean before the operation.
	 */
	default boolean makeFalseIfTrue() {
		return compareAndSet(true, false);
	}

	/**
	 * Atomically sets the value to {@code true} if it is currently {@code false}.
	 * This method shall always return the previous value. Which means if the value
	 * was previously true, this method shall return true. But if the value
	 * was previously false, this method shall return false.
	 * @return The original value of the boolean before the operation.
	 */
	default boolean makeTrueIfFalse() {
		return !compareAndSet(false, true);
	}

	/**
	 * Performs thread-safe action with read or write lock.
	 * @param write If this flag is true, the action is performed
	 *              using write lock.
	 * @param action Action to be performed.
	 * @return The result after the action is performed.
	 */
	default <Type> Type performThreadSafeOperation(final boolean write, final ThreadSafeAction<Type> action) {
		throw new UnsupportedOperationException("This method is not supported by the current implementation.");
	}

	/**
	 * Creates and returns an instance of a thread-safe boolean with a lock-free implementation.
	 * The returned instance ensures atomic operations on the boolean value, utilizing
	 * lock-free mechanisms for thread safety.
	 * @return A new instance of {@code ThreadSafeBoolean} with a lock-free implementation.
	 */
	static ThreadSafeBoolean createLockFree() {
		return new LockFreeThreadSafeBoolean();
	}

	/**
	 * Creates and returns an instance of a thread-safe boolean with a lock-free implementation.
	 * The returned instance is initialized with the specified boolean value and ensures
	 * atomic operations on the boolean value, utilizing lock-free mechanisms for thread safety.
	 * @param initialValue The initial boolean value to be set for the returned instance.
	 *                     Passing {@code true} initializes it to {@code true}, while
	 *                     passing {@code false} initializes it to {@code false}.
	 * @return A new instance of {@code ThreadSafeBoolean} with a lock-free implementation,
	 * initialized with the provided initial value.
	 */
	static ThreadSafeBoolean createLockFree(final boolean initialValue) {
		return new LockFreeThreadSafeBoolean(initialValue);
	}

	/**
	 * Creates and returns an instance of a thread-safe boolean with a lock-based implementation.
	 * The returned instance ensures atomic operations on the boolean value using locks to maintain thread safety.
	 * @return A new instance of {@code ThreadSafeBoolean} with a lock-based implementation.
	 */
	static ThreadSafeBoolean createLockBased() {
		return new LockBasedThreadSafeBoolean();
	}

	/**
	 * Creates and returns an instance of a thread-safe boolean with a lock-based implementation.
	 * The instance is initialized with the provided initial boolean value and ensures
	 * atomic operations on the boolean value using locks to maintain thread safety.
	 * @param initialValue The initial boolean value to be set for the returned instance.
	 *                     Passing {@code true} initializes it to {@code true},
	 *                     while passing {@code false} initializes it to {@code false}.
	 * @return A new instance of {@code ThreadSafeBoolean} with a lock-based implementation,
	 * initialized with the specified initial value.
	 */
	static ThreadSafeBoolean createLockBased(final boolean initialValue) {
		return new LockBasedThreadSafeBoolean(initialValue);
	}
}
