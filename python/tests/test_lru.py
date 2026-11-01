import pytest

from labs.lru import LRUCache


def test_get_missing_returns_default():
    c = LRUCache(2)
    assert c.get("x") is None
    assert c.get("x", 0) == 0


def test_evicts_least_recently_used():
    c = LRUCache(2)
    c.put("a", 1)
    c.put("b", 2)
    c.get("a")          # a is now most recent
    c.put("c", 3)       # evicts b
    assert "b" not in c
    assert c.get("a") == 1 and c.get("c") == 3


def test_put_existing_refreshes_and_updates():
    c = LRUCache(2)
    c.put("a", 1)
    c.put("b", 2)
    c.put("a", 10)      # a refreshed
    c.put("c", 3)       # evicts b
    assert c.get("a") == 10 and "b" not in c
    assert len(c) == 2


def test_capacity_must_be_positive():
    with pytest.raises(ValueError):
        LRUCache(0)
