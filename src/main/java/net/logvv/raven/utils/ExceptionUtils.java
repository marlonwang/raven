package net.logvv.raven.utils;

import net.logvv.raven.common.exception.ExceptionLevel;
import net.logvv.raven.common.exception.ExceptionMessage;

public class ExceptionUtils
{
	/** 日志记录器 */
	/** 网页换行 */
	private static final String newLine = "</br>";
	
	/** html字体颜色模板 */
	private static final String colorTemplate = "%s%s%s";
	
	public ExceptionUtils()
	{
		
	}
	

	/** 获取异常堆栈异常信息 */
	public static String getStackTraceMsg(Throwable e)
	{
		if (null == e){
			return "";
		}
		
		StackTraceElement[] messages = e.getStackTrace();
		StringBuilder exceptionMsg = new StringBuilder(2000);
		exceptionMsg.append(bold(e.toString())).append(newLine);
		int length = messages.length;
		exceptionMsg.append("<font color=\"red\">");
		for (int i = 0; i < length; i++)
		{
			exceptionMsg.append("at " + messages[i].toString()).append(newLine);
		}
		exceptionMsg.append("</font>");

		return exceptionMsg.toString();
	}
	
	/** 生成异常信息实体类*/
	public static ExceptionMessage generateExceptionMsg(String phoneNumber, String productCode, String versionName, String clientType, 
			ExceptionLevel exceptionLevel,String operationDesc, String exceptionMsg)
	{
		ExceptionMessage exceptionMsgObj = new ExceptionMessage();
		exceptionMsgObj.setPhoneNumber(null == phoneNumber ? "NONE" : phoneNumber);
		exceptionMsgObj.setExceptionLevel(null == exceptionLevel ? ExceptionLevel.CRITICAL : exceptionLevel);
		exceptionMsgObj.setExceptionMessage(null == exceptionMsg ? "Unkown Exception" : exceptionMsg);
		exceptionMsgObj.setProductCode(null == productCode ? "" : productCode);
		exceptionMsgObj.setClientType(null == clientType ? "SERVER" : clientType);
		exceptionMsgObj.setUserOperationDesc(null == operationDesc ? "" : operationDesc);
		exceptionMsgObj.setVersionName(null == versionName ? "" : versionName);
		return exceptionMsgObj;
	}

	private static String bold(String str)
	{
		return String.format(colorTemplate, "<b>", str, "</b>");
	}
}
