#!/usr/bin/env bash
set -xeo pipefail
VERSION=$1

if [[ -z $VERSION ]]; then
    echo "Usage $0 <sdk version>"
    exit 1
fi
sed -i "s@<version>.*</version>@<version>$VERSION</version>@" scripts/fetch_sdk_pom.xml
rm -rf lib/*.jar lib/dependencies.html
scripts/fetch_sdk.sh
cp {scripts/target/site/dependencies.html,scripts/target/dependency/*.jar} lib/

scripts/update_build.properties.py
scripts/update_MANIFEST.MF.py
scripts/update_classpath.py
