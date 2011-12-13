#!/bin/bash

# set path to eclipse folder. If local folder, use '.'; otherwise, use /path/to/eclipse/
eclipsehome="server";

# get path to equinox jar inside $eclipsehome folder
cp=$(find $eclipsehome -name "org.eclipse.equinox.launcher_*.jar" | sort | tail -1);

# debug options
debug="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n"

echo Launching the openHAB runtime in debug mode...
java $debug -Declipse.ignoreApp=true -Dosgi.noShutdown=true -Djetty.port=8000 -Djetty.home=. -Dlogback.configurationFile=logs/logback_debug.xml -Dfelix.fileinstall.dir=addons -Djava.library.path=lib -Djava.security.auth.login.config=./etc/login.conf -Dopenhab.security -jar $cp $* -console

