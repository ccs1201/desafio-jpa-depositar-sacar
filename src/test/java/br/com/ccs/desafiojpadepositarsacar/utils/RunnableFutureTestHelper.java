package br.com.ccs.desafiojpadepositarsacar.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.*;

@Slf4j
public class RunnableFutureTestHelper {

    public static void run(int tasks, int delay, Runnable runnable) {

        if (tasks <= 0) {
            throw new IllegalArgumentException("tasks deve ser maior que Zero");
        }

        var executor = delay > 0 ? getDelayedExecutor(delay) : Executors.newVirtualThreadPerTaskExecutor();

        var futures = new CompletableFuture[tasks];

        for (var i = 0; i < tasks; i++) {
            int finalI = i;
            futures[i] = runAsync(() -> {
                log.info("Running task {}", finalI);
                runnable.run();
            }, executor);
        }

        allOf(futures).join();
    }

    public static void run(int tasks, Runnable runnable) {
        run(tasks, 0, runnable);
    }

    private static Executor getDelayedExecutor(int delay) {
        return delayedExecutor(delay, TimeUnit.MILLISECONDS, Executors.newVirtualThreadPerTaskExecutor());
    }
}
