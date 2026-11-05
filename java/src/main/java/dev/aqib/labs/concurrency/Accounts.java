package dev.aqib.labs.concurrency;

/** Thu 12 Nov · a fixed set of accounts (ids 0..n-1), each balance in pence. */
public interface Accounts {
    /** Move amount from one account to another. No overdraft rule today: balances may go negative. */
    void transfer(int from, int to, long amount);
    long balance(int id);
    /** Sum of all balances. Must equal the starting total whenever no transfer is in flight. */
    long total();
}
