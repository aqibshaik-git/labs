import os
import tempfile

from labs.kvlog import KVLog


def _path():
    d = tempfile.mkdtemp()
    return os.path.join(d, "data.log")


def test_put_get_overwrite():
    p = _path()
    with KVLog(p) as db:
        db.put("acct:1", b"100")
        db.put("acct:2", b"250")
        db.put("acct:1", b"80")
        assert db.get("acct:1") == b"80"
        assert db.get("acct:2") == b"250"
        assert db.get("acct:3") is None


def test_delete_writes_a_tombstone():
    p = _path()
    with KVLog(p) as db:
        db.put("k", b"v")
        size_before = os.path.getsize(p)
        db.delete("k")
        assert db.get("k") is None
        assert os.path.getsize(p) > size_before       # appended, never edited in place


def test_reopen_rebuilds_the_index():
    p = _path()
    with KVLog(p) as db:
        db.put("a", b"1"); db.put("b", b"2"); db.put("a", b"3"); db.delete("b")
    with KVLog(p) as db:
        assert db.get("a") == b"3"
        assert db.get("b") is None


def test_compact_shrinks_and_keeps_live_values():
    p = _path()
    with KVLog(p) as db:
        for i in range(200):
            db.put("hot", str(i).encode())
        db.put("cold", b"keep me")
        db.put("gone", b"x"); db.delete("gone")
        before = os.path.getsize(p)
        db.compact()
        assert os.path.getsize(p) < before / 10
        assert db.get("hot") == b"199"
        assert db.get("cold") == b"keep me"
        assert db.get("gone") is None
    with KVLog(p) as db:
        assert db.get("hot") == b"199"


def test_empty_value_is_not_a_tombstone():
    p = _path()
    with KVLog(p) as db:
        db.put("e", b"")
    with KVLog(p) as db:
        assert db.get("e") == b""
