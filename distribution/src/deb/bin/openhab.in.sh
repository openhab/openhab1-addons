#!/bin/sh

OPENHAB_CONF_DIR="/etc/openhab"
OPENHAB_CONFIGURATIONS_DIR="${OPENHAB_CONF_DIR}/configurations"
OPENHAB_DIR="/usr/share/openhab"
OPENHAB_LOG_DIR="/var/log/openhab"
OPENHAB_HOME_DIR="/var/lib/openhab"
OPENHAB_USER_DATA_DIR="${OPENHAB_HOME_DIR}"
OPENHAB_WORKSPACE_DIR="${OPENHAB_HOME_DIR}/workspace"
JETTY_CONFDIR="${OPENHAB_CONF_DIR}/jetty/etc"
if [ -z "$USER_AND_GROUP" ]; then
	USER_AND_GROUP="openhab:openhab"
fi
