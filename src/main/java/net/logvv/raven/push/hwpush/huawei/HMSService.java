package net.logvv.raven.push.hwpush.huawei;

import net.logvv.raven.cache.CacheService;
import net.logvv.raven.common.constant.CacheKeyPrefixConst;
import net.logvv.raven.push.hwpush.huawei.common.AccessToken;
import net.logvv.raven.push.hwpush.huawei.common.NSPException;
import net.logvv.raven.push.hwpush.huawei.nsp.NSPClient;
import net.logvv.raven.push.hwpush.huawei.nsp.OAuth2Client;
import net.logvv.raven.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Author: wangwei
 * Created on 2017/1/12 20:39.
 */
@Service
public class HMSService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HMSService.class);

    @Value("${huawei.android.appid}")
    private String appId;
    @Value("${huawei.android.appsecret}")
    private String appKey;

    @Autowired
    private CacheService cacheService;

    /** 申请accessToken */
    public AccessToken applyHMSAccessToken()
    {
        /*
         * appId为开发者联盟上面创建应用的APP ID appKey为开发者联盟上面创建应用的 APP SECRET
         * APP ID：appid100  应用包名：com.open.test | APP SECRET：xxxxdtsb4abxxxlz2uyztxxxfaxxxxxx
         */
        try {
            OAuth2Client oauth2Client = new OAuth2Client();
            System.out.println(HMSService.class.getResource("/httpskeys/mykeystorebj.jks"));
            oauth2Client.initKeyStoreStream(HMSService.class.getResource("/httpskeys/mykeystorebj.jks").openStream(), "123456");

            AccessToken accessToken = oauth2Client.getAccessToken("client_credentials", appId, appKey);

            // 缓存accessToken到redis
            cacheService.cacheHuaweiAK(accessToken, CacheKeyPrefixConst.HW_ACCESS_TOKEN);

            return accessToken;

        }catch (Exception e){
            LOGGER.error("failed to apply access token from hua wei server.");
            return null;
        }
    }

    /** 获取可用的华为 accessToken */
    public String findAvailableToken()
    {
        AccessToken ak = cacheService.getHuaweiAK();
        if(null == ak){
            ak = applyHMSAccessToken();
        }
        if(null == ak){
            LOGGER.error("no available token found. please check expires time or redis");
            return "";
        }
        return ak.getAccess_token();
    }

    /** 初始化 HMS连接请求*/
    public NSPClient initClient()
    {
        String accessToken = findAvailableToken();
        try{
            NSPClient client = new NSPClient(accessToken);
            client.initHttpConnections(30, 50);//设置每个路由的连接数和最大连接数
            client.initKeyStoreStream(HMSService.class.getResource("/httpskeys/mykeystorebj.jks").openStream(), "123456");

            return client;

        }catch (Exception e){
            LOGGER.error("cannot initialize NSPClient, error:{}",e);
            return null;
        }
    }

}
