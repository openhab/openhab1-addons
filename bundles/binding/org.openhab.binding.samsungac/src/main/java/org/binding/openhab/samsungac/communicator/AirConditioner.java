package org.binding.openhab.samsungac.communicator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.openhab.binding.samsungac.internal.Command;
import org.openhab.binding.samsungac.internal.ConvenientMode;
import org.openhab.binding.samsungac.internal.OperationMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirConditioner {

	private static Logger logger = LoggerFactory
			.getLogger(AirConditioner.class);

	private String IP;
	private String MAC;
	private String TOKEN_STRING;
	private final Integer PORT = 2878;
	private Map<String, String> statusMap = new HashMap<String, String>();
	private SSLSocket socket;

	public AirConditioner(String ipAddress, String macAddress) {
		this(ipAddress, macAddress, null);
	}

	public AirConditioner(String ipAddress, String macAddress, String token) {
		IP = ipAddress;
		MAC = macAddress;
		TOKEN_STRING = token;
	}

	public AirConditioner login() {
		try {
			socket = connect();
			getToken();
			loginWithToken();
		} catch (Exception e) {
			logger.warn("Connection to Air Conditioner failed", e);
		}
		return this;
	}

	public void disconnect() {
		if (isConnected()) {
			try {
				socket.close();
			} catch (IOException e) {
				logger.warn(
						"Could not disconnect from Air Conditioner with IP: "
								+ IP, e);
			}
		}
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}

	private Map<String, String> loginWithToken() throws Exception {
		if (TOKEN_STRING != null) {
			writeLine("<Request Type=\"AuthToken\"><User Token=\""
					+ TOKEN_STRING + "\" /></Request>");
			handleResponse();
		} else
			throw new Exception(
					"Must connect and retrieve a token before login in");
		return getStatus();
	}

	private void getToken() throws Exception {
		while (TOKEN_STRING == null) {
			handleResponse();
			Thread.sleep(2000);
		}
		logger.debug("Token has been acquired: " + TOKEN_STRING);
	}

	private void handleResponse() throws Exception {
		handleResponse(null);
	}

	private void handleResponse(String commandId) throws Exception {
		String line;
		while ((line = readLine(socket)) != null) {
			logger.debug("Got response:'" + line + "'");

			if (line == null || ResponseParser.isFirstLine(line)) {
				return;
			}

			if (ResponseParser.isNotLoggedInResponse(line)) {
				if (TOKEN_STRING != null)
					return;
				writeLine("<Request Type=\"GetToken\" />");
				return;
			}

			if (ResponseParser.isFailedAuthenticationResponse(line)) {
				throw new Exception("failed to connect");
			}

			if (commandId != null
					&& ResponseParser
							.isCorrectCommandResponse(line, commandId)) {
				return;
			}

			if (ResponseParser.isResponseWithToken(line)) {
				TOKEN_STRING = ResponseParser.parseTokenFromResponse(line);
				return;
			}
			if (ResponseParser.isReadyForTokenResponse(line)) {
				logger.warn("Switch off and on the air conditioner within 30 seconds");
				return;
			}

			if (ResponseParser.isSuccessfulLoginResponse(line)) {
				return;
			}

			if (ResponseParser.isDeviceState(line)) {
				statusMap = ResponseParser.parseStatusResponse(line);
				return;
			}

			if (ResponseParser.isDeviceControl(line)) {
				return;
			}

			if (ResponseParser.isUpdateStatus(line)) {
				Pattern pattern = Pattern
						.compile("Attr ID=\"(.*)\" Value=\"(.*)\"");
				Matcher matcher = pattern.matcher(line);
				if (matcher.groupCount() == 2) {
					statusMap.put(matcher.group(0), matcher.group(1));
				}
				return;
			}
		}
	}

	private void writeLine(String line) throws Exception {
		logger.debug("Sending request:'" + line + "'");
		connect();
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			writer.write(line);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String readLine(SSLSocket socket) throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		try {
			return r.readLine();
		} catch (SocketTimeoutException e) {
		}
		return null;
	}

	private SSLSocket connect() throws Exception {
		if (isConnected())
			return socket;
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
			} };

			ctx.init(null, trustAllCerts, null);
			socket = (SSLSocket) ctx.getSocketFactory().createSocket(IP, PORT);
			socket.setSoTimeout(1000);
			socket.startHandshake();
		} catch (Exception e) {
			throw new Exception("Cannot connect to " + IP + ":" + PORT, e);
		}
		handleResponse();
		handleResponse();
		return socket;
	}

	private String sendCommand(Object command, String value) throws Exception {
		connect();
		logger.debug("Sending command: '" + command + "' value: '" + value);
		String id = "cmd" + Math.round(Math.random() * 10000);
		writeLine("<Request Type=\"DeviceControl\"><Control CommandID=\"" + id
				+ "\" DUID=\"" + MAC + "\"><Attr ID=\"" + command
				+ "\" Value=\"" + value + "\" /></Control></Request>");
		return id;
	}

	private void handleCommandRequestResponse(Command command, String value) {
		try {
			connect();
			handleResponse(sendCommand(command, value));
		} catch (Exception e) {
			logger.warn("Could not handle command request respone", e);
		}
	}

	public void sendCommand(String command, String value) {
		handleCommandRequestResponse(Command.valueOf(command), value);
	}

	public void on() {
		handleCommandRequestResponse(Command.AC_FUN_POWER, "On");
	}

	public void off() {
		handleCommandRequestResponse(Command.AC_FUN_POWER, "Off");
	}

	public void setMode(OperationMode mode) {
		handleCommandRequestResponse(Command.AC_FUN_OPMODE, mode.toString());
	}

	public void setTemperature(Integer temp) {
		handleCommandRequestResponse(Command.AC_FUN_TEMPSET, temp.toString());
	}

	/*
	 * public String getTemperatureNow() { return
	 * handleCommandRequestResponse(Command.AC_FUN_TEMPNOW, ""); }
	 */

	public void setConvenientMode(ConvenientMode mode) {
		handleCommandRequestResponse(Command.AC_FUN_COMODE, mode.toString());
	}

	public Map<String, String> getStatus() {
		try {
			writeLine("<Request Type=\"DeviceState\" DUID=\"" + MAC
					+ "\"></Request>");
			handleResponse();
		} catch (Exception e) {
			logger.warn("Could not update status for air conditioner with IP: "
					+ IP, e);
		}
		return statusMap;
	}
}
