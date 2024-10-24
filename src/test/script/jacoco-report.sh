#! /bin/bash

POM="$(dirname "$0")"/../../../pom.xml

# If we're not in the root of the Maven project, then find it using
# this script's name:
if ! [ -r pom.xml ]; then
    cd "$(dirname "$0")"/../../../
fi

if [ "$#" -gt 0 ]; then
    args="$*"
else
    args=""
fi

mvn clean
mvn -Djacoco.skip=false verify -Dmaven.test.failure.ignore
mvn -Djacoco.skip=false jacoco:restore-instrumented-classes
mvn -Djacoco.skip=false jacoco:report
firefox target/site/jacoco/index.html
