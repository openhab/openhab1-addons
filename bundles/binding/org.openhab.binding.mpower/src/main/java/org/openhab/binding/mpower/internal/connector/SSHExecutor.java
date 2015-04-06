package org.openhab.binding.mpower.internal.connector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHExecutor {
	private Session session;

	public SSHExecutor(Session aSession) {
		this.session = aSession;
	}

	public String execute(String command) throws JSchException {
		ChannelExec channel;
		if (session.isConnected()) {
			channel = (ChannelExec) this.session.openChannel("exec");
			channel.setCommand(command);
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			channel.setOutputStream(bytearrayoutputstream);
			channel.connect();

			waitForInput(channel);

			String result = bytearrayoutputstream.toString();
			try {
				bytearrayoutputstream.close();
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
			}

			channel.disconnect();
			return result;
		}

		return null;
	}

	private static void waitForInput(ChannelExec channel) {
		do {
			if (!channel.isConnected()) {
				return;
			}
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				// logger.error("Internal error", e);
			}
		} while (true);
	}
}