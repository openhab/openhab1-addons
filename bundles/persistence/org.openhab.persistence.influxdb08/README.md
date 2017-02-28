# InfluxDB (up to 0.8.x) Persistence

This service allows you to persist and query states using the [InfluxDB](http://influxdb.org) time series database. The persisted values can be queried from within openHAB. There also are nice tools on the web for visualizing InfluxDB time series, such as [Grafana](http://grafana.org/).

> There are two Influxdb persistence bundles which support different InfluxDB versions.  This service, named `influxdb08`, supports InfluxDB up to version 0.8.x, and the `influxdb` service supports InfluxDB 0.9 and newer.

## Database Structure

The states of an item are persisted in time series with names equal to the name of the item.  All values are stored in a field called "value" using integers or doubles, `OnOffType` and `OpenClosedType` values are stored using 0 or 1. The times for the entries are calculated by InfluxDB.

An example entry for an item with the name "AmbientLight" would look like this:

|time |   sequence_number| value|
|-----|-----------------|-------|
|1402243200072 |  79370001 |   6|

## Prerequisites

First of all you have to setup and run an InfluxDB server up to version 0.8.x. This is very easy and you will find good documentation on it on the [InfluxDB web site](https://docs.influxdata.com/influxdb/v0.8/).

Then database and the user must be created. This can be done using the InfluxDB admin web interface. If you want to use the defaults, then create a database called `openhab` and a user with write access on the database called `openhab`. Choose a password and remember it.

## Configuration

This service can be configured in the file `services/influxdb08.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| url      | http://127.0.0.1:8086 | No | database URL |
| user     | openhab |    No    | name of the database user, e.g. `openhab` |
| password |         |    Yes   | password of the database user that you chose in [Prerequisites](#prerequisites) above |
| db       | openhab |    No    | name of the database |

All item- and event-related configuration is defined in the file `persistence/influxdb08.persist`.