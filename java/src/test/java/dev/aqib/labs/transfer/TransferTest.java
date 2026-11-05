package dev.aqib.labs.transfer;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

/** The same rules, checked against both versions. If one passes and the other fails, the difference is the lesson. */
class TransferTest {

    private static BigDecimal d(String s) { return new BigDecimal(s); }

    @Test void byHand_sameFieldsAreEqual_evenAtDifferentScale() {
        var a = new TransferByHand("A1", "B1", d("10"), "GBP");
        var b = new TransferByHand("A1", "B1", d("10.00"), "GBP");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test void record_sameFieldsAreEqual_evenAtDifferentScale() {
        var a = new Transfer("A1", "B1", d("10"), "GBP");
        var b = new Transfer("A1", "B1", d("10.00"), "GBP");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(d("10.00"), a.amount(), "the compact constructor stored the normalised amount");
    }

    @Test void aRetryIsRecognisedInASet() {
        var seen = new HashSet<Transfer>();
        seen.add(new Transfer("A1", "B1", d("25.5"), "GBP"));
        assertTrue(seen.contains(new Transfer("A1", "B1", d("25.50"), "GBP")));
        var seenByHand = new HashSet<TransferByHand>();
        seenByHand.add(new TransferByHand("A1", "B1", d("25.5"), "GBP"));
        assertTrue(seenByHand.contains(new TransferByHand("A1", "B1", d("25.50"), "GBP")));
    }

    @Test void directionMatters() {
        assertNotEquals(new Transfer("A1", "B1", d("1"), "GBP"), new Transfer("B1", "A1", d("1"), "GBP"));
        assertNotEquals(new TransferByHand("A1", "B1", d("1"), "GBP"), new TransferByHand("B1", "A1", d("1"), "GBP"));
    }

    @Test void currencyMatters() {
        assertNotEquals(new Transfer("A1", "B1", d("1"), "GBP"), new Transfer("A1", "B1", d("1"), "EUR"));
    }

    @Test void invalidTransfersCannotExist() {
        for (Runnable bad : new Runnable[] {
                () -> new Transfer("A1", "A1", d("1"), "GBP"),        // same account
                () -> new Transfer("A1", "B1", d("0"), "GBP"),        // not positive
                () -> new Transfer("A1", "B1", d("-5"), "GBP"),
                () -> new Transfer("A1", "B1", d("1.005"), "GBP"),    // sub-penny
                () -> new Transfer("A1", "B1", d("100.5"), "JPY"),    // yen has no decimals
                () -> new Transfer(" ", "B1", d("1"), "GBP"),         // blank id
                () -> new Transfer("A1", "B1", d("1"), "XXQ"),        // not a currency
                () -> new TransferByHand("A1", "A1", d("1"), "GBP"),
                () -> new TransferByHand("A1", "B1", d("0"), "GBP"),
                () -> new TransferByHand("A1", "B1", d("1.005"), "GBP") }) {
            assertThrows(IllegalArgumentException.class, bad::run);
        }
        assertThrows(NullPointerException.class, () -> new Transfer(null, "B1", d("1"), "GBP"));
        assertThrows(NullPointerException.class, () -> new TransferByHand("A1", "B1", null, "GBP"));
    }

    @Test void toStringIsReadableInLogs() {
        assertEquals("Transfer[A1 -> B1, GBP 10.00]", new TransferByHand("A1", "B1", d("10"), "GBP").toString());
        assertEquals("Transfer[fromAccountId=A1, toAccountId=B1, amount=10.00, currency=GBP]",
                new Transfer("A1", "B1", d("10"), "GBP").toString());
    }
}
