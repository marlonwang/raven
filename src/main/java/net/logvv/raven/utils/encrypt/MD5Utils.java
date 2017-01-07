package net.logvv.raven.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MD5Utils
{
	private static final String DIGEST_TYPE = "MD5";

	private static final Logger LOGGER = LoggerFactory.getLogger(MD5Utils.class);
	
	private MD5Utils()
	{
		
	}
	
	
	/**
	 * 
	 * 获取MD5加密后的的摘要信息<br>
	 *
	 * @param message
	 * @param digestKey
	 * @return
	 * String
	 * @Author fanyaowu
	 * @data 2014年7月23日
	 * @exception 
	 * @version
	 *
	 */
	public static String getMD5Digest(String message)
	{
		String digestResult = null;
		
		try
		{
			MessageDigest md5 = MessageDigest.getInstance(DIGEST_TYPE);
			
			byte[] temp;
			// 使用指定的 byte 数组对摘要进行最后更新，然后完成摘要计算
			temp = md5.digest(message.getBytes("UTF-8"));
			
			digestResult = HexByte.parseByte2HexStr(temp);

		}
		catch (NoSuchAlgorithmException e)
		{
			LOGGER.error("Failed to get md5 instance!",e);
		}
		catch (UnsupportedEncodingException e)
		{
			LOGGER.error("Failed to encode utf-8 instance!",e);
		}
		
		return digestResult;
	}
	
}
