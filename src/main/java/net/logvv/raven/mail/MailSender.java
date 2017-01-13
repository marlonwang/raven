package net.logvv.raven.mail;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailSender implements Runnable
{
	/** 邮件发送器 */
	private JavaMailSender mailSender;
	
	/** 邮件消息 */
	private MessageBody mailMessage;
	
	/** 日志记录器 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);
	
	public MailSender(JavaMailSender mailSender, MessageBody mailMessage)
	{
		this.mailSender = mailSender;
		this.mailMessage = mailMessage;
	}
	
	@Override
	public void run() 
	{
		if (null != mailMessage)
		{
			MimeMessage mimeMsg = mailSender.createMimeMessage();
			try
			{
				// 设置true,表示支持附件
				MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMsg, true, "UTF-8");
				msgHelper.setFrom(mailMessage.getFromUser(), mailMessage.getCompanyName());
				msgHelper.setSubject(mailMessage.getSubject());
				String[] toArray = new String[mailMessage.getReceiveUsers().size()];
				msgHelper.setTo(mailMessage.getReceiveUsers().toArray(toArray));
				msgHelper.setText(mailMessage.getContent(), true);
				mailSender.send(mimeMsg);
			}
			catch (Exception e)
			{
				LOGGER.error("Send mail failed. Error message : {}", e.getMessage());
			}
		}
	}
}
