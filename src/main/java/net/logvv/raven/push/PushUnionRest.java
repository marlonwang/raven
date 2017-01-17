package net.logvv.raven.push;

import io.swagger.annotations.ApiOperation;
import net.logvv.raven.common.model.ErrorCode;
import net.logvv.raven.push.model.PushMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import net.logvv.raven.common.model.GeneralResult;

/**
 * 统一推送Controller </br>
 * @author  wangwei
 * @version 1.0
 * @date    2017/1/12 15:58
 */
@RestController
public class PushUnionRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PushUnionRest.class);

    @Autowired
    private PushMessageService pushService;

    /**
     * 方法描述: 发送通知,通知栏显示</br>
     * 适用条件: 通知栏消息</br>
     * @param   notification
     * @return  result
     * @author  wangwei
     * @version 1.0
     * @date    2017/1/12 15:58
     */
    @ApiOperation(value = "通知栏消息推送",notes = "通知栏展示,批量推送设备不能超过1000")
	@RequestMapping(value = "/push/notification", method=RequestMethod.POST)
	public GeneralResult notification(@RequestBody PushMessage notification)
	{
		GeneralResult result = new GeneralResult();
        if(null == notification){
            result.setErr(ErrorCode.INVALID_REQ_PARAMS);
            return result;
        }
        if(PushMessage.PushChannel.HUAWEI == notification.getPushChannel()
                && StringUtils.isBlank(notification.getIntent()))
        {
            result.setErr(ErrorCode.INVALID_REQ_PARAMS);
            return result;
        }

		LOGGER.info("Begin to push notification.");
		try
        {
            notification.setDisplayType(PushMessage.DisplayType.NOTIFICATION);
            if(null == notification.getPushType()){
                notification.setPushType(PushMessage.PushType.LISTCAST);
            }

            pushService.push(notification);
            result.setResultStatus(true);

		}catch (Exception e){
			LOGGER.error("failed to push notification, error:{}",e);
            result.setErr(ErrorCode.PUSH_MESSAGE_FAIL);
            return result;
		}

		return result;
		
	}

    /**
     * 方法描述: 发送消息,app内处理</br>
     * 适用条件: 不显示在通知栏,app自行处理</br>
     * @param   message
     * @return  result
     * @author  wangwei
     * @version 1.0
     * @date    2017/1/12 15:59
     */
    @ApiOperation(value = "应用透传消息",notes = "应用内接收,批量推送设备不能超过1000")
	@RequestMapping(value = "/push/message", method = RequestMethod.POST)
	public GeneralResult message(@RequestBody PushMessage message)
	{
		GeneralResult result = new GeneralResult();
        if(null == message)
        {
            result.setErr(ErrorCode.INVALID_REQ_PARAMS);
            return result;
        }
        LOGGER.info("Begin to push message.");

        try
        {
            message.setDisplayType(PushMessage.DisplayType.MESSAGE);
            if(null == message.getPushType()){
                message.setPushType(PushMessage.PushType.LISTCAST);
            }

            pushService.push(message);
            result.setResultStatus(true);

        }catch (Exception e){
            LOGGER.error("failed to push message, error:{}",e);
            result.setErr(ErrorCode.PUSH_MESSAGE_FAIL);
            return result;
        }
		return result;
	}

}
