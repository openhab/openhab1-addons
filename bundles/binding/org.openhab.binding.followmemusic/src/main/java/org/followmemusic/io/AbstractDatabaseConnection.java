package org.followmemusic.io;

import java.sql.*;
import java.util.*;

/**
 * Manage JDBC connection (no dependency with the type of DDB)
 * User: pieraggi
 * Date: 19/03/2014
 * Time: 11:43
 *
 * @author pieraggi
 */
public class AbstractDatabaseConnection {

    /**
     * SQL Request types
     */
    public enum QueryType {
        /**
         * Selection query
         */
        QUERY_TYPE_SELECT,
        /**
         * Update query
         */
        QUERY_TYPE_UPDATE,
        /**
         * Insert query
         */
        QUERY_TYPE_INSERT
    }

    /**
     * Schéma d'url utilisé pour se connecter à la BDD (spécifie le driver à utiliser)
     */
    private String scheme;
    /**
     * URL de Connection (spécifie l'hôte, le port et la Base de donnée à utiliser)
     */
    private String url;
    /**
     * URL complète de connexion
     */
    private String fullURL;
    /**
     * Username
     */
    private String user;
    /**
     * Password
     */
    private String password;

    /**
     * Java Driver class used to connect to the Database through the JDBC
     */
    private String driver;

    /**
     * Instance of the java.sql.Connection object used to connection to the database
     */
    private Connection connection = null;

    /* ************************
     *       Constructors     *
     ************************ */

    /**
     * Constructor
     *
     * @param driver Java Driver class used to connect to the Database through the JDBC
     * @param scheme Scheme use to connection to the database (specify the driver)
     * @param url Connection URL (specify host, port and database)
     * @param user Username
     * @param password Password
     * @throws java.sql.SQLException If the connection to the database fail
     */
    public AbstractDatabaseConnection( String driver,  String scheme,  String url, String user,  String password) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        this.driver = driver;
        this.scheme = scheme;
        this.url = url;
        this.user = user;
        this.password = password;
        this.fullURL = this.scheme+"://"+this.url;
        this.connection = DriverManager.getConnection(fullURL, user, password);
    }

    /* ************************
     *         Methods        *
     ************************ */

    /**
     * Créé un objet PreparedStatement initialisé avec la requête SQL
     *
     * @param query requête SQL
     * @param values paramètres de la requête
     * @param queryType type de la requête
     * @return Statement créé
     * @throws SQLException
     * @throws UnexpectedSqlParameter 
     */
    private PreparedStatement createPreparedStatement( String query, ArrayList<Object> values,  QueryType queryType) throws SQLException, UnexpectedSqlParameter {

        PreparedStatement statement;

        switch (queryType) {
             case QUERY_TYPE_SELECT:
                 statement  = this.connection.prepareStatement(
                         query,
                         ResultSet.TYPE_SCROLL_INSENSITIVE,
                         ResultSet.CONCUR_READ_ONLY
                 );
                 break;

            case QUERY_TYPE_UPDATE:
                statement  = this.connection.prepareStatement(
                        query
                );
                break;

            case QUERY_TYPE_INSERT:
                statement  = this.connection.prepareStatement(
                        query,
                        Statement.RETURN_GENERATED_KEYS
                );
                break;

            default:
                return null;
        }

        if (values != null) {
            int i, size = values.size();

            for (i=1; i<=size; i++) {

                // Get Element
                Object obj = values.get(i-1);

                if (obj instanceof Integer) {
                    statement.setInt(i, ((Integer) obj));
                }
                else if (obj instanceof Float) {
                    statement.setFloat(i, ((Float) obj));
                }
                else if( obj instanceof byte[] ) {
                    statement.setBytes(i, (byte[]) obj);
                }
                else if (obj instanceof String) {
                    statement.setString(i, (String)obj);
                }
                else if (obj instanceof java.util.Date) {
                    statement.setTimestamp(i, new Timestamp(((java.util.Date)obj).getTime()));
                }
                else if (obj instanceof java.util.GregorianCalendar) {

                    // Get date
                    GregorianCalendar calendar = (GregorianCalendar)obj;
                    java.util.Date dt = calendar.getTime();

                    // Format date
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateTime = sdf.format(dt);

                    // Set date
                    statement.setString(i, dateTime);
                }
                else if( obj == null )
                    statement.setNull(i, 0);
                else
                	throw new UnexpectedSqlParameter(obj);

            }
        }

        return statement;
    }

    /**
     * Execute une requête SQL de sélection
     *
     * @param query requête SQL
     * @param values paramètres de la requête
     * @return Table de hachage au format {"result":résultat, "statement":statement ayant effectué la requête}
     * @throws SQLException
     * @throws UnexpectedSqlParameter 
     */
    public Hashtable<String, Object> executeQuery( String query, ArrayList<Object> values) throws SQLException, UnexpectedSqlParameter {

        final PreparedStatement statement = this.createPreparedStatement(query, values, QueryType.QUERY_TYPE_SELECT);
        final ResultSet resultSet = statement.executeQuery();

        return new Hashtable<String, Object>() {{
            put("result", resultSet);
            put("statement", statement);
        }};
    }

    /**
     * Execute une requête SQL de mise à jour
     *
     * @param query requête SQL
     * @param values paramètres de la requête
     * @param isInsertion requête d'insertion ?
     * @return Table de hachage au format {"result":résultat, "statement":statement ayant effectué la requête}
     * @throws SQLException
     * @throws UnexpectedSqlParameter 
     */
    public Hashtable<String, Object> executeUpdate( String query, ArrayList<Object> values, boolean isInsertion) throws SQLException, UnexpectedSqlParameter {

        final PreparedStatement statement;
        if (isInsertion)
            statement = this.createPreparedStatement(query, values, QueryType.QUERY_TYPE_INSERT);
        else
            statement = this.createPreparedStatement(query, values, QueryType.QUERY_TYPE_UPDATE);

        final int result = statement.executeUpdate();

        Hashtable<String, Object> resultMap = new Hashtable<String, Object>() {{
            put("result", result);
            put("statement", statement);
        }};

        if (isInsertion) {

            ResultSet rs = statement.getGeneratedKeys();
            ArrayList<Integer> generatedKeys = new ArrayList<Integer>();

            while (rs.next()) {
                generatedKeys.add(rs.getInt(1));
            }

            resultMap.put("generatedKeys", generatedKeys);

            rs.close();
        }

        return resultMap;
    }

    /**
     * Compte le nombre d'éléments dans une table
     *
     * @param table nom de la table
     * @return Nombre d'éléments
     */
    public int countTable(String table) {

        String queryStr = "SELECT COUNT(*) FROM "+table;

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(queryStr);

            int count = resultSet.getInt("RECORDCOUNT");

            resultSet.close();
            statement.close();

            return count;
        } catch (SQLException e) {
            
        }

        return -1;
    }

    /* ************************
     *    Getters & Setters   *
     ************************ */

    public String getUrl() {
        return url;
    }

    public String getScheme() {
        return scheme;
    }

    public String getFullURL() {
        return fullURL;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public String getDriver() {
        return driver;
    }
}