Documentation of the db4o Persistence Service

## Introduction

db4o is a pure Java object database, which uses a simple database file for persistence. 

## Features

This persistence service supports writing information to the database as well as querying from it.
It keeps all data that you ask it to persist, so the database is growing over time. Please take this into account when defining the items and the strategies for persistence with this service.

As it can happen that the (single) database file is corrupted if the runtime is shutdown ungracefully, this persistence service offers an automatic backup management, so that you can revert at least to an earlier version in case of data corruption.

Additionally, a commit interval can be configured for the database, so that you can optimize the write access to the file system.

## Installation

For installation of this persistence package please follow the same steps as if you would [install a binding](Bindings).

Additionally, place a persistence file called db4o.persist in the `${openhab.home}/configuration/persistence` folder.

## Configuration

This persistence service can be configured in the "Db4o Persistence Service" section in `openhab.cfg`.
You can choose the backup interval, the number of backups to keep and the commit interval.

All item and event related configuration is done in the db4o.persist file. Aliases do not have any special meaning for the db4o persistence service.