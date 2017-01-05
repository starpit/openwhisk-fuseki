# openwhisk-fuseki

A simple [OpenWhisk](https://github.com/openwhisk/openwhisk) wrapper
around [Apache
Fuseki](https://jena.apache.org/documentation/fuseki2/).

## Prerequisites

 1. [install the `wsk` CLI](https://bluemix.net/openwhisk/cli)


## Usage

```
# ./init.sh
...
ok: created action fuseki

# ./test.sh
PASS
```


## Details

If for some reason you wish to use a different version of Google's
GSON library, or of the Apache Fuseki library, update the values in
config.sh.