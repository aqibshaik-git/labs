package dev.aqib.labs.concurrency;

import org.junit.jupiter.api.Test;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountsTest {
    static void conserves(Accounts a) throws Exception {
        long before = a.total();
        Hammer.run(a, 100, 2_000);
        assertEquals(before, a.total(), a.getClass().getSimpleName() + " lost or created money");
    }

    /** Two threads transferring in opposite directions between the same pair, forever-ish. Deadlocks if lock order is wrong. */
    static void noDeadlock(Accounts a) throws Exception {
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
            Future<?> x = pool.submit(() -> { for (int i = 0; i < 200_000; i++) a.transfer(1, 2, 1); });
            Future<?> y = pool.submit(() -> { for (int i = 0; i < 200_000; i++) a.transfer(2, 1, 1); });
            try { x.get(20, TimeUnit.SECONDS); y.get(20, TimeUnit.SECONDS); }
            catch (TimeoutException e) { System.exit(97); }   // a deadlock never times out on its own
        }
        assertEquals(a.balance(1) + a.balance(2), 2_000_000L);
    }

    @Test void synchronizedConservesMoney() throws Exception { conserves(new SynchronizedAccounts(10, 1_000_000)); }
    @Test void stripedConservesMoney() throws Exception { conserves(new StripedAccounts(10, 1_000_000)); }
    @Test void casConservesMoney() throws Exception { conserves(new CasAccounts(10, 1_000_000)); }
    @Test void stripedNeverDeadlocks() throws Exception { noDeadlock(new StripedAccounts(10, 1_000_000)); }
}
