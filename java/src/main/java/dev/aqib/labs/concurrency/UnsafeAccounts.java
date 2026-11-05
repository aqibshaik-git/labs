package dev.aqib.labs.concurrency;

/**
 * GIVEN · the bug. Run main() and watch money appear and vanish.
 * Three separate problems hide here: atomicity (read-modify-write isn't one step),
 * visibility (another core may never see the write) and ordering (the JIT may reorder).
 */
public final class UnsafeAccounts implements Accounts {
    private final long[] balances;
    public UnsafeAccounts(int n, long start) { balances = new long[n]; java.util.Arrays.fill(balances, start); }
    @Override public void transfer(int from, int to, long amount) { balances[from] -= amount; balances[to] += amount; }
    @Override public long balance(int id) { return balances[id]; }
    @Override public long total() { long t = 0; for (long b : balances) t += b; return t; }

    public static void main(String[] args) throws Exception {
        var a = new UnsafeAccounts(10, 1_000_000);
        long before = a.total();
        Hammer.run(a, 100, 10_000);
        System.out.printf("before %,d  after %,d  diff %,d%n", before, a.total(), a.total() - before);
    }
}
