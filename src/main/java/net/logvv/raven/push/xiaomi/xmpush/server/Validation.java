package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Validation extends HttpBase {
    protected static final Logger logger = Logger.getLogger(Validation.class.getName());

    public Validation(String security) {
        super(security);
    }

    public String validateRegistrationIds(List<String> regIds, int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to validate regids.");
            }

            result = this.validateRegistrationIdsNoRetry(regIds);
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

    public String validateRegistrationIdsNoRetry(List<String> regIds) throws UnsupportedEncodingException, InvalidRequestException {
        StringBuilder parameter = newBodyWithArrayParameters("registration_ids", regIds);
        String parameterString = parameter.toString();

        HttpURLConnection conn;
        int status;
        try {
            logger.fine("get from: " + Constants.XmPushRequestPath.V1_VALIDATE_REGID);
            conn = this.doPost(Constants.XmPushRequestPath.V1_VALIDATE_REGID, parameterString);
            status = conn.getResponseCode();
        } catch (IOException var10) {
            logger.log(Level.WARNING, "IOException while validating registration ids: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var10);
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
}
