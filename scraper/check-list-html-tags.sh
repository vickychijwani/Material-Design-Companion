grep --recursive --only-matching '</[^>]*>' spec/ | sed -e 's/[^:]*://' | sort | uniq
