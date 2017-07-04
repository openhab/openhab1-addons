# InfluxDB (0.9 and newer) Persistence

This service allows you to persist and query states using the [InfluxDB](http://influxdb.org) time series database. The persisted values can be queried from within openHAB. There also are nice tools on the web for visualizing InfluxDB time series, such as [Grafana](http://grafana.org/).

> There are two Influxdb persistence bundles which support different InfluxDB versions.  This service, named `influxdb`, supports InfluxDB 0.9 and newer, and the `influxdb08` service supports InfluxDB up to version 0.8.x.

## Database Structure (classic)

The states of an item are persisted in time series with names equal to the name of the item.  All values are stored in a field called "value" using integers or doubles, `OnOffType` and `OpenClosedType` values are stored using 0 or 1. The times for the entries are calculated by InfluxDB.

An example entry for an item with the name "AmbientLight" would look like this:

|time |   sequence_number| value|
|-----|-----------------|-------|
|1402243200072 |  79370001 |   6|

## Database Structure (single measurement)

To be able to combine different item states in a single query it might be use full to have them stored in a single measurement. If a measurement is defined in the service properties all data will end up in a single measurement. To reflect different value types, they are stored in type specific fields. The field used is referenced in the `type-name`.

An example entry for an item with the name "AmbientLight" would look like this:

|time          |   item-name   | type-name       | value-double | value-integer | value-string | value |
|--------------|---------------|-----------------|--------------|---------------|--------------|-------|
|1402243200072 |  AmbientLight |   value-integer |              | 6             |              |       |

## Prerequisites

First of all you have to setup and run an InfluxDB server. This is very easy and you will find good documentation on it on the [InfluxDB web site](https://docs.influxdata.com/influxdb/v1.2/).

Then database and the user must be created. This can be done using the InfluxDB admin web interface. If you want to use the defaults, then create a database called `openhab` and a user with write access on the database called `openhab`. Choose a password and remember it.

## Configuration

This service can be configured in the file `services/influxdb.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| url      | http://127.0.0.1:8086 | No | database URL |
| user     | openhab |    No    | name of the database user, e.g. `openhab` |
| password |         |    Yes   | password of the database user that you chose in [Prerequisites](#prerequisites) above |
| db       | openhab |    No    | name of the database |
| retentionPolicy | autogen |  No | name of the retentionPolicy. Please note starting with InfluxDB >= 1.0, the default retention policy name is no longer `default` but `autogen`. |
| measurement |     |     No    | Should be set if all data should end up in a single measurement. | 

All item- and event-related configuration is defined in either the file `persistence/influxdb.persist`.
