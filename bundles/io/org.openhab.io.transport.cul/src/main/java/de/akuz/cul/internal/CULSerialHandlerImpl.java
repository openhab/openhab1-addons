package de.akuz.cul.internal;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akuz.cul.CULCommunicationException;
import de.akuz.cul.CULDeviceException;
import de.akuz.cul.CULMode;

/**
 * Implementation for culfw based devices which communicate via serial port
 * (cullite for example). This is based on rxtx and assumes constant parameters
 * for the serial port.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULSerialHandlerImpl extends AbstractCULHandler implements SerialPortEventListener {

	private final static Logger log = LoggerFactory.getLogger(CULSerialHandlerImpl.class);

	private SerialPort serialPort;
	private InputStream is;
	private OutputStream os;
	private BufferedReader br;
	private BufferedWriter bw;

	public CULSerialHandlerImpl(String deviceName, CULMode mode) {
		super(deviceName, mode);
	}

	@Override
	public void open() throws CULDeviceException {
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(deviceName);
			if (portIdentifier.isCurrentlyOwned()) {
				throw new CULDeviceException("The port " + deviceName + " is currenty used by "
						+ portIdentifier.getCurrentOwner());
			}
			CommPort port = portIdentifier.open(this.getClass().getName(), 2000);
			if (!(port instanceof SerialPort)) {
				throw new CULDeviceException("The device " + deviceName + " is not a serial port");
			}
			serialPort = (SerialPort) port;
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
			is = serialPort.getInputStream();
			os = serialPort.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			bw = new BufferedWriter(new OutputStreamWriter(os));

			serialPort.notifyOnDataAvailable(true);
			serialPort.addEventListener(this);
		} catch (NoSuchPortException e) {
			throw new CULDeviceException(e);
		} catch (PortInUseException e) {
			throw new CULDeviceException(e);
		} catch (UnsupportedCommOperationException e) {
			throw new CULDeviceException(e);
		} catch (IOException e) {
			throw new CULDeviceException(e);
		} catch (TooManyListenersException e) {
			throw new CULDeviceException(e);
		}
	}

	@Override
	public void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
		}
		try {
			if (br != null) {
				br.close();
			}
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			log.error("Can't close the input and output streams propberly", e);
		} finally {
			if (serialPort != null) {
				serialPort.close();
			}
		}
	}

	@Override
	public void send(String command) throws CULCommunicationException {
		if (isMessageAllowed(command)) {
			sendRaw(command);
		}
	}

	public void sendRaw(String sendString) throws CULCommunicationException {
		if (!sendString.endsWith("\r\n")) {
			sendString = sendString + "\r\n";
		}
		log.debug("Sending raw message to CUL: " + sendString);
		if (bw == null) {
			throw new CULCommunicationException("BufferedWriter is null, probably the device is not open");
		}
		synchronized (bw) {
			try {
				bw.write(sendString);
				bw.flush();
			} catch (IOException e) {
				log.error("Can't write to CUL", e);
				throw new CULCommunicationException(e);
			}
		}
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String data = br.readLine();
				// Ignore the EOB messages
				if ("EOB".equals(data)) {
					log.debug("Received message from CUL: " + data);
					return;
				} else if ("LOVF".equals(data)) {
					log.warn("Limit Overflow: Last message lost. You are using more than 1% transmitting time. Reduce the number of rf messages");
					return;
				}
				notifyDataReceived(data);
			} catch (IOException e) {
				notifyError(e);
			}
		}

	}

}
