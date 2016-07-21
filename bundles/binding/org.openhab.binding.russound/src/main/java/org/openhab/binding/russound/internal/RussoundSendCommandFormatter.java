package org.openhab.binding.russound.internal;

import java.util.Arrays;

import org.openhab.binding.russound.connection.SendCommandFormatter;

public class RussoundSendCommandFormatter implements SendCommandFormatter {

	public byte[] processCommand(byte[] bytes) {
		return addChecksumandTerminator(bytes);
	}

	private byte[] addChecksumandTerminator(byte[] command) {
		byte[] commandWithChecksumandTerminator = Arrays.copyOf(command,
				command.length + 2);
		commandWithChecksumandTerminator[commandWithChecksumandTerminator.length - 2] = russChecksum(command);
		commandWithChecksumandTerminator[commandWithChecksumandTerminator.length - 1] = (byte) 0xf7;
		return commandWithChecksumandTerminator;
	}

	private byte russChecksum(byte[] data) {
		int sum = 0;
		for (int i = 0; i < data.length; i++) {
			sum = sum + data[i];
		}
		sum = sum + data.length;
		byte checksum = (byte) (sum & 0x007F);
		return checksum;
	}

}
