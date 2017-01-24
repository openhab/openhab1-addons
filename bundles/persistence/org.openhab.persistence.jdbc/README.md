*Available as of openHAB 1.8*

### Table of Contents

 * [Introduction](#introduction)
 * [Features](#features)
 * [Installing JDBC Persistence](#installing-jdbc-persistence)
   * [New Installation](#new-installation)
   * [Migrating from MySQL Bundle](#migrating-from-mysql-bundle)
   * [JDBC Driver Files](#jdbc-driver-files)
 * [Configuring JDBC Persistence](#configuring-jdbc-persistence)
 * [Database Table Schema](#database-table-schema)
 * [Number Precision](#number-precision)
 * [Rounding results](#rounding-results)
 * [Not representative Database Performance Tests](#not-representative-database-performance-tests)

### Introduction

This service allows you to persist state updates using one of several different underlying database services. 
The **JDBC Persistence Service** is designed for a maximum of scalability. It is designed to store very large amounts of data, and still over the years not lose its speed.
The generic design makes it relatively easy for developers to integrate other databases that have JDBC drivers.
It can act as a replacement for the [MySQL-Persistence](https://github.com/openhab/openhab/wiki/MySQL-Persistence) bundle (with additional configuration in `openhab.cfg`).

Currently the following databases are supported and tested:
 - [Apache Derby](https://db.apache.org/derby/)
 - [H2](http://www.h2database.com/)
 - [HSQLDB](http://hsqldb.org/)
 - [MariaDB](https://mariadb.org/)
 - [MySQL](https://www.mysql.com/)
 - [PostgreSQL](http://www.postgresql.org/)
 - [SQLite](https://www.sqlite.org/)

### Features

**NOTE Sept. 2016:** 
- The JDBC Persistence respects server's **LOCALTIME** now, it used **UTC** before. Through an update, there may be a time lag in the recorded data! See [Time zones are different...](https://github.com/openhab/openhab/issues/3905) and [JDBC now with high precision by fractional seconds](https://github.com/openhab/openhab/pull/4550#discussion_r75578302)

*General:*
- Writing/reading information to relational database systems.
- [Database Table Name Schema](#database-table-schema) can be reconfigured after creation.
- JDBC drivers are not contained within the bundle and must be downloaded and added separately to your `${openhab.home}/addons` directory.
 
*For Developers:*
- Clearly separated source files for the database-specific part of openHAB logic.
- Code duplication by similar services is prevented.
- Integrating a new SQL and JDBC enabled database is fairly simple.

### Installing JDBC Persistence

#### New Installation
  1. For installation of this persistence bundle, please follow the same steps as if you would [install a binding](Bindings).
  1. Copy the database-specific driver JAR file (see below) to your `${openhab.home}/addons` directory. 
  1. Place a persistence file called `jdbc.persist` into the `${openhab.home}/configuration/persistence` folder. This has the standard format as described in [[Persistence]].
  1. In `openhab.cfg` change `persistence:default` parameter to `jdbc`:
```
persistence:default=jdbc
```

#### Migrating from MySQL Bundle
If you are migrating from the MySQL persistence bundle to the JDBC persistence bundle, follow these steps:
  1. For installation of this persistence bundle, please follow the same steps as if you would [install a binding](Bindings).
  1. Copy the database-specific driver JAR file (see below) to your `${openhab.home}/addons` directory. 
  1. Remove the MySQL persistence bundle from your `${openhab.home}/addons` directory.
  1. In your `${openhab.home}/configurations/persistence` directory, rename your `mysql.persist` file to `jdbc.persist`.
  1. In your `openhab.cfg` file, add or change these configuration items:
```
persistence:default=jdbc
jdbc:tableNamePrefix=Item
jdbc:tableUseRealItemNames=false
jdbc:tableIdDigitCount=0
```

#### JDBC Driver Files
Database | Tested File | Repository
------------- | ------------- | -------------
Derby | derby-10.12.1.1.jar | [Maven](http://mvnrepository.com/artifact/org.apache.derby/derby)
H2 | h2-1.4.191.jar | [Maven](http://mvnrepository.com/artifact/com.h2database/h2)
HSQLDB | hsqldb-2.3.3.jar | [Maven](http://mvnrepository.com/artifact/org.hsqldb/hsqldb)
MariaDB | mariadb-java-client-1.4.6.jar | [Maven](http://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client)
MySQL | mysql-connector-java-5.1.39.jar | [Maven](http://mvnrepository.com/artifact/mysql/mysql-connector-java)
PostgreSQL | postgresql-9.4.1209.jre7.jar  | [Maven](http://mvnrepository.com/artifact/org.postgresql/postgresql)
SQLite | sqlite-jdbc-3.8.11.2.jar | [Maven](http://mvnrepository.com/artifact/org.xerial/sqlite-jdbc)

### Configuring JDBC Persistence
You can configure the persistence service in **JDBC Persistence Service** section in `openhab.cfg`.

#### Minimal Configuration
```
############################ JDBC Persistence Service ##################################
#
# required database url like 'jdbc:<service>:<host>[:<port>;<attributes>]'
jdbc:url=jdbc:postgresql://192.168.0.1:5432/testPostgresql
#
```

#### Migration from MYSQL Bundle to JDBC Bundle
```
############################ JDBC Persistence Service ##################################
#
# required database url like 'jdbc:<service>:<host>[:<port>;<attributes>]'
jdbc:url=jdbc:mysql://192.168.0.1:3306/testMysql
#
# optional database user
jdbc:user=test
#
# optional database password
jdbc:password=test
#
# for Migration from MYSQL-Bundle set to 'Item'.
jdbc:tableNamePrefix=Item
#
# for Migration from MYSQL-Bundle do not use real names.
jdbc:tableUseRealItemNames=false
#
# for Migration from MYSQL-Bundle set to 0.
jdbc:tableIdDigitCount=0

```

#### Full Configuration
```
############################ JDBC Persistence Service ##################################
# I N S T A L L   J D B C   P E R S I S T E N C E   S E R V I C E 
# To use this JDBC-service-bundle (org.openhab.persistence.jdbc-X.X.X.jar),
# a appropriate JDBC database-driver is needed in OpenHab addons Folder.
# Copy both (JDBC-service-bundle and a JDBC database-driver) to your OpenHab '[OpenHab]/addons' Folder to make it work. 
#
# Driver jars see: https://github.com/openhab/openhab/wiki/JDBC-Persistence#jdbc-driver-files
#
# Tested databases/url-prefix: jdbc:derby, jdbc:h2, jdbc:hsqldb, jdbc:mariadb, jdbc:mysql, jdbc:postgresql, jdbc:sqlite
# 
# derby, h2, hsqldb, sqlite can be embedded, 
# If no database is available it will be created, for example the url 'jdbc:h2:./testH2' creates a new DB in OpenHab Folder. 
#
# Create new database, for example on a MySQL-Server use: 
# CREATE DATABASE 'yourDB' CHARACTER SET utf8 COLLATE utf8_general_ci;

# D A T A B A S E  C O N F I G
# Some URL-Examples, 'service' identifies and activates internally the correct jdbc driver.
# required database url like 'jdbc:<service>:<host>[:<port>;<attributes>]'
# jdbc:url=jdbc:derby:./testDerby;create=true
# jdbc:url=jdbc:h2:./testH2
# jdbc:url=jdbc:hsqldb:./testHsqlDb
# jdbc:url=jdbc:mariadb://192.168.0.1:3306/testMariadb
# jdbc:url=jdbc:mysql://192.168.0.1:3306/testMysql?serverTimezone=UTC
# jdbc:url=jdbc:postgresql://192.168.0.1:5432/testPostgresql
# jdbc:url=jdbc:sqlite:./testSqlite.db

# optional database user
#jdbc:user=
jdbc:user=test

# optional database password
#jdbc:password=
jdbc:password=test

# E R R O R   H A N D L I N G
# optional when Service is deactivated (optional, default: 0 -> ignore) 
#jdbc:errReconnectThreshold=

# I T E M   O P E R A T I O N S
# optional tweaking SQL datatypes
# see: https://mybatis.github.io/mybatis-3/apidocs/reference/org/apache/ibatis/type/JdbcType.html	
# see: http://www.h2database.com/html/datatypes.html
# see: http://www.postgresql.org/docs/9.3/static/datatype.html
# defaults:
#jdbc:sqltype.CALL			=	VARCHAR(200)
#jdbc:sqltype.COLOR			=	VARCHAR(70)
#jdbc:sqltype.CONTACT		=	VARCHAR(6)
#jdbc:sqltype.DATETIME		=	DATETIME
#jdbc:sqltype.DIMMER		=	TINYINT
#jdbc:sqltype.LOCATION		=	VARCHAR(30)
#jdbc:sqltype.NUMBER		=	DOUBLE
#jdbc:sqltype.ROLLERSHUTTER	=	TINYINT
#jdbc:sqltype.STRING		=	VARCHAR(65500)
#jdbc:sqltype.SWITCH		=	VARCHAR(6)
#jdbc:sqltype.TABLEPRIMARYKEY   =	TIMESTAMP
#jdbc:sqltype.TABLEPRIMARYVALUE =	NOW()

# For Itemtype "Number" default decimal digit count (optional, default: 3) 
#jdbc:numberDecimalcount=

# T A B L E   O P E R A T I O N S
# Tablename Prefix String (optional, default: "item") 
# for Migration from MYSQL-Bundle set to 'Item'.
#jdbc:tableNamePrefix=Item

# Tablename Prefix generation, using Item real names or "item" (optional, default: false -> "item") 
# If true, 'tableNamePrefix' is ignored.
#jdbc:tableUseRealItemNames=
jdbc:tableUseRealItemNames=true

# Tablename Suffix length (optional, default: 4 -> 0001-9999) 
# for Migration from MYSQL-Bundle set to 0.
#jdbc:tableIdDigitCount=

# Rename existing Tables using tableUseRealItemNames and tableIdDigitCount (optional, default: false) 
# USE WITH CARE! Deactivate after Renaming is done!
#jdbc:rebuildTableNames=true

# D A T A B A S E  C O N N E C T I O N S
# Some embeded Databases can handle only one Connection (optional, default: configured per database in packet org.openhab.persistence.jdbc.db.* )
# see: https://github.com/brettwooldridge/HikariCP/issues/256
# jdbc.maximumPoolSize = 1
# jdbc.minimumIdle = 1

# T I M E K E E P I N G
# (optional, default: false) 
#jdbc:enableLogTime=true

```

### Database Table Schema
The service will create a mapping table to link each item to a table, and a separate table is generated for each item.
The item data tables include time and data values.  The SQL data type used depends on the openHAB item type, and allows the item state to be recovered back into openHAB in the same way it was stored.
With this *per-item* layout, the scalability and easy maintenance of the database is ensured, even if large amounts of data must be managed. To rename existing tables, use the parameters `jdbc:tableUseRealItemNames` and `jdbc:tableIdDigitCount` in the **JDBC Persistence Service** section of `openhab.cfg`.

### Number Precision
Default openHab number items are persisted with sql datatype `double`. Internally openHab uses `BigDecimal`. If better numerical precision is needed, for example set `jdbc:sqltype.NUMBER = DECIMAL(max digits, max decimals)`  in the **JDBC Persistence Service** section of `openhab.cfg`, then on java side the service works with `BigDecimal` without type conversion. If more come decimals as `max decimals` provides, this persisted value is rounded mathematically correct. The sql types `DECIMAL ` or  `NUMERIC` are precise, but to work with `DOUBLE` is faster.

### Rounding results
The results of database queries of number items are rounded to three decimal places by default. With `jdbc:numberDecimalcount` in the **JDBC Persistence Service** section of `openhab.cfg` decimals can be changed. Especially if sql types `DECIMAL ` or  `NUMERIC` are used for `jdbc:sqltype.NUMBER`, rounding can be disabled by setting `jdbc:numberDecimalcount=-1`. 


### Not representative Database Performance Tests
#### Results per DATABASE:
DATABASE | FIRST RUN | AVERAGE | FASTEST | SIZE AFTER | COMMENT
-------- | --------: | ------: | ------: | ---------: | --------
Derby | 7.829 | 6.892 | 5.381 | 5.36 MB  | local embedded
H2 | 1.797 | 2.080 | 1.580 |  0.96 MB | local embedded
hsqldb | 3.474 | 2.104 | 1.310 | 1.23 MB | local embedded
mysql | 11.873 | 11.524 | 10.971 | - | ext. Server VM
postgresql | 8.147 | 7.072 | 6.895 | - | ext. Server VM
sqlite | 2.406 | 1.249 | 1.137 | 0.28 MB| local embedded

Each Test ran about 20 Times every 30 seconds.
OpenHAB has ready started for about a Minute.
The data in seconds for the evaluation are from the Console Output.
#### Used a script like this:
````
var count = 0;
rule "DB STRESS TEST"
when 
	Time cron "30 * * * * ?"
then
	if( count = 24) count = 0
	count = count+1
	if( count > 3 && count < 23){
		for( var i=500; i>1; i=i-1){
			postUpdate( NUMBERITEM, i)
			SWITCHITEM.previousState().state
			postUpdate( DIMMERITEM, OFF)
			NUMBERITEM.changedSince( now().minusMinutes(1))
			postUpdate( DIMMERITEM, ON)
		}
	}
end
````


