/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarm.internal.bus;

import java.util.Dictionary;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.alarm.config.AlarmBindingProvider;
import org.openhab.binding.alarm.config.AlarmCondition;
import org.openhab.binding.alarm.config.AlarmCondition.MatchingFunction;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.AlarmState;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the central class that takes care of the event reception from openHAB.
 * 
 * The received messages are converted into the right format for the other bus and published 
 * to it.
 * 
 * @author Volker Daube
 * @since 1.7.0
 *
 */
public class AlarmBinding extends AbstractBinding<AlarmBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(AlarmBinding.class);
	private static final AlarmScheduler sAlarmScheduler = AlarmScheduler.getInstance();
	private static boolean sStaleDeviceCheckAutoCycle = true; 
	private static int sStaleDeviceCheckCycleInSecs = -1; 

	public void activate(ComponentContext componentContext) {
		logger.debug("AlarmBinding: activating");
		sAlarmScheduler.registerAlarmListener(new AlarmListener() {

			/* (non-Javadoc)
			 * @see org.openhab.binding.alarm.internal.bus.AlarmListener#staleAlarm(java.lang.String, org.openhab.binding.alarm.config.AlarmCondition)
			 */
			@Override
			public void staleAlarm(String itemName, AlarmCondition alarmCondition) {
				GenericItem gItem=getGenericItem(itemName);
				gItem.setAlarmed(new AlarmState(alarmCondition.getAlarmText(), alarmCondition.getAlarmClass()));
				alarmItem(gItem, alarmCondition);
			}

			/* (non-Javadoc)
			 * @see org.openhab.binding.alarm.internal.bus.AlarmListener#delayedAlarm(java.lang.String, org.openhab.binding.alarm.config.AlarmCondition)
			 */
			@Override
			public void delayedAlarm(String itemName, AlarmCondition alarmCondition) {
				GenericItem gItem=getGenericItem(itemName);
				gItem.setAlarmed(new AlarmState(alarmCondition.getAlarmText(), alarmCondition.getAlarmClass()));
				alarmItem(gItem, alarmCondition);

			}

			/* (non-Javadoc)
			 * @see org.openhab.binding.alarm.internal.bus.AlarmListener#staleAlarmCanceled(java.lang.String, java.lang.String)
			 */
			@Override
			public void staleAlarmCanceled(String itemName, String messageItemName) {
				GenericItem gItem=getGenericItem(itemName);
				gItem.cancelAlarm();
			}
		});
	}

	public void deactivate(ComponentContext componentContext) {
		logger.debug("AlarmBinding: deactivating");

		for (AlarmBindingProvider provider : providers) {
			provider.removeBindingChangeListener(this);
		}
		providers.clear();
		sAlarmScheduler.shutdown();
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.binding.AbstractBinding#internalReceiveUpdate(java.lang.String, org.openhab.core.types.State)
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		//User or rule triggered a status change
		logger.debug("Received update (item='{}', state='{}')", itemName, newState.toString());
		for (AlarmBindingProvider provider : providers) {
			//Get the item
			GenericItem item = (GenericItem) provider.getItem(itemName);
			if (item==null) {
				continue;
			}

			//Update the items last updated time in the AlarmScheduler
			sAlarmScheduler.touch(item.getName());

			Iterable<AlarmCondition> alarmConditions = provider.getAlarmConditions(itemName);
			State oldState = item.getState();

			if (alarmConditions != null) {
				for (AlarmCondition alarmCondition : alarmConditions) {
					//Ignore stale alarm condition
					if (!alarmCondition.getMatchingFunction().equals(MatchingFunction.STALE)) {
						//Not a 'stale' alarm condition
						boolean isAlarm=isAlarm(alarmCondition, oldState, newState);
						if (item.isAlarmed()) {
							if (isAlarm) {
								//Item is already in alarm state and an alarm condition is fulfilled
								logger.debug("Item is already alarmed! Ignoring alarm. item {}, old value {},  value {}", itemName, oldState, newState);
							}
							else {
								//Item is already in alarm state and no alarm condition is fulfilled
								logger.debug("Alarm cancel: item {}, function {} value {}", itemName, alarmCondition.getMatchingFunction(), newState);
								cancelAlarm(item, alarmCondition.getMessageItemName());
							}
						}
						else {
							if (isAlarm) {
								//Item is not in alarm state and an alarm condition is fulfilled
								if (alarmCondition.getAlarmTimeInSeconds()>0) {
									//Check is item has a delayed alarm. Ignore repeating alarms.
									if (!sAlarmScheduler.isDelayAlarmed(itemName)) {
										//Delayed alarm. 
										sAlarmScheduler.addDelayedAlarm(itemName, alarmCondition);
									}
								}
								else {
									logger.debug("Alarm triggered: item {}, function {} value {}", item.getName(), alarmCondition.getMatchingFunction(), newState);
									//Not a delayed alarm, alarm now.
									alarmItem(item, alarmCondition);
								}
							}
							else {
								//Item is not in alarm state and no alarm condition is fulfilled
							}
						}
					}
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.openhab.core.binding.AbstractBinding#bindingChanged(org.openhab.core.binding.BindingProvider, java.lang.String)
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.debug("Binding changed: "+itemName);

		if (provider instanceof AlarmBindingProvider) {
			initializeItem((AlarmBindingProvider) provider, itemName);
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.binding.AbstractBinding#allBindingsChanged(org.openhab.core.binding.BindingProvider)
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		logger.debug("All bindings changed.");
		if (provider instanceof AlarmBindingProvider) {
			initializeAllItems((AlarmBindingProvider) provider);			
		}
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		if (properties != null) {
			logger.debug("AlarmBinding configuration present.");
			if (properties.get("staleDeviceCheckCycle")!=null) {
				String staleDeviceTimeoutInSecs = (String) properties.get("staleDeviceCheckCycle");
				if (staleDeviceTimeoutInSecs.equalsIgnoreCase("auto")) {
					sStaleDeviceCheckAutoCycle=true;
					sStaleDeviceCheckCycleInSecs=-1;
				}
				else if (StringUtils.isNotBlank(staleDeviceTimeoutInSecs)) {
					try {
						sStaleDeviceCheckCycleInSecs = Integer.parseInt(staleDeviceTimeoutInSecs);
					}
					catch (NumberFormatException e) {
						throw new ConfigurationException("staleDeviceCheckCycle", "Not a number! Using default: "+sStaleDeviceCheckCycleInSecs, e);
					}
				}
			}

			if (!sStaleDeviceCheckAutoCycle) {
				logger.debug("Starting AlarmScheduler with cycle: "+sStaleDeviceCheckCycleInSecs);
				sAlarmScheduler.startCyclicalStaleChecking(sStaleDeviceCheckCycleInSecs);
			}
			else {
				logger.debug("Using staleDeviceCheckCycle: auto");
			}
		}
		else {
			logger.info("AlarmBinding configuration is not present. Using defaults.");
		}
	}

	private void alarmItem(GenericItem item, AlarmCondition alarmCondition) {
		AlarmState alarmState= new AlarmState(alarmCondition.getAlarmText(), alarmCondition.getAlarmClass());

		item.setAlarmed(alarmState);
		eventPublisher.postAlarm(item.getName(), alarmState);
		String messageItemName=alarmCondition.getMessageItemName();
		if (messageItemName!=null) {
			logger.debug("Pushing alarm to alarmitem  out={}", messageItemName);
			eventPublisher.postUpdate(messageItemName, new StringType(alarmCondition.getAlarmText()));
		}
	}

	private void cancelAlarm(GenericItem item, String messageItemName) {
		item.cancelAlarm();
		if (sAlarmScheduler.isDelayAlarmed(item.getName())) {
			sAlarmScheduler.cancelDelayedAlarm(item.getName());
		}
		eventPublisher.postAlarmCancel(item.getName());

		if (messageItemName!=null) {
			logger.debug("Removing alarm from alarmitem  out={}", messageItemName);
			eventPublisher.postUpdate(messageItemName, new StringType(""));
		}
	}
	/**
	 * Return the cycle time to check for stale devices
	 * @return the cycle time to check for stale devices
	 */
	public static int getsStaleDeviceTimeoutInSecs() {
		return sStaleDeviceCheckCycleInSecs;
	}

	private GenericItem getGenericItem(String itemName) {
		for (AlarmBindingProvider provider : providers) {
			//Get the item
			GenericItem gItem = (GenericItem) provider.getItem(itemName);
			if (gItem==null) {
				continue;
			}
			return gItem;
		}
		return null;
	}

	private boolean isAlarm(AlarmCondition alarmCondition, State oldState, State newState) {
		switch (alarmCondition.getMatchingFunction()) {
		case EQ:
			if (newState == alarmCondition.getTriggerValue()) {
				return true;
			}
			break;
		case GE:
			if (newState == alarmCondition.getTriggerValue()) {
				return true;
			}
			else {
				if ((newState instanceof DecimalType) && (alarmCondition.getTriggerValue() instanceof DecimalType)) {
					if (((DecimalType) newState).compareTo((DecimalType)alarmCondition.getTriggerValue())==1) {
						return true;
					}
				}
			}
			break;
		case GT:
			if ((newState instanceof DecimalType) && (alarmCondition.getTriggerValue() instanceof DecimalType)) {
				if (((DecimalType) newState).compareTo((DecimalType)alarmCondition.getTriggerValue())==1) {
					return true;
				}
			}
			break;
		case LE:
			if (newState == alarmCondition.getTriggerValue()) {
				return true;
			}
			else {
				if ((newState instanceof DecimalType) && (alarmCondition.getTriggerValue() instanceof DecimalType)) {
					if (((DecimalType) newState).compareTo((DecimalType)alarmCondition.getTriggerValue())==-1) {
						return true;
					}
				}
			}
			break;
		case LT:
			if ((newState instanceof DecimalType) && (alarmCondition.getTriggerValue() instanceof DecimalType)) {
				if (((DecimalType) newState).compareTo((DecimalType)alarmCondition.getTriggerValue())==-1) {
					return true;
				}
			}
			break;
		case NE:
			if (newState != alarmCondition.getTriggerValue()) {
				return true;
			}
			break;
		case STALE:
			//Do nothing. This is handled via timer.
			return false;
		default:
			break;

		}
		return false;
	}

	private void initializeItem(AlarmBindingProvider provider, String itemName) {
		boolean staleCheckCycleModified=false;
		if (sAlarmScheduler.hasStaleCheck(itemName)) {
			sAlarmScheduler.removeStaleCheck(itemName);
		}
		Iterable<AlarmCondition> acl =provider.getAlarmConditions(itemName);
		if (acl!=null) {
			for (Iterator<AlarmCondition> iterator = acl.iterator(); iterator.hasNext();) {
				AlarmCondition ac=iterator.next();
				if (ac.getMatchingFunction().equals(MatchingFunction.STALE)) {
					//If the stale checking time is was set to auto, then we need to set the cycle time to the smallest cycle time defined in the items file.
					if ((sStaleDeviceCheckAutoCycle) &&
							((sStaleDeviceCheckCycleInSecs==-1)||(ac.getAlarmTimeInSeconds()<sStaleDeviceCheckCycleInSecs))) {
						sStaleDeviceCheckCycleInSecs=ac.getAlarmTimeInSeconds();
						staleCheckCycleModified=true;
					}
					//Add the stale rule to the scheduler
					sAlarmScheduler.addStaleCheck(itemName, ac);
					//Only use the first stale rule, ignore all other stale rules
					break;
				}	
			}
			if (sStaleDeviceCheckAutoCycle && staleCheckCycleModified) {
				sAlarmScheduler.shutdown();
				logger.debug("Starting AlarmScheduler with cycle: "+sStaleDeviceCheckCycleInSecs);
				sAlarmScheduler.startCyclicalStaleChecking(sStaleDeviceCheckCycleInSecs);
			}
		}
	}

	private void initializeAllItems(AlarmBindingProvider provider) {
		sAlarmScheduler.shutdown();
		for (String itemName : provider.getItemNames()) {
			Iterable<AlarmCondition> acl =provider.getAlarmConditions(itemName);
			for (Iterator<AlarmCondition> iterator = acl.iterator(); iterator.hasNext();) {
				AlarmCondition ac=iterator.next();
				if (ac.getMatchingFunction().equals(MatchingFunction.STALE)) {
					if ((sStaleDeviceCheckAutoCycle) &&
							((sStaleDeviceCheckCycleInSecs==-1)||(ac.getAlarmTimeInSeconds()<sStaleDeviceCheckCycleInSecs))) {
						sStaleDeviceCheckCycleInSecs=ac.getAlarmTimeInSeconds();
					}
					//Add the stale rule to the scheduler
					sAlarmScheduler.addStaleCheck(itemName, ac);
					//Only use the first stale rule, ignore all other stale rules
					break;
				}
			}	
		}
		logger.debug("Starting AlarmScheduler with cycle: "+sStaleDeviceCheckCycleInSecs);
		sAlarmScheduler.startCyclicalStaleChecking(sStaleDeviceCheckCycleInSecs);

	}

}
