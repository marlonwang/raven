package net.logvv.raven.push;

import net.logvv.raven.push.model.PushMessage;

public interface MessagePusher
{
	/**
	 * (推送IOS消息，默认重试3次)<br>
	 */
	boolean pushIosMessage(PushMessage pushMessage);
	
	/**
	 * (推送安卓消息，默认重试3次)<br>
	 */
	boolean pushAndroidMessage(PushMessage pushMessage);
	
	/**
	 * (推送消息给所有设备)<br>
	 */
	boolean pushAllMessage(PushMessage pushMessage);
}
