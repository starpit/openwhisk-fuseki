#!/usr/bin/env bash

. ./config.sh

if [ ! -d deps ]; then

    FUSEKI=apache-jena-fuseki-${FUSEKI_VERSION}

    mkdir deps \
	&& cd deps \
	&& curl -O http://repo1.maven.org/maven2/com/google/code/gson/gson/${GSON_VERSION}/gson-${GSON_VERSION}.jar \
	&& curl -O http://mirror.symnds.com/software/Apache/jena/binaries/${FUSEKI}.tar.gz \
	&& tar zxf apache-jena-fuseki-${FUSEKI_VERSION}.tar.gz apache-jena-fuseki-${FUSEKI_VERSION}/fuseki-server.jar \
	&& rm ${FUSEKI}.tar.gz \
	&& mv ${FUSEKI}/fuseki-server.jar . \
	&& rmdir ${FUSEKI} \
	&& cd ..

    if [ $? == 1 ]; then
	exit 1
    fi
fi

javac FusekiWhisk.java -cp deps/fuseki-server.jar:deps/gson-2.8.0.jar -Xlint:deprecation \
    && echo -n "." \
    && cp deps/fuseki-server.jar FusekiWhisk.jar \
    && echo -n "." \
    && jar uf FusekiWhisk.jar *.class \
    && echo "."

if [ $? == 1 ]; then
    exit 1
fi

wsk action delete fuseki 2>&1 | grep -v "resource does not exist"
wsk action create fuseki FusekiWhisk.jar --main FusekiWhisk --memory 512