package net.logvv.raven.push.hwpush.huawei;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * HMS返回结果
 * Author: wangwei
 * Created on 2016/12/8 10:58.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushRet {
    // 透传消息和通知栏消息返回结果
    private String message;
    private int resultcode;
    private String requestID;

    // 自定义(标签 通知栏消息返回结果)
    private int result_code;

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

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }
}
