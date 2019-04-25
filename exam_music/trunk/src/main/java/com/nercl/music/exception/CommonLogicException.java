package com.nercl.music.exception;

public class CommonLogicException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5837294008696760546L;

	private Integer errorCode = 500;

	public CommonLogicException() {
	}

	public CommonLogicException(int errorCode) {
		this.errorCode = errorCode;
	}

	public CommonLogicException(String message) {
		super(message);
	}

	public CommonLogicException(Throwable cause) {
		super(cause);
	}

	public CommonLogicException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public CommonLogicException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public CommonLogicException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonLogicException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public static CommonLogicException create(Integer code, String message) {
		return new CommonLogicException(code, message);
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
