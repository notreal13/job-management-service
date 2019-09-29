package com.example.demo.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class DwhService {
    private static final Logger log = LoggerFactory.getLogger(DwhService.class);

    public static void simulateDataLoading() {
        log.warn("Loading data to DWH");

        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new RuntimeException("some error occurred");
        }

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
