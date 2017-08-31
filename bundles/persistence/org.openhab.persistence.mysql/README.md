# MySQL Persistence

This service allows you to persist state updates using the [MySQL](https://www.mysql.com/) database. Note that other SQL databases need a separate service due to incompatibilities between different SQL databases.

This persistence service supports writing information to MySQL relational database systems, as well as querying from them.

The service will create a mapping table called `Items` to link each item to a table, and a separate table is generated for each item. The item data tables include the time and data - the data type is dependent on the item type and allows the item state to be recovered back into openHAB in the same way it was stored.

## Configuration

This service can be configured in the file `services/mysql.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| url      |         |   Yes    | database URL, in the format `jdbc:mysql://<host>:<port>/<database>`.  For example, `jdbc:mysql://127.0.0.1/openhab` |
| user     |         | if needed | database user |
| password |         | if needed | database password |
| reconnectCnt |     |   No      | reconnection counter. Setting this to 1 will cause the service to close the connection and reconnect if there are any errors. |
| waitTimeout |      |   No      | connection timeout (in seconds). This sets the number of seconds that MySQL will keep the session open without any transactions. It should default to 8 hours within mySQL, but some implementations may use lower values (possibly as low as 60 seconds) which would cause unnecessary reconnections. This value needs to be set higher than the maximum logging period. |
| sqltype.string |   |   No      | mapping of an openHAB item type to an SQL data type.  See [this issue](https://github.com/openhab/openhab1-addons/issues/710) for more information. |
| localtime | `false` |  No      | use MySQL server time to store item values (if set to `false`) or use openHAB server time (if set to `true`). For new installations, setting this to `true` is recommended. |

All item and event related configuration is done in the file `persistence/mysql.persist`.
