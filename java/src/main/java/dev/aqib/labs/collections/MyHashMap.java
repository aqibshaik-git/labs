package dev.aqib.labs.collections;

/**
 * Mon 9 Nov drill: your own HashMap, by hand. No java.util collections inside.
 * Lesson: ~/dev/learning/2026-11-09-java-collections-generics/README.md, section 6.
 *
 * Rules (each one has a test):
 *  - Buckets are an array of singly linked nodes; capacity is always a power of two (start at 16).
 *  - Index = spread(hash) & (capacity - 1), where spread(h) = h ^ (h >>> 16), like the JDK.
 *  - put returns the previous value (or null); get returns null for a missing key; null keys are allowed.
 *  - When size exceeds capacity * 0.75, double the capacity and re-place every entry.
 *  - Keys with the same hashCode but not equal must both survive (collisions).
 */
public final class MyHashMap<K, V> {

    public V put(K key, V value) { throw new UnsupportedOperationException("TODO"); }

    public V get(K key) { throw new UnsupportedOperationException("TODO"); }

    public V remove(K key) { throw new UnsupportedOperationException("TODO"); }

    public int size() { throw new UnsupportedOperationException("TODO"); }

    /** Exposed for the tests: the current number of buckets. */
    int capacity() { throw new UnsupportedOperationException("TODO"); }
}
