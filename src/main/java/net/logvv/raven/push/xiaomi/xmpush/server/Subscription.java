package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class Subscription extends HttpBase {
    public Subscription(String security) {
        super((String)nonNull(security));
    }

    protected static String joinString(List<String> stringList, char sep) {
        StringBuffer sb = new StringBuffer();
        Iterator var3 = stringList.iterator();

        while(var3.hasNext()) {
            String s = (String)var3.next();
            sb.append(sep).append(s);
        }

        return sb.substring(1);
    }

    public Result subscribeTopic(List<String> regIds, String topic, String category, int retries) throws IOException, ParseException {
        return this.topicSubscribeBase(regIds, topic, category, retries, true);
    }

    public Result subscribeTopic(List<String> regIds, String topic, String packageName, String category, int retries) throws IOException, ParseException {
        return this.topicSubscribeBase(regIds, topic, packageName, category, retries, true);
    }

    public Result subscribeTopic(List<String> regIds, String topic, String category) throws IOException, ParseException {
        return this.subscribeTopic((List)regIds, topic, category, 1);
    }

    public Result subscribeTopic(String regId, String topic, String category, int retries) throws IOException, ParseException {
        ArrayList regIds = new ArrayList();
        regIds.add(regId);
        return this.subscribeTopic((List)regIds, topic, category, retries);
    }

    public Result subscribeTopic(String regId, String topic, String packageName, String category, int retries) throws IOException, ParseException {
        ArrayList regIds = new ArrayList();
        regIds.add(regId);
        return this.subscribeTopic((List)regIds, topic, packageName, category, retries);
    }

    public Result subscribeTopic(String regId, String topic, String category) throws IOException, ParseException {
        return this.subscribeTopic((String)regId, topic, category, 1);
    }

    public Result subscribeTopic(String regId, String topic, String packageName, String category) throws IOException, ParseException {
        return this.subscribeTopic((String)regId, topic, packageName, category, 1);
    }

    public Result unsubscribeTopic(List<String> regIds, String topic, String category, int retries) throws IOException, ParseException {
        return this.topicSubscribeBase(regIds, topic, category, retries, false);
    }

    public Result unsubscribeTopic(List<String> regIds, String topic, String category) throws IOException, ParseException {
        return this.unsubscribeTopic((List)regIds, topic, category, 1);
    }

    public Result unsubscribeTopic(List<String> regIds, String topic, String packageName, String category, int retries) throws IOException, ParseException {
        return this.topicSubscribeBase(regIds, topic, packageName, category, retries, false);
    }

    public Result unsubscribeTopic(String regId, String topic, String category, int retries) throws IOException, ParseException {
        ArrayList regIds = new ArrayList();
        regIds.add(regId);
        return this.unsubscribeTopic((List)regIds, topic, category, retries);
    }

    public Result unsubscribeTopic(String regId, String topic, String packageName, String category, int retries) throws IOException, ParseException {
        ArrayList regIds = new ArrayList();
        regIds.add(regId);
        return this.unsubscribeTopic((List)regIds, topic, packageName, category, retries);
    }

    public Result unsubscribeTopic(String regId, String topic, String category) throws IOException, ParseException {
        return this.unsubscribeTopic((String)regId, topic, category, 1);
    }

    public Result unsubscribeTopic(String regId, String topic, String packageName, String category) throws IOException, ParseException {
        return this.unsubscribeTopic((String)regId, topic, packageName, category, 1);
    }

    public Result subscribeTopicByAlias(String topic, List<String> aliases, String category, int retries) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;

        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to subscribe topic " + topic + " for aliases " + aliases);
            }

            StringBuilder body = newBody("aliases", URLEncoder.encode(joinString(aliases, ','), "UTF-8"));
            addParameter(body, "topic", URLEncoder.encode(topic, "UTF-8"));
            if(category != null) {
                addParameter(body, "category", URLEncoder.encode(category, "UTF-8"));
            }

            String bodyStr = body.toString();
            result = this.sendMessage(Constants.XmPushRequestPath.V2_SUBSCRIBE_TOPIC_BY_ALIAS, bodyStr);
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
            throw new IOException("Could not subscribe topic after " + attempt + " attempts");
        } else {
            return result;
        }
    }

    public Result subscribeTopicByAlias(String topic, List<String> aliases, String packageName, String category, int retries) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;

        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to subscribe topic " + topic + " for aliases " + aliases);
            }

            StringBuilder body = newBody("aliases", URLEncoder.encode(joinString(aliases, ','), "UTF-8"));
            if(!XMStringUtils.isBlank(packageName)) {
                addParameter(body, "restricted_package_name", URLEncoder.encode(packageName, "UTF-8"));
            }

            addParameter(body, "topic", URLEncoder.encode(topic, "UTF-8"));
            if(category != null) {
                addParameter(body, "category", URLEncoder.encode(category, "UTF-8"));
            }

            String bodyStr = body.toString();
            result = this.sendMessage(Constants.XmPushRequestPath.V2_SUBSCRIBE_TOPIC_BY_ALIAS, bodyStr);
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
            throw new IOException("Could not subscribe topic after " + attempt + " attempts");
        } else {
            return result;
        }
    }

    public Result unsubscribeTopicByAlias(String topic, List<String> aliases, String category, int retries) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;

        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to unsubscribe topic " + topic + " for aliases " + aliases);
            }

            StringBuilder body = newBody("aliases", URLEncoder.encode(joinString(aliases, ','), "UTF-8"));
            addParameter(body, "topic", URLEncoder.encode(topic, "UTF-8"));
            if(category != null) {
                addParameter(body, "category", URLEncoder.encode(category, "UTF-8"));
            }

            String bodyStr = body.toString();
            result = this.sendMessage(Constants.XmPushRequestPath.V2_UNSUBSCRIBE_TOPIC_BY_ALIAS, bodyStr);
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
            throw new IOException("Could not unsubscribe topic after " + attempt + " attempts");
        } else {
            return result;
        }
    }

    public Result unsubscribeTopicByAlias(String topic, List<String> aliases, String packageName, String category, int retries) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;

        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to unsubscribe topic " + topic + " for aliases " + aliases);
            }

            StringBuilder body = newBody("aliases", URLEncoder.encode(joinString(aliases, ','), "UTF-8"));
            if(!XMStringUtils.isBlank(packageName)) {
                addParameter(body, "restricted_package_name", URLEncoder.encode(packageName, "UTF-8"));
            }

            addParameter(body, "topic", URLEncoder.encode(topic, "UTF-8"));
            if(category != null) {
                addParameter(body, "category", URLEncoder.encode(category, "UTF-8"));
            }

            String bodyStr = body.toString();
            result = this.sendMessage(Constants.XmPushRequestPath.V2_UNSUBSCRIBE_TOPIC_BY_ALIAS, bodyStr);
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
            throw new IOException("Could not unsubscribe topic after " + attempt + " attempts");
        } else {
            return result;
        }
    }

    protected Result topicSubscribeBase(List<String> regIds, String topic, String category, int retries, boolean isSubscribe) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;
        boolean tryAgain = false;
        String regIdsString = joinString(regIds, ',');
        String type = isSubscribe?"subscribe":"unsubscribe";

        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to send " + type + " topic " + topic + " to regIds " + regIdsString);
            }

            StringBuilder body = newBody("registration_id", URLEncoder.encode(regIdsString, "UTF-8"));
            addParameter(body, "topic", URLEncoder.encode(topic, "UTF-8"));
            if(category != null) {
                addParameter(body, "category", URLEncoder.encode(category, "UTF-8"));
            }

            String bodyStr = body.toString();
            if(bodyStr.charAt(0) == 38) {
                bodyStr = body.toString().substring(1);
            }

            Constants.XmPushRequestPath requestPath = isSubscribe? Constants.XmPushRequestPath.V2_SUBSCRIBE_TOPIC: Constants.XmPushRequestPath.V2_UNSUBSCRIBE_TOPIC;
            result = this.sendMessage(requestPath, bodyStr);
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
            throw new IOException("Could not " + type + " topic after " + attempt + " attempts");
        } else {
            return result;
        }
    }

    protected Result topicSubscribeBase(List<String> regIds, String topic, String packageName, String category, int retries, boolean isSubscribe) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;
        boolean tryAgain = false;
        String regIdsString = joinString(regIds, ',');
        String type = isSubscribe?"subscribe":"unsubscribe";

        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to send " + type + " topic " + topic + " to regIds " + regIdsString);
            }

            StringBuilder body = newBody("registration_id", URLEncoder.encode(regIdsString, "UTF-8"));
            if(!XMStringUtils.isBlank(packageName)) {
                addParameter(body, "restricted_package_name", URLEncoder.encode(packageName, "UTF-8"));
            }

            addParameter(body, "topic", URLEncoder.encode(topic, "UTF-8"));
            if(category != null) {
                addParameter(body, "category", URLEncoder.encode(category, "UTF-8"));
            }

            String bodyStr = body.toString();
            if(bodyStr.charAt(0) == 38) {
                bodyStr = body.toString().substring(1);
            }

            Constants.XmPushRequestPath requestPath = isSubscribe? Constants.XmPushRequestPath.V2_SUBSCRIBE_TOPIC: Constants.XmPushRequestPath.V2_UNSUBSCRIBE_TOPIC;
            result = this.sendMessage(requestPath, bodyStr);
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
            throw new IOException("Could not " + type + " topic after " + attempt + " attempts");
        } else {
            return result;
        }
    }

    protected Result sendMessage(Constants.RequestPath requestPath, String message) throws IOException, ParseException {
        HttpURLConnection conn;
        int status;
        try {
            logger.fine("post to: " + requestPath.getPath());
            conn = this.doPost(requestPath, message);
            status = conn.getResponseCode();
        } catch (IOException var11) {
            logger.log(Level.WARNING, "IOException send message: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var11);
            return null;
        }

        if(status / 100 == 5) {
            logger.fine("XmPush service is unavailable (status " + status + "): remote server " + this.remoteHost + "(" + this.remoteIp + ")");
            return null;
        } else {
            String responseBody;
            if(status != 200) {
                try {
                    responseBody = getAndClose(conn.getErrorStream());
                    logger.finest("Plain post error response: " + responseBody);
                } catch (IOException var8) {
                    responseBody = "N/A";
                    logger.log(Level.FINE, "Exception reading response: ", var8);
                }

                throw new InvalidRequestException(status, responseBody);
            } else {
                try {
                    responseBody = getAndClose(conn.getInputStream());
                } catch (IOException var10) {
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var10);
                    return null;
                }

                try {
                    JSONParser e = new JSONParser();
                    JSONObject json = (JSONObject)e.parse(responseBody);
                    return (new Result.Builder()).fromJson(json);
                } catch (ParseException var9) {
                    logger.log(Level.WARNING, "Exception parsing response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var9);
                    throw new IOException("Invalid response from XmPush: " + responseBody);
                }
            }
        }
    }
}

