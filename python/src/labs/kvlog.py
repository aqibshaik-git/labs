"""Sun 15 Nov drill: an append-only key-value log with an in-memory index (the Bitcask design). YOU write it.

Lesson: ~/dev/learning/2026-11-15-storage-engines-indexes/README.md, section 6.

The file is a sequence of records, only ever appended:
    [4-byte key length][4-byte value length][key bytes][value bytes]      (lengths big-endian, struct ">II")
A delete appends a TOMBSTONE: value length 0xFFFFFFFF and no value bytes.
The index is a dict: key -> byte offset of that key's latest record.

Rules (tests/test_kvlog.py):
  - put(key: str, value: bytes) appends and updates the index. get(key) -> bytes | None with ONE seek + read.
  - delete(key) appends a tombstone and drops the key from the index. get then returns None.
  - Reopening KVLog(path) on an existing file rebuilds the index by scanning the file once.
  - compact() rewrites the file with only the live records (latest value per key, no tombstones),
    swaps it in atomically (write a temp file, then os.replace), and rebuilds the index.
    After compaction the file is smaller and every get still returns the same values.
  - close() closes the file. Use it as a context manager: `with KVLog(path) as db: ...`
"""
import struct

HEADER = struct.Struct(">II")
TOMBSTONE = 0xFFFFFFFF


class KVLog:
    def __init__(self, path):
        raise NotImplementedError

    def put(self, key: str, value: bytes) -> None:
        raise NotImplementedError

    def get(self, key: str) -> bytes | None:
        raise NotImplementedError

    def delete(self, key: str) -> None:
        raise NotImplementedError

    def compact(self) -> None:
        raise NotImplementedError

    def close(self) -> None:
        raise NotImplementedError

    def __enter__(self):
        return self

    def __exit__(self, *exc):
        self.close()
