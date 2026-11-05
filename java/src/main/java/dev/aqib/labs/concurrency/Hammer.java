package dev.aqib.labs.concurrency;

import java.util.concurrent.*;

/** GIVEN · runs `threads` threads, each doing `perThread` random transfers, all starting together. */
public final class Hammer {
    public static void run(Accounts a, int threads, int perThread) throws InterruptedException {
        int n = 10;
        var start = new CountDownLatch(1);
        try (ExecutorService pool = Executors.newFixedThreadPool(threads)) {
            for (int t = 0; t < threads; t++) {
                pool.submit(() -> {
                    var r = ThreadLocalRandom.current();
                    try { start.await(); } catch (InterruptedException e) { return; }
                    for (int i = 0; i < perThread; i++) {
                        int from = r.nextInt(n), to = r.nextInt(n);
                        if (from != to) a.transfer(from, to, 1 + r.nextInt(100));
                    }
                });
            }
            start.countDown();
        }
    }
}
