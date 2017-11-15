/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.cardio2e.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.cardio2e.Cardio2eBindingProvider;
import org.openhab.binding.cardio2e.internal.code.Cardio2eCurtainTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eDateTimeTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eHvacControlTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eHvacSystemModes;
import org.openhab.binding.cardio2e.internal.code.Cardio2eHvacTemperatureTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eLightingTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eObjectTypes;
import org.openhab.binding.cardio2e.internal.code.Cardio2eRelayTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eScenarioTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eSecurityTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eZoneStates;
import org.openhab.binding.cardio2e.internal.code.Cardio2eZonesBypassTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eZonesMask;
import org.openhab.binding.cardio2e.internal.code.Cardio2eZonesTransaction;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @since 1.11.0
 */
public class Cardio2eGenericBindingProvider extends
		AbstractGenericBindingProvider implements Cardio2eBindingProvider {

	private Map<String, Item> items = new HashMap<String, Item>();
	/** the binding type to register for as a binding config reader */
	public static final String CARDIO2E_BINDING_TYPE = "c2e";

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return CARDIO2E_BINDING_TYPE;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem
				|| item instanceof RollershutterItem
				|| item instanceof ContactItem || item instanceof NumberItem || item instanceof DateTimeItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Switch, Dimmer, Rollershutter, Contact, Number and DateTime items are allowed - please check your *.items configuration");
		}
	}

	/**
	 * This is the main method that takes care of parsing a binding
	 * configuration string for a given item. It returns a collection of
	 * {@link BindingConfig} instances, which hold all relevant data about the
	 * binding to Cardio2e of an item.
	 * 
	 * @param item
	 *            the item for which the binding configuration string is
	 *            provided
	 * @param bindingConfig
	 *            a string which holds the binding information
	 * @return a cardio2e binding config, a collection of
	 *         {@link Cardio2eBindingConfigItem} instances, which hold all
	 *         relevant data about the binding
	 * @throws BindingConfigParseException
	 *             if the configuration string has no valid syntax
	 */
	protected LinkedList<Cardio2eBindingConfigItem> parseBindingConfigString(
			Item item, String bindingConfig) throws BindingConfigParseException {
		LinkedList<Cardio2eBindingConfigItem> configItems = new LinkedList<Cardio2eBindingConfigItem>();
		Cardio2eObjectTypes objectType = null;
		boolean reverseMode;
		String[] parameters = bindingConfig.trim().split(",");
		if (parameters.length > 0) { // Minimum parameters count must be one
			if ((parameters[0].length() > 1) && (parameters[0].startsWith("!"))) {
				reverseMode = true;
				parameters[0] = parameters[0].substring(1);
			} else {
				reverseMode = false;
			}
			objectType = Cardio2eObjectTypes.fromString(parameters[0]);
			if (objectType != null) {
				switch (objectType) {
				case LIGHTING:
					if (parameters.length == 2) {
						if ((item.getAcceptedDataTypes()
								.contains(PercentType.class))
								|| (item.getAcceptedDataTypes()
										.contains(OnOffType.class))) {
							Cardio2eLightingTransaction lightingTransaction = new Cardio2eLightingTransaction();
							try {
								parameters[1] = parameters[1].trim();
								if (parameters[1].length() > 1) {
									if (parameters[1].startsWith("%")) { //
										lightingTransaction.lightIntensityCorrection = true;
										parameters[1] = parameters[1]
												.substring(1);
									}
								}
								lightingTransaction.setObjectNumber(Short
										.parseShort(parameters[1]));
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).reverseMode = reverseMode;
								configItems.get(0).transaction = lightingTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Can not accept value '"
												+ parameters[1]
												+ "' for LIGHTING object number.");
							}
						} else {
							throw new BindingConfigParseException(
									"LIGHTING object type must be associate to a on/off or percentage (dimming) item.");
						}
					} else {
						throw new BindingConfigParseException(
								"LIGHTING config just needs two parameters: object type (LIGHTING) and object number.");
					}
					break;
				case RELAY:
					if (parameters.length == 2) {
						if (item.getAcceptedDataTypes().contains(
								OnOffType.class)) {
							Cardio2eRelayTransaction relayTransaction = new Cardio2eRelayTransaction();
							try {
								parameters[1] = parameters[1].trim();
								relayTransaction.setObjectNumber(Short
										.parseShort(parameters[1]));
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).reverseMode = reverseMode;
								configItems.get(0).transaction = relayTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Can not accept value '"
												+ parameters[1]
												+ "' for RELAY object number.");
							}
						} else {
							throw new BindingConfigParseException(
									"One RELAY object type must be associate to a Switch item.");
						}
					} else if (parameters.length == 3) {
						if ((item.getAcceptedDataTypes()
								.contains(UpDownType.class))
								|| (item.getAcceptedDataTypes()
										.contains(StopMoveType.class))) {
							Cardio2eRelayTransaction relayTransactionUp = new Cardio2eRelayTransaction();
							Cardio2eRelayTransaction relayTransactionDown = new Cardio2eRelayTransaction();
							try {
								parameters[1] = parameters[1].trim();
								parameters[2] = parameters[2].trim();
								relayTransactionUp.setObjectNumber(Short
										.parseShort(parameters[1]));
								relayTransactionDown.setObjectNumber(Short
										.parseShort(parameters[2]));
								relayTransactionUp.setBlindUpMode(true);
								relayTransactionDown.setBlindDownMode(true);
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).reverseMode = reverseMode;
								configItems.get(0).transaction = relayTransactionUp;
								configItems.add(1,
										new Cardio2eBindingConfigItem());
								configItems.get(1).reverseMode = reverseMode;
								configItems.get(1).transaction = relayTransactionDown;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Can not accept values '"
												+ parameters[1]
												+ "' or '"
												+ parameters[2]
												+ "' for a pair of RELAY object number.");
							}
						} else {
							throw new BindingConfigParseException(
									"A pair of RELAY object type must be associate to RollerShutter item.");
						}
					} else {
						throw new BindingConfigParseException(
								"RELAY config needs two parameters (object type 'RELAY' and object number) or three parameters (object type 'RELAY', shutter up object number and shutter down object number.");
					}
					break;
				case HVAC_TEMPERATURE:
					if (parameters.length == 2) {
						if (item.getAcceptedDataTypes().contains(
								DecimalType.class)) {
							Cardio2eHvacTemperatureTransaction hvacTemperatureTransaction = new Cardio2eHvacTemperatureTransaction();
							try {
								parameters[1] = parameters[1].trim();
								hvacTemperatureTransaction
										.setObjectNumber(Short
												.parseShort(parameters[1]));
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).transaction = hvacTemperatureTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Can not accept value '"
												+ parameters[1]
												+ "' for HVAC_TEMPERATURE object number.");
							}
						} else {
							throw new BindingConfigParseException(
									"HVAC_TEMPERATURE object type must be associate to a Number item.");
						}
					} else {
						throw new BindingConfigParseException(
								"HVAC_TEMPERATURE config just needs two parameters: object type (HVAC_TEMPERATURE) and object number.");
					}
					break;
				case HVAC_CONTROL:
					if (parameters.length == 2) {
						// Accepts ON/OFF to switch last HVAC system mode (by
						// default AUTO, if last HVAC system mode is unknown).
						// Accepts Number for compatible 8 bit KNX DPT 20.105
						// HVAC control values (only AUTO/COOLING/HEATING/OFF
						// HVAC system modes allowed) .
						if ((item.getAcceptedDataTypes()
								.contains(OnOffType.class))
								|| (item.getAcceptedDataTypes()
										.contains(DecimalType.class))) {
							Cardio2eHvacControlTransaction hvacControlTransaction = new Cardio2eHvacControlTransaction();
							try {
								parameters[1] = parameters[1].trim();
								hvacControlTransaction.setObjectNumber(Short
										.parseShort(parameters[1]));
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).transaction = hvacControlTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Can not accept value '"
												+ parameters[1]
												+ "' for HVAC_CONTROL object number.");
							}
						} else {
							throw new BindingConfigParseException(
									"HVAC_CONTROL object type must be associate to a Switch or a Number item.");
						}
					} else if (parameters.length == 3) {
						// Accepts ON/OFF to switch specified single HVAC system
						// mode. Accepts Number for specified COOLING or HEATING
						// SetPoint.
						if ((item.getAcceptedDataTypes()
								.contains(OnOffType.class))
								|| (item.getAcceptedDataTypes()
										.contains(DecimalType.class))) {
							Cardio2eHvacControlTransaction hvacControlTransaction = new Cardio2eHvacControlTransaction();
							try {
								parameters[1] = parameters[1].trim();
								parameters[2] = parameters[2].trim();
								if (parameters[2].toUpperCase().equals("FAN"))
									parameters[2] = "OFF"; // "FAN" will be an
															// alias of "OFF"
															// (value for single
															// FAN HVAC control
															// on/off)
								hvacControlTransaction.setObjectNumber(Short
										.parseShort(parameters[1]));
								hvacControlTransaction.singleHvacSystemMode = Cardio2eHvacSystemModes
										.fromString(parameters[2]);
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).transaction = hvacControlTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Can not accept value '"
												+ parameters[1]
												+ "' for HVAC_CONTROL object number.");
							}
						} else {
							throw new BindingConfigParseException(
									"HVAC_CONTROL object type must be associate to a Switch or a Number item.");
						}
					} else {
						throw new BindingConfigParseException(
								"HVAC_CONTROL config needs two parameters (object type 'HVAC_CONTROL' and object number) or three parameters (object type 'HVAC_CONTROL', object number, and single HVAC system mode");
					}
					break;

				case ZONES:
					if (parameters.length == 3) {
						if ((item.getAcceptedDataTypes()
								.contains(OpenClosedType.class))
								|| (item.getAcceptedDataTypes()
										.contains(OnOffType.class))) {
							Cardio2eZonesTransaction zonesTransaction = new Cardio2eZonesTransaction();
							try {
								parameters[1] = parameters[1].trim();
								parameters[2] = parameters[2].trim();
								Cardio2eZonesMask zonesMask = new Cardio2eZonesMask();
								zonesMask.setZoneMask(Byte
										.parseByte(parameters[1]),
										Cardio2eZoneStates
												.fromString(parameters[2]));
								zonesTransaction.setObjectNumber(Short
										.parseShort(parameters[1]));
								zonesTransaction.setZonesMask(zonesMask);
								zonesTransaction.invertZoneDetection = reverseMode;
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).reverseMode = reverseMode;
								configItems.get(0).transaction = zonesTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Can not accept value '"
												+ parameters[1] + "' or '"
												+ parameters[2]
												+ "' for ZONES object.");
							}
						} else {
							throw new BindingConfigParseException(
									"ZONES object type must be associate to a Contact or Switch item.");
						}
					} else {
						throw new BindingConfigParseException(
								"ZONES config just needs two parameters: object type (ZONES) and object number.");
					}
					break;
				case ZONES_BYPASS:
					if (parameters.length == 2) {
						if (item.getAcceptedDataTypes().contains(
								OnOffType.class)) {
							Cardio2eZonesBypassTransaction zonesBypassTransaction = new Cardio2eZonesBypassTransaction();
							try {
								parameters[1] = parameters[1].trim();
								zonesBypassTransaction.setObjectNumber(Short
										.parseShort(parameters[1]));
								zonesBypassTransaction.invertZoneBypassBooleanStates = reverseMode;
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).transaction = zonesBypassTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Can not accept value '"
												+ parameters[1]
												+ "' for ZONES_BYPASS object.");
							}
						} else {
							throw new BindingConfigParseException(
									"ZONES_BYPASS object type must be associate to a Contact or Switch item.");
						}
					} else {
						throw new BindingConfigParseException(
								"ZONES_BYPASS config just needs two parameters: object type (ZONES_BYPASS) and object number.");
					}
					break;
				case SECURITY:
					if (parameters.length == 1) {
						if ((item.getAcceptedDataTypes()
								.contains(OnOffType.class))
								|| (item.getAcceptedDataTypes()
										.contains(DecimalType.class))) {
							Cardio2eSecurityTransaction securityTransaction = new Cardio2eSecurityTransaction();
							try {
								securityTransaction.setObjectNumber((short) 1);
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).transaction = securityTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Error processing SECURITY config.");
							}
						} else {
							throw new BindingConfigParseException(
									"SECURITY object type must be associate to a Switch item.");
						}
					} else {
						throw new BindingConfigParseException(
								"SECURITY config only accepts one parameter (object type 'SECURITY')");
					}
					break;
				case SCENARIO:
					if (parameters.length == 1) {
						if (item.getAcceptedDataTypes().contains(
								DecimalType.class)) {
							Cardio2eScenarioTransaction scenarioTransaction = new Cardio2eScenarioTransaction();
							try {
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).transaction = scenarioTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Error processing SCENARIO config.");
							}
						} else {
							throw new BindingConfigParseException(
									"SCENARIO object type must be associate to a Number item.");
						}
					} else {
						throw new BindingConfigParseException(
								"SCENARIO config only accepts one parameter (object type 'SCENARIO')");
					}
					break;
				case DATE_AND_TIME:
					if (parameters.length == 1) {
						if (item.getAcceptedDataTypes().contains(
								DateTimeType.class)) {
							Cardio2eDateTimeTransaction dateTimeTransaction = new Cardio2eDateTimeTransaction();
							try {
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).transaction = dateTimeTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Error processing DATE_AND_TIME config.");
							}
						} else {
							throw new BindingConfigParseException(
									"DATE_AND_TIME object type must be associate to a DateTime item.");
						}
					} else {
						throw new BindingConfigParseException(
								"DATE_AND_TIME config only accepts one parameter (object type 'DATE_AND_TIME')");
					}
					break;
				case CURTAIN:
					if (parameters.length == 2) {
						if ((item.getAcceptedDataTypes()
								.contains(PercentType.class))
								|| ((item.getAcceptedDataTypes()
										.contains(UpDownType.class)) && (!reverseMode))) {
							Cardio2eCurtainTransaction curtainTransaction = new Cardio2eCurtainTransaction();
							try {
								parameters[1] = parameters[1].trim();
								curtainTransaction.setObjectNumber(Short
										.parseShort(parameters[1]));
								configItems.add(0,
										new Cardio2eBindingConfigItem());
								configItems.get(0).reverseMode = reverseMode;
								configItems.get(0).transaction = curtainTransaction;
							} catch (IllegalArgumentException e1) {
								throw new BindingConfigParseException(
										"Can not accept value '"
												+ parameters[1]
												+ "' for CURTAIN object number.");
							}
						} else {
							throw new BindingConfigParseException(
									"CURTAIN object type must be associate to a RollerShutter (up/down) or Dimmer (percentage) item.");
						}
					} else {
						throw new BindingConfigParseException(
								"CURTAIN config just needs two parameters: object type (CURTAIN) and object number.");
					}
					break;
				default:
					throw new BindingConfigParseException(
							"Unsupported object type '" + parameters[0] + "'.");
				}
			} else {
				throw new BindingConfigParseException("Invalid object type '"
						+ parameters[0] + "'.");
			}
		} else {
			throw new BindingConfigParseException(
					"At least a parameter must be specified.");
		}
		return configItems;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		Cardio2eBindingConfig config = getConfig(item.getName());
		if (config == null) {
			config = new Cardio2eBindingConfig();
			addBindingConfig(item, config);
		}
		LinkedList<Cardio2eBindingConfigItem> configItems = parseBindingConfigString(
				item, bindingConfig);
		for (Cardio2eBindingConfigItem configItem : configItems) {
			config.add(configItem);
		}
	}

	@Override
	protected void addBindingConfig(Item item, BindingConfig config) {
		items.put(item.getName(), item);
		super.addBindingConfig(item, config);
	}

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

	@Override
	public Item getItem(String itemName) {
		return items.get(itemName);
	}

	@Override
	public Cardio2eBindingConfig getConfig(String itemName) {
		return (Cardio2eBindingConfig) bindingConfigs.get(itemName);
	}

	public Cardio2eBindingConfig getReverseOrderConfig(String itemName) {
		Cardio2eBindingConfig reverseOrderConfig = (Cardio2eBindingConfig) getConfig(
				itemName).clone();
		Collections.reverse(reverseOrderConfig);
		return reverseOrderConfig;
	}

	public Map<String, BindingConfig> getMatchBindingConfigs(
			Cardio2eTransaction transaction) { // Returns a Map that
												// contains only the
												// BindingConfigs with
												// Cardio2eTransaction like
												// "transaction" and their
												// IntemNames
		Map<String, BindingConfig> matchBindingConfigs = new HashMap<String, BindingConfig>();
		if (!(bindingConfigs.isEmpty())) {
			for (String itemName : bindingConfigs.keySet()) {
				Cardio2eBindingConfig storedBindingConfig = getConfig(itemName);
				Cardio2eBindingConfig matchBindingConfig = new Cardio2eBindingConfig();
				for (Cardio2eBindingConfigItem configItem : storedBindingConfig) {
					if (configItem.transaction.isLike(transaction)) {
						matchBindingConfig.add(configItem);
					}
				}
				if (!matchBindingConfig.isEmpty()) {
					matchBindingConfigs.put(itemName, matchBindingConfig);
				}
			}
		}
		return matchBindingConfigs;
	}

	public class Cardio2eBindingConfig extends
			LinkedList<Cardio2eBindingConfigItem> implements BindingConfig {
		private static final long serialVersionUID = 1L;
	}

	public class Cardio2eBindingConfigItem {
		private static final long MILLIS_UNTIL_DISCARD_PENDING_UPDATES = 5000; // Milliseconds
																				// to
																				// wait
																				// until
																				// discard
																				// pending
																				// updates
		public Cardio2eTransaction transaction; // The Cardio2eTransaction
												// object for send data to
												// Cardio 2è
		public boolean reverseMode = false; // Set reverseMode = true to enable
											// Cardio 2è control only (updates
											// will be sent to Cardio 2è
											// instead commands, and Cardio 2è
											// received updates will be sent as
											// commands to openHab bus instead
											// states)
		public long lastStateSentTimestamp; // Used for store a timestamp of the
											// last state sent
		private int pendingUpdates = 0; // Used for prevent updates sent to
										// Cardio 2è causes command sending
										// back to bus when reverseMode = true
		private long lastPendingUpdateSet;

		public int getPendingUpdates() {
			if ((System.currentTimeMillis() - lastPendingUpdateSet) >= MILLIS_UNTIL_DISCARD_PENDING_UPDATES)
				setPendingUpdates(0); // Discard pending updates if is more or
										// equal than
										// MILLIS_UNTIL_DISCARD_PENDING_UPDATES
										// milliseconds old
			return pendingUpdates;
		}

		public void setPendingUpdates(int pendingUpdates) {
			lastPendingUpdateSet = System.currentTimeMillis();
			this.pendingUpdates = pendingUpdates;
		}
	}
}