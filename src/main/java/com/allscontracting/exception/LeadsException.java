package com.allscontracting.exception;

public class LeadsException extends Exception {

	private static final long serialVersionUID = -8848942532056568071L;

	public LeadsException() {
	}

	public LeadsException(String message) {
		super(message);
	}

	public LeadsException(Throwable cause) {
		super(cause);
	}

	public LeadsException(String message, Throwable cause) {
		super(message, cause);
	}

	public LeadsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
