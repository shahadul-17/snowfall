package com.snowfall.scaffolding;

import com.snowfall.core.MainBase;

public final class Main extends MainBase {

    public static void main(final String[] args) {
        // creating an instance of Main class...
        final var main = new Main();
        // calling the run() method...
        main.run(args, SnowfallApplication.class);
    }
}
