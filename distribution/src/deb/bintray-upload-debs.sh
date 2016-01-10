#!/bin/bash

# bintray repo setup for a new version:
# - use the web interface to add the new versions: e.g. 1.7.0.RC2
# - build openHAB: mvn clean package
# - cd into distribution/target/apt-repo
# - call this script to upload the files:
#     bintray-upload-debs.sh theoweiss 9999999999999999999999999 gpgsecret 1.6.2 stable
#     bintray-upload-debs.sh theoweiss 9999999999999999999999999 gpgsecret 1.7.0.RC2 testing
# - use the web interface to publish the files

showUsageAndExit () {
	echo $@
	echo "usage: $0 <username> <apikey> <gpgpasswd> <version> <distribution>"
	echo "       username: bintray username"
	echo "       apikey: bintray apikey"
	echo "       gpgpasswd: password of the gpg key"
	echo "       version: openhab version"
	echo "       distribution: stable (release), testing (release candidate) or unstable (snapshot)"
	exit 2
}

if [ $# -eq 5 ]; then
	username="$1"
	apikey="$2"
	gpgpasswd="$3"
	version="$4"
	distribution="$5"
	case "$distribution" in
		stable|testing|unstable) : ;;
		*) showUsageAndExit ;;
	esac
else
	showUsageAndExit
fi

DRY_RUN=false
BASE_URL="https://api.bintray.com/content/openhab"
BINTRAY_REPO="apt-repo"
BINTRAY_PACKAGE="openhab"
BINTRAY_VERSION="${version}"
for debfile in *.deb; do
	ls ${debfile}
	if [ $DRY_RUN = "false" ]; then
		msg=`curl -H "X-GPG-PASSPHRASE: ${gpgpasswd}" -T ${debfile} -u${username}:${apikey} "${BASE_URL}/${BINTRAY_REPO}/${BINTRAY_PACKAGE}/${BINTRAY_VERSION}/pool/main/${version}/${debfile};deb_distribution=${distribution};deb_distribution=${version};deb_component=main;deb_architecture=all;publish=1" 2>/dev/null`
		echo $msg | awk -F ":" '{ if ( $2 == "\"success\"}" )  exit 0 ; else { print $0 ; exit 1 }} '
		if [ $? -eq 0 ]; then
			echo "ok"
		else
			echo "failed"
			exit 1
		fi
	else
		echo "${BASE_URL}/${BINTRAY_REPO}/${BINTRAY_PACKAGE}/${BINTRAY_VERSION}/pool/main/${version}/${debfile};deb_distribution=${distribution};deb_distribution=${version};deb_component=main;deb_architecture=all;publish=1"
	fi
done
if [ $DRY_RUN = "false" ]; then
	curl -X POST -H "X-GPG-PASSPHRASE: ${gpgpasswd}" -u${username}:${apikey} https://api.bintray.com/calc_metadata/openhab/${BINTRAY_REPO}
else
	echo "X-GPG-PASSPHRASE: ${gpgpasswd}" -u${username}:${apikey} https://api.bintray.com/calc_metadata/openhab/${BINTRAY_REPO}
fi
