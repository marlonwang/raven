package net.logvv.raven.push.xg;

import java.util.List;

import net.logvv.raven.push.xg.xinge.MessageIOS;
import net.logvv.raven.push.model.PushMessage;
import net.logvv.raven.push.xg.xinge.Message;
import net.logvv.raven.push.xg.xinge.XingeClient;
import net.logvv.raven.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.logvv.raven.push.MessagePusher;

@Service
public class XGMessagePusher implements MessagePusher
{
	private static final Logger LOGGER = LoggerFactory.getLogger(XGMessagePusher.class);
	
	@Value("${xg.android.accessid}")
	private long androidAccessId;
	
	@Value("${xg.android.accesskey}")
	private String androidAccessKey;
	
	@Value("${xg.android.secretkey}")
	private String androidSecretKey;
	
	@Value("${xg.ios.accessid}")
	private long iosAccessId;
	
	@Value("${xg.ios.accesskey}")
	private String iosAccessKey;
	
	@Value("${xg.ios.secretkey}")
	private String iosSecretKey;
	
	@Override
	public boolean pushIosMessage(PushMessage pushMessage)
	{
		boolean status = false;
        switch (pushMessage.getPushType())
        {
            case UNICAST:
            case LISTCAST:
                status = pushIosMsg(pushMessage);
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
                status = pushAndroidMsg(pushMessage);
                break;
            default:
                break;
        }		
		return status;
	}

	@Override
	public boolean pushAllMessage(PushMessage pushMessage) {
		boolean status = false;
        switch (pushMessage.getPushType())
        {
            case UNICAST:
            case LISTCAST:
                status = pushAllMsg(pushMessage);
                break;
            default:
                break;
        }		
		return status;
	}
	
	/**
	 * pushIosMsg (IOS消息推送)
	 * @param pushMessage
	 * @return
	 * @return boolean  返回类型 
	 * @author wangwei
	 * @date 2016年2月16日 上午11:36:31 
	 * @version  [1.0, 2016年2月16日]
	 * @since  version 1.0
	 */
	private boolean pushIosMsg(PushMessage pushMessage)
	{
		boolean flag = false;
		XingeClient xinge = new XingeClient(iosAccessId, iosSecretKey);
		// 这里只考虑根据设备推送，不考虑根据账号推送(需要app端关联设备token)
		MessageIOS msgIos = this.convertPushMesage2MessageIOS(pushMessage);

		// System.out.println(JsonUtils.obj2json(msgIos));
	
		List<String> deviceTokens = pushMessage.getAudiences();
		for(String token : deviceTokens)
		{
			try {
				JSONObject result = xinge.pushSingleDevice(token, msgIos, XingeClient.IOSENV_PROD);
				LOGGER.info("push ios message result:{}", result.toString());
				if(result.getInt("ret_code") == 0){
					flag = true;
				}
			} catch (JSONException e) {
				LOGGER.error("push error:{}",e);
			}
		}

		return flag;
	}
	
	/**
	 * pushAndroidMsg (android消息推送)
	 * @param pushMessage
	 * @return
	 * @return boolean  返回类型 
	 * @author wangwei
	 * @date 2016年2月16日 上午11:37:58 
	 * @version  [1.0, 2016年2月16日]
	 * @since  version 1.0
	 */
	private boolean pushAndroidMsg(PushMessage pushMessage)
	{
		boolean flag = false;
		XingeClient xinge = new XingeClient(androidAccessId, androidSecretKey);
		Message msg = this.convertPushMessage2MessageAndroid(pushMessage);
		List<String> deviceTokens = pushMessage.getAudiences();
		for(String token : deviceTokens)
		{
			try {
				JSONObject result = xinge.pushSingleDevice(token, msg);
				LOGGER.info("push android message result:{}",result.toString());
				if(result.getInt("ret_code") == 0){
					flag = true;
				}
			} catch (JSONException e) {
				LOGGER.error("push error:{}",e);
			}
		}
		return flag;
	}
	
	/**
	 * pushAllMsg (推送消息到ios和android设备)
	 * @param pushMessage
	 * @return
	 * @return boolean  返回类型 
	 * @author wangwei
	 * @date 2016年2月16日 上午11:39:03 
	 * @version  [1.0, 2016年2月16日]
	 * @since  version 1.0
	 */
	private boolean pushAllMsg(PushMessage pushMessage)
	{
		Message msg = this.convertPushMessage2MessageAndroid(pushMessage);
		MessageIOS msgIos = this.convertPushMesage2MessageIOS(pushMessage);
		
		try {
			// android 推送
			XingeClient xinge = new XingeClient(androidAccessId, androidSecretKey);
			JSONObject result = xinge.pushAllDevice(0, msg);
			LOGGER.info("push all device android, result:{}", JsonUtils.obj2json(result));
			// ios 推送
			xinge = new XingeClient(iosAccessId, iosSecretKey);
//			JSONObject result2 = xinge.pushAllDevice(0, msgIos, XingeClient.IOSENV_DEV); // 开发环境使用
			JSONObject result2 = xinge.pushAllDevice(0, msgIos, XingeClient.IOSENV_PROD); // 正式生产环境使用
			LOGGER.info("push all device ios, result:{}",JsonUtils.obj2json(result2));
			
			return true;
		} catch (JSONException e) {
			LOGGER.error("push error:{}",e);
		}

		return false;
	}
	
	public Message convertPushMessage2MessageAndroid(PushMessage pushMessage)
	{
		Message msg = new Message();
		msg.setContent(pushMessage.getText());
		msg.setTitle(StringUtils.isEmpty(pushMessage.getTitle()) ? pushMessage.getText() : pushMessage.getTitle());
		msg.setExpireTime(60*60*24*2); // 离线消息存储时间 单位s,最多3天   这里设置为2天
		msg.setSendTime("2016-02-16 15:37:00"); // 小于服务器当前时间，则会立即推送
		
		switch (pushMessage.getDisplayType()) 
		{
			case NOTIFICATION:
				msg.setType(Message.TYPE_NOTIFICATION);
				break;
			case MESSAGE:
				msg.setType(Message.TYPE_MESSAGE);
			default:
				msg.setType(Message.TYPE_NOTIFICATION);
				break;
		}
		// msg.setCustom(pushMessage.getExtraParams());
		return msg;
	}
	
	public MessageIOS convertPushMesage2MessageIOS(PushMessage pushMessage)
	{
		MessageIOS msg = new MessageIOS();
		msg.setExpireTime(60*60*24*2); // 离线消息存储时间 单位s,最多3天   这里设置为2天
		msg.setSendTime("2015-12-20 15:37:00"); // 小于服务器当前时间，则会立即推送
		// msg.setCustom(pushMessage.getExtraParams());
		msg.setAlert(pushMessage.getText());
		msg.setSound(pushMessage.getSound());
		msg.setBadge(1);
		msg.setSound("beep.wav");
		return msg;
	}
	
}
