package net.logvv.raven.push.hwpush;

import net.logvv.raven.common.constant.DateConstants;
import net.logvv.raven.push.MessagePusher;
import net.logvv.raven.push.hwpush.huawei.HMSService;
import net.logvv.raven.push.hwpush.huawei.PushRet;
import net.logvv.raven.push.hwpush.huawei.nsp.NSPClient;
import net.logvv.raven.push.model.PushMessage;
import net.logvv.raven.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Author: wangwei
 * Created on 2016/12/3 16:44.
 */
public class HWMessagePusher implements MessagePusher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HWMessagePusher.class);
    private static final int RETRY_TIMES = 3;

    @Value("${huawei.android.appid}")
    private String appId;
    @Value("${huawei.android.appsecret}")
    private String appKey;

    @Autowired
    private HMSService hmsService;

    @Override
    public boolean pushAndroidMessage(PushMessage pushMessage)
    {
        boolean success = false;
        int count = 0;
        while (!success && count < RETRY_TIMES)
        {
            switch (pushMessage.getPushType())
            {
                case UNICAST:
                    success = androidUnicast(pushMessage);
                    break;
                case LISTCAST:
                    success = androidListcast(pushMessage);
                    break;
                case ALIASCAST:
                    success = androidAliascast(pushMessage);
                    break;
                default:
                    LOGGER.error("unsupported push type:{} ", pushMessage.getPushType());
                    break;
            }

            ++count;
        }
        return success;
    }

    // android push method
    private boolean androidUnicast(PushMessage pushMessage)
    {
        boolean success = false;

        NSPClient client = hmsService.initClient();
        long currentTime = System.currentTimeMillis();
        String deviceToken = pushMessage.getAudiences().get(0);

        SimpleDateFormat dataFormat = new SimpleDateFormat(DateConstants.DEFAULT_DATE_FORMAT);
        //unix时间戳，可选格式：2013-08-29 19:55
        // 消息过期删除时间,如果不填写，默认超时时间为当前时间后48小时
        String expire_time = dataFormat.format(currentTime + 5 * 60 * 60 * 1000);

        //构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("deviceToken", deviceToken);
        hashMap.put("message", pushMessage.getText());
        //必选 0：高优先级 1：普通优先级 缺省值为1
        hashMap.put("priority", 1);
        //消息是否需要缓存，必选 0：不缓存 1：缓存 缺省值为0
        hashMap.put("cacheMode", 1);
        //标识消息类型（缓存机制），必选
        //由调用端赋值，取值范围（1~100）。当TMID+msgType的值一样时，仅缓存最新的一条消息
        hashMap.put("msgType", 1);
        hashMap.put("expire_time", expire_time);
        //可选
        //如果请求消息中，没有带，则MC根据ProviderID+timestamp生成，各个字段之间用下划线连接
        // hashMap.put("requestID", "14842861397221040851");

        //设置http超时时间
        client.setTimeout(10000, 15000);

        try{
            PushRet resp = client.call("openpush.message.single_send", hashMap, PushRet.class);
            success = ("success".equalsIgnoreCase(resp.getMessage()));
            LOGGER.info("result from HMS:{}", JsonUtils.obj2json(resp));
        }catch (Exception e){
            return false;
        }

        return success;
    }

    private boolean androidListcast(PushMessage pushMessage){

        return false;
    }

    private boolean androidAliascast(PushMessage pushMessage){
        return false;
    }

    @Override
    public boolean pushIosMessage(PushMessage pushMessage)
    {
        return false;
    }

    @Override
    public boolean pushAllMessage(PushMessage pushMessage)
    {
        return false;
    }

    // ios push method
    private boolean iosUnicast(PushMessage pushMessage){
        return false;
    }

    private boolean iosListcast(PushMessage pushMessage){
        return false;
    }

    private boolean iosAliascast(PushMessage pushMessage){
        return false;
    }
}
