package net.logvv.raven.push.xiaomi;

import net.logvv.raven.push.MessagePusher;
import net.logvv.raven.push.xiaomi.xmpush.server.Message;
import net.logvv.raven.push.xiaomi.xmpush.server.Result;
import net.logvv.raven.push.xiaomi.xmpush.server.Sender;
import net.logvv.raven.push.model.PushMessage;
import net.logvv.raven.push.xiaomi.xmpush.server.Constants;
import net.logvv.raven.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

/**
 * mi push
 * Author: wangwei
 * Created on 2016/11/25 15:25.
 */
public class XMMessagePusher implements MessagePusher {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMMessagePusher.class);
    private static final int RETRY_TIMES = 3;

    @Value("${mipush.android.appkey}")
    private String androidAppkey;
    @Value("${mipush.android.secretkey}")
    private String androidSecretKey;

    @Value("${mipush.ios.appkey}")
    private String iosAppkey;
    @Value("${mipush.ios.secretkey}")
    private String iosSecretKey;

    @Value("${mipush.android.packageName}")
    private String androidPackageName;
    @Value("${mipush.ios.packageName}")
    private String iosPackageName;

    @Override
    public boolean pushIosMessage(PushMessage pushMessage)
    {
        int count = 0;
        boolean isSuccess = false;
        while(count < RETRY_TIMES)
        {
            switch (pushMessage.getPushType())
            {
                case BROADCAST:
                    isSuccess = iosBroadCast(pushMessage);
                    break;
                case UNICAST:
                    isSuccess = iosUnicast(pushMessage);
                    break;
                case LISTCAST:
                    isSuccess = iosListCast(pushMessage);
                    break;
                case ALIASCAST:
                    isSuccess = iosAliasCast(pushMessage);
                    break;
                default:
                    LOGGER.error("IOS Not support push type:{} ", pushMessage.getPushType());
                    return false;
            }
            count ++;

            if (isSuccess)
            {
                LOGGER.info("Send message, try {} times.",count);
                break;
            }
        }

        return isSuccess;
    }

    @Override
    public boolean pushAndroidMessage(PushMessage pushMessage)
    {
        int count = 0;
        boolean isSuccess = false;
        while(count < RETRY_TIMES)
        {
            switch (pushMessage.getPushType())
            {
                case BROADCAST:
                    isSuccess = androidBroadCast(pushMessage);
                    break;
                case UNICAST:
                    isSuccess = androidUnicast(pushMessage);
                    break;
                case LISTCAST:
                    isSuccess = androidListCast(pushMessage);
                    break;
                case ALIASCAST:
                    isSuccess = androidAliasCast(pushMessage);
                    break;
                default:
                    LOGGER.error("IOS Not support push type:{} ", pushMessage.getPushType());
                    return false;
            }
            count ++;

            if (isSuccess)
            {
                LOGGER.info("Send message, success:{}, try {} times.",isSuccess,count);
                break;
            }
        }

        return isSuccess;
    }

    // 根据设备regid推送 单个
    private boolean androidUnicast(PushMessage pushMessage)
    {
        try
        {
            // playLoad 要发送的消息内容,透传消息必填非透传消息可选
            Constants.useOfficial(); // 生产环境; 测试环境 Constants.useSandbox();
            Sender sender = new Sender(androidSecretKey);
            Message message = new Message.Builder()
                    .title(pushMessage.getTitle())
                    .description(pushMessage.getText())
                    .payload(pushMessage.getText())
                    .restrictedPackageName(androidPackageName)
                    .passThrough(0)    // 1 透传消息 0 通知栏消息
                    .notifyType(1)     // 使用默认提示音提示
                    .build();

            Result result = sender.send(message, pushMessage.getAudiences().get(0), 3); //根据regID，发送消息到指定设备上
            LOGGER.info("result from xiao mi:{}", JsonUtils.obj2json(result));
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to unicast, error:{}", e);
            return false;
        }
        return true;
    }

    // 根据设备regid推送 单个
    //发送消息到一组设备上, regids个数不得超过1000个
    private boolean androidListCast(PushMessage pushMessage)
    {
        try{
            Constants.useOfficial();
            Sender sender = new Sender(androidSecretKey);
            Message message = new Message.Builder()
                    .title(pushMessage.getTitle())
                    .description(pushMessage.getText())
                    .payload(pushMessage.getText())
                    .restrictedPackageName(androidPackageName)
                    .notifyType(1)     // 使用默认提示音提示
                    .passThrough(0)
                    .build();
            Result result = sender.send(message, pushMessage.getAudiences(), 3);
            LOGGER.info("result from xiao mi:{}", JsonUtils.obj2json(result));
        }catch (Exception e){
            LOGGER.error("Failed to unicast, error:{}", e);
            return false;
        }
        return true;
    }

    // 根据regid绑定的别名推送
    private boolean androidAliasCast(PushMessage pushMessage)
    {
        try{
            Constants.useOfficial();
            Sender sender = new Sender(androidSecretKey);
            Message message = new Message.Builder()
                    .title(pushMessage.getTitle())
                    .description(pushMessage.getText())
                    .payload(pushMessage.getText())
                    .restrictedPackageName(androidPackageName)
                    .notifyType(1)     // 使用默认提示音提示
                    .build();
            Result result = sender.sendToAlias(message, pushMessage.getAudiences(), 3); //根据aliasList, 发送消息到指定设备上
            LOGGER.info("result from xiao mi:{}", JsonUtils.obj2json(result));
            // 一次不能超过1000
        }catch (Exception e){
            LOGGER.error("Failed to alias cast, error:{}", e);
            return false;
        }

        return true;
    }

    // 标签或者全量推送
    private boolean androidBroadCast(PushMessage pushMessage)
    {
        try{
            Constants.useOfficial();
            Sender sender = new Sender(androidSecretKey);
            Message message = new Message.Builder()
                    .title(pushMessage.getTitle())
                    .description(pushMessage.getText()).payload(pushMessage.getText())
                    .restrictedPackageName(androidPackageName)
                    .notifyType(1)     // 使用默认提示音提示
                    .build();

            Result result = sender.broadcastAll(message, 3); //全量推送
            //sender.broadcast(message, topic, 3); //根据topic, 发送消息到指定一组设备上
            LOGGER.info("result from xiao mi:{}", JsonUtils.obj2json(result));
        }catch (Exception e){
            LOGGER.error("Failed to unicast, error:{}", e);
            return false;
        }

        return true;
    }

    @Override
    public boolean pushAllMessage(PushMessage pushMessage)
    {
        boolean androidResult = this.pushAndroidMessage(pushMessage);
        if (!androidResult)
        {
            LOGGER.error("Android push failed.");
        }
        boolean iosResult = this.pushIosMessage(pushMessage);
        if (!iosResult)
        {
            LOGGER.error("IOS push failed.");
        }
        // 只有安卓和苹果都推送失败时才返回false, 避免触发重试机制, 导致发送成功的重复推送消息
        return !androidResult && !iosResult ? false : true;
    }

    // 根据设备regid推送 单个
    private boolean iosUnicast(PushMessage pushMessage)
    {
        try
        {
            // playLoad 要发送的消息内容,透传消息必填非透传消息可选
            Constants.useOfficial(); // 生产环境; 测试环境 Constants.useSandbox();
            Sender sender = new Sender(iosSecretKey);
            Message message = new Message.IOSBuilder()
                    .description(pushMessage.getText())
                    .soundURL("default")    // 消息铃声
                    .badge(1)               // 数字角标
                    .build();

            sender.send(message, pushMessage.getAudiences().get(0), 3).getErrorCode(); //根据regID，发送消息到指定设备上
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to unicast, error:{}", e);
            return false;
        }
        return true;
    }

    // 根据设备regid推送 单个
    private boolean iosListCast(PushMessage pushMessage)
    {
        try{
            Constants.useOfficial();
            Sender sender = new Sender(iosSecretKey);
            Message message = new Message.IOSBuilder()
                    .description(pushMessage.getText())
                    .soundURL("default")    // 消息铃声
                    .badge(1)               // 数字角标
                    .build();
            sender.send(message, pushMessage.getAudiences(), 3); //发送消息到一组设备上, regids个数不得超过1000个
        }catch (Exception e){
            LOGGER.error("Failed to unicast, error:{}", e);
            return false;
        }
        return true;
    }

    // 根据regid绑定的别名推送
    private boolean iosAliasCast(PushMessage pushMessage)
    {
        try{
            Constants.useOfficial();
            Sender sender = new Sender(iosSecretKey);
            Message message = new Message.IOSBuilder()
                    .description(pushMessage.getText())
                    .soundURL("default")    // 消息铃声
                    .badge(1)               // 数字角标
                    .build();
            sender.sendToAlias(message, pushMessage.getAudiences(), 3); //根据aliasList, 发送消息到指定设备上
            // 一次不能超过1000
        }catch (Exception e){
            LOGGER.error("Failed to unicast, error:{}", e);
            return false;
        }

        return true;
    }

    // 标签或者全量推送
    private boolean iosBroadCast(PushMessage pushMessage)
    {
        try{
            Constants.useOfficial();
            Sender sender = new Sender(iosSecretKey);
            Message message = new Message.IOSBuilder()
                    .description(pushMessage.getText())
                    .soundURL("default")    // 消息铃声
                    .badge(1)               // 数字角标
                    .build();
//            .category("action")     // 快速回复类别、可选
//                    .extra("key", "value")  // 自定义键值对、可选

            sender.broadcastAll(message, 3); //全量推送
            //sender.broadcast(message, topic, 3); //根据topic, 发送消息到指定一组设备上
        }catch (Exception e){
            LOGGER.error("Failed to unicast, error:{}", e);
            return false;
        }

        return true;
    }
}
