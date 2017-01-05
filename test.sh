#!/usr/bin/env bash

wsk action invoke fuseki -b -r \
    -p query "`base64 examples/q-ds-1.rq`" \
    -p model "`base64 examples/ds-dft.ttl`" \
    -p lang ttl

if [ $? == 0 ]; then
    echo "PASS"
    exit 0
else
    echo "FAIL"
    exit 1
fi
