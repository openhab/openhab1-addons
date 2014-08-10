/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.dscalarm.internal.protocol;

/**
 * Class to create a API Command string 
 * @author Russell Stephens
 * @author Donn Renk
 * @since 1.6.0
 */
public class APICommand {

	private String apiCommand;
	private String apiData;
	private String apiChecksum;
	private String apiTerminator = "\r\n";
	
	/**
	 * Basic constructor to create an API Command
	 */
	APICommand() {
	}

	public String getAPICommand() {
		StringBuffer apiCommandSB = new StringBuffer();

		apiCommandSB.append(apiCommand);
		apiCommandSB.append(apiData);
		apiCommandSB.append(apiChecksum);
		apiCommandSB.append(apiTerminator);
		
		return apiCommandSB.toString();
	}
	
	public void setAPICommand(String command, String data) {
		this.apiCommand = command;
		this.apiData = data;
		calculateChecksum();
	
	}

	private void calculateChecksum() {
        int checkSum;
        int runningTotal = 0;
        apiChecksum = "ZZ";
  
	     for(byte s : apiCommand.getBytes()) {
	         runningTotal = s + runningTotal;
	     }
	     for(byte s : apiData.getBytes()) {
	         runningTotal = s + runningTotal;
	     }
  
		 checkSum = runningTotal;
	     String hexCheckSum = Integer.toHexString(checkSum);
	     hexCheckSum = hexCheckSum.substring(hexCheckSum.length() - 2).toUpperCase();
	
	     apiChecksum = hexCheckSum;
    }

	@Override
	public String toString() {
		return getAPICommand();
	}
}
