package net.logvv.raven.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import net.logvv.raven.common.constant.Constants;
import net.logvv.raven.common.exception.ExceptionMessage;
import net.logvv.raven.mail.MessageBody.MessageType;

@Service
public class MailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
	
	/** 网页换行 */
	private final static String newLine = "</br>";
	
	/** html字体颜色模板 */
	private final String colorTemplate = "%s%s%s";
	
	@Value("${mail.company.name}")
	private String defaultCompany;
	
	@Value("${mail.account.from}")
	private String emailSender;
	
	@Value("${mail.account.to}")
	private String emailReceiver;
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	private String bold(String str)
	{
		return String.format(colorTemplate, "<b>", str, "</b>");
	}
	
	/**
	 * sendMail
	 * 通用邮件发送方法 <br/>
	 * @param message
	 * @throws Exception
	 * @return void  返回类型 
	 * @author wangwei
	 * @date 2016年1月4日 下午11:20:12 
	 * @version  [1.0, 2016年1月4日]
	 * @since  version 1.0
	 */
	public void sendMail(MessageBody message) throws Exception
	{
		if(!message.getSubject().contains("NORMAL"))
		{
			taskExecutor.execute(new MailSender(mailSender, message));
			// 由于 ThreadPoolTaskExecutor 会在 Spring 容器关闭时自动关闭，
			// 如果用junit测试 程序需要提供一段模拟的运行时间
			// TODO 正式运行注释以下代码
			// Thread.sleep(15000); 
		}
	}
	
	/**
	 * sendExceptionMail
	 * 异常邮件发送 <br/>
	 * @param exceptionMsg
	 * @return
	 * @return boolean  返回类型 
	 * @author wangwei
	 * @date 2016年1月4日 下午11:20:42 
	 * @version  [1.0, 2016年1月4日]
	 * @throws Exception 
	 * @since  version 1.0
	 */
	@Async
	public void sendExceptionMail(ExceptionMessage exceptionMsg)
	{
		MessageBody emailMessage = new MessageBody();
		emailMessage.setMessageType(MessageType.MAIL);
		// from
		emailMessage.setFromUser(emailSender);
		emailMessage.setCompanyName(StringUtils.isEmpty(defaultCompany) ? Constants.COMPANY_NAME : defaultCompany);
		
		// to
		String[] receiver = emailReceiver.split(",");
		List<String> receiveUsers = new ArrayList<String>();
		for(int i = 0; i<receiver.length;i++){
			receiveUsers.add(receiver[i]);
		}
				
		emailMessage.setReceiveUsers(receiveUsers);
		
		// subject
		StringBuilder subjectStr = new StringBuilder(100);
		subjectStr.append("【Exception】")
				  .append("[").append(exceptionMsg.getExceptionLevel()).append("]")
				  .append("[").append(exceptionMsg.getProductCode()).append("]");
		
		if (StringUtils.isEmpty(exceptionMsg.getClientType()))
		{
			subjectStr.append("[").append("ANDROID").append("]");
		}
		else
		{
			subjectStr.append("[").append(exceptionMsg.getClientType()).append("]");
		}
		
		if (!StringUtils.isEmpty(exceptionMsg.getVersionName()))
		{
			subjectStr.append("[").append(exceptionMsg.getVersionName()).append("]");
		}
		String occurTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
		subjectStr.append("_").append(occurTime);
		emailMessage.setSubject(subjectStr.toString());
		
		// content
		StringBuilder exceptionContent = new StringBuilder();
		if (!StringUtils.isEmpty(exceptionMsg.getPhoneNumber()))
		{
			exceptionContent.append(bold("[User] : ")).append(exceptionMsg.getPhoneNumber()).append(newLine);
		}
		exceptionContent.append(bold("[occur time]: ")).append(occurTime).append(newLine);
		exceptionContent.append(bold("[operation]: ")).append(exceptionMsg.getUserOperationDesc()).append(newLine);
		exceptionContent.append(bold("[Exception] : ")).append(exceptionMsg.getExceptionMessage()).append(newLine);
		emailMessage.setContent(exceptionContent.toString());
		
		try
		{
			sendMail(emailMessage);
		}
		catch (Exception e)
		{
			LOGGER.error("Send email failed.error message:{}",e);
		}
		
	}
	
}
