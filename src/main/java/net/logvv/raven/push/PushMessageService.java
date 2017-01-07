package net.logvv.raven.push;

import net.logvv.raven.push.model.PushMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * (消息推送服务类)<br>
 */
@Service
public class PushMessageService
{

	private static final Logger LOGGER = LoggerFactory.getLogger(PushMessageService.class);
	
	public boolean validateMessage(PushMessage pushMessage)
	{
		//校验必选参数
		if (null == pushMessage || StringUtils.isEmpty(pushMessage.getText()) 
				|| StringUtils.isEmpty(pushMessage.getClientType().name())
				|| null == pushMessage.getPushChannel())
		{
			LOGGER.error("Invalid request params.");
			return false;
		}
		
		// 安卓推送, ticker和title不能为空
		if ("ANDROID".equals(pushMessage.getClientType().name()) 
				&& StringUtils.isEmpty(pushMessage.getTicker()) && StringUtils.isEmpty(pushMessage.getTitle()))
		{
			LOGGER.error("Invalid request params. ticker or title is empty");
			return false;
		}
		
		// 如果是单播, topicList参数必须不能为空
		if (PushMessage.PushType.UNICAST == pushMessage.getPushType()
				&& CollectionUtils.isEmpty(pushMessage.getAudiences()))
		{
			LOGGER.error("Invalid request params. topicList is empty.");
			return false;
		}
		return true;
	}

	@Async
	public void push(PushMessage pushMessage)
	{
		// 根据推送渠道获取实际的推送处理类
		MessagePusher pusher = PushFactroy.createPusher(pushMessage.getPushChannel());
		if (null == pusher)
		{
			LOGGER.error("Can not find message pusher, pushChannel is {}", pushMessage.getPushChannel());
			return;
		}
		
		boolean isSuccess = false;
		switch (pushMessage.getClientType())
		{
			case IOS:
				isSuccess = pusher.pushIosMessage(pushMessage);
				break;
			case ANDROID:
				isSuccess = pusher.pushAndroidMessage(pushMessage);
				break;
			case ALL:
				isSuccess = pusher.pushAllMessage(pushMessage);
				break;
			default:
				LOGGER.error("Failed to find client type pusher: {}", pushMessage.getClientType());
				break;
		}
		
		// 重试3次发送不成功
		if (!isSuccess)
		{
			LOGGER.error("Send push notification failed.");
		}

	}

}
