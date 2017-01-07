package net.logvv.raven.push.jpush;

import net.logvv.raven.push.MessagePusher;
import net.logvv.raven.push.jpush.common.resp.APIRequestException;
import net.logvv.raven.push.jpush.notification.Notification;
import net.logvv.raven.push.model.jpush.PushPayload;
import net.logvv.raven.push.model.jpush.PushResult;
import net.logvv.raven.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.logvv.raven.push.jpush.common.resp.APIConnectionException;
import net.logvv.raven.push.model.PushMessage;
import net.logvv.raven.push.model.jpush.Audience;
import net.logvv.raven.push.model.jpush.Platform;

@Service
public class JPushMessagePusher implements MessagePusher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(JPushMessagePusher.class);
    
    @Value("${jpush.app.key}")
    private String appKey;
    
    @Value("${jpush.master.secret}")
    private String masterSecret;
    
    @Override
    public boolean pushIosMessage(PushMessage pushMessage)
    {
    	boolean status = false; 
        switch (pushMessage.getPushType())
        {
            case UNICAST:
            case LISTCAST:
                status = pushIosRegistration(pushMessage);
                break;
            default:
                break;
        }
        return status;
    }
    
    @Override
    public boolean pushAndroidMessage(PushMessage pushMessage)
    {
    	boolean status = false;
        switch (pushMessage.getPushType())
        {
            case UNICAST:
            case LISTCAST:
                status = pushAndroidRegistration(pushMessage);
                break;
            default:
                break;
        }
        return status;
    }
    
    @Override
    public boolean pushAllMessage(PushMessage pushMessage)
    {
    	boolean status = false;
        switch (pushMessage.getPushType())
        {
            case UNICAST:
            case LISTCAST:
            	status = pushALLRegistration(pushMessage);
                break;
            default:
                break;
        }
        return status;
    }
    
    /**
     * 
     * pushIosRegistration(通过)
     * 
     * @return void 返回类型
     * @author lvyongwen
     * @date 2015年9月14日 下午2:48:16
     * @version [1.0, 2015年9月14日]
     * @param pushMessage
     * @return
     * @since version 1.0
     */
    private boolean pushIosRegistration(PushMessage pushMessage)
    {
        PushClient pushClient = new PushClient(masterSecret, appKey);
        
        try
        {
            PushPayload payload =
                PushPayload.newBuilder()
                    .setPlatform(Platform.ios())
                    .setAudience(Audience.registrationId(pushMessage.getAudiences()))
                    .setNotification(Notification.ios(pushMessage.getText(),pushMessage.getExtraParams()))
                    .setOptions(pushMessage.getOptions())
                    .build();
            PushResult result = pushClient.sendPush(payload);
            LOGGER.info("push ios registration result data is:{} ", JsonUtils.obj2json(result));
            
            if ("200".equals(result.getResponseCode()))
            {
                return true;
            }
        }
        catch (APIConnectionException | APIRequestException e)
        {
            LOGGER.error("Error message is :{}", e);
        }
        return false;
    }
    
    private boolean pushAndroidRegistration(PushMessage pushMessage)
    {
        PushClient pushClient = new PushClient(masterSecret, appKey);
        
        try
        {
            PushPayload payload =
                PushPayload.newBuilder()
                    .setPlatform(Platform.android())
                    .setAudience(Audience.registrationId(pushMessage.getAudiences()))
                    .setNotification(Notification.android(pushMessage.getText(),pushMessage.getTitle(), pushMessage.getExtraParams()))
                    .build();
            PushResult result = pushClient.sendPush(payload);
            LOGGER.info("push ios registration result data is:{} ", JsonUtils.obj2json(result));
            
            if (result.getResponseCode() == 200)
            {
                return true;
            }
        }
        catch (APIConnectionException | APIRequestException e)
        {
            LOGGER.error("Error message is :{}", e);
        }
        return false;
    }
    
    private boolean pushALLRegistration(PushMessage pushMessage)
    {
        PushClient pushClient = new PushClient(masterSecret, appKey);
        
        try
        {
            PushPayload payload =
                PushPayload.newBuilder()
                    .setPlatform(Platform.all())
                    .setAudience(Audience.registrationId(pushMessage.getAudiences()))
                    .setNotification(Notification.ios(null, pushMessage.getExtraParams()))
                    .build();
            PushResult result = pushClient.sendPush(payload);
            LOGGER.info("push ios registration result data is:{} ", JsonUtils.obj2json(result));
            
            if ("200".equals(result.getResponseCode()))
            {
                return true;
            }
        }
        catch (APIConnectionException | APIRequestException e)
        {
            LOGGER.error("Error message is :{}", e);
        }
        return false;
    }
    
}
