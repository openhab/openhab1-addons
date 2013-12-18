/**Copyright (c) 2010-${year}, openHAB.org and others.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
**/

package org.openhab.binding.wmbus.internal;

import gnu.io.SerialPort;

import java.io.*;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class holds the library to process a Frame received from a WMBus device.
 * It throws an Event holding the processed data.
 * 
 * @author Christoph Parnitzke
 *
 * @since 1.3.0
 */
public class WMBus {
	private static EventListenerList listeners = new EventListenerList();

	public final int AMB8425M_TEST_FRAME_SIZE = 16;
	public final int TEST_FRAME_SIZE = 12;
	public final int SND_NKE = 0x40;
	public final int SND_UD = 0x53;  // or 0x73
	public final int REQ_UD1 = 0x5A; // or 0x7A 
	public final int REQ_UD2 = 0x5B; // or 0x7B
	public final int ACK = 0x00; // or 0x10, 0x20, 0x30
	public final int CNF_IR = 0x06;
	public final int SND_NR = 0x44;
	public final int SND_IR = 0x46;
	public final int ACC_DMD = 0x48;
	public final int RSP_UD = 0x08; // or 0x18, 0x28, 0x38

	public static Frame myFrame;
	public static List<DataBlock> myData = new ArrayList<DataBlock>();
	public static List<Value> FinalValues = new ArrayList<Value>();
	public static int RSSI=0;

	public static String meterID="none";
	public static String serialN ="none";
	byte[] MUCmanufacturer = { (byte) 0xA4, 0x52 };
	byte[] MUCSerialN = { 0x60, 0x00, 0x00, 0x27 };
	byte[] MUCversionType = {0x01, 0x31};

	public static Logger logger = LoggerFactory.getLogger(WMBus.class);;
	
	

	public boolean CheckCRC16AMB845(byte[] data)
	{/*This function receives a complete block of bytes including the last two CRC bytes, and checks 
	 * the CRC returning a boolean variable.
	 * NOTE: This function is no longer used because the CRC is already calculated and 
	 * filtered out by the module.*/

		char crcReg=0;

		for (int i = 0; i < data.length-2; i++)
			crcReg = CalculateCRC16AMB845(crcReg, data[i]);
		crcReg = (char)~crcReg;
		if ((data[data.length - 2] == (byte)(crcReg >> 8)) && (data[data.length - 1] == (byte)crcReg))
			return true;
		else
			return false;
	}
	public char CalculateCRC16AMB845(char crcReg, byte crcData)
	{/*This function calculates the CRC of one Byte according to the CRC-polynom: 
	 * x16 + x13 + x12 + x11 + x10 + x8 + x6 + x5 + x2 + 1*/
		char CRC_POLYNOM = 0x3D65;

		for (int i = 0; i < 8; i++)
		{
			//If upper most bit is 1
			if ((((crcReg & 0x8000) >> 8) ^ (crcData & 0x80)) == 0x80)
				crcReg = (char)((crcReg << 1) ^ CRC_POLYNOM);
			else
				crcReg = (char)(crcReg << 1);

			crcData <<= 1;
		}
		return crcReg;
	}
	public void SendFrame(SerialPort mySerialPort, byte[] testFrame ) throws IOException
	{/*This function sends a frame through the indicated port*/
		DataOutputStream output = new DataOutputStream(mySerialPort.getOutputStream());
		output.write(testFrame, 0, testFrame.length);
	}        
	public void SetModeT2other(SerialPort mySerialPort) throws IOException
	{/*This function configures the wireless M-bus radio module AMB8425-M to operate in mode 
	 * T2-other*/
		byte[] T2otherFrame = {(byte) 0xFF, (byte) 0x09, (byte) 0x03, (byte) 0x46, (byte) 0x01, (byte) 0x08, (byte) 0xBA};
		DataOutputStream output = new DataOutputStream(mySerialPort.getOutputStream());
		output.write(T2otherFrame, 0, T2otherFrame.length);
		logger.warn("Set operation mode T2-other! \n");
	}
	public void SetModeT1meter(SerialPort mySerialPort) throws IOException
	{/*This function configures the wireless M-bus radio module AMB8425-M to operate in mode 
	 * T1-meter (unidirectional, only sending frames)*/
		byte[] T1meterFrame = { (byte) 0xFF, (byte) 0x09, (byte) 0x03, (byte) 0x46, (byte) 0x01, (byte) 0x05, (byte) 0xB7 };
		DataOutputStream output = new DataOutputStream(mySerialPort.getOutputStream());
		output.write(T1meterFrame, 0, T1meterFrame.length);
		logger.warn("Set operation mode T1-meter! \n");
	}
	public void getRSSI(SerialPort mySerialPort) throws IOException
	{/*This function request the RSSI level from the RF module*/
		byte[] RSSIframe = { (byte) 0xFF, (byte) 0x0D, (byte) 0x00, (byte) 0xF2 };
		DataOutputStream output = new DataOutputStream(mySerialPort.getOutputStream());
		output.write(RSSIframe, 0, RSSIframe.length);
	}
	public static synchronized void mySerialPort_DataReceived(DataInputStream result) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
	{/* This function handles data each time new data is received on the serial port */
		// AES keys
		byte[] CoronaKey = { (byte) 0x51, (byte) 0x72, (byte) 0x89, (byte) 0x10, (byte) 0xE6, (byte) 0x6D, (byte) 0x83, (byte) 0xF8, (byte) 0x51, (byte) 0x72, (byte) 0x89, (byte) 0x10, (byte) 0xE6, (byte) 0x6D, (byte) 0x83, (byte) 0xF8 };
		byte[] UnifloKey = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
		myFrame = new Frame();
		// int bytesToRead = i.available();
		byte[] length = new byte[1];
		length[0] = (byte)result.readByte();
		logger.warn("length is" + Integer.parseInt(String.valueOf(length[0])));
		myFrame.Length[0] = length[0];
		/* Receiving response from the Amber module when it is configured through serial port*/
		if (length[0] == 0xFF)       
		{
			byte[] response = new byte[3];
			response[0] = (byte)result.readByte();
			if (response[0] == 0x89)
				logger.warn("Confirmed! \n");
			if (response[0] == 0x8D)
			{                       
				response[1] = (byte)result.readByte();
				response[2] = (byte)result.readByte();  // RSSI value
				RSSI = (byte)(~response[2]+1);

				if (RSSI >= 128)
					RSSI = ((RSSI - 256) / 2 - 78);
				else
					RSSI = (RSSI / 2 - 78);
				logger.warn("RSSI:" + RSSI +"\n");
			}
			byte[] AmberResponse = null;
			result.read(AmberResponse);
		}
		else if (length[0] <= 16)   // Checking for minimum frame length
		{/*(9 bytes) DLL_Block + 
		 *(1 byte)  CI-Field + 
		 *(4 bytes) APL header length for CI-field:0x7A + 
		 *(2 bytes) AES-Check */
			logger.warn("Error: Wrong framesize! Discarding frame!\n");
			return;
		}
		else
		{
			byte[] bytes = new byte[length[0]];
			result.readFully(bytes);
			/*String output1 = "Length: \n";
			for (int run = 0; run < result.available(); run++)
			{
				bytes[run] = (byte) result.readByte();

				output1 += String.format("0x%x ", bytes[run]);
			}
			output1 += ("\n");
			logger.warn(output1);*/
			//*****************************************************
			System.arraycopy(bytes, 0, myFrame.CField, 0, 1);
			WriteConsole("C_Field", myFrame.CField);                    
			System.arraycopy(bytes, 1, myFrame.MField, 0, 2);
			WriteConsole("M_Field", myFrame.MField);
			//Identifying the device
			if (myFrame.MField[0] == (byte)0x24)
				meterID = "Corona";
			if (myFrame.MField[0] == (byte)0x8f)
				meterID = "Uniflo";
			//Address field:  Identification number + Version + Device Type 
			System.arraycopy(bytes, 3, myFrame.AField, 0, 6);
			WriteConsole("A_Field", myFrame.AField);
			byte[] serial = new byte[4];
			System.arraycopy(bytes, 3, serial, 0, 4);
			serialN = new String(serial, "UTF-8");  //getting the identification number
			//only to store and print the DLL header
			byte[] DLL_Block = new byte[12];
			System.arraycopy(length, 0, DLL_Block, 0, 1);
			System.arraycopy(bytes, 0, DLL_Block, 1, 9);
			WriteConsole("Block1", DLL_Block);

			//*****************************************************
			//Reading bytes of the Application Layer header
			//*****************************************************
			System.arraycopy(bytes, 9, myFrame.CIField, 0, 1);
			WriteConsole("CI_Field", myFrame.CIField);
			switch (myFrame.CIField[0])
			{
			case 0x7A:
				System.arraycopy(bytes, 10, myFrame.ACC, 0, 1);
				WriteConsole("Access number", myFrame.ACC);
				System.arraycopy(bytes, 11, myFrame.Status, 0, 1);
				WriteConsole("Status", myFrame.Status);
				System.arraycopy(bytes, 12, myFrame.Signature, 0, 2);
				WriteConsole("Signature word", myFrame.Signature);
				System.arraycopy(bytes, 14, myFrame.AES, 0, 2);
				WriteConsole("AES-Check", myFrame.AES);
				break;
			}
			//*****************************************************
			//Building Initialisation vector
			//*****************************************************
			IvParameterSpec ivSpec;
			byte[] IV = new byte[16];
			System.arraycopy(myFrame.MField, 0, IV, 0, 2);
			System.arraycopy(myFrame.AField, 0, IV, 2, 6);
			for (int j = 0; j < 8; j++)
				System.arraycopy(myFrame.ACC, 0, IV, 8 + j, 1);
			WriteConsole("IV", IV);
			ivSpec = new IvParameterSpec(IV);
			//*****************************************************
			//Building AES coded blocks
			//*****************************************************
			/*This part copies all data blocks (16 bytes each one) to a new byte array 
			 * according to the number of data blocks that is indicated in the 4 upper 
			 * bits of the lower byte of the signature world. 
			 * The lower 4 bits of the upper byte indicate the encryption mode. Corona 
			 * and Uniflow meters operate in mode 5. */
			int aesBlocksLength = 16 * ((myFrame.Signature[0]) >> 4);
			byte[] AESblocks = new byte[aesBlocksLength];
			System.arraycopy(bytes, 14, AESblocks, 0, bytes.length-14);
			WriteConsole("AES Block crypted:", AESblocks);
			logger.warn("AES Block Length: " + AESblocks.length);
			byte[] AESplain = new byte[AESblocks.length];
			//*****************************************************
			// AES Decrypting 
			//*****************************************************
			if (myFrame.MField[0] == (byte)0x24 && myFrame.MField[1] == (byte)0x23)
				AESplain = AES_decryption(AESblocks, CoronaKey, ivSpec);
			else if (myFrame.MField[0] == (byte)0x8f && myFrame.MField[1] == (byte)0x19)
				AESplain = AES_decryption(AESblocks, UnifloKey, ivSpec);
			else
				logger.warn("AES key not known");
			
			WriteConsole("AES Block plain:", AESplain);

			/*AES verification. First 2 bytes of input array should be 0x2f. 
			 * If not, it is discarded */
			if (AESplain[0] == 0x2F && AESplain[1] == 0x2F)
			{
				//Unpacking data
				logger.warn("Unpacking Data...");
				UnpackData(AESplain);
				//Decoding data blocks
				FinalValues.clear();
				for(int k = 0; k < myData.size(); k++){
					DecodeDataBlock(myData.get(k));
				}

				//Print  values
				String output = "";
				output += ("Final Values: " + "\n");
				for (int k = 0; k < FinalValues.size(); k++)
				{
					output += (FinalValues.get(k).storageNum + " ");
					output += (FinalValues.get(k).value + "\t");
					if (FinalValues.get(k).tariff != 0)
						output += (FinalValues.get(k).tariff + "\t");
					output += (FinalValues.get(k).description + "\n");
				}
				output += ("\n");
				logger.warn(output);

				WMBus w = new WMBus();
				FinalValuesEvent e = new FinalValuesEvent(w, FinalValues, meterID, serialN);
				notifyFinalValues(e);
			}
			else
				logger.warn("Invalid decryption verification");
		}
	}
	public static void WriteConsole(String name, byte[] field)
	{
		String output = "";
		output += (name+ ": ");
		for (int run = 0; run < field.length; run++)
			output += String.format("0x%x ", field[run]);
		output += "\n";
		logger.warn(output);
	}
	public static void UnpackData(byte[] data)
	{/* This function separates decrypted Data Blocks (records) into different DataBlock objects 
	 * and arranges them into a DataBlock list called myData.
	 * According to EN 13757-3, each data record has a structure like: 
	 * DIF - DIFE - VIF - VIFE - DATA. Therefore the input data is processed following that order.*/
	 //TODO: Zuletzt bearbeitetes Binding
		myData.clear();
		int datIndx = 2;
		int lstIndx = 0;
		int secIndx = 0;
		boolean fixTry = false;
		logger.warn("Data length: "+data.length);
		while (datIndx < data.length){
			if (secIndx == datIndx){
				if(!fixTry && data.length-datIndx > 2){
					fixTry = true;
					logger.warn("Unpacking failure detected, try to fix!");
					datIndx++;
				}	
				else{
					if(fixTry && data.length-datIndx > 2){
						logger.warn("Unpacking interrupted to avoid heap overflow!");
						break;
					}
					else if(fixTry && !(data.length-datIndx > 2)){
						logger.warn("Unpacking interrupted! Too less bytes left!");
						break;
					}
					else{
						logger.warn("Unpacking interrupted to avoid heap overflow!");
						break;
					}
				}
			}
			else {
				fixTry = false;
			}
			secIndx = datIndx;
			logger.warn("Actual data index: "+datIndx);
			if (data[datIndx] == 0x2F)
			{
				datIndx++;
				continue;
			}
			myData.add(new DataBlock());
			boolean variableLength = false;
			/*DIF (Data Information Field) decoding. This field contains length and 
			 * coding of the data, function field, LSB of storage number and indicates
                 in its MSbit if a DIFE follows.*/
			switch (data[datIndx] & 0x0F)//DIF: Data length and type decoding                         
			{
			case 0x00:
				
				myData.get(lstIndx).length = 1;
				myData.get(lstIndx).datTyp = DataType.ND;
				break;
			case 0x01:
				myData.get(lstIndx).length = 1;
				myData.get(lstIndx).datTyp = DataType.B;
				break;
			case 0x02:
				myData.get(lstIndx).length = 2;
				myData.get(lstIndx).datTyp = DataType.B;
				break;
			case 0x03:
				myData.get(lstIndx).length = 3;
				myData.get(lstIndx).datTyp = DataType.B;
				break;
			case 0x04:
				myData.get(lstIndx).length = 4;
				myData.get(lstIndx).datTyp = DataType.B;
				break;
			case 0x06:
				myData.get(lstIndx).length = 6;
				myData.get(lstIndx).datTyp = DataType.B;
				break;
			case 0x07:
				myData.get(lstIndx).length = 8;
				myData.get(lstIndx).datTyp = DataType.B;
				break;
			case 0x09:
				myData.get(lstIndx).length = 1;
				myData.get(lstIndx).datTyp = DataType.A;
				break;
			case 0x0A:
				myData.get(lstIndx).length = 2;
				myData.get(lstIndx).datTyp = DataType.A;
				break;
			case 0x0B:
				myData.get(lstIndx).length = 3;
				myData.get(lstIndx).datTyp = DataType.A;
				break;
			case 0x0C:
				myData.get(lstIndx).length = 4;
				myData.get(lstIndx).datTyp = DataType.A;
				break;
			case 0x0D:// Variable length
				variableLength = true;
				break;
			case 0x0F:// special functions
				continue;
			default:
				continue;
			}
			myData.get(lstIndx).LSBstorageNum = (data[datIndx] & 0x40) >> 6; // DIF: LSB of Storage Number
			switch ((data[datIndx] & 0x30) >> 4)//DIF: Function field decoding                         
			{
			case 0x00:
				myData.get(lstIndx).Func = FunctionValue.Instant;
				break;
			case 0x01:
				myData.get(lstIndx).Func = FunctionValue.Max;
				break;
			case 0x02:
				myData.get(lstIndx).Func = FunctionValue.Min;
				break;
			case 0x03:
				myData.get(lstIndx).Func = FunctionValue.Error;
				break;
			}
			/* DIFE ((Data Information Field Extension) contains upper bits of storage number and tariff 
			 * among others.*/
			while (((data[datIndx])>>7) == 1)// DIFE: Is there an extension bit?
			{
				myData.get(lstIndx).DIFE.add(data[datIndx + 1]);
				datIndx++;
			}
			//logger.warn("DIFE succesfully set");
			datIndx++;
			/* VIF (Value Information Field) contains unit and multiplier of the transmitted value. */ 
			myData.get(lstIndx).VIF = data[datIndx];
			/* VIFE ((Value Information Field Extension) */
			while (((data[datIndx])>>7) == 1)// VIFE: Is there an extension bit?
			{
				myData.get(lstIndx).VIFE.add(data[datIndx + 1]);
				datIndx++;
			}
			//logger.warn("VIFE succesfully set");
			datIndx++;
			if (variableLength)
			{
				myData.get(lstIndx).length = data[datIndx];
				datIndx++;
			}
			for (int i = 0; i < myData.get(lstIndx).length; i++)
			{
				myData.get(lstIndx).value.add(data[datIndx + i]);
			}
			datIndx += myData.get(lstIndx).length;
			lstIndx++;
		}
		logger.warn("Unpacking successful!");
	}
	public static void DecodeDataBlock(DataBlock myDataBlock)
	{/* This function decodes a single Data Block and extracts and stores the information 
	 * in a readable way into a list of Value objects. 
	 */
		logger.warn("Decoding unpacked DataBlock");
		WMBus.FinalValues.add(new Value());

		// Extracting value
		Byte[] rawValue = myDataBlock.value.toArray(new Byte[myDataBlock.value.size()]);     // Getting storage number
		int sto = getStorageNumber(myDataBlock.DIFE.toArray(new Byte[myDataBlock.DIFE.size()]), myDataBlock.LSBstorageNum);
		WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).storageNum = sto;
		// Getting tariff
		WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).tariff = getTariff(myDataBlock.DIFE.toArray(new Byte[myDataBlock.DIFE.size()]));

		/* According to the coding of data records, the raw value may be decoded in different ways, 
		 * then depending on the coding type, raw values are translated into final values using the 
		 * getType functions and applying the right multiplier and unit from the VIF. Refer to table 9 in 
		 * EN 13757-3:2004    */

		if (myDataBlock.datTyp == DataType.A)   //BCD values
		{   
			long value = 0;
			double mult;

			value = getTypeA(rawValue);
			/* Calculating multipliers. */
			if (((myDataBlock.VIF & 0x7F) >> 3) == 0x02)        // Volume [m^3] 
			{
				WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).description = "Volume";
				mult = Math.pow(10, ((myDataBlock.VIF & 0x07) - 6));               
				WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).value = new Double(value * mult).toString();
			}
			if (((myDataBlock.VIF & 0x7F) >> 3) == 0x07)        // Volume Flow [m^3/h] 
			{
				WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).description = "Volume Flow";
				mult = Math.pow(10, ((myDataBlock.VIF & 0x07) - 6));
				WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).value = new Double(value * mult).toString();                  
			}
			if (((myDataBlock.VIF & 0x7F) >> 3) == 0x00)        // Energy [Wh] 
			{
				WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).description = "Energy";
				mult = Math.pow(10, ((myDataBlock.VIF & 0x07) - 6));
				WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).value = new Double(value * mult).toString();
			}
			if (myDataBlock.VIFE.size()>0)
				if (myDataBlock.VIFE.get(0) == 0x3A)   //VIF contains uncorrected unit instead of corrected unit
					WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).description += " uncorrected unit"; 
		}

		if (myDataBlock.VIF == 0x6C)    // Date Type G
		{
			WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).description = "Date";
			WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).value = getTypeG(rawValue);
		}
		if (myDataBlock.VIF == 0x6D)    // Extented Date and Time Point
		{
			if (myDataBlock.length == 0x06) // Date Type I
			{
				WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).description = "Date and Time";
				WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).value = getTypeI(rawValue);
			}
		}
		if (myDataBlock.datTyp == DataType.B)   //Binary Integer values
		{
			if (myDataBlock.VIF == 0xFD && myDataBlock.VIFE.get(0) == 0x74)//Remaining battery life time (days)
			{
				WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).description = "Remaining battery life time (days)";
				WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).value = new Long(getTypeB(rawValue)).toString();
			}

		}
		if (myDataBlock.VIF == 0x78)    // Fabrication N.
		{
			WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).description = "Fabrication Number";
			Collections.reverse(myDataBlock.value);
			rawValue = myDataBlock.value.toArray(new Byte[myDataBlock.value.size()]);
			WMBus.FinalValues.get(WMBus.FinalValues.size() - 1).value = BuildStrfrByteArr(rawValue);
		}

	}
	public static int getStorageNumber(Byte[] DIFE, int LSB)// Decode the storage number
	{/* This function takes the LSBit of the storage number that is contained in the DIF field 
	 * and the upper bits in the DIFE field to build the complete number.       
	 */ 
		int sto=0;
		for (int i = 0; i < DIFE.length; i++)
			sto = (sto<<4) + (DIFE[i] & 0x0F);
		sto = (sto<< 1) + LSB;
		return sto;
	}
	public static int getTariff(Byte[] DIFE)// Decode the storage number
	{/* This function calculates the tariff of the value that is indicated in bits 4 and 5
	 * of the DIFE */
		int tariff = 0;
		for (int i = 0; i < DIFE.length; i++)
			tariff = (tariff << 2) + ((DIFE[i] & 0x30)>>4);
		return tariff;
	} 

	/* The following funtions are used to decode data records according to its coding type.
	 * Refer to AnnexA of EN 13757-3:2004 */
	public static long getTypeA(Byte[] data)//Unsigned Integer BCD
	{
		long value = 0;
		for (int i = data.length-1; i >= 0; i-- )
		{
			int digit1 = data[i] >> 4;
		int digit2 = data[i] & 0x0f;
		value = (value * 100) + digit1 * 10 + digit2;
		}
		return value;
	}
	public static long getTypeB(Byte[] data)//Binary Integer
	{
		long value = 0;
		int sign = 0;

		if ((data[data.length - 1] & 0x80) == 0x80)     //Most significant bit: 0-positive, 1-negative
		{
			data[data.length - 1] = (byte)(data[data.length - 1] & 0x7F);
			sign = 1;
		}              
		for (int i = data.length - 1; i >= 0; i--)
			value = (value << 8) + data[i];

		if (sign ==1)
			value = (~value)+ 1;    // two's complement

		return value;
	}
	public static String getTypeG(Byte[] data)//Compound CP16: Date
	{
		String date="";
		int day, month, year;

		year = ((data[0] & 0xE0)>>5) + ((data[1]& 0xF0)>>1);
		if (year == 127)
			date = "every year";
		else
			date = new Integer(year+2000).toString();

		month = data[1] & 0x0F;
		if (month == 15)
			date = date + "." + "every month";
		else
			date = date + "." + new Integer(month).toString();

		day = data[0] & 0x1F;
		if (day == 0)
			date = date + "." + "every day";
		else
			date = date + "." + new Integer(day).toString();

		return date;
	}
	public static String getTypeI(Byte[] data)//Compound CP16: Date and time
	{
		String dateTime = "";
		int day, month, year, hour, minute, second, weekday, week, daylight;
		String sday, smonth, syear, shour, sminute, ssecond; 
		String sweekday, sweek, stimeInvalid, ssummerTime, sleapYear, sdaylight;

		year = ((data[3] & 0xE0) >> 5) + ((data[4] & 0xF0) >> 1);
		if (year == 127)
			syear = " "; // Not specified
		else
			syear = new Integer(year + 2000).toString();

		month = data[4] & 0x0F;
		if (month == 0)
			smonth = " "; // Not specified
		else
			smonth = new Integer(month).toString();

		day = data[3] & 0x1F;
		if (day == 0)
			sday = " "; // Not specified
		else
			sday = new Integer(day).toString();

		hour = data[2] & 0x1F;
		if (hour == 31)
			shour = "every hour";
		else
			shour = new Integer(hour).toString();

		minute = data[1] & 0x3F;
		if (minute == 63)
			sminute = "every minute";
		else
			sminute = new Integer(minute).toString();

		second = data[0] & 0x3F;
		if (second == 63)
			ssecond = "every second";
		else
			ssecond = new Integer(second).toString();

		dateTime = syear + "." + smonth + "." + sday + " " + shour + ":" + sminute + ":" + ssecond;

		//Day of the week
		weekday = data[2] & 0xE0;
		switch ((int)(weekday>>5))
		{
		case 1: sweekday = "Monday"; break;
		case 2: sweekday = "Tuesday"; break;
		case 3: sweekday = "Wednesday"; break;
		case 4: sweekday = "Thursday"; break;
		case 5: sweekday = "Friday"; break;
		case 6: sweekday = "Saturday"; break;
		case 7: sweekday = "Sunday"; break;
		default: sweekday = ""; break; // Not specified
		}

		week = data[5] & 0x3F;
		if (week == 0)
			sweek = ""; // Not specified
		else
			sweek = " week: " + new Integer(week).toString();

		if ((data[1] & 0x80) == 0x80)
			stimeInvalid = " invalid time";
		else
			stimeInvalid = " valid time";

		if ((data[0] & 0x40) == 0x40)
			ssummerTime = " summer time";
		else
			ssummerTime = "";

		if ((data[0] & 0x80) == 0x80)
			sleapYear = " leap year";
		else
			sleapYear = "";

		if ((data[1] & 0x40) == 0x40)
			sdaylight = " +";
		else
			sdaylight = " -";

		daylight = (data[5] & 0xC0)>>6;
		if (daylight == 0)
			sdaylight = "";
		else
			sdaylight = sdaylight +  new Integer(daylight).toString();


		String ext =  sweek + stimeInvalid + sleapYear + ssummerTime + sdaylight;

		return dateTime + sweekday + ext;
	}

	public static byte[] AES_decryption(byte[] aesBlocks, byte[] coronaKey, IvParameterSpec ivSpec) throws ArrayIndexOutOfBoundsException, IllegalStateException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
	{/* This function decryptes data using the encryption key and the initialisation vector (VI)
	 * provided. The algorithm takes a single block of 16 bytes
	 * and performs the AES128-decryption. Due to the WM-Bus protocol uses CBC (Cipher Block Chaining)
	 * the decrypted output should be XORed with the previous ciphertext block before being decrypted 
	 * again, as it is shown in the link: 
	 * http://en.wikipedia.org/wiki/Block_cipher_modes_of_operation#Cipher-block_chaining_.28CBC.29 */
		
		//Cipher a = Cipher.getInstance("AES/CBC/PKCS5Padding");
		Cipher a = Cipher.getInstance("AES/CBC/NoPadding");
		byte[] aesPlain = new byte[aesBlocks.length];
		//byte[] encrypted = new byte[16];
		//byte[] decrypted = new byte[16];
		//Byte[] plain = new Byte[16];
		SecretKey aesKey = new SecretKeySpec(coronaKey, "AES");
		AlgorithmParameters.getInstance("AES"); //Maybe not needed?
		a.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);

		aesPlain = a.doFinal(aesBlocks);
		WriteConsole("Decrypted Message: ", aesPlain);
		/*for (int i=0;i<aesBlocks.length/16;i++)
		{
			System.arraycopy(aesBlocks, 16*i, encrypted, 0, 16);
			WriteConsole("Encrypted Bytearray", encrypted);
			decrypted = a.update(encrypted);

			//for (int j = 0; j < 16; j++)
				//plain[j] = (byte)(ivSpec.toString().getBytes()[j] ^ decrypted[j]);

			System.arraycopy(decrypted, 0, aesPlain, 16*i, 16);

			System.arraycopy(encrypted, 0, ivSpec, 0, 16);  // New initialisation vector
			logger.warn("number of successful decrypted blocks: " + i);
		}*/
		return aesPlain;
	}
	public static String BuildStrfrByteArr(Byte[] input)
	{
		String erg = "";
		for(int l=0; l<input.length ;l++) {
			erg += Byte.toString(input[l]);
		}
		return erg;
	}
	public static void addListener(FinalValuesEventListener listener)
	{
		logger.warn("listener added");
		listeners.add(FinalValuesEventListener.class, listener);	
	}
	public static void removeListener(FinalValuesEventListener listener)
	{
		logger.warn("listener removed");
		listeners.remove(FinalValuesEventListener.class, listener);
	}
	protected static synchronized void notifyFinalValues(FinalValuesEvent event)
	{
		logger.warn("notification fired!");
		for (FinalValuesEventListener l : listeners.getListeners(FinalValuesEventListener.class))
			l.receiveFinalValues(event);
	}
}
