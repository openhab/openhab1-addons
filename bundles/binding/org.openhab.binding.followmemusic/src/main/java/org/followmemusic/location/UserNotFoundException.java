package org.followmemusic.location;

public class UserNotFoundException extends Exception {

	public UserNotFoundException(int id) {
		super("Could not find the user with the id : "+id);
	}
	
}
