package com.snowfall.core.common;

import com.snowfall.core.utilities.DateTimeFormatter;

class StopwatchImpl implements Stopwatch {

    private boolean nanosecondPrecisionEnabled = true;
    private long startTime = 0L;
    private long endTime = 0L;
    private long elapsedTime = 0L;

    private long getCurrentTimestamp() {
        return nanosecondPrecisionEnabled
                ? System.nanoTime()
                : System.currentTimeMillis();
    }

    @Override
    public Stopwatch enableNanosecondPrecision() {
        nanosecondPrecisionEnabled = true;

        return this;
    }

    @Override
    public Stopwatch disableNanosecondPrecision() {
        nanosecondPrecisionEnabled = false;

        return this;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    @Override
    public Stopwatch reset() {
        startTime = 0L;
        endTime = 0L;
        elapsedTime = 0L;

        return this;
    }

    @Override
    public Stopwatch start() {
        // if the stopwatch is already started or
        // the stopwatch is already stopped...
        if (startTime != 0L || endTime != 0L) { return this; }

        // getting current timestamp...
        startTime = getCurrentTimestamp();

        return this;
    }

    @Override
    public Stopwatch startNew() {
        reset();

        return start();
    }

    @Override
    public Stopwatch stop() {
        // if the stopwatch is not started, we'll return...
        if (startTime == 0L) { return this; }

        // getting current timestamp...
        endTime = getCurrentTimestamp();
        // measuring the elapsed time...
        elapsedTime = endTime - startTime;

        return this;
    }

    @Override
    public long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public String getHumanReadableElapsedTime() {
        // getting the elapsed time...
        final var elapsedTime = getElapsedTime();
        // formatting the elapsed time to make it more human-readable...
        final var humanReadableElapsedTime = nanosecondPrecisionEnabled
                ? DateTimeFormatter.formatNanosecondsTime(elapsedTime)
                : DateTimeFormatter.formatTime(elapsedTime);

        // returning the human-readable elapsed time...
        return humanReadableElapsedTime;
    }
}
