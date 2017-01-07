package net.logvv.raven.utils.encrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import net.logvv.raven.common.exception.EncryptException;


/**
 * 
 * (DES加密算法)<br>
 * JDK对DES算法的支持
 * 密钥长度：56位 
 * 工作模式：ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128
 * 填充方式：Nopadding/PKCS5Padding/ISO10126Padding/
 * @author fanyaowu
 * @data 2015年2月2日
 * @version 1.0.0
 *
 */
public final class DESUtils
{
	private DESUtils()
	{

	}

	// 加密算法
	private static final String ENCRYPT_TYPE = "DES";

	private static final String DEFAULT_CIPHER_ALGORITHM = "DES/EBC/PKCS5Padding";

	private static final String CBC_ALGORITHM = "DES/CBC/PKCS5Padding";

	// DES默认加密key
	private static final String DES_DEFAULT_KEY = "30.TECH";
	
	private static final byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	/**
	 * 
	 * (根据CBC 加密模式、指定加密key输出对应的BASE64编码加密串)<br>
	 *
	 * @param content
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 * String
	 * @Author fanyaowu
	 * @data 2015年2月2日
	 * @exception 
	 * @version
	 *
	 */
	public static String encryptByCBC(String content, String encryptKey)  throws Exception
	{
		try
		{
			DESKeySpec dks = new DESKeySpec(encryptKey.getBytes());

			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPT_TYPE);
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(CBC_ALGORITHM);

			// 用密匙初始化Cipher对象
			IvParameterSpec param = new IvParameterSpec(iv);
			
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);

			// 执行加密操作
			byte encryptedData[] = cipher.doFinal(content.getBytes());
			
			return Base64.encodeBase64String(encryptedData);
		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to encrypt for content: "
					+ content + ", encrypt key: " + encryptKey, e);
		}
		
	}
	
	/**
	 * 
	 * (根据CBC 加密模式、指定加密key输出对应的BASE64编码解密串)<br>
	 *
	 * @param content
	 * @param decryptKey
	 * @return
	 * @throws Exception
	 * String
	 * @Author fanyaowu
	 * @data 2015年2月2日
	 * @exception 
	 * @version
	 *
	 */
	public static String decryptByCBC(String content, String decryptKey) throws Exception
	{
		try
		{
			DESKeySpec dks = new DESKeySpec(decryptKey.getBytes());

			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPT_TYPE);
			
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// using DES in CBC mode
			Cipher cipher = Cipher.getInstance(CBC_ALGORITHM);

			// 用密匙初始化Cipher对象
			IvParameterSpec param = new IvParameterSpec(iv);
			
			cipher.init(Cipher.DECRYPT_MODE, secretKey, param);

			// 正式执行解密操作
			byte decryptedData[] = cipher.doFinal(Base64.decodeBase64(content));

			return new String(decryptedData);

		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to encrypt for content: "
					+ content + ", encrypt key: " + decryptKey, e);
		}
	}

	public static SecretKey getSecretKey(String secretKey) throws Exception
	{
		// 返回生成指定算法的秘密密钥的 KeyGenerator 对象
		KeyGenerator kg = KeyGenerator.getInstance(ENCRYPT_TYPE);

		// 初始化此密钥生成器，使其具有确定的密钥大小
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(secretKey.getBytes());
		kg.init(56, random);

		// 生成一个密钥
		byte[] key = kg.generateKey().getEncoded();

		// 实例化DES密钥规则
		DESKeySpec dks = new DESKeySpec(key);
		// 实例化密钥工厂
		SecretKeyFactory skf = SecretKeyFactory.getInstance(ENCRYPT_TYPE);
		// 生成密钥
		return skf.generateSecret(dks);
	}

	/**
	 * 
	 * (使用默认key（APP.MAKER）加密内容)<br>
	 *
	 * @param content
	 * @return
	 * @throws EncryptException
	 * String
	 * @Author fanyaowu
	 * @data 2015年2月2日
	 * @exception 
	 * @version
	 *
	 */
	public static String encrypt(String content) throws EncryptException
	{
		return encrypt(content, DES_DEFAULT_KEY);
	}

	/**
	 * 
	 * (使用指定key加密)<br>
	 *
	 * @param content
	 * @param encryptKey
	 * @return
	 * @throws EncryptException
	 * String
	 * @Author fanyaowu
	 * @data 2015年2月2日
	 * @exception 
	 * @version
	 *
	 */
	public static String encrypt(String content, String encryptKey) throws EncryptException
	{
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(encryptKey))
		{
			return null;
		}

		try
		{
			SecretKey securekey = getSecretKey(encryptKey);

			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey);
			byte[] byteContent = content.getBytes("utf-8");
			// 执行加密操作
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
	 * 
	 * (使用默认key解密)<br>
	 *
	 * @param content
	 * @return
	 * @throws EncryptException
	 * String
	 * @Author fanyaowu
	 * @data 2015年2月2日
	 * @exception 
	 * @version
	 *
	 */
	public static String decrypt(String content) throws EncryptException
	{
		return decrypt(content, DES_DEFAULT_KEY);
	}

	/**
	 * 
	 * (使用指定key解密)<br>
	 *
	 * @param content
	 * @param decryptKey
	 * @return
	 * @throws EncryptException
	 * String
	 * @Author fanyaowu
	 * @data 2015年2月2日
	 * @exception 
	 * @version
	 *
	 */
	public static String decrypt(String content, String decryptKey) throws EncryptException
	{
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(decryptKey))
		{
			return null;
		}

		try
		{
			SecretKey securekey = getSecretKey(decryptKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey);

			// 正式执行解密操作
			byte[] result = cipher.doFinal(HexByte.parseHexStr2Byte(content)); // 解密
			return new String(result, "UTF-8");
		}
		catch (Exception e)
		{
			throw new EncryptException("Failed to decrypt for content: "
					+ content + ", decrypt key: " + decryptKey, e);
		}

	}
}
