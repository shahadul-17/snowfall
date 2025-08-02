package com.snowfall.core.common;

/**
 * Provides a set of methods that you can use
 * to measure elapsed time.
 * NOTES:
 * 1. IMPLEMENTATIONS OF THIS INTERFACE ARE NOT THREAD SAFE.
 * 2. PRECISION OF THE ELAPSED TIME DEPENDS ON THE IMPLEMENTATION.
 */
public interface Stopwatch {

    /**
     * Enables nanosecond-level precision.
     * @return The stopwatch instance.
     */
    Stopwatch enableNanosecondPrecision();

    /**
     * Disables nanosecond-level precision.
     * @return The stopwatch instance.
     */
    Stopwatch disableNanosecondPrecision();

    /**
     * Resets the stopwatch.
     * @return The stopwatch instance.
     */
    Stopwatch reset();

    /**
     * Starts the stopwatch.
     * @return The stopwatch instance.
     */
    Stopwatch start();

    /**
     * Resets the current state of the stopwatch
     * and starts again.
     * @return The stopwatch instance.
     */
    Stopwatch startNew();

    /**
     * Stops the stopwatch.
     * @implNote This method updates the elapsed time.
     * So you may stop a stopwatch multiple times.
     * @return The stopwatch instance.
     */
    Stopwatch stop();

    /**
     * Gets the time when the stopwatch was started.
     * @return The start time.
     */
    long getStartTime();

    /**
     * Gets the time when the stopwatch was stopped.
     * @return The end time.
     */
    long getEndTime();

    /**
     * Gets the elapsed time.
     * @return The elapsed time.
     */
    long getElapsedTime();

    /**
     * Gets the elapsed time in a human-readable format.
     * @return The elapsed time.
     */
    String getHumanReadableElapsedTime();

    /**
     * Creates a new instance of Stopwatch.
     * @return An instance of Stopwatch.
     */
    static Stopwatch create() {
        return new StopwatchImpl();
    }
}
