# db4o Persistence

db4o is a pure Java object database, which uses a simple database file for persistence. 

This persistence service supports writing information to the database as well as querying from it.

It keeps all data that you ask it to persist, so the database is growing over time. Please take this into account when defining the items and the strategies for persistence with this service.

As it can happen that the (single) database file is corrupted if the runtime is shutdown ungracefully, this persistence service offers automatic backup management, so that you can revert at least to an earlier version in case of data corruption.

Additionally, a commit interval can be configured for the database, so that you can optimize the write access to the file system.

## Configuration

This service can be configured in the file `services/db4o.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| backupinterval | `0 0 1 * * ?` | No | A Cron-like expression that defines the interval at which to create a backup of the database.  The default defines a backup is to back place every morning at 01:00. |
| commitinterval | 5 |    No    | commit interval, in seconds |
| maxbackups | 7     |    No    | number of backup files allowed in the database folder.  The defaults provide for a week of daily backups. |

All item and event related configuration is done in the file `persistence/db4o.persist`.  Aliases do not have any special meaning for the db4o persistence service.
