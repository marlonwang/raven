package net.logvv.raven.push.jpush.common.resp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class APIRequestException extends Exception implements IRateLimiting {
    private static final long serialVersionUID = -3921022835186996212L;
    
    protected static Gson _gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    
    private final ResponseWrapper responseWrapper;
    
    public APIRequestException(ResponseWrapper responseWrapper) {
        super(responseWrapper.responseContent);
        this.responseWrapper = responseWrapper;
    }
    
    public int getStatus() {
        return this.responseWrapper.responseCode;
    }
    
    public long getMsgId() {
        ResponseWrapper.ErrorObject eo = getErrorObject();
        if (null != eo) {
            return eo.msg_id;
        }
        return 0;
    }
    
    public int getErrorCode() {
        ResponseWrapper.ErrorObject eo = getErrorObject();
        if (null != eo && null != eo.error) {
            return eo.error.code;
        }
        return -1;
    }
    
    public String getErrorMessage() {
        ResponseWrapper.ErrorObject eo = getErrorObject();
        if (null != eo && null != eo.error) {
            return eo.error.message;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return _gson.toJson(this);
    }
    
    private ResponseWrapper.ErrorObject getErrorObject() {
        return this.responseWrapper.error;
    }
    

    @Override
    public int getRateLimitQuota() {
        return responseWrapper.rateLimitQuota;
    }

    @Override
    public int getRateLimitRemaining() {
        return responseWrapper.rateLimitRemaining;
    }

    @Override
    public int getRateLimitReset() {
        return responseWrapper.rateLimitReset;
    }
        
}

