package com.snowfall.scaffolding;

import com.snowfall.core.Application;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class SnowfallApplication implements Application {

    private final Logger logger = LogManager.getLogger(SnowfallApplication.class);

    private SnowfallApplication() { }

    @Override
    public void initialize() throws Exception {
        logger.log(Level.INFO, "Initializing Snowfall application.");
        logger.log(Level.INFO, "Snowfall application initialization successful.");
    }

    @Override
    public void execute() throws Exception {
        logger.log(Level.INFO, "Executing Snowfall application.");
        logger.log(Level.INFO, "The quick brown fox jumps over the lazy dog.");
        logger.log(Level.INFO, "Snowfall application execution completed.");
    }
}
