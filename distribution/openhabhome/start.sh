#!/bin/sh

cd `dirname $0`

# set path to eclipse folder. If local folder, use '.'; otherwise, use /path/to/eclipse/
eclipsehome="server";

# set ports for HTTP(S) server
HTTP_PORT=8080
HTTPS_PORT=8443
DEBUG_PORT=8001
CONSOLE_PORT=

if [ -z "$OH_HTTP_PORT" ]; then OH_HTTP_PORT=$HTTP_PORT; fi
if [ -z "$OH_HTTPS_PORT" ]; then OH_HTTPS_PORT=$HTTPS_PORT; fi
if [ -z "$OH_DEBUG_PORT" ]; then OH_DEBUG_PORT=$DEBUG_PORT; fi
if [ -z "$OH_CONSOLE_PORT" ]; then OH_CONSOLE_PORT=$CONSOLE_PORT; fi

# get path to equinox jar inside $eclipsehome folder
cp=$(find $eclipsehome -name "org.eclipse.equinox.launcher_*.jar" | sort | tail -1);

# debug options
debug_opts="-agentlib:jdwp=transport=dt_socket,address=$OH_DEBUG_PORT,server=y,suspend=n"

echo Launching the openHAB runtime in debug mode...
java $debug_opts -Dosgi.clean=true -Declipse.ignoreApp=true -Dosgi.noShutdown=true -Djetty.port=$OH_HTTP_PORT -Djetty.port.ssl=$OH_HTTPS_PORT -Djetty.home=. -Dlogback.configurationFile=configurations/logback_debug.xml -Dfelix.fileinstall.dir=addons -Djava.library.path=lib -Dorg.quartz.properties=./etc/quartz.properties -Djava.security.auth.login.config=./etc/login.conf -Dequinox.ds.block_timeout=240000 -Dequinox.scr.waitTimeOnBlock=60000 -Dfelix.fileinstall.active.level=4 -Djava.awt.headless=true -jar $cp $* -console $OH_CONSOLE_PORT
