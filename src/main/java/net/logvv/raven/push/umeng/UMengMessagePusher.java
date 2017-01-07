package net.logvv.raven.push.umeng;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.logvv.raven.push.umeng.android.AndroidBroadcast;
import net.logvv.raven.push.umeng.ios.IOSBroadcast;
import net.logvv.raven.push.umeng.ios.IOSCustomizedcast;
import net.logvv.raven.push.umeng.android.AndroidCustomizedcast;
import net.logvv.raven.push.umeng.android.AndroidListcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.logvv.raven.push.MessagePusher;
import net.logvv.raven.push.model.PushMessage;
import net.logvv.raven.push.model.PushMessage.DisplayType;
import net.logvv.raven.push.umeng.android.AndroidUnicast;
import net.logvv.raven.push.umeng.ios.IOSListcast;
import net.logvv.raven.push.umeng.ios.IOSUnicast;

/**
 * 
 * (友盟推送)<br>
 * 
 * @author fanyaowu
 * @data 2015年2月6日
 * @version 1.0.0
 *
 */
@Service
public class UMengMessagePusher implements MessagePusher
{

	private static final Logger LOGGER = LoggerFactory.getLogger(UMengMessagePusher.class);
	
	private static final int RETRY_TIMES = 3;
	
	@Value("${android.appkey}")
	private String androidAppKey;
	
	@Value("${android.mastersecret}")
	private String androidMasterSecret;
	
	@Value("${ios.appkey}")
	private String iosAppKey;
	
	@Value("${ios.mastersecret}")
	private String iosMasterSecret;
	
	@Value("${ios.push.switch}")
	private String iosPushSwitch;
	
	/**
	 *  苹果推送
	 */
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
					isSuccess = iosBroadcast(pushMessage);
					break;
				case UNICAST:
					isSuccess = iosUnicast(pushMessage);
					break;
				case LISTCAST:
				    isSuccess = iosListcast(pushMessage);
                    break;
                case ALIASCAST:
                    isSuccess = iosAliascast(pushMessage);
                    break;
				default:
					LOGGER.error("IOS Not support push type:{} ", pushMessage.getPushType());
					return false;
			}
			count ++;
			
			if (isSuccess)
			{
				break;
			}
		}
		
		return isSuccess;
		
	}

	private boolean iosListcast(PushMessage pushMessage)
    {
       try
        {
            IOSListcast listcast = new IOSListcast();
            listcast.setAppMasterSecret(iosMasterSecret);
            listcast.setPredefinedKeyValue("appkey", iosAppKey);
            listcast.setPredefinedKeyValue("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            
            listcast.setPredefinedKeyValue("device_tokens", pushMessage.list2Str(pushMessage.getAudiences()));
            listcast.setPredefinedKeyValue("alert", pushMessage.getText());
            listcast.setPredefinedKeyValue("badge", 1);
            listcast.setPredefinedKeyValue("sound", "default");
            listcast.setPredefinedKeyValue("production_mode", iosPushSwitch);
            // 设置扩展参数
            Map<String, String> extraParams = pushMessage.getExtraParams();
            if (!extraParams.isEmpty())
            {
                Set<Entry<String, String>> entrySet = extraParams.entrySet();
                for (Entry<String, String> entry : entrySet)
                {
                    listcast.setCustomizedField(entry.getKey(), entry.getValue());
                }
            }
            return listcast.send();
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to broadcast!", e);
        }
        return false;
    }

    /**
	 * 
	 * 苹果单播推送<br>
	 * @param pushMessage
	 * @return
	 * boolean
	 * @Author chenxiaojin
	 * @date 2015年4月10日
	 * @exception 
	 * @version
	 */
	public boolean iosUnicast(PushMessage pushMessage)
	{
		try
		{
			IOSUnicast unicast = new IOSUnicast();
			unicast.setAppMasterSecret(iosMasterSecret);
			unicast.setPredefinedKeyValue("appkey", iosAppKey);
			unicast.setPredefinedKeyValue("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
			unicast.setPredefinedKeyValue("device_tokens", pushMessage.getAudiences().get(0));
			unicast.setPredefinedKeyValue("alert", pushMessage.getText());
			unicast.setPredefinedKeyValue("badge", 1);
			unicast.setPredefinedKeyValue("sound", "default");
			unicast.setPredefinedKeyValue("production_mode", iosPushSwitch);
			// 设置扩展参数
			Map<String, String> extraParams = pushMessage.getExtraParams();
			if (!extraParams.isEmpty())
			{
				Set<Entry<String, String>> entrySet = extraParams.entrySet();
				for (Entry<String, String> entry : entrySet)
				{
					unicast.setCustomizedField(entry.getKey(), entry.getValue());
				}
			}
			return unicast.send();
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to broadcast!", e);
		}
		return false;
	}
	
	/**
	 * 苹果广播推送<br>
	 * @param pushMessage
	 * @return
	 * boolean
	 * @Author chenxiaojin
	 * @date 2015年4月10日
	 * @exception 
	 * @version
	 */
	public boolean iosBroadcast(PushMessage pushMessage)
	{
		try
		{
			IOSBroadcast broadcast = new IOSBroadcast();
			broadcast.setAppMasterSecret(iosMasterSecret);
			broadcast.setPredefinedKeyValue("appkey", iosAppKey);
			broadcast.setPredefinedKeyValue("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
			broadcast.setPredefinedKeyValue("alert", pushMessage.getText());
			broadcast.setPredefinedKeyValue("badge", 1);
			broadcast.setPredefinedKeyValue("sound", "default");
			broadcast.setPredefinedKeyValue("production_mode", iosPushSwitch);
			// 设置扩展参数
			Map<String, String> extraParams = pushMessage.getExtraParams();
			if (!extraParams.isEmpty())
			{
				Set<Entry<String, String>> entrySet = extraParams.entrySet();
				for (Entry<String, String> entry : entrySet)
				{
					broadcast.setCustomizedField(entry.getKey(), entry.getValue());
				}
			}
			return broadcast.send();
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to broadcast!", e);
		}
		return false;
	}

    /** IOS按别名推送 */
    private boolean iosAliascast(PushMessage pushMessage)
    {
        try
        {
            IOSCustomizedcast customizedcast = new IOSCustomizedcast();
            customizedcast.setAppMasterSecret(iosMasterSecret);
            customizedcast.setPredefinedKeyValue("appkey", iosAppKey);
            customizedcast.setPredefinedKeyValue("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            customizedcast.setPredefinedKeyValue("alias_type", pushMessage.getAliasType());
            customizedcast.setPredefinedKeyValue("alias", pushMessage.list2Str(pushMessage.getAudiences()));
            customizedcast.setPredefinedKeyValue("alert", pushMessage.getText());
            customizedcast.setPredefinedKeyValue("badge", 1);
            customizedcast.setPredefinedKeyValue("sound", "default");
            customizedcast.setPredefinedKeyValue("production_mode", iosPushSwitch);
			// customizedcast.setPredefinedKeyValue("production_mode", "false");
            // 设置扩展参数
            Map<String, String> extraParams = pushMessage.getExtraParams();
            if (!extraParams.isEmpty())
            {
                Set<Entry<String, String>> entrySet = extraParams.entrySet();
                for (Entry<String, String> entry : entrySet)
                {
                    customizedcast.setCustomizedField(entry.getKey(), entry.getValue());
                }
            }
            return customizedcast.send();
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to broadcast!", e);
        }
        return false;
    }

	/**
	 * 安卓推送
	 */
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
					isSuccess = androidBroadcast(pushMessage);
					break;
				case UNICAST:
					isSuccess = androidUnicast(pushMessage);
				case LISTCAST:
                    isSuccess = androidListcast(pushMessage);
					break;
                case ALIASCAST:
                    isSuccess = androidAliascast(pushMessage);
                    break;
				default:
					LOGGER.error("Android Not support push type:{} ", pushMessage.getPushType());
					return false;
			}
			
			count ++;
			if (isSuccess)
			{
				break;
			}
		}
		
		return isSuccess;
	}

    // device_tokens 最多500个
	private boolean androidListcast(PushMessage pushMessage)
    {
	    boolean flag = false;

        try
        {
            AndroidListcast listcast = new AndroidListcast();
            listcast.setAppMasterSecret(androidMasterSecret);
            listcast.setPredefinedKeyValue("appkey", androidAppKey);
            listcast.setPredefinedKeyValue("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            // 设置推送设备
            listcast.setPredefinedKeyValue("device_tokens", pushMessage.list2Str(pushMessage.getAudiences()));
            listcast.setPredefinedKeyValue("ticker", null == pushMessage.getTicker() ?
					pushMessage.getText() : pushMessage.getTicker());
            listcast.setPredefinedKeyValue("title", pushMessage.getTitle());
            listcast.setPredefinedKeyValue("text", pushMessage.getText());
            listcast.setPredefinedKeyValue("play_sound", pushMessage.isPlaySound());
            listcast.setPredefinedKeyValue("sound", pushMessage.getSound());
            if (DisplayType.NOTIFICATION == pushMessage.getDisplayType())
            {
                Map<String, String> extraParams = pushMessage.getExtraParams();
                if (!extraParams.isEmpty())
                {
                    Set<Entry<String, String>> entrySet = extraParams.entrySet();
                    for (Entry<String, String> entry : entrySet)
                    {
                        listcast.setExtraField(entry.getKey(), entry.getValue());
                    }
                }
                listcast.setPredefinedKeyValue("display_type", DisplayType.NOTIFICATION.toString());
            }
            else
            {
                listcast.setPredefinedKeyValue("display_type", DisplayType.MESSAGE.toString());
            }

            listcast.setPredefinedKeyValue("production_mode", "true"); // 只针对广播、组播有效
            // 转换为友盟的点击推送消息后的动作结构
            switch (pushMessage.getAfterAction())
            {
                case "APP":
                    listcast.setPredefinedKeyValue("after_open", "go_app");
                    break;
                case "URL":
                    listcast.setPredefinedKeyValue("after_open", "go_url");
                    listcast.setPredefinedKeyValue("url", pushMessage.getActionContent());
                    break;
                case "ACTIVITY":
                    listcast.setPredefinedKeyValue("after_open", "go_activity");
                    listcast.setPredefinedKeyValue("activity", pushMessage.getActionContent());
                    break;
            }

            
            flag = listcast.send();
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to broadcast!", e);
        }

        return flag;

    }


    /**
	 * 安卓单播推送<br>
	 * @param pushMessage
	 * @return
	 * boolean
	 * @Author fanyaowu
	 * @data 2015年2月6日
	 * @exception 
	 * @version
	 *
	 */
	private boolean androidUnicast(PushMessage pushMessage)
	{
		boolean flag = false;

		try
		{
			AndroidUnicast unicast = new AndroidUnicast();
			unicast.setAppMasterSecret(androidMasterSecret);
			unicast.setPredefinedKeyValue("appkey", androidAppKey);
			unicast.setPredefinedKeyValue("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
			// 设置推送设备
			unicast.setPredefinedKeyValue("device_tokens", pushMessage.getAudiences().get(0));
			unicast.setPredefinedKeyValue("ticker", null == pushMessage.getTicker() ?
					pushMessage.getText() : pushMessage.getTicker());
			unicast.setPredefinedKeyValue("title", pushMessage.getTitle());
			unicast.setPredefinedKeyValue("text", pushMessage.getText());
			unicast.setPredefinedKeyValue("play_sound", pushMessage.isPlaySound());
			unicast.setPredefinedKeyValue("sound", pushMessage.getSound());
			if (DisplayType.NOTIFICATION == pushMessage.getDisplayType())
			{
				Map<String, String> extraParams = pushMessage.getExtraParams();
				if (!extraParams.isEmpty())
				{
					Set<Entry<String, String>> entrySet = extraParams.entrySet();
					for (Entry<String, String> entry : entrySet)
					{
						unicast.setExtraField(entry.getKey(), entry.getValue());
					}
				}
				unicast.setPredefinedKeyValue("display_type", DisplayType.NOTIFICATION.toString());
			}
			else
			{
				unicast.setPredefinedKeyValue("display_type", DisplayType.MESSAGE.toString());
			}

			unicast.setPredefinedKeyValue("production_mode", "true");
			// 转换为友盟的点击推送消息后的动作结构
			switch (pushMessage.getAfterAction())
			{
				case "APP":
					unicast.setPredefinedKeyValue("after_open", "go_app");
					break;
				case "URL":
					unicast.setPredefinedKeyValue("after_open", "go_url");
					unicast.setPredefinedKeyValue("url", pushMessage.getActionContent());
					break;
				case "ACTIVITY":
					unicast.setPredefinedKeyValue("after_open", "go_activity");
					unicast.setPredefinedKeyValue("activity", pushMessage.getActionContent());
					break;
			}

			
			flag = unicast.send();
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to broadcast!", e);
		}

		return flag;
	}

	/**
	 * 
	 * (广播消息)<br>
	 *
	 * @param pushMessage
	 * @return
	 * boolean
	 * @Author fanyaowu
	 * @data 2015年2月6日
	 * @exception 
	 * @version
	 *
	 */
	public boolean androidBroadcast(PushMessage pushMessage)
	{
		boolean flag = false;
		try
		{
			AndroidBroadcast broadcast = new AndroidBroadcast();
			broadcast.setAppMasterSecret(androidMasterSecret);
			broadcast.setPredefinedKeyValue("appkey", androidAppKey);
			broadcast.setPredefinedKeyValue("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
			broadcast.setPredefinedKeyValue("ticker", null == pushMessage.getTicker() ?
					pushMessage.getText() : pushMessage.getTicker());
			broadcast.setPredefinedKeyValue("title", pushMessage.getTitle());
			broadcast.setPredefinedKeyValue("text", pushMessage.getText());
			// 推送类型为通知类型时, 设置扩展参数.
			if (DisplayType.NOTIFICATION == pushMessage.getDisplayType())
			{
				Map<String, String> extraParams = pushMessage.getExtraParams();
				if (!extraParams.isEmpty())
				{
					Set<Entry<String, String>> entrySet = extraParams.entrySet();
					for (Entry<String, String> entry : entrySet)
					{
						broadcast.setExtraField(entry.getKey(), entry.getValue());
					}
				}
				broadcast.setPredefinedKeyValue("display_type", DisplayType.NOTIFICATION.toString());
			}
			else
			{
				broadcast.setPredefinedKeyValue("display_type", DisplayType.MESSAGE.toString());
			}
	
			// 转换为友盟的点击推送消息后的动作结构
			switch (pushMessage.getAfterAction())
			{
				case "APP":
					broadcast.setPredefinedKeyValue("after_open", "go_app");
					break;
				case "URL":
					broadcast.setPredefinedKeyValue("after_open", "go_url");
					broadcast.setPredefinedKeyValue("url", pushMessage.getActionContent());
					break;
				case "ACTIVITY":
					broadcast.setPredefinedKeyValue("after_open", "go_activity");
					broadcast.setPredefinedKeyValue("activity", pushMessage.getActionContent());
					break;
			}
	
			broadcast.setPredefinedKeyValue("production_mode", "true"); // 只针对广播组播有效
			flag = broadcast.send();
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to broadcast!", e);
		}
	
		return flag;
	
	}

    /**
     * 别名推送，一次最多推送50个
     * 方法描述: </br>
     * 适用条件: </br>
     * @param   
     * @return  
     * @author  wangwei
     * @version 2.0
     * @date    2016/11/8 15:46
     */
    private boolean androidAliascast(PushMessage pushMessage)
    {
        boolean flag = false;
        try
        {
            AndroidCustomizedcast customizedcast = new AndroidCustomizedcast();
            customizedcast.setAppMasterSecret(androidMasterSecret);
            customizedcast.setPredefinedKeyValue("appkey", androidAppKey);
            customizedcast.setPredefinedKeyValue("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            customizedcast.setPredefinedKeyValue("ticker", null == pushMessage.getTicker() ?
                    pushMessage.getText() : pushMessage.getTicker());
            customizedcast.setPredefinedKeyValue("title", pushMessage.getTitle());
            customizedcast.setPredefinedKeyValue("text", pushMessage.getText());
            customizedcast.setPredefinedKeyValue("alias_type", pushMessage.getAliasType());
            customizedcast.setPredefinedKeyValue("alias", pushMessage.list2Str(pushMessage.getAudiences()));
            // 推送类型为通知类型时, 设置扩展参数.
            if (DisplayType.NOTIFICATION == pushMessage.getDisplayType())
            {
                Map<String, String> extraParams = pushMessage.getExtraParams();
                if (!extraParams.isEmpty())
                {
                    Set<Entry<String, String>> entrySet = extraParams.entrySet();
                    for (Entry<String, String> entry : entrySet)
                    {
                        customizedcast.setExtraField(entry.getKey(), entry.getValue());
                    }
                }
                customizedcast.setPredefinedKeyValue("display_type", DisplayType.NOTIFICATION.toString());
            }
            else
            {
                customizedcast.setPredefinedKeyValue("display_type", DisplayType.MESSAGE.toString());
            }

            // 转换为友盟的点击推送消息后的动作结构
            switch (pushMessage.getAfterAction())
            {
                case "APP":
                    customizedcast.setPredefinedKeyValue("after_open", "go_app");
                    break;
                case "URL":
                    customizedcast.setPredefinedKeyValue("after_open", "go_url");
                    customizedcast.setPredefinedKeyValue("url", pushMessage.getActionContent());
                    break;
                case "ACTIVITY":
                    customizedcast.setPredefinedKeyValue("after_open", "go_activity");
                    customizedcast.setPredefinedKeyValue("activity", pushMessage.getActionContent());
                    break;
            }

            customizedcast.setPredefinedKeyValue("production_mode", "true");
            flag = customizedcast.send();
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to broadcast!", e);
        }

        return flag;
    }

	/**
	 * 苹果和安卓推送
	 */
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

}
