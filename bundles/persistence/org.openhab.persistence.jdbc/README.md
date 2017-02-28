# JDBC Persistence

This service writes and reads item states to and from a number of relational database systems that support [Java Database Connectivity (JDBC)](https://en.wikipedia.org/wiki/Java_Database_Connectivity).  This service allows you to persist state updates using one of several different underlying database services. It is designed for a maximum of scalability, to store very large amounts of data and still over the years not lose its speed.

The generic design makes it relatively easy for developers to integrate other databases that have JDBC drivers.  The following databases are currently supported and tested:

| Database                                     | Tested Driver / Version |
|----------------------------------------------|-------------------------|
| [Apache Derby](https://db.apache.org/derby/) | [derby-10.12.1.1.jar](http://mvnrepository.com/artifact/org.apache.derby/derby) |
| [H2](http://www.h2database.com/)             | [h2-1.4.191.jar](http://mvnrepository.com/artifact/com.h2database/h2) |
| [HSQLDB](http://hsqldb.org/)                 | [hsqldb-2.3.3.jar](http://mvnrepository.com/artifact/org.hsqldb/hsqldb) |
| [MariaDB](https://mariadb.org/)              | [mariadb-java-client-1.4.6.jar](http://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client) |
| [MySQL](https://www.mysql.com/)              | [mysql-connector-java-5.1.39.jar](http://mvnrepository.com/artifact/mysql/mysql-connector-java) |
| [PostgreSQL](http://www.postgresql.org/)     | [postgresql-9.4.1209.jre7.jar](http://mvnrepository.com/artifact/org.postgresql/postgresql) |
| [SQLite](https://www.sqlite.org/)            | [sqlite-jdbc-3.16.1.jar](http://mvnrepository.com/artifact/org.xerial/sqlite-jdbc) |

## Table of Contents

<!-- MarkdownTOC -->

- [Configuration](#configuration)
	- [Minimal Configuration](#minimal-configuration)
	- [Migration from MySQL to JDBC Persistence Services](#migration-from-mysql-to-jdbc-persistence-services)
- [Technical Notes](#technical-notes)
	- [Database Table Schema](#database-table-schema)
	- [Number Precision](#number-precision)
	- [Rounding results](#rounding-results)
	- [For Developers](#for-developers)
	- [Performance Tests](#performance-tests)

<!-- /MarkdownTOC -->

## Configuration

This service can be configured in the file `services/jdbc.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| url      |         |    Yes   | JDBC URL to establish a connection to your database.  Examples:<br/><br/>`jdbc:derby:./testDerby;create=true`<br/>`jdbc:h2:./testH2`<br/>`jdbc:hsqldb:./testHsqlDb`<br/>`jdbc:mariadb://192.168.0.1:3306/testMariadb`<br/>`jdbc:mysql://192.168.0.1:3306/testMysql?serverTimezone=UTC`<br/>`jdbc:postgresql://192.168.0.1:5432/testPostgresql`<br/>`jdbc:sqlite:./testSqlite.db`.<br/><br/>If no database is available it will be created; for example the url `jdbc:h2:./testH2` creates a new H2 database in openHAB folder. Example to create your own MySQL database directly:<br/><br/>`CREATE DATABASE 'yourDB' CHARACTER SET utf8 COLLATE utf8_general_ci;`|
| user     |        | if needed | database user name |
| password |        | if needed | database user password |
| errReconnectThreshold | 0 | No | when the service is deactivated (0 means ignore) |
| sqltype.CALL			    | `VARCHAR(200)`   | No | All `sqlType` options allow you to change the SQL data type used to store values for different openHAB item states.  See the following links for further information: [mybatis](https://mybatis.github.io/mybatis-3/apidocs/reference/org/apache/ibatis/type/JdbcType.html) [H2](http://www.h2database.com/html/datatypes.html) [PostgresSQL](http://www.postgresql.org/docs/9.3/static/datatype.html) |
| sqltype.COLOR			    | `VARCHAR(70)`    | No | see above |
| sqltype.CONTACT		    | `VARCHAR(6)`     | No | see above |
| sqltype.DATETIME		    | `DATETIME`       | No | see above |
| sqltype.DIMMER		    | `TINYINT`        | No | see above |
| sqltype.LOCATION		    | `VARCHAR(30)`    | No | see above |
| sqltype.NUMBER		    | `DOUBLE`         | No | see above |
| sqltype.ROLLERSHUTTER	    | `TINYINT`        | No | see above |
| sqltype.STRING		    | `VARCHAR(65500)` | No | see above |
| sqltype.SWITCH		    | `VARCHAR(6)`     | No | see above |
| sqltype.TABLEPRIMARYKEY   | `TIMESTAMP`      | No | see above |
| sqltype.TABLEPRIMARYVALUE | `NOW()`          | No | see above |
| numberDecimalcount        | 3                | No | for Itemtype "Number" default decimal digit count |
| tableNamePrefix           | `item`           | No | table name prefix. For Migration from MySQL Persistence, set to `Item`. |
| tableUseRealItemNames     | `false`          | No | table name prefix generation.  When set to `true`, real item names are used for table names and `tableNamePrefix` is ignored.  When set to `false`, the `tableNamePrefix` is used to generate table names with sequential numbers. |
| tableIdDigitCount         | 4                | No | when `tableUseRealItemNames` is `false` and thus table names are generated sequentially, this controls how many zero-padded digits are used in the table name.  With the default of 4, the first table name will end with `0001`. For migration from the MySQL persistence service, set this to 0. |
| rebuildTableNames         | false            | No | rename existing tables using `tableUseRealItemNames` and `tableIdDigitCount`. USE WITH CARE! Deactivate after Renaming is done! |
| jdbc.maximumPoolSize      | configured per database in package `org.openhab.persistence.jdbc.db.*` | No | Some embeded databases can handle only one connection.  See [this link](https://github.com/brettwooldridge/HikariCP/issues/256) for more information |
| jdbc.minimumIdle          | see above        | No | see above |
| enableLogTime             | `false`          | No | timekeeping |

All item- and event-related configuration is done in the file `persistence/jdbc.persist`.

To configure this service as the default persistence service for openHAB 2, add or change the line

```
org.eclipse.smarthome.persistence:default=jdbc
```

in the file `services/runtime.cfg`.

### Minimal Configuration

services/jdbc.cfg

```
url=jdbc:postgresql://192.168.0.1:5432/testPostgresql
```

### Migration from MySQL to JDBC Persistence Services

The JDBC Persistence service can act as a replacement for the MySQL Persistence service.  Here is an example of a configuration for a MySQL database named `testMysql` with user `test` and password `test`:

services/jdbc.cfg

```
url=jdbc:mysql://192.168.0.1:3306/testMysql
user=test
password=test
tableNamePrefix=Item
tableUseRealItemNames=false
tableIdDigitCount=0
```

Remember to install and uninstall the services you want, and rename `persistence/mysql.persist` to `persistence/jdbc.persist`.

## Technical Notes

### Database Table Schema

The table name schema can be reconfigured after creation, if needed.

The service will create a mapping table to link each item to a table, and a separate table is generated for each item.  The item data tables include time and data values.  The SQL data type used depends on the openHAB item type, and allows the item state to be recovered back into openHAB in the same way it was stored.

With this *per-item* layout, the scalability and easy maintenance of the database is ensured, even if large amounts of data must be managed. To rename existing tables, use the parameters `tableUseRealItemNames` and `tableIdDigitCount` in the configuration.

### Number Precision

Default openHAB number items are persisted with SQL datatype `double`. Internally openHAB uses `BigDecimal`. If better numerical precision is needed, for example set `sqltype.NUMBER = DECIMAL(max digits, max decimals)`, then on the Java side, the service works with `BigDecimal` without type conversion. If more come decimals as `max decimals` provides, this persisted value is rounded mathematically correctly. The SQL types `DECIMAL` or  `NUMERIC` are precise, but to work with `DOUBLE` is faster.

### Rounding results

The results of database queries of number items are rounded to three decimal places by default. With `numberDecimalcount` decimals can be changed. Especially if sql types `DECIMAL` or  `NUMERIC` are used for `sqltype.NUMBER`, rounding can be disabled by setting `numberDecimalcount=-1`. 

### For Developers

* Clearly separated source files for the database-specific part of openHAB logic.
* Code duplication by similar services is prevented.
* Integrating a new SQL and JDBC enabled database is fairly simple.

### Performance Tests

Not necessarily representative of the performance you may experience.

DATABASE | FIRST RUN | AVERAGE | FASTEST | SIZE AFTER | COMMENT
-------- | --------: | ------: | ------: | ---------: | --------
Derby | 7.829 | 6.892 | 5.381 | 5.36 MB  | local embedded
H2 | 1.797 | 2.080 | 1.580 |  0.96 MB | local embedded
hsqldb | 3.474 | 2.104 | 1.310 | 1.23 MB | local embedded
mysql | 11.873 | 11.524 | 10.971 | - | ext. Server VM
postgresql | 8.147 | 7.072 | 6.895 | - | ext. Server VM
sqlite | 2.406 | 1.249 | 1.137 | 0.28 MB| local embedded

* Each test ran about 20 Times every 30 seconds.
* openHAB 1.x has ready started for about a Minute.
* the data in seconds for the evaluation are from the console output.

Used a script like this:

```
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
```


