package dev.aqib.labs.concurrency;

/**
 * Thu 12 Nov drill · by hand, with synchronized + wait/notifyAll only (no java.util.concurrent).
 *  - put(x) blocks while the queue is full; take() blocks while it's empty.
 *  - FIFO. Capacity fixed at construction (> 0).
 *  - Always wait in a while loop, never an if. (Why? Spurious wake-ups, and notifyAll wakes everyone.)
 */
public final class BoundedBlockingQueue<T> {
    public BoundedBlockingQueue(int capacity) { throw new UnsupportedOperationException("TODO"); }
    public void put(T item) throws InterruptedException { throw new UnsupportedOperationException("TODO"); }
    public T take() throws InterruptedException { throw new UnsupportedOperationException("TODO"); }
    public int size() { throw new UnsupportedOperationException("TODO"); }
}
