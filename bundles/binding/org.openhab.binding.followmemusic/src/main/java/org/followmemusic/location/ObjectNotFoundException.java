package org.followmemusic.location;

public class ObjectNotFoundException extends Exception {

	public ObjectNotFoundException(String objectName, int id) {
		super("Could not find object \""+objectName+"\" with the id : "+id);
	}
	
}
