package org.openhab.persistence.jdbc.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class StringUtilsExt {
    private static final Logger logger = LoggerFactory.getLogger(StringUtilsExt.class);

    /**
     * Replaces multiple found words with the given Array contents
     * 
     * @param str - String for replacement
     * @param separate - A String or Array to be replaced
     * @param separators - Array will be merged to str
     * @return
     */
    public static final String replaceArrayMerge(String str, String separate, Object[] separators) {
        for (int i = 0; i < separators.length; i++) {
            str = str.replaceFirst(separate, (String) separators[i]);
        }
        return str;
    }

    /**
     * @see #replaceArrayMerge(String str, String separate, Object[] separators)
     */
    public static final String replaceArrayMerge(String str, String[] separate, String[] separators) {
        for (int i = 0; i < separators.length; i++) {
            str = str.replaceFirst(separate[i], separators[i]);
        }
        return str;
    }

    /**
     * @see #parseJdbcURL(String url, Properties def)
     */
    public static Properties parseJdbcURL(String url) {
        return parseJdbcURL(url, null);
    }

    /**
     * <b>JDBC-URI Examples:</b><br/>
     * jdbc:dbShortcut:c:/dev/databaseName<br/>
     * jdbc:dbShortcut:scheme:c:/dev/databaseName<br/>
     * jdbc:dbShortcut:scheme:c:\\dev\\databaseName<br/>
     * jdbc:dbShortcut:./databaseName<br/>
     * jdbc:dbShortcut:/databaseName<br/>
     * jdbc:dbShortcut:~/databaseName<br/>
     * jdbc:dbShortcut:/path/databaseName.db<br/>
     * jdbc:dbShortcut:./../../path/databaseName<br/>
     * jdbc:dbShortcut:scheme:./path/../path/databaseName;param1=true;<br/>
     * jdbc:dbShortcut://192.168.0.145:3306/databaseName?param1=false&param2=true
     * <p/>
     *
     * @param url - JDBC-URI
     * @param def - Predefined Properties Object
     * @return A merged Properties Object may contain:<br/>
     *         parseValid (mandatory)<br/>
     *         scheme<br/>
     *         serverPath<br/>
     *         dbShortcut<br/>
     *         databaseName<br/>
     *         portNumber<br/>
     *         serverName<br/>
     *         pathQuery<br/>
     */
    public static Properties parseJdbcURL(String url, Properties def) {

        Properties props;
        if (def == null) {
            props = new Properties();
        } else {
            props = new Properties(def);
        }

        if (url == null || url.length() < 9) {
            return props;
        }

        // replace all \
        if (url.contains("\\"))
            url = url.replaceAll("\\\\", "/");

        // replace first ; with ?
        if (url.contains(";")) {
            // replace first ; with ?
            url = url.replaceFirst(";", "?");
            // replace other ; with &
            url = url.replaceAll(";", "&");
        }

        if (url.split(":").length < 3 || url.indexOf("/") == -1) {
            logger.error("parseJdbcURL: URI '{}' is not well formated, expected uri like 'jdbc:dbShortcut:/path'", url);
            props.put("parseValid", "false");
            return props;
        }

        String[] protAndDb = stringBeforeSubstr(url, ":", 1).split(":");
        if (!"jdbc".equals(protAndDb[0])) {
            logger.error("parseJdbcURL: URI '{}' is not well formated, expected suffix 'jdbc' found '{}'", url,
                    protAndDb[0]);
            props.put("parseValid", "false");
            return props;
        }
        props.put("parseValid", "true");
        props.put("dbShortcut", protAndDb[1]);

        URI dbURI = null;
        try {
            dbURI = new URI(stringAfterSubstr(url, ":", 1));
            if (dbURI.getScheme() != null) {
                props.put("scheme", dbURI.getScheme());
                dbURI = new URI(stringAfterSubstr(url, ":", 2));
            }
        } catch (URISyntaxException e) {
            logger.error("parseJdbcURL: URI '{}' is not well formated URISyntaxException: {}", url, e);
            return props;
        }

        // Query-Parameters
        if (dbURI.getQuery() != null) {
            String[] q = dbURI.getQuery().split("&");
            for (int i = 0; i < q.length; i++) {
                String[] t = q[i].split("=");
                props.put(t[0], t[1]);
            }
            props.put("pathQuery", dbURI.getQuery());
        }

        String path = "";
        if (dbURI.getPath() != null) {
            String gp = dbURI.getPath();
            String st = "/";
            if (gp.indexOf("/") <= 1) {
                if (substrPos(gp, st).size() > 1) {
                    path = stringBeforeLastSubstr(gp, st) + st;
                } else {
                    path = stringBeforeSubstr(gp, st) + st;
                }
            }
            if (dbURI.getScheme() != null && dbURI.getScheme().length() == 1)
                path = dbURI.getScheme() + ":" + path;
            props.put("serverPath", path);
        }
        if (dbURI.getPath() != null)
            props.put("databaseName", stringAfterLastSubstr(dbURI.getPath(), "/"));
        if (dbURI.getPort() != -1)
            props.put("portNumber", dbURI.getPort() + "");
        if (dbURI.getHost() != null)
            props.put("serverName", dbURI.getHost());

        return props;
    }

    /**
     * @param s
     * @param substr
     * @return - Returns a String before the last occurrence of a Substring
     */
    public static String stringBeforeLastSubstr(String s, String substr) {
        ArrayList<Integer> a = substrPos(s, substr);
        return s.substring(0, a.get(a.size() - 1));
    }

    /**
     * @param s
     * @param substr
     * @return - Returns a String after the last occurrence of a Substring
     */
    public static String stringAfterLastSubstr(String s, String substr) {
        ArrayList<Integer> a = substrPos(s, substr);
        return s.substring(a.get(a.size() - 1) + 1);
    }

    /**
     * @param s
     * @param substr
     * @return - Returns a String after the first occurrence of a Substring
     */
    public static String stringAfterSubstr(String s, String substr) {
        return s.substring(s.indexOf(substr) + 1);
    }

    /**
     * @param s
     * @param substr
     * @param pos
     * @return - Returns a String after the n occurrence of a Substring
     */
    public static String stringAfterSubstr(String s, String substr, int n) {
        return s.substring(substrPos(s, substr).get(n) + 1);
    }

    /**
     * @param s
     * @param substr
     * @return - Returns a String before the first occurrence of a Substring
     */
    public static String stringBeforeSubstr(String s, String substr) {
        return s.substring(0, s.indexOf(substr));
    }

    /**
     * @param s
     * @param substr
     * @param pos
     * @return - Returns a String before the n occurrence of a Substring
     */
    public static String stringBeforeSubstr(String s, String substr, int n) {
        return s.substring(0, substrPos(s, substr).get(n));
    }

    /**
     * @param s
     * @param substr
     * @return - Returns an ArrayList with Indices of the occurrence of a Substring
     */
    public static ArrayList<Integer> substrPos(String s, String substr) {
        return substrPos(s, substr, true);
    }

    /**
     * @param s
     * @param substr
     * @param ignoreCase
     * @return - Returns an ArrayList with Indices of the occurrence of a Substring
     */
    public static ArrayList<Integer> substrPos(String s, String substr, boolean ignoreCase) {
        int substrLength = substr.length();
        int strLength = s.length();
        ArrayList<Integer> arr = new ArrayList<Integer>();

        for (int i = 0; i < strLength - substrLength + 1; i++) {
            if (s.regionMatches(ignoreCase, i, substr, 0, substrLength)) {
                arr.add(i);
            }
        }
        return arr;
    }

}