package istarwyh.explore;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

import static java.time.Instant.*;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;

@Slf4j
public class ParallelShowWithSleep {

    void sleep200() {
        run(sleep(200));
    }

    void sleep300() {
        run(sleep(300));
    }

    void sleep500() {
        run(sleep(500));
    }

    private Runnable sleep(int during) {
        return () -> {
            try {
                Thread.sleep(during);
            } catch (InterruptedException e) {
                log.info(""+e);
            }
        };
    }

    public static void run(Runnable runnable) {
        Instant start = now();
        runnable.run();
        Instant end = now();
        log.info(Thread.currentThread().getName() + " ------------------------ " +
                "I have run "+ Duration.between(start,end).toMillis() + " ms");
    }

    public void async() {
        run(()-> {
            try {
                allOf(runAsync(this::sleep200), runAsync(this::sleep300), runAsync(this::sleep500)).get();
            } catch (InterruptedException | ExecutionException e) {
                log.info("" + e);
            }
        });
    }

}
