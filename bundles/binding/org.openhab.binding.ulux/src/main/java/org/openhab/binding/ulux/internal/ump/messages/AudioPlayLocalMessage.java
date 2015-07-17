/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump.messages;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * With this message an audio file saved on the switch can be played back.
 * <p>
 * This message can only be sent, not received.
 * 
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class AudioPlayLocalMessage extends AbstractUluxMessage {

	private static final byte MESSAGE_LENGTH = (byte) 0x10;

	private static final int PLAY_FLAGS_ALARM = 0;

	private static final int PLAY_FLAGS_NO_AUDIO_PAGE = 3;

	private static final int PLAY_FLAGS_NO_VOLUME_CHANGE = 4;

	private static final int PLAY_FLAGS_INCREMENT_VOLUME = 5;

	/**
	 * Default play flags:
	 * <ul>
	 * <li>don't change volume
	 * <li>no audio page
	 * </ul>
	 */
	private static final int DEFAULT_PLAY_FLAGS = 24;

	private byte volume = (byte) 0;

	private byte equalizer = (byte) 0;

	private BigInteger playFlags = BigInteger.valueOf(DEFAULT_PLAY_FLAGS);

	private short incVolumeTime;

	private short repeats = 0;

	private short delayBeforeRepeat = 0;

	private final short soundIndex;

	/**
	 * Creates a message for the specified audio file.
	 * 
	 * @param soundIndex
	 *            the index of the audio file to play starting at 1
	 */
	public AudioPlayLocalMessage(short soundIndex) {
		super(MESSAGE_LENGTH, UluxMessageId.AudioPlayLocal);

		if (soundIndex < 1) {
			throw new IllegalArgumentException();
		}

		this.soundIndex = soundIndex;
	}

	public void setDelayBeforeRepeat(short delayBeforeRepeat) {
		if (delayBeforeRepeat < 0) {
			throw new IllegalArgumentException();
		}

		this.delayBeforeRepeat = delayBeforeRepeat;
	}

	public void setAudioPage(boolean audioPage) {
		if (audioPage) {
			this.playFlags = this.playFlags.clearBit(PLAY_FLAGS_NO_AUDIO_PAGE);
		} else {
			this.playFlags = this.playFlags.setBit(PLAY_FLAGS_NO_AUDIO_PAGE);
		}
	}

	public void setAlarm(boolean alarm) {
		if (alarm) {
			this.playFlags = this.playFlags.setBit(PLAY_FLAGS_ALARM);
		} else {
			this.playFlags = this.playFlags.clearBit(PLAY_FLAGS_ALARM);
		}
	}

	private void setIncrementVolume(boolean incrementVolume) {
		if (incrementVolume) {
			this.playFlags = this.playFlags.setBit(PLAY_FLAGS_INCREMENT_VOLUME);
		} else {
			this.playFlags = this.playFlags.clearBit(PLAY_FLAGS_INCREMENT_VOLUME);
		}
	}

	private void setDontChangeVolume(boolean dontChangeVolume) {
		if (dontChangeVolume) {
			this.playFlags = this.playFlags.setBit(PLAY_FLAGS_NO_VOLUME_CHANGE);
		} else {
			this.playFlags = this.playFlags.clearBit(PLAY_FLAGS_NO_VOLUME_CHANGE);
		}
	}

	/**
	 * 0=normal, 1=rock, 2=jazz, 3=classic, 4=pop, 5=reserved
	 */
	public void setEqualizer(byte equalizer) {
		if (equalizer < 1 || equalizer > 5) {
			throw new IllegalArgumentException();
		}

		this.equalizer = equalizer;
	}

	public void setIncVolumeTime(short incVolumeTime) {
		if (incVolumeTime < 0) {
			throw new IllegalArgumentException();
		}

		this.incVolumeTime = incVolumeTime;

		if (incVolumeTime > 0) {
			setIncrementVolume(true);
		} else {
			setIncrementVolume(false);
		}
	}

	public void setRepeats(short repeats) {
		if (repeats < 0) {
			throw new IllegalArgumentException();
		}

		this.repeats = repeats;
	}

	/**
	 * 0=don't change volume, 100=max
	 */
	public void setVolume(byte volume) {
		if (volume < 0 || volume > 100) {
			throw new IllegalArgumentException();
		}

		this.volume = volume;

		if (volume == 0) {
			setDontChangeVolume(true);
		} else {
			setDontChangeVolume(false);
		}
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		buffer.put(this.volume);
		buffer.put(this.equalizer);
		buffer.putShort(this.playFlags.shortValue());
		buffer.putShort(this.incVolumeTime);
		buffer.putShort(this.repeats);
		buffer.putShort(this.delayBeforeRepeat);
		buffer.putShort(this.soundIndex);
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("soundIndex", this.soundIndex);
		builder.append("volume", this.volume);
		builder.append("equalizer", this.equalizer);
		builder.append("playFlags", this.playFlags);
		builder.append("incVolumeTime", this.incVolumeTime);
		builder.append("repeats", this.repeats);
		builder.append("delayBeforeRepeat", this.delayBeforeRepeat);

		return builder.toString();
	}

}
