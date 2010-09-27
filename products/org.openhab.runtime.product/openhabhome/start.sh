#!/bin/bash

# set path to eclipse folder. If local folder, use '.'; otherwise, use /path/to/eclipse/
eclipsehome=".";

# get path to equinox jar inside $eclipsehome folder
cp=$(find $eclipsehome -name "org.eclipse.equinox.launcher_*.jar" | sort | tail -1);

echo $*
java -jar $cp $*

