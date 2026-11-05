package dev.aqib.labs.concurrency;

/**
 * Thu 12 Nov · YOU write this.
 * 2 · One lock per account (ReentrantLock[]). Two transfers on different accounts run in parallel.
 *     Lock both accounts in a FIXED order (lower id first) or two opposite transfers deadlock.
 *     total() must lock every account (in order) so it never sees money in flight.
 */
public final class StripedAccounts implements Accounts {
    public StripedAccounts(int n, long start) { throw new UnsupportedOperationException("TODO"); }
    @Override public void transfer(int from, int to, long amount) { throw new UnsupportedOperationException("TODO"); }
    @Override public long balance(int id) { throw new UnsupportedOperationException("TODO"); }
    @Override public long total() { throw new UnsupportedOperationException("TODO"); }
}
