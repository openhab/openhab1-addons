package org.openhab.binding.megadevice.internal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MegadeviceHttpServer extends Thread {
	private static Logger logger = LoggerFactory
			.getLogger(MegadeviceHttpServer.class);
	private static int portnumber = 8989;
	private static boolean isRunning = true;
	private static ServerSocket ss = null;

	public void run() {

		logger.info("Starting MegaHttpServer at " + portnumber + " port");

		try {
			ss = new ServerSocket(portnumber);
		} catch (IOException e) {
			logger.debug("ERROR -> " + e.getMessage());
			// e.printStackTrace();
		}
		while (isRunning) {
			Socket s = null;
			try {
				 s = ss != null ? ss.accept() : null;
			} catch (IOException e) {
				logger.debug("ERROR --> " + e.getMessage());
				// e.printStackTrace();
			}
			if (!ss.isClosed()) {
				new Thread(new MegaDeviceHttpSocket(s)).start();
			}
		}
	}

	public static void setPort(int port) {
		portnumber = port;
	}

	public static void setRunningState(boolean isRun) {
		isRunning = isRun;
		try {
			logger.info("closing port");
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean getRunningState() {
		return isRunning;
	}
}
