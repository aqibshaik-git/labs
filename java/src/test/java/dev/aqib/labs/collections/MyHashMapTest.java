package dev.aqib.labs.collections;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyHashMapTest {
    /** Every instance has the same hashCode, so all of them collide. */
    record Clash(String id) { @Override public int hashCode() { return 7; } }

    @Test void putGetAndReplace() {
        var m = new MyHashMap<String, Integer>();
        assertEquals(null, m.put("a", 1));
        assertEquals(1, m.put("a", 2));
        assertEquals(2, m.get("a"));
        assertEquals(null, m.get("missing"));
        assertEquals(1, m.size());
    }

    @Test void collisionsKeepBothKeys() {
        var m = new MyHashMap<Clash, String>();
        m.put(new Clash("x"), "X"); m.put(new Clash("y"), "Y");
        assertEquals("X", m.get(new Clash("x")));
        assertEquals("Y", m.get(new Clash("y")));
        assertEquals(2, m.size());
    }

    @Test void nullKeyWorks() {
        var m = new MyHashMap<String, String>();
        m.put(null, "n");
        assertEquals("n", m.get(null));
    }

    @Test void resizesAtThreeQuartersAndKeepsEverything() {
        var m = new MyHashMap<Integer, Integer>();
        assertEquals(16, m.capacity());
        for (int i = 0; i < 12; i++) m.put(i, i * 10);
        assertEquals(16, m.capacity());
        m.put(12, 120);
        assertEquals(32, m.capacity());
        for (int i = 0; i < 1000; i++) m.put(i, i * 10);
        for (int i = 0; i < 1000; i++) assertEquals(i * 10, m.get(i));
        assertEquals(1000, m.size());
        assertEquals(2048, m.capacity());
    }

    @Test void removeUnlinksFromTheChain() {
        var m = new MyHashMap<Clash, String>();
        m.put(new Clash("a"), "A"); m.put(new Clash("b"), "B"); m.put(new Clash("c"), "C");
        assertEquals("B", m.remove(new Clash("b")));
        assertEquals(null, m.get(new Clash("b")));
        assertEquals("A", m.get(new Clash("a")));
        assertEquals("C", m.get(new Clash("c")));
        assertEquals(2, m.size());
        assertEquals(null, m.remove(new Clash("zz")));
    }
}
