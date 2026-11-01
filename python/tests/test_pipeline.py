import inspect
import itertools
import json
from decimal import Decimal

from labs.pipeline import only, parse, read_lines, spend_by_account, top_n

LINES = [
    '{"type": "debit", "account": "A", "amount": "10.00"}',
    '{"type": "credit", "account": "A", "amount": "99.00"}',
    "",
    "not json at all",
    '{"type": "debit", "account": "B", "amount": "2.50"}',
    '{"type": "debit", "account": "A", "amount": "5.25"}',
]


def test_read_lines_is_a_generator(tmp_path):
    p = tmp_path / "events.jsonl"
    p.write_text("\n".join(LINES))
    lines = read_lines(p)
    assert inspect.isgenerator(lines), "read_lines must yield, not return a list"
    assert next(lines) == LINES[0]


def test_parse_skips_blank_and_malformed_lines():
    events = list(parse(LINES))
    assert len(events) == 4
    assert events[0] == json.loads(LINES[0])


def test_parse_and_only_are_lazy():
    def endless():
        while True:
            yield '{"type": "debit", "account": "Z", "amount": "1.00"}'

    first_three = list(itertools.islice(only(parse(endless()), "debit"), 3))
    assert len(first_three) == 3  # would hang forever if parse or only built a list


def test_only_filters_by_type():
    assert [e["account"] for e in only(parse(LINES), "debit")] == ["A", "B", "A"]


def test_spend_by_account_uses_decimal():
    totals = spend_by_account(only(parse(LINES), "debit"))
    assert totals == {"A": Decimal("15.25"), "B": Decimal("2.50")}


def test_top_n():
    totals = {"A": Decimal("15.25"), "B": Decimal("2.50"), "C": Decimal("99")}
    assert top_n(totals, 2) == [("C", Decimal("99")), ("A", Decimal("15.25"))]
