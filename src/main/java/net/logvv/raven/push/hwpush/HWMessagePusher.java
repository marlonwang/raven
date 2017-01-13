package net.logvv.raven.push.hwpush;

import net.logvv.raven.common.constant.DateConstants;
import net.logvv.raven.push.MessagePusher;
import net.logvv.raven.push.hwpush.huawei.HMSService;
import net.logvv.raven.push.hwpush.huawei.PushRet;
import net.logvv.raven.push.hwpush.huawei.common.AccessToken;
import net.logvv.raven.push.hwpush.huawei.nsp.NSPClient;
import net.logvv.raven.push.model.PushMessage;
import net.logvv.raven.utils.JsonUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

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

    /**
     * 安卓单发透传及时消息
     * @param pushMessage
     * @return
     */
    private boolean singleMessage(PushMessage pushMessage)
    {
        boolean success;

        NSPClient client = hmsService.initClient();
        long currentTime = System.currentTimeMillis();
        String deviceToken = pushMessage.getAudiences().get(0);

        SimpleDateFormat dataFormat = new SimpleDateFormat(DateConstants.DEFAULT_DATE_FORMAT);
        // unix时间戳，可选格式：2013-08-29 19:55
        // 消息过期删除时间,如果不填写，默认超时时间为当前时间后48小时
        String expire_time = dataFormat.format(currentTime + 24 * 60 * 60 * 1000);

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
        // 下发不成功的情况下有一次重发的机会，重发后不再次重发deviceToken和msgType相同的缓存消息为同一类缓存
        hashMap.put("msgType", RandomUtils.nextInt(1,100));
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
            LOGGER.error("send single massage error:{}",e);
            return false;
        }

        return success;
    }

    /**
     * 单发通知栏及时消息
     * @param pushMessage
     * @return
     */
    private boolean singleNotification(PushMessage pushMessage)
    {
        boolean success = false;

        NSPClient client = hmsService.initClient();
        long currentTime = System.currentTimeMillis();
        String deviceToken = pushMessage.getAudiences().get(0);

        SimpleDateFormat dataFormat = new SimpleDateFormat(DateConstants.DEFAULT_DATE_FORMAT);
        String expire_time = dataFormat.format(currentTime + 24 * 60 * 60 * 1000);

        // 发送到设备上的消息，必选
        // 最长为4096 字节（开发者自定义，自解析）
        String android = "{\"notification_title\":\""+ pushMessage.getTitle() +"\"," +
                "\"notification_content\":\""+ pushMessage.getText() +"\"}";

        // 构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("deviceToken", deviceToken);
        hashMap.put("android", android);
        hashMap.put("cacheMode", 1);
        hashMap.put("msgType", RandomUtils.nextInt(1,100));
        // 可选 0: 当前用户  1: 主要用户 -1: 默认用户
        hashMap.put("userType", "1");
        hashMap.put("expire_time", expire_time);

        client.setTimeout(10000, 15000);
        try{
            PushRet resp = client.call("openpush.message.psSingleSend", hashMap, PushRet.class);
            success = ("success".equalsIgnoreCase(resp.getMessage()));
            LOGGER.info("result from HMS:{}", JsonUtils.obj2json(resp));
        }catch (Exception e){
            LOGGER.error("send single notification error:{}",e);
            return false;
        }

        return success;
    }

    /**
     * 群发透传消息
     * 最多1000个device_token
     * @param pushMessage
     * @return
     */
    private boolean batchMessage(PushMessage pushMessage)
    {
        boolean success = false;

        NSPClient client = hmsService.initClient();
        long currentTime = System.currentTimeMillis();
        List<String> deviceTokenList = pushMessage.getAudiences();

        SimpleDateFormat dataFormat = new SimpleDateFormat(DateConstants.DEFAULT_DATE_FORMAT);
        String expire_time = dataFormat.format(currentTime + 24 * 60 * 60 * 1000);

        // 构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("deviceTokenList", deviceTokenList);
        hashMap.put("message", pushMessage.getText());
        hashMap.put("cacheMode", 1);
        hashMap.put("msgType", RandomUtils.nextInt(1,100));
        hashMap.put("expire_time", expire_time);
        //设置http超时时间
        client.setTimeout(10000, 15000);

        try{
            PushRet resp = client.call("openpush.message.batch_send", hashMap, PushRet.class);
            success = ("success".equalsIgnoreCase(resp.getMessage()));
            LOGGER.info("result from HMS:{}", JsonUtils.obj2json(resp));
        }catch (Exception e){
            LOGGER.error("batch send message error:{}",e);
            return false;
        }

        return success;
    }

    /**
     * 群发通知栏及时消息
     * 最多1000个device_token
     * @param pushMessage
     * @return
     */
    private boolean batchNotification(PushMessage pushMessage)
    {
        boolean success = false;

        NSPClient client = hmsService.initClient();
        long currentTime = System.currentTimeMillis();
        List<String> deviceTokenList = pushMessage.getAudiences();

        SimpleDateFormat dataFormat = new SimpleDateFormat(DateConstants.DEFAULT_DATE_FORMAT);
        String expire_time = dataFormat.format(currentTime + 24 * 60 * 60 * 1000);

        // 发送到设备上的消息，必选
        // 最长为4096 字节（开发者自定义，自解析）
        String android = "{\"notification_title\":\""+ pushMessage.getTitle() +"\"," +
                "\"notification_content\":\""+ pushMessage.getText() +"\"}";

        // 构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("deviceTokenList", deviceTokenList);
        hashMap.put("android", android);
        hashMap.put("cacheMode", 1);
        hashMap.put("msgType", RandomUtils.nextInt(1,100));
        // 可选 0: 当前用户  1: 主要用户 -1: 默认用户
        hashMap.put("userType", "1");
        hashMap.put("expire_time", expire_time);

        client.setTimeout(10000, 15000);
        try{
            PushRet resp = client.call("openpush.message.psBatchSend", hashMap, PushRet.class);
            success = ("success".equalsIgnoreCase(resp.getMessage()));
            LOGGER.info("result from HMS:{}", JsonUtils.obj2json(resp));
        }catch (Exception e){
            LOGGER.error("send batch notification error:{}",e);
            return false;
        }

        return success;
    }

    private boolean androidUnicast(PushMessage pushMessage)
    {
        boolean success = false;
        // 区分透传消息和通知栏消息
        switch (pushMessage.getDisplayType())
        {
            case MESSAGE:
                success = singleMessage(pushMessage);
                break;
            case NOTIFICATION:
                success = singleNotification(pushMessage);
                break;
            default:
                break;
        }
        return success;
    }

    private boolean androidListcast(PushMessage pushMessage)
    {
        boolean success = false;
        // 区分透传消息和通知栏消息
        switch (pushMessage.getDisplayType())
        {
            case MESSAGE:
                success = batchMessage(pushMessage);
                break;
            case NOTIFICATION:
                success = batchNotification(pushMessage);
                break;
            default:
                break;
        }
        return success;
    }

    /***
     * 按标签或者别名推送
     * @param pushMessage
     * @return
     */
    private boolean androidAliascast(PushMessage pushMessage)
    {
        boolean success = false;

        NSPClient client = hmsService.initClient();
        long currentTime = System.currentTimeMillis();
        String deviceToken = pushMessage.list2Str(pushMessage.getAudiences());

        SimpleDateFormat dataFormat = new SimpleDateFormat(DateConstants.DEFAULT_DATE_FORMAT);
        String expire_time = dataFormat.format(currentTime + 24 * 60 * 60 * 1000);

        // 发送到设备上的消息，必选
        // 最长为4096 字节（开发者自定义，自解析）
        String android = "{\"notification_title\":\""+ pushMessage.getTitle() +"\"," +
                "\"notification_content\":\""+ pushMessage.getText() +"\"}";

        //推送范围，必选
        //1：指定用户，必须指定tokens字段
        //2：所有人，无需指定tokens，tags，exclude_tags
        //3：一群人，必须指定tags或者exclude_tags字段
        int pushType = 1;
        if(PushMessage.PushType.ALIASCAST == pushMessage.getPushType() ||
                PushMessage.PushType.TAGCAST == pushMessage.getPushType())
        {
            pushType = 3;
        }

        // 构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("push_type", pushType);
        hashMap.put("tokens", deviceToken);
        hashMap.put("android", android);
        hashMap.put("expire_time", expire_time);

        if(3 == pushType)
        {
            //标签，可选, {"tags":[{"location":["ShangHai","GuangZhou"]},{"age":["20","30"]}]}
            //当push_type的取值为3时，该字段生效
            String tags = "{\"tags\":"+ pushMessage.getTagsJson() +"}";

            //排除的标签，可选, {"exclude_tags":[{"music":["blue"]},{"fruit":["apple"]}]}
            //当push_type的取值为2时，该字段生效
            String exclude_tags = "{\"exclude_tags\":"+ pushMessage.getExcludeTagsJson() +"}";
            hashMap.put("tags", tags);
            hashMap.put("exclude_tags", exclude_tags);
        }

        client.setTimeout(10000, 15000);
        try{
            PushRet resp = client.call("openpush.openapi.notification_send", hashMap, String.class);
            success = (0 == resp.getResult_code() || "success".equalsIgnoreCase(resp.getMessage()));

            LOGGER.info("result from HMS:{}", JsonUtils.obj2json(resp));
        }catch (Exception e){
            LOGGER.error("send tag/alias notification error:{}",e);
            return false;
        }
        return success;
    }

    @Override
    public boolean pushIosMessage(PushMessage pushMessage)
    {
        return false;
    }

    @Override
    public boolean pushAllMessage(PushMessage pushMessage)
    {
        boolean success = false;

        NSPClient client = hmsService.initClient();
        long currentTime = System.currentTimeMillis();
        String deviceToken = pushMessage.list2Str(pushMessage.getAudiences());

        SimpleDateFormat dataFormat = new SimpleDateFormat(DateConstants.DEFAULT_DATE_FORMAT);
        String expire_time = dataFormat.format(currentTime + 24 * 60 * 60 * 1000);

        // 发送到设备上的消息，必选
        // 最长为4096 字节（开发者自定义，自解析）
        String android = "{\"notification_title\":\""+ pushMessage.getTitle() +"\"," +
                "\"notification_content\":\""+ pushMessage.getText() +"\"}";

        // 构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        //推送范围，必选
        //1：指定用户，必须指定tokens字段
        //2：所有人，无需指定tokens，tags，exclude_tags
        //3：一群人，必须指定tags或者exclude_tags字段
        hashMap.put("push_type", 2);
        hashMap.put("android", android);
        hashMap.put("expire_time", expire_time);

        client.setTimeout(10000, 15000);
        try{
            PushRet resp = client.call("openpush.openapi.notification_send", hashMap, String.class);
            success = (0 == resp.getResult_code() || "success".equalsIgnoreCase(resp.getMessage()));

            LOGGER.info("result from HMS:{}", JsonUtils.obj2json(resp));
        }catch (Exception e){
            LOGGER.error("send tag/alias notification error:{}",e);
            return false;
        }
        return success;
    }

    // ios push method
    // 无需支持
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
