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

public class Stats extends HttpBase {
    protected static final Logger logger = Logger.getLogger(Stats.class.getName());

    public Stats(String security) {
        super((String)nonNull(security));
    }

    public String getStats(String startDate, String endDate, String packageName, int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to get realtime stats data between " + startDate + " and " + endDate);
            }

            result = this.getStatsNoRetry(startDate, endDate, packageName);
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

    protected String getStatsNoRetry(String startDate, String endDate, String packageName) throws UnsupportedEncodingException, InvalidRequestException {
        StringBuilder parameter = newBody("start_date", URLEncoder.encode(startDate, "UTF-8"));
        addParameter(parameter, "end_date", URLEncoder.encode(endDate, "UTF-8"));
        addParameter(parameter, "restricted_package_name", URLEncoder.encode(packageName, "UTF-8"));
        String parameterString = parameter.toString();

        HttpURLConnection conn;
        int status;
        try {
            logger.fine("get from: " + Constants.XmPushRequestPath.V1_GET_MESSAGE_COUNTERS);
            conn = this.doGet(Constants.XmPushRequestPath.V1_GET_MESSAGE_COUNTERS, parameterString);
            status = conn.getResponseCode();
        } catch (IOException var12) {
            logger.log(Level.FINE, "IOException while get from XmPush", var12);
            this.lastException = var12;
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
                } catch (IOException var10) {
                    responseBody = "N/A";
                    logger.log(Level.FINE, "Exception reading response: ", var10);
                }

                throw new InvalidRequestException(status, responseBody);
            } else {
                try {
                    responseBody = getAndClose(conn.getInputStream());
                    return responseBody;
                } catch (IOException var11) {
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var11);
                    return null;
                }
            }
        }
    }
}

