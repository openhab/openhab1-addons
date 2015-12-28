#!/bin/sh

javac -cp "/opt/openhab/server/plugins/*:/root/openhab.copy/bundles/io/org.openhab.io.rest.lib/lib/*:./" Test.java
java -cp "/opt/openhab/server/plugins/*:/root/openhab.copy/bundles/io/org.openhab.io.rest.lib/lib/*:./" Test $*
find . -name '*.class' -exec rm -f \{\} \;
