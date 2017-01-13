package net.logvv.raven.mail;

import java.util.List;

import org.joda.time.DateTime;

public class MessageBody {
	
	public enum MessageType {
		MAIL, SMS
	}

	/** 邮件主题 */
	private String subject;

	/** 邮件发件人 */
	private String fromUser;

	// 短信则为手机号码，邮件则为邮箱
	private List<String> receiveUsers;

	/** 邮件抄送人 */
	private List<String> copyToUsers;

	/** 邮件密送人 */
	private List<String> bccUsers;

	// 邮件短信内容
	private String content;

	// 定时发送用
	private DateTime dateTime;

	// 消息类型， 默认为短信
	private MessageType messageType = MessageType.MAIL;

	// 公司名称，默认为亿家网络
	private String companyName;

	public List<String> getReceiveUsers() {
		return receiveUsers;
	}

	public void setReceiveUsers(List<String> receiveUsers) {
		this.receiveUsers = receiveUsers;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public List<String> getCopyToUsers() {
		return copyToUsers;
	}

	public void setCopyToUsers(List<String> copyToUsers) {
		this.copyToUsers = copyToUsers;
	}

	public List<String> getBccUsers() {
		return bccUsers;
	}

	public void setBccUsers(List<String> bccUsers) {
		this.bccUsers = bccUsers;
	}
}
