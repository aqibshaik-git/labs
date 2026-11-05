package dev.aqib.labs.concurrency;

import org.junit.jupiter.api.Test;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.jupiter.api.Assertions.*;

class BoundedBlockingQueueTest {
    @Test void fifoWithinCapacity() throws Exception {
        var q = new BoundedBlockingQueue<String>(3);
        q.put("a"); q.put("b"); q.put("c");
        assertEquals(3, q.size());
        assertEquals("a", q.take()); assertEquals("b", q.take()); assertEquals("c", q.take());
    }

    @Test void putBlocksWhenFullUntilATake() throws Exception {
        var q = new BoundedBlockingQueue<Integer>(1);
        q.put(1);
        try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
            Future<?> blocked = pool.submit(() -> { q.put(2); return null; });
            Thread.sleep(200);
            assertFalse(blocked.isDone());
            assertEquals(1, q.take());
            blocked.get(2, TimeUnit.SECONDS);
            assertEquals(2, q.take());
        }
    }

    @Test void manyProducersAndConsumersLoseNothing() throws Exception {
        var q = new BoundedBlockingQueue<Long>(8);
        var sum = new AtomicLong();
        try (ExecutorService pool = Executors.newFixedThreadPool(8)) {
            for (int p = 0; p < 4; p++) pool.submit(() -> { for (long i = 1; i <= 10_000; i++) q.put(i); return null; });
            for (int c = 0; c < 4; c++) pool.submit(() -> { for (int i = 0; i < 10_000; i++) sum.addAndGet(q.take()); return null; });
        }
        assertEquals(4L * 10_000 * 10_001 / 2, sum.get());
    }
}
