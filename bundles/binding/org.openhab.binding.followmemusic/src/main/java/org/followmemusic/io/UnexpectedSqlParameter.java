package org.followmemusic.io;

public class UnexpectedSqlParameter extends Exception {

	public UnexpectedSqlParameter(Object object) {
		super("Unexpected parameter type : " + object.getClass().getName());
	}

}
