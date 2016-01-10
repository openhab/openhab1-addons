#!/bin/bash

# upload fake packages: the packages must be available in the current working directory
# the packages can be downloaded from bintray: https://bintray.com/openhab/apt-repo/fake/fake-0.1/view#files/pool/main/fake

DRY_RUN=false
#BASE_URL="https://api.bintray.com/content/theoweiss"
#BINTRAY_REPO="openhab-test"
#BINTRAY_PACKAGE="fake"

BASE_URL="https://api.bintray.com/content/openhab"
BINTRAY_REPO="apt-repo"
BINTRAY_PACKAGE="fake"

BINTRAY_VERSION="fake-0.2"

showUsageAndExit () {
	echo $@
	echo "usage: $0 <username> <apikey> <gpgpasswd> <openhab version>"
	echo "Uploads the fake package for the given openHAB version."
	echo "The package will be placed in pool/main/fake/${openhabversion}"
	exit 2
}

upload(){
	debfile=$1
	arch=$2
	openhabversion="$3"
	echo "uploading arch $arch, version $openhabversion ${debfile}"
	if [ $DRY_RUN = "false" ]; then
		msg=`curl  -H "X-GPG-PASSPHRASE: ${gpgpasswd}" -T ${debfile} -u${username}:${apikey} "${BASE_URL}/${BINTRAY_REPO}/${BINTRAY_PACKAGE}/${BINTRAY_VERSION}/pool/main/fake/${openhabversion}/${debfile};deb_distribution=${openhabversion};deb_component=main;deb_architecture=${arch};publish=1" 2>/dev/null`
		echo $msg | awk -F ":" '{ if ( $2 == "\"success\"}" )  exit 0 ; else { print $0 ; exit 1 }} '
		if [ $? -eq 0 ]; then
			echo "ok"
		else
			echo "failed"
			exit 1
		fi
	else
		echo "${BASE_URL}/${BINTRAY_REPO}/${BINTRAY_PACKAGE}/${BINTRAY_VERSION}/pool/main/fake/${openhabversion}/${debfile};deb_distribution=${openhabversion};deb_component=main;deb_architecture=${arch};publish=1"
	fi
}

sign(){
	echo "requesting signing..."
	if [ $DRY_RUN = "false" ]; then
		curl -X POST -H "X-GPG-PASSPHRASE: ${gpgpasswd}" -u${username}:${apikey} https://api.bintray.com/calc_metadata/openhab/${BINTRAY_REPO}
	else
		echo "X-GPG-PASSPHRASE: ${gpgpasswd}" -u${username}:${apikey} https://api.bintray.com/calc_metadata/openhab/${BINTRAY_REPO}
	fi
}

if [ $# -eq 4 ]; then
	username="$1"
	apikey="$2"
	gpgpasswd="$3"
	openhabversion="$4"
else
	showUsageAndExit
fi


upload trigger-bintray-to-make-all-archs-fake_1.0_armhf.deb armhf ${openhabversion}
upload trigger-bintray-to-make-all-archs-fake_1.0_armel.deb armel ${openhabversion}
upload trigger-bintray-to-make-all-archs-fake_1.0_mips.deb mips ${openhabversion}
upload trigger-bintray-to-make-all-archs-fake_1.0_arm64.deb arm64 ${openhabversion}

sign
