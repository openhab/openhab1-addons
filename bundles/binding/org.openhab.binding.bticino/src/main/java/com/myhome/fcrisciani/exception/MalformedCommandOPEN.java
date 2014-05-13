package com.myhome.fcrisciani.exception;

public class MalformedCommandOPEN extends Exception {
	// ----- TYPES ----- //

	// ---- MEMBERS ---- //
	private static final long serialVersionUID = -4446224937634207778L;
	
	String commandNotRecognised = null;

	// ---- METHODS ---- //

	public MalformedCommandOPEN(String commandNotRecognised) {
		super();
		this.commandNotRecognised = commandNotRecognised;
	}
	
	@Override
	public String getLocalizedMessage(){
		StringBuilder builder = new StringBuilder();
		
		builder.append("Command OPEN: ").append(commandNotRecognised).append(" has not a valid format");
		
		return builder.toString();
	}
}
