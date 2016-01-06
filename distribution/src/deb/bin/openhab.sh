#!/bin/sh

# This script is meant to be used with the systemd startup.
# The script relies on few environment variables to determine startup
# behavior, those variables are:
#
# optional:
#   OPENHAB_JAVA   -- The path of the java runtime executable. The default
#                     is /usr/bin/java.
#   USER_AND_GROUP -- The user and group the openHAB is running as. The 
#                     default is "openhab:openhab". In systemd environments
#                     openhab.service must be adapted, when changing this.
#   JAVA_ARGS      -- Additional arguments to the JVM.
#   OPENHAB_ARGS   -- Additional arguments to the openHAB runtime.
#   DEBUG          -- Start openHAB in debugging mode. The default is no, set 
#                     it to yes for debugging purposes.
#   HTTP_PORT      -- The http port of the web ui. The default is 8080.
#   HTTPS_PORT     -- The https port of the web ui. The default is 8443.
#   TELNET_PORT    -- OSGi console port
# Normally systemd provides this variable using the /etc/default/openhab file.

include="`dirname "$0"`"/openhab.in.sh
if [ -r "$include" ]; then
	. "$include"
else
	echo "Error $include not found or not readable" >&2
	exit 1
fi

if [ -z $HTTP_PORT ]; then
	HTTP_PORT=8080
fi
if [ -z $HTTPS_PORT ]; then
	HTTPS_PORT=8443
fi
if [ -z $TELNET_PORT ]; then
	TELNET_PORT=5555
fi

JAVA="/usr/bin/java"
SETOWNER="no"

LAUNCHER=`ls ${OPENHAB_DIR}/server/plugins/org.eclipse.equinox.launcher_*.jar`

# Exit if the package is not installed
if [ ! -r "$LAUNCHER" ]; then
    echo "launcher jar is missing" >&2
    exit 5
fi

if [ x"${OPENHAB_JAVA}" != x ]; then
    JAVA="${OPENHAB_JAVA}"
fi

if [ ! -x "${JAVA}" ]; then
	echo "Error: no java executable found at ${JAVA}" >&2
	exit 2
fi

if [ x"${USER_AND_GROUP}" != x ]; then

    USER=`echo ${USER_AND_GROUP} | cut -d ":" -f 1`
    GROUP=`echo ${USER_AND_GROUP} | cut -d ":" -f 2`

    #Check whether the specified user exists
    if ! getent passwd "${USER}" > /dev/null 2>&1; then
        echo "runtime user doesn't exists" >&2
        exit 2
    fi

    #Check whether the specified group exists
    if ! getent group "${GROUP}" > /dev/null 2>&1; then
        echo "runtime group doesn't exists" >&2
        exit 2
    fi
fi


DEBUG_ARGS="-Xdebug \
  -Xnoagent \
  -Djava.compiler=NONE \
  -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n \
  -Dlogback.configurationFile=${OPENHAB_CONF_DIR}/logback_debug.xml"

JAVA_ARGS_DEFAULT="-Dosgi.clean=true \
 -Declipse.ignoreApp=true \
 -Dosgi.noShutdown=true \
 -Djetty.port=${HTTP_PORT} \
 -Dopenhab.configfile="${OPENHAB_CONF_DIR}/configurations/openhab.cfg" \
 -Dopenhab.configdir="${OPENHAB_CONF_DIR}/configurations" \
 -Dopenhab.logdir="${OPENHAB_LOG_DIR}" \
 -Dsmarthome.userdata="${OPENHAB_USER_DATA_DIR}"
 -Djetty.home="${OPENHAB_DIR}" \
 -Djetty.port.ssl=${HTTPS_PORT} \
 -Djetty.config="${OPENHAB_CONF_DIR}/jetty" \
 -Djetty.logs="${OPENHAB_LOG_DIR}" \
 -Djetty.rundir="${OPENHAB_DIR}" \
 -Dfelix.fileinstall.dir="${OPENHAB_DIR}/addons" \
 -Dfelix.fileinstall.filter=.*\\.jar \
 -Djava.library.path="${OPENHAB_DIR}/lib" \
 -Djava.security.auth.login.config="${OPENHAB_CONF_DIR}/login.conf" \
 -Dorg.quartz.properties="${OPENHAB_CONF_DIR}/quartz.properties" \
 -Dequinox.ds.block_timeout=240000 \
 -Dequinox.scr.waitTimeOnBlock=60000 \
 -Dfelix.fileinstall.active.level=4 \
 -Djava.awt.headless=true \
 -jar ${LAUNCHER} \
 -configuration ${OPENHAB_WORKSPACE_DIR} \
 -data ${OPENHAB_WORKSPACE_DIR} \
 -console ${TELNET_PORT}"


if [ x"${JAVA_ARGS}" != x ]; then
    JAVA_ARGS_RUN="${JAVA_ARGS} ${JAVA_ARGS_DEFAULT}"
else 
    JAVA_ARGS_RUN="${JAVA_ARGS_DEFAULT}"
fi

if [ x"${OPENHAB_ARGS}" != x ]; then
    JAVA_ARGS_RUN="${JAVA_ARGS_RUN} ${OPENHAB_ARGS}"
fi

if echo ${DEBUG} | grep -qi "^yes$" ; then
    JAVA_ARGS_RUN="${DEBUG_ARGS} ${JAVA_ARGS_RUN}"
else
    JAVA_ARGS_RUN="-Dlogback.configurationFile=${OPENHAB_CONF_DIR}/logback.xml ${JAVA_ARGS_RUN}"
fi

$JAVA $JAVA_ARGS_RUN
