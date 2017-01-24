Documentation of the JPA Persistence Service

## Introduction

This service allows you to persist state updates using a SQL or NoSQL database.
The Binding uses an abstraction layer that theoretically allows it to support many available SQL or NoSQL databases.

## Features

This persistence service supports writing information to SQL or NoSQL database systems.
Currently, with openHAB 1.6, the binding supports MySQL, Apache Derby and PostgreSQL databases.

UPDATE: from openHAB 1.8+ only the embedded Derby database driver is included. Other drivers must be installed manually. (See below for more information on that.)

## Installation

For installation of this persistence package please follow the same steps as if you would [install a binding](Bindings).

Additionally, place a persistence file called jpa.persist in the _${openhab.home}/configuration/persistence_ folder. This has the standard format as described in [[Persistence]].

## Configuration

This persistence service can be configured in the "JPA Persistence Service" section in openhab.cfg.
Available are four configuration settings.
### Connection URL
```
jpa:url   # this is jdbc connection url to the database
```
Examples:
```
jpa:url=jdbc:postgresql://hab.local:5432/openhab
jpa:url=jdbc:derby://hab.local:1527/openhab;create=true
jpa:url=jdbc:mysql://localhost:3306/openhab
```
Attention: databases "openhab" for MySQL and PostgreSQL must be created manually first.
The JPA binding does not create databases, except this is possible as configuration in the jdbc url (see Apache Derby example).

Update:
From version 1.8 the binding does create tables.

### Database driver
```
jpa:driver        # the database driver class name
```
Examples:
```
jpa:driver=org.postgresql.Driver
jpa:driver=org.apache.derby.jdbc.ClientDriver
jpa:driver=com.mysql.jdbc.Driver
```

### Username
```
jpa:user=         # the database username for connection
```

### Password
```
jpa:password=     # the database username password for connection
```

## Database overview
The binding will create one table "historic_item" where all item states are stored.
The item state as such is stored as a string representation.

## Notes for openHAB 1.8+
In openHAB 1.8 the binding has been modified in that now only the embedded Derby driver is included.
Drivers for other databases must be installed manually. This is a trivial process. Normally JDBC database drivers are packaged as OSGi bundles and can just be dropped into the 'addons' folder of openHAB.
This has the advantage that users can update their drivers as needed.

The following database drivers are known to work:
- postgresql-9.4-1203-jdbc41.jar
- postgresql-9.4-1206-jdbc41.jar

If a database driver is not an OSGi bundle the below technique can be used to extend the openHAB classpath.

## Adding support for other JPA supported databases
Other database drivers can be added by expanding the openhab classpath.
Use the following classpath setup in start.sh / start_debug.sh of openhab:
```
cp=$(echo lib/*.jar | tr ' ' ':'):$(find $eclipsehome -name "org.eclipse.equinox.launcher_*.jar" | sort | tail -1);
```
This will add all .jar files in a folder "lib" in the root of openhab.
All databases that are supported by JPA can be added.
Define jpa:driver and jpa:url according to the database definitions.