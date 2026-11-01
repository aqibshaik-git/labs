"""Tue 3 Nov lab: a lazy pipeline over ledger events (one JSON object per line).

Every stage except the last two must be a generator: it pulls one item at a time from
the stage before it, so a 5 GB file uses the same memory as a 5 KB one.

    read_lines(path) -> parse(lines) -> only(events, "debit") -> spend_by_account(...) -> top_n(...)
"""
from collections.abc import Iterable, Iterator
from decimal import Decimal


def read_lines(path) -> Iterator[str]:
    """Yield each line of the file without its trailing newline. Never read the whole file."""
    raise NotImplementedError("TODO")


def parse(lines: Iterable[str]) -> Iterator[dict]:
    """Yield one dict per JSON line. Skip blank lines and lines that aren't valid JSON."""
    raise NotImplementedError("TODO")


def only(events: Iterable[dict], event_type: str) -> Iterator[dict]:
    """Yield only events whose "type" equals event_type. Must stay lazy."""
    raise NotImplementedError("TODO")


def spend_by_account(events: Iterable[dict]) -> dict[str, Decimal]:
    """Total "amount" per "account". Amounts are strings in the file: use Decimal, never float."""
    raise NotImplementedError("TODO")


def top_n(totals: dict[str, Decimal], n: int) -> list[tuple[str, Decimal]]:
    """The n accounts with the biggest totals, biggest first. Hint: heapq."""
    raise NotImplementedError("TODO")
