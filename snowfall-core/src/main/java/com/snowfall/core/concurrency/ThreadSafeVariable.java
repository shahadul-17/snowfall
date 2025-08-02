package com.snowfall.core.concurrency;

import com.snowfall.core.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ThreadSafeVariable<Type> {

    private Type value;

    private final Logger logger = LogManager.getLogger(ThreadSafeVariable.class);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public ThreadSafeVariable() { }

    public ThreadSafeVariable(final Type initialValue) {
        value = initialValue;
    }

    public Type getUnsafe() {
        return this.value;
    }

    public Type get() {
        return performThreadSafeOperation(false, () -> this.value);
    }

    public void setUnsafe(final Type value) {
        this.value = value;
    }

    public void set(final Type value) {
        setAndGet(value);
    }

    public Type getAndSet(final Type value) {
        return ThreadSafeExecutor.execute(writeLock, () -> {
            // storing the previous value into a temporary variable...
            final Type previousValue = this.value;
            // assigning the new value...
            this.value = value;

            return previousValue;
        });
    }

    public Type setAndGet(final Type value) {
        return performThreadSafeOperation(true, () -> {
            // assigning the new value...
            this.value = value;

            return this.value;
        });
    }

    @SuppressWarnings("unchecked")
    private Tuple performArithmeticOperation(final char arithmeticOperator, final Type operand) throws RuntimeException {
        if (value instanceof Integer valueAsNumber && operand instanceof Integer operandAsNumber) {
            final Object result = switch (arithmeticOperator) {
                case '-' -> valueAsNumber - operandAsNumber;
                case '*' -> valueAsNumber * operandAsNumber;
                case '/' -> valueAsNumber / operandAsNumber;
                default -> valueAsNumber + operandAsNumber;
            };

            var tuple = Tuple.of(value, result);
            value = (Type) result;

            return tuple;
        }

        if (value instanceof Long valueAsNumber && operand instanceof Long operandAsNumber) {
            final Object result = switch (arithmeticOperator) {
                case '-' -> valueAsNumber - operandAsNumber;
                case '*' -> valueAsNumber * operandAsNumber;
                case '/' -> valueAsNumber / operandAsNumber;
                default -> valueAsNumber + operandAsNumber;
            };

            var tuple = Tuple.of(value, result);
            value = (Type) result;

            return tuple;
        }

        if (value instanceof Float valueAsNumber && operand instanceof Float operandAsNumber) {
            final Object result = switch (arithmeticOperator) {
                case '-' -> valueAsNumber - operandAsNumber;
                case '*' -> valueAsNumber * operandAsNumber;
                case '/' -> valueAsNumber / operandAsNumber;
                default -> valueAsNumber + operandAsNumber;
            };

            var tuple = Tuple.of(value, result);
            value = (Type) result;

            return tuple;
        }

        if (value instanceof Double valueAsNumber && operand instanceof Double operandAsNumber) {
            final Object result = switch (arithmeticOperator) {
                case '-' -> valueAsNumber - operandAsNumber;
                case '*' -> valueAsNumber * operandAsNumber;
                case '/' -> valueAsNumber / operandAsNumber;
                default -> valueAsNumber + operandAsNumber;
            };

            var tuple = Tuple.of(value, result);
            value = (Type) result;

            return tuple;
        }

        if (value instanceof BigInteger valueAsNumber && operand instanceof BigInteger operandAsNumber) {
            final Object result = switch (arithmeticOperator) {
                case '-' -> valueAsNumber.subtract(operandAsNumber);
                case '*' -> valueAsNumber.multiply(operandAsNumber);
                case '/' -> valueAsNumber.divide(operandAsNumber);
                default -> valueAsNumber.add(operandAsNumber);
            };

            var tuple = Tuple.of(value, result);
            value = (Type) result;

            return tuple;
        }

        throw new RuntimeException("Arithmetic operations are not supported for the type provided.");
    }

    public Type getAndAdd(final Type valueToAdd) {
        return performThreadSafeOperation(true, () -> performArithmeticOperation('+', valueToAdd).get(1));
    }

    public Type addAndGet(final Type valueToAdd) {
        return performThreadSafeOperation(true, () -> performArithmeticOperation('+', valueToAdd).get(2));
    }

    public Type getAndSubtract(final Type valueToSubtract) {
        return performThreadSafeOperation(true, () -> performArithmeticOperation('-', valueToSubtract).get(1));
    }

    public Type subtractAndGet(final Type valueToSubtract) {
        return performThreadSafeOperation(true, () -> performArithmeticOperation('-', valueToSubtract).get(2));
    }

    public Type getAndMultiply(final Type multiplyBy) {
        return performThreadSafeOperation(true, () -> performArithmeticOperation('*', multiplyBy).get(1));
    }

    public Type multiplyAndGet(final Type multiplyBy) {
        return performThreadSafeOperation(true, () -> performArithmeticOperation('*', multiplyBy).get(2));
    }

    public Type getAndDivide(final Type divideBy) {
        return performThreadSafeOperation(true, () -> performArithmeticOperation('/', divideBy).get(1));
    }

    public Type divideAndGet(final Type divideBy) {
        return performThreadSafeOperation(true, () -> performArithmeticOperation('/', divideBy).get(2));
    }

    public Type getAndIncrement(final Type step) {
        return getAndAdd(step);
    }

    public Type incrementAndGet(final Type step) {
        return addAndGet(step);
    }

    public Type getAndDecrement(final Type step) {
        return getAndSubtract(step);
    }

    public Type decrementAndGet(final Type step) {
        return subtractAndGet(step);
    }

    /**
     * Performs thread-safe action with read-lock.
     * @param action Action to be performed.
     * @return The result after the action is performed.
     */
    public Type performThreadSafeOperation(final ThreadSafeAction<Type> action) {
        return performThreadSafeOperation(false, action);
    }

    /**
     * Performs thread-safe action with read-lock.
     * @param write If this flag is true, the action is performed
     *              using write lock.
     * @param action Action to be performed.
     * @return The result after the action is performed.
     */
    public Type performThreadSafeOperation(final boolean write, final ThreadSafeAction<Type> action) {
        return ThreadSafeExecutor.execute(write ? writeLock : readLock, action);
    }
}
