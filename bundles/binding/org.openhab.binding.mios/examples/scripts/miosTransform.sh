#!/bin/bash

if [ "$#" != "1" ]
then
    echo "Usage:   $0 <openHAB-MiOSUnitName>"
    echo "Example: $0 house"
    exit 1
else
    MIOS_UNIT="${1}"; export MIOS_UNIT
    MIOS_IN=user_data.xml; export MIOS_OUT
    MIOS_ITEM_FILE="${MIOS_UNIT}.items"; export MIOS_ITEM_FILE
fi

echo "INFO: Transforming MiOS Unit Metadata from ${MIOS_IN}..."
xsltproc --stringparam MIOS_UNIT "${MIOS_UNIT}" --output "${MIOS_ITEM_FILE}" miosTransform.xslt "${MIOS_IN}"

if [ "$?" == "0" ]
then
    echo "INFO: Metadata Transformed into openHAB Item file ${MIOS_ITEM_FILE}!"
else
    echo "ERROR: Failed to Transform, Check for bogus XML in ${MIOS_IN}."
    exit 1
fi

echo "INFO: Processing for any Duplicate Item names"
lastItemName=""
grep -v ^$ "${MIOS_ITEM_FILE}" | grep -v "^/" | sort -k 2 | while read -r line; do
    read -r itemType itemName _ <<< "$line"

    if [ "$itemName" = "$lastItemName" ]; then
        echo "  $line"
    else
        lastItemName="$itemName"
    fi
done

if [ "${lastItemName}" == "" ];
then
    echo "INFO: Your generated openHAB Item file (${MIOS_ITEM_FILE}) is good to go!"
else
    echo "WARN: The duplicate openHAB Item names above must be manually corrected in the generated openHAB Item file (${MIOS_ITEM_FILE})"
fi


echo "INFO: Remember to configure your MiOS Unit Name ('${1}') openhab.cfg (openHAB 1.x), or mios.cfg (openHAB 2.x)"
