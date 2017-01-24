Documentation of the MongoDB Persistence Service

## Introduction

This service allows you to persist state updates using the MongoDB database.

## Features

This persistence service supports writing information to MongoDB document store.

## Installation

For installation of this persistence package please follow the same steps as if you would [install a binding](Bindings).

Additionally, place a persistence file called mongodb.persist in the _${openhab.home}/configuration/persistence_ folder. This has the standard format as described in [[Persistence]].

## Configuration

This persistence service can be configured in the "MongoDB Persistence Service" section in openhab.cfg.
```
############################ MongoDB Persistence Service ##################################
# the database url
mongodb:url=mongodb://localhost:27017

# the database name
mongodb:database=openhab

# the collection name
mongodb:collection=openhab
```
