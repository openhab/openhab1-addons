#!/bin/bash

# set path to eclipse folder. If local folder, use '.'; otherwise, use /path/to/eclipse/
eclipsehome="server";

# get path to equinox jar inside $eclipsehome folder
cp=$(find $eclipsehome -name "org.eclipse.equinox.launcher_*.jar" | sort | tail -1);

echo Launching the openHAB runtime...
java -Declipse.ignoreApp=true -Dosgi.noShutdown=true -D32 -Djetty.home=. -Dlogback.configurationFile=logs/logback.xml -Dfelix.fileinstall.dir=addons -Djava.library.path=lib -Djava.security.auth.login.config=./etc/login.conf -Dopenhab.securityEnabled -jar $cp $* -console
