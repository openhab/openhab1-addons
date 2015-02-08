#!/bin/sh

include="`dirname "$0"`"/openhab.in.sh
if [ -r "$include" ]; then
	. "$include"
else
	echo "Error $include not found or not readable" >&2
	exit 1
fi

for ohdir in "${OPENHAB_WORKSPACE_DIR}" "${OPENHAB_LOG_DIR}"; do
	if [ x"${ohdir}" != x ] && [ x"${ohdir}" != x/ ]; then
		echo "setting permissions and owner ${USER_AND_GROUP} recursively for ${ohdir}"
		chown -Rh ${USER_AND_GROUP} "${ohdir}"
		chmod 755 "${ohdir}"
		find "${ohdir}" -type f -exec chmod 644 {} ';'
		find "${ohdir}" -type d -exec chmod 755 {} ';'
	fi
done
echo "setting permissions and owner ${USER_AND_GROUP} for ${OPENHAB_DIR}/webapps/static"
chown ${USER_AND_GROUP} "${OPENHAB_DIR}/webapps/static"
chmod 755 "${OPENHAB_DIR}/webapps/static"
