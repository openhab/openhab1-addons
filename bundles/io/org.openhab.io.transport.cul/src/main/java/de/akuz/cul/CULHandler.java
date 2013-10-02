package de.akuz.cul;

public interface CULHandler {

	public void registerListener(CULListener listener);

	public void unregisterListener(CULListener listener);

	public void send(String command) throws CULCommunicationException;
	
	public CULMode getCULMode();
}
