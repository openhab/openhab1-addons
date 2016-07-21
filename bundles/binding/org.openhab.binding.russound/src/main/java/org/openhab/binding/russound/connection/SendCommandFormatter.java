package org.openhab.binding.russound.connection;

public interface SendCommandFormatter {

	public byte[] processCommand(byte[] bytes);
}
