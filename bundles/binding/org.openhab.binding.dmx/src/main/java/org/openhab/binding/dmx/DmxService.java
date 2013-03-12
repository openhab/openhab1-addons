/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.dmx;

import org.openhab.core.library.types.PercentType;

/**
 * DMX Service to send DMX values.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
/**
 * @author davy
 * 
 */
public interface DmxService {

	public static final int CHANNEL_MAX_VALUE = 255;

	public static final int CHANNEL_MIN_VALUE = 0;

	/**
	 * Start DMX Transmission
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception;

	/**
	 * Stop DMX Transmission
	 */
	public void stop();

	/**
	 * Set a given DMX channel to the provided value. If a channel is disabled,
	 * setting the value will also enable the channel.
	 * 
	 * @param channel
	 *            DMX channel id.
	 * @param value
	 *            0-255 target value.
	 */
	public void setChannelValue(int channel, int value);

	/**
	 * For channels without active actions, this will set the channel to the
	 * provided value. If a channel is disabled, setting the value will also
	 * enable the channel. For channels with active actions, this will set the
	 * output level on the actions to the provided value.
	 * 
	 * @param channelId
	 * @param outputlevel
	 *            0-100
	 */
	public void setChannelValue(int channelId, PercentType value);

	/**
	 * Get the output value of a given DMX channel.
	 * 
	 * @param universeId
	 *            DMX universe id.
	 * @param channel
	 *            DMX channel id.
	 * @return output value 0-255.
	 */
	public int getChannelValue(int channelId);

	/**
	 * Disable a channel. This will switch off the channel. The last know value
	 * is stored in the channel and will be restored when the channel is enabled
	 * again.
	 * 
	 * @param channel
	 *            DMX channel to disable.
	 */
	public void disableChannel(int channel);

	/**
	 * Enable a channel. The last know value in the channel will be restored.
	 * 
	 * @param channel
	 *            DMX channel to enable.
	 */
	public void enableChannel(int channel);

	/**
	 * Register a new listener which receives the new state of a channel if the
	 * value has been changed.
	 * 
	 * @param listener
	 *            change listener.
	 */
	public void registerStatusListener(DmxStatusUpdateListener listener);

	/**
	 * Unregister a status listener.
	 * 
	 * @param listener
	 *            change listener.
	 */
	public void unregisterStatusListener(DmxStatusUpdateListener listener);

	/**
	 * Add a channel fade action.
	 * 
	 * @param channel
	 *            DMX channel id.
	 * @param fadeTime
	 *            time in ms for fading from the current channel value to the
	 *            new target value.
	 * @param targetValue
	 *            target value for the channel.
	 * @param holdTime
	 *            time in ms to hold the target value for. A value of -1 is
	 *            indefinite.
	 * @param immediate
	 *            when true, the new fade will replace all existing fades for
	 *            this channel. When false, the new fade is appended to the list
	 *            of existing fades for this channel.
	 */
	public void fadeChannel(int channel, int fadeTime, int targetValue,
			int holdTime, boolean immediate);

	/**
	 * Add a fade action to multiple channels.
	 * 
	 * @param channel
	 *            DMX channel id.
	 * @param fadeTime
	 *            time in ms for fading from the current channel value to the
	 *            new target value.
	 * @param targetValue
	 *            target value array. Every value will be applied to channel
	 *            (channel + arrary index).
	 * @param holdTime
	 *            time in ms to hold the target value for. A value of -1 is
	 *            indefinite.
	 * @param immediate
	 *            when true, the new fade will replace all existing fades for
	 *            this channel. When false, the new fade is appended to the list
	 *            of existing fades for this channel.
	 */
	public void fadeChannels(int channel, int fadeTime, int[] targetValue,
			int holdTime, boolean immediate);

	/**
	 * Switch to the next available action for a DMX channel.
	 * 
	 * @param channel
	 *            channel id
	 */
	public void switchToNextAction(int channel);

	/**
	 * Switch to the next available action for a range of DMX channels.
	 * 
	 * @param fromChannel
	 *            start channel id
	 * @param numberOfChannels
	 *            number of channels
	 */
	public void switchToNextAction(int fromChannel, int numberOfChannels);

	/**
	 * Make a channel mirror another channel for a given period.
	 * 
	 * @param sourceChannel
	 * @param mirrorChannel
	 * @param duration
	 */
	public void mirrorChannel(int sourceChannel, int mirrorChannel, int duration);

	/**
	 * Increase a channel output value with a given increment
	 * 
	 * @param channel
	 * @param increment
	 *            0-100
	 */
	public void increaseChannel(int channel, int increment);

	/**
	 * Reduce a channel output value with a given decrement
	 * 
	 * @param channel
	 * @param decrement
	 *            0-100
	 */
	public void decreaseChannel(int channel, int decrement);

	/**
	 * Suspend output of the DMX service so that the configuration can be
	 * changed without immediately effecting the DMX output.
	 * 
	 * @param suspend
	 *            true to suspend, false to resume
	 */
	public void suspend(boolean suspend);

	/**
	 * @param channelId
	 * @return true if there are fade actions or others active
	 */
	public boolean hasChannelActions(int channelId);

	/**
	 * Get an active DMX device connection;
	 * 
	 * @return active DMX Device Connection;
	 */
	public DmxConnection getConnection();

	/**
	 * Inject DMX connection to use.
	 * 
	 * @param conn
	 */
	public void setConnection(DmxConnection conn);

	/**
	 * Stop using given DMX connection.
	 * 
	 * @param conn
	 */
	public void unsetConnection(DmxConnection conn);

	/**
	 * Suspend any active fades for the given channel. If previous fades have
	 * been suspended, these will be overwritten with the current fade being
	 * suspended.
	 * 
	 * @param channel
	 *            channel number
	 */
	public void suspendChannel(int channel);

	/**
	 * Add an action to a channel to resume a fades which was previously suspended.
	 * The action will be executed after any existing actions.
	 * @param channel
	 *            channel number
	 */
	public void addChannelResume(int channel);

}
