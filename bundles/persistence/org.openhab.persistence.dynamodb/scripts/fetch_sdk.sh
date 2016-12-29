#!/usr/bin/env bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mvn -f $DIR/fetch_sdk_pom.xml clean process-sources project-info-reports:dependencies

echo "Check $DIR/target/site/dependencies.html and $DIR/target/dependency"