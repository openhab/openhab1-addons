#!/bin/bash

if [ "$#" != "1" ]
then
    echo "Usage: $0 <openHAB-MiOSUnitName>"
    exit 1
else
    MIOS_UNIT="${1}"; export MIOS_UNIT
    MIOS_IN=user_data.xml; export MIOS_OUT
    MIOS_ITEM_FILE="${MIOS_UNIT}.items"; export MIOS_ITEM_FILE
fi

echo "Transforming MiOS Unit Metadata from ${MIOS_IN}..."
xsltproc --stringparam MIOS_UNIT "${MIOS_UNIT}" --output "${MIOS_ITEM_FILE}" miosTransform.xslt "${MIOS_IN}"

if [ "$?" == "0" ]
then
    echo "Metadata Transformed into ${MIOS_ITEM_FILE}!"
else
    echo "Failed to Transform, Check for bogus XML in ${MIOS_IN}."
    exit 1
fi

echo "Duplicate Item names requiring manual fixes:"
lastItemName=""
grep -v ^$ "${MIOS_ITEM_FILE}" | grep -v "^/" | sort -k 2 | while read -r line; do
    read -r itemType itemName _ <<< "$line"

    if [ "$itemName" = "$lastItemName" ]; then
        echo "  $line"
    else
        lastItemName="$itemName"
    fi
done

