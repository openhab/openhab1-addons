/**
 * 
 */
package org.openhab.binding.stiebelheatpump.protocol;

import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.TooManyListenersException;

/**
 * @author Peter Kreutzer
 * @since 1.4.0
 */
public class StiebelHeatPumpSerialConnector extends StiebelHeatPumpConnector {
	
	private static final Logger logger = LoggerFactory.getLogger(StiebelHeatPumpSerialConnector.class);
	
	private static byte STARTCOMMUNICATION = (byte)02;
	private static byte ESCAPE = (byte)10;
	private static byte[] BEGIN = {(byte)01, (byte)00};
	private static byte END = (byte)03;
	private static byte GETVERSION = (byte) 0xfd;
	private static byte[] HEADER = {(byte) 01,(byte) 00,(byte) 0xb5,(byte) 0xfd};
	private static byte[] FOOTER = {ESCAPE,END};
	
	/** the serial port to use for connecting to the heat pump device */
    private final String serialPort;
   
	/**  baud rate of serial port */
    private final int baudRate;
    
    /**  output stream of serial port */
    private OutputStream outStream;
    
    /**  output stream of serial port */
    private InputStream inStream;
    
    /**  output stream of serial port */
    private SerialPort connectedSerialPort;

	public StiebelHeatPumpSerialConnector(String serialPort, int baudRate) {
		logger.debug("Stiebel heatpump serial message listener started");
		this.serialPort = serialPort;
		this.baudRate = baudRate;
	}
	
    /**
    * Get the serial port input stream
    * @return The serial port input stream     */   
	public InputStream getSerialInputStream() {
		return inStream;
		}
	
	/**     
	 * brief Get the serial port output stream     
	 * @return The serial port output stream     */
	public OutputStream getSerialOutputStream() {
		return outStream;
		}
	
	@Override
	public void connect() throws StiebelHeatPumpException {
		try {
            // Obtain a CommPortIdentifier object for the port you want to open
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(serialPort);
            // Get the port's ownership
			connectedSerialPort = (SerialPort) portId.open("Openhab stiebel heat pump binding", 5000);
			// Set the parameters of the connection.
			setSerialPortParameters(baudRate);
			// Open the input and output streams for the connection.
			// If they won't open, close the port before throwing an
			// exception.
			outStream = connectedSerialPort.getOutputStream();
			inStream = connectedSerialPort.getInputStream();
			
			} 
		catch (NoSuchPortException e) {
			throw new StiebelHeatPumpException(e.getMessage());
			} 
		catch (PortInUseException e) {
			throw new StiebelHeatPumpException(e.getMessage());
			} 
		catch (IOException e) { 
			connectedSerialPort.close();
			throw new StiebelHeatPumpException(e.getMessage());
			}
	}

	@Override
	public void disconnect() throws StiebelHeatPumpException {
		
		if (serialPort != null) {
			try {
				// close the i/o streams.
				outStream.close();
				inStream.close();
				} 
			catch (IOException ex) {
				// don't care
				}
			// Close the port.
			connectedSerialPort.close();
			connectedSerialPort = null;
			}
	}

	@Override
	public byte[] receiveDatagram() throws StiebelHeatPumpException {

		throw new StiebelHeatPumpException("Not implemented");
	}
	
    /**
     *  Register listener for data available event 
     *  @param dataAvailableListener The data available listener */
	public void addDataAvailableListener(SerialPortEventListener dataAvailableListener) throws StiebelHeatPumpException {
		// Add the serial port event listener
		try {
			connectedSerialPort.addEventListener(dataAvailableListener);
		} catch (TooManyListenersException e) {
			throw new StiebelHeatPumpException(e.getMessage());
		}
		connectedSerialPort.notifyOnDataAvailable(true);
		}
	
	/** Sets the serial port parameters to 57600bps-8N1     */    
	protected void setSerialPortParameters(int baudrate) throws IOException {
		        
		try {            
			// Set serial port to xxxbps-8N1
			connectedSerialPort.setSerialPortParams(baudRate,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE); 
			connectedSerialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);        
			} 
		catch (UnsupportedCommOperationException ex) {  
			throw new IOException("Unsupported serial port parameter for serial port");
			}
		}
	
	/** Gets version information of connected heat pump*/    
	public String getHeatPumpVersion() throws StiebelHeatPumpException {
		        
		try {
			// send request to heat pump
			short checkSum = calculateChecksum(new byte[]{GETVERSION});
			
			byte[] serialVersionMessage = {BEGIN[0],BEGIN[1],shortToByte(checkSum)[0], GETVERSION , ESCAPE, END};
			outStream.write(serialVersionMessage);
			outStream.flush();
			Thread.sleep(1000);
			
			// wait  for response of heatpump
			int availableBytes = 0;
			int retry = 0;
			int maxRetries = 5;
			byte[] readBuffer = new byte[2];
			while ( retry < maxRetries ){
				availableBytes = inStream.available();
				if (availableBytes > 0) {
					// Read the serial port
					inStream.read(readBuffer, 0, availableBytes);
				}
				else{
					retry++;
					}
			}
			
			if (retry == maxRetries ){
				logger.debug("Could not connect heatpump, tryed {} time ", maxRetries);
				return "unknown";
			}
					
			// acknowledge to heat pump to now send the data
			outStream.write(ESCAPE);
			outStream.flush();
			
			// receive data
			// get header first
			availableBytes = 0;
			readBuffer = new byte[HEADER.length];
			availableBytes = inStream.available();
			if (availableBytes > 0) {
				// Read the serial port
				inStream.read(readBuffer, 0, HEADER.length);
			}
			if(readBuffer != HEADER){
				logger.debug("Invalid header received from heatpump {} ", new String(readBuffer, 0, HEADER.length));
				return "unknown";
			}
			availableBytes = inStream.available();
			inStream.read(readBuffer, 0, availableBytes);
			
			byte[] data = new byte[availableBytes- FOOTER.length];
			System.arraycopy(readBuffer, 2, data, 0, 2);
			
			logger.debug("Heat pump version in bytes: {} ", new String(data));
									
			return new String(readBuffer, 0, availableBytes);
		}
		catch (IOException ex) {  
			throw new StiebelHeatPumpException(ex.getMessage());
		} 
		catch (InterruptedException ex) {
			throw new StiebelHeatPumpException(ex.getMessage());
		}	
	}

	/** calc the checksum of a byte data array
	 * @return calculated checksum */    
	private short calculateChecksum(byte[] data ) throws StiebelHeatPumpException {	
		short checkSum = 1, i = 0;
        for( i = 0; i < data.length; i++){
             checkSum += (short)(data[i] & 0xFF);
        }
		return checkSum;
	}

	/** converts short to byte
	 * @return array of bytes */    
	private byte[] shortToByte(short value) throws StiebelHeatPumpException {
		byte[] returnByteArray = new byte[2];
		returnByteArray[0] = (byte)(value & 0xff);
		returnByteArray[1] = (byte)((value>>8) & 0xff);
		
		return returnByteArray;
	}
	
	/** converts short to byte
	 * @return array of bytes */    
	private short byteToShort (byte [] bytes) throws StiebelHeatPumpException {		
		
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
}
