/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.cardio2e.internal;

import java.util.Calendar;
import org.openhab.binding.cardio2e.internal.code.Cardio2eCurtainTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eDateTime;
import org.openhab.binding.cardio2e.internal.code.Cardio2eDateTimeTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eHvacControlTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eHvacTemperatureTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eLightingTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eRelayTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eScenarioTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eSecurityTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eTransactionTypes;
import org.openhab.binding.cardio2e.internal.code.Cardio2eZonesBypassTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eZonesTransaction;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * This class is responsible for parsing the Cardio 2é transactions.
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @since 1.11.0
 */
class Cardio2eTransactionParser {

	protected static boolean filterUnnecessaryCommand;
	protected static boolean filterUnnecessaryReverseModeUpdate;

	private static final String ON_OFF_TYPE_STRING = "OnOffType";
	private static final String PERCENT_TYPE_STRING = "PercentType";
	private static final String INCREASE_DECREASE_TYPE_STRING = "IncreaseDecreaseType";
	private static final String UP_DOWN_TYPE_STRING = "UpDownType";

	static void commandToTransaction(Command command,
			Cardio2eTransaction transaction) {
		commandToTransaction(command, transaction, null);
	}

	static void commandToTransaction(Command command,
			Cardio2eTransaction transaction, String securityCode) {
		transaction.mustBeSent = false;
		switch (transaction.getObjectType()) {
		case LIGHTING:
			Cardio2eLightingTransaction lightingTransaction = (Cardio2eLightingTransaction) transaction;
			byte initialLightIntensityValue = lightingTransaction
					.getLightIntensity();
			switch (command.getClass().getSimpleName()) {
			case ON_OFF_TYPE_STRING:
				lightingTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				if ((OnOffType) command == OnOffType.ON) {
					lightingTransaction.setLightPowered(true);
				} else if ((OnOffType) command == OnOffType.OFF) {
					lightingTransaction.setLightPowered(false);
				} else
					throw new IllegalArgumentException("invalid command '"
							+ command + "'");
				break;
			case INCREASE_DECREASE_TYPE_STRING:
				lightingTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				if ((IncreaseDecreaseType) command == IncreaseDecreaseType.INCREASE) {
					lightingTransaction
							.setLightIntensity(byteStep(
									lightingTransaction.getLightIntensity(),
									10,
									Cardio2eTransaction.CARDIO2E_MIN_LIGHTING_INTENSITY,
									Cardio2eTransaction.CARDIO2E_MAX_LIGHTING_INTENSITY));
				} else if ((IncreaseDecreaseType) command == IncreaseDecreaseType.DECREASE) {
					lightingTransaction
							.setLightIntensity(byteStep(
									lightingTransaction.getLightIntensity(),
									-10,
									Cardio2eTransaction.CARDIO2E_MIN_LIGHTING_INTENSITY,
									Cardio2eTransaction.CARDIO2E_MAX_LIGHTING_INTENSITY));
				} else
					throw new IllegalArgumentException("invalid command '"
							+ command + "'");
				break;
			case PERCENT_TYPE_STRING:
				lightingTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				PercentType percent = (PercentType) command;
				lightingTransaction.setLightIntensity(percent.byteValue());
				break;
			default:
				throw new IllegalArgumentException("invalid command '"
						+ command + "'");
			}
			lightingTransaction.mustBeSent = ((!filterUnnecessaryCommand) || (initialLightIntensityValue != lightingTransaction
					.getLightIntensity())); // Signs must send if value changed
											// or filter is disabled
			break;
		case RELAY:
			Cardio2eRelayTransaction relayTransaction = (Cardio2eRelayTransaction) transaction;
			boolean initialRelayOnValue = relayTransaction.getRelayON();
			if (relayTransaction.getBlindMode()) {
				if (filterUnnecessaryCommand)
					relayTransaction.smartSendingEnabledTransaction = true; // relay
																			// blind
																			// mode
																			// with
																			// filterUnnecessaryCommand
																			// needs
																			// smartSending
																			// function
																			// to
																			// work
																			// properly
				if (command instanceof UpDownType) {
					relayTransaction
							.setTransactionType(Cardio2eTransactionTypes.SET);
					if ((UpDownType) command == UpDownType.UP) {
						relayTransaction.blindMoveUp();
					} else if ((UpDownType) command == UpDownType.DOWN) {
						relayTransaction.blindMoveDown();
					}
				} else if (command instanceof StopMoveType) {
					relayTransaction
							.setTransactionType(Cardio2eTransactionTypes.SET);
					if (((StopMoveType) command == StopMoveType.STOP)
							|| ((StopMoveType) command == StopMoveType.MOVE)) { // MOVE
																				// command
																				// is
																				// also
																				// interpreted
																				// as
																				// STOP
																				// for
																				// KNX
																				// compatibility
						relayTransaction.blindStop();
						relayTransaction.smartSendingEnqueueFirst = true; // Tells
																			// smartSending
																			// that
																			// this
																			// transaction
																			// (blind
																			// stop
																			// command)
																			// must
																			// have
																			// absolute
																			// priority
					} else {
						throw new IllegalArgumentException("invalid command '"
								+ command + "'");
					}
				} else
					throw new IllegalArgumentException("invalid command '"
							+ command + "'");
			} else {
				if (command instanceof OnOffType) {
					relayTransaction
							.setTransactionType(Cardio2eTransactionTypes.SET);
					if ((OnOffType) command == OnOffType.ON) {
						relayTransaction.setRelayON(true);
					} else if ((OnOffType) command == OnOffType.OFF) {
						relayTransaction.setRelayON(false);
					}
				} else
					throw new IllegalArgumentException("invalid command '"
							+ command + "'");
			}
			relayTransaction.mustBeSent = ((!filterUnnecessaryCommand) || (initialRelayOnValue != relayTransaction
					.getRelayON())); // Signs must send if value is changed or
										// filter is enabled
			break;
		case HVAC_CONTROL:
			Cardio2eHvacControlTransaction hvacControlTransaction = (Cardio2eHvacControlTransaction) transaction;
			if (command instanceof OnOffType) {
				hvacControlTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				if (hvacControlTransaction.singleHvacSystemMode == null) {
					if ((OnOffType) command == OnOffType.ON) {
						hvacControlTransaction.simplePowerOn(true);
					} else if ((OnOffType) command == OnOffType.OFF) {
						hvacControlTransaction.simplePowerOn(false);
					}
				} else {
					if ((OnOffType) command == OnOffType.ON) {
						hvacControlTransaction.setSingleHvacSystemModeOn(true);
					} else if ((OnOffType) command == OnOffType.OFF) {
						hvacControlTransaction.setSingleHvacSystemModeOn(false);
					}
				}
			} else if (command instanceof DecimalType) {
				hvacControlTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				DecimalType decimalValue = (DecimalType) command;
				if (hvacControlTransaction.singleHvacSystemMode == null) {
					hvacControlTransaction.setKnxDpt20_105(decimalValue
							.intValue());
				} else {
					hvacControlTransaction.setSingleHvacSetPoint(decimalValue
							.doubleValue());
				}
			} else {
				throw new IllegalArgumentException("invalid command '"
						+ command + "'");
			}
			hvacControlTransaction.mustBeSent = true; // Signs always must send
			break;
		case ZONES_BYPASS:
			Cardio2eZonesBypassTransaction zonesBypassTransaction = (Cardio2eZonesBypassTransaction) transaction;
			if (command instanceof OnOffType) {
				zonesBypassTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				if ((OnOffType) command == OnOffType.ON) {
					zonesBypassTransaction.setZoneBypassBooleanState(true);
				} else if ((OnOffType) command == OnOffType.OFF) {
					zonesBypassTransaction.setZoneBypassBooleanState(false);
				}
			} else
				throw new IllegalArgumentException("invalid command '"
						+ command + "'");
			zonesBypassTransaction.mustBeSent = true; // Signs always must send
			break;
		case SECURITY:
			Cardio2eSecurityTransaction securityTransaction = (Cardio2eSecurityTransaction) transaction;
			if (command instanceof OnOffType) {
				if (securityCode != null) {
					securityTransaction
							.setTransactionType(Cardio2eTransactionTypes.SET);
					securityTransaction.setSecurityCode(securityCode);
					if ((OnOffType) command == OnOffType.ON) {
						securityTransaction.setSecurityArmed(true);
						;
					} else if ((OnOffType) command == OnOffType.OFF) {
						securityTransaction.setSecurityArmed(false);
						;
					}
				} else
					throw new IllegalArgumentException(
							"cannot arm / disarm because no security code is provided");
			} else
				throw new IllegalArgumentException("invalid command '"
						+ command + "'");
			securityTransaction.mustBeSent = true; // Signs always must send
			break;
		case SCENARIO:
			Cardio2eScenarioTransaction scenarioTransaction = (Cardio2eScenarioTransaction) transaction;
			if (command instanceof DecimalType) {
				scenarioTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				DecimalType scenarioNumber = (DecimalType) command;
				scenarioTransaction.setObjectNumber((short) (scenarioNumber
						.intValue() + 1));
				if (securityCode != null)
					scenarioTransaction.setSecurityCode(securityCode);
			} else
				throw new IllegalArgumentException("invalid command '"
						+ command + "'");
			scenarioTransaction.mustBeSent = true; // Signs always must send
			break;
		case DATE_AND_TIME:
			Cardio2eDateTimeTransaction dateTimeTransaction = (Cardio2eDateTimeTransaction) transaction;
			if (command instanceof DateTimeType) {
				dateTimeTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				DateTimeType dateTimeCommand = (DateTimeType) command;
				Calendar dateTime = (Calendar) dateTimeCommand.getCalendar();
				dateTimeTransaction.setDateTime(new Cardio2eDateTime(dateTime));
			} else
				throw new IllegalArgumentException("invalid command '"
						+ command + "'");
			dateTimeTransaction.mustBeSent = true; // Signs always must send
			break;
		case CURTAIN:
			Cardio2eCurtainTransaction curtainTransaction = (Cardio2eCurtainTransaction) transaction;
			byte initialOpeningPercentageValue = curtainTransaction
					.getOpeningPercentage();
			switch (command.getClass().getSimpleName()) {
			case PERCENT_TYPE_STRING:
				curtainTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				PercentType percent = (PercentType) command;
				curtainTransaction.setClosingPercentage(percent.byteValue());
				break;
			case INCREASE_DECREASE_TYPE_STRING:
				curtainTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				if ((IncreaseDecreaseType) command == IncreaseDecreaseType.INCREASE) {
					curtainTransaction
							.setClosingPercentage(byteStep(
									curtainTransaction.getClosingPercentage(),
									10,
									Cardio2eTransaction.CARDIO2E_MIN_LIGHTING_INTENSITY,
									Cardio2eTransaction.CARDIO2E_MAX_LIGHTING_INTENSITY));
				} else if ((IncreaseDecreaseType) command == IncreaseDecreaseType.DECREASE) {
					curtainTransaction
							.setClosingPercentage(byteStep(
									curtainTransaction.getClosingPercentage(),
									-10,
									Cardio2eTransaction.CARDIO2E_MIN_LIGHTING_INTENSITY,
									Cardio2eTransaction.CARDIO2E_MAX_LIGHTING_INTENSITY));
				} else
					throw new IllegalArgumentException("invalid command '"
							+ command + "'");
				break;
			case UP_DOWN_TYPE_STRING:
				curtainTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				if ((UpDownType) command == UpDownType.UP) {
					curtainTransaction.setCurtainOpen(true);
					;
				} else if ((UpDownType) command == UpDownType.DOWN) {
					curtainTransaction.setCurtainClosed(true);
				} else
					throw new IllegalArgumentException("invalid command '"
							+ command + "'");
				break;
			default:
				throw new IllegalArgumentException("invalid command '"
						+ command + "'");
			}
			curtainTransaction.mustBeSent = ((!filterUnnecessaryCommand) || (initialOpeningPercentageValue != curtainTransaction
					.getOpeningPercentage())); // Signs must send if value
												// changed or filter is disabled
			break;
		default:
			throw new IllegalArgumentException("invalid command '" + command
					+ "' for '" + transaction.getObjectType() + "'");
		}
	}

	static void stateToTransaction(State state, Cardio2eTransaction transaction) {
		transaction.mustBeSent = false;
		switch (transaction.getObjectType()) {
		case LIGHTING:
			Cardio2eLightingTransaction lightingTransaction = (Cardio2eLightingTransaction) transaction;
			byte initialLightIntensityValue = lightingTransaction
					.getLightIntensity();
			switch (state.getClass().getSimpleName()) {
			case ON_OFF_TYPE_STRING:
				lightingTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				if ((OnOffType) state == OnOffType.ON) {
					lightingTransaction.setLightPowered(true);
				} else if ((OnOffType) state == OnOffType.OFF) {
					lightingTransaction.setLightPowered(false);
				} else
					throw new IllegalArgumentException("invalid state '"
							+ state + "'");
				break;
			case PERCENT_TYPE_STRING:
				lightingTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				PercentType percent = (PercentType) state;
				lightingTransaction.setLightIntensity(percent.byteValue());
				break;
			default:
				throw new IllegalArgumentException("invalid state '" + state
						+ "' for Cardio 2é lighting object");
			}
			lightingTransaction.mustBeSent = ((!filterUnnecessaryReverseModeUpdate) || (initialLightIntensityValue != lightingTransaction
					.getLightIntensity())); // Signs must send if value changed
											// or filter is disabled
			break;
		case RELAY:
			Cardio2eRelayTransaction relayTransaction = (Cardio2eRelayTransaction) transaction;
			boolean initialRelayOnValue = relayTransaction.getRelayON();
			if (relayTransaction.getBlindMode()) {
				if (filterUnnecessaryReverseModeUpdate)
					relayTransaction.smartSendingEnabledTransaction = true; // relay
																			// blind
																			// mode
																			// with
																			// filterUnnecessaryReverseModeUpdate
																			// needs
																			// smartSending
																			// function
																			// to
																			// work
																			// properly
				if (state instanceof UpDownType) {
					relayTransaction
							.setTransactionType(Cardio2eTransactionTypes.SET);
					if ((UpDownType) state == UpDownType.UP) {
						relayTransaction.blindMoveUp();
					} else if ((UpDownType) state == UpDownType.DOWN) {
						relayTransaction.blindMoveDown();
					} else
						throw new IllegalArgumentException("invalid state '"
								+ state
								+ "' for no blind mode Cardio 2é relay object");
				} else
					throw new IllegalArgumentException("invalid state '"
							+ state + "' for blind mode Cardio 2é relay object");
			} else {
				if (state instanceof OnOffType) {
					relayTransaction
							.setTransactionType(Cardio2eTransactionTypes.SET);
					if ((OnOffType) state == OnOffType.ON) {
						relayTransaction.setRelayON(true);
					} else if ((OnOffType) state == OnOffType.OFF) {
						relayTransaction.setRelayON(false);
					}
				} else
					throw new IllegalArgumentException("invalid state '"
							+ state
							+ "' for no blind mode Cardio 2é relay object");
			}
			relayTransaction.mustBeSent = ((!filterUnnecessaryReverseModeUpdate) || (initialRelayOnValue != relayTransaction
					.getRelayON())); // Signs must send if value changed or
										// filter is disabled
			break;
		case DATE_AND_TIME:
			Cardio2eDateTimeTransaction dateTimeTransaction = (Cardio2eDateTimeTransaction) transaction;
			if (state instanceof DateTimeType) {
				dateTimeTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				DateTimeType dateTimeState = (DateTimeType) state;
				Calendar dateTime = (Calendar) dateTimeState.getCalendar();
				dateTimeTransaction.setDateTime(new Cardio2eDateTime(dateTime));
			} else
				throw new IllegalArgumentException("invalid state '" + state
						+ "'");
			dateTimeTransaction.mustBeSent = true; // Signs always must send
			break;
		case CURTAIN:
			Cardio2eCurtainTransaction curtainTransaction = (Cardio2eCurtainTransaction) transaction;
			byte initialOpeningPercentageValue = curtainTransaction
					.getOpeningPercentage();
			if (state.getClass().getSimpleName().equals(PERCENT_TYPE_STRING)) {
				curtainTransaction
						.setTransactionType(Cardio2eTransactionTypes.SET);
				PercentType percent = (PercentType) state;
				curtainTransaction.setClosingPercentage(percent.byteValue());
			} else {
				throw new IllegalArgumentException("invalid state '" + state
						+ "'");
			}
			curtainTransaction.mustBeSent = ((!filterUnnecessaryReverseModeUpdate) || (initialOpeningPercentageValue != curtainTransaction
					.getOpeningPercentage())); // Signs must send if value
												// changed or filter is disabled
			break;
		default:
			throw new IllegalArgumentException("invalid state '" + state
					+ "' for '" + transaction.getObjectType()
					+ "' for Cardio 2é curtain object");
		}
	}

	static Command transactionToCommand(Cardio2eTransaction transaction,
			Item item) {
		Command command = null;
		switch (transaction.getObjectType()) {
		case LIGHTING:
			Cardio2eLightingTransaction lightingTransaction = (Cardio2eLightingTransaction) transaction;
			if (item.getAcceptedDataTypes().contains(PercentType.class)) {
				command = PercentType.valueOf(Byte.toString(lightingTransaction
						.getLightIntensity()));
			} else if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
				command = (lightingTransaction.getLightPowered()) ? OnOffType.ON
						: OnOffType.OFF;
			} else {
				throw new IllegalArgumentException(
						"item '"
								+ item
								+ "' command does not accept percent or on/off lighting values");
			}
			break;
		case RELAY:
			Cardio2eRelayTransaction relayTransaction = (Cardio2eRelayTransaction) transaction;
			if (relayTransaction.getBlindMode()) {
				if ((item.getAcceptedDataTypes().contains(UpDownType.class))
						&& (item.getAcceptedDataTypes()
								.contains(StopMoveType.class))) {
					if (relayTransaction.getRelayON()) {
						command = (relayTransaction.getBlindUpMode()) ? UpDownType.UP
								: UpDownType.DOWN;
					} else
						command = StopMoveType.STOP;
				} else {
					throw new IllegalArgumentException(
							"item '"
									+ item
									+ "' command does not accept up/down/stop blind values");
				}
			} else {
				if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
					command = (relayTransaction.getRelayON()) ? OnOffType.ON
							: OnOffType.OFF;
				} else {
					throw new IllegalArgumentException("item '" + item
							+ "' command does not accept on/off relay values");
				}
			}
			break;
		case CURTAIN:
			Cardio2eCurtainTransaction curtainTransaction = (Cardio2eCurtainTransaction) transaction;
			if (item.getAcceptedDataTypes().contains(PercentType.class)) {
				command = PercentType.valueOf(Byte.toString(curtainTransaction
						.getClosingPercentage()));
			} else {
				throw new IllegalArgumentException("item '" + item
						+ "' command does not accept percent curtain values");
			}
			break;
		default:
			throw new IllegalArgumentException(
					"invalid transaction value for item '" + item + "'");
		}
		return command;
	}

	static State transactionToState(Cardio2eTransaction transaction, Item item) {
		State state = null;
		switch (transaction.getObjectType()) {
		case LIGHTING:
			Cardio2eLightingTransaction lightingTransaction = (Cardio2eLightingTransaction) transaction;
			if (item.getAcceptedDataTypes().contains(PercentType.class)) {
				state = PercentType.valueOf(Byte.toString(lightingTransaction
						.getLightIntensity()));
			} else if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
				state = (lightingTransaction.getLightPowered()) ? OnOffType.ON
						: OnOffType.OFF;
			} else {
				throw new IllegalArgumentException(
						"item '"
								+ item
								+ "' state does not accept percent or on/off lighting value");
			}
			break;
		case RELAY:
			Cardio2eRelayTransaction relayTransaction = (Cardio2eRelayTransaction) transaction;
			if (relayTransaction.getBlindMode()) {
				if (item.getAcceptedDataTypes().contains(UpDownType.class)) {
					if (relayTransaction.blindIsMovingUp()) {
						state = UpDownType.UP;
					} else if (relayTransaction.blindIsMovingDown()) {
						state = UpDownType.DOWN;
						;
					}
				} else {
					throw new IllegalArgumentException(
							"item '"
									+ item
									+ "' state does not accept up/down value (from blind mode relay)");
				}
			} else {
				if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
					state = (relayTransaction.getRelayON()) ? OnOffType.ON
							: OnOffType.OFF;
				} else {
					throw new IllegalArgumentException("item '" + item
							+ "' state does not accept on/off relay value");
				}
			}
			break;
		case HVAC_TEMPERATURE:
			Cardio2eHvacTemperatureTransaction hvacTemperatureTransaction = (Cardio2eHvacTemperatureTransaction) transaction;
			if (item.getAcceptedDataTypes().contains(DecimalType.class)) {
				state = DecimalType.valueOf(Double
						.toString(hvacTemperatureTransaction
								.getHvacTemperature()));
			} else {
				throw new IllegalArgumentException(
						"item '"
								+ item
								+ "' state does not accept decimal hvacTemperature value");
			}
			break;
		case HVAC_CONTROL:
			Cardio2eHvacControlTransaction hvacControlTransaction = (Cardio2eHvacControlTransaction) transaction;
			if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
				if (hvacControlTransaction.singleHvacSystemMode == null) {
					state = (hvacControlTransaction.isPoweredOn()) ? OnOffType.ON
							: OnOffType.OFF;
				} else {
					state = (hvacControlTransaction.getSingleHvacSystemModeOn()) ? OnOffType.ON
							: OnOffType.OFF;
				}
			} else if (item.getAcceptedDataTypes().contains(DecimalType.class)) {
				if (hvacControlTransaction.singleHvacSystemMode == null) {
					state = DecimalType
							.valueOf(Integer.toString(hvacControlTransaction
									.getKnxDpt20_105()));
					;
				} else {
					state = DecimalType.valueOf(Double
							.toString(hvacControlTransaction
									.getSingleHvacSetPoint()));
				}
			} else {
				throw new IllegalArgumentException(
						"item '"
								+ item
								+ "' state does not accept on/off or decimal hvacControl value");
			}
			break;
		case ZONES:
			Cardio2eZonesTransaction zonesTransaction = (Cardio2eZonesTransaction) transaction;
			if (item.getAcceptedDataTypes().contains(OpenClosedType.class)) {
				state = (zonesTransaction.getZoneDetection()) ? OpenClosedType.OPEN
						: OpenClosedType.CLOSED;
			} else if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
				state = (zonesTransaction.getZoneDetection()) ? OnOffType.ON
						: OnOffType.OFF;
			} else {
				throw new IllegalArgumentException(
						"item '"
								+ item
								+ "' state does not accept open/closed or on/off zones value");
			}
			break;
		case ZONES_BYPASS:
			Cardio2eZonesBypassTransaction zonesBypassTransaction = (Cardio2eZonesBypassTransaction) transaction;
			if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
				state = (zonesBypassTransaction.getZoneBypassBooleanState()) ? OnOffType.ON
						: OnOffType.OFF;
			} else {
				throw new IllegalArgumentException("item '" + item
						+ "' state does not accept on/off zonesBypass value");
			}
			break;
		case SECURITY:
			Cardio2eSecurityTransaction securityTransaction = (Cardio2eSecurityTransaction) transaction;
			if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
				state = (securityTransaction.getSecurityArmed()) ? OnOffType.ON
						: OnOffType.OFF;
			} else if (item.getAcceptedDataTypes().contains(DecimalType.class)) {
				state = DecimalType.valueOf(Integer
						.toString(securityTransaction.getErrorCode()));
			} else {
				throw new IllegalArgumentException("item '" + item
						+ "' state does not accept on/off security value");
			}
			break;
		case CURTAIN:
			Cardio2eCurtainTransaction curtainTransaction = (Cardio2eCurtainTransaction) transaction;
			if (item.getAcceptedDataTypes().contains(PercentType.class)) {
				state = PercentType.valueOf(Byte.toString(curtainTransaction
						.getClosingPercentage()));
			} else if (item.getAcceptedDataTypes().contains(UpDownType.class)) {
				state = (curtainTransaction.getCurtainClosed()) ? UpDownType.DOWN
						: UpDownType.UP;
			} else {
				throw new IllegalArgumentException(
						"item '"
								+ item
								+ "' state does not accept percent or up/down curtain value");
			}
			break;
		default:
			throw new IllegalArgumentException(
					"invalid transaction value for item '" + item + "'");
		}
		return state;
	}

	private static byte byteStep(byte value, int step, byte min, byte max) {
		int newValue = value + step;
		if (newValue < min)
			newValue = min;
		if (newValue > max)
			newValue = max;
		return (byte) newValue;
	}
}