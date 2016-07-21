package org.openhab.binding.russound.connection;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public interface ConnectionProvider {

	public ObjectOutputStream getOutputStream();

	public DataInputStream getInputStream();

	public boolean isConnected();

	public boolean connect();

	public void disconnect() throws IOException;
}
