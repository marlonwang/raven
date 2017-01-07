package net.logvv.raven.push.hwpush;

import net.logvv.raven.push.MessagePusher;
import net.logvv.raven.push.model.PushMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Author: wangwei
 * Created on 2016/12/3 16:44.
 */
public class HWMessagePusher implements MessagePusher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HWMessagePusher.class);
    private static final int RETRY_TIMES = 3;

    @Value("${huawei.android.appkey}")
    private String appKey;

    @Value("${huawei.android.appsecret}")
    private String appSecret;

    @Override
    public boolean pushIosMessage(PushMessage pushMessage)
    {
        return false;
    }

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

    @Override
    public boolean pushAllMessage(PushMessage pushMessage)
    {
        return false;
    }

    // android push method
    private boolean androidUnicast(PushMessage pushMessage){
        return false;
    }

    private boolean androidListcast(PushMessage pushMessage){
        return false;
    }

    private boolean androidAliascast(PushMessage pushMessage){
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
