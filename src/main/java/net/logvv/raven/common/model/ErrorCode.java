package net.logvv.raven.common.model;


public enum ErrorCode 
{
	INVALID_REQ_PARAMS("000400", "参数有误"),
	PERMISSION_DENY("000403","访问拒绝"),
	SERVER_EXCEPTION("000500", "服务器忙,请稍后重试"),
    PUSH_MESSAGE_FAIL("000600","消息发送失败");
	
    private String code;
    private String msg;
    
    /** 产品错误码前缀 */
    private ErrorCode(String errCode, String errMsg)
    {
        this.code = errCode;
        this.msg = errMsg;
    }

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
    
}
