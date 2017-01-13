package net.logvv.raven.push;

import net.logvv.raven.push.hwpush.HWMessagePusher;
import net.logvv.raven.push.jpush.JPushMessagePusher;
import net.logvv.raven.push.umeng.UMengMessagePusher;
import net.logvv.raven.push.xg.XGMessagePusher;
import net.logvv.raven.push.xiaomi.XMMessagePusher;
import net.logvv.raven.push.model.PushMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.logvv.raven.common.spring.SpringContextHolder;

/**
 * (推送工厂，用于生产消息推送器)<br>
 */
public final class PushFactroy
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PushFactroy.class);
    
    private PushFactroy()
    {
        
    }
    
    public static MessagePusher createPusher(PushMessage.PushChannel pushChannel)
    {
        if (pushChannel == null)
        {
            return null;
        }
        
        switch (pushChannel)
        {
            case UMENG:
                return SpringContextHolder.getBean(UMengMessagePusher.class);
            case JPUSH:
                return SpringContextHolder.getBean(JPushMessagePusher.class);
            case XG:
            	return SpringContextHolder.getBean(XGMessagePusher.class);
            case MI:
                return SpringContextHolder.getBean(XMMessagePusher.class);
            case HUAWEI:
                return SpringContextHolder.getBean(HWMessagePusher.class);
            default:
                LOGGER.error("Failed to find push channel: {}", pushChannel.toString());
                return null;
        }
    }
    
}
