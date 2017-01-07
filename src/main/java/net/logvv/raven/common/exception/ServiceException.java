package net.logvv.raven.common.exception;

import net.logvv.raven.common.model.ErrorCode;

@SuppressWarnings("serial")
public class ServiceException extends Exception{
	private String errCode;
	private String errMsg;
	
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public ServiceException(String errorCode, String errorMessage) {
		super(errorMessage);
		this.errCode = errorCode;
		this.errMsg = errorMessage;
	}
	
	public ServiceException(ErrorCode errorCode){
		super(errorCode.getMsg());
		this.errCode = errorCode.getCode();
		this.errMsg = errorCode.getMsg();
	}
	
	public ServiceException(Throwable cause) {
		super(cause);
		if (cause instanceof ServiceException) {
			this.errCode = ((ServiceException) cause).getErrCode();
		} else {
			this.errCode = ErrorCode.SERVER_EXCEPTION.getCode();
			this.errMsg = ErrorCode.SERVER_EXCEPTION.getMsg();
		}
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		if (cause instanceof ServiceException) {
			this.errCode = ((ServiceException) cause).getErrCode();
		} else {
			this.errCode = ErrorCode.SERVER_EXCEPTION.getCode();
			this.errMsg = ErrorCode.SERVER_EXCEPTION.getMsg();
		}
	}
	
	public ServiceException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errCode = errorCode;
	}
	
}
