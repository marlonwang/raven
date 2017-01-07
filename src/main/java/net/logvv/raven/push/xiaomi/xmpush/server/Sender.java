//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.logvv.raven.push.xiaomi.xmpush.server;

import net.logvv.raven.push.xiaomi.xmpush.ErrorCode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Level;

public class Sender extends HttpBase {
    protected JSONObject lastResult;
    public static final int BROADCAST_TOPIC_MAX = 5;
    private static final String TOPIC_SPLITTER = ";$;";

    public Sender(String security) {
        super((String)nonNull(security));
    }

    public Sender(String security, String token) {
        super((String)nonNull(security), (String)nonNull(token));
    }

    public Result send(Message message, String registrationId, int retries) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;

        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to send message " + message + " to regIds " + registrationId);
            }

            result = this.sendNoRetry(message, registrationId);
            tryAgain = result == null && attempt <= retries;
            if(tryAgain) {
                int sleepTime = backoff / 2 + this.random.nextInt(backoff);
                this.sleep((long)sleepTime);
                if(2 * backoff < 1024000) {
                    backoff *= 2;
                }
            }
        } while(tryAgain);

        if(result == null) {
            throw this.exception(attempt);
        } else {
            return result;
        }
    }

    public Result broadcast(Message message, String topic, int retries) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;

        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to broadcast message " + message + " to topic: " + topic);
            }

            result = this.broadcastNoRetry(message, topic);
            tryAgain = result == null && attempt <= retries;
            if(tryAgain) {
                int sleepTime = backoff / 2 + this.random.nextInt(backoff);
                this.sleep((long)sleepTime);
                if(2 * backoff < 1024000) {
                    backoff *= 2;
                }
            }
        } while(tryAgain);

        if(result == null) {
            throw this.exception(attempt);
        } else {
            return result;
        }
    }

    public Result multiTopicBroadcast(Message message, List<String> topics, Sender.BROADCAST_TOPIC_OP topicOp, int retries) throws IOException, ParseException, IllegalArgumentException {
        if(topics != null && topics.size() > 0 && topics.size() <= 5) {
            if(topics.size() == 1) {
                return this.broadcast(message, (String)topics.get(0), retries);
            } else {
                int attempt = 0;
                int backoff = 1000;
                boolean tryAgain = false;

                Result result;
                do {
                    ++attempt;
                    logger.fine("Attempt #" + attempt + " to broadcast message " + message + " to topic: " + (String)topics.get(0) + " op=" + topicOp.toString());
                    result = this.multiTopicBroadcastNoRetry(message, topics, topicOp);
                    tryAgain = result == null && attempt <= retries;
                    if(tryAgain) {
                        int sleepTime = backoff / 2 + this.random.nextInt(backoff);
                        this.sleep((long)sleepTime);
                        if(2 * backoff < 1024000) {
                            backoff *= 2;
                        }
                    }
                } while(tryAgain);

                if(result == null) {
                    throw this.exception(attempt);
                } else {
                    return result;
                }
            }
        } else {
            throw new IllegalArgumentException("topics size invalid");
        }
    }

    public Result broadcastAll(Message message, int retries) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;

        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to broadcast message " + message + " to all");
            }

            result = this.broadcastAllNoRetry(message);
            tryAgain = result == null && attempt <= retries;
            if(tryAgain) {
                int sleepTime = backoff / 2 + this.random.nextInt(backoff);
                this.sleep((long)sleepTime);
                if(2 * backoff < 1024000) {
                    backoff *= 2;
                }
            }
        } while(tryAgain);

        if(result == null) {
            throw this.exception(attempt);
        } else {
            return result;
        }
    }

    public Result sendToAlias(Message message, String alias, int retries) throws IOException, ParseException {
        ArrayList aliases = new ArrayList();
        aliases.add(alias);
        return this.sendToAlias(message, (List)aliases, retries);
    }

    public Result sendToAlias(Message message, List<String> aliases, int retries) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;

        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to send message " + message + " to alias " + aliases);
            }

            result = this.sendToAliasNoRetry(message, aliases);
            tryAgain = result == null && attempt <= retries;
            if(tryAgain) {
                int sleepTime = backoff / 2 + this.random.nextInt(backoff);
                this.sleep((long)sleepTime);
                if(2 * backoff < 1024000) {
                    backoff *= 2;
                }
            }
        } while(tryAgain);

        if(result == null) {
            throw this.exception(attempt);
        } else {
            return result;
        }
    }

    protected Result sendToAliasNoRetry(Message message, List<String> aliases) throws IOException, ParseException {
        StringBuilder body = newBodyWithArrayParameters("alias", aliases);
        return this.sendMessageNoRetry(Constants.XmPushRequestPath.V3_ALIAS_MESSAGE, message, body);
    }

    public Result sendToUserAccount(Message message, String userAccount, int retries) throws IOException, ParseException {
        ArrayList userAccounts = new ArrayList();
        userAccounts.add(userAccount);
        return this.sendToUserAccount(message, (List)userAccounts, retries);
    }

    public Result sendToUserAccount(Message message, List<String> userAccounts, int retries) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;

        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to send message " + message + " to user account " + userAccounts);
            }

            result = this.sendToUserAccountNoRetry(message, userAccounts);
            tryAgain = result == null && attempt <= retries;
            if(tryAgain) {
                int sleepTime = backoff / 2 + this.random.nextInt(backoff);
                this.sleep((long)sleepTime);
                if(2 * backoff < 1024000) {
                    backoff *= 2;
                }
            }
        } while(tryAgain);

        if(result == null) {
            throw this.exception(attempt);
        } else {
            return result;
        }
    }

    public Result sendToUserAccountNoRetry(Message message, List<String> userAccounts) throws IOException, ParseException {
        StringBuilder body = newBodyWithArrayParameters("user_account", userAccounts);
        return this.sendMessageNoRetry(Constants.XmPushRequestPath.V2_USERACCOUNT_MESSAGE, message, body);
    }

    public Result sendNoRetry(Message message, String registrationId) throws IOException, ParseException {
        StringBuilder body = newBody("registration_id", URLEncoder.encode(registrationId, "UTF-8"));
        return this.sendMessageNoRetry(Constants.XmPushRequestPath.V3_REGID_MESSAGE, message, body);
    }

    protected Result broadcastNoRetry(Message message, String topic) throws IOException, ParseException {
        StringBuilder body = newBody("topic", URLEncoder.encode(topic, "UTF-8"));
        return isMultiPackageName(message)?this.sendMultiPackageNameMessageNoRetry(Constants.XmPushRequestPath.V3_BROADCAST, message, body):this.sendMessageNoRetry(Constants.XmPushRequestPath.V2_BROADCAST, message, body);
    }

    protected Result multiTopicBroadcastNoRetry(Message message, List<String> topics, Sender.BROADCAST_TOPIC_OP topicOp) throws IOException, ParseException, IllegalArgumentException {
        if(topics != null && topics.size() > 0 && topics.size() <= 5) {
            if(topics.size() == 1) {
                return this.broadcastNoRetry(message, (String)topics.get(0));
            } else {
                StringBuilder body = newBody("topic_op", topicOp.toString());
                StringBuilder topicsStr = new StringBuilder();

                String topic;
                for(Iterator var6 = topics.iterator(); var6.hasNext(); topicsStr.append(topic)) {
                    topic = (String)var6.next();
                    if(topicsStr.length() != 0) {
                        topicsStr.append(";$;");
                    }
                }

                addParameter(body, "topics", URLEncoder.encode(topicsStr.toString(), "UTF-8"));
                if(isMultiPackageName(message)) {
                    return this.sendMultiPackageNameMessageNoRetry(Constants.XmPushRequestPath.V3_MULTI_TOPIC_BROADCAST, message, body);
                } else {
                    return this.sendMessageNoRetry(Constants.XmPushRequestPath.V2_MULTI_TOPIC_BROADCAST, message, body);
                }
            }
        } else {
            throw new IllegalArgumentException("topics size invalid");
        }
    }

    protected Result broadcastAllNoRetry(Message message) throws IOException, ParseException {
        StringBuilder body = new StringBuilder("");
        return isMultiPackageName(message)?this.sendMultiPackageNameMessageNoRetry(Constants.XmPushRequestPath.V3_BROADCAST_TO_ALL, message, body):this.sendMessageNoRetry(Constants.XmPushRequestPath.V2_BROADCAST_TO_ALL, message, body);
    }

    private Result sendMessageNoRetry(Constants.XmPushRequestPath requestPath, Message message, StringBuilder target) throws IOException, ParseException {
        StringBuilder body = new StringBuilder(target);
        if(!XMStringUtils.isEmpty(message.getCollapseKey())) {
            addParameter(body, "collapse_key", URLEncoder.encode(message.getCollapseKey(), "UTF-8"));
        }

        if(!XMStringUtils.isEmpty(message.getRestrictedPackageName())) {
            addParameter(body, "restricted_package_name", URLEncoder.encode(message.getRestrictedPackageName(), "UTF-8"));
        }

        Long timeToLive = message.getTimeToLive();
        if(timeToLive != null) {
            addParameter(body, "time_to_live", Long.toString(timeToLive.longValue()));
        }

        if(!XMStringUtils.isEmpty(message.getPayload())) {
            addParameter(body, "payload", URLEncoder.encode(message.getPayload(), "UTF-8"));
        }

        if(!XMStringUtils.isEmpty(message.getTitle())) {
            addParameter(body, "title", URLEncoder.encode(message.getTitle(), "UTF-8"));
        }

        if(!XMStringUtils.isEmpty(message.getDescription())) {
            addParameter(body, "description", URLEncoder.encode(message.getDescription(), "UTF-8"));
        }

        if(message.getNotifyType() != null) {
            addParameter(body, "notify_type", Integer.toString(message.getNotifyType().intValue()));
        }

        if(message.getPassThrough() != null) {
            addParameter(body, "pass_through", Integer.toString(message.getPassThrough().intValue()));
        }

        if(message.getNotifyId() != null) {
            addParameter(body, "notify_id", Integer.toString(message.getNotifyId().intValue()));
        }

        if(message.getTimeToSend() != null) {
            addParameter(body, "time_to_send", Long.toString(message.getTimeToSend().longValue()));
        }

        Map extraInfo = message.getExtra();
        if(extraInfo != null && !extraInfo.isEmpty()) {
            Iterator bodyStr = extraInfo.entrySet().iterator();

            while(bodyStr.hasNext()) {
                Map.Entry entry = (Map.Entry)bodyStr.next();
                addParameter(body, URLEncoder.encode("extra." + (String)entry.getKey(), "UTF-8"), URLEncoder.encode((String)entry.getValue(), "UTF-8"));
            }
        }

        String bodyStr1 = body.toString();
        if(!bodyStr1.isEmpty() && bodyStr1.charAt(0) == 38) {
            bodyStr1 = body.toString().substring(1);
        }

        return this.sendMessage(requestPath, bodyStr1);
    }

    protected Result sendMultiPackageNameMessageNoRetry(Constants.XmPushRequestPath requestPath, Message message, StringBuilder target) throws IOException, ParseException {
        StringBuilder body = new StringBuilder(target);
        if(message.getRestrictedPackageNames() != null && message.getRestrictedPackageNames().length != 0) {
            ((StringBuilder)nonNull(body)).append('&').append(newBodyWithArrayParameters("restricted_package_name", Arrays.asList(message.getRestrictedPackageNames())));
        }

        if(!XMStringUtils.isEmpty(message.getCollapseKey())) {
            addParameter(body, "collapse_key", URLEncoder.encode(message.getCollapseKey(), "UTF-8"));
        }

        Long timeToLive = message.getTimeToLive();
        if(timeToLive != null) {
            addParameter(body, "time_to_live", Long.toString(timeToLive.longValue()));
        }

        if(!XMStringUtils.isEmpty(message.getPayload())) {
            addParameter(body, "payload", URLEncoder.encode(message.getPayload(), "UTF-8"));
        }

        if(!XMStringUtils.isEmpty(message.getTitle())) {
            addParameter(body, "title", URLEncoder.encode(message.getTitle(), "UTF-8"));
        }

        if(!XMStringUtils.isEmpty(message.getDescription())) {
            addParameter(body, "description", URLEncoder.encode(message.getDescription(), "UTF-8"));
        }

        if(message.getNotifyType() != null) {
            addParameter(body, "notify_type", Integer.toString(message.getNotifyType().intValue()));
        }

        if(message.getPassThrough() != null) {
            addParameter(body, "pass_through", Integer.toString(message.getPassThrough().intValue()));
        }

        if(message.getNotifyId() != null) {
            addParameter(body, "notify_id", Integer.toString(message.getNotifyId().intValue()));
        }

        if(message.getTimeToSend() != null) {
            addParameter(body, "time_to_send", Long.toString(message.getTimeToSend().longValue()));
        }

        Map extraInfo = message.getExtra();
        if(extraInfo != null && !extraInfo.isEmpty()) {
            Iterator bodyStr = extraInfo.entrySet().iterator();

            while(bodyStr.hasNext()) {
                Map.Entry entry = (Map.Entry)bodyStr.next();
                addParameter(body, URLEncoder.encode("extra." + (String)entry.getKey(), "UTF-8"), URLEncoder.encode((String)entry.getValue(), "UTF-8"));
            }
        }

        String bodyStr1 = body.toString();
        if(!bodyStr1.isEmpty() && bodyStr1.charAt(0) == 38) {
            bodyStr1 = body.toString().substring(1);
        }

        return this.sendMessage(requestPath, bodyStr1);
    }

    private Result execScheduleJobNoRetry(Constants.XmPushRequestPath requestPath, StringBuilder target) throws IOException, ParseException {
        return this.sendMessage(requestPath, target.toString());
    }

    public Result send(Message message, List<String> regIds, int retries) throws IOException, ParseException {
        StringBuilder sb = new StringBuilder((String)regIds.get(0));

        for(int i = 1; i < regIds.size(); ++i) {
            sb.append(",").append((String)regIds.get(i));
        }

        return this.send(message, sb.toString(), retries);
    }

    public Result send(List<TargetedMessage> messages, int retries) throws IOException, ParseException {
        return this.send(messages, retries, 0L);
    }

    public Result send(List<TargetedMessage> messages, int retries, long timeToSend) throws IOException, ParseException {
        if(messages.isEmpty()) {
            logger.log(Level.WARNING, "Empty message, returned. Remote server " + this.remoteHost + "(" + this.remoteIp + ")");
            return (new Result.Builder()).errorCode(ErrorCode.Success).build();
        } else {
            int attempt = 0;
            Result result = null;
            int backoff = 1000;
            Constants.XmPushRequestPath requestPath;
            if(((TargetedMessage)messages.get(0)).getTargetType() == 2) {
                requestPath = Constants.XmPushRequestPath.V2_SEND_MULTI_MESSAGE_WITH_ALIAS;
            } else if(((TargetedMessage)messages.get(0)).getTargetType() == 3) {
                requestPath = Constants.XmPushRequestPath.V2_SEND_MULTI_MESSAGE_WITH_ACCOUNT;
            } else {
                requestPath = Constants.XmPushRequestPath.V2_SEND_MULTI_MESSAGE_WITH_REGID;
            }

            StringBuilder body = newBody("messages", URLEncoder.encode(this.toString(messages), "UTF-8"));
            addParameter(body, "time_to_send", Long.toString(timeToSend));
            String message = body.toString();

            boolean tryAgain;
            do {
                ++attempt;
                if(logger.isLoggable(Level.FINE)) {
                    logger.fine("Attempt #" + attempt + " to send messages " + messages.size());
                }

                result = this.sendMessage(requestPath, message);
                tryAgain = result == null && attempt <= retries;
                if(tryAgain) {
                    int sleepTime = backoff / 2 + this.random.nextInt(backoff);
                    this.sleep((long)sleepTime);
                    if(2 * backoff < 1024000) {
                        backoff *= 2;
                    }
                }
            } while(tryAgain);

            if(result == null) {
                throw this.exception(attempt);
            } else {
                return result;
            }
        }
    }

    public Result deleteScheduleJob(String jobId) throws IOException, ParseException {
        StringBuilder body = newBody("job_id", URLEncoder.encode(jobId, "UTF-8"));
        return this.execScheduleJobNoRetry(Constants.XmPushRequestPath.V2_DELETE_SCHEDULE_JOB, body);
    }

    public Result deleteScheduleJobByJobKey(String jobKey) throws IOException, ParseException {
        StringBuilder body = newBody("jobkey", URLEncoder.encode(jobKey, "UTF-8"));
        return this.execScheduleJobNoRetry(Constants.XmPushRequestPath.V3_DELETE_SCHEDULE_JOB, body);
    }

    public Result checkScheduleJobExist(String jobId) throws IOException, ParseException {
        StringBuilder body = newBody("job_id", URLEncoder.encode(jobId, "UTF-8"));
        return this.execScheduleJobNoRetry(Constants.XmPushRequestPath.V2_CHECK_SCHEDULE_JOB_EXIST, body);
    }

    protected Result sendMessage(Constants.RequestPath requestPath, String message) throws IOException, ParseException {
        HttpURLConnection conn = null;

        int status;
        try {
            logger.fine("post to: " + requestPath.getPath());
            conn = this.doPost(requestPath, message);
            status = conn.getResponseCode();
        } catch (IOException var11) {
            this.lastException = var11;
            String e = "Failed to send http request: remote server " + this.remoteHost + "(" + this.remoteIp + ")";
            logger.log(Level.WARNING, e, var11);
            return null;
        }

        if(status / 100 == 5) {
            logger.fine("XmPush service is unavailable (status " + status + ")");
            return null;
        } else {
            String responseBody;
            if(status != 200) {
                try {
                    responseBody = getAndClose(conn.getErrorStream());
                    logger.finest("Plain post error response: " + responseBody);
                } catch (IOException var8) {
                    responseBody = "N/A";
                    this.lastException = var8;
                    logger.log(Level.FINE, "Exception reading response: ", var8);
                }

                throw new InvalidRequestException(status, responseBody);
            } else {
                try {
                    responseBody = getAndClose(conn.getInputStream());
                } catch (IOException var10) {
                    this.lastException = var10;
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var10);
                    return null;
                }

                try {
                    JSONParser e1 = new JSONParser();
                    JSONObject json = (JSONObject)e1.parse(responseBody);
                    this.lastResult = json;
                    return (new Result.Builder()).fromJson(json);
                } catch (ParseException var9) {
                    logger.log(Level.WARNING, "Exception parsing response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var9);
                    this.lastException = var9;
                    throw new IOException("Invalid response from XmPush: " + responseBody + "\n server " + this.remoteHost + " ip " + this.remoteIp);
                }
            }
        }
    }

    private String toString(List<TargetedMessage> messages) {
        JSONArray jsonArray = new JSONArray();
        Iterator var3 = messages.iterator();

        while(var3.hasNext()) {
            TargetedMessage message = (TargetedMessage)var3.next();
            JSONObject jsonMessage = new JSONObject();
            JSONObject msg = this.toJson(message.getMessage());
            tryAddJson(jsonMessage, "target", message.getTarget());
            tryAddJson(jsonMessage, "message", msg);
            jsonArray.add(jsonMessage);
        }

        return jsonArray.toString();
    }

    private JSONObject toJson(Message msg) {
        JSONObject json = new JSONObject();
        tryAddJson(json, "payload", msg.getPayload());
        tryAddJson(json, "title", msg.getTitle());
        tryAddJson(json, "description", msg.getDescription());
        tryAddJson(json, "notify_type", msg.getNotifyType());
        tryAddJson(json, "notify_id", msg.getNotifyId());
        tryAddJson(json, "pass_through", msg.getPassThrough());
        tryAddJson(json, "restricted_package_name", msg.getRestrictedPackageName());
        tryAddJson(json, "time_to_live", msg.getTimeToLive());
        tryAddJson(json, "collapse_key", msg.getCollapseKey());
        Map extraInfo = msg.getExtra();
        if(extraInfo != null && !extraInfo.isEmpty()) {
            JSONObject extraJson = new JSONObject();
            Iterator var5 = extraInfo.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry entry = (Map.Entry)var5.next();
                tryAddJson(extraJson, (String)entry.getKey(), entry.getValue());
            }

            tryAddJson(json, "extra", extraJson);
        }

        return json;
    }

    protected static void tryAddJson(JSONObject json, String parameterName, Object value) {
        if(!XMStringUtils.isEmpty(parameterName) && value != null) {
            json.put(parameterName, value);
        }

    }

    protected static final Map<String, String> newKeyValues(String key, String value) {
        HashMap keyValues = new HashMap(1);
        keyValues.put(nonNull(key), nonNull(value));
        return keyValues;
    }

    protected static boolean isMultiPackageName(Message message) {
        String[] packageNames = message.getRestrictedPackageNames();
        return packageNames == null || packageNames.length == 0 || message.getRestrictedPackageNames().length >= 2;
    }

    protected void prepareConnection(HttpURLConnection conn) {
        super.prepareConnection(conn);
        if(Constants.INCLUDE_LAST_METRICS && this.lastResult != null && this.lastResult.containsKey("trace_id")) {
            String traceId = (String)this.lastResult.get("trace_id");
            conn.setRequestProperty("X-PUSH-LAST-REQUEST-ID", traceId);
        }

    }

    public static enum BROADCAST_TOPIC_OP {
        UNION,
        INTERSECTION,
        EXCEPT;

        private BROADCAST_TOPIC_OP() {
        }
    }
}
