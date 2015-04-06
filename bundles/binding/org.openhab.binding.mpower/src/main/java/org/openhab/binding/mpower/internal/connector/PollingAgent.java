package org.openhab.binding.mpower.internal.connector;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class PollingAgent extends Thread {
	private MpowerSSHConnector connector;
	// this is the internal poll interval. Its is fixed and independent
	// from the 'item update interval'
	// the smaller the value the more instant switches will be synchronized
	private long pollInterval = 5000;
	private static final Logger logger = LoggerFactory
			.getLogger(PollingAgent.class);

	public PollingAgent(MpowerSSHConnector connector) {
		super("Mpower polling agent for " + connector.getId());
		this.setDaemon(true);
		this.connector = connector;
	}

	@Override
	public void run() {
		logger.debug("Polling Agent started");
		while (!isInterrupted()) {
			try {
				Session session = this.connector.getSession();
				if (session != null && session.isConnected()) {
					SSHExecutor exec = new SSHExecutor(
							this.connector.getSession());
					StringBuilder builder = new StringBuilder();
					if (this.connector.getPorts() > 0) {
						builder.append("cat");
					}
					for (int i = 1; i < this.connector.getPorts() + 1; i++) {
						builder.append(" /proc/power/v_rms").append(i);
						builder.append(" /proc/power/active_pwr").append(i);
						builder.append(" /proc/power/energy_sum").append(i);
						builder.append(" /proc/power/relay").append(i);
					}
					String command = builder.toString();
					if (StringUtils.isNotBlank(command)) {

						String result = exec.execute(command);
						this.connector.message(result);
					}
				}
			} catch (JSchException e) {
				logger.error("Failed to query mPower", e);
			}

			try {
				Thread.sleep(pollInterval);
			} catch (InterruptedException e) {
				this.interrupt();
			}
		}
		logger.debug("Polling Agent stopped");
	}
}