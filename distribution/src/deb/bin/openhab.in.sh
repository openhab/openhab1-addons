#!/bin/sh

OPENHAB_CONF_DIR="/etc/openhab"
OPENHAB_DIR="/usr/share/openhab"
OPENHAB_LOG_DIR="/var/log/openhab"
OPENHAB_WORKSPACE_DIR="/var/lib/openhab/workspace"
if [ -z "$USER_AND_GROUP" ]; then
	USER_AND_GROUP="openhab:openhab"
fi
