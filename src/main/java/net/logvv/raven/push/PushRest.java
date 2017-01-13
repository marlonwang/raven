package net.logvv.raven.push;

import net.logvv.raven.common.model.ErrorCode;
import net.logvv.raven.push.model.PushMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import net.logvv.raven.common.model.GeneralResult;

@RestController
public class PushRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PushRest.class);

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
	@RequestMapping(value = "/push/notification", method=RequestMethod.POST)
	public GeneralResult notification(@RequestBody PushMessage notification)
	{
		GeneralResult result = new GeneralResult();
		LOGGER.info("Begin to push notification.");
		try {

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
	@RequestMapping(value = "/push/message", method = RequestMethod.POST)
	public GeneralResult message(@RequestBody PushMessage message)
	{
		GeneralResult result = new GeneralResult();
        LOGGER.info("Begin to push message.");
        try{

        }catch (Exception e){
            LOGGER.error("failed to push message, error:{}",e);
            result.setErr(ErrorCode.PUSH_MESSAGE_FAIL);
            return result;
        }
		return result;
	}

}
