package dev.aqib.labs.concurrency;

/**
 * Thu 12 Nov · YOU write this.
 * 1 · One lock for everything (synchronized). Correct and simple: what does it cost?
 */
public final class SynchronizedAccounts implements Accounts {
    public SynchronizedAccounts(int n, long start) { throw new UnsupportedOperationException("TODO"); }
    @Override public void transfer(int from, int to, long amount) { throw new UnsupportedOperationException("TODO"); }
    @Override public long balance(int id) { throw new UnsupportedOperationException("TODO"); }
    @Override public long total() { throw new UnsupportedOperationException("TODO"); }
}
