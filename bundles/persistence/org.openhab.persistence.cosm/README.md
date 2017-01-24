Documentation of the Cosm Persistence Service

## Introduction

This service allows you to feed item data to Cosm web site (see http://cosm.com for more details)

## Features

This persistence service supports only writing information to the Cosm web site.
You need a Cosm API key and data feed to put data to. Each item being persisted represens a separate datastream.

## Installation

For installation of this persistence package please follow the same steps as if you would [install a binding](Bindings).

Additionally, place a persistence file called cosm.persist in the `${openhab.home}/configuration/persistence` folder.

## Configuration

This persistence service can be configured in the "Cosm Persistence Service" section in `openhab.cfg`.
You need to specify your data feed url in the form

    cosm:url=http://api.cosm.com/v2/feeds/XXXXX/datastreams/

where `XXXXX` is your data feed number. You also need to specify your Cosm API key as

    cosm:apikey=your_api_key

All item and event related configuration is done in the cosm.persist file. Aliases correspond to cosm datastream IDs for the cosm persistence service.