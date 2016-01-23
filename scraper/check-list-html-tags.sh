#!/usr/bin/env sh

grep --recursive --only-matching '</[^>]*>' spec/ | sed -e 's/[^:]*://' | sort | uniq -c | sort -nr
