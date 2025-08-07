package com.snowfall.core.common;

import com.snowfall.core.concurrency.ThreadSafeExecutor;
import com.snowfall.core.configurations.ConfigurationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class UidGeneratorImpl implements UidGenerator {

    private volatile long count = INITIAL_COUNT;
    private volatile long previousTimeInMilliseconds = -1L;

    private final Lock lock = new ReentrantLock(false);

    private static final Logger logger = LogManager.getLogger(UidGeneratorImpl.class);
    private static final byte BLOCK_LENGTH = 4;
    private static final int MINIMUM_RANDOM_VALUE = 1000;
    private static final int MAXIMUM_RANDOM_VALUE = 9999;
    private static final long INITIAL_COUNT = 1L;
    private static final char DASH = '-';

    UidGeneratorImpl() { }

    private long getNextCount(final long currentTimeInMilliseconds) {
        return ThreadSafeExecutor.execute(lock, () -> {
            // computing next count...
            // if the previous time is equal to the current time...
            final var count = previousTimeInMilliseconds == currentTimeInMilliseconds
                    ? this.count + 1            // <-- we shall increment the count...
                    : INITIAL_COUNT;            // <-- otherwise, we shall reset the count with the initial value...

            // then we shall re-assign the global count...
            this.count = count;
            // we'll then assign the current time to the previous time (global)...
            previousTimeInMilliseconds = currentTimeInMilliseconds;

            // lastly, we shall return the count...
            return count;
        });
    }

    @Override
    public String generate(final boolean dashed) {
        // retrieving the unique value of this application instance from configuration...
        // NOTE: THIS UNIQUE VALUE ENSURES THAT NO TWO-APPLICATION INSTANCES CAN GENERATE THE SAME UNIQUE ID...
        final var uniqueValue = ConfigurationProvider.getConfiguration().getUniqueValue();
        // taking the enhanced current system time (in milliseconds)...
        final var currentTimeInMilliseconds = EnhancedTimeProvider.getCurrentTimeInMilliseconds();
        // getting the next count value...
        final var count = getNextCount(currentTimeInMilliseconds);
        // getting a thread local random generator...
        final Random random = ThreadLocalRandom.current();
        // we shall generate a random value within the pre-defined range...
        final var randomValue = random.nextInt(MINIMUM_RANDOM_VALUE, MAXIMUM_RANDOM_VALUE) + 1;
        // appending all the values to prepare a unique ID...
        // lastly, we shall return the unique ID...
        final var uid = uniqueValue + currentTimeInMilliseconds + count + randomValue;

        if (dashed) { return toDashedUid(uid); }

        return uid;
    }

    private static String toDashedUid(final String uid) {
        if (uid.length() < BLOCK_LENGTH) { return uid; }

        var j = 0;
        final var dashedUidMaximumLength = uid.length() + ((uid.length() - 1) / BLOCK_LENGTH);
        final var dashedUid = new char[dashedUidMaximumLength];

        for (var i = 0; i < uid.length(); i += BLOCK_LENGTH) {
            var length = Math.min(BLOCK_LENGTH, uid.length() - i);

            uid.getChars(i, i + length, dashedUid, j);

            j += length;

            if (j < dashedUidMaximumLength) {
                dashedUid[j] = DASH;
                ++j;
            }
        }

        return new String(dashedUid, 0, j);
    }
}
