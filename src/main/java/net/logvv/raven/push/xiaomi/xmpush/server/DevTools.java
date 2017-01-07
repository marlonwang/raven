package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;

public class DevTools extends HttpBase {
    private static final String REG_ID_SPLITTER = ",";

    public DevTools(String security) {
        super(security);
    }

    public String getAliasesOf(String packageName, String regId, int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to get all aliases of the device.");
            }

            result = this.getAliasesNoRetry(packageName, regId);
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

    public String getTopicsOf(String packageName, String regId, int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to get all topics of the device.");
            }

            result = this.getTopicsNoRetry(packageName, regId);
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

    public String getAccountsOf(String packageName, String regId, int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to get all user account of the device.");
            }

            result = this.getAccountsNoRetry(packageName, regId);
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

    public String getPresence(String packageName, String regId, int retries) throws IOException {
        int attempt = 0;
        int backoff = 1000;

        String result;
        boolean tryAgain;
        do {
            ++attempt;
            if(logger.isLoggable(Level.FINE)) {
                logger.fine("Attempt #" + attempt + " to get presence of the device.");
            }

            result = this.getPresenceNoRetry(packageName, regId);
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

    public String getPresence(String packageName, List<String> regIds, int retries) throws IOException {
        StringBuilder sb = new StringBuilder((String)regIds.get(0));

        for(int i = 1; i < regIds.size(); ++i) {
            sb.append(",").append((String)regIds.get(i));
        }

        return this.getPresence(packageName, sb.toString(), retries);
    }

    protected String getAliasesNoRetry(String packageName, String regId) throws InvalidRequestException {
        HttpURLConnection conn;
        int status;
        try {
            StringBuilder responseBody = newBody("restricted_package_name", URLEncoder.encode(packageName, "UTF-8"));
            addParameter(responseBody, "registration_id", URLEncoder.encode(regId, "UTF-8"));
            logger.fine("get from: " + Constants.XmPushRequestPath.V1_GET_ALL_ALIAS);
            conn = this.doGet(Constants.XmPushRequestPath.V1_GET_ALL_ALIAS, responseBody.toString());
            status = conn.getResponseCode();
        } catch (IOException var9) {
            logger.log(Level.WARNING, "IOException while get alias: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var9);
            this.lastException = var9;
            return null;
        }

        if(status / 100 == 5) {
            logger.fine("XmPush service is unavailable (status " + status + ")");
            return null;
        } else {
            String responseBody1;
            if(status != 200) {
                try {
                    responseBody1 = getAndClose(conn.getErrorStream());
                    logger.finest("Plain get error response: " + responseBody1);
                } catch (IOException var7) {
                    responseBody1 = "N/A";
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var7);
                }

                throw new InvalidRequestException(status, responseBody1);
            } else {
                try {
                    responseBody1 = getAndClose(conn.getInputStream());
                    return responseBody1;
                } catch (IOException var8) {
                    this.lastException = var8;
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var8);
                    return null;
                }
            }
        }
    }

    protected String getTopicsNoRetry(String packageName, String regId) throws InvalidRequestException {
        HttpURLConnection conn;
        int status;
        try {
            StringBuilder responseBody = newBody("restricted_package_name", URLEncoder.encode(packageName, "UTF-8"));
            addParameter(responseBody, "registration_id", URLEncoder.encode(regId, "UTF-8"));
            conn = this.doGet(Constants.XmPushRequestPath.V1_GET_ALL_TOPIC, responseBody.toString());
            status = conn.getResponseCode();
        } catch (IOException var9) {
            this.lastException = var9;
            logger.log(Level.WARNING, "IOException while get topic: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var9);
            return null;
        }

        if(status / 100 == 5) {
            logger.fine("Service is unavailable (status " + status + ") remote server " + this.remoteHost + "(" + this.remoteIp + ")");
            return null;
        } else {
            String responseBody1;
            if(status != 200) {
                try {
                    responseBody1 = getAndClose(conn.getErrorStream());
                    logger.finest("Plain get error response: " + responseBody1);
                } catch (IOException var7) {
                    responseBody1 = "N/A";
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var7);
                }

                throw new InvalidRequestException(status, responseBody1);
            } else {
                try {
                    responseBody1 = getAndClose(conn.getInputStream());
                    return responseBody1;
                } catch (IOException var8) {
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var8);
                    return null;
                }
            }
        }
    }

    protected String getAccountsNoRetry(String packageName, String regId) throws InvalidRequestException {
        HttpURLConnection conn;
        int status;
        try {
            StringBuilder responseBody = newBody("restricted_package_name", URLEncoder.encode(packageName, "UTF-8"));
            addParameter(responseBody, "registration_id", URLEncoder.encode(regId, "UTF-8"));
            conn = this.doGet(Constants.XmPushRequestPath.V1_GET_ALL_ACCOUNT, responseBody.toString());
            status = conn.getResponseCode();
        } catch (IOException var9) {
            logger.log(Level.WARNING, "IOException while get user-account: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var9);
            this.lastException = var9;
            return null;
        }

        if(status / 100 == 5) {
            logger.fine("Service is unavailable (status " + status + "): remote server " + this.remoteHost + "(" + this.remoteIp + ")");
            return null;
        } else {
            String responseBody1;
            if(status != 200) {
                try {
                    responseBody1 = getAndClose(conn.getErrorStream());
                    logger.finest("Plain get error response: " + responseBody1);
                } catch (IOException var7) {
                    responseBody1 = "N/A";
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var7);
                }

                throw new InvalidRequestException(status, responseBody1);
            } else {
                try {
                    responseBody1 = getAndClose(conn.getInputStream());
                    return responseBody1;
                } catch (IOException var8) {
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var8);
                    return null;
                }
            }
        }
    }

    protected String getPresenceNoRetry(String packageName, String regId) throws InvalidRequestException {
        HttpURLConnection conn;
        int status;
        try {
            StringBuilder responseBody = newBody("restricted_package_name", URLEncoder.encode(packageName, "UTF-8"));
            addParameter(responseBody, "registration_id", URLEncoder.encode(regId, "UTF-8"));
            if(regId.contains(",")) {
                conn = this.doGet(Constants.XmPushRequestPath.V2_REGID_PRESENCE, responseBody.toString());
            } else {
                conn = this.doGet(Constants.XmPushRequestPath.V1_REGID_PRESENCE, responseBody.toString());
            }

            status = conn.getResponseCode();
        } catch (IOException var9) {
            logger.log(Level.WARNING, "IOException while get presence: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var9);
            return null;
        }

        if(status / 100 == 5) {
            logger.fine("Service is unavailable (status " + status + "): remote server " + this.remoteHost + "(" + this.remoteIp + ")");
            return null;
        } else {
            String responseBody1;
            if(status != 200) {
                try {
                    responseBody1 = getAndClose(conn.getErrorStream());
                    logger.finest("Plain get error response: " + responseBody1);
                } catch (IOException var7) {
                    responseBody1 = "N/A";
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var7);
                }

                throw new InvalidRequestException(status, responseBody1);
            } else {
                try {
                    responseBody1 = getAndClose(conn.getInputStream());
                    return responseBody1;
                } catch (IOException var8) {
                    logger.log(Level.WARNING, "Exception reading response: remote server " + this.remoteHost + "(" + this.remoteIp + ")", var8);
                    return null;
                }
            }
        }
    }
}

