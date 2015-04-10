package org.followmemusic.io;

import java.sql.SQLException;

/**
 * Gestion de la connexion au JDBC (pour une BDD MySQL).
 *
 * Date: 19/03/2014
 * Time: 12:01
 *
 * @author pieraggi
 * @see AbstractDataBaseConnection
 */
public class MySQLDatabaseConnection extends AbstractDatabaseConnection {

    private static final String mysqlScheme = "jdbc:mysql";
    private static final String mysqlDriver = "com.mysql.jdbc.Driver";

    /* ************************
     *         Methods        *
     ************************ */

    /**
     * @see AbstractDataBaseConnection
     */
    public MySQLDatabaseConnection(String mysqlUsername, String mysqlPassword, String mysqlURL) throws SQLException, ClassNotFoundException {
        super(mysqlDriver, mysqlScheme, mysqlURL, mysqlUsername, mysqlPassword);
    }

}
