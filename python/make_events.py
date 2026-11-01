"""Writes N fake ledger events to events.jsonl so you can watch the pipeline's memory stay flat.

    uv run python make_events.py 2000000
    /usr/bin/time -l uv run python -c "from labs.pipeline import *; print(top_n(spend_by_account(only(parse(read_lines('events.jsonl')),'debit')),5))"
"""
import json
import random
import sys

n = int(sys.argv[1]) if len(sys.argv) > 1 else 1_000_000
with open("events.jsonl", "w") as f:
    for i in range(n):
        f.write(json.dumps({"type": random.choice(["debit", "credit"]), "account": f"ACC{random.randint(1, 50_000):06d}",
                            "amount": f"{random.randint(1, 50_000) / 100:.2f}"}) + "\n")
        if i % 100_000 == 0:
            f.write("this line is broken on purpose\n")
print(f"wrote {n:,} events to events.jsonl")
