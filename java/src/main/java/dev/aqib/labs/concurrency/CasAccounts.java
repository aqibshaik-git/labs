package dev.aqib.labs.concurrency;

/**
 * Thu 12 Nov · YOU write this.
 * 3 · No locks: AtomicLongArray and compare-and-set (getAndAdd is fine too).
 *     Each account is always right on its own. But total() mid-transfer can see the debit without the credit:
 *     explain in the README why that's acceptable or not for a ledger.
 */
public final class CasAccounts implements Accounts {
    public CasAccounts(int n, long start) { throw new UnsupportedOperationException("TODO"); }
    @Override public void transfer(int from, int to, long amount) { throw new UnsupportedOperationException("TODO"); }
    @Override public long balance(int id) { throw new UnsupportedOperationException("TODO"); }
    @Override public long total() { throw new UnsupportedOperationException("TODO"); }
}
