
############################ DBMS Persistence Service ##################################
# I N S T A L L   D B M S   P E R S I S T E N C E   B U N D L E 
# To use this DBMS-service-bundle (org.openhab.persistence.dbms-X.X.X.jar),
# a appropriate JDBC database-driver is needed in OpenHab addons Folder.
# Prepared matching Bundles with JDBC database-driver embedded can be found in DBMS project. 
# Use: '/drivers/org.openhab.persistence.dbms.jdbc.[databaseName]-X.X.X.jar'.
# Copy both (DBMS-service-bundle and a JDBC database-driver) to your OpenHab '[OpneHab]/addons' Folder to make it work. 
#
# A special case is the driver for H2, it ONLY works by copying "h2-X.X.XXX.jar" to your '[OpneHab]/addons' folder directly.
# Alternatively copying the driver directly may work for some other JDBC database-driver Files too. 
#
# Tested databases/url-prefix: jdbc:derby, jdbc:h2, jdbc:hsqldb, jdbc:mariadb, jdbc:mysql, jdbc:postgresql, jdbc:sqlite
# If using a not known url like jdbc:XXX, configuration is falling back to the default mysql profile and SQL.
# 
# derby, h2, hsqldb, sqlite can be embedded, 
# If no database is available, for Example the url 'jdbc:h2:./testH2' creates a new DB in OpenHab Folder. 

# D A T A B A S E  C O N F I G
# Some URL-Examples, 'service' identifies and activates internally the correct jdbc driver.
# required database url like 'jdbc:<service>:<host>[:<port>;<attributes>]'
# dbms:url=jdbc:derby:./testDerby;create=true
# dbms:url=jdbc:h2:./testH2
# dbms:url=jdbc:hsqldb:./testHsqlDb
# dbms:url=jdbc:mariadb://192.168.0.1:3306/testMariadb
# dbms:url=jdbc:mysql://192.168.0.1:3306/testMysql
# dbms:url=jdbc:postgresql://192.168.0.1:5432/testPostgresql
# dbms:url=jdbc:sqlite:./testSqlite.db

# required database user
#dbms:user=
dbms:user=test

# required database password
#dbms:password=
dbms:password=test

# E R R O R   H A N D L I N G
# optional when Service is deactivated (optional, default: 0 -> ignore) 
#dbms:errReconnectThreshold=

# I T E M   O P E R A T I O N S
# optional tweaking SQL datatypes
# see: https://mybatis.github.io/mybatis-3/apidocs/reference/org/apache/ibatis/type/JdbcType.html	
# see: http://www.h2database.com/html/datatypes.html
# see: http://www.postgresql.org/docs/9.3/static/datatype.html
# defaults:
#dbms:sqltype.CALL			=	VARCHAR(200)
#dbms:sqltype.COLOR			=	VARCHAR(70)
#dbms:sqltype.CONTACT		=	VARCHAR(6)
#dbms:sqltype.DATETIME		=	DATETIME
#dbms:sqltype.DIMMER		=	TINYINT
#dbms:sqltype.LOCATION		=	VARCHAR(30)
#dbms:sqltype.NUMBER		=	DOUBLE
#dbms:sqltype.ROLLERSHUTTER	=	TINYINT
#dbms:sqltype.STRING		=	VARCHAR(65500)
#dbms:sqltype.SWITCH		=	VARCHAR(6)

# For OpenHab Itemtype "Number" default decimal digit count (optional, default: 3) 
#dbms:numberDecimalcount=

# T A B L E   O P E R A T I O N S
# Tablename Prefix generation, using Item real names or "item" (optional, default: false -> "item") 
#dbms:tableUseRealItemNames=true

# Tablename Suffix length (optional, default: 4 -> 0001-9999) 
#dbms:tableIdDigitCount=

# Rename existing Tables using tableUseRealItemNames and tableIdDigitCount (optional, default: false) 
# USE WITH CARE! Deactivate after Renaming is done!
#dbms:rebuildTableNames=true

# D A T A B A S E / M Y B A T I S   C O N N E C T I O N S
# Some embeded Databases can handle only one Connection (optional, default: 1) 
# dbms.maxActiveConnections = 1
# dbms.maxIdleConnections = 1
