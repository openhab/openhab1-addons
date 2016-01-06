#!/bin/sh

include="`dirname "$0"`"/openhab.in.sh
if [ -r "$include" ]; then
	. "$include"
else
	echo "Error $include not found or not readable" >&2
	exit 1
fi

setPermRecursive () {
	sprohdir="$1"
	if [ x"${sprohdir}" != x ] && [ x"${sprohdir}" != x/ ]; then
		echo "setting permissions and owner ${USER_AND_GROUP} recursively for ${sprohdir}"
		chown -Rh ${USER_AND_GROUP} "${sprohdir}"
		chmod 755 "${sprohdir}"
		find "${sprohdir}" -type f -exec chmod 644 {} ';'
		find "${sprohdir}" -type d -exec chmod 755 {} ';'
	fi
}

setPerm () {
	spmode=$1
	spfile="$2" # may be a dir
	echo "setting permissions and owner ${USER_AND_GROUP} for ${spfile}"
	chown ${USER_AND_GROUP} "${spfile}"
	chmod $spmode "${spfile}"
}

for ohdir in "${OPENHAB_HOME_DIR}" "${OPENHAB_LOG_DIR}" "${OPENHAB_CONFIGURATIONS_DIR}"; do
	setPermRecursive "${ohdir}"
done

for dir in "${OPENHAB_DIR}/webapps/static"; do
	setPerm 755 "$dir"
done

for file in "${JETTY_CONFDIR}/keystore" "${OPENHAB_CONFIGURATIONS_DIR}/users.cfg"; do
	setPerm 440 "$file"
done

exit 0
