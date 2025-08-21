package com.snowfall.core.common;

import com.snowfall.core.configurations.ConfigurationProvider;
import com.snowfall.core.utilities.StringUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicStampedReference;

class UidGeneratorImpl implements UidGenerator {

    // retrieving the unique value of this application instance from configuration...
    // NOTE: THIS UNIQUE VALUE ENSURES THAT NO TWO-APPLICATION INSTANCES CAN GENERATE THE SAME UNIQUE ID...
    private final String uniqueValue = ConfigurationProvider.getConfiguration().getUniqueValue();
    private final AtomicStampedReference<Long> atomicStampedReference
            = new AtomicStampedReference<>(INITIAL_PREVIOUS_TIME_IN_MILLISECONDS, INITIAL_COUNT);

    private static final Logger logger = LogManager.getLogger(UidGeneratorImpl.class);
    private static final int MINIMUM_RANDOM_VALUE = 1_000;
    private static final int MAXIMUM_RANDOM_VALUE = 10_000;
    private static final int INITIAL_COUNT = 1;
    private static final long INITIAL_PREVIOUS_TIME_IN_MILLISECONDS = 0L;
    private static final int UID_BUILDER_INITIAL_CAPACITY = 64;

    UidGeneratorImpl() { }

    protected long getNextCount(final int randomValue, final long currentTimeInMilliseconds) {
        while (true) {
            final var previousTimeInMilliseconds = atomicStampedReference.getReference();
            final var previousCount = atomicStampedReference.getStamp();
            final var newCount = previousTimeInMilliseconds == currentTimeInMilliseconds
                    ? previousCount + 1
                    : INITIAL_COUNT;
            final var successful = atomicStampedReference.compareAndSet(
                    previousTimeInMilliseconds, currentTimeInMilliseconds, previousCount, newCount);

            if (!successful) { continue; }

            return newCount;
        }
    }

    @Override
    public String generate() {
        // taking the enhanced current system time (in milliseconds)...
        final var currentTimeInMilliseconds = EnhancedTimeProvider.getCurrentTimeInMilliseconds();
        // getting a thread local random generator...
        final Random random = ThreadLocalRandom.current();
        // we shall generate a random value within the pre-defined range...
        final var randomValue = random.nextInt(MINIMUM_RANDOM_VALUE, MAXIMUM_RANDOM_VALUE);
        // getting the next count value...
        final var count = getNextCount(randomValue, currentTimeInMilliseconds);
        // initializing a string builder to prepare the unique ID...
        final var uidBuilder = new StringBuilder(UID_BUILDER_INITIAL_CAPACITY);

        // if unique values is provided...
        if (!StringUtilities.isEmpty(uniqueValue)) {
            // we shall append the unique value to the string builder...
            uidBuilder
                    .append(uniqueValue)
                    .append(SymbolCharacter.HYPHEN.getValue());
        }

        // appending all the values to prepare a unique ID...
        uidBuilder
                .append(currentTimeInMilliseconds)
                .append(SymbolCharacter.HYPHEN.getValue())
                .append(count)
                .append(SymbolCharacter.HYPHEN.getValue())
                .append(randomValue);

        // lastly, we shall return the unique ID...
        return uidBuilder.toString();
    }
}
