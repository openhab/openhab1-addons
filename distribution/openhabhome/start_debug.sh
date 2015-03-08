#!/bin/sh

cd `dirname $0`

# set path to eclipse folder. If local folder, use '.'; otherwise, use /path/to/eclipse/
eclipsehome="server";

# set ports for HTTP(S) server
HTTP_PORT=8080
HTTPS_PORT=8443

# get path to equinox jar inside $eclipsehome folder
cp=$(find $eclipsehome -name "org.eclipse.equinox.launcher_*.jar" | sort | tail -1);

# debug options
debug_opts="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n"

echo Launching the openHAB runtime in debug mode...
java $debug_opts \
	-Dosgi.clean=true \
	-Declipse.ignoreApp=true \
	-Dosgi.noShutdown=true \
	-Djetty.port=$HTTP_PORT \
	-Djetty.port.ssl=$HTTPS_PORT \
	-Djetty.home=. \
	-Dlogback.configurationFile=configurations/logback_debug.xml \
	-Dfelix.fileinstall.dir=addons \
	-Dfelix.fileinstall.filter=.*\\.jar \
	-Djava.library.path=lib \
	-Dorg.quartz.properties=./etc/quartz.properties \
	-Djava.security.auth.login.config=./etc/login.conf \
	-Dequinox.ds.block_timeout=240000 \
	-Dequinox.scr.waitTimeOnBlock=60000 \
	-Dfelix.fileinstall.active.level=4 \
	-Djava.awt.headless=true \
	-jar $cp $* \
	-console
