#!/bin/bash

if [ "$#" != "1" ]
then
    echo "Usage: $0 <VeraIpAddress>"
    exit 1
else
    MIOS_IP=$1; export MIOS_IP
    MIOS_OUT=user_data.xml; export MIOS_OUT
fi

echo "Loading MiOS Unit Metadata from ${MIOS_IP}..."
curl --max-time 15 \
     --output ${MIOS_OUT} \
     --silent "http://${MIOS_IP}:49451/data_request?id=user_data&output_format=xml"

if [ "$?" == "0" ]
then
    echo "Metadata Loaded into ${MIOS_OUT}!"
else
    echo "Failed to load, Check IP Address supplied."
fi

