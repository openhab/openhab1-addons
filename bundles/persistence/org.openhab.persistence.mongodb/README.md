# MongoDB Persistence

This service allows you to persist state updates using the MongoDB database.  It supports writing information to a MongoDB document store, as well as querying from it.

## Configuration

This service can be configured in the file `services/mongodb.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| url      |         |   Yes    | connection URL to address Mongodb.  For example, `mongodb://localhost:27017` |
| database |         |   Yes    | database name |
| collection |       |   Yes    | collection name |

All item and event related configuration is done in the file `persistence/mongodb.persist`.
