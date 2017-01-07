package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String collapseKey;
    private final String payload;
    private final String title;
    private final String description;
    private final Integer notifyType;
    private final Long timeToLive;
    private final Integer passThrough;
    private final Integer notifyId;
    private final String[] restrictedPackageNames;
    private final Map<String, String> extra;
    private final Long timeToSend;
    public static final int PASS_THROUGH_PASS = 1;
    public static final int PASS_THROUGH_NOTIFICATION = 0;
    public static final int NOTIFY_TYPE_ALL = -1;
    public static final int NOTIFY_TYPE_SOUND = 1;
    public static final int NOTIFY_TYPE_VIBRATE = 2;
    public static final int NOTIFY_TYPE_LIGHTS = 4;

    protected Message(Message.IOSBuilder builder) {
        this.collapseKey = null;
        this.payload = null;
        this.title = null;
        this.description = builder.description;
        this.notifyType = null;
        this.timeToLive = builder.timeToLive;
        this.restrictedPackageNames = null;
        this.passThrough = null;
        this.notifyId = null;
        this.extra = builder.extra;
        this.timeToSend = builder.timeToSend;
    }

    protected Message(Message.Builder builder) {
        this.collapseKey = builder.collapseKey;
        this.payload = builder.payload;
        this.title = builder.title;
        this.description = builder.description;
        this.notifyType = builder.notifyType;
        this.timeToLive = builder.timeToLive;
        this.restrictedPackageNames = builder.restrictedPackageNames;
        this.passThrough = builder.passThrough;
        this.notifyId = builder.notifyId;
        this.extra = builder.extra;
        this.timeToSend = builder.timeToSend;
    }

    protected String getCollapseKey() {
        return this.collapseKey;
    }

    public String getPayload() {
        return this.payload;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getNotifyType() {
        return this.notifyType;
    }

    public Integer getNotifyId() {
        return this.notifyId;
    }

    public Long getTimeToLive() {
        return this.timeToLive;
    }

    public String getRestrictedPackageName() {
        return this.restrictedPackageNames != null && this.restrictedPackageNames.length != 0?this.restrictedPackageNames[0]:null;
    }

    public String[] getRestrictedPackageNames() {
        return this.restrictedPackageNames;
    }

    public Integer getPassThrough() {
        return this.passThrough;
    }

    public Map<String, String> getExtra() {
        return this.extra;
    }

    public Long getTimeToSend() {
        return this.timeToSend;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Message(");
        if(!XMStringUtils.isEmpty(this.collapseKey)) {
            builder.append("collapseKey=").append(this.collapseKey).append(", ");
        }

        if(!XMStringUtils.isEmpty(this.payload)) {
            builder.append("payload=").append(this.payload).append(", ");
        }

        if(!XMStringUtils.isEmpty(this.title)) {
            builder.append("title=").append(this.title).append(", ");
        }

        if(!XMStringUtils.isEmpty(this.description)) {
            builder.append("description=").append(this.description).append(", ");
        }

        if(this.timeToLive != null) {
            builder.append("timeToLive=").append(this.timeToLive).append(", ");
        }

        if(this.restrictedPackageNames != null && this.restrictedPackageNames.length != 0) {
            builder.append("restrictedPackageNames=").append("[").append(this.arrayToString(this.restrictedPackageNames)).append("]").append(", ");
        }

        if(this.notifyType != null) {
            builder.append("notifyType=").append(this.notifyType).append(", ");
        }

        if(this.notifyId != null) {
            builder.append("notifyId=").append(this.notifyId).append(", ");
        }

        if(!this.extra.isEmpty()) {
            Iterator var2 = this.extra.entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry entry = (Map.Entry)var2.next();
                builder.append("extra.").append((String)entry.getKey()).append("=").append((String)entry.getValue()).append(", ");
            }
        }

        if(builder.charAt(builder.length() - 1) == 32) {
            builder.delete(builder.length() - 2, builder.length());
        }

        builder.append(")");
        return builder.toString();
    }

    private String arrayToString(String[] array) {
        StringBuilder sb = new StringBuilder();

        int i;
        for(i = 0; i < array.length - 1; ++i) {
            sb.append(array[i]).append(",");
        }

        sb.append(array[i]);
        return sb.toString();
    }

    public static final class IOSBuilder {
        private String description;
        private Long timeToLive;
        private Map<String, String> extra = new LinkedHashMap();
        private Long timeToSend;

        public IOSBuilder() {
        }

        public Message.IOSBuilder description(String value) {
            this.description = value;
            return this;
        }

        public Message.IOSBuilder timeToLive(long value) {
            this.timeToLive = Long.valueOf(value);
            return this;
        }

        public Message.IOSBuilder extra(String key, String value) {
            this.extra.put(key, value);
            return this;
        }

        public Message.IOSBuilder timeToSend(long timeToSend) {
            this.timeToSend = Long.valueOf(timeToSend);
            return this;
        }

        public Message build() {
            return new Message(this);
        }

        public Message.IOSBuilder badge(int badge) {
            this.extra.put("badge", String.valueOf(badge));
            return this;
        }

        public Message.IOSBuilder category(String category) {
            this.extra.put("category", category);
            return this;
        }

        public Message.IOSBuilder soundURL(String url) {
            this.extra.put("sound_url", url);
            return this;
        }

        public Message.IOSBuilder apnsOnly() {
            this.extra.put("ios_msg_channel", "1");
            return this;
        }

        public Message.IOSBuilder connectionOnly() {
            this.extra.put("ios_msg_channel", "2");
            return this;
        }
    }

    public static final class Builder {
        private String collapseKey;
        private String payload;
        private String title;
        private String description;
        private Integer notifyType;
        private Long timeToLive;
        private String[] restrictedPackageNames;
        private Integer passThrough = Integer.valueOf(0);
        private Integer notifyId = Integer.valueOf(0);
        private Map<String, String> extra = new LinkedHashMap();
        private Long timeToSend;

        public Builder() {
        }

        protected Message.Builder collapseKey(String value) {
            this.collapseKey = value;
            return this;
        }

        public Message.Builder payload(String value) {
            this.payload = value;
            return this;
        }

        public Message.Builder title(String value) {
            this.title = value;
            return this;
        }

        public Message.Builder description(String value) {
            this.description = value;
            return this;
        }

        public Message.Builder notifyType(Integer value) {
            this.notifyType = value;
            return this;
        }

        public Message.Builder notifyId(Integer value) {
            this.notifyId = value;
            return this;
        }

        public Message.Builder timeToLive(long value) {
            this.timeToLive = Long.valueOf(value);
            return this;
        }

        public Message.Builder restrictedPackageName(String value) {
            this.restrictedPackageNames = new String[1];
            this.restrictedPackageNames[0] = value;
            return this;
        }

        public Message.Builder restrictedPackageNames(String[] value) {
            this.restrictedPackageNames = value;
            return this;
        }

        public Message.Builder passThrough(int passThrough) {
            this.passThrough = Integer.valueOf(passThrough);
            return this;
        }

        public Message.Builder extra(String key, String value) {
            this.extra.put(key, value);
            return this;
        }

        public Message.Builder timeToSend(long timeToSend) {
            this.timeToSend = Long.valueOf(timeToSend);
            return this;
        }

        public Message.Builder enableFlowControl(boolean needFlowControl) {
            if(needFlowControl) {
                this.extra.put("flow_control", "1");
            } else {
                this.extra.remove("flow_control");
            }

            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}

