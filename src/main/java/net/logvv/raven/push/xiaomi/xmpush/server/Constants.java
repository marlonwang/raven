package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public class Constants {
    static final String SDK_VERSION = "2016.08.15";
    public static boolean INCLUDE_LAST_METRICS = true;
    public static final long MAX_MESSAGE_LENGTH = 140L;
    static boolean autoSwitchHost = true;
    static int accessTimeOut = 5000;
    static String HTTP_PROTOCOL = "https";
    static boolean USE_HTTPS = true;
    static String HOST_SANDBOX = "sandbox.xmpush.xiaomi.com";
    static String HOST_PRODUCTION = "api.xmpush.xiaomi.com";
    static String HOST_PRODUCTION_B1 = "lg.api.xmpush.xiaomi.com";
    static String HOST_PRODUCTION_B2 = "c3.api.xmpush.xiaomi.com";
    static String HOST_PRODUCTION_FEEDBACK = "feedback.xmpush.xiaomi.com";
    static boolean sandbox = false;
    static String host = null;
    public static final String PARAM_REGISTRATION_ID = "registration_id";
    public static final String PARAM_COLLAPSE_KEY = "collapse_key";
    public static final String PARAM_JOB_KEY = "jobkey";
    public static final String PARAM_PAYLOAD = "payload";
    public static final String PARAM_TOPIC = "topic";
    public static final String PARAM_ALIAS = "alias";
    public static final String PARAM_ALIASES = "aliases";
    public static final String PARAM_USER_ACCOUNT = "user_account";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_DESCRIPTION = "description";
    public static final String PARAM_NOTIFY_TYPE = "notify_type";
    public static final String PARAM_NOTIFY_ID = "notify_id";
    public static final String PARAM_TIMER_TO_SEND = "time_to_send";
    public static final String PARAM_URL = "url";
    public static final String PARAM_PASS_THROUGH = "pass_through";
    public static final String PARAM_MESSAGES = "messages";
    public static final String PARAM_NAME_EXTRA_PREFIX = "extra.";
    public static final String PARAM_CATEGORY = "category";
    public static final String PARAM_JOB_ID = "job_id";
    public static final String PARAM_TOPICS = "topics";
    public static final String PARAM_TOPIC_OP = "topic_op";
    public static final String PARAM_APPID = "app_id";
    public static final String PARAM_START_TS = "start_time";
    public static final String PARAM_END_TS = "end_time";
    public static final String PARAM_JOB_TYPE = "type";
    public static final String PARAM_MAX_COUNT = "max_count";
    public static final String EXTRA_PARAM_SOUND_URI = "sound_uri";
    public static final String EXTRA_PARAM_NOTIFY_EFFECT = "notify_effect";
    public static final String NOTIFY_LAUNCHER_ACTIVITY = "1";
    public static final String NOTIFY_ACTIVITY = "2";
    public static final String NOTIFY_WEB = "3";
    public static final String EXTRA_PARAM_INTENT_URI = "intent_uri";
    public static final String EXTRA_PARAM_WEB_URI = "web_uri";
    public static final String EXTRA_PARAM_NOTIFICATION_TICKER = "ticker";
    public static final String EXTRA_PARAM_CLASS_NAME = "class_name";
    public static final String EXTRA_PARAM_INTENT_FLAG = "intent_flag";
    public static final String EXTRA_PARAM_IOS_MSG_CHANNEL = "ios_msg_channel";
    public static final String EXTRA_PARAM_IOS_MSG_CHANNEL_APNS_ONLY = "1";
    public static final String EXTRA_PARAM_IOS_MSG_CHANNEL_CONNECTION_ONLY = "2";
    public static final String EXTRA_PARAM_NOTIFY_FOREGROUND = "notify_foreground";
    public static final String EXTRA_PARAM_ALERT_TITLE = "apsAlert-title";
    public static final String EXTRA_PARAM_ALERT_BODY = "apsAlert-body";
    public static final String EXTRA_PARAM_ALERT_TITLE_LOC_KEY = "apsAlert-title-loc-key";
    public static final String EXTRA_PARAM_ALERT_TITLE_LOC_ARGS = "apsAlert-title-loc-args";
    public static final String EXTRA_PARAM_ALERT_ACTION_LOC_KEY = "apsAlert-action-loc-key";
    public static final String EXTRA_PARAM_ALERT_LOC_KEY = "apsAlert-loc-key";
    public static final String EXTRA_PARAM_ALERT_LOC_ARGS = "apsAlert-loc-args";
    public static final String EXTRA_PARAM_ALERT_LAUNCH_IMAGE = "apsAlert-launch-image";
    public static final String PARAM_DELAY_WHILE_IDLE = "delay_while_idle";
    public static final String PARAM_DRY_RUN = "dry_run";
    public static final String PARAM_RESTRICTED_PACKAGE_NAME = "restricted_package_name";
    public static final String PARAM_PAYLOAD_PREFIX = "data.";
    public static final String PARAM_TIME_TO_LIVE = "time_to_live";
    public static final String ERROR_QUOTA_EXCEEDED = "QuotaExceeded";
    public static final String ERROR_DEVICE_QUOTA_EXCEEDED = "DeviceQuotaExceeded";
    public static final String ERROR_MISSING_REGISTRATION = "MissingRegistration";
    public static final String ERROR_INVALID_REGISTRATION = "InvalidRegistration";
    public static final String ERROR_MISMATCH_SENDER_ID = "MismatchSenderId";
    public static final String ERROR_NOT_REGISTERED = "NotRegistered";
    public static final String ERROR_MESSAGE_TOO_BIG = "MessageTooBig";
    public static final String ERROR_MISSING_COLLAPSE_KEY = "MissingCollapseKey";
    public static final String ERROR_UNAVAILABLE = "Unavailable";
    public static final String ERROR_INTERNAL_SERVER_ERROR = "InternalServerError";
    public static final String ERROR_INVALID_TTL = "InvalidTtl";
    public static final String TOKEN_MESSAGE_ID = "id";
    public static final String TOKEN_CANONICAL_REG_ID = "registration_id";
    public static final String TOKEN_ERROR = "Error";
    public static final String REGISTRATION_IDS = "registration_ids";
    public static final String JSON_PAYLOAD = "data";
    public static final String JSON_SUCCESS = "success";
    public static final String JSON_FAILURE = "failure";
    public static final String JSON_MULTICAST_ID = "multicast_id";
    public static final String JSON_RESULTS = "results";
    public static final String JSON_ERROR = "error";
    public static final String JSON_MESSAGE_ID = "message_id";
    public static final String PARAM_START_DATE = "start_date";
    public static final String PARAM_END_DATE = "end_date";
    public static final String TRACE_BEGIN_TIME = "begin_time";
    public static final String TRACE_END_TIME = "end_time";
    public static final String TRACE_MSG_ID = "msg_id";
    public static final String TRACE_JOB_KEY = "job_key";

    protected Constants() {
        throw new UnsupportedOperationException();
    }

    public static void useSandbox() {
        sandbox = true;
        host = null;
    }

    public static void useOfficial() {
        sandbox = false;
        host = null;
    }

    public static void useInternalHost(String hostOrIP) {
        host = hostOrIP;
    }

    public static void useHttp() {
        HTTP_PROTOCOL = "http";
        USE_HTTPS = false;
    }

    static enum XmPushRequestPath implements RequestPath {
        V2_SEND("/v2/send"),
        V2_REGID_MESSAGE("/v2/message/regid"),
        V3_REGID_MESSAGE("/v3/message/regid"),
        V2_SUBSCRIBE_TOPIC("/v2/topic/subscribe"),
        V2_UNSUBSCRIBE_TOPIC("/v2/topic/unsubscribe"),
        V2_SUBSCRIBE_TOPIC_BY_ALIAS("/v2/topic/subscribe/alias"),
        V2_UNSUBSCRIBE_TOPIC_BY_ALIAS("/v2/topic/unsubscribe/alias"),
        V2_ALIAS_MESSAGE("/v2/message/alias"),
        V3_ALIAS_MESSAGE("/v3/message/alias"),
        V2_BROADCAST_TO_ALL("/v2/message/all"),
        V3_BROADCAST_TO_ALL("/v3/message/all"),
        V2_BROADCAST("/v2/message/topic"),
        V3_BROADCAST("/v3/message/topic"),
        V2_MULTI_TOPIC_BROADCAST("/v2/message/multi_topic"),
        V3_MULTI_TOPIC_BROADCAST("/v3/message/multi_topic"),
        V2_DELETE_BROADCAST_MESSAGE("/v2/message/delete"),
        V2_USERACCOUNT_MESSAGE("/v2/message/user_account"),
        V2_SEND_MULTI_MESSAGE_WITH_REGID("/v2/multi_messages/regids"),
        V2_SEND_MULTI_MESSAGE_WITH_ALIAS("/v2/multi_messages/aliases"),
        V2_SEND_MULTI_MESSAGE_WITH_ACCOUNT("/v2/multi_messages/user_accounts"),
        V1_VALIDATE_REGID("/v1/validation/regids"),
        V1_GET_ALL_ACCOUNT("/v1/account/all"),
        V1_GET_ALL_TOPIC("/v1/topic/all"),
        V1_GET_ALL_ALIAS("/v1/alias/all"),
        V1_MESSAGES_STATUS("/v1/trace/messages/status"),
        V1_MESSAGE_STATUS("/v1/trace/message/status"),
        V1_GET_MESSAGE_COUNTERS("/v1/stats/message/counters"),

        V1_FEEDBACK_INVALID_ALIAS("/v1/feedback/fetch_invalid_aliases",XmPushRequestType.FEEDBACK),
        V1_FEEDBACK_INVALID_REGID("/v1/feedback/fetch_invalid_regids",XmPushRequestType.FEEDBACK),
        V1_REGID_PRESENCE("/v1/regid/presence"),
        V2_REGID_PRESENCE("/v2/regid/presence"),
        V2_DELETE_SCHEDULE_JOB("/v2/schedule_job/delete"),
        V3_DELETE_SCHEDULE_JOB("/v3/schedule_job/delete"),
        V2_CHECK_SCHEDULE_JOB_EXIST("/v2/schedule_job/exist"),
        V2_QUERY_SCHEDULE_JOB("/v2/schedule_job/query");

        private String path;
        private XmPushRequestType requestType;

        private XmPushRequestPath(String path, XmPushRequestType requestType) {
            this.path = path;
            this.requestType = requestType;
        }

        private XmPushRequestPath(String path) {
            this(path, XmPushRequestType.MESSAGE);
        }

        public String getPath() {
            return this.path;
        }

        public XmPushRequestType getRequestType() {
            return this.requestType;
        }

        public String toString() {
            return this.path;
        }

    }

    interface RequestPath {
        String getPath();

        XmPushRequestType getRequestType();
    }

    static enum XmPushRequestType {
        MESSAGE,
        FEEDBACK;

        private XmPushRequestType() {
        }
    }
}
