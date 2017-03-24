/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.cardio2e.internal.code;

import java.util.ArrayList;
import java.util.List;
import java.util.EventListener;
import java.util.Vector;
import java.util.Enumeration;


/**
 * Decodes Cardio2e communication RS-232 protocol stream
 * and stores data in a Cardio2eTransaction subclass object 
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.7.0
 */


public class Cardio2eDecoder {
	public boolean decodeZonesStateTransaction = false; //Permits to disable security zones state transaction decode (saves CPU)
	public Cardio2eZonesMask zonesMask = new Cardio2eZonesMask(); //Mask for no detection zones states 
	private String streamBuffer = "";
	private transient Vector<Cardio2eDecodedTransactionListener> decodedTransactionListeners;

	public interface Cardio2eDecodedTransactionListener extends EventListener {
		public void decodedTransaction(Cardio2eDecodedTransactionEvent e);
	}
	
	synchronized public void addDecodedTransactionListener(Cardio2eDecodedTransactionListener l) {
	    if (decodedTransactionListeners == null) {
	    	decodedTransactionListeners = new Vector<Cardio2eDecodedTransactionListener>();
	    }
	decodedTransactionListeners.addElement(l);
	}

	synchronized public void removeDecodedTransactionListener(Cardio2eDecodedTransactionListener l) {
		if (decodedTransactionListeners == null) {
			decodedTransactionListeners = new Vector<Cardio2eDecodedTransactionListener>();
		}
		decodedTransactionListeners.removeElement(l);
	}

	@SuppressWarnings("unchecked")
	protected void signalDecodedTransaction(Cardio2eTransaction transaction) {
		if (decodedTransactionListeners != null && !decodedTransactionListeners.isEmpty()) {
			Cardio2eDecodedTransactionEvent event = new Cardio2eDecodedTransactionEvent(this, transaction);
			Vector<Cardio2eDecodedTransactionListener> targets; //make a copy of the listener list in case anyone adds/removes listeners
			synchronized (this) {
				targets = (Vector<Cardio2eDecodedTransactionListener>) decodedTransactionListeners.clone();
			}
			Enumeration<Cardio2eDecodedTransactionListener> e = targets.elements();
			while (e.hasMoreElements()) {
				Cardio2eDecodedTransactionListener l = (Cardio2eDecodedTransactionListener) e.nextElement();
				l.decodedTransaction(event);
			}
		}
	}
	
	public synchronized void decodeReceivedCardio2eStream(String cardioStream){
		//Accumulates partial receiver Cardio2e Streams y decodes complete transactions.
		int i = -1, f = -1;
		boolean incompleteTransactionFound = false;
		String transactionToDecode, transactionRests;
		if (this.streamBuffer.equals("")){
			i = cardioStream.indexOf(Cardio2eTransaction.CARDIO2E_START_TRANSACTION_INITIATOR);
			//If no cardio2eStream decode initiated will search CARDIO2E_START_TRANSACTION_INITIATOR
			//If is it found will trim chars before and stores rest in streamBuffer.
			if (i > -1){
				this.streamBuffer = cardioStream.substring(i);
				cardioStream = ""; //Prevents to be accumulated in streamBuffer if next lines were executed
			}
		}
		if (!this.streamBuffer.equals("")){
			//If there is Cardio2eStream decode initiated will accumulates cardioStream in streamBuffer
			this.streamBuffer = this.streamBuffer + cardioStream;
			//Do trim incomplete transaction if multiple initiator found before end of transaction in streamBuffer
			do{
				//Search for CARDIO2E_END_TRANSACTION_CHARACTER in Cardio2eTransaction
				f = this.streamBuffer.indexOf(Cardio2eTransaction.CARDIO2E_END_TRANSACTION_CHARACTER);
				//Search for a second transaction initiator
				if (this.streamBuffer.length() > 1) {
					i = this.streamBuffer.indexOf(Cardio2eTransaction.CARDIO2E_START_TRANSACTION_INITIATOR, 1);			
				}
				else{
					i = -1;
				}
				incompleteTransactionFound = ((i>-1) && (f>i));
				if (incompleteTransactionFound){ //Found incomplete transaction.
					//Trim up to new initiator and recheck.
					this.streamBuffer = this.streamBuffer.substring(i);
				}				
			}while (incompleteTransactionFound);
			if (f > -1){ //If CARDIO2E_END_TRANSACTION_CHARACTER was found will clean streamBuffer, decode complete transaction, and recursively call this same function to decode rests
				transactionToDecode = this.streamBuffer.substring(0, f+1);
				if (f < (streamBuffer.length()-1)){
					transactionRests = streamBuffer.substring(f+1);
				}
				else{
					transactionRests = "";
				}
				this.streamBuffer = "";
				decodeCardio2eTransaction (transactionToDecode);
				if (transactionRests.length() > 0) {
					decodeReceivedCardio2eStream(transactionRests);
				}
			}
		}
	}
	
	private void decodeCardio2eTransaction (String transactionToDecode){
		//Inits decoding complete transaction
		boolean decodeComplete = false;
		Cardio2eTransaction receivedCardio2eTransaction;
		Cardio2eTransactionTypes transactionType = null;
		Cardio2eObjectTypes objectType = null;
		List<String> digestedTransaction = new ArrayList<String>();
		//System.out.print ("Decoding "+transactionToDecode+" (-"); //Testing output
		//Splits tokens
		digestedTransaction = digestCardio2eTransaction (transactionToDecode);
		/*for (String value : digestedTransaction) { //Testing output
			System.out.print(value + "-");
		}
		System.out.print(")\n");*/
		if (digestedTransaction.size() >= 2) { //Will process transactions with a minimum of 2 tokens only
			//Decode transaction type
			try {
				if (digestedTransaction.get(0).length() == 1) {
					transactionType = Cardio2eTransactionTypes.fromSymbol(digestedTransaction.get(0).charAt(0));
				}
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			}
			//Decode object type
			try {
				if (digestedTransaction.get(1).length() == 1) {
					objectType = Cardio2eObjectTypes.fromSymbol(digestedTransaction.get(1).charAt(0));
				}
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			}
			//Decode object parameters
			receivedCardio2eTransaction = new Cardio2eTransaction();
			if ((transactionType != null) && (objectType != null)) { 
				switch (objectType){
				case DATE_AND_TIME:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eDateTimeTransaction receivedCardio2eDateTimeTransaction = new Cardio2eDateTimeTransaction();
					receivedCardio2eDateTimeTransaction.setTransactionType(transactionType);
					decodeComplete = decodeDateTimeParameters(receivedCardio2eDateTimeTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eDateTimeTransaction;
					//System.out.print("\n");//Testing output
					break;
				case HVAC_CONTROL:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eHvacControlTransaction receivedCardio2eHvacControlTransaction = new Cardio2eHvacControlTransaction();
					receivedCardio2eHvacControlTransaction.setTransactionType(transactionType);
					decodeComplete = decodeHvacControlParameters(receivedCardio2eHvacControlTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eHvacControlTransaction;
					//System.out.print("\n");//Testing output
					break;
				case HVAC_TEMPERATURE:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eHvacTemperatureTransaction receivedCardio2eHvacTemperatureTransaction = new Cardio2eHvacTemperatureTransaction();
					receivedCardio2eHvacTemperatureTransaction.setTransactionType(transactionType);
					decodeComplete = decodeHvacTemperatureParameters(receivedCardio2eHvacTemperatureTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eHvacTemperatureTransaction;
					//System.out.print("\n");//Testing output
					break;
				case LIGHTING:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eLightingTransaction receivedCardio2eLightingTransaction = new Cardio2eLightingTransaction();
					receivedCardio2eLightingTransaction.setTransactionType(transactionType);
					decodeComplete = decodeLightingParameters(receivedCardio2eLightingTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eLightingTransaction;
					//System.out.print("\n");//Testing output
					break;
				case LOGIN:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eLoginTransaction receivedCardio2eLoginTransaction = new Cardio2eLoginTransaction();
					receivedCardio2eLoginTransaction.setTransactionType(transactionType);
					decodeComplete = decodeLoginParameters(receivedCardio2eLoginTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eLoginTransaction;
					//System.out.print("\n");//Testing output
					break;
				case RELAY:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eRelayTransaction receivedCardio2eRelayTransaction = new Cardio2eRelayTransaction();
					receivedCardio2eRelayTransaction.setTransactionType(transactionType);
					decodeComplete = decodeRelayParameters(receivedCardio2eRelayTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eRelayTransaction;
					//System.out.print("\n");//Testing output
					break;
				case SCENARIO:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eScenarioTransaction receivedCardio2eScenarioTransaction = new Cardio2eScenarioTransaction();
					receivedCardio2eScenarioTransaction.setTransactionType(transactionType);
					decodeComplete = decodeScenarioParameters(receivedCardio2eScenarioTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eScenarioTransaction;
					//System.out.print("\n");//Testing output
					break;
				case SECURITY:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eSecurityTransaction receivedCardio2eSecurityTransaction = new Cardio2eSecurityTransaction();
					receivedCardio2eSecurityTransaction.setTransactionType(transactionType);
					decodeComplete = decodeSecurityParameters(receivedCardio2eSecurityTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eSecurityTransaction;
					//System.out.print("\n");//Testing output
					break;
				case ZONES:
					if (decodeZonesStateTransaction) {
						//System.out.print("Received "+transactionType+" "); //Testing output					
						//System.out.print(objectType+" "); //Testing output
						Cardio2eZonesTransaction receivedCardio2eZonesTransaction = new Cardio2eZonesTransaction();
						receivedCardio2eZonesTransaction.setTransactionType(transactionType);
						decodeComplete = decodeZonesParameters(receivedCardio2eZonesTransaction, digestedTransaction);
						receivedCardio2eTransaction = receivedCardio2eZonesTransaction;
						//System.out.print("\n");//Testing output
					}
					break;
				case ZONES_BYPASS:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eZonesBypassTransaction receivedCardio2eZoneBypassTransaction = new Cardio2eZonesBypassTransaction();
					receivedCardio2eZoneBypassTransaction.setTransactionType(transactionType);
					decodeComplete = decodeZoneBypassParameters(receivedCardio2eZoneBypassTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eZoneBypassTransaction;
					//System.out.print("\n");//Testing output
					break;
				case VERSION:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eVersionTransaction receivedCardio2eVersionTransaction = new Cardio2eVersionTransaction();
					receivedCardio2eVersionTransaction.setTransactionType(transactionType);
					decodeComplete = decodeVersionParameters(receivedCardio2eVersionTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eVersionTransaction;
					//System.out.print("\n");//Testing output
					break;
				case CURTAIN:
					//System.out.print("Received "+transactionType+" "); //Testing output					
					//System.out.print(objectType+" "); //Testing output
					Cardio2eCurtainTransaction receivedCardio2eCurtainTransaction = new Cardio2eCurtainTransaction();
					receivedCardio2eCurtainTransaction.setTransactionType(transactionType);
					decodeComplete = decodeCurtainParameters(receivedCardio2eCurtainTransaction, digestedTransaction);
					receivedCardio2eTransaction = receivedCardio2eCurtainTransaction;
					//System.out.print("\n");//Testing output
					break;
				default:
					break;
				}
				if (decodeComplete) {
					receivedCardio2eTransaction.primitiveStringTransaction = transactionToDecode;
					receivedCardio2eTransaction.isDataComplete = true;
					signalDecodedTransaction(receivedCardio2eTransaction); //Signals new decoded transaction by event.
					//System.out.println("Message successfully decoded to a "+receivedCardio2eTransaction.getClass()+".");//Testing output
				}
			}
		}
	}
	
	private List<String> digestCardio2eTransaction (String transactionToDigest){
		//Splits tokens in transaction 
		List<String> digestedTransaction = new ArrayList<String>();
		int i=-1;
		transactionToDigest = transactionToDigest.substring(1, (transactionToDigest.length()-1));
		while (transactionToDigest.length() > 0){
			i = transactionToDigest.indexOf(" ");
			if (i > -1){
				digestedTransaction.add(transactionToDigest.substring(0, i));
				transactionToDigest = transactionToDigest.substring(i + 1);
			}
			else{
				digestedTransaction.add(transactionToDigest);
				transactionToDigest = "";
			}
		}
		return digestedTransaction;
	}
	
	private boolean decodeNACK(Cardio2eTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		if (digestedTransaction.size() == 3) {
			try {
				receivedCardio2eTransaction.setErrorCode(Integer.parseInt(digestedTransaction.get(2)));
				//System.out.print(" "+receivedCardio2eTransaction.getErrorCodeDescription()); //Testing output
				decodeComplete = true;
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			}
		}
		if (digestedTransaction.size() == 4) {
			try {
				receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
				receivedCardio2eTransaction.setErrorCode(Integer.parseInt(digestedTransaction.get(3)));
				//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()+" "+receivedCardio2eTransaction.getErrorCodeDescription()); //Testing output
				decodeComplete = true;
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			}
		}
		return decodeComplete;	
	}
	
	private boolean decodeLightingParameters(Cardio2eLightingTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
		case GET:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case INFORMATION:
		case SET:
			if (digestedTransaction.size() == 4) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					receivedCardio2eTransaction.setLightIntensity (Byte.parseByte(digestedTransaction.get(3)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()+" "+receivedCardio2eTransaction.getLightIntensity()+"%"); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}

	private boolean decodeRelayParameters(Cardio2eRelayTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
		case GET:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case INFORMATION:
		case SET:
			if (digestedTransaction.size() == 4) {
				try {
					if (digestedTransaction.get(3).length()==1) {
						receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
						receivedCardio2eTransaction.setRelayState (Cardio2eRelayStates.fromSymbol(digestedTransaction.get(3).charAt(0)));
						//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()+" "+receivedCardio2eTransaction.getRelayState()); //Testing output
						decodeComplete = true;
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean decodeHvacTemperatureParameters(Cardio2eHvacTemperatureTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
		case GET:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case INFORMATION:
			if (digestedTransaction.size() == 5) {
				try {
					if (digestedTransaction.get(4).length()==1) {
						receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
						receivedCardio2eTransaction.setHvacTemperature(Double.parseDouble(digestedTransaction.get(3)));
						receivedCardio2eTransaction.setHvacSystemMode (Cardio2eHvacSystemModes.fromSymbol(digestedTransaction.get(4).charAt(0)));
						//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()+" "+receivedCardio2eTransaction.getHvacTemperature()+"ºC MODE "+receivedCardio2eTransaction.getHvacSystemMode()); //Testing output
						decodeComplete = true;
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}

	private boolean decodeHvacControlParameters(Cardio2eHvacControlTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
		case GET:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case INFORMATION:
		case SET:
			if (digestedTransaction.size() == 7) {
				try {
					if ((digestedTransaction.get(5).length()==1) && (digestedTransaction.get(6).length()==1)) {
						receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
						receivedCardio2eTransaction.setHvacHeatingSetPoint(Double.parseDouble(digestedTransaction.get(3)));
						receivedCardio2eTransaction.setHvacCoolingSetPoint(Double.parseDouble(digestedTransaction.get(4)));
						receivedCardio2eTransaction.setHvacFanState (Cardio2eHvacFanStates.fromSymbol(digestedTransaction.get(5).charAt(0)));
						receivedCardio2eTransaction.setHvacSystemMode (Cardio2eHvacSystemModes.fromSymbol(digestedTransaction.get(6).charAt(0)));
						//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()+" HEATING / COOLING SETPOINTS: "+receivedCardio2eTransaction.getHvacHeatingSetPoint()+" / "+receivedCardio2eTransaction.getHvacCoolingSetPoint()+"ºC FAN "+receivedCardio2eTransaction.getHvacFanState()+" MODE "+receivedCardio2eTransaction.getHvacSystemMode()); //Testing output
						decodeComplete = true;
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean decodeScenarioParameters(Cardio2eScenarioTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case SET:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			if (digestedTransaction.size() == 4) {
				try {
					if (digestedTransaction.get(3).length() <= Cardio2eTransaction.CARDIO2E_MAX_SECURITY_CODE_LENGTH) {
						receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
						receivedCardio2eTransaction.setSecurityCode(digestedTransaction.get(3));
						//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()+" USING SECURITY CODE "+receivedCardio2eTransaction.getSecurityCode()); //Testing output
						decodeComplete = true;
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean decodeZonesParameters(Cardio2eZonesTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		short objectNumber = 0;
		int maxZonesToDecode = 0;
		String parameters;
		int parametersLength;
		Cardio2eZoneStates zoneStates [];
		switch (receivedCardio2eTransaction.getTransactionType()){
		case GET:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case INFORMATION:
			if (digestedTransaction.size() == 4) {
				try {
					objectNumber = Short.parseShort(digestedTransaction.get(2));
					receivedCardio2eTransaction.setObjectNumber (objectNumber);
					if ((objectNumber >= Cardio2eTransaction.CARDIO2E_MIN_OBJECT_NUMBER) && (objectNumber <= Cardio2eTransaction.CARDIO2E_MAX_SECURITY_ZONE_NUMBER)) {
						maxZonesToDecode = (Cardio2eTransaction.CARDIO2E_MAX_SECURITY_ZONE_NUMBER-(objectNumber-1));
						parameters = digestedTransaction.get(3);
						parametersLength = parameters.length();
						if ((parametersLength > 0) && (parametersLength <= maxZonesToDecode)) {
							zoneStates = new Cardio2eZoneStates[parametersLength];
							for (int n = 0; n < parametersLength; n++) {
								char c = parameters.charAt (n);
								zoneStates[n] = Cardio2eZoneStates.fromSymbol(c);
								//System.out.print("#"+(objectNumber+n)+":"+zoneStates[n]+" "); //Testing output
							}
							receivedCardio2eTransaction.setZoneStates(zoneStates);
							receivedCardio2eTransaction.setZonesMask(zonesMask);
							decodeComplete = true;
						}	
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}

	private boolean decodeZoneBypassParameters(Cardio2eZonesBypassTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		short objectNumber = 0;
		int maxZonesBypassToDecode = 0;
		String parameters;
		int parametersLength;
		Cardio2eZoneBypassStates zoneBypassStates [];
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
		case GET:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case SET:
		case INFORMATION:
			if (digestedTransaction.size() == 4) {
				try {
					objectNumber = Short.parseShort(digestedTransaction.get(2));
					receivedCardio2eTransaction.setObjectNumber (objectNumber);
					if ((objectNumber >= Cardio2eTransaction.CARDIO2E_MIN_OBJECT_NUMBER) && (objectNumber <= Cardio2eTransaction.CARDIO2E_MAX_SECURITY_ZONE_NUMBER)) {
						maxZonesBypassToDecode = (Cardio2eTransaction.CARDIO2E_MAX_SECURITY_ZONE_NUMBER-(objectNumber-1));
						parameters = digestedTransaction.get(3);
						parametersLength = parameters.length();
						if ((parametersLength > 0) && (parametersLength <= maxZonesBypassToDecode)) {
							zoneBypassStates = new Cardio2eZoneBypassStates[parametersLength];
							for (int n = 0; n < parametersLength; n++) {
								char c = parameters.charAt (n);
								zoneBypassStates[n] = Cardio2eZoneBypassStates.fromSymbol(c);
								//System.out.print("#"+(objectNumber+n)+":"+zoneBypassStates[n]+" "); //Testing output
							}
							receivedCardio2eTransaction.setZoneBypassStates(zoneBypassStates);
							decodeComplete = true;
						}	
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}

	private boolean decodeDateTimeParameters(Cardio2eDateTimeTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
		case GET:
			decodeComplete = true;
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case INFORMATION:
		case SET:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setDateTime (new Cardio2eDateTime (digestedTransaction.get(2)));
					//System.out.print(receivedCardio2eTransaction.getDateTime()); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}
	private boolean decodeSecurityParameters(Cardio2eSecurityTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
		case GET:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					//System.out.print("STATE"); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case INFORMATION:
			if (digestedTransaction.size() == 4) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					if ((receivedCardio2eTransaction.getObjectNumber()==1) && (digestedTransaction.get(3).length()==1)) { //ObjectNumber must be 1 and security status length must also be 1
						receivedCardio2eTransaction.setSecurityState(Cardio2eSecurityStates.fromSymbol(digestedTransaction.get(3).charAt(0)));
						//System.out.print("STATE "+receivedCardio2eTransaction.getSecurityState()); //Testing output
						decodeComplete = true;
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
		case SET:
			if (digestedTransaction.size() == 5) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					if ((receivedCardio2eTransaction.getObjectNumber()==1) && (digestedTransaction.get(3).length()==1)) { //ObjectNumber must be 1 and security status length must also be 1
						receivedCardio2eTransaction.setSecurityState(Cardio2eSecurityStates.fromSymbol(digestedTransaction.get(3).charAt(0)));
						receivedCardio2eTransaction.setSecurityCode(digestedTransaction.get(4));
						//System.out.print("STATE "+receivedCardio2eTransaction.getSecurityState()+" USING CODE "+receivedCardio2eTransaction.getSecurityCode()); //Testing output
						decodeComplete = true;
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean decodeLoginParameters(Cardio2eLoginTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
			if (digestedTransaction.size() == 2) {
				try {
					//System.out.print("COMMAND ACEPTED"); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case INFORMATION: //Signals Login Information End (@I P E)
			if (digestedTransaction.size() == 3) {
				try {
					if (digestedTransaction.get(2).equals("E")) { 
						//System.out.print("END"); //Testing output
						decodeComplete = true;							
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case SET:
			if (digestedTransaction.size() == 4) { //LOGIN command (or LOGOUT, because Cardio2e ignores programCode in logout command)
				try {
					if ((digestedTransaction.get(2).length()==1) && (digestedTransaction.get(3).length() <= Cardio2eTransaction.CARDIO2E_MAX_PROGRAM_CODE_LENGTH)) { //Login command length must be 1 and code length <= CARDIO2E_MAX_PROGRAM_CODE_LENGTH
						receivedCardio2eTransaction.setLoginCommand (Cardio2eLoginCommands.fromSymbol(digestedTransaction.get(2).charAt(0)));
						receivedCardio2eTransaction.setProgramCode(digestedTransaction.get(3));
						//System.out.print("COMMAND: "+receivedCardio2eTransaction.getLoginCommand()+", USING PROGRAM CODE "+receivedCardio2eTransaction.getProgramCode()); //Testing output
						decodeComplete = true;
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			if (digestedTransaction.size() == 3) { //LOGOUT command (obsolete: only for legacy systems)
				try {
					if (digestedTransaction.get(2).length()==1)
					{
						receivedCardio2eTransaction.setLoginCommand (Cardio2eLoginCommands.fromSymbol(digestedTransaction.get(2).charAt(0)));
						//System.out.print("COMMAND: "+receivedCardio2eTransaction.getLoginCommand()); //Testing output
						decodeComplete = (receivedCardio2eTransaction.getLoginCommand() == Cardio2eLoginCommands.LOGOUT);
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}

	private boolean decodeCurtainParameters(Cardio2eCurtainTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
		case GET:
			if (digestedTransaction.size() == 3) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case INFORMATION:
		case SET:
			if (digestedTransaction.size() == 4) {
				try {
					receivedCardio2eTransaction.setObjectNumber (Short.parseShort(digestedTransaction.get(2)));
					receivedCardio2eTransaction.setOpeningPercentage (Byte.parseByte(digestedTransaction.get(3)));
					//System.out.print("#"+receivedCardio2eTransaction.getObjectNumber()+" OPENED AT "+receivedCardio2eTransaction.getOpeningPercentage()+"%"); //Testing output
					decodeComplete = true;
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean decodeVersionParameters(Cardio2eVersionTransaction receivedCardio2eTransaction, List<String> digestedTransaction) {
		boolean decodeComplete = false;  
		switch (receivedCardio2eTransaction.getTransactionType()){
		case ACK:
		case GET:
			if (digestedTransaction.size() == 3) {
				if (digestedTransaction.get(2).length() == 1) {
					try {
						receivedCardio2eTransaction.setVersionType(Cardio2eVersionTypes.fromSymbol(digestedTransaction.get(2).charAt(0)));
						//System.out.print(receivedCardio2eTransaction.getVersionType()); //Testing output
						decodeComplete = true;
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
					}
				}
			}
			break;
		case NACK:
			decodeComplete = decodeNACK(receivedCardio2eTransaction, digestedTransaction);
			break;
		case INFORMATION:
			if (digestedTransaction.size() == 4) {
				try {
					if (digestedTransaction.get(2).length()==1) {
						receivedCardio2eTransaction.setVersionType(Cardio2eVersionTypes.fromSymbol(digestedTransaction.get(2).charAt(0)));
						receivedCardio2eTransaction.setVersion(digestedTransaction.get(3));
						//System.out.print(receivedCardio2eTransaction.getVersionType()+"="+receivedCardio2eTransaction.getVersion()); //Testing output
						decodeComplete = true;
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
		return decodeComplete;
	}
}
