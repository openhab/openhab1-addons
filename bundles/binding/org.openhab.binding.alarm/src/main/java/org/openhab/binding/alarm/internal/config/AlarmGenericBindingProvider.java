/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarm.internal.config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.alarm.config.AlarmBindingProvider;
import org.openhab.binding.alarm.config.AlarmCondition;
import org.openhab.binding.alarm.config.AlarmCondition.MatchingFunction;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.AlarmState.AlarmClass;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This class can parse information from the generic binding format and provides alarm binding information from it. It
 * registers as a {@link BindingConfigReader} service as well as as a {@link AlarmBindingProvider} service.</p>
 * 
 * 
 * @author Volker Daube
 * @since 1.7.0
 * 
 */
public class AlarmGenericBindingProvider extends AbstractGenericBindingProvider implements AlarmBindingProvider, AutoUpdateBindingProvider {

	/** the binding type to register for as a binding config reader */
	public static final String ALARM_BINDING_TYPE = "alarm";

	private static final Logger sLogger = LoggerFactory.getLogger(AlarmGenericBindingProvider.class);
	/**
	 *  Regular expression for a single alarm condition: example: "eq(1.0,'Alarm Text')" 
	 */
	private static final Pattern sBindingConfigRulePattern=Pattern.compile(buildBindingConfigRulePattern());
	/**
	 *  Regular expression for a list of alarm conditions: example: "lt(10.0,'Cold Alarm'); gt(30.0,'Heat Alarm');"  
	 */
	private static final Pattern sBindConfigSyntaxPattern =Pattern.compile("("+buildBindingConfigRulePattern()+"\\s*\\;?\\s*)+");
	private Map<String, Item> items = new HashMap<String, Item>();

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#getBindingType()
	 */
	public String getBindingType() {
		return ALARM_BINDING_TYPE;
	}

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#validateItemType(org.openhab.core.items.Item, java.lang.String)
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (item instanceof DateTimeItem ) {
			throw new BindingConfigParseException("DateTimeItems are not supported by AlarmBinding");
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.AbstractGenericBindingProvider#processBindingConfiguration(java.lang.String, org.openhab.core.items.Item, java.lang.String)
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {

		super.processBindingConfiguration(context, item, bindingConfig);

		addBindingConfig(item, parseBindingConfigString(item, bindingConfig));
	}

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.AbstractGenericBindingProvider#addBindingConfig(org.openhab.core.items.Item, org.openhab.core.binding.BindingConfig)
	 */
	@Override
	protected void addBindingConfig(Item item, BindingConfig config) {
		items.put(item.getName(), item);
		super.addBindingConfig(item, config);
	}


	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.AbstractGenericBindingProvider#removeConfigurations(java.lang.String)
	 */
	@Override
	public void removeConfigurations(String context) {
		Set<Item> configuredItems = contextMap.get(context);
		if (configuredItems != null) {
			for (Item item : configuredItems) {
				items.remove(item.getName());
			}
		}
		super.removeConfigurations(context);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.alarm.config.AlarmBindingProvider#getItem(java.lang.String)
	 */
	@Override
	public Item getItem(String itemName) {
		return items.get(itemName);
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.autoupdate.AutoUpdateBindingProvider#autoUpdate(java.lang.String)
	 */
	@Override
	public Boolean autoUpdate(String itemName) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.alarm.config.AlarmBindingProvider#getAlarmConditions(java.lang.String)
	 */
	@Override
	public Iterable<AlarmCondition> getAlarmConditions(final String itemName) {
		BindingConfig config = bindingConfigs.get(itemName);

		if(config instanceof AlarmBindingConfig) {
			AlarmBindingConfig alarmBindingConfig = (AlarmBindingConfig) config;
			return alarmBindingConfig.alarmConditions;
		}
		return null;
	}

	/**
	 * This is the main method that takes care of parsing a binding configuration
	 * string for a given item. It returns a collection of {@link AlarmBindingConfig}
	 * instances, which hold all relevant data about the alarm binding of an item.
	 * 
	 * @param item the item for which the binding configuration string is provided
	 * @param bindingConfig a string which holds the binding information
	 * @return a alarm binding config, a collection of {@link AlarmBindingConfigItem} 
	 *   instances, which hold all relevant data about the binding 
	 * @throws BindingConfigParseException if the configuration string has no valid syntax
	 */
	protected AlarmBindingConfig parseBindingConfigString(Item item, String bindingConfig) throws BindingConfigParseException {

		sLogger.debug("Parsing item {}", item.getName());
		
		if (bindingConfig==null) {
			throw new BindingConfigParseException("Alarm config string is null for item: "+item.getName());
		}
		
		AlarmBindingConfig alarmConfig = new AlarmBindingConfig();


		//Check if the binding configuration is valid
		Matcher matcher = sBindConfigSyntaxPattern.matcher(bindingConfig.trim());
		if (!matcher.matches()) {
			throw new BindingConfigParseException("Alarm config string doesn't match syntax: "+bindingConfig+", item: "+item.getName());
		}

		//Extract and iterate over each rule from the configuration string
		matcher = sBindingConfigRulePattern.matcher(bindingConfig.trim());
		String alarmTriggerValueString;
		String alarmDelayString;

		while (matcher.find()) {

			AlarmCondition alarmCondition = new AlarmCondition();
			String op;

			//Replace synonyms. Example == is a synonym for EQ.
			if (matcher.group("op")!=null) {
				// If this is not! a stale rule, then fetch the op group.
				op = matcher.group("op").toUpperCase();
				if (op.equals("==")) { op="EQ"; }
				else if (op.equals("!=")) { op="NE"; } 
				else if (op.equals(">=")) { op="GE"; } 
				else if (op.equals(">")) { op="GT"; } 
				else if (op.equals("<=")) { op="LE"; } 
				else if (op.equals("<")) { op="LT"; } 
			}
			else if (matcher.group("ops")!=null) {
				// If this is a stale rule, then fetch the ops group.
				op = matcher.group("ops").toUpperCase();
			}
			else {
				//This shouldn't occur since we checked the syntax, but ...
				throw new BindingConfigParseException("Unknown function ("+item.getName()+"):  "+matcher.group("op"));
			}

			try {
				alarmCondition.setMatchingFunction(MatchingFunction.valueOf(op));
			}
			catch (IllegalArgumentException e) {
				//This shouldn't occur since we checked the syntax, but ...
				throw new BindingConfigParseException("No function ("+item.getName()+").");
			}

			String alarmClass;
			if (matcher.group("alarmclass")!=null) {
				alarmClass = matcher.group("alarmclass").toUpperCase();
			}
			else if (matcher.group("alarmclassstale")!=null) {
				// If this is a stale rule, then fetch the alarmclass_stale group.
				alarmClass = matcher.group("alarmclassstale").toUpperCase();
			}
			else {
				//AlarmClass is not defined in rule. Use default.
				alarmClass=AlarmClass.HIGH.toString();
			}
			try {
				alarmCondition.setAlarmClass(AlarmClass.valueOf(alarmClass));
			}
			catch (IllegalArgumentException e) {
				//This shouldn't occur since we checked the syntax, but ...
				throw new BindingConfigParseException("No function ("+item.getName()+").");
			}

			//Handle stale rules and regular rules
			if (alarmCondition.getMatchingFunction().equals(MatchingFunction.STALE)) {
				//Get the alarm text
				// If this is a stale rule, then fetch the msgs group.
				alarmCondition.setAlarmText(matcher.group("msgs"));

				//Get the alarm delay,if there is one.
				String staleTimeString=matcher.group("staletime");
				if (staleTimeString!=null) {
					try {
						alarmCondition.setAlarmTimeInSeconds(Integer.parseInt(staleTimeString));
					}
					catch (NumberFormatException e) {
						throw new BindingConfigParseException("Stale time for item ("+item.getName()+") is not a number ("+staleTimeString+")!");
					}
				}
				else {
					throw new BindingConfigParseException("Stale time for item ("+item.getName()+") is mandatory!");
				}
				alarmCondition.setMessageItemName(matcher.group("outitems"));

			}
			else {
				//Handle regular rules

				//Get the alarm value,if there is one. Not available for stale rules.
				alarmTriggerValueString=matcher.group("value");

				//Get the alarm text
				// If this is not a stale rule, then fetch the msg group.
				alarmCondition.setAlarmText(matcher.group("msg"));

				//Get the alarm delay,if there is one.
				alarmDelayString=matcher.group("delay");
				if (alarmDelayString!=null) {
					try {
						alarmCondition.setAlarmTimeInSeconds(Integer.parseInt(alarmDelayString));
					}
					catch (NumberFormatException e) {
						throw new BindingConfigParseException("Alarm delay for item ("+item.getName()+") is not a number ("+alarmDelayString+")!");
					}
				}

				alarmCondition.setMessageItemName(matcher.group("outitem"));

				if (item instanceof ColorItem) {
					if (isNumber(alarmTriggerValueString)) {
						try {
							alarmCondition.setTriggerValue(PercentType.valueOf(alarmTriggerValueString));
						}
						catch (NumberFormatException e1) {
							throw new BindingConfigParseException("ColorItem ("+item.getName()+") percentage value is not parsebale ("+alarmTriggerValueString+")!");
						}
						catch (IllegalArgumentException e) {
							throw new BindingConfigParseException("ColorItem ("+item.getName()+") percentage value must be with between 0 and 100 but was "+alarmTriggerValueString+"!");
						}
					}
					else if (OnOffType.ON.toString().equalsIgnoreCase(alarmTriggerValueString)||OnOffType.OFF.toString().equalsIgnoreCase(alarmTriggerValueString)) {
						if (alarmCondition.getMatchingFunction()==MatchingFunction.EQ||alarmCondition.getMatchingFunction()==MatchingFunction.NE) {
							alarmCondition.setTriggerValue(OnOffType.valueOf(alarmTriggerValueString.toUpperCase()));
						}
						else {
							throw new BindingConfigParseException("ColorItem ("+item.getName()+") only supports ON and OFF with alarm conditions eq() and ne()!");
						}
					}
					else {
						if (alarmTriggerValueString.startsWith("(") && alarmTriggerValueString.endsWith(")")) {
							try {
								alarmCondition.setTriggerValue(new HSBType(alarmTriggerValueString.substring(1, alarmTriggerValueString.length()-1)));
							}
							catch (IllegalArgumentException e) {
								throw new BindingConfigParseException("ColorItem ("+item.getName()+") alarm value is neither a number, ON, OFF nor (h,g,b)!");
							}
						}
					}
				}
				else if (item instanceof ContactItem) {
					//Allows: eq and ne
					if ((alarmCondition.getMatchingFunction()!=MatchingFunction.EQ)&&(alarmCondition.getMatchingFunction()!=MatchingFunction.NE)) {
						throw new BindingConfigParseException("ContactItem ("+item.getName()+") only supports alarm conditions eq and ne!");
					}
					if (OpenClosedType.CLOSED.toString().equalsIgnoreCase(alarmTriggerValueString)||OpenClosedType.OPEN.toString().equalsIgnoreCase(alarmTriggerValueString)) {
						alarmCondition.setTriggerValue(OpenClosedType.valueOf(alarmTriggerValueString.toUpperCase()));
					}
					else {
						throw new BindingConfigParseException("ContactItem ("+item.getName()+") only supports alarm conditions values:"+OpenClosedType.CLOSED+" or "+OpenClosedType.OPEN);
					}
				}
				else if (item instanceof DateTimeItem) {
					//Allows: lt, le, gt, ge, eq and ne
					alarmCondition.setTriggerValue(DateTimeType.valueOf(alarmTriggerValueString.toUpperCase()));
				}
				else if (item instanceof DimmerItem) {
					if (isNumber(alarmTriggerValueString)) {
						try {
							alarmCondition.setTriggerValue(PercentType.valueOf(alarmTriggerValueString));
						}
						catch (NumberFormatException e1) {
							throw new BindingConfigParseException("DimmerItem ("+item.getName()+") percentage value is not parsebale ("+alarmTriggerValueString+")!");
						}
						catch (IllegalArgumentException e) {
							throw new BindingConfigParseException("DimmerItem ("+item.getName()+") percentage value must be with between 0 and 100 but was "+alarmTriggerValueString+"!");
						}
					}
					else if (OnOffType.ON.toString().equalsIgnoreCase(alarmTriggerValueString)||OnOffType.OFF.toString().equalsIgnoreCase(alarmTriggerValueString)) {
						if (alarmCondition.getMatchingFunction()==MatchingFunction.EQ||alarmCondition.getMatchingFunction()==MatchingFunction.NE) {
							alarmCondition.setTriggerValue(OnOffType.valueOf(alarmTriggerValueString.toUpperCase()));
						}
						else {
							throw new BindingConfigParseException("DimmerItem ("+item.getName()+") only supports ON and OFF with alarm conditions eq() and ne()!");
						}
					}
					else {
						throw new BindingConfigParseException("DimmerItem ("+item.getName()+") alarm value '"+alarmTriggerValueString+"' is neither a number nor ON or OFF!");
					}
				}
				else if (item instanceof NumberItem) {
					//Allows: lt, le, gt, ge, eq and ne
					try {
						if (alarmTriggerValueString.contains(",")) {
							alarmTriggerValueString=alarmTriggerValueString.replace(',','.');
						}
						
						alarmCondition.setTriggerValue(DecimalType.valueOf(alarmTriggerValueString));
					}
					catch (NumberFormatException e) {
						throw new BindingConfigParseException("NumberItem ("+item.getName()+") alarm value is illegal: !"+alarmTriggerValueString);
					}
				}
				else if (item instanceof RollershutterItem) {
					if (isNumber(alarmTriggerValueString)) {
						try {
							alarmCondition.setTriggerValue(PercentType.valueOf(alarmTriggerValueString));
						}
						catch (NumberFormatException e1) {
							throw new BindingConfigParseException("RollershutterItem ("+item.getName()+") percentage value is not parsebale ("+alarmTriggerValueString+")!");
						}
						catch (IllegalArgumentException e) {
							throw new BindingConfigParseException("RollershutterItem ("+item.getName()+") percentage value must be with between 0 and 100 but was "+alarmTriggerValueString+"!");
						}
					}
					else if (UpDownType.UP.toString().equalsIgnoreCase(alarmTriggerValueString)||UpDownType.DOWN.toString().equalsIgnoreCase(alarmTriggerValueString)) {
						if (alarmCondition.getMatchingFunction()==MatchingFunction.EQ||alarmCondition.getMatchingFunction()==MatchingFunction.NE) {
							alarmCondition.setTriggerValue(UpDownType.valueOf(alarmTriggerValueString.toUpperCase()));
						}
						else {
							throw new BindingConfigParseException("RollershutterItem ("+item.getName()+") only supports UP and DOWN with alarm conditions eq() and ne()!");
						}
					}
					else {
						throw new BindingConfigParseException("RollershutterItem ("+item.getName()+") alarm value is neither a number nor UP or DOWN, but "+alarmTriggerValueString+"!");
					}
				}
				else if (item instanceof StringItem) {
					//Allows: eq and ne
					if ((alarmCondition.getMatchingFunction()!=MatchingFunction.EQ)&&(alarmCondition.getMatchingFunction()!=MatchingFunction.NE)) {
						throw new BindingConfigParseException("StringItem ("+item.getName()+") only supports alarm conditions eq andimplements BindingConfig ne!");
					}
					if (alarmTriggerValueString.startsWith("'") && alarmTriggerValueString.endsWith("'")) {
						try {
							alarmCondition.setTriggerValue(new StringType(alarmTriggerValueString.substring(1, alarmTriggerValueString.length()-1)));
						}
						catch (IllegalArgumentException e) {
							throw new BindingConfigParseException("StringItem ("+item.getName()+") alarm value is not parseable: "+alarmTriggerValueString+"!");
						}
					}
				}
				else if (item instanceof SwitchItem) {
					//Allows: eq and ne
					if ((alarmCondition.getMatchingFunction()!=MatchingFunction.EQ)&&(alarmCondition.getMatchingFunction()!=MatchingFunction.NE)) {
						throw new BindingConfigParseException("SwitchItem ("+item.getName()+") only supports alarm conditions eq and ne!");
					}
					if (OnOffType.ON.toString().equalsIgnoreCase(alarmTriggerValueString)||OnOffType.OFF.toString().equalsIgnoreCase(alarmTriggerValueString)) {
						alarmCondition.setTriggerValue(OnOffType.valueOf(alarmTriggerValueString.toUpperCase()));
					}
					else {
						throw new BindingConfigParseException("SwitchItem ("+item.getName()+") only supports alarm conditions values:"+OnOffType.ON+" or "+OnOffType.OFF
								+ " and not "+alarmTriggerValueString);
					}
				}
				else {
					throw new BindingConfigParseException("Alarm config: unknown item: "+item.getName());
				}
			}

			alarmConfig.itemName = item.getName();
			alarmConfig.alarmConditions.add(alarmCondition);
		}
		sLogger.debug("Got alarm config {}", alarmConfig);
		return alarmConfig;
	}

	protected boolean isNumber(String value) {
		return isInt(value)|isFloat(value);
	}
	protected boolean isFloat(String value) {
		return value.matches("-?\\d+([\\.,]\\d+)+");
	}

	protected boolean isInt(String value) {
		return value.matches("-?\\d+");
	}

	/**
	 * Creates a String with a regular expression describing this binding's configuration syntax.
	 *  
	 * 
	 * @return a String with a regular expression describing this binding's configuration syntax 
	 */
	private static String buildBindingConfigRulePattern() {
		// 
		String regexOpPart="(?<op>eq|==|ne|!=|lt|\\<|le|\\<=|gt|\\>|ge|\\>=)";
		String regexIntPart="-?\\d+?";
		String regexFloatPart="-?\\d+?(\\.|\\,)\\d+?";
		String regexStringPart="\\'([^\\']*?)\\'";
		//		String regexDatePart="\\d{4}-\\d{2}-\\d{2}T\\d{2}\\:\\d{2}\\:\\d{2}";
		//		String regexHSBValuePart="\\(\\s*\\d+?\\s*\\,\\s*\\d+?\\s*\\,\\s*\\d+?\\s*\\)";
		//Mandatory parameters
		String regexValuePart="(?<value>"+regexIntPart+"|"+regexFloatPart+"|"+regexStringPart+"|on|off|open|closed|up|down)";//+regexHSBValuePart+"|"+regexDatePart+")";
		String regexAlarmTextPart="(\\,\\s*\\'(?<msg>[^\\']+?)\\')";
		//Optional parameters
		String regexAlarmDelayTimePart="(delay\\s*=\\s*(?<delay>\\d+?))";

		String regexAlarmOutputItemPart="(outitem\\s*=\\s*(?<outitem>[^\\)]+?))";
		String regexAlarmClassPart="(class\\s*=\\s*(?<alarmclass>high|medium|low))";
		
		String regexOptionalParams="\\s*\\,\\s*("+regexAlarmClassPart+"|"+regexAlarmDelayTimePart+"|"+regexAlarmOutputItemPart+")\\s*";
		String regexRule=regexOpPart+"\\s*\\(\\s*"+regexValuePart+"\\s*"+regexAlarmTextPart+"\\s*("+regexOptionalParams+")*?\\s*\\)";

		String regexOpPartStale="(?<ops>stale)";
		String regexAlarmTextPartStale="\\'(?<msgs>[^\\']+?)\\'";
		String regexAlarmStaleTimePart="\\,\\s*period\\s*=\\s*(?<staletime>\\d+?)";
		
		String regexAlarmOutputItemPartStale="(outitem\\s*=\\s*(?<outitems>[^\\)]+?))";
		String regexAlarmClassPartStale="(class\\s*=\\s*(?<alarmclassstale>high|medium|low))";

		String regexOptionalParamsStale="\\s*\\,\\s*("+regexAlarmClassPartStale+"|"+regexAlarmOutputItemPartStale+")\\s*";

		String regexStale=regexOpPartStale+"\\s*\\(\\s*"+regexAlarmTextPartStale+"\\s*("+regexAlarmStaleTimePart+")\\s*("+regexOptionalParamsStale+")*?\\s*\\)";

		return "(?i)(?<rule>"+regexStale+"|"+regexRule+")";
	}

	/**
	 * This is an internal data structure to store information from the binding config strings and use it to answer the
	 * requests to the Alarm binding provider.
	 * 
	 * @author Volker Daube
	 * 
	 */
	/* default */ static class AlarmBindingConfig implements BindingConfig {
		public String itemName;
		public List<AlarmCondition> alarmConditions = new LinkedList<AlarmCondition>();

		public String toString() {
			String result="AlarmBindingConfig (" +
					"Item name=" + itemName;
			for (AlarmCondition ac : alarmConditions) {
				result+=", "+ac.toString();
			}
			result+=")";
			return  result;
		}
	}

}
