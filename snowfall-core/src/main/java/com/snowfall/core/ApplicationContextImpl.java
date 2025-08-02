package com.snowfall.core;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ApplicationContextImpl implements ApplicationContext {

    private int exitCode = DEFAULT_EXIT_CODE;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private static final int DEFAULT_EXIT_CODE = 0;

    private ApplicationContextImpl() { }

    @Override
    public int getExitCode() {
        int exitCode;

        readLock.lock();                // <-- acquiring the read lock...

        // storing the exit code in a temporary variable...
        exitCode = this.exitCode;

        readLock.unlock();              // <-- releasing the read lock...

        // returning the exit code...
        return exitCode;
    }

    @Override
    public int getDefaultExitCode() {
        return DEFAULT_EXIT_CODE;
    }

    @Override
    public void setExitCode(final int exitCode) {
        writeLock.lock();                       // <-- acquiring the write lock...

        // assigning the exit code to the global variable...
        this.exitCode = exitCode;

        writeLock.unlock();                     // <-- releasing the write lock...
    }
}
