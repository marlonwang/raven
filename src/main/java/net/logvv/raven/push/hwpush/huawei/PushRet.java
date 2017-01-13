package net.logvv.raven.push.hwpush.huawei;

/**
 * HMS返回结果
 * Author: wangwei
 * Created on 2016/12/8 10:58.
 */
public class PushRet {

    private String message;
    private int resultcode;
    private String requestID;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getResultcode()
    {
        return resultcode;
    }

    public void setResultcode(int resultcode)
    {
        this.resultcode = resultcode;
    }

    public String getRequestID()
    {
        return requestID;
    }

    public void setRequestID(String requestID)
    {
        this.requestID = requestID;
    }
}
