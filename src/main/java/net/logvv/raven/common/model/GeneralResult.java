package net.logvv.raven.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralResult 
{
	// 结果状态, true 成功 false 失败
    private boolean resultStatus;
    
    // 如果错误则返回 错误代码
    private String errorCode;
    
    // 错误详情描述
    private String errorMessage;
    
    // 结果为true时的 业务数据，可为空
    private Object resultData;
    
    public GeneralResult(){
    	
    }
    
    public GeneralResult(ErrorCode error){
    	this.resultStatus = false;
    	this.errorCode = error.getCode();
    	this.errorMessage = error.getMsg();
    }
    
    public GeneralResult(ErrorCode error,Object obj){
    	this.resultStatus = false;
    	this.errorCode = error.getCode();
    	this.errorMessage = error.getMsg();
    	this.resultData = obj;
    }

	public boolean isResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(boolean resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errCode) {
		this.errorCode = errCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errMsg) {
		this.errorMessage = errMsg;
	}

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}
	
	public void setErr(ErrorCode errCode) {
		this.resultStatus = false;
		this.errorCode = errCode.getCode();
		this.errorMessage = errCode.getMsg();
	}
	
}
