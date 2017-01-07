package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tracer extends HttpBase {
    protected static final Logger logger = Logger.getLogger(Tracer.class.getName());

    public Tracer(String security) {
        super((String)nonNull(security));
    }

    public String getMessageGroupStatus(String jobKey, int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to get status of message group " + jobKey);
            }

            result = this.getMessageGroupStatusNoRetry(jobKey);
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

    public String getMessageStatus(String msgId, int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to get status of message " + msgId);
            }

            result = this.getMessageStatusNoRetry(msgId);
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

    public String getMessageStatus(long beginTime, long endTime, int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to get messages status between " + beginTime + " and " + endTime);
            }

            result = this.getMessageStatusNoRetry(beginTime, endTime);
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

    public String getMessageGroupStatusNoRetry(String jobKey) throws UnsupportedEncodingException, InvalidRequestException {
        StringBuilder parameter = newBody("job_key", URLEncoder.encode(jobKey, "UTF-8"));
        String parameterString = parameter.toString();

        HttpURLConnection conn;
        int status;
        try {
            logger.fine("get from: " + Constants.XmPushRequestPath.V1_MESSAGE_STATUS);
            conn = this.doGet(Constants.XmPushRequestPath.V1_MESSAGE_STATUS, parameterString);
            status = conn.getResponseCode();
        } catch (IOException var10) {
            this.lastException = var10;
            logger.log(Level.FINE, "IOException while get from XmPush: server " + this.remoteHost + " ip " + this.remoteIp, var10);
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
                    logger.finest("Plain get error response: " + responseBody);
                } catch (IOException var8) {
                    responseBody = "N/A";
                    logger.log(Level.FINE, "Exception reading response: ", var8);
                }

                throw new InvalidRequestException(status, responseBody);
            } else {
                try {
                    responseBody = getAndClose(conn.getInputStream());
                    return responseBody;
                } catch (IOException var9) {
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var9);
                    return null;
                }
            }
        }
    }

    public String getMessageStatusNoRetry(String msgId) throws UnsupportedEncodingException, InvalidRequestException {
        StringBuilder parameter = newBody("msg_id", URLEncoder.encode(msgId, "UTF-8"));
        String parameterString = parameter.toString();

        HttpURLConnection conn;
        int status;
        try {
            logger.fine("get from: " + Constants.XmPushRequestPath.V1_MESSAGE_STATUS);
            conn = this.doGet(Constants.XmPushRequestPath.V1_MESSAGE_STATUS, parameterString);
            status = conn.getResponseCode();
        } catch (IOException var10) {
            this.lastException = var10;
            logger.log(Level.FINE, "IOException while get from XmPush: server " + this.remoteHost + " ip " + this.remoteIp, var10);
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
                    logger.finest("Plain get error response: " + responseBody);
                } catch (IOException var8) {
                    responseBody = "N/A";
                    this.lastException = var8;
                    logger.log(Level.FINE, "Exception reading response: ", var8);
                }

                throw new InvalidRequestException(status, responseBody);
            } else {
                try {
                    responseBody = getAndClose(conn.getInputStream());
                    return responseBody;
                } catch (IOException var9) {
                    logger.log(Level.WARNING, "Exception reading response: ", var9);
                    return null;
                }
            }
        }
    }

    public String getMessageStatusNoRetry(long beginTime, long endTime) throws UnsupportedEncodingException, InvalidRequestException {
        StringBuilder parameter = newBody("begin_time", URLEncoder.encode("" + beginTime, "UTF-8"));
        addParameter(parameter, "end_time", URLEncoder.encode("" + endTime, "UTF-8"));
        String parameterString = parameter.toString();

        HttpURLConnection conn;
        int status;
        try {
            logger.fine("get from: " + Constants.XmPushRequestPath.V1_MESSAGES_STATUS);
            conn = this.doGet(Constants.XmPushRequestPath.V1_MESSAGES_STATUS, parameterString);
            status = conn.getResponseCode();
        } catch (IOException var13) {
            this.lastException = var13;
            logger.log(Level.FINE, "IOException while get from XmPush: server " + this.remoteHost + " ip " + this.remoteIp, var13);
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
                    logger.finest("Plain get error response: " + responseBody);
                } catch (IOException var11) {
                    responseBody = "N/A";
                    logger.log(Level.FINE, "Exception reading response: ", var11);
                }

                throw new InvalidRequestException(status, responseBody);
            } else {
                try {
                    responseBody = getAndClose(conn.getInputStream());
                    return responseBody;
                } catch (IOException var12) {
                    logger.log(Level.WARNING, "Exception reading response: ", var12);
                    return null;
                }
            }
        }
    }
}

