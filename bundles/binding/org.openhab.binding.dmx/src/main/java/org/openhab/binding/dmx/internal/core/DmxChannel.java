/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.core;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.dmx.internal.action.BaseAction;
import org.openhab.binding.dmx.internal.action.ResumeAction;
import org.openhab.core.library.types.PercentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DmxChannel. Represents a single dimmable channel in DMX universe.
 * 
 * When the channel is switched on, it will switch on to the last know state.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxChannel implements Comparable<DmxChannel> {

	protected static final Logger logger = LoggerFactory
			.getLogger(DmxChannel.class);

	private int channelId;

	/** Current channel value in the range from 0-255 **/
	private int value;

	/** Channel is alive or not.. **/
	private boolean switchedOn = false;

	private List<BaseAction> actions = new ArrayList<BaseAction>();

	private List<BaseAction> suspendedActions = new ArrayList<BaseAction>();

	private int suspendedValue;

	/** Maximum DMX output value **/
	public static int DMX_MAX_VALUE = 255;

	/** Minimum output value **/
	public static int DMX_MIN_VALUE = 0;

	/**
	 * Create new DMX channel.
	 * 
	 * @param channelId
	 *            DMX channel id.
	 */
	public DmxChannel(int channelId) {
		this.channelId = channelId;
	}

	/**
	 * Set the channel output to a fixed value. This will stop any active
	 * actions on the channel.
	 * 
	 * @param value
	 *            output value
	 */
	public synchronized void setValue(int value) {

		switchedOn = true;

		// stop active/looping fades..
		actions.clear();

		// set value
		this.value = DmxUtil.capDmxValue(value);
	}

	/**
	 * Set the channel output to a fixed value of output level * 255, if no
	 * actions are available. If actions are available, they are faded to the
	 * output level.
	 * 
	 * @param value
	 *            output value
	 */
	public synchronized void setValue(PercentType outputlevel) {

		if (outputlevel.intValue() == 0) {
			switchOff();
			return;
		} else {
			switchOn();
		}

		if (hasRunningActions()) {
			for (BaseAction a : actions) {
				a.setOutputLevel(outputlevel.intValue());
			}
		} else {
			value = DmxUtil.getOutputValue(value, outputlevel.intValue());
		}

	}

	/**
	 * Get the value of the channel.
	 * 
	 * @return value 0 - 255.
	 */
	public synchronized int getValue() {

		if (!switchedOn) {
			return DMX_MIN_VALUE;
		}

		return value;
	}

	/**
	 * Switch on this channel. If no actions are active, this will also put the
	 * channel value back to the last known state or to DMX Max value if no
	 * state is known.
	 */
	public synchronized void switchOn() {
		switchedOn = true;
	}

	/**
	 * Switch off this channel. The current state of the channel (but not
	 * actions) will be kept for later use.
	 */
	public synchronized void switchOff() {
		switchedOn = false;
		actions.clear();
	}

	/**
	 * Get the new value for this channel as determined by active actions or the
	 * current value.
	 * 
	 * @param calculationTime
	 * 
	 * @return value 0-255
	 */
	public synchronized Integer getNextValue(long calculationTime) {

		if (!switchedOn) {
			return DMX_MIN_VALUE;
		}

		if (hasRunningActions()) {
			BaseAction action = actions.get(0);
			value = action.getNewValue(this, calculationTime);
			if (action.isCompleted()) {
				switchToNextAction();
			}
		}

		return value;
	}

	/**
	 * Move to the next action in the action chain. This method is used by
	 * automatic chains and to manually move to the next action if actions are
	 * set as indefinite (e.g. endless hold). This allows the user to toggle
	 * through a fixed set of values.
	 */
	public synchronized void switchToNextAction() {

		logger.trace("Switching to next action on channel {}", getChannelId());
		// push action to the back of the action list
		BaseAction action = actions.get(0);
		actions.remove(0);
		action.reset();
		actions.add(action);
	}

	/**
	 * Replace the current list of channel actions with the provided one.
	 * 
	 * @param channelAction
	 *            action for this channel.
	 */
	public synchronized void setChannelAction(BaseAction channelAction) {
		if (!switchedOn) {
			value = 0;
		}
		switchedOn = true;
		actions.clear();
		actions.add(channelAction);
	}

	/**
	 * Add a channel action to the current list of channel actions.
	 * 
	 * @param channelAction
	 *            action for this channel.
	 */
	public synchronized void addChannelAction(BaseAction channelAction) {
		actions.add(channelAction);
	}

	/**
	 * @return dmx channel id.
	 */
	public int getChannelId() {
		return channelId;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public int compareTo(DmxChannel arg0) {

		if (arg0 == null) {
			return -1;
		}
		return new Integer(getChannelId()).compareTo(new Integer(arg0
				.getChannelId()));
	}

	/**
	 * Increase channel value.
	 * 
	 * @param increment
	 *            % to increase
	 */
	public synchronized void increaseChannel(int increment) {

		if (increment < 0) {
			return;
		}
		switchedOn = true;

		if (!hasRunningActions()) {
			// increase channel value
			value = DmxUtil
					.capDmxValue(value
							+ DmxUtil.getByteFromPercentType(new PercentType(
									increment)));
		} else {
			// increase channel actions output
			for (BaseAction a : actions) {
				a.increase(increment);
			}
		}
	}

	/**
	 * Decrease channel value level.
	 * 
	 * @param decrement
	 *            % to decrease.
	 */
	public synchronized void decreaseChannel(int decrement) {

		if (decrement < 0) {
			return;
		}

		if (!hasRunningActions()) {
			// increase channel value
			value = DmxUtil
					.capDmxValue(value
							- DmxUtil.getByteFromPercentType(new PercentType(
									decrement)));
		} else {
			// decrease channel actions output
			for (BaseAction a : actions) {
				a.decrease(decrement);
			}
		}
	}

	/**
	 * @return true if there are running actions
	 */
	public boolean hasRunningActions() {
		return !actions.isEmpty();
	}

	/**
	 * Suspend the current actions and value. This will store the values for
	 * later resume.
	 */
	public void suspend() {
		suspendedValue = value;
		suspendedActions.clear();
		suspendedActions.addAll(actions);
	}

	/**
	 * Resume previously suspended actions. If no actions were suspended, the
	 * suspended value will be restored.
	 */
	public void resume() {
		actions.clear();
		if (!suspendedActions.isEmpty()) {
			actions.addAll(suspendedActions);
			suspendedActions.clear();
		} else {
			setValue(suspendedValue);
		}
	}

	/**
	 * Add a resume action to the end of the action list to trigger a resume of previously
	 * suspended actions.
	 */
	public void addResumeAction() {
		actions.add(new ResumeAction());
	}

}