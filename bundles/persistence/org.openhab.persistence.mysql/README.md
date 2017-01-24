Documentation of the MYSQL Persistence Service

## Introduction

This service allows you to persist state updates using the MySQL database. Note that other SQL databases need a separate binding due to incompatibilities between different SQL databases.

## Features

This persistence service supports writing information to mysql relational database systems.

## Installation

For installation of this persistence package please follow the same steps as if you would [install a binding](Bindings).

Additionally, place a persistence file called mysql.persist in the _${openhab.home}/configuration/persistence_ folder. This has the standard format as described in [[Persistence]].

## Configuration

This persistence service can be configured in the "SQL Persistence Service" section in openhab.cfg.
```
############################ mySQL Persistence Service ##################################

# the database url like 'jdbc:mysql://<host>:<port>/<database>'
mysql:url=jdbc:mysql://127.0.0.1/openhab

# the database user
mysql:user=<your user here>

# the database password
mysql:password=<your password here>

# the reconnection counter
#mysql:reconnectCnt=

# the connection timeout (in seconds)
#mysql:waitTimeout=

# optional tweaking of mysql datatypes
# example as described in https://github.com/openhab/openhab/issues/710
# mysql:sqltype.string=VARCHAR(20000)

# Use MySQL Server time to store item values (=false) or use openHAB Server time (=true).
# For new installations, its recommend to set "mysql:localtime=true".
# (available since 1.9, optional, defaults to false)
#mysql:localtime=true
```
The database location, user and password need to be modified as per your database.

## Database overview
The service will create a mapping table to link each item to a table, and a separate table is generated for each item. The item data tables include the time and data - the data type is dependent on the item type and allows the item state to be recovered back into openHAB in the same way it was stored.

## Reconnecting on error
The persistence service should attempt to reconnect to the mySQL server if it detect errors. There are two configuration parameters that can be adjusted to control this -:
*reconnectCnt* - Setting this to 1 will cause the service to close the connection and reconnect if there are any errors.
*waitTimeout* - This sets the number of seconds that mySQL will keep the session open without any transactions. It should default to 8 hours within mySQL, but some implementations may use lower values (possibly as low as 60 seconds) which would cause unnecessary reconnections. This value needs to be set higher than the maximum logging period.
