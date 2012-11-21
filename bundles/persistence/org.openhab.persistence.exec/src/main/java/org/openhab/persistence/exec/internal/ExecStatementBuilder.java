package org.openhab.persistence.exec.internal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Parses an item and alias and generates an Executable command.
 * 
 * @author Henrik Sjöstrand
 * @author Thomas.Eichstaedt-Engelen
 * 
 * @since 1.1.0
 */
public class ExecStatementBuilder {

	private static final Logger logger = 
		LoggerFactory.getLogger(ExecStatementBuilder.class);
	
	private static final Pattern BASE_PATTERN = Pattern.compile(".*?(\\$\\{.*?\\}).*?");


	/**
	 * Parses an item and alias into a executable Exec statement
	 * 
	 * @param item the item
	 * @param alias the Exec string, specified in exec.persist
	 * @param date the System date from which to take the time to log
	 * @return an executable Exec statement
	 */
	public String getStatement(String value, String alias, Date date) {
		String result = "";
		
		Matcher matcher = BASE_PATTERN.matcher(alias);				
		while (matcher.find()) {
			result += matcher.group(0);
			String group = matcher.group(1);
			
			if (group.contains("value")) {
				result = result.replace(group, value);
			} else if (group.contains("date:ms")) {
				result = result.replace(group, "" + date.getTime());
			} else if (group.contains("date")) {
				String type = "";
				try {
					// Trim surrounding quotes and format date
					type = group.substring(8, group.length() - 2) ;
					SimpleDateFormat dateFormat = new SimpleDateFormat(type);
					result = result.replace(group, dateFormat.format(date));
				} catch (Exception e) {
					logger.warn("Could not parse date using format [" + type + "]");
				}
			}
		}
		
		return result;
	}

}
