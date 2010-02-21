#!/bin/sh
#
# Script to run Pax Runner, which starts OSGi frameworks with applications.
#
#

SCRIPTS=`readlink $0`
if [ "${SCRIPTS}" != "" ]
then
  SCRIPTS=`dirname $SCRIPTS`
else
  SCRIPTS=`dirname $0`
fi

java $JAVA_OPTS -cp .:$SCRIPTS:$SCRIPTS/pax-runner-1.4.0.jar org.ops4j.pax.runner.Run "$@"
