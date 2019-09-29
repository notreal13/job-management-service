package com.example.demo.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class IndexingService {
    private static final Logger log = LoggerFactory.getLogger(IndexingService.class);

    public static void simulateIndexing() {
        int nextInt = ThreadLocalRandom.current().nextInt(1_000);
        log.warn("Indexing {} started", nextInt);
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(42));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.warn("Indexing {} finished", nextInt);
    }
}
