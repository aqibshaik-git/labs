package dev.aqib.labs.transfer;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Drill, part 2: the same value object as a record.
 *
 * The record gives you, for free: final class, private final fields, the canonical constructor,
 * accessors, and equals/hashCode/toString over all components. What it can't know is your
 * invariants, so they go in the compact constructor, which runs before the fields are assigned.
 * Reassigning a parameter there (amount = ...) changes what gets stored: that's how you normalise.
 *
 * Two things to notice against TransferByHand:
 *  - equals here is component-wise with each component's own equals. If amount weren't normalised,
 *    10 and 10.00 would be different transfers (BigDecimal.equals compares scale). Normalising in the
 *    compact constructor is what keeps the generated equals honest.
 *  - currency is kept as a String component (records expose components as-is), but validated
 *    through java.util.Currency, so "GBX" or "gbp" can't get in.
 */
public record Transfer(String fromAccountId, String toAccountId, BigDecimal amount, String currency) {

    public Transfer {
        TransferByHand.requireText(fromAccountId, "fromAccountId");
        TransferByHand.requireText(toAccountId, "toAccountId");
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("A transfer needs two different accounts, got " + fromAccountId + " twice");
        }
        Currency c = Currency.getInstance(Objects.requireNonNull(currency, "currency"));
        amount = TransferByHand.normalise(Objects.requireNonNull(amount, "amount"), c);
    }
}
