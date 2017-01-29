# InfluxDB Persistence

This service allows you to persist and query states using the [InfluxDB](http://influxdb.org) time series database. The persisted values can be queried from within openHAB. There also are nice tools on the web for visualizing InfluxDB time series, such as [Grafana](http://grafana.org/).

> There are two Influxdb persistence bundles which support different InfluxDB versions.  The bundle named `influxdb` supports InfluxDB 0.9 and newer, and the `influxdb08` bundle supports InfluxDB up to version 0.8.x.

## Database Structure

The states of an item are persisted in time series with names equal to the name of the item.  All values are stored in a field called "value" using integers or doubles, `OnOffType` and `OpenClosedType` values are stored using 0 or 1. The times for the entries are calculated by InfluxDB.

An example entry for an item with the name "AmbientLight" would look like this:

|time |   sequence_number| value|
|-----|-----------------|-------|
|1402243200072 |  79370001 |   6|

## Prerequisites

First of all you have to setup and run an InfluxDB server. This is very easy and you will find good documentation on it on the [InfluxDB web site](https://docs.influxdata.com/influxdb/v1.2/).

Then database and the user must be created. This can be done using the InfluxDB admin web interface. If you want to use the defaults, then create a database called `openhab` and a user with write access on the database called `openhab`. Choose a password and remember it.

## Configuration

If you are using InfluxDB version 0.9 or later, you can configure this service in the file `services/influxdb.cfg`.  If you are using InfluxDB 0.8 or earlier, you can configure this service in the file `services/influxdb08.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| url      | http://127.0.0.1:8086 | No | database URL |
| user     | openhab |    No    | name of the database user, e.g. `openhab` |
| password |         |    Yes   | password of the database user that you chose in [Prerequisites](#prerequisites) above |
| db       | openhab |    No    | name of the database |
| retentionPolicy | autogen |  No | name of the retentionPolicy. Please note starting with InfluxDB >= 1.0, the default retention policy name is no longer `default` but `autogen`. The `retentionPolicy` configuration property is not available for the `influxdb08` persistence service. |

All item and event related configuration is defined in either the file `persistence/influxdb.persist` or `persistence/influxdb08.persist`, depending on the version of InfluxDB you are using.
