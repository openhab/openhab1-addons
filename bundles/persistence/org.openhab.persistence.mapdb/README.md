# mapdb Persistence

The [mapdb](http://www.mapdb.org/) Persistence Service is based on simple key-value store that only saves the last value. The intention is to use this for `restoreOnStartup` items because all other persistence options have their drawbacks if values are only needed for reload.  They:

* grow in time
* require complex installs (`mysql`, `influxdb`, ...)
* `rrd4j` can't store all item types (only numeric types)

Querying the mapdb persistence service for historic values other than the last value make no sense since the persistence service can only store one value per item.

## Configuration

This service can be configured in the file `services/mapdb.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| commitinterval | 5 |    No    | commit interval in seconds |
| commitsamestate | false | No  | set to `true` to issue a commit even if the state did not change

All item and event related configuration is done in the file `persistence/mapdb.persist`.

To configure this service as the default persistence service for openHAB 2, add or change the line

```
org.eclipse.smarthome.persistence:default=mapdb
```

in the file `services/runtime.cfg`.


## Troubleshooting

Restore of items after startup is taking some time. Rules are already started in parallel. Especially in rules that are started via `System started` trigger, it may happen that the restore is not completed resulting in undefined items. In these cases the use of restored items has to be delayed by a couple of seconds. This delay has to be determined experimentally.
