package dev.aqib.labs.transfer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Drill, part 1: a value object written the long way, so every line a record generates is visible.
 *
 * A transfer moves a positive amount from one account to a different account, in one currency.
 * Value semantics: two transfers with the same fields are the same transfer (that's what lets an
 * idempotency store or a HashSet recognise a retry).
 *
 * The checklist a record does for you, done by hand:
 *  1. final class          - nobody can subclass it and break equals (the Liskov/equals trap, EJ item 10)
 *  2. private final fields - state can't change after construction, so it's safe to share and to use as a key
 *  3. validate in the constructor - an invalid Transfer can never exist (no "half-built" objects)
 *  4. normalise before storing    - 10 and 10.00 GBP become the same BigDecimal, so equals is honest
 *  5. equals + hashCode over the same fields (EJ item 11: override both or neither)
 *  6. toString for logs, accessors with no setters
 */
public final class TransferByHand {

    private final String fromAccountId;
    private final String toAccountId;
    private final BigDecimal amount;
    private final Currency currency;

    public TransferByHand(String fromAccountId, String toAccountId, BigDecimal amount, String currencyCode) {
        this.fromAccountId = requireText(fromAccountId, "fromAccountId");
        this.toAccountId = requireText(toAccountId, "toAccountId");
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("A transfer needs two different accounts, got " + fromAccountId + " twice");
        }
        this.currency = Currency.getInstance(Objects.requireNonNull(currencyCode, "currencyCode"));
        this.amount = normalise(Objects.requireNonNull(amount, "amount"), currency);
    }

    public String fromAccountId() { return fromAccountId; }
    public String toAccountId() { return toAccountId; }
    public BigDecimal amount() { return amount; }
    public String currency() { return currency.getCurrencyCode(); }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferByHand t)) return false; // final class, so instanceof is safe (no subclass asymmetry)
        return fromAccountId.equals(t.fromAccountId)
                && toAccountId.equals(t.toAccountId)
                && amount.equals(t.amount)        // safe: both already at the currency's scale
                && currency.equals(t.currency);
    }

    @Override public int hashCode() {
        return Objects.hash(fromAccountId, toAccountId, amount, currency);
    }

    @Override public String toString() {
        return "Transfer[" + fromAccountId + " -> " + toAccountId + ", " + currency.getCurrencyCode() + " " + amount.toPlainString() + "]";
    }

    // shared with the record version so both enforce identical rules
    static String requireText(String s, String name) {
        Objects.requireNonNull(s, name);
        if (s.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
        return s;
    }

    static BigDecimal normalise(BigDecimal amount, Currency currency) {
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive: " + amount.toPlainString());
        int digits = currency.getDefaultFractionDigits();
        if (amount.stripTrailingZeros().scale() > digits) {
            throw new IllegalArgumentException(amount.toPlainString() + " has more decimals than " + currency.getCurrencyCode() + " allows");
        }
        return amount.setScale(digits, RoundingMode.UNNECESSARY);
    }
}
