package net.logvv.raven.utils.encrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import net.logvv.raven.common.exception.EncryptException;


/**
 * 
 * AES加解密工具类<br>
 * 
 * @author fanyaowu
 * @data 2014年7月9日
 * @version 1.0.0
 *
 */
public final class AESUtils
{

	// 加密算法
	private static final String ENCRYPT_TYPE = "AES";

	// AES默认加密key
	private static final String AES_DEFAULT_KEY = "30.TECH";

	// AES 默认加密长度
	private static final int AES_DEFAULT_KEY_SIZE = 128;

	private static final byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6 };

	private static final String VIPARA = "0102030405060708";
	
	/**
	 * 私有构造，工具类的通用做法
	 */
	private AESUtils()
	{

	}

	public static SecretKeySpec getSecretKey(String secretKey) throws Exception
	{
		KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPT_TYPE);
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(secretKey.getBytes());

		kgen.init(AES_DEFAULT_KEY_SIZE, random);
		SecretKey sk = kgen.generateKey();

		byte[] enCodeFormat = sk.getEncoded();
		return new SecretKeySpec(enCodeFormat, ENCRYPT_TYPE);
	}

	public static String encrypt(String content) throws EncryptException
	{

		return encrypt(content, AES_DEFAULT_KEY);
	}

	public static String decrypt(String content) throws EncryptException
	{
		return decrypt(content, AES_DEFAULT_KEY);
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 * @throws EncryptException
	 */
	public static String encrypt(String content, String encryptKey)
			throws EncryptException
	{

		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(encryptKey))
		{
			return null;
		}

		try
		{
			SecretKeySpec key = getSecretKey(encryptKey);
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化

			// 加密
			byte[] result = cipher.doFinal(byteContent);

			return HexByte.parseByte2HexStr(result);
		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to encrypt for content: "
					+ content + ", encrypt key: " + encryptKey, e);
		}
	}

	/**
	 * 解密
	 * 
	 * @param content  待解密内容
	 *           
	 * @param password  解密密钥
	 *           
	 * @return
	 */
	public static String decrypt(String content, String decryptKey) throws EncryptException
	{
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(decryptKey))
		{
			return null;
		}

		try
		{
			SecretKeySpec key = getSecretKey(decryptKey);
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(HexByte.parseHexStr2Byte(content)); // 解密
			return new String(result, "UTF-8");
		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to decrypt for content: "
					+ content + ", decrypt key: " + decryptKey, e);
		}
	}

	public static String encryptCbc(String content, String encryptKey) throws EncryptException
	{
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(encryptKey))
		{
			return null;
		}

		try
		{
			SecretKeySpec key = getSecretKey(encryptKey);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");

			IvParameterSpec param = new IvParameterSpec(iv);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, key, param);

			// 加密
			byte[] result = cipher.doFinal(byteContent);

			return Base64.encodeBase64String(result);
		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to encrypt for content: "
					+ content + ", encrypt key: " + encryptKey, e);
		}
	}

	public static String decryptCbc(String content, String decryptKey) throws EncryptException
	{
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(decryptKey))
		{
			return null;
		}

		try
		{
			SecretKeySpec key = getSecretKey(decryptKey);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器

			IvParameterSpec param = new IvParameterSpec(iv);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, key, param);

			// 正式执行解密操作
			byte decryptedData[] = cipher.doFinal(Base64.decodeBase64(content));

			return new String(decryptedData);
		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to decrypt for content: "
					+ content + ", encrypt key: " + decryptKey, e);
		}
	}

	public static String encryptNoPadding(String content, String encryptKey) throws EncryptException
	{
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(encryptKey))
		{
			return null;
		}

		try
		{
			SecretKeySpec key = getSecretKey(encryptKey);
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");

			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, key);

			// 加密
			byte[] result = cipher.doFinal(byteContent);

			return Base64.encodeBase64String(result);
		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to encrypt for content: "
					+ content + ", encrypt key: " + encryptKey, e);
		}
	}

	public static String decryptNoPadding(String content, String decryptKey) throws EncryptException
	{
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(decryptKey))
		{
			return null;
		}

		try
		{
			SecretKeySpec key = getSecretKey(decryptKey);

			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");// 创建密码器

			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, key);

			// 正式执行解密操作
			byte decryptedData[] = cipher.doFinal(Base64.decodeBase64(content));

			return new String(decryptedData);
		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to decrypt for content: "
					+ content + ", encrypt key: " + decryptKey, e);
		}
	}

	public static String encryptCbcTemp(String content, String encryptKey)
			throws EncryptException
	{
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(encryptKey))
		{
			return null;
		}
		
		// 因为加密key必须为16位
		if(encryptKey.length() > 16)
		{
			encryptKey = encryptKey.substring(0, 16);
		}
		else
		{
			int padLength = 16-encryptKey.length();
			
			for(int i = 0; i < padLength ;i++)
			{
				encryptKey += '0';
			}
		}
		
		try
		{
			IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			byte[] encryptedData = cipher.doFinal(content.getBytes("utf-8"));

			return Base64.encodeBase64String(encryptedData);
		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to encrypt for content: "
					+ content + ", encrypt key: " + encryptKey, e);
		}

	}

	public static String decryptCbcTemp(String content, String decryptKey)
			throws Exception
	{
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(decryptKey))
		{
			return null;
		}
		
		if(decryptKey.length() > 16)
		{
			decryptKey = decryptKey.substring(0, 16);
		}
		else
		{
			int padLength = 16-decryptKey.length();
			for(int i = 0; i < padLength ;i++)
			{
				decryptKey += '0';
			}
		}
		
		try
		{
			IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
			SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte[] decryptedData = cipher.doFinal(Base64.decodeBase64(content));

			return new String(decryptedData, "utf-8");
		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to decrypt for content: "
					+ content + ", decrypt key: " + decryptKey, e);
		}
	}

}
