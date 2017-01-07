package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import net.logvv.raven.push.xiaomi.xmpush.ErrorCode;
import org.json.simple.JSONObject;

import java.io.Serializable;

public final class Result implements Serializable {
    private static final long serialVersionUID = 8030699726843716781L;
    private final String messageId;
    private final ErrorCode errorCode;
    private final JSONObject data;
    private final String reason;

    private Result(Result.Builder builder) {
        this.messageId = builder.messageId;
        this.data = builder.data;
        this.errorCode = builder.errorCode;
        this.reason = builder.reason;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public String getReason() {
        return this.reason;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public JSONObject getData() {
        return this.data;
    }

    public String getData(String key) {
        return this.data == null?null:(this.data.containsKey(key)?(String)this.data.get(key):null);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        if(this.messageId != null) {
            builder.append(" messageId=").append(this.messageId);
        }

        if(this.errorCode != null) {
            builder.append(" errorCode=").append(this.errorCode.getValue());
        }

        if(this.reason != null) {
            builder.append(" reason=").append(this.reason);
        }

        if(this.data != null) {
            builder.append(" data=").append(this.data.toString());
        }

        return builder.append(" ]").toString();
    }

    public static final class Builder {
        private String messageId;
        private JSONObject data;
        private ErrorCode errorCode;
        private String reason;

        public Builder() {
        }

        public Result fromJson(JSONObject json) {
            if(json.containsKey("data")) {
                this.data = (JSONObject)json.get("data");
            }

            this.reason = (String)json.get("reason");
            Integer code = Integer.valueOf(((Long)json.get("code")).intValue());
            this.errorCode = ErrorCode.valueOf(code, this.reason);
            this.messageId = this.data == null?null:(String)this.data.get("id");
            if((this.messageId == null || this.messageId.length() == 0) && json.containsKey("trace_id")) {
                this.messageId = (String)json.get("trace_id");
            }

            return this.build();
        }

        public Result.Builder messageId(String msgId) {
            this.messageId = msgId;
            return this;
        }

        public Result.Builder errorCode(ErrorCode value) {
            this.errorCode = value;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }
}

