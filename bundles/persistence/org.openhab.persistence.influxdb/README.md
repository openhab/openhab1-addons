Documentation of the InfluxDB Persistence Service

## Introduction

This service allows you to persist and query states using the time series database 
[InfluxDB](http://influxdb.org).

There are two influxdb persistence bundles which support different InfluxDB versions.
The bundle named influxdb supports InfluxDB 0.9 and newer and the influxdb08 bundle supports 
InfluxDB up to version 0.8.x.

## 1.9.0 New & Noteworthy
New configuration setting "retentionPolicy" was added.

## 1.8.0 New & Noteworthy
As of openHAB 1.8.0 there are two influxdb persistence bundles which support different InfluxDB versions.
If you are upgrading from prior openHAB versions you have to either upgrade your InfluxDB installation to 0.9.x or newer
(Upgrading docs: https://github.com/influxdata/influxdb/blob/master/importer/README.md) or if you want 
keep your current InfluxDB installation switch to the influxdb08 persistence bundle.

## Features

The InfluxDB persistence service persists item values using the the InfluxDB time series database.
The persisted values can be queried from within openHAB. There also are nice tools on the web for 
visualizing InfluxDB time series.

## Database Structure
The states of an item are persisted in time series with names equal to the name of the item. 
All values are stored in a field called "value" using integers or doubles, OnOffType and 
OpenClosedType values are stored using 0 or 1. 
The times for the entries are calculated by InfluxDB.

An example entry for an item with the name "AmbientLight" would look like this:

|time |   sequence_number| value|
|-----|-----------------|-------|
|1402243200072 |  79370001 |   6|


## Installation and Configuration
### Database Setup
First of all you have to setup and run a InfluxDB server. This is very easy and you will find good
documentation on it on the [InfluxDB web site](http://influxdb.com/docs/v0.8/introduction/installation.html).

Then database and the user must be created. This can be done using the influxdb 
admin web interface. If you want to use the defaults then create a database called
```openhab``` and a user with write access on the database called ```openhab```. 
Choose a password and remember it.

### openhab.cfg
After this the persistence service needs some configuration in the "InfluxDB Persistence Service" 
respectively "InfluxDB 0.8 Persistence Service" section in openhab.cfg.

The defaults for the database name, the database user, the database url and the retentionPolicy are "openhab",
"openhab", "http://127.0.0.1:8086" and "default" respectively. If you took this defaults for the database setup 
you only have to add the password value to the influxdb:password=<password> variable.

| variable            | description                   | default |openhab version|
|---------------------|-------------------------------|---------|---------------|
|influxdb:url         | the database URL              | http://127.0.0.1:8086 | <= 1.8 | 
|influxdb:user        | the name of the database user | openhab | <= 1.8 |
|influxdb:db          | the name of the database      | openhab | <= 1.8 |
|influxdb:password   | the password of the database user | no default | <= 1.8 |
|influxdb:retentionPolicy | the retentionPolicy name | "autogen" (builds before 5. Nov 2016 used "default") | >= 1.9 |

Please note starting with InfluxDB >= 1.0, default retention policy name is no longer "default" but "autogen". Please update the InfluxDB Persistence Service to the newest SNAPSHOT release, which also uses "autogen" as default retention policy name. 

For __influxdb08__ users the variables are named influxdb08:url, influxdb08:user, influxdb08:db and influxdb08:password. The retentionPolicy variable isn't available for influxdb08. If influxdb08 is your default persitence service, then change to persistence:default=influxdb08 as well. 

### Installation
For installation of this persistence package please follow the same steps as if you would [install a binding](Bindings).

Now place a persistence configuration file called influxdb.persist (or influxdb08.persist for the influxdb08 bundle) in the 
_${openhab.home}/configuration/persistence_ folder. This has the standard format as described in [[Persistence]].