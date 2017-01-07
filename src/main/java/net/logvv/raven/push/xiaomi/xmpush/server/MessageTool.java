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
import java.util.logging.Level;

public class MessageTool extends HttpBase {
    public MessageTool(String security) {
        super((String)nonNull(security));
    }

    public Result deleteTopic(String jobId, int retries) throws IOException, ParseException {
        StringBuilder body = newBody("id", URLEncoder.encode(jobId, "UTF-8"));
        return this.sendMessage(Constants.XmPushRequestPath.V2_DELETE_BROADCAST_MESSAGE, body, retries);
    }

    public Result deleteTopic(String jobId) throws IOException, ParseException {
        return this.deleteTopic(jobId, 1);
    }

    protected Result sendMessage(Constants.RequestPath requestPath, StringBuilder body, int retries) throws IOException, ParseException {
        int attempt = 0;
        Result result = null;
        int backoff = 1000;
        boolean tryAgain = false;

        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to send " + body + " to url " + requestPath.getPath());
            }

            String bodyStr = body.toString();
            if(bodyStr.charAt(0) == 38) {
                bodyStr = body.toString().substring(1);
            }

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
            throw this.exception(attempt);
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
            this.lastException = var11;
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
                    this.lastException = var8;
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var8);
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

