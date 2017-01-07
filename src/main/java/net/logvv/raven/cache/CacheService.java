package net.logvv.raven.cache;

import net.logvv.raven.common.constant.CacheKeyPrefixConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 
 * CacheService(对缓存的增删改查)
 * 
 * @author lvyongwen
 * @date 2015年9月15日 下午12:58:42
 * @version [1.0, 2015年9月15日]
 * @since version 1.0
 * @see [相关类/方法]
 * 
 * 
 * redisCache1101 
 *  0：位标识工厂序号：工厂主要用于区分 主机、端口、库等
 *  1：位标识模板序号：模板主要用于区分 事务、缓存序列化等
 *  2、3：位标识缓存序号：缓存时间：前缀等
 * 
 * redisCache1101 :缓存限制码 —— 一分钟
 * redisCache1102 :缓存重置密码/注册码 —— 30分钟
 * redisCache1201 :缓存用户信息 —— 永久
 * redisCache1301 :缓存环信信息 —— 60天
 */
@Service
public class CacheService
{
    
    @Resource(name = "commonCacheManager")
    private RedisCacheManager commonCacheManager;
    
    /** cleanValidateLimit(清空验证码限制)  */
    @CacheEvict(value = "redisCache1101", key = "T(CacheKeyPrefixConst).SMS_MIN_LIMIT+#param")
    public void cleanValidateLimit(String param)
    {
        
    }
    
    /** putSendValidateCodeLimit(保存发送验证码的限制) */                                           
	@Cacheable(value = "redisCache1101", key = "T(CacheKeyPrefixConst).SMS_MIN_LIMIT+#phoneNumber")
	public boolean putValidateCodeLimit(String phoneNumber)
	{
	    return true;
	}

	/** cleanRegValidate(清空注册验证码) */
    @CacheEvict(value = "redisCache1102", key = "T(CacheKeyPrefixConst).REGIST_VALIDATE_CODE+#phoneNumber+T(CacheKeyPrefixConst).SPLIT_LINE+#validate")
    public void cleanRegValidate(String phoneNumber, String validate)
    {
        
    }
    
    /** putSendValidateCode(保存发送的验证码) */
	@Cacheable(value = "redisCache1102", key = "T(CacheKeyPrefixConst).REGIST_VALIDATE_CODE+#phoneNumber+T(CacheKeyPrefixConst).SPLIT_LINE+#validateCode")
	public String putSendRegisterValidateCode(String phoneNumber, String validateCode)
	{
	    return validateCode;
	}

	/** cleanPwdValidate(清除重置密码验证码) */
    @CacheEvict(value = "redisCache1102", key = "T(CacheKeyPrefixConst).RESET_VALIDATE_CODE+#phoneNumber+T(CacheKeyPrefixConst).SPLIT_LINE+#validate")
    public void cleanPwdValidate(String phoneNumber, String validate)
    {
        
    }
    
    /** putSendResetPwdValidateCode(发送重置密码的验证码) */
	@Cacheable(value = "redisCache1102", key = "T(CacheKeyPrefixConst).RESET_VALIDATE_CODE+#phoneNumber+T(CacheKeyPrefixConst).SPLIT_LINE+#validateCode")
	public String putSendResetPwdValidateCode(String phoneNumber, String validateCode)
	{
	    return validateCode;
	}

    /** cleanRebindValidate(清除改绑手机号码验证码) */
	@CacheEvict(value = "redisCache1102", key = "T(CacheKeyPrefixConst).REBIND_VALIDATE_CODE+#phoneNumber+T(CacheKeyPrefixConst).SPLIT_LINE+#validate")
	public void cleanRebindValidate(String phoneNumber, String validate)
	{
	    
	}

	/** putSendRebindValidateCode(发送改绑手机号码的验证码) */
	@Cacheable(value = "redisCache1102", key = "T(CacheKeyPrefixConst).REBIND_VALIDATE_CODE+#phoneNumber+T(CacheKeyPrefixConst).SPLIT_LINE+#validateCode")
	public String putSendRebindValidateCode(String phoneNumber, String validateCode)
	{
	    return validateCode;
	}

	/** cleanLoginValidate(临时验证码登录) */
    @CacheEvict(value = "redisCache1102", key = "T(CacheKeyPrefixConst).LOGIN_VALIDATE_CODE+#phoneNumber+T(CacheKeyPrefixConst).SPLIT_LINE+#validate")
    public void cleanLoginValidate(String phoneNumber, String validate)
    {
        
    }
    
    @Cacheable(value = "redisCache1102", key = "T(CacheKeyPrefixConst).LOGIN_VALIDATE_CODE+#phoneNumber+T(CacheKeyPrefixConst).SPLIT_LINE+#validateCode")
    public String putSendLoginCode(String phoneNumber, String validateCode)
    {
        return validateCode;
    }
    
    /** TODO 没有实现 judgeSendValidateCodeBeyoud(判断发送验证码是否在一分钟之外) */
	public boolean judgeSendValidateCodeLimit(String phoneNumber)
	{
	    Cache redisCache1101 = commonCacheManager.getCache("redisCache1101");
	    Boolean limit = redisCache1101.get(CacheKeyPrefixConst.SMS_MIN_LIMIT + phoneNumber, Boolean.class);
	    return limit == null ? false : limit;
	}

	/** checkRegisterCode(判断验证码是否有效) */
    public boolean checkRegisterCode(String phoneNumber, String validateCode)
    {
        Cache redisCache1102 = commonCacheManager.getCache("redisCache1102");
        return StringUtils.isEmpty(redisCache1102.get(CacheKeyPrefixConst.REGIST_VALIDATE_CODE + phoneNumber
            + CacheKeyPrefixConst.SPLIT_LINE + validateCode, String.class)) ? false : true;
        
    }
    
    /** checkResetPwdCode(判断重置密码验证码是否正确) */
    public boolean checkResetPwdCode(String phoneNumber, String validateCode)
    {
        Cache redisCache1102 = commonCacheManager.getCache("redisCache1102");
        return StringUtils.isEmpty(redisCache1102.get(CacheKeyPrefixConst.RESET_VALIDATE_CODE + phoneNumber
            + CacheKeyPrefixConst.SPLIT_LINE + validateCode, String.class)) ? false : true;
    }
    
    /** 改绑手机号吗 */
    public boolean checkRebindPhoneCode(String phoneNumber, String validateCode)
    {
        Cache redisCache1102 = commonCacheManager.getCache("redisCache1102");
        return StringUtils.isEmpty(redisCache1102.get(CacheKeyPrefixConst.REBIND_VALIDATE_CODE + phoneNumber
            + CacheKeyPrefixConst.SPLIT_LINE + validateCode, String.class)) ? false : true;
    }

}
