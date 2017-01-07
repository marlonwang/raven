package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;

public class Feedback extends HttpBase {
    public Feedback(String security) {
        super((String)nonNull(security));
    }

    public String getInvalidRegIds(int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to get invalid registration ids");
            }

            result = this.getInvalidRegIdsNoRetry();
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

    public String getInvalidAliases(int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to get invalid aliases");
            }

            result = this.getInvalidAliasesNoRetry();
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

    protected String getInvalidRegIdsNoRetry() throws InvalidRequestException {
        HttpURLConnection conn;
        int status;
        try {
            logger.fine("get from: " + Constants.XmPushRequestPath.V1_FEEDBACK_INVALID_REGID);
            conn = this.doGet(Constants.XmPushRequestPath.V1_FEEDBACK_INVALID_REGID, "");
            status = conn.getResponseCode();
        } catch (IOException var7) {
            logger.log(Level.WARNING, "IOException while get invalid regid: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var7);
            this.lastException = var7;
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
                    logger.finest("Plain get error response: " + responseBody);
                } catch (IOException var5) {
                    responseBody = "N/A";
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var5);
                }

                throw new InvalidRequestException(status, responseBody);
            } else {
                try {
                    responseBody = getAndClose(conn.getInputStream());
                    return responseBody;
                } catch (IOException var6) {
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var6);
                    return null;
                }
            }
        }
    }

    protected String getInvalidAliasesNoRetry() throws InvalidRequestException {
        HttpURLConnection conn;
        int status;
        try {
            logger.fine("get from: " + Constants.XmPushRequestPath.V1_FEEDBACK_INVALID_ALIAS);
            conn = this.doGet(Constants.XmPushRequestPath.V1_FEEDBACK_INVALID_ALIAS, "");
            status = conn.getResponseCode();
        } catch (IOException var7) {
            logger.log(Level.WARNING, "IOException while get invalid aliases: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var7);
            this.lastException = var7;
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
                } catch (IOException var5) {
                    responseBody = "N/A";
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var5);
                }

                throw new InvalidRequestException(status, responseBody);
            } else {
                try {
                    responseBody = getAndClose(conn.getInputStream());
                    return responseBody;
                } catch (IOException var6) {
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var6);
                    return null;
                }
            }
        }
    }
}

