/**
 *  Written by Tom Gutwin - WebARTS Design.
 *  Copyright (C) 2012 WebARTS Design, North Vancouver Canada
 *  http://www.webarts.bc.ca
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without_ even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package ca.bc.webarts.tools.eiscp;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.onkyo.internal.eiscp.EiscpCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p><b>Note:</b>This class was originally developed by Tom Gutwin but suffered heavy 
 * refactorings recently.</p><br> 
 * 
 * A class that wraps the communication to Onkyo/Integra devices using the
 * ethernet Integra Serial Control Protocal (eISCP).
 * 
 * <p>
 * The Message packet looks like:<br />
 * <img src=
 * "http://tom.webarts.ca/_/rsrc/1320209141605/Blog/new-blog-items/javaeiscp-integraserialcontrolprotocol/eISCP-Packet.png"
 * border="1"/> <br />
 * </p>
 * See also <a href=
 * "http://tom.webarts.ca/Blog/new-blog-items/javaeiscp-integraserialcontrolprotocol">tom.webarts.ca</a> writeup.
 * 
 * @author Tom Gutwin P.Eng
 * @author Thomas.Eichstaedt-Engelen (Refactorings)
 */
public class Eiscp {
	
	private static Logger logger = LoggerFactory.getLogger(Eiscp.class);
	
	/** the timeout in ms for socket reads. */
	private static int SOCKET_TIME_OUT = 500;
	
	/** Var to hold the volume level to or from a message. **/
	private static int DEFAULT_VOLUME = 32;
	
	/** default receiver IP Address. **/
	public static final String DEFAULT_EISCP_IP = "10.0.0.203";
	/** Instantiated class IP for the receiver to communicate with. **/
	private String receiverIP = DEFAULT_EISCP_IP;

	/** default eISCP port. **/
	public static final int DEFAULT_EISCP_PORT = 60128;
	/** Instantiated class Port for the receiver to communicate with. **/
	private int receiverPort = DEFAULT_EISCP_PORT;	
	
	/**
	 * the socket for communication - the protocol spec says to use one socket
	 * connection AND HOLD ONTO IT for re-use.
	 */
	private static Socket eiscpSocket = null;
	
	private static ObjectOutputStream outStream = null;
	private static DataInputStream inStream = null;
	private static boolean connected = false;


	/**
	 * Constructor that takes your receivers ip and port.
	 **/
	public Eiscp(String ip, int eiscpPort) {
		if (StringUtils.isNotBlank(ip)) {
			receiverIP = ip;
		}
		if (eiscpPort >= 1) {
			receiverPort = eiscpPort;
		}
	}

		
	/**
	 * Connects to the receiver by opening a socket connection through the
	 * DEFAULT IP and DEFAULT eISCP port.
	 **/
	public boolean connectSocket() {
		return connectSocket(null, -1);
	}

	/**
	 * Connects to the receiver by opening a socket connection through the eISCP
	 * port.
	 **/
	public boolean connectSocket(String ip, int eiscpPort) {
		if (StringUtils.isBlank(ip)) {
			ip = receiverIP;
		}
		if (eiscpPort < 1) {
			eiscpPort = receiverPort;
		}

		if (eiscpSocket == null || !connected || !eiscpSocket.isConnected())
			try {
				// 1. creating a socket to connect to the server
				eiscpSocket = new Socket(ip, eiscpPort);
				logger.debug("Connected to {} on port {}", ip, eiscpPort);
				// 2. get Input and Output streams
				outStream = new ObjectOutputStream(eiscpSocket.getOutputStream());
				inStream = new DataInputStream(eiscpSocket.getInputStream());

				// System.out.println("out_Init");
				outStream.flush();
				// System.out.println("inInit");
				connected = true;
			} catch (UnknownHostException unknownHost) {
				logger.error("You are trying to connect to an unknown host!", unknownHost);
			} catch (IOException ioException) {
				logger.error("Can't connect: " + ioException.getMessage());
			}
		return connected;
	}

	/**
	 * test the connection to the receiver by opening a socket connection
	 * through the eISCP port AND THEN CLOSES it if it was not already open.
	 * 
	 * @return true if already connected or can connect, and false if can't
	 *         connect
	 **/
	public boolean testConnection(String ip, int eiscpPort) {
		boolean retVal = false;
		
		if (ip == null || ip.equals("")) {
			ip = DEFAULT_EISCP_IP;
		}
		
		if (eiscpPort == 0) {
			eiscpPort = DEFAULT_EISCP_PORT;
		}

		if (connected) {
			// test existing connection
			if (eiscpSocket.isConnected()) {
				retVal = true;
			}
		} else {
			// test a new connection
			try {
				// 1. creating a socket to connect to the server
				eiscpSocket = new Socket(ip, eiscpPort);
				if (eiscpSocket != null) {
					eiscpSocket.close();
				}
				retVal = true;
			} catch (UnknownHostException unknownHost) {
				logger.error("You are trying to connect to an unknown host!", unknownHost);
			} catch (IOException ioException) {
				logger.error("Can't connect: " + ioException.getMessage());
			}
		}
		
		return retVal;
	}

	/**
	 * Closes the socket connection.
	 * 
	 * @return true if the closed successfully
	 **/
	public boolean closeSocket() {
		// 4: Closing connection
		try {
			if (inStream != null) {
				inStream.close();
				inStream = null;
				logger.debug("closed input stream!");
			}
			if (outStream != null) {
				outStream.close();
				outStream = null;
				logger.debug("closed output stream!");
			}
			if (eiscpSocket != null) {
				eiscpSocket.close();
				eiscpSocket = null;
				logger.debug("closed socket!");
			}
			connected = false;
		} catch (IOException ioException) {
			logger.error("Closing connection throws an exception!", ioException);
		}
		
		return connected;
	}

	/**
	 * Converts an ascii decimal String to a hex String.
	 * 
	 * @param String
	 *            holding the string to convert to HEX
	 * @param boolean flag to turn some debug output on/off
	 * @return a string holding the HEX representation of the passed in str.
	 **/
	private static String convertStringToHex(String str, boolean dumpOut) {
		char[] chars = str.toCharArray();
		String out_put = "";

		if (dumpOut) {
			System.out.println("    Ascii: " + str);
		}
		if (dumpOut) {
			System.out.print("    Hex: ");
		}
		
		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			out_put = Integer.toHexString((int) chars[i]);
			if (out_put.length() == 1) {
				hex.append("0");
			}
			hex.append(out_put);
			if (dumpOut) {
				System.out.print("0x" + (out_put.length() == 1 ? "0" : "") + out_put + " ");
			}
		}
		
		if (dumpOut) {
			System.out.println("");
		}

		return hex.toString();
	}

	/**
	 * Converts an HEX number String to its decimal equivalent.
	 * 
	 * @param String
	 *            holding the Hex Number string to convert to decimal
	 * @param boolean flag to turn some debug output on/off
	 * @return an int holding the decimal equivalent of the passed in HEX
	 *         numberStr.
	 **/
	private static int convertHexNumberStringToDecimal(String str,
			boolean dumpOut) {
		char[] chars = str.toCharArray();
		String out_put = "";

		if (dumpOut)
			System.out.println("        Ascii: " + str);
		if (dumpOut)
			System.out.print("          Hex: 0x");
		StringBuffer hex = new StringBuffer();
		String hexInt = new String();
		for (int i = 0; i < chars.length; i++) {
			out_put = Integer.toHexString((int) chars[i]);
			if (out_put.length() == 1)
				hex.append("0");
			hex.append(out_put);
			if (dumpOut)
				System.out.print((out_put.length() == 1 ? "0" : "") + out_put);
		}
		hexInt = "" + (Integer.parseInt(hex.toString(), 16));
		if (dumpOut)
			System.out.println("");
		if (dumpOut)
			System.out.println("      Decimal: " + hexInt.toString());

		return Integer.parseInt(hexInt.toString());
	}

	/**
	 * Wraps a command in a eiscp data message (data characters).
	 * 
	 * @param cmd
	 *            must be one of the Command Class Constants from the
	 *            eiscp.Eiscp.Command class.
	 * @return StringBuffer holing the full iscp message packet
	 **/
	private StringBuilder getEiscpMessage(EiscpCommand cmd) {
		String cmdStr = "";
		
		if (EiscpCommand.VOLUME_SET.equals(cmd)) {
			cmdStr = "MVL" + Integer.toHexString(DEFAULT_VOLUME);
		} else {
			cmdStr = cmd.getCommand();
		}

		StringBuilder sb = new StringBuilder();
		int eiscpDataSize = cmdStr.length() + 2; // this is the eISCP data size
		int eiscpMsgSize = eiscpDataSize + 1 + 16; // this is the eISCP data size

		/*
		 * This is where I construct the entire message character by character.
		 * Each char is represented by a 2 disgit hex value
		 */
		sb.append("ISCP");
		// the following are all in HEX representing one char

		// 4 char Big Endian Header
		sb.append((char) Integer.parseInt("00", 16));
		sb.append((char) Integer.parseInt("00", 16));
		sb.append((char) Integer.parseInt("00", 16));
		sb.append((char) Integer.parseInt("10", 16));

		// 4 char Big Endian data size
		sb.append((char) Integer.parseInt("00", 16));
		sb.append((char) Integer.parseInt("00", 16));
		sb.append((char) Integer.parseInt("00", 16));
		// the official ISCP docs say this is supposed to be just the data size
		// (eiscpDataSize)
		// ** BUT **
		// It only works if you send the size of the entire Message size
		// (eiscpMsgSize)
		sb.append((char) Integer.parseInt(Integer.toHexString(eiscpMsgSize), 16));

		// eiscp_version = "01";
		sb.append((char) Integer.parseInt("01", 16));

		// 3 chars reserved = "00"+"00"+"00";
		sb.append((char) Integer.parseInt("00", 16));
		sb.append((char) Integer.parseInt("00", 16));
		sb.append((char) Integer.parseInt("00", 16));

		// eISCP data
		// Start Character
		sb.append("!");

		// eISCP data - unittype char '1' is receiver
		sb.append("1");

		// eISCP data - 3 char command and param ie PWR01
		sb.append(cmdStr);

		// msg end - EOF
		sb.append((char) Integer.parseInt("0D", 16));

		logger.debug("  eISCP data size: {} (0x{}) chars", eiscpDataSize, Integer.toHexString(eiscpDataSize));
		logger.debug("  eISCP msg size: {} (0x{}) chars", sb.length(), Integer.toHexString(sb.length()));

		return sb;
	}

	/**
	 * Sends to command to the receiver and does not wait for a reply.
	 * @param command the {@link EiscpCommand} to send
	 **/
	public void sendCommand(EiscpCommand command) {
		sendCommand(command, false);
	}

	/**
	 * Sends to command to the receiver and close the connection when done.
	 * It does not wait for a reply.
	 * @param command the {@link EiscpCommand} to send
	 **/
	public void sendCommandAndClose(EiscpCommand cmd) {
		sendCommand(cmd, true);
	}

	/**
	 * Sends to command to the receiver and does not wait for a reply.
	 * 
	 * @param command the {@link EiscpCommand} to send
	 * @param closeSocket flag to close the connection when done or leave it open.
	 **/
	private void sendCommand(EiscpCommand cmd, boolean closeSocket) {
		StringBuilder sb = getEiscpMessage(cmd);

		if (connectSocket()) {
			try {
				logger.trace("sending {} chars!", sb.length());
				convertStringToHex(sb.toString(), true);
				outStream.writeBytes(sb.toString());
				outStream.flush();
				logger.trace("sent!");
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
		// finally close the socket if required ...
		if (closeSocket) {
			closeSocket();
		}
	}

	/**
	 * Sends to command to the receiver and then waits for the response(s) <br />
	 * and returns all response packetMessages and leave the socket open.
	 * 
	 * @param command the {@link EiscpCommand} to send
	 * @return the response to the command
	 **/
	public String sendQueryCommand(EiscpCommand command) {
		return sendQueryCommand(command, false, true);
	}

	/**
	 * Sends to command to the receiver and then waits for the response(s) <br />
	 * and returns all response packetMessages and closes the socket.
	 * 
	 * @param command the {@link EiscpCommand} to send
	 * @return the response to the command
	 **/
	public String sendQueryCommandAndClose(EiscpCommand command) {
		return sendQueryCommand(command, true, true);
	}

	/**
	 * Sends to command to the receiver and then waits for the response(s). The
	 * responses often have nothing to do with the command sent so this method
	 * can filter them to return only the responses related to the command sent.
	 * 
	 * @param command the {@link EiscpCommand} to send
	 * @param closeSocket flag to close the connection when done or leave it open.
	 * @param returnAll
	 *            flags if all response packetMessages are returned, if no then
	 *            ONLY the ones related to the command requested
	 * @return the response to the command
	 **/
	private String sendQueryCommand(EiscpCommand command, boolean closeSocket, boolean returnAll) {
		String retVal = "";

		/* Send The Command and then... */
		sendCommand(command, false);
		// sleep(50); // docs say so

		/* now listen for the response. */
		Vector<String> rv = readQueryResponses();
		String currResponse = "";
		for (int i = 0; i < rv.size(); i++) {
			currResponse = (String) rv.elementAt(i);
			/* Send ALL responses OR just the one related to the commad sent??? */
			String commandStr = command.getCommand();
			if (returnAll || currResponse.startsWith(commandStr.substring(0, 3))) {
				retVal += currResponse + "\n";
			}
		}

		if (closeSocket) {
			closeSocket();
		}

		return retVal;
	}

	/**
	 * This method reads ALL responses (possibly more than one) after a query
	 * command.
	 * 
	 * @return an array of the data portion of the response messages only -
	 *         There might be more than one response message received.
	 **/
	private Vector<String> readQueryResponses() {
		boolean debugging = false;
		Vector<String> retVal = new Vector<String>();
		byte[] responseBytes = new byte[32];
		String currResponse = "";
		int numBytesReceived = 0;
		int totBytesReceived = 0;
		int i = 0;
		int packetCounter = 0;
		int headerSizeDecimal = 0;
		int dataSizeDecimal = 0;
		char endChar1 = '!';// NR-5008 response sends 3 chars to terminate the
							// packet - 0x1a 0x0d 0x0a
		char endChar2 = '!';
		char endChar3 = '!';

		if (connected) {
			try {
				if (debugging) {
					logger.debug("\nReading Response Packet");
				}
				
				// this must be set or the following read will BLOCK /
				// hang the method when the messages are done
				eiscpSocket.setSoTimeout(SOCKET_TIME_OUT);
				
				while ((numBytesReceived = inStream.read(responseBytes)) > 0) {
					totBytesReceived = 0;
					StringBuilder msgBuffer = new StringBuilder("");
					if (debugging) {
						logger.debug("Packet [{}]:", packetCounter );
					}

					/* Read ALL the incoming Bytes and buffer them */
					// *******************************************
					if (debugging) {
						System.out.print("" + numBytesReceived);
					}
					
					while (numBytesReceived > 0) {
						totBytesReceived += numBytesReceived;
						msgBuffer.append(new String(responseBytes));
						responseBytes = new byte[32];
						numBytesReceived = 0;
						if (inStream.available() > 0) {
							numBytesReceived = inStream.read(responseBytes);
						}
						if (debugging) {
							System.out.print(" " + numBytesReceived);
						}
					}
					if (debugging) {
						System.out.println();
					}
					convertStringToHex(msgBuffer.toString(), debugging);

					/* Response is done... process it into dataMessages */
					// *******************************************
					
					// use the charArray to step through
					char[] responseChars = msgBuffer.toString().toCharArray(); 
					int responseByteCnt = 0;
					char versionChar = '1';
					char dataStartChar = '!';
					char dataUnitChar = '1';

					// loop through all the chars and split out the dataMessages
					while (responseByteCnt < totBytesReceived) {
						/* read Header */
						// 1st 4 chars are the leadIn
						responseByteCnt += 4;

						// read headerSize
						char[] headerSizeBytes = {
								responseChars[responseByteCnt++],
								responseChars[responseByteCnt++],
								responseChars[responseByteCnt++],
								responseChars[responseByteCnt++] };
						// 4 char Big Endian data size
						char[] dataSizeBytes = {
								responseChars[responseByteCnt++],
								responseChars[responseByteCnt++],
								responseChars[responseByteCnt++],
								responseChars[responseByteCnt++] };
						
						if (debugging) {
							System.out.println(" -HeaderSize-");
						}
						
						headerSizeDecimal = convertHexNumberStringToDecimal(new String(headerSizeBytes), debugging);
						if (debugging) {
							System.out.println(" -DataSize-");
						}
						dataSizeDecimal = convertHexNumberStringToDecimal( new String(dataSizeBytes), debugging);

						// version
						versionChar = responseChars[responseByteCnt++];

						// 3 reserved bytes
						responseByteCnt += 3;
						int dataByteCnt = 0;

						// Now the data message
						
						// parse and throw away (like parsley)
						dataStartChar = responseChars[responseByteCnt++];
						dataUnitChar = responseChars[responseByteCnt++]; // dito
						char[] dataMessage = new char[dataSizeDecimal];

						/* Get the dataMessage from this response */
						// NR-5008 response sends 3 chars to terminate the
						// packet - so DON't include them in the message
						while (dataByteCnt < (dataSizeDecimal - 3)
								&& responseByteCnt < (totBytesReceived - 3)) {
							dataMessage[dataByteCnt++] = responseChars[responseByteCnt++];
						}
						if (debugging) {
							System.out.println(" -DataMessage-");
						}
						if (debugging) {
							System.out.println("    " + (new String(dataMessage)) + "\n");
						}
						
						retVal.addElement(new String(dataMessage));

						// Read the end packet char(s) "[EOF]"
						// [EOF] End of File ASCII Code 0x1A
						// NOTE: the end of packet char (0x1A) for a response
						// message is DIFFERENT that the sent message
						// NOTE: ITs also different than what is in the Onkyo
						// eISCP docs
						// NR-5008 sends 3 chars to terminate the packet - 0x1a
						// 0x0d 0x0a
						endChar1 = responseChars[responseByteCnt++];
						endChar2 = responseChars[responseByteCnt++];
						endChar3 = responseChars[responseByteCnt++];
						if (endChar1 == (char) Integer.parseInt("1A", 16)
								&& endChar2 == (char) Integer.parseInt("0D", 16)
								&& endChar3 == (char) Integer.parseInt("0A", 16))
							if (debugging) {
								System.out.println(" EndOfPacket[" + packetCounter + "]\n");
							}
						packetCounter++;
					}

				}

			} catch (java.net.SocketTimeoutException noMoreDataException) {
				logger.error("Response already done!", noMoreDataException);
			} catch (EOFException eofException) {
				logger.error("received: \"" + retVal + "\"");
			} catch (IOException ioException) {
				logger.error("Cannot connect!", ioException);
			}
		} else {
			logger.debug("!!Not Connected to Receive ");
		}
		return retVal;
	}

	/**
	 * This method takes the 3 character response from the USB Play status query
	 * (NETUSB_PLAY_STATUS_QUERY) and creates a human readable String. NET/USB
	 * Play Status QUERY returns 3 letters - PRS.
	 * <oL>
	 * <LI>p -> Play Status
	 * <ul>
	 * <li>"S": STOP</li>
	 * <li>"P": Play</li>
	 * <li>"p": Pause</li>
	 * <li>"F": FF</li>
	 * <li>"R": FastREW</li>
	 * </ul>
	 * </LI>
	 * <LI>r -> Repeat Status
	 * <ul>
	 * <li>"-": Off</li>
	 * <li>"R": All</li>
	 * <li>"F": Folder</li>
	 * <li>"1": Repeat 1</li>
	 * </ul>
	 * </LI>
	 * <LI>s -> Shuffle Status
	 * <ul>
	 * <li>"-": Off</li>
	 * <li>"S": All</li>
	 * <li>"A": Album</li>
	 * <li>"F": Folder</li>
	 * </ul>
	 * </LI>
	 * </oL>
	 * 
	 * @param queryResponses
	 *            is the entire response packet with the 3 char reply embedded
	 *            in it.
	 **/
	protected static String decipherUsbPlayStatusResponse(String queryResponses) {
		String[] responses = queryResponses.split("[\n]");
		String retVal = "NETUSB_PLAY_STATUS_QUERY response: " + queryResponses.trim();
		String queryResponse = "";
		
		for (int i = 0; i < responses.length; i++) {
			queryResponse = responses[i];
			if (queryResponse.substring(3, 4).equals("P")) {
				retVal += "\n  Play Status: ";
				if (queryResponse.substring(5).equals("S"))
					retVal += "Stop";
				else if (queryResponse.substring(5).equals("P"))
					retVal += "Play";
				else if (queryResponse.substring(5).equals("p"))
					retVal += "Pause";
				else if (queryResponse.substring(5).equals("F"))
					retVal += "FastForward";
				else if (queryResponse.substring(5).equals("R"))
					retVal += "FastRewind";
				else
					retVal += "NotSpecified";
			} else if (queryResponse.substring(3, 4).equals("R")) {
				retVal += "\n  Repeat Status: ";
				if (queryResponse.substring(5).equals("-"))
					retVal += "Off";
				else if (queryResponse.substring(5).equals("R"))
					retVal += "All";
				else if (queryResponse.substring(5).equals("F"))
					retVal += "Folder";
				else if (queryResponse.substring(5).equals("1"))
					retVal += "1 song";
				else
					retVal += "NotSpecified";
			} else if (queryResponse.substring(3, 4).equals("S")) {
				retVal += "\n  Schuffle Status: ";
				if (queryResponse.trim().substring(5).equals("-"))
					retVal += "Off";
				else if (queryResponse.trim().substring(5).equals("S"))
					retVal += "All";
				else if (queryResponse.trim().substring(5).equals("A"))
					retVal += "Album";
				else if (queryResponse.trim().substring(5).equals("F"))
					retVal += "Folder";
				else
					retVal += "NotSpecified";
			}
		}

		return retVal;
	}

	
}
