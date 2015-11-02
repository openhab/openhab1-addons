/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfJsonMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;

/**
 * An abstract class to allow easier processing of JSON like  messsages
 * received from the Lightwave Wifi Link
 * 
 * @author Neil Renaud
 * @since 1.8.0
 * 
 */
public abstract class AbstractLightwaveRfJsonMessage implements LightwaveRFCommand {

	private static final Pattern MESSAGE_ID_REG_EXP = Pattern
			.compile(".*\"trans\":([^,}]*).*");

	
	private final LightwaveRfMessageId messageId;

	public AbstractLightwaveRfJsonMessage(String message) throws LightwaveRfMessageException {
		messageId = getMessageIdFromString(MESSAGE_ID_REG_EXP, message);
	}
	
	@Override
	public final LightwaveRfMessageId getMessageId() {
		return messageId;
	}
	
	private LightwaveRfJsonMessageId getMessageIdFromString(Pattern regExp, String message) throws LightwaveRfMessageException{
		return new LightwaveRfJsonMessageId(getIntFromText(regExp, message));		
		
	}
	
	protected String getStringFromText(Pattern regExp, String message) throws LightwaveRfMessageException {
		try{
			Matcher matcher = regExp.matcher(message);
			matcher.matches();
			return matcher.group(1);
		}
		catch(IllegalStateException e){
			throw new LightwaveRfMessageException("Error parsing message for regExp[" + regExp + "] message[" + message + "]", e);
		}
	}

	protected Date getDateFromText(Pattern regExp, String message) throws LightwaveRfMessageException {
		long timeInSeconds = getLongFromText(regExp, message);
		long timeInMillis = timeInSeconds * 1000;
		return new Date(timeInMillis);
	}

	protected int getIntFromText(Pattern regExp, String message) throws LightwaveRfMessageException {
		return Integer.valueOf(getStringFromText(regExp, message));
	}

	protected double getDoubleFromText(Pattern regExp, String message) throws LightwaveRfMessageException {
		return Double.valueOf(getStringFromText(regExp, message));
	}
	
	protected long getLongFromText(Pattern regExp, String message) throws LightwaveRfMessageException {
		return Long.valueOf(getStringFromText(regExp, message));
	}
	
	protected long getLightwaveDateFromJavaDate(Date javaDate){
		return javaDate.getTime() / 1000;
	}
}
