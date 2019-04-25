package com.nercl.music.exception;

import com.nercl.music.constant.CList;

public class NoPrivilegeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5669589966105043863L;

	private Integer errorCode = CList.Api.Client.NO_PRIVILEGE;

	public NoPrivilegeException() {
	}

	public NoPrivilegeException(int errorCode) {
		this.errorCode = errorCode;
	}

	public NoPrivilegeException(String message) {
		super(message);
	}

	public NoPrivilegeException(Throwable cause) {
		super(cause);
	}

	public NoPrivilegeException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public NoPrivilegeException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public NoPrivilegeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoPrivilegeException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public static NoPrivilegeException create(Integer code, String message) {
		return new NoPrivilegeException(code, message);
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#fillInStackTrace()
	 */
	@Override
	public Throwable fillInStackTrace() {
		if (getCause() != null) {
			return super.fillInStackTrace();
		}
		return this;
	}

}
